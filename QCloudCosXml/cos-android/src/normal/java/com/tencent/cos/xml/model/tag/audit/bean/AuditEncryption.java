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

package com.tencent.cos.xml.model.tag.audit.bean;

import com.tencent.qcloud.qcloudxml.annoation.XmlBean;

/**
 * 文件加密信息。
 */
@XmlBean(name = "Encryption")
public class AuditEncryption {
    /**
     * 当前支持aes-256-ctr、aes-256-cfb、aes-256-ofb、aes-192-ctr、aes-192-cfb、aes-192-ofb、aes-128-ctr、aes-128-cfb、aes-128-ofb，不区分大小写。以aes-256-ctr为例，aes代表加密算法，256代表密钥长度，ctr代表加密模式。
     */
    public String algorithm;

    /**
     * 文件加密使用的密钥的值，需进行 Base64 编码。当KeyType值为1时，需要将Key进行指定的加密后再做Base64 编码。Key的长度与使用的算法有关，详见Algorithm介绍，如：使用aes-256-ctr算法时，需要使用256位密钥，即32个字节。
     */
    public String key;

    /**
     * 初始化向量，需进行 Base64 编码。AES算法要求IV长度为128位，即16字节。
     */
    public String iV;

    /**
     * 当KeyType值为1时，该字段表示RSA加密密钥的版本号，当前支持1.0。默认值为1.0。
     */
    public String keyId = "1.0";

    /**
     * 指定加密算法的密钥（参数Key）的传输模式，有效值：0（明文传输）、1（RSA密文传输，使用OAEP填充模式），默认值为0。
     */
    public int keyType;
}
