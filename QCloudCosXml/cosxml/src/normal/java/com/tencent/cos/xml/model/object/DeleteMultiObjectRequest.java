package com.tencent.cos.xml.model.object;

import android.text.TextUtils;

import com.tencent.cos.xml.CosXmlServiceConfig;
import com.tencent.cos.xml.common.COSRequestHeaderKey;
import com.tencent.cos.xml.common.ClientErrorCode;
import com.tencent.cos.xml.common.RequestMethod;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.model.tag.Delete;
import com.tencent.cos.xml.transfer.XmlBuilder;
import com.tencent.qcloud.core.auth.STSCredentialScope;
import com.tencent.qcloud.core.http.RequestBodySerializer;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 批量删除Object，单次请求最大支持批量删除 1000 个 Object。
 * </p>
 * <p>
 * 对于响应结果，COS 提供 Verbose 和 Quiet 两种模式：
 * Verbose 模式将返回每个 Object 的删除结果；Quiet 模式只返回报错的 Object 信息。
 * </p>
 *
*/
final public class DeleteMultiObjectRequest extends ObjectRequest {
    private Delete delete;
    public DeleteMultiObjectRequest(String bucket, List<String> deleteObjectList){
        super(bucket, null);
        delete = new Delete();
        delete.deleteObjects = new ArrayList<Delete.DeleteObject>();
        setObjectList(deleteObjectList);
    }

    public DeleteMultiObjectRequest(String bucket) {
        super(bucket, null);
        delete = new Delete();
        delete.deleteObjects = new ArrayList<Delete.DeleteObject>();
    }

    public DeleteMultiObjectRequest(){
        super(null, null);
        delete = new Delete();
        delete.deleteObjects = new ArrayList<Delete.DeleteObject>();
    }

    @Override
    public String getMethod() {
        return RequestMethod.POST;
    }

    @Override
    public Map<String, String> getQueryString() {
        queryParameters.put("delete", null);
        return queryParameters;
    }

    @Override
    public RequestBodySerializer getRequestBody() throws CosXmlClientException {
        try {
            return RequestBodySerializer.string(COSRequestHeaderKey.APPLICATION_XML, XmlBuilder.buildDelete(delete));
        } catch (XmlPullParserException e) {
            throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(), e);
        } catch (IOException e) {
            throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(), e);
        }
    }

    @Override
    public void checkParameters() throws CosXmlClientException {
        if(requestURL != null){
            return;
        }
        if(bucket == null){
            throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(), "bucket must not be null");
        }
        if( delete.deleteObjects.size() == 0)
        {
            throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(), "object（null or empty) is invalid");
        }
    }

    @Override
    public STSCredentialScope[] getSTSCredentialScope(CosXmlServiceConfig config) {
        STSCredentialScope[] scopes = new STSCredentialScope[delete.deleteObjects.size()];
        int i = 0;
        for (Delete.DeleteObject deleteObject : delete.deleteObjects) {
            scopes[i++] = new STSCredentialScope("name/cos:DeleteObject", config.getBucket(bucket),
                    config.getRegion(), deleteObject.key);
        }

        return scopes;
    }

    @Override
    public String getPath(CosXmlServiceConfig config) {
        return config.getUrlPath(bucket, "/");
    }

    /**
     * <p>
     * 设置是否为quiet模式。
     * </p>
     * <p>
     * Quiet 模式只返回报错的 Object 信息。否则返回每个 Object 的删除结果。
     * </p>
     * <p>默认false</p>
     *
     * @param quiet 设置是否为quiet模式
     */
    public void setQuiet(boolean quiet) {
        delete.quiet = quiet;
    }

    /**
     * 添加需要删除的Object
     *
     * @param object Object的路径
     */
    public void setObjectList(String object) {
        if(TextUtils.isEmpty(object))return;
        if(object != null){
            if(object.startsWith("/")){
                object = object.substring(1);
            }
            Delete.DeleteObject deleteObject = new Delete.DeleteObject();
            deleteObject.key = object;
            delete.deleteObjects.add(deleteObject);
        }
    }

    public void setObjectList(String object, String versionId) {
        if(TextUtils.isEmpty(object))return;
        if(object != null){
            if(object.startsWith("/")){
                object = object.substring(1);
            }
            Delete.DeleteObject deleteObject = new Delete.DeleteObject();
            deleteObject.key = object;
            if(versionId != null){
                deleteObject.versionId = versionId;
            }
            delete.deleteObjects.add(deleteObject);
        }
    }

    /**
     * 添加多个需要删除的Objects
     *
     * @param objectList Objects的路径列表
     */
    public void setObjectList(List<String> objectList) {
        if(objectList != null){
            int size = objectList.size();
            Delete.DeleteObject deleteObject;
            for(int i = 0; i < size; ++ i){
                deleteObject = new Delete.DeleteObject();
                String object = objectList.get(i);
                if(TextUtils.isEmpty(object))continue;
                if(object.startsWith("/")){
                    deleteObject.key = object.substring(1);
                }else{
                    deleteObject.key = object;
                }
                delete.deleteObjects.add(deleteObject);
            }
        }
    }

    public void setObjectList(Map<String, String> objectListWithVersionId) {
        if(objectListWithVersionId != null){
            Delete.DeleteObject deleteObject;
            for(Map.Entry<String, String> entry : objectListWithVersionId.entrySet()){
                deleteObject = new Delete.DeleteObject();
                String object = entry.getKey();
                String versionId = entry.getValue();
                if(TextUtils.isEmpty(object))continue;
                if(object.startsWith("/")){
                    deleteObject.key = object.substring(1);
                }else{
                    deleteObject.key = object;
                }
                if(versionId != null){
                    deleteObject.versionId = versionId;
                }
                delete.deleteObjects.add(deleteObject);
            }
        }
    }

    @Override
    public boolean isNeedMD5() {
        return true;
    }

    /**
     * 获取用户设置的需要批量删除的Objects
     *
     * @return Delete
     */
    public Delete getDelete() {
        return delete;
    }

}
