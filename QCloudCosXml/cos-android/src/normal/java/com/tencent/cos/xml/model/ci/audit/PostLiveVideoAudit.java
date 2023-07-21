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

package com.tencent.cos.xml.model.ci.audit;

import com.tencent.qcloud.qcloudxml.annoation.XmlBean;

@XmlBean(name = "Request", method = XmlBean.GenerateMethod.TO)
public class PostLiveVideoAudit {

    /**
     *审核的任务类型，直播流审核固定为 live_video。;是否必传：是;
     */
    public String type = "live_video";

    /**
     *包含需要审核的直播流信息。;是否必传：是;
     */
    public PostLiveVideoAuditInput input;

    /**
     *包含审核规则的配置信息。;是否必传：是;
     */
    public PostLiveVideoAuditConf conf;

    /**
     *包含直播流转存的配置信息。;是否必传：否;
     */
    public PostLiveVideoAuditStorageConf storageConf;

    @XmlBean(name = "Conf", method = XmlBean.GenerateMethod.TO)
    public static class PostLiveVideoAuditConf {
        /**
         *表示审核策略的唯一标识，您可以通过控制台上的审核策略页面，配置您希望审核的场景，例如涉黄、广告、违法违规等，配置指引： 设置审核策略。您可以在控制台上获取到 BizType。BizType 填写时，此条审核请求将按照该审核策略中配置的场景进行审核。BizType 不填写时��将自动使用默认的审核策略。;是否必传：是;
         */
        public String bizType;

        /**
         *回调地址，以http://或者https://开头的地址。;是否必传：否;
         */
        public String callback;

        /**
         *回调片段类型，有效值：1（回调全部截帧和音频片段）、2（仅回调违规截帧和音频片段）。默认为 1。;是否必传：否;
         */
        public int callbackType;

    }

    @XmlBean(name = "Input", method = XmlBean.GenerateMethod.TO)
    public static class PostLiveVideoAuditInput {
        /**
         *需要审核的直播流播放地址，例如 rtmp://example.com/live/123。;是否必传：是;
         */
        public String url;

        /**
         *该字段在审核结果中会返回原始内容，长度限制为512字节。您可以使用该字段对待审核的数据进行唯一业务标识。;是否必传：否;
         */
        public String dataId;

        /**
         *自定义字段，可用于辅助行为数据分析。;是否必传：否;
         */
        public PostLiveVideoAuditUserInfo userInfo;

    }

    @XmlBean(name = "StorageConf", method = XmlBean.GenerateMethod.TO)
    public static class PostLiveVideoAuditStorageConf {
        /**
         *表示直播流所要转存的路径，直播流的 ts 文件和 m3u8 文件将保存在本桶该目录下。m3u8 文件保存文件名为 Path/{$JobId}.m3u8，ts 文件的保存文件名为 Path/{$JobId}-{$Realtime}.ts，其中 Realtime 为17位年月日时分秒毫秒时间。;是否必传：否;
         */
        public String path;

    }

    @XmlBean(name = "UserInfo", method = XmlBean.GenerateMethod.TO)
    public static class PostLiveVideoAuditUserInfo {
        /**
         *一般用于表示账号信息，长度不超过128字节。;是否必传：否;
         */
        public String tokenId;

        /**
         *一般用于表示昵称信息，长度不超过128字节。;是否必传：否;
         */
        public String nickname;

        /**
         *一般用于表示设备信息，长度不超过128字节。;是否必传：否;
         */
        public String deviceId;

        /**
         *一般用于表示 App 的唯一标识，长度不超过128字节。;是否必传：否;
         */
        public String appId;

        /**
         *一般用于表示房间号信息，长度不超过128字节。;是否必传：否;
         */
        public String room;

        /**
         *一般用于表示 IP 地址信息，长度不超过128字节。;是否必传：否;
         */
        public String iP;

        /**
         *一般用于表示业务类型，长度不超过128字节。;是否必传：否;
         */
        public String type;

        /**
         *一般用于表示接收消息的用户账号，长度不超过128字节。;是否必传：否;
         */
        public String receiveTokenId;

        /**
         *一般用于表示性别信息，长度不超过128字节。;是否必传：否;
         */
        public String gender;

        /**
         *一般用于表示等级信息，长度不超过128字节。;是否必传：否;
         */
        public String level;

        /**
         *一般用于表示角色信息，长度不超过128字节。;是否必传：否;
         */
        public String role;

    }


   
}
