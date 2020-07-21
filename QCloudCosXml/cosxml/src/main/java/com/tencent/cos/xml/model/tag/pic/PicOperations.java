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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * 盲水印header（Pic-Operations）参数
 * 详情请参考：<a herf="https://cloud.tencent.com/document/product/1246/45384">盲水印功能</a>
 */
public class PicOperations {

    private boolean isPicInfo;

    private List<PicOperationRule> rules;

    /**
     * 构造盲水印Pic-Operations
     * @param isPicInfo 是否返回原图信息
     * @param rules 处理规则，一条规则对应一个处理结果（目前最多支持五条规则），不填则不进行图片处理
     */
    public PicOperations(boolean isPicInfo, List<PicOperationRule> rules) {
        this.isPicInfo = isPicInfo;
        this.rules = rules;
    }

    /**
     * 转换为json字符串
     * @return json字符串
     */
    public String toJsonStr() {
        if (rules == null || rules.isEmpty()) {
            return "{}";
        }
        JSONObject operations = new JSONObject();
        try {
            operations.put("is_pic_info", isPicInfo ? 1 : 0);
            JSONArray rulesArray = new JSONArray();
            for (PicOperationRule rule : rules) {
                rulesArray.put(rule.toJsonObject());
            }
            operations.put("rules", rulesArray);
            return operations.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return "{}";
    }
}
