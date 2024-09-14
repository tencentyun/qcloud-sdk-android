package com.tencent.qcloud.network.sonar.command;

import com.tencent.qcloud.network.sonar.utils.SonarLog;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class CommandResult implements JsonSerializable {
    protected CommandStatus status;

    public CommandStatus getStatus() {
        return status;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        try {
            json.put("status", status == null ? null : status.name());
        } catch (JSONException e) {
            if(SonarLog.openLog){
                e.printStackTrace();
            }
        }

        return json;
    }
}