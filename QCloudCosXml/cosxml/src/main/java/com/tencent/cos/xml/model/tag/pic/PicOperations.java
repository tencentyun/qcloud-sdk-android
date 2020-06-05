package com.tencent.cos.xml.model.tag.pic;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class PicOperations {

    private boolean isPicInfo;

    private List<PicOperationRule> rules;

    public PicOperations(boolean isPicInfo, List<PicOperationRule> rules) {
        this.isPicInfo = isPicInfo;
        this.rules = rules;
    }

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
