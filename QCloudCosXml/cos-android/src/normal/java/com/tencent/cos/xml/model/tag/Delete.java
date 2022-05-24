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

import java.util.List;

/**
 * 批量删除对象数据
 */
public class Delete {
    /**
     * 布尔值，默认为 false
     * true 为使用 Quiet 模式，在响应中仅包含删除失败的对象信息和错误信息
     * false 为使用 Verbose 模式，在响应中包含每个对象的删除结果
     */
    public boolean quiet;
    /**
     * 要删除的目标对象列表
     */
    public List<DeleteObject> deleteObjects;

    public static class DeleteObject{
        /**
         * 对象键
         */
        public String key;
        /**
         * 当启用版本控制并且要删除对象的指定版本时需指定该元素，值为要删除的版本 ID。若未开启版本控制或开启版本控制但需要插入删除标记，则无需指定该元素
         */
        public String versionId;
    }
}
