package com.tencent.cos.xml.model.bucket;

import android.text.TextUtils;
import android.widget.TextView;

import com.tencent.cos.xml.common.COSRequestHeaderKey;
import com.tencent.cos.xml.common.ClientErrorCode;
import com.tencent.cos.xml.common.RequestMethod;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.model.tag.InventoryConfiguration;
import com.tencent.cos.xml.transfer.XmlBuilder;
import com.tencent.qcloud.core.http.RequestBodySerializer;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public PutBucketInventoryRequest() {
        this(null);
    }

    public void setInventoryId(String inventoryId) {
        inventoryConfiguration.id = inventoryId;
    }

    public void isEnable(boolean isEnabled){
        inventoryConfiguration.isEnabled = isEnabled;
    }

    public void setFilter(String prefix){
        if(!TextUtils.isEmpty(prefix)){
           inventoryConfiguration.filter = new InventoryConfiguration.Filter();
           inventoryConfiguration.filter.prefix = prefix;
        }
    }

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

    public void setScheduleFrequency(String frequency){
        if(frequency != null){
            inventoryConfiguration.schedule.frequency = frequency;
        }
    }

    public void setOptionalFields(InventoryConfiguration.Field field){
        if(field != null){
            if(inventoryConfiguration.optionalFields == null){
                inventoryConfiguration.optionalFields = new InventoryConfiguration.OptionalFields();
                inventoryConfiguration.optionalFields.fields = new HashSet<>(6);
            }
            inventoryConfiguration.optionalFields.fields.add(field.getValue());
        }
    }

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
    public RequestBodySerializer getRequestBody() throws CosXmlClientException {
        try {
            return RequestBodySerializer.string(COSRequestHeaderKey.APPLICATION_XML,
                    XmlBuilder.buildInventoryConfiguration(inventoryConfiguration));
        } catch (IOException e) {
            throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(), e);
        } catch (XmlPullParserException e) {
            throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(), e);
        }
    }

    @Override
    public boolean isNeedMD5() {
        return true;
    }

    @Override
    public void checkParameters() throws CosXmlClientException {
        super.checkParameters();
        if(inventoryConfiguration.id == null) throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(), "inventoryId == null");
        Matcher matcher = pattern.matcher(inventoryConfiguration.id);
        if(!matcher.find()){
            throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(),
                    "inventoryId must be in [a-zA-Z0-9-_.]");
        }
        if(inventoryConfiguration.includedObjectVersions == null){
            throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(), "includedObjectVersions == null");
        }
        if(inventoryConfiguration.schedule.frequency == null){
            throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(), "schedule.frequency == null");
        }
        if(inventoryConfiguration.destination.cosBucketDestination.bucket == null){
            throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(), "cosBucketDestination.bucket == null");
        }
        if(inventoryConfiguration.destination.cosBucketDestination.format == null){
            throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(), "cosBucketDestination.format == null");
        }

    }
}
