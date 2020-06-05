package com.tencent.cos.xml.model.tag.pic;

import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class PicOperationRule {

    private String bucket;

    private String fileId;

    private String rule;


    public PicOperationRule(String fileId, String rule) {
        this.fileId = fileId;
        this.rule = rule;
    }

    public PicOperationRule(String bucket, String fileId, String rule) {
        this.bucket = bucket;
        this.fileId = fileId;
        this.rule = rule;
    }


    public JSONObject toJsonObject() {

        JSONObject ruleJson = new JSONObject();
        try {
            if (!TextUtils.isEmpty(bucket)) {
                ruleJson.put("bucket", bucket);
            }
            ruleJson.put("fileid", fileId);
            ruleJson.put("rule", rule);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ruleJson;
    }
}
