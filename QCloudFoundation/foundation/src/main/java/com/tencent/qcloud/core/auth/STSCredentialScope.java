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

package com.tencent.qcloud.core.auth;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * STS范围限制信息
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
