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

package com.tencent.cos.xml.model.tag.pic;

import android.text.TextUtils;

import com.tencent.cos.xml.BeaconService;
import com.tencent.qcloud.qcloudxml.annoation.XmlBean;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 盲水印处理规则
 */
public class PicOperationRule {
    private static final String TAG = "PicOperationRule";
    private String bucket;

    private String fileId;

    private String rule;

    /**
     * 构造盲水印处理规则
     * @param fileId 处理结果的文件路径名称，如以/开头，则存入指定文件夹中，否则，存入原图文件存储的同目录
     * @param rule 处理参数，参见数据万象图片处理 API。 若按指定样式处理，则以style/开头，后加样式名，如样式名为“test”，则 rule 字段为style/test
     */
    public PicOperationRule(String fileId, String rule) {
        this.fileId = fileId;
        this.rule = rule;
    }

    /**
     * 构造盲水印处理规则
     * @param bucket 存储结果的目标 bucket 名称，格式为：BucketName-APPID，如果不指定的话默认保存到当前 bucket
     * @param fileId 处理结果的文件路径名称，如以/开头，则存入指定文件夹中，否则，存入原图文件存储的同目录
     * @param rule 处理参数，参见数据万象图片处理 API。 若按指定样式处理，则以style/开头，后加样式名，如样式名为“test”，则 rule 字段为style/test
     */
    public PicOperationRule(String bucket, String fileId, String rule) {
        this.bucket = bucket;
        this.fileId = fileId;
        this.rule = rule;
    }

    /**
     * 转换为json字符串
     * @return json字符串
     */
    public JSONObject toJsonObject() {
        JSONObject ruleJson = new JSONObject();
        try {
            if (!TextUtils.isEmpty(bucket)) {
                ruleJson.put("bucket", bucket);
            }
            ruleJson.put("fileid", fileId);
            ruleJson.put("rule", rule);
        } catch (JSONException e) {
            BeaconService.getInstance().reportError(TAG, e);
            e.printStackTrace();
        }
        return ruleJson;
    }
}
