package com.tencent.qcloud.network.sonar.traceroute;

import com.tencent.qcloud.network.sonar.command.CommandStatus;
import com.tencent.qcloud.network.sonar.command.JsonSerializable;
import com.tencent.qcloud.network.sonar.utils.SonarLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TracerouteResult implements JsonSerializable {
    private String targetIp;
    private List<TracerouteNodeResult> tracerouteNodeResults;
    private long timestamp;
    private CommandStatus status;
    private String host;
    private long timeConsuming;

    public TracerouteResult(String targetIp, long timestamp, CommandStatus status, String host, long timeConsuming) {
        this.targetIp = targetIp;
        this.timestamp = timestamp;
        this.status = status;
        this.host = host;
        this.timeConsuming = timeConsuming;
        tracerouteNodeResults = new ArrayList<>();
    }

    public String getTargetIp() {
        return targetIp;
    }

    public List<TracerouteNodeResult> getTracerouteNodeResults() {
        return tracerouteNodeResults;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public CommandStatus getCommandStatus() {
        return status;
    }

    public long getTimeConsuming() {
        return timeConsuming;
    }

    public int getHopCount() {
        if(tracerouteNodeResults != null){
            return tracerouteNodeResults.size();
        } else {
            return 0;
        }
    }

    public int getTotalDelay() {
        if(tracerouteNodeResults != null){
            int total = 0;
            for (TracerouteNodeResult node : tracerouteNodeResults) {
                if (node == null)
                    continue;
                if (node.averageDelay() <= 0.f)
                    continue;
                total += node.averageDelay();
            }
            return total;
        } else {
            return 0;
        }
    }

    public float getLossRate() {
        if(tracerouteNodeResults != null){
            int count = 0;
            float total = 0.f;
            for (TracerouteNodeResult node : tracerouteNodeResults) {
                if (node == null)
                    continue;
                count++;
                total += node.lossRate();
            }

            return Math.round(total / count);
        } else {
            return 1;
        }
    }

    public String getNodeResultsString() {
        JSONObject json = new JSONObject();
        JSONArray jarr = new JSONArray();
        if (tracerouteNodeResults != null && !tracerouteNodeResults.isEmpty()) {
            for (TracerouteNodeResult result : tracerouteNodeResults) {
                if (result == null || result.toJson().length() == 0)
                    continue;

                jarr.put(result.toJson());
            }
        }
        try {
            json.put("traceroute_node_results", jarr);
        } catch (JSONException e) {
            if(SonarLog.openLog){
                e.printStackTrace();
            }
        }

        return json.toString();
    }


    @Override
    public String toString() {
        return toJson().toString();
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        JSONArray jarr = new JSONArray();
        if (tracerouteNodeResults != null && !tracerouteNodeResults.isEmpty()) {
            for (TracerouteNodeResult result : tracerouteNodeResults) {
                if (result == null || result.toJson().length() == 0)
                    continue;

                jarr.put(result.toJson());
            }
        }
        try {
            json.put("host", host);
            json.put("host_ip", targetIp);
            json.put("timestamp", timestamp);
            json.put("command_status", status.getName().toString());
            json.put("hopCount", getHopCount());
            json.put("totalDelay", getTotalDelay());
            json.put("lossRate", getLossRate());
            json.put("timeConsuming", timeConsuming);
            json.put("traceroute_node_results", jarr);
        } catch (JSONException e) {
            if(SonarLog.openLog){
                e.printStackTrace();
            }
        }

        return json;
    }
}
