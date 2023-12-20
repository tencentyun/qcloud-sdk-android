package com.tencent.qcloud.track;

import android.util.Log;

import com.tencent.qcloud.track.cls.ClsAuthenticationException;
import com.tencent.qcloud.track.cls.ClsLifecycleCredentialProvider;
import com.tencent.qcloud.track.cls.ClsSessionCredentials;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Created by jordanqin on 2023/11/13 17:20.
 * Copyright 2010-2020 Tencent Cloud. All Rights Reserved.
 */
public class MyClsLifecycleCredentialProvider extends ClsLifecycleCredentialProvider {
    @Override
    protected ClsSessionCredentials fetchNewCredentials() throws ClsAuthenticationException {
        // 首先从您的临时密钥服务器获取包含了密钥信息的响应
        HttpURLConnection connection = null;
        try {
            URL url = new URL("http://9.135.00.00:3000/sts/cls");
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                throw new ClsAuthenticationException("Unexpected code " + responseCode);
            }

            Map<String, List<String>> responseHeaders = connection.getHeaderFields();
            for (Map.Entry<String, List<String>> entry : responseHeaders.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }

            InputStream inputStream = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            String jsonStr = stringBuilder.toString();
            Log.d("MySessionCredentialProvider", jsonStr);

            JSONTokener jsonParser = new JSONTokener(jsonStr);
            JSONObject data = (JSONObject) jsonParser.nextValue();
            JSONObject credentials = data.getJSONObject("credentials");
            return new ClsSessionCredentials(credentials.getString("tmpSecretId"), credentials.getString("tmpSecretKey"),
                    credentials.getString("sessionToken"), data.getLong("expiredTime"));
        } catch (IOException | JSONException e) {
            throw new ClsAuthenticationException(e.toString());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}
