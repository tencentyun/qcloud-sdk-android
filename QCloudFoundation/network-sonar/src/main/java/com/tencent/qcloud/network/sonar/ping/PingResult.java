package com.tencent.qcloud.network.sonar.ping;

import com.tencent.qcloud.network.sonar.utils.SonarLog;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * <p>
 * Created by jordanqin on 2024/8/19 11:03.
 * Copyright 2010-2020 Tencent Cloud. All Rights Reserved.
 */
public class PingResult {
    public final String result;
    public final String host;
    public final String ip;
    public final int size;
    public final int interval;
    private final String lastLinePrefix = "rtt min/avg/max/mdev = ";
    private final String packetWords = " packets transmitted";
    private final String receivedWords = " received";
    public int sent;
    public int dropped;
    public float max;
    public float min;
    public float avg;
    public float stddev;
    public int count;
    public long timeConsuming;

    PingResult(String result, String host, String ip, int size, int interval) {
        this.result = result;
        this.ip = ip;
        this.size = size;
        this.interval = interval;
        this.host = host;
        parseResult();
    }

    static String trimNoneDigital(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char[] v = s.toCharArray();
        char[] v2 = new char[v.length];
        int j = 0;
        for (char aV : v) {
            if ((aV >= '0' && aV <= '9') || aV == '.') {
                v2[j++] = aV;
            }
        }
        return new String(v2, 0, j);
    }

    private void parseRttLine(String s) {
        String s2 = s.substring(lastLinePrefix.length(), s.length() - 3);
        String[] l = s2.split("/");
        if (l.length != 4) {
            return;
        }
        min = Float.parseFloat(trimNoneDigital(l[0]));
        avg = Float.parseFloat(trimNoneDigital(l[1]));
        max = Float.parseFloat(trimNoneDigital(l[2]));
        stddev = Float.parseFloat(trimNoneDigital(l[3]));
    }

    private void parsePacketLine(String s) {
        String[] l = s.split(",");
        if (l.length != 4) {
            return;
        }
        if (l[0].length() > packetWords.length()) {
            String s2 = l[0].substring(0, l[0].length() - packetWords.length());
            count = Integer.parseInt(s2);
        }
        if (l[1].length() > receivedWords.length()) {
            String s3 = l[1].substring(0, l[1].length() - receivedWords.length());
            sent = Integer.parseInt(s3.trim());
        }
        dropped = count - sent;
    }

    private void parseResult() {
        String[] rs = result.split("\n");
        try {
            for (String s : rs) {
                if (s.contains(packetWords)) {
                    parsePacketLine(s);
                } else if (s.contains(lastLinePrefix)) {
                    parseRttLine(s);
                }
            }
        } catch (Exception e) {
            if(SonarLog.openLog){
                e.printStackTrace();
            }
        }
    }

    public int getResponseNum() {
        return this.sent;
    }

    public float getLoss() {
        if (0 == this.count) {
            return 1;
        } else {
            return (float) this.dropped / (float) this.count;
        }
    }

    public String encode() {
        JSONObject o = new JSONObject();
        try {
            o.put("method", "ping");
            o.put("ip", this.ip);
            o.put("host", this.host);
            o.put("max", String.format("%.2f", this.max));
            o.put("min", String.format("%.2f", this.min));
            o.put("avg", String.format("%.2f", this.avg));
            o.put("stddev", String.format("%.2f", this.stddev));
            if (0 == this.count) {
                o.put("loss", "1");
            } else {
                o.put("loss", String.format("%.2f", Float.valueOf(this.dropped) / Float.valueOf(this.count)));
            }
            o.put("count", this.count);
            o.put("size", this.size);
            o.put("responseNum", this.sent);
            o.put("interval", this.interval);
            o.put("timestamp",  System.currentTimeMillis() / 1000);
            o.put("timeConsuming", this.timeConsuming);
            return o.toString();
        } catch (JSONException err) {
            if(SonarLog.openLog){
                err.printStackTrace();
            }
            return null;
        }
    }

    @Override
    public String toString() {
        return encode();
    }
}
