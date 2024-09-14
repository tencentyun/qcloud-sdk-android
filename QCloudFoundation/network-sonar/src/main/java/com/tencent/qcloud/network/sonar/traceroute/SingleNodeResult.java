package com.tencent.qcloud.network.sonar.traceroute;

import android.text.TextUtils;

import com.tencent.qcloud.network.sonar.command.CommandStatus;
import com.tencent.qcloud.network.sonar.command.NetCommandResult;
import com.tencent.qcloud.network.sonar.utils.SonarLog;

import org.json.JSONException;
import org.json.JSONObject;

public class SingleNodeResult extends NetCommandResult {
    private int hop;
    private String routeIp;
    private boolean isFinalRoute;
    protected float delay;

    protected SingleNodeResult(String targetIp, int hop) {
        super(targetIp);
        this.hop = hop;
        setRouteIp("*");
        delay = 0.f;
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

    public void setDelay(float delay) {
        this.delay = delay;
    }

    public float getDelay() {
        return delay;
    }

    SingleNodeResult setHop(int hop) {
        this.hop = hop;
        return this;
    }

    SingleNodeResult setRouteIp(String routeIp) {
        this.routeIp = routeIp;
        isFinalRoute = TextUtils.equals(targetIp, routeIp);
        return this;
    }

    SingleNodeResult setFinalRoute(boolean isFinalRoute) {
        this.isFinalRoute = isFinalRoute;
        return this;
    }

    SingleNodeResult setStatus(CommandStatus status) {
        this.status = status;
        return this;
    }

    @Override
    public String toString() {
        return toJson().toString();
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = super.toJson();
        try {
            json.put("hop", hop);
            json.put("route_ip", routeIp);
            json.put("delay", String.format("%.2f", delay));
            json.put("is_final_route", isFinalRoute);
        } catch (JSONException e) {
            if(SonarLog.openLog){
                e.printStackTrace();
            }
        }
        return json;
    }
}
