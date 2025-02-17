package com.tencent.qcloud.network.sonar.ping;

import android.text.TextUtils;

import com.tencent.qcloud.network.sonar.Sonar;
import com.tencent.qcloud.network.sonar.SonarCallback;
import com.tencent.qcloud.network.sonar.SonarRequest;
import com.tencent.qcloud.network.sonar.SonarResult;
import com.tencent.qcloud.network.sonar.SonarType;
import com.tencent.qcloud.network.sonar.utils.SonarLog;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Locale;

/**
 * <p>
 * Created by jordanqin on 2024/8/19 11:04.
 * Copyright 2010-2020 Tencent Cloud. All Rights Reserved.
 */
public class PingSonar implements Sonar<PingResult> {
    private final int count = 10;
    private final int size = 56;
    private final int interval = 200;
    public SonarCallback.Step<String> stepCallback;

    @Override
    public SonarResult<PingResult> start(SonarRequest request) {
        if(!request.isNetworkAvailable()){
            return new SonarResult<>(SonarType.PING, new Exception(Sonar.ERROR_MSG_NO_NETWORK));
        }

        if(TextUtils.isEmpty(request.getIp())){
            return new SonarResult<>(SonarType.PING, new Exception(Sonar.ERROR_MSG_IP_IS_EMPTY));
        }

        long startTime = System.currentTimeMillis();
        String cmdPing = "ping";
        if (null!=request.getIp() && request.getIp().contains(":")) {
            cmdPing = "ping6";
        }
        String cmd = String.format(Locale.getDefault(), "%s -n -i %f -s %d -c %d %s", cmdPing, ((double) interval / 1000), size, count, request.getIp());
        Process process = null;
        StringBuilder str = new StringBuilder();
        BufferedReader reader = null;
        BufferedReader errorReader = null;
        try {
            process = Runtime.getRuntime().exec(cmd);
            if (null == process) {
                return new SonarResult<>(SonarType.PING, new Exception(Sonar.ERROR_MSG_PING_PROCESS_IS_NULL));
            }
            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            while ((line = reader.readLine()) != null) {
                str.append(line).append("\n");
                if (null != stepCallback) {
                    stepCallback.step(line);
                }

            }
            while ((line = errorReader.readLine()) != null) {
                str.append(line);
                if (null != stepCallback) {
                    stepCallback.step(line);
                }
            }
            reader.close();
            errorReader.close();
            process.waitFor();

        } catch (IOException e) {
            if (null != stepCallback) {
                stepCallback.step(e.getMessage());
            } else {
                if(SonarLog.openLog){
                    e.printStackTrace();
                }
            }
        } catch (InterruptedException e) {
            if (null != stepCallback) {
                stepCallback.step(e.getMessage());
            } else {
                if(SonarLog.openLog){
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            if (null != stepCallback) {
                stepCallback.step(e.getMessage());
            } else {
                if(SonarLog.openLog){
                    e.printStackTrace();
                }
            }
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
                if (process != null) {
                    process.destroy();
                }
            } catch (Exception e) {
                if (null != stepCallback) {
                    stepCallback.step(e.getMessage());
                } else {
                    if(SonarLog.openLog){
                        e.printStackTrace();
                    }
                }
            }
        }
        PingResult pingResult = new PingResult(str.toString(), request.getHost(), request.getIp(), size, interval);
        pingResult.timeConsuming = System.currentTimeMillis() - startTime;
        return new SonarResult<>(SonarType.PING, pingResult);
    }

    @Override
    public void stop() {

    }
}
