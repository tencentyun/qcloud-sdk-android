package com.tencent.cos.xml.model.object;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.cos.xml.model.tag.pic.PicObject;
import com.tencent.cos.xml.model.tag.pic.PicOriginalInfo;
import com.tencent.cos.xml.model.tag.pic.PicUploadResult;
import com.tencent.qcloud.core.http.HttpResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * 简单上传返回的结果.
 * 关于简单上传接口的描述，请查看 <a href="https://cloud.tencent.com/document/product/436/7749">
 * https://cloud.tencent.com/document/product/436/7749.</a><br>
 */
final public class PutObjectResult extends CosXmlResult {

    /**
     * 返回文件的 MD5 算法校验值.eTag 的值可以用于检查 Object 在上传过程中是否有损坏
     */
    public String eTag;

    private String body;

    /**
     *  @see CosXmlResult#parseResponseBody(HttpResponse)
     */
    @Override
    public void parseResponseBody(HttpResponse response) throws CosXmlServiceException, CosXmlClientException {
        super.parseResponseBody(response);
        eTag = response.header("ETag");
        try {
            body = response.string();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public @Nullable PicUploadResult picUploadResult() {

        if (TextUtils.isEmpty(body)) {
            return null;
        }

        try {
            return parseUploadResult(new JSONObject(body));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private PicUploadResult parseUploadResult(JSONObject uploadResult) throws JSONException {

        if (uploadResult == null) {
            return null;
        }
        PicOriginalInfo originalInfo = parseOriginalInfo(uploadResult.getJSONObject("OriginalInfo"));
        List<PicObject> processResults = parseProcessResults(uploadResult.getJSONArray("ProcessResults"));
        return new PicUploadResult(originalInfo, processResults);
    }

    private PicOriginalInfo parseOriginalInfo(JSONObject originalInfo) {

        if (originalInfo == null) {
            return null;
        }

        try {
            String key = originalInfo.getString("Key");
            String location = originalInfo.getString("Location");
            return new PicOriginalInfo(key, location);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    private List<PicObject> parseProcessResults(JSONArray processResults) {

        if (processResults == null) {
            return null;
        }

        List<PicObject> objects = new LinkedList<>();

        for (int i = 0; i < processResults.length(); i++) {

            try {
                JSONObject result = processResults.getJSONObject(0);
                String key = result.getString("Key");
                String location = result.getString("Location");
                String format = result.getString("Format");
                int width = result.getInt("Width");
                int height = result.getInt("Height");
                int size = result.getInt("Size");
                int quality = result.getInt("Quality");
                objects.add(new PicObject(key, location, format, width, height, size, quality));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return objects;
    }
}
