package com.tencent.qcloud.core.http.interceptor;


import com.tencent.qcloud.core.common.QCloudClientException;
import com.tencent.qcloud.core.common.QCloudServiceException;
import com.tencent.qcloud.core.http.HttpConfiguration;
import com.tencent.qcloud.core.http.HttpConstants;
import com.tencent.qcloud.core.http.HttpTask;
import com.tencent.qcloud.core.http.QCloudHttpRetryHandler;
import com.tencent.qcloud.core.logger.QCloudLogger;
import com.tencent.qcloud.core.task.RetryStrategy;
import com.tencent.qcloud.core.task.TaskManager;
import com.tencent.qcloud.core.util.QCloudUtils;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.nio.charset.Charset;
import java.security.cert.CertificateException;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLPeerUnverifiedException;

import okhttp3.Interceptor;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

import static com.tencent.qcloud.core.http.QCloudHttpClient.HTTP_LOG_TAG;

/**
 * Copyright 2010-2017 Tencent Cloud. All Rights Reserved.
 */

public class RetryInterceptor implements Interceptor {

    private RetryStrategy retryStrategy;
    private RetryStrategy.WeightAndReliableAddition additionComputer = new RetryStrategy.WeightAndReliableAddition();

    private volatile static Map<String, HostReliable> hostReliables = new HashMap<>();

    private static final int MIN_CLOCK_SKEWED_OFFSET = 600;
    private static final int NETWORK_DETECT_RETRY_DELAY = 3000; // ms

    // 线程安全
    private static class HostReliable {

        private final int maxReliable = 4;
        private final int minReliable = 0;
        private static final int defaultReliable = 2;
        private final long resetPeriod = 1000 * 60 * 5;

        private final String host;
        private int reliable;

        private HostReliable(String host) {

            this.host = host;
            reliable = defaultReliable;
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {

                }
            };
            new Timer(host + "reliable").schedule(timerTask, resetPeriod, resetPeriod);
        }

        synchronized private void increaseReliable() {

            if (reliable < maxReliable) {
                reliable += 1;
            }
        }

        synchronized private void decreaseReliable() {

            if (reliable > minReliable) {
                reliable -= 1;
            }
        }

        synchronized private int getReliable() {
            return reliable;
        }

        synchronized private void zeroReliable() {
            reliable = 0;
        }

        synchronized private void resetReliable() {
            reliable = defaultReliable;
        }
    }

    public RetryInterceptor(RetryStrategy retryStrategy) {
        this.retryStrategy = retryStrategy;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        HttpTask task = (HttpTask) TaskManager.getInstance().get((String) request.tag());
        return processRequest(chain, request, task);
    }

    Response processRequest(Chain chain, Request request, HttpTask task) throws IOException {
        Response response = null;
        IOException e;

        if (task == null || task.isCanceled()) {
            throw new IOException("CANCELED");
        }

        int attempts = 0;
        long startTime = System.nanoTime();

        while (true) {
            // wait for attempt
            long delay = retryStrategy.getNextDelay(attempts);
            if (delay > 0) {
                try {
                    TimeUnit.MILLISECONDS.sleep(delay);
                } catch (InterruptedException ex) {
                }
            }

            // avoid useless retry
            if (!QCloudUtils.isNetworkConnected()) {
                try {
                    TimeUnit.MILLISECONDS.sleep(NETWORK_DETECT_RETRY_DELAY);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }

                if (!QCloudUtils.isNetworkConnected()) {
                    e = new IOException(new QCloudClientException("NetworkNotConnected"));
                    break;
                }
            }
            QCloudLogger.i(HTTP_LOG_TAG, "%s start to execute, attempts is %d", request, attempts);

            attempts++;
            int statusCode = -1;
            try {
                response = executeTaskOnce(chain, request, task);
                statusCode = response.code();
                e = null;
            } catch (IOException exception) {
                e = exception;
            }
            // server date header
            String serverDate = response != null ? response.header(HttpConstants.Header.DATE) : null;

            if ((e == null && response.isSuccessful())) {
                if (serverDate != null) {
                    HttpConfiguration.calculateGlobalTimeOffset(serverDate, new Date(), MIN_CLOCK_SKEWED_OFFSET);
                }
                // access success
                increaseHostReliable(request.url().host());
                retryStrategy.onTaskEnd(true, null);
                break;
            }

            String clockSkewError = getClockSkewError(response, statusCode);
            if (clockSkewError != null) {
                QCloudLogger.i(HTTP_LOG_TAG, "%s failed for %s", request, clockSkewError);
                long minTimeOffsetDeltaInMill = 2; // 2s 内的校准偏移不会重试
                if (serverDate != null && HttpConfiguration
                        .calculateGlobalTimeOffset(serverDate, new Date()) > minTimeOffsetDeltaInMill) {
                    // stop here, re sign request and try again
                    e = new IOException(new QCloudServiceException("client clock skewed").setErrorCode(clockSkewError));
                }
                break;
            } else if (shouldRetry(request, response, attempts, task.getWeight(), startTime, e, statusCode) && !task.isCanceled()) {
                QCloudLogger.i(HTTP_LOG_TAG, "%s failed for %s, code is %d", request, e, statusCode);
                retryStrategy.onTaskEnd(false, e);
            } else {
                QCloudLogger.i(HTTP_LOG_TAG, "%s ends for %s, code is %d", request, e, statusCode);
                break;
            }
        }
        if (e != null) {
            // access failed
            decreaseHostAccess(request.url().host());
            retryStrategy.onTaskEnd(false, e);
            throw e;
        }
        return response;
    }

    private Response executeTaskOnce(Chain chain, Request request, HttpTask task) throws IOException {
        try {
            if (task.isCanceled()) {
                throw new IOException("CANCELED");
            } else {
                return processSingleRequest(chain, request);
            }
        } catch (ProtocolException exception) {
            // OkHttp在Http code为204时，不允许body不为空，这里为了阻止抛出异常，对response进行修改
            if (exception.getMessage() != null && exception.getMessage().contains(
                    "HTTP " + 204 + " had non-zero Content-Length: ")) {
                return new Response.Builder()
                        .request(request)
                        .message(exception.toString())
                        .code(204)
                        .protocol(Protocol.HTTP_1_1)
                        .build();
            } else {
                exception.printStackTrace();
                throw exception;
            }

        } catch (IOException exception) {
            exception.printStackTrace();
            throw exception;
        }
    }

    private boolean isUserCancelled(IOException exception) {
        return exception != null && exception.getMessage() != null &&
                exception.getMessage().toLowerCase().equals("canceled");
    }

    Response processSingleRequest(Chain chain, Request request) throws IOException {
        return chain.proceed(request);
    }

    String getClockSkewError(Response response, int statusCode) {
        if (response != null && statusCode == HttpURLConnection.HTTP_FORBIDDEN) {
            if(response.request().method().toUpperCase().equals("HEAD")) return QCloudServiceException.ERR0R_REQUEST_IS_EXPIRED;
            ResponseBody body = response.body();
            if (body != null) {
                try {
                    BufferedSource source = body.source();
                    source.request(Long.MAX_VALUE);
                    Buffer buffer = source.buffer();
                    String bodyString = buffer.clone().readString(Charset.forName("UTF-8"));
                    Pattern patternCode = Pattern.compile("<Code>(RequestTimeTooSkewed|AccessDenied)</Code>");
                    Pattern patternMessage = Pattern.compile("<Message>Request has expired</Message>");
                    Matcher matcherCode = patternCode.matcher(bodyString);
                    Matcher matcherMessage = patternMessage.matcher(bodyString);
                    if (matcherCode.find()) {
                        String code = matcherCode.group(1);
                        if ("RequestTimeTooSkewed".equals(code)) {
                            return QCloudServiceException.ERR0R_REQUEST_TIME_TOO_SKEWED;
                        } else if ("AccessDenied".equals(code) && matcherMessage.find()) {
                            return QCloudServiceException.ERR0R_REQUEST_IS_EXPIRED;
                        }
                    }
                } catch (IOException e) {
                    //ignore
                }
            }

        }

        return null;
    }


    // @UnThreadSafe
    private void increaseHostReliable(String host) {

        HostReliable hostReliable = hostReliables.get(host);

        if (hostReliable != null) {
            hostReliable.increaseReliable();
        } else {
            hostReliables.put(host, new HostReliable(host));
        }
    }

    // @UnThreadSafe
    private void decreaseHostAccess(String host) {

        HostReliable hostReliable = hostReliables.get(host);
        if (hostReliable != null) {
            hostReliable.decreaseReliable();
        } else {
            hostReliables.put(host, new HostReliable(host));
        }
    }

    // @UnThreadSafe
    private int getHostReliable(String host) {

        HostReliable hostReliable = hostReliables.get(host);
        if (hostReliable != null) {
            return hostReliable.getReliable();
        } else {
            return HostReliable.defaultReliable;
        }
    }


    private boolean shouldRetry(Request request, Response response, int attempts, int weight, long startTime, IOException e, int statusCode) {
        if (isUserCancelled(e)) {
            return false;
        }

        int reliable = getHostReliable(request.url().host());
        int retryAddition = additionComputer.getRetryAddition(weight, reliable);
        QCloudLogger.i(HTTP_LOG_TAG, String.format(Locale.ENGLISH, "attempts = %d, weight = %d, reliable = %d, addition = %d",
                attempts, weight, reliable, retryAddition));

        if (!retryStrategy.shouldRetry(attempts, System.nanoTime() - startTime, retryAddition)) {
            return false;
        }

        QCloudHttpRetryHandler qCloudHttpRetryHandler = retryStrategy.getQCloudHttpRetryHandler();
        if(!qCloudHttpRetryHandler.shouldRetry(request, response, e)){
            return false;
        }

        if (e != null && isRecoverable(e)) {
            return true;
        }

        return statusCode == HttpURLConnection.HTTP_INTERNAL_ERROR ||
                statusCode == HttpURLConnection.HTTP_BAD_GATEWAY ||
                statusCode == HttpURLConnection.HTTP_UNAVAILABLE ||
                statusCode == HttpURLConnection.HTTP_GATEWAY_TIMEOUT;
    }

    private boolean isRecoverable(IOException e) {
        // If there was a protocol problem, don't recover.
        if (e instanceof ProtocolException) {
            return false;
        }

        // If there was an interruption don't recover, but if there was a timeout connecting to a route
        // we should try the next route (if there is one).
        if (e instanceof InterruptedIOException) {
            return e instanceof SocketTimeoutException;
        }

        // Look for known client-side or negotiation errors that are unlikely to be fixed by trying
        // again with a different route.
        if (e instanceof SSLHandshakeException) {
            // If the problem was a CertificateException from the X509TrustManager,
            // do not retry.
            if (e.getCause() instanceof CertificateException) {
                return false;
            }
        }
        if (e instanceof SSLPeerUnverifiedException) {
            // e.g. a certificate pinning error.
            return false;
        }

        // An example of one we might want to retry with a different route is a problem connecting to a
        // proxy and would manifest as a standard IOException. Unless it is one we know we should not
        // retry, we return true and try a new route.
        return true;
    }
}
