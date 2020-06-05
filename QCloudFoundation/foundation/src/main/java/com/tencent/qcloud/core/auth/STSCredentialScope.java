package com.tencent.qcloud.core.auth;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * <p>
 * </p>
 * Created by wjielai on 2018/12/3.
 * Copyright 2010-2017 Tencent Cloud. All Rights Reserved.
 */
public class STSCredentialScope {

    public final String action;
    public final String bucket;
    public final String prefix;
    public final String region;

    static final STSCredentialScope NONE = new STSCredentialScope(
            null, null, null, null
    );

    public STSCredentialScope(String action, String bucket, String region, String prefix) {
        this.action = action;
        this.bucket = bucket;
        this.region = region;
        if (prefix != null && prefix.charAt(0) == '/') {
            this.prefix = prefix.substring(1);
        } else {
            this.prefix = prefix;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof STSCredentialScope)) {
            return false;
        }
        STSCredentialScope another = (STSCredentialScope) obj;
        return TextUtils.equals(action, another.action) &&
                TextUtils.equals(bucket, another.bucket) &&
                TextUtils.equals(prefix, another.prefix) &&
                TextUtils.equals(region, another.region);

    }

    public STSCredentialScope[] toArray() {
        return toArray(this);
    }

    public static STSCredentialScope[] toArray(STSCredentialScope... scopes) {
        return scopes;
    }

    public static String jsonify(STSCredentialScope[] scopes) {
        JSONArray scopeArray = new JSONArray();
        for (STSCredentialScope scope : scopes) {
            try {
                JSONObject scopeObject = new JSONObject();
                scopeObject.put("action", scope.action);
                scopeObject.put("bucket", scope.bucket);
                scopeObject.put("prefix", scope.prefix);
                scopeObject.put("region", scope.region);
                scopeArray.put(scopeObject);
            } catch (JSONException e) {
                //ignore
            }
        }
        return scopeArray.toString();
    }

}
