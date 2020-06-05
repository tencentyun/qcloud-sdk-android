package com.tencent.cos.xml;


import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.tencent.cos.xml.common.VersionInfo;
import com.tencent.qcloud.core.http.QCloudHttpRetryHandler;
import com.tencent.qcloud.core.task.RetryStrategy;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

/**
 * Client configuration options such as timeout settings, protocol string, max
 * retry attempts, etc.
 */

public class CosXmlServiceConfig implements Parcelable {

    /**
     * The default protocol to use when connecting to cos Services.
     */
    public static final String HTTP_PROTOCOL = "http";
    public static final String HTTPS_PROTOCOL = "https";

    public static final String ACCELERATE_ENDPOINT_SUFFIX = "cos.accelerate";

    public static final String DEFAULT_HOST_FORMAT = "${bucket}.cos.${region}.myqcloud.com";
    public static final String ACCELERATE_HOST_FORMAT = "${bucket}.cos.accelerate.myqcloud.com";
    public static final String PATH_STYLE_HOST_FORMAT = "cos.${region}.myqcloud.com";

    /**
     * The default user agent header for cos android sdk clients.
     */
    public static final String DEFAULT_USER_AGENT = VersionInfo.getUserAgent();

    private String protocol;
    private String userAgent;

    private String region;
    private String appid;

    private String host;
    private int port;
    private String endpointSuffix;

    private boolean isDebuggable;

    private RetryStrategy retryStrategy;
    private QCloudHttpRetryHandler qCloudHttpRetryHandler;

    private int connectionTimeout;
    private int socketTimeout;

    private Executor executor;

    private boolean isQuic;

    private List<String> prefetchHosts;

    private Map<String, List<String>> commonHeaders;

    private List<String> noSignHeaders;

    private boolean dnsCache;

    private String hostFormat = DEFAULT_HOST_FORMAT;

    private boolean bucketInPath; // path style

    private boolean accelerate; //

    public CosXmlServiceConfig(Builder builder) {
        this.protocol = builder.protocol;
        this.userAgent = builder.userAgent;
        this.isDebuggable = builder.isDebuggable;

        this.appid = builder.appid;
        this.region = builder.region;
        this.host = builder.host;
        this.port = builder.port;
        this.endpointSuffix = builder.endpointSuffix;
        this.bucketInPath = builder.bucketInPath;
        this.commonHeaders = builder.commonHeaders;
        this.noSignHeaders = builder.noSignHeaders;

        if (TextUtils.isEmpty(hostFormat) && TextUtils.isEmpty(region) &&
                TextUtils.isEmpty(host)) {
            throw new IllegalArgumentException("please set host or endpointSuffix or region !");
        }

        this.retryStrategy = builder.retryStrategy;
        this.qCloudHttpRetryHandler = builder.qCloudHttpRetryHandler;
        this.socketTimeout = builder.socketTimeout;
        this.connectionTimeout = builder.connectionTimeout;
        this.hostFormat = builder.hostFormat;

        this.executor = builder.executor;
        this.isQuic = builder.isQuic;
        this.accelerate = builder.accelerate;

        this.dnsCache = builder.dnsCache;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public String getRegion() {
        return region;
    }

    public String getBucket(String bucket) {
        return getBucket(bucket, appid);
    }

    public String getBucket(String bucket, String appid) {
        String myBucket = bucket;
        if (bucket != null && !bucket.endsWith("-" + appid) && !TextUtils.isEmpty(appid)){
            myBucket = bucket + "-" + appid;
        }
        return myBucket;
    }

    public List<String> getNoSignHeaders() {
        return noSignHeaders;
    }

    public String getAppid() {
        return appid;
    }


    /**
     *
     * @return
     */
    public String getRequestHost(String bucket, boolean accelerate) {
       return getRequestHost(null, bucket, accelerate);
    }

    /**
     *
     * @return
     */
    public String getRequestHost(String region, String bucket, boolean accelerate) {

        if (!TextUtils.isEmpty(host)) {
            return host;
        }
        region = TextUtils.isEmpty(region) ? this.region : region; // 优先 request 中的 region
        bucket = getBucket(bucket, appid); // 获取最终的 bucket 名称，这里兼容之前 bucket 没带 appid 的情况
        accelerate = accelerate || this.accelerate;  //  只要请求或者 config 中使能了加速域名，则使能加速域名
        boolean pathStyle = bucketInPath; // Path style 暂时只能在 config 中设置

        return getFormatHost(getHostFormat(accelerate, pathStyle), region, bucket);
    }

    public String getDefaultRequestHost(String region, String bucket, String appid) {

        bucket = getBucket(bucket, appid);
        return getFormatHost(DEFAULT_HOST_FORMAT, region, bucket);
    }

    private String getFormatHost(String hostFormat, String region, String bucket) {
        return hostFormat.replace("${bucket}", bucket).replace("${region}", region);
    }

    private String getHostFormat(boolean accelerate, boolean pathStyle) {

        if (!TextUtils.isEmpty(hostFormat)) {
            return hostFormat;
        }

        String hostFormat = DEFAULT_HOST_FORMAT;

        if (accelerate) {
            hostFormat = ACCELERATE_HOST_FORMAT;
        } else if (pathStyle) {
            hostFormat = PATH_STYLE_HOST_FORMAT;
        }

        // endpointSuffix 兼容代码
        // 最终的 host
        // 1. path style 下，host = endpointSuffix
        // 2. accelerate 下，将 cos.region 用 cos.accelerate 替换
        if (endpointSuffix != null) {

            hostFormat = bucketInPath ? endpointSuffix : "${bucket}.".concat(endpointSuffix);
            if (accelerate) {
                hostFormat = hostFormat.replace("cos.${region}", "cos.accelerate");
            }
        }

        return hostFormat;
    }

    public int getPort(){
        return port;
    }

    @Deprecated
    public String getHost(String bucket,
                          boolean isSupportAccelerate) {
        return getHost(bucket, null, isSupportAccelerate);
    }

    @Deprecated
    public String getHost(String bucket, String region,
                          boolean isSupportAccelerate) {
        return getHost(bucket, region, appid, isSupportAccelerate);
    }

    @Deprecated
    public String getHost(String bucket, String region,
                          boolean isSupportAccelerate, boolean isHeader) {
        return getHost(bucket, region, appid, isSupportAccelerate, isHeader);
    }

    @Deprecated
    public String getHost(String bucket, String region,
                          String appId, boolean isSupportAccelerate, boolean isHeader){

        if (!isHeader && !TextUtils.isEmpty(host)) {
            return host;
        }

        String myBucket = getBucket(bucket, appId);

        String hostBuilder = "";
        if (!bucketInPath) {
            hostBuilder += myBucket + ".";
        }
        hostBuilder += getEndpointSuffix(region, isSupportAccelerate);
        return hostBuilder;
    }

    @Deprecated
    public String getHost(String bucket, String region,
                          String appId, boolean isSupportAccelerate) {
        return getHost(bucket, region, appId, isSupportAccelerate, false);
    }

    public boolean isDnsCache() {
        return dnsCache;
    }

    public Map<String, List<String>> getCommonHeaders() {
        return commonHeaders;
    }

    @Deprecated
    public String getEndpointSuffix() {
        return getEndpointSuffix(region, false);
    }

    @Deprecated
    public String getEndpointSuffix(String region,
                                    boolean isSupportAccelerate) {
        String myRegion = TextUtils.isEmpty(region) ? getRegion() : region;
        String myEndpointSuffix = endpointSuffix;

        // 默认是 cos.region.myqcloud.com
        if (endpointSuffix == null && myRegion != null) {
            myEndpointSuffix = "cos." + myRegion + ".myqcloud.com";
        }

        // endpointSuffix 替换 region
        myEndpointSuffix = substituteEndpointSuffix(myEndpointSuffix, myRegion);

        // 全球加速 suffix 将 cos.region 替换为 accelerate
        if (myEndpointSuffix != null && isSupportAccelerate) {
            myEndpointSuffix = myEndpointSuffix.replace("cos." + myRegion,
                    ACCELERATE_ENDPOINT_SUFFIX);
        }
        return myEndpointSuffix;
    }

    private String substituteEndpointSuffix(String formatString,
                                            String region) {
        if (!TextUtils.isEmpty(formatString) && region != null) {
            return formatString
                    .replace("${region}", region);
        }
        return formatString;
    }



    public String getUrlPath(String bucket, String cosPath) {
        StringBuilder path = new StringBuilder();

        if (bucketInPath) {
            String myBucket = bucket;
            if (!bucket.endsWith("-" + appid) && !TextUtils.isEmpty(appid)){
                myBucket = bucket + "-" + appid;
            }
            path.append("/").append(myBucket);
        }

        if(cosPath != null && !cosPath.startsWith("/")){
            path.append("/").append(cosPath);
        } else {
            path.append(cosPath);
        }

        return path.toString();
    }

    private boolean isEndWithV4Appid(String bucket) {

        String appid = extractAppidFromBucket(bucket);
        return isCosV4Appid(appid);
    }

    private String extractAppidFromBucket(String bucket) {

        if (bucket == null || !bucket.contains("-") || bucket.endsWith("-")) {
            return "";
        }
        int index = bucket.lastIndexOf("-");
        return bucket.substring(index + 1);
    }

    private boolean isCosV4Appid(String appid) {

        if( appid == null || appid.length() != 8 || !appid.startsWith("100")) {
            return false;
        }

        try {
            Long.valueOf(appid);
        } catch (NumberFormatException exception) {
            return false;
        }
        return true;
    }

    public boolean isDebuggable() {
        return isDebuggable;
    }

    public int getSocketTimeout() {
        return socketTimeout;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public RetryStrategy getRetryStrategy() {
        return retryStrategy;
    }

    public QCloudHttpRetryHandler getQCloudHttpRetryHandler(){
        return qCloudHttpRetryHandler;
    }

    public Executor getExecutor(){
        return executor;
    }

    public boolean isEnableQuic(){
        return isQuic;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(protocol);
        dest.writeString(region);
        dest.writeInt(isDebuggable ? 1 : 0);
    }

    private CosXmlServiceConfig(Parcel in) {

        this(new Builder()
                .isHttps("https".equals(in.readString()))
                .setRegion(in.readString())
                .setDebuggable(in.readInt() == 1));

    }

    public static final Parcelable.Creator<CosXmlServiceConfig> CREATOR
            = new Parcelable.Creator<CosXmlServiceConfig>() {
        public CosXmlServiceConfig createFromParcel(Parcel in) {
            return new CosXmlServiceConfig(in);
        }

        public CosXmlServiceConfig[] newArray(int size) {
            return new CosXmlServiceConfig[size];
        }
    };

    public final static class Builder {

        private String protocol;
        private String userAgent;

        private String region;
        private String appid;
        private String host;
        private int port = -1;
        private String endpointSuffix;

        private boolean bucketInPath;

        private boolean isDebuggable;

        private RetryStrategy retryStrategy;
        private QCloudHttpRetryHandler qCloudHttpRetryHandler;

        private int connectionTimeout = 15 * 1000;  //in milliseconds
        private int socketTimeout = 30 * 1000;  //in milliseconds

        private Executor executor;

        private boolean isQuic = false;
        private boolean dnsCache = true;

        private Map<String, List<String>> commonHeaders = new HashMap<>();
        private List<String> noSignHeaders = new LinkedList<>();

        private String hostFormat;
        private boolean accelerate;

        public Builder() {
            protocol = HTTPS_PROTOCOL;
            userAgent = DEFAULT_USER_AGENT;
            isDebuggable = false;
            retryStrategy = RetryStrategy.DEFAULT;
            bucketInPath = false;
        }

        /**
         * 设置连接超时时间
         *
         * @param connectionTimeoutMills 超时时间
         * @return Builder 对象
         */
        public Builder setConnectionTimeout(int connectionTimeoutMills) {
            this.connectionTimeout = connectionTimeoutMills;
            return this;
        }

        /**
         * 设置 Socket 超时时间
         *
         * @param socketTimeoutMills 超时时间
         * @return Builder 对象
         */
        public Builder setSocketTimeout(int socketTimeoutMills) {
            this.socketTimeout = socketTimeoutMills;
            return this;
        }

        /**
         * 设置是否 Https 协议，默认为 Https
         *
         * @param isHttps 是否 Https
         * @return Builder 对象
         */
        public Builder isHttps(boolean isHttps) {
            if (isHttps) {
                protocol = HTTPS_PROTOCOL;
            } else {
                protocol = HTTP_PROTOCOL;
            }
            return this;
        }

        /**
         * 设置 Host 的格式化字符串，sdk 会将 ${bucket} 替换为真正的 bucket，${region} 替换为真正的 region，
         * 比如将 hostFormat 设置为  ${bucket}.${region}.tencent.com，并且您的存储桶和地域分别为 bucket-1250000000
         * 和 ap-shanghai，那么最终的请求地址为 bucket-1250000000.ap-shanghai.tencent.com
         *
         * </>
         * 注意，这个设置不会影响 GetService 请求，GetService 请求 Host 通过 {@link CosXmlService#setServiceDomain(String)} 设置
         *
         * @param hostFormat host 格式化字符串
         * @return Builder 对象
         */
        public Builder setHostFormat(String hostFormat) {

            this.hostFormat = hostFormat;
            return this;
        }


        /**
         * 设置用户的 appid 和存储桶的地域。
         * <br>
         * COS 服务的 存储桶名称的格式为 bucketName-appid ，如果您调用了这个方法设置了 appid，那么后续
         * 在使用存储桶名称时，只需要填写 bucketName 即可，当 SDK 检测到没有以 -appid 结尾时，会进行自动
         * 拼接。
         *
         * <br>
         *
         * 该方法已不推荐使用。建议使用 {@link CosXmlServiceConfig.Builder#setRegion(String)} 来设置
         * 存储桶的地域，后续的存储桶名称需要严格按照 bucketName-appid 的格式，否则会报存储桶不存在的错误，
         * 这种方式下SDK 不会对您填写的存储桶名称做任何处理。
         *
         * @param appid appid
         * @param region 存储桶地域
         * @return Builder 对象
         */
        @Deprecated
        public Builder setAppidAndRegion(String appid, String region) {
            this.appid = appid;
            this.region = region;
            return this;
        }

        /**
         * 设置默认地域
         *
         * @param region 默认地域
         * @return Builder 对象
         */
        public Builder setRegion(String region) {
            this.region = region;
            return this;
        }

        /**
         * 已废弃，请换用 {@link CosXmlServiceConfig.Builder#setHostFormat(String)} 方法
         *
         * @param endpointSuffix Host 的 suffix
         * @return Builder 对象
         */
        @Deprecated
        public Builder setEndpointSuffix(String endpointSuffix) {
            this.endpointSuffix = endpointSuffix;
            return this;
        }

        /**
         * 设置除了 GetService 请求外的 host
         *
         * @param host sdk 请求的 host，优先级比 hostFormat 更高
         * @return Builder 对象
         */
        public Builder setHost(String host) {
            this.host = host;
            return this;
        }

        /**
         * 设置除了 GetService 请求外的 Uri，用于解析 host、port 和 protocol
         *
         * @param uri sdk 请求的地址，优先级比 hostFormat 更高
         * @return Builder 对象
         */
        public Builder setHost(Uri uri) {
            this.host = uri.getHost();
            if (uri.getPort() != -1) {
//                this.host += ":" + uri.getPort();
                this.port = uri.getPort();
            }
            this.protocol = uri.getScheme();
            return this;
        }

        /**
         * 是否打印 Debug 日志
         *
         * @param isDebuggable 是否打印 Debug 日志
         * @return Builder 对象
         */
        public Builder setDebuggable(boolean isDebuggable) {
            this.isDebuggable = isDebuggable;
            return this;
        }

        /**
         * 自定义重试策略
         *
         * @param retryStrategy 重试策略
         * @return Builder 对象
         */
        public Builder setRetryStrategy(RetryStrategy retryStrategy) {
            this.retryStrategy = retryStrategy;
            return this;
        }

        /**
         * 自定义重试策略
         *
         * @param qCloudHttpRetryHandler 重试策略
         * @return Builder 对象
         */
        public Builder setRetryHandler(QCloudHttpRetryHandler qCloudHttpRetryHandler){
            this.qCloudHttpRetryHandler = qCloudHttpRetryHandler;
            return this;
        }

        /**
         * 已废弃，请使用 {@link Builder#setPathStyle(boolean)} 方法
         * @param bucketInPath
         * @return Builder 对象
         */
        @Deprecated
        public Builder setBucketInPath(boolean bucketInPath) {
            this.bucketInPath = bucketInPath;
            return this;
        }

        /**
         * 设置 Bucket 参数在请求 Url 的 path 中，而是不 host 中，
         * 比如 cos.ap-shanghai.myqcloud.com/1250000000-bucket/readMe.txt
         *
         * @param pathStyle
         * @return Builder 对象
         */
        public Builder setPathStyle(boolean pathStyle) {
            this.bucketInPath = pathStyle;
            return this;
        }

        /**
         * 自定义线程池
         *
         * @param excutor 线程池
         * @return Builder 对象
         */
        public Builder setExecutor(Executor excutor){
            this.executor = excutor;
            return this;
        }

        /**
         * 是否使能 Quic 协议，使能后，还需要额外引入 quic sdk 才能正常使用
         *
         * @param isEnable 是否使能
         * @return Builder 对象
         */
        public Builder enableQuic(boolean isEnable){
            this.isQuic = isEnable;
            return this;
        }

        /**
         * 是否开启 DNS 解析缓存，开启后，将 DNS 解析的结果缓存在本地，
         * 当系统 DNS 解析失败后，会使用本地缓存的 DNS 结果
         *
         * @param dnsCache 是否使能
         * @return Builder 对象
         */
        public Builder dnsCache(boolean dnsCache) {
            this.dnsCache = dnsCache;
            return this;
        }

        /**
         * 是否使用全球加速域名
         *
         * @param accelerate
         * @return Builder 对象
         */
        public Builder setAccelerate(boolean accelerate) {
            this.accelerate = accelerate;
            return this;
        }

        public CosXmlServiceConfig builder() {

            return new CosXmlServiceConfig(this);
        }

        /**
         * 给所有的请求统一添加 Header
         *
         * @param key
         * @param value
         */
        public Builder addHeader(String key, String value) {

            List<String> values = commonHeaders.get(key);
            if (values == null) {
                values = new LinkedList<>();
            }
            values.add(value);
            commonHeaders.put(key, values);
            return this;
        }

        public Builder addNoSignHeaders(String key) {
            noSignHeaders.add(key);
            return this;
        }
    }
}
