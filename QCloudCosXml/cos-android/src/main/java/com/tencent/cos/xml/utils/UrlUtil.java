package com.tencent.cos.xml.utils;

import android.text.TextUtils;
import android.util.Patterns;
import android.webkit.URLUtil;

import com.tencent.cos.xml.common.COSRequestHeaderKey;
import com.tencent.cos.xml.common.Range;
import com.tencent.cos.xml.model.tag.UrlUploadPolicy;
import com.tencent.qcloud.core.common.QCloudClientException;
import com.tencent.qcloud.core.common.QCloudServiceException;
import com.tencent.qcloud.core.http.HttpRequest;
import com.tencent.qcloud.core.http.HttpResult;
import com.tencent.qcloud.core.http.QCloudHttpClient;
import com.tencent.qcloud.core.util.QCloudHttpUtils;

import java.net.MalformedURLException;
import java.net.URL;

import static com.tencent.qcloud.core.http.HttpConstants.Header.CONTENT_LENGTH;
import static com.tencent.qcloud.core.http.HttpConstants.Header.CONTENT_RANGE;

/**
 * url相关工具类
 * <p>
 *
 * @author jordanqin
 * @since 2021/1/25
 * <p>
 * Copyright (c) 2010-2020 Tencent Cloud. All rights reserved.
 */
public class UrlUtil {
    /**
     * 是否是URL
     * @param url 需要校验的字符串
     * @return 是否是URL
     */
    public static boolean isUrl(String url){
        if(TextUtils.isEmpty(url)) return false;
        return Patterns.WEB_URL.matcher(url).matches() && (URLUtil.isHttpsUrl(url) || URLUtil.isHttpUrl(url));
    }

    /**
     * 获取url上传策略
     * 注意：需要外部控制在非主线程执行
     * @param url url
     * @return 上传策略
     */
    public static UrlUploadPolicy getUrlUploadPolicy(String url) {
        if (!UrlUtil.isUrl(url))
            return new UrlUploadPolicy(UrlUploadPolicy.Type.NOTSUPPORT, 0);

        URL url1 = null;
        try {
            url1 = new URL(url);
        } catch (MalformedURLException e) {
            return new UrlUploadPolicy(UrlUploadPolicy.Type.NOTSUPPORT, 0);
        }

        Range range = new Range(0, 1);
        HttpRequest<String> request = new HttpRequest.Builder<String>()
                .url(url1)
                .method("GET")
                .addHeader(COSRequestHeaderKey.RANGE, range.getRange())
                .build();


        HttpResult<String> result = null;
        QCloudHttpClient defaultClient = QCloudHttpClient.getDefault();
        try {
            result = defaultClient
                    .resolveRequest(request)
                    .executeNow();
        } catch (QCloudServiceException | QCloudClientException e) {
            return new UrlUploadPolicy(UrlUploadPolicy.Type.NOTSUPPORT, 0);
        }

        if (result != null &&
                result.isSuccessful() &&
                result.headers() != null && result.headers().size() > 0) {
            String acceptRanges = result.header("Accept-Ranges");
            String contentRange = result.header(CONTENT_RANGE);

            if("bytes".equals(acceptRanges) && !TextUtils.isEmpty(contentRange)){
                long[] contentRanges = QCloudHttpUtils.parseContentRange(contentRange);
                if (contentRanges != null) {
                    return new UrlUploadPolicy(UrlUploadPolicy.Type.RANGE, contentRanges[2]);
                }
            } else {
                String contentLength = result.header(CONTENT_LENGTH);
                if(!TextUtils.isEmpty(contentLength)){
                    return new UrlUploadPolicy(UrlUploadPolicy.Type.ENTIRETY, Long.parseLong(contentLength));
                }
            }
        }

        return new UrlUploadPolicy(UrlUploadPolicy.Type.NOTSUPPORT, 0);
    }
}
