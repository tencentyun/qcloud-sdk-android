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
import com.tencent.qcloud.core.http.QCloudHttpRequest;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
                try {
                    QCloudHttpRequest qCloudHttpRequest = task.retrySignRequest();
                    request = request.newBuilder()
                            .header(HttpConstants.Header.COS_SDK_RETRY, String.valueOf(true))
                            .header(HttpConstants.Header.AUTHORIZATION, qCloudHttpRequest.header(HttpConstants.Header.AUTHORIZATION))
                            .build();
                } catch (Exception ex) {
                }
            }

            COSLogger.iNetwork(HTTP_LOG_TAG, "%s start to execute, attempts is %d, hasSwitchedDomain is %b", request, attempts, task.hasSwitchedDomain());

            //记录重试次数
            HttpTaskMetrics metrics = task.metrics();
            if (metrics != null) {
                metrics.setRetryCount(attempts);
            }

            attempts++;
            int statusCode = -1;
            try {
                //解决okhttp 3.14 以上版本报错 cannot make a new request because the previous response is still open: please call response.close()
                if (response != null && response.body() != null) {
                    response.close();
                }
                response = executeTaskOnce(chain, request, task);
                statusCode = response.code();
                e = null;
            } catch (DomainSwitchException exception) {
                // 捕获域名切换异常（来自RedirectInterceptor的3xx处理）
                if (!task.hasSwitchedDomain()) {
                    // 还未切换过域名，抛出异常让HttpTask切换域名
                    COSLogger.iNetwork(HTTP_LOG_TAG, "%s 3xx from RedirectInterceptor, switch domain, attempts is %d", request, attempts);
                    throw exception;
                } else {
                    // 已经切换过域名，不应该再收到DomainSwitchException，转换为普通IOException
                    COSLogger.iNetwork(HTTP_LOG_TAG, "%s 3xx after domain switch, should not happen, attempts is %d", request, attempts);
                    e = new IOException(exception);
                }
            } catch (IOException exception) {
                e = exception;
                if(e.getCause() instanceof QCloudServiceException) {
                    statusCode = ((QCloudServiceException) e.getCause()).getStatusCode();
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

            // 判断是否满足域名切换条件
            boolean meetDomainSwitchCondition = checkDomainSwitchCondition(task, request, response, statusCode);

            // 2xx：成功，不重试
            if (e == null && statusCode >= 200 && statusCode < 300) {
                if (serverDate != null) {
                    HttpConfiguration.calculateGlobalTimeOffset(serverDate, new Date(), MIN_CLOCK_SKEWED_OFFSET);
                }
                // access success
                increaseHostReliable(request.url().host());
                retryStrategy.onTaskEnd(true, null);
                break;
            }

            // 时钟偏移错误特殊处理
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
            }

            // 3xx：已经在RedirectInterceptor中处理，重定向已完成，不需要重试
            if (statusCode >= 300 && statusCode < 400) {
                COSLogger.iNetwork(HTTP_LOG_TAG, "%s 3xx ends, attempts is %d, code is %d", request, attempts, statusCode);
                // access success
                increaseHostReliable(request.url().host());
                retryStrategy.onTaskEnd(true, null);
                break;
            }

            // 4xx：不重试
            if (statusCode >= 400 && statusCode < 500) {
                COSLogger.iNetwork(HTTP_LOG_TAG, "%s 4xx ends without retry, code is %d", request, statusCode);
                break;
            }

            // 5xx 或 未收到回包：按RetryStrategy重试，最后一次重试时切换域名
            boolean is5xxOrNoResponse = (statusCode >= 500 && statusCode < 600) || e != null;
            if (is5xxOrNoResponse) {
                // 判断是否应该继续重试
                boolean shouldContinueRetry = shouldRetry(request, response, attempts, task.getWeight(), startTime, e, statusCode) && !task.isCanceled();
                
                if (shouldContinueRetry) {
                    // 继续重试（使用原域名）
                    COSLogger.iNetwork(HTTP_LOG_TAG, "%s 5xx/error retry with original domain, attempts is %d, error is %s, code is %d", 
                            request, attempts, e, statusCode);
                    retryStrategy.onTaskEnd(false, e);
                    continue;
                } else {
                    // 这是最后一次机会，判断是否需要切换域名
                    if (meetDomainSwitchCondition && !task.hasSwitchedDomain()) {
                        COSLogger.iNetwork(HTTP_LOG_TAG, "%s last retry with domain switch, attempts is %d", request, attempts);
                        throw new DomainSwitchException();
                    } else {
                        // 不满足切换条件或已切换过，结束重试
                        COSLogger.iNetwork(HTTP_LOG_TAG, "%s ends after retries, attempts is %d, code is %d", request, attempts, statusCode);
                        break;
                    }
                }
            }

            // 其他情况，结束重试
            COSLogger.iNetwork(HTTP_LOG_TAG, "%s ends, code is %d", request, statusCode);
            break;
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
                        request = buildNewRangeRequest(request, transferBodySize, task);
                    }
                }
                return processSingleRequest(chain, request);
            }
        } catch (DomainSwitchException exception) {
            // 域名切换异常直接向上抛出
            throw exception;
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

        // 判断是否是5xx状态码（覆盖所有5xx）
        return statusCode >= 500 && statusCode < 600;
    }

    /**
     * 检查是否满足域名切换条件
     * 条件：域名匹配myqcloud.com & 响应不含requestid & 开启域名切换开关 & 签名是SDK自己生成
     */
    private boolean checkDomainSwitchCondition(HttpTask task, Request request, Response response, int statusCode) {
        if (task == null || !task.isDomainSwitch() || task.isSelfSigner()) {
            return false;
        }
        
        if (!DomainSwitchUtils.isMyqcloudUrl(request.url().host())) {
            return false;
        }
        
        // 未收到响应
        if (response == null) {
            return true;
        }
        
        // 收到响应但没有 x-cos-request-id和 x-ci-request-id
        return TextUtils.isEmpty(response.header("x-cos-request-id")) && TextUtils.isEmpty(response.header("x-ci-request-id"));
    }

    /**
     * 判断异常是否可恢复（是否值得重试）
     * 用于判断"未收到回包"的场景（reset、超时等网络异常）
     */
    private boolean isRecoverable(IOException e) {
        // 协议异常不可恢复
        if (e instanceof ProtocolException) {
            return false;
        }

        // 中断异常：只有超时异常可恢复
        if (e instanceof InterruptedIOException) {
            return e instanceof SocketTimeoutException;
        }

        if(e.getCause() instanceof QCloudServiceException) {
            QCloudServiceException qCloudServiceException = (QCloudServiceException) e.getCause();
            return qCloudServiceException.getStatusCode() >= 500 && qCloudServiceException.getStatusCode() < 600;
        }

        // 其他网络异常都值得重试（如连接失败、连接重置、SSL异常等）
        // 切换域名后可能成功
        return true;
    }

    /**
     * 生成新的Range请求
     * 如果是下载请求 重试时需要更新请求Range 防止重试后之前的下载进度丢失
     * @param request 原来的请求
     * @param transferBodySize 已传输的内容长度
     * @return 新的Range请求
     */
    private Request buildNewRangeRequest(Request request, long transferBodySize, HttpTask task){
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
        String newRange = String.format("bytes=%s-%s", start, (end == -1 ?"": String.valueOf(end)));
        try {
            QCloudHttpRequest qCloudHttpRequest = task.newRangeSignRequest(newRange);
            headerBuilder.set(RANGE, newRange);
            headerBuilder.set(HttpConstants.Header.AUTHORIZATION, qCloudHttpRequest.header(HttpConstants.Header.AUTHORIZATION));
        } catch (Exception ex) {
        }
        requestBuilder.headers(headerBuilder.build());
        return requestBuilder.build();
    }
}
