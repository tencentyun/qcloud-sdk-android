package com.tencent.qcloud.network.sonar.traceroute;

import android.text.TextUtils;

import com.tencent.qcloud.network.sonar.command.CommandStatus;
import com.tencent.qcloud.network.sonar.command.NetCommandResult;
import com.tencent.qcloud.network.sonar.utils.SonarLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class TracerouteNodeResult extends NetCommandResult {
    private int hop;
    private String routeIp;
    private boolean isFinalRoute;
    private List<SingleNodeResult> singleNodeList;

    protected TracerouteNodeResult(String targetIp, int hop, List<SingleNodeResult> singleNodeList) {
        super(targetIp);
        this.hop = hop;
        isFinalRoute = false;
        routeIp = "*";
        setSingleNodeList(singleNodeList);
    }

    public int getHop() {
        return hop;
    }

    public String getRouteIp() {
        return routeIp;
    }

    public boolean isFinalRoute() {
        return isFinalRoute;
    }

    TracerouteNodeResult setHop(int hop) {
        this.hop = hop;
        return this;
    }

    TracerouteNodeResult setRouteIp(String routeIp) {
        this.routeIp = routeIp;
        isFinalRoute = TextUtils.equals(targetIp, routeIp);
        return this;
    }

    TracerouteNodeResult setFinalRoute(boolean isFinalRoute) {
        this.isFinalRoute = isFinalRoute;
        return this;
    }

    public List<SingleNodeResult> getSingleNodeList() {
        return singleNodeList;
    }

    void setSingleNodeList(List<SingleNodeResult> singleNodeList) {
        this.singleNodeList = singleNodeList;
        if (this.singleNodeList == null)
            return;

        for (SingleNodeResult node : this.singleNodeList) {
            if (!TextUtils.equals("*", node.getRouteIp())) {
                setRouteIp(node.getRouteIp());
                break;
            }
        }
    }

    public int averageDelay() {
        if (singleNodeList == null || singleNodeList.isEmpty())
            return 0;

        int count = 0;
        float total = 0.f;
        for (SingleNodeResult node : singleNodeList) {
            if (node == null)
                continue;
            if (node.delay <= 0.f)
                continue;

            count++;
            total += node.delay;
        }

        return Math.round(total / count);
    }

    public float lossRate() {
        if (singleNodeList == null || singleNodeList.isEmpty())
            return 1;

        int loss = 0;
        float total = singleNodeList.size();
        for (SingleNodeResult node : singleNodeList) {
            if (node == null || node.getStatus() != CommandStatus.CMD_STATUS_SUCCESSFUL || node.delay == 0.f)
                loss++;
        }
        return (float) loss / total;
    }

    @Override
    public String toString() {
        return toJson().toString();
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = super.toJson();
//        JSONArray jarr = new JSONArray();
//        if (singleNodeList != null && !singleNodeList.isEmpty()) {
//            for (SingleNodeResult result : singleNodeList) {
//                if (result == null || result.toJson().length() == 0)
//                    continue;
//
//                jarr.put(result.toJson());
//            }
//        }
        try {
            json.put("hop", hop);
            json.put("route_ip", routeIp);
            json.put("avg_delay", averageDelay());
            json.put("loss", String.format("%.2f", lossRate()));
            json.put("is_final_route", isFinalRoute);
//            json.put("single_node_list", jarr);
        } catch (JSONException e) {
            if(SonarLog.openLog){
                e.printStackTrace();
            }
        }
        return json;
    }
}
