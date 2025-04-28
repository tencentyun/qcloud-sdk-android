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

package com.tencent.cos.xml.s3;

import com.tencent.qcloud.core.logger.COSLogger;

import java.util.HashMap;
import java.util.Map;

public enum Base64 {
    ;
    // private static final InternalLogApi LOG = InternalLogFactory.getLog(Base64.class);
    private static final Base64Codec codec = new Base64Codec();
    private static final boolean isJaxbAvailable;

    static {
        boolean available;
        try {
            Class.forName("javax.xml.bind.DatatypeConverter");
            available = true;
        } catch (Exception e) {
            available = false;
        }
        isJaxbAvailable = available;
        if (isJaxbAvailable) {
            Map<String, String> inconsistentJaxbImpls = new HashMap<String, String>();
            inconsistentJaxbImpls.put("org.apache.ws.jaxme.impl.JAXBContextImpl", "Apache JaxMe");

            // TODO: 2019-11-06 remove by rickenwang
//            try {
//                String className = JAXBContext.newInstance().getClass().getName();
//                if (inconsistentJaxbImpls.containsKey(className)) {
//                    QCloudLogger.w("S3", "A JAXB implementation known to produce base64 encodings that are " +
//                            "inconsistent with the reference implementation has been detected. The " +
//                            "results of the encodeAsString() method may be incorrect. Implementation: " +
//                            inconsistentJaxbImpls.get(className));
//                }
//            } catch (Exception ignored) {
//                // ignore
//            } catch (NoClassDefFoundError error){
//                // ignore
//            }
        } else {
            COSLogger.wProcess("S3", "JAXB is unavailable. Will fallback to SDK implementation which may be less performant." +
                    "If you are using Java 9+, you will need to include javax.xml.bind:jaxb-api as a dependency.");
        }
    }

    /**
     * Returns a base 64 encoded string of the given bytes.
     */
    public static String encodeAsString(byte... bytes) {
        if (bytes == null) {
            return null;
        }
        // TODO: 2019-11-06 remove by rickenwang 
//        if (isJaxbAvailable) {
//            try {
//                return DatatypeConverter.printBase64Binary(bytes);
//            } catch (NullPointerException ex) {
//                // https://netbeans.org/bugzilla/show_bug.cgi?id=224923
//                // https://issues.apache.org/jira/browse/CAMEL-4893
//
//                // Note the converter should eventually be initialized and printBase64Binary should start working again
//                QCloudLogger.d("S3", "Recovering from JAXB bug: https://netbeans.org/bugzilla/show_bug.cgi?id=224923", ex);
//            }
//        }

        return bytes.length == 0 ? "" : CodecUtils.toStringDirect(codec.encode(bytes));
    }

    /**
     * Returns a 64 encoded byte array of the given bytes.
     */
    public static byte[] encode(byte[] bytes) {
        return bytes == null || bytes.length == 0 ? bytes : codec.encode(bytes);
    }

    /**
     * Decodes the given base 64 encoded string,
     * skipping carriage returns, line feeds and spaces as needed.
     */
    public static byte[] decode(String b64) {
        if (b64 == null) {
            return null;
        }
        if (b64.length() == 0) {
            return new byte[0];
        }
        byte[] buf = new byte[b64.length()];
        int len = CodecUtils.sanitize(b64, buf);
        return codec.decode(buf, len);
    }

    /**
     * Decodes the given base 64 encoded bytes.
     */
    public static byte[] decode(byte[] b64) {
        return b64 == null || b64.length == 0 ? b64 : codec.decode(b64, b64.length);
    }
}


