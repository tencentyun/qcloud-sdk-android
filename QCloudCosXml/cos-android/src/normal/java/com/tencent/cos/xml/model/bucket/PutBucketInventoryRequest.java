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

package com.tencent.cos.xml.model.bucket;

import android.text.TextUtils;

import com.tencent.cos.xml.common.COSRequestHeaderKey;
import com.tencent.cos.xml.common.RequestMethod;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.tag.InventoryConfiguration;
import com.tencent.cos.xml.transfer.XmlBuilder;
import com.tencent.qcloud.core.http.RequestBodySerializer;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 在存储桶中创建清单任务的请求.
 * @see com.tencent.cos.xml.CosXml#putBucketInventory(PutBucketInventoryRequest)
 * @see com.tencent.cos.xml.CosXml#putBucketInventoryAsync(PutBucketInventoryRequest, CosXmlResultListener)
 */
public class PutBucketInventoryRequest extends BucketRequest {

    private static Pattern pattern = Pattern.compile("[a-zA-Z0-9-_.]+");
    private InventoryConfiguration inventoryConfiguration;

    public PutBucketInventoryRequest(String bucket) {
        super(bucket);
        inventoryConfiguration = new InventoryConfiguration();
        inventoryConfiguration.isEnabled = true;
        inventoryConfiguration.id = "None";
        inventoryConfiguration.schedule = new InventoryConfiguration.Schedule();
        inventoryConfiguration.destination = new InventoryConfiguration.Destination();
        inventoryConfiguration.destination.cosBucketDestination = new InventoryConfiguration.COSBucketDestination();
    }

    /**
     * 设置清单的名称
     * @param inventoryId 清单的名称
     */
    public void setInventoryId(String inventoryId) {
        inventoryConfiguration.id = inventoryId;
    }

    /**
     * 设置清单是否启用
     * @param isEnabled 清单是否启用的标识
     */
    public void isEnable(boolean isEnabled){
        inventoryConfiguration.isEnabled = isEnabled;
    }

    /**
     * 设置筛选待分析的对象前缀
     * @param prefix 筛选待分析的对象前缀
     */
    public void setFilter(String prefix){
        if(!TextUtils.isEmpty(prefix)){
           inventoryConfiguration.filter = new InventoryConfiguration.Filter();
           inventoryConfiguration.filter.prefix = prefix;
        }
    }

    /**
     * 设置存放清单结果的存储桶信息
     * @param format 清单分析结果的文件形式，可选项为 CSV 格式和 ORC 格式
     * @param accountId 存储桶的所有者 ID
     * @param bucket 清单分析结果的存储桶名
     * @param region 清单分析结果的存储桶区域
     * @param prefix 清单分析结果的前缀
     */
    public void setDestination(String format, String accountId, String bucket, String region, String prefix){
        if(format != null) inventoryConfiguration.destination.cosBucketDestination.format = format;
        if(accountId != null)inventoryConfiguration.destination.cosBucketDestination.accountId = accountId;
        if(bucket != null && region != null){
            inventoryConfiguration.destination.cosBucketDestination.bucket = "qcs::cos:" + region
                    + "::" + bucket;
        }
        if(prefix != null)inventoryConfiguration.destination.cosBucketDestination.prefix = prefix;
        inventoryConfiguration.destination.cosBucketDestination.encryption = new InventoryConfiguration.Encryption();
        inventoryConfiguration.destination.cosBucketDestination.encryption.sSECOS = ""; //默认不填
    }

    /**
     * 设置清单任务周期
     * @param frequency 清单任务周期，可选项为按日或者按周
     */
    public void setScheduleFrequency(String frequency){
        if(frequency != null){
            inventoryConfiguration.schedule.frequency = frequency;
        }
    }

    /**
     * 添加清单结果中应包含的分析维度
     * @param field 清单结果中应包含的分析维度
     */
    public void setOptionalFields(InventoryConfiguration.Field field){
        if(field != null){
            if(inventoryConfiguration.optionalFields == null){
                inventoryConfiguration.optionalFields = new InventoryConfiguration.OptionalFields();
                inventoryConfiguration.optionalFields.fields = new HashSet<>(6);
            }
            inventoryConfiguration.optionalFields.fields.add(field.getValue());
        }
    }

    /**
     * 设置是否在清单中包含对象版本
     * 如果设置为 All ，清单中将会包含所有对象版本，并在清单中增加 VersionId， IsLatest， DeleteMarker 这几个字段
     * 如果设置为 Current，则清单中不包含对象版本信息
     * @param includedObjectVersions 是否在清单中包含对象版本
     */
    public void setIncludedObjectVersions(InventoryConfiguration.IncludedObjectVersions includedObjectVersions){
        if(includedObjectVersions != null){
            inventoryConfiguration.includedObjectVersions = includedObjectVersions.getDesc();
        }
    }

    @Override
    public String getMethod() {
        return RequestMethod.PUT;
    }

    @Override
    public Map<String, String> getQueryString() {
        queryParameters.put("inventory", null);
        queryParameters.put("id", inventoryConfiguration.id);
        return super.getQueryString();
    }

    @Override
    protected RequestBodySerializer xmlBuilder() throws XmlPullParserException, IOException {
        return RequestBodySerializer.string(COSRequestHeaderKey.APPLICATION_XML,
                XmlBuilder.buildInventoryConfiguration(inventoryConfiguration));
    }

    @Override
    public boolean isNeedMD5() {
        return true;
    }

    @Override
    public void checkParameters() throws CosXmlClientException {
        super.checkParameters();
        Matcher matcher = pattern.matcher(inventoryConfiguration.id);
        if(!matcher.find()){
        }
        if(inventoryConfiguration.includedObjectVersions == null){
        }
        if(inventoryConfiguration.schedule.frequency == null){
        }
        if(inventoryConfiguration.destination.cosBucketDestination.bucket == null){
        }
        if(inventoryConfiguration.destination.cosBucketDestination.format == null){
        }

    }
}
