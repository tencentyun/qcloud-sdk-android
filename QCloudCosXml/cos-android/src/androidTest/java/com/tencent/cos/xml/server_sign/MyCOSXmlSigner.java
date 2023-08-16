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

package com.tencent.cos.xml.server_sign;

import com.tencent.qcloud.core.auth.AuthConstants;
import com.tencent.qcloud.core.auth.SessionQCloudCredentials;
import com.tencent.qcloud.core.auth.Utils;
import com.tencent.qcloud.core.common.QCloudAuthenticationException;
import com.tencent.qcloud.core.common.QCloudClientException;

import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * COS签名器<br>
 * 服务端实现可以参考这里
 * 对请求进行签名
 * 请参考文档：<a href="https://cloud.tencent.com/document/product/436/7778">请求签名</a>
 */
public class MyCOSXmlSigner {
    /**
     * 临时秘钥
     */
    public static final SessionQCloudCredentials credentials = new SessionQCloudCredentials(
            "xxxxxxxxxxxxxxxxxxxxxxxx",
            "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
            "xxxxxxxxxxxxxxxxxxxxxxx",
            111111111, 11111111
    );

    /**
     * 简单上传最大size
     * 限定上传文件大小最大值，单位Byte。大于限制上传文件大小的最大值会被判为上传失败
     */
    public final static long SIMPLE_MAX_SIZE = 100 * 1024 * 1024;
    /**
     * 简单上传最大size header key
     */
    public final static String SIMPLE_MAX_SIZE_HEADER_KEY = "x-cos-fsize-max";
    /**
     * Upload Part最大size
     * 用于UploadPart，限定上传分块大小最大值，单位 Byte。大于限制上传文件大小的最大值会被判为上传失败
     */
    public final static long UPLOAD_PART_MAX_SIZE = 1024 * 1024;
    /**
     * Upload Part最大size header key
     */
    public final static String UPLOAD_PART_MAX_SIZE_HEADER_KEY = "x-cos-psize-max";
    /**
     * CompleteMultipartUpload最多分块数量
     * 用于CompleteMultipartUpload，限定上传分块的数量。分块数量超过限制，请求会被拒绝。
     */
    public final static int Complete_MAX_NUM = 100;
    /**
     * CompleteMultipartUpload最多分块数量 header key
     */
    public final static String Complete_MAX_NUM_HEADER_KEY = "x-cos-pnum-max";

    /**
     * 限定放通或不放通若干类型的 mimetype(可以限制上传文件的类型。一些比较常用的类型可以，特别少见的类型可能不太准确)
     */
//    public final static String MIME_LIMIT = "text/plain;img/jpg;img/*";
    public final static String MIME_LIMIT = "!text/plain";

    /**
     * mimetype header key
     */
    public final static String MIME_LIMIT_HEADER_KEY = "x-cos-mime-limit";

    /**
     * 签名方法
     * @param source 签名物料 采用了客户端拼source的方法，原因是需要端侧需要根据source进行分块上传签名缓存，如果服务端拼source的话可能和端侧不一致导致缓存失效
     * @param realHeaderList 要签名的header key集合字符串
     * @param realParameterList 要签名的param key集合字符串
     * @param httpMethod http请求方法
     * @param path http请求path
     * @param headers http请求header map
     * @param queryNameValues http请求query map
     * @return 签名结果
     */
    public static MyQCloudSelfSigner.SignResult sign(String source, String realHeaderList, String realParameterList,
                                                     String httpMethod, String path,
                                                     Map<String, List<String>> headers, Map<String, List<String>> queryNameValues) throws QCloudClientException{
        if (credentials == null) {
            throw new QCloudClientException(new QCloudAuthenticationException("Credentials is null."));
        }

        // 上传各api接口格式请参考：
        // PUT Object （简单上传） https://cloud.tencent.com/document/product/436/7749
        // Initiate Multipart Upload（初始化分块上传） https://cloud.tencent.com/document/product/436/7746
        // List Parts（查询特定分块上传中的已上传的块） https://cloud.tencent.com/document/product/436/7747
        // Upload Part（将对象按照分块的方式上传到 COS） https://cloud.tencent.com/document/product/436/7750
        // Complete Multipart Upload（完成整个分块上传） https://cloud.tencent.com/document/product/436/7742
        // Abort Multipart Upload（舍弃一个分块上传并删除已上传的块） https://cloud.tencent.com/document/product/436/7740

        // --------------- 处理业务逻辑 开始 ------------------
        // 如果是简单Put或Upload Part请求
        if("put".equals(httpMethod)){
            if(!queryNameValues.containsKey("partNumber")){
                // 简单put
                // 限制大小 判断x-cos-fsize-max是否存在并限制
                if(headers.containsKey(SIMPLE_MAX_SIZE_HEADER_KEY)){
                    long simpleMaxSize = Long.parseLong(headers.get(SIMPLE_MAX_SIZE_HEADER_KEY).get(0));
                    if(simpleMaxSize > SIMPLE_MAX_SIZE){
                        throw new QCloudClientException(new QCloudAuthenticationException("x-cos-fsize-max exceed the limit"));
                    }
                } else {
                    throw new QCloudClientException(new QCloudAuthenticationException("x-cos-fsize-max can not be empty"));
                }
            } else {
                // Upload Part
                // 限制大小 判断x-cos-psize-max是否存在并限制
                if(headers.containsKey(UPLOAD_PART_MAX_SIZE_HEADER_KEY)){
                    long uploadPartMaxSize = Long.parseLong(headers.get(UPLOAD_PART_MAX_SIZE_HEADER_KEY).get(0));
                    if(uploadPartMaxSize > UPLOAD_PART_MAX_SIZE){
                        throw new QCloudClientException(new QCloudAuthenticationException("x-cos-psize-max exceed the limit"));
                    }
                } else {
                    throw new QCloudClientException(new QCloudAuthenticationException("x-cos-psize-max can not be empty"));
                }
            }

            // 限制格式 判断x-cos-mime-limit是否存在并限制
            if(headers.containsKey(MIME_LIMIT_HEADER_KEY)){
                String mimeLimit = (headers.get(MIME_LIMIT_HEADER_KEY).get(0));
                if(!MIME_LIMIT.equals(mimeLimit)){
                    throw new QCloudClientException(new QCloudAuthenticationException("x-cos-mime-limit incorrect"));
                }
            } else {
                throw new QCloudClientException(new QCloudAuthenticationException("x-cos-mime-limit can not be empty"));
            }
        }
        // 如果是CompleteMultipartUpload请求 限制大小 判断x-cos-pnum-max是否存在并限制
        if("post".equals(httpMethod) && queryNameValues.containsKey("uploadId")){
            if(headers.containsKey(Complete_MAX_NUM_HEADER_KEY)){
                long completeMaxNum = Integer.parseInt(headers.get(Complete_MAX_NUM_HEADER_KEY).get(0));
                if(completeMaxNum > Complete_MAX_NUM){
                    throw new QCloudClientException(new QCloudAuthenticationException("x-cos-pnum-max exceed the limit"));
                }
            } else {
                throw new QCloudClientException(new QCloudAuthenticationException("x-cos-pnum-max can not be empty"));
            }
        }


        // 可以使用httpMethod、path、headers、queryNameValues做其他业务处理
        // --------------- 处理业务逻辑 结束 ------------------

        // --------------- 计算签名开始 ------------------
        StringBuilder authorization = new StringBuilder();

        String keyTime = credentials.getKeyTime();
        String signature = signature(source, credentials.getSignKey());

        authorization.append(AuthConstants.Q_SIGN_ALGORITHM).append("=").append(AuthConstants.SHA1).append("&")
                .append(AuthConstants.Q_AK).append("=")
                .append(credentials.getSecretId()).append("&")
                .append(AuthConstants.Q_SIGN_TIME).append("=")
                .append(keyTime).append("&")
                .append(AuthConstants.Q_KEY_TIME).append("=")
                .append(credentials.getKeyTime()).append("&")
                .append(AuthConstants.Q_HEADER_LIST).append("=")
                .append(realHeaderList.toLowerCase(Locale.ROOT)).append("&")
                .append(AuthConstants.Q_URL_PARAM_LIST).append("=")
                .append(realParameterList.toLowerCase(Locale.ROOT)).append("&")
                .append(AuthConstants.Q_SIGNATURE).append("=").append(signature);
        String auth = authorization.toString();
        // --------------- 计算签名结束 ------------------
        return new MyQCloudSelfSigner.SignResult(auth, credentials.getToken());
    }

    private static String signature(String source, String signKey) {
        byte[] sha1Bytes = Utils.hmacSha1(source, signKey);
        String signature = "";
        if (sha1Bytes != null) {
            signature = new String(Utils.encodeHex(sha1Bytes));
        }
        return signature;
    }
}
