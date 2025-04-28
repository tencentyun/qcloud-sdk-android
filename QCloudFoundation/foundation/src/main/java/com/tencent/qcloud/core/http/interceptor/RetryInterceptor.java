/*
 * Copyright (c) 2010-2020 Tencent Cloud. All rights reserved.
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */

package com.tencent.qcloud.core.http.interceptor;


import static com.tencent.qcloud.core.http.HttpConstants.Header.RANGE;
import static com.tencent.qcloud.core.http.QCloudHttpClient.HTTP_LOG_TAG;

import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.tencent.qcloud.core.common.DomainSwitchException;
import com.tencent.qcloud.core.common.QCloudServiceException;
import com.tencent.qcloud.core.http.HttpConfiguration;
import com.tencent.qcloud.core.http.HttpConstants;
import com.tencent.qcloud.core.http.HttpTask;
import com.tencent.qcloud.core.http.HttpTaskMetrics;
import com.tencent.qcloud.core.http.QCloudHttpRetryHandler;
import com.tencent.qcloud.core.logger.COSLogger;
import com.tencent.qcloud.core.task.RetryStrategy;
import com.tencent.qcloud.core.task.TaskManager;
import com.tencent.qcloud.core.util.DomainSwitchUtils;

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

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

public class RetryInterceptor implements Interceptor {
    private RetryStrategy retryStrategy;
    private RetryStrategy.WeightAndReliableAddition additionComputer = new RetryStrategy.WeightAndReliableAddition();

    private volatile static Map<String, HostReliable> hostReliables = new HashMap<>();

    private static final int MIN_CLOCK_SKEWED_OFFSET = 600;

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

    Response processRequest(Chain chain, Request request, @Nullable HttpTask task) throws IOException {
        Response response = null;
        IOException e;

        if (task == null || task.isCanceled()) {
            throw new IOException("CANCELED");
        }

        if(task.isDomainSwitch() && !task.isSelfSigner() && DomainSwitchUtils.isMyqcloudUrl(request.url().host())){
            try {
                response = executeTaskOnce(chain, request, task);
                // 判断响应 状态码非2XX，且没有x-cos-request-id头部
                if (!response.isSuccessful() && TextUtils.isEmpty(response.header("x-cos-request-id"))) {
                    throw new DomainSwitchException();
                }
            } catch (Exception exception){
                // 下载convertResponse可能会产生服务端异常，这时候不用切
                if(exception.getCause() instanceof QCloudServiceException &&
                        !TextUtils.isEmpty(((QCloudServiceException) exception.getCause()).getRequestId())){
                } else {
                    // 没有收到响应
                    throw new DomainSwitchException();
                }
            }
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

            // 属于重试
            if(attempts > 0){
                // 添加重试header
                request = request.newBuilder()
                        .header(HttpConstants.Header.COS_SDK_RETRY, String.valueOf(true))
                        .build();
            }

            COSLogger.iNetwork(HTTP_LOG_TAG, "%s start to execute, attempts is %d", request, attempts);

            //记录重试次数
            HttpTaskMetrics metrics = task.metrics();
            if (metrics != null) {
                metrics.setRetryCount(attempts);
            }

            attempts++;
            int statusCode = -1;
            try {
                if(attempts == 1 && response != null){
                    // 第一次执行 且response已经有值了，说明尝试成功，则不再重复执行
                } else {
                    //解决okhttp 3.14 以上版本报错 cannot make a new request because the previous response is still open: please call response.close()
                    if (response != null && response.body() != null) {
                        response.close();
                    }
                    response = executeTaskOnce(chain, request, task);
                }
                statusCode = response.code();
                e = null;
            } catch (IOException exception) {
                if(exception instanceof DomainSwitchException){
                    throw exception;
                } else {
                    e = exception;
                }
            } catch (IllegalStateException exception){
                // 再次处理 okhttp 3.14 以上版本报错 cannot make a new request because the previous response is still open: please call response.close()
                if(exception.getMessage().startsWith("cannot make a new request because the previous response is still open: please call response.close()")){
                    if (response != null && response.body() != null) {
                        response.close();
                    }
                    // response.close()后暂停3s
                    try {
                        TimeUnit.MILLISECONDS.sleep(3000);
                    } catch (InterruptedException ex) {
                    }
                    if (response != null && response.body() != null) {
                        response.close();
                    }
                    response = executeTaskOnce(chain, request, task);
                    statusCode = response.code();
                    e = null;
                } else {
                    throw exception;
                }
            }
            // server date header
            String serverDate = null;
            if(response != null){
                // 增加cos服务端响应校验 防止cdn等其他服务端响应的date不准确
                String server = response.header(HttpConstants.Header.SERVER);
                if(HttpConstants.TENCENT_COS_SERVER.equals(server)){
                    serverDate = response.header(HttpConstants.Header.DATE);
                    COSLogger.iNetwork(HTTP_LOG_TAG, "serverDate is %s", serverDate);
                }
            }

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
                COSLogger.iNetwork(HTTP_LOG_TAG, "%s failed for %s", request, clockSkewError);
                long minTimeOffsetDeltaInMill = 2; // 2s 内的校准偏移不会重试
                if (serverDate != null && HttpConfiguration
                        .calculateGlobalTimeOffset(serverDate, new Date()) > minTimeOffsetDeltaInMill) {
                    // stop here, re sign request and try again
                    e = new IOException(new QCloudServiceException("client clock skewed").setErrorCode(clockSkewError));
                }
                break;
            } else if (shouldRetry(request, response, attempts, task.getWeight(), startTime, e, statusCode) && !task.isCanceled()) {
                COSLogger.iNetwork(HTTP_LOG_TAG, "%s failed for %s, code is %d", request, e, statusCode);
                retryStrategy.onTaskEnd(false, e);
            } else {
                COSLogger.iNetwork(HTTP_LOG_TAG, "%s ends for %s, code is %d", request, e, statusCode);
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
                //如果是文件路径下载请求 重试时需要更新请求Range 防止重试后之前的下载进度丢失
                if(task.isResponseFilePathConverter()){
                    long transferBodySize = task.getTransferBodySize();
                    if(transferBodySize > 0){
                        request = buildNewRangeRequest(request, transferBodySize);
                    }
                }
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
                exception.getMessage().toLowerCase(Locale.ROOT).equals("canceled");
    }

    Response processSingleRequest(Chain chain, Request request) throws IOException {
        return chain.proceed(request);
    }

    String getClockSkewError(Response response, int statusCode) {
        if (response != null && statusCode == HttpURLConnection.HTTP_FORBIDDEN) {
            if(response.request().method().toUpperCase(Locale.ROOT).equals("HEAD")) return QCloudServiceException.ERR0R_REQUEST_IS_EXPIRED;
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
        COSLogger.iNetwork(HTTP_LOG_TAG, String.format(Locale.ENGLISH, "attempts = %d, weight = %d, reliable = %d, addition = %d",
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

    /**
     * 生成新的Range请求
     * 如果是下载请求 重试时需要更新请求Range 防止重试后之前的下载进度丢失
     * @param request 原来的请求
     * @param transferBodySize 已传输的内容长度
     * @return 新的Range请求
     */
    private Request buildNewRangeRequest(Request request, long transferBodySize){
        long start = -1;
        long end = -1;
        String range = request.header(RANGE);
        if(range != null){
            range = range.replace("bytes=","");
            String[] start_end = range.split("-");
            if(start_end.length > 0){
                try {
                    start = Long.parseLong(start_end[0]);
                } catch (NumberFormatException e){
                    e.printStackTrace();
                }
            }
            if(start_end.length > 1){
                try {
                    end = Long.parseLong(start_end[1]);
                } catch (NumberFormatException e){
                    e.printStackTrace();
                }
            }
        }
        if(start != -1){
            start += transferBodySize;
        } else {
            start = transferBodySize;
        }
        Request.Builder requestBuilder = request.newBuilder();
        Headers.Builder headerBuilder = request.headers().newBuilder();
        headerBuilder.set(RANGE, String.format("bytes=%s-%s", start, (end == -1 ?"": String.valueOf(end))));
        requestBuilder.headers(headerBuilder.build());
        return requestBuilder.build();
    }
}
