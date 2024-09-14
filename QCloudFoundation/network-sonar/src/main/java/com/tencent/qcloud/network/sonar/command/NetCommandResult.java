package com.tencent.qcloud.network.sonar.command;

import com.tencent.qcloud.network.sonar.utils.SonarLog;

import org.json.JSONException;
import org.json.JSONObject;

public class NetCommandResult extends CommandResult {
    protected String targetIp;

    protected NetCommandResult(String targetIp) {
        this.targetIp = targetIp;
    }

    public String getTargetIp() {
        return targetIp;
    }

    protected NetCommandResult setTargetIp(String targetIp) {
        this.targetIp = targetIp;
        return this;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = super.toJson();
        try {
            json.put("targetIp", targetIp);
        } catch (JSONException e) {
            if(SonarLog.openLog){
                e.printStackTrace();
            }
        }
        return json;
    }
}
