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

package com.tencent.cos.xml.model.tag;

/**
 * Object restore 操作的所有请求信息
 */
public class RestoreConfigure {
    /**
     * 指定恢复出的临时副本的有效时长，单位为“天”
     */
    public int days;
    /**
     * 恢复工作参数
     */
    public CASJobParameters casJobParameters;

    @Override
    public String toString(){
        StringBuilder stringBuilder = new StringBuilder("{RestoreRequest:\n");
        stringBuilder.append("Days:").append(days).append("\n");
        if(casJobParameters != null)stringBuilder.append(casJobParameters.toString()).append("\n");
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    /**
     * 恢复工作参数
     */
    public static class CASJobParameters{
        public String tier = Tier.Standard.getTier();

        @Override
        public String toString(){
            StringBuilder stringBuilder = new StringBuilder("{CASJobParameters:\n");
            stringBuilder.append("Tier:").append(tier).append("\n");
            stringBuilder.append("}");
            return stringBuilder.toString();
        }
    }

    /**
     * 恢复时，Tier 可以指定为支持的三种恢复模式，分别为：
     * Standard：标准模式，恢复任务在3 - 5小时内完成
     * Expedited：极速模式，恢复任务在1 - 5分钟内可完成，仅支持不超过 256MB 的对象
     * Bulk：批量模式，恢复任务在5 - 12小时内完成
     */
    public static enum Tier{
        Expedited("Expedited") ,
        Standard("Standard"),
        Bulk("Bulk");
        private String tier;
        Tier(String tier) {
            this.tier = tier;
        }

        public String getTier() {
            return tier;
        }
    }
}
