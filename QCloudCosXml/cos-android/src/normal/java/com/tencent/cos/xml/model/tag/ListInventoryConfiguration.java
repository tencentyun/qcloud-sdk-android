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

import java.util.Set;

/**
 * 存储桶中所有清单任务信息的列表
 */
public class ListInventoryConfiguration {
    /**
     * 清单任务集合
     */
    public Set<InventoryConfiguration> inventoryConfigurations;
    /**
     * 是否已列出所有清单任务信息的标识。如果已经展示完则为 false，否则为 true
     */
    public boolean isTruncated = false;
    /**
     * 页清单列表的标识，可理解为页数。该标识与请求中的 continuation-token 参数对应
     */
    public String continuationToken;
    /**
     * 下一页清单列表的标识。如果该参数中有值，
     * 则可将该值作为 continuation-token 参数并发起 GET 请求以获取下一页清单任务信息
     */
    public String nextContinuationToken;

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("{ListInventoryConfigurationResult\n");
        stringBuilder.append("IsTruncated:").append(isTruncated).append("\n");
        if(continuationToken != null)stringBuilder.append("ContinuationToken:").append(continuationToken).append("\n");
        if(nextContinuationToken != null)stringBuilder.append("NextContinuationToken:").append(nextContinuationToken).append("\n");
        if(inventoryConfigurations != null){
            for(InventoryConfiguration inventoryConfiguration : inventoryConfigurations){
                if(inventoryConfiguration != null)stringBuilder.append(inventoryConfiguration.toString()).append("\n");
            }
        }
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}
