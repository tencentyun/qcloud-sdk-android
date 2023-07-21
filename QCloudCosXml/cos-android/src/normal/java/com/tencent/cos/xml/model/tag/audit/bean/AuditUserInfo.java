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
 * 用户业务字段。
 */
@XmlBean(name = "UserInfo")
public class AuditUserInfo {
    /**
     *一般用于表示账号信息，长度不超过128字节。
     */
    public String tokenId;

    /**
     *一般用于表示昵称信息，长度不超过128字节。
     */
    public String nickname;

    /**
     *一般用于表示设备信息，长度不超过128字节。
     */
    public String deviceId;

    /**
     *一般用于表示 App 的唯一标识，长度不超过128字节。
     */
    public String appId;

    /**
     *一般用于表示房间号信息，长度不超过128字节。
     */
    public String room;

    /**
     *一般用于表示 IP 地址信息，长度不超过128字节。
     */
    public String iP;

    /**
     *一般用于表示业务类型，长度不超过128字节。
     */
    public String type;

    /**
     *一般用于表示接收消息的用户账号，长度不超过128字节。
     */
    public String receiveTokenId;

    /**
     *一般用于表示性别信息，长度不超过128字节。
     */
    public String gender;

    /**
     *一般用于表示等级信息，长度不超过128字节。
     */
    public String level;

    /**
     *一般用于表示角色信息，长度不超过128字节。
     */
    public String role;
}
