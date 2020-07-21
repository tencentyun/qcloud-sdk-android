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

package com.tencent.cos.xml;

import androidx.annotation.Nullable;

import com.tencent.qcloud.core.auth.COSXmlSignSourceProvider;
import com.tencent.qcloud.core.auth.COSXmlSigner;
import com.tencent.qcloud.core.auth.QCloudCredentials;
import com.tencent.qcloud.core.auth.QCloudSigner;
import com.tencent.qcloud.core.auth.ShortTimeCredentialProvider;
import com.tencent.qcloud.core.common.QCloudClientException;
import com.tencent.qcloud.core.http.HttpConstants;
import com.tencent.qcloud.core.http.QCloudHttpRequest;
import com.tencent.qcloud.core.http.RequestBodySerializer;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;

public class MyQCloudSigner implements QCloudSigner {
    @Override
    public void sign(QCloudHttpRequest request, QCloudCredentials credentials) throws QCloudClientException {

        URL url = request.url();

        String method = request.method();
        String host = url.getHost();
        String schema = url.getProtocol();
        String path = url.getPath();
        Map<String, List<String>> headers = request.headers();

        String sign = exampleLocalSignerService(method, schema, host, path, headers);
        request.addHeader("Authorization", sign);
    }

    /**
     * 这里仅仅是为了方便测试，在本地生成了签名串，生产环境下不能使用这个方法。
     *
     * 正式使用时请在服务端根据 COS 签名文档来计算 COS 请求对应的签名串，然后返回给终端用于身份校验。
     *
     * @return
     */
    @Deprecated
    private String exampleLocalSignerService(String method, String schema, String host, String path,
                                             Map<String, List<String>> headers) {

        String secretId = QServer.secretId;
        String secretKey = QServer.secretKey;

        ShortTimeCredentialProvider credentialProvider = new ShortTimeCredentialProvider(secretId, secretKey, 600);
        QCloudHttpRequest.Builder httpRequestBuilder = null;

        httpRequestBuilder = new QCloudHttpRequest.Builder()
                .method(method)
                .scheme(schema)
                .host(host)
                .path(path)
                .addHeader(HttpConstants.Header.HOST, host)
                .userAgent(CosXmlServiceConfig.DEFAULT_USER_AGENT)
                .signer("CosXmlSigner", (new COSXmlSignSourceProvider()));

        /**
         * PUT HTTP 请求 body 不能为空
         */
        if (method.equalsIgnoreCase("put") || method.equalsIgnoreCase("post")) {
            httpRequestBuilder.body(new RequestBodySerializer() {
                @Override
                public RequestBody body() {
                    return new RequestBody() {
                        @Nullable
                        @Override
                        public MediaType contentType() {
                            return MediaType.parse("plaint/text");
                        }

                        @Override
                        public void writeTo(BufferedSink bufferedSink) throws IOException {
                            bufferedSink.write(new byte[3]);
                        }
                    };
                }
            });
        }

        QCloudHttpRequest httpRequest = httpRequestBuilder.build();

        try {
            COSXmlSigner cosXmlSigner =  new COSXmlSigner();
            cosXmlSigner.sign(httpRequest, credentialProvider.getCredentials());
        } catch (QCloudClientException e) {
            e.printStackTrace();
        }
        Object auth = httpRequest.headers().get("Authorization");
        return (String) ((List)auth).get(0);
    }


}
