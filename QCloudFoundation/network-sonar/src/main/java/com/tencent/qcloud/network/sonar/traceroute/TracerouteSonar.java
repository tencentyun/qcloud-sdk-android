package com.tencent.qcloud.network.sonar.traceroute;

import android.os.SystemClock;
import android.text.TextUtils;

import com.tencent.qcloud.network.sonar.Sonar;
import com.tencent.qcloud.network.sonar.SonarCallback;
import com.tencent.qcloud.network.sonar.SonarRequest;
import com.tencent.qcloud.network.sonar.SonarResult;
import com.tencent.qcloud.network.sonar.SonarType;
import com.tencent.qcloud.network.sonar.command.CommandStatus;
import com.tencent.qcloud.network.sonar.utils.SonarLog;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Created by jordanqin on 2024/8/19 11:04.
 * Copyright 2010-2020 Tencent Cloud. All Rights Reserved.
 */
public class TracerouteSonar implements Sonar<TracerouteResult> {
    protected final String TAG = getClass().getSimpleName();
    private final TracerouteSonar.Config config = new TracerouteSonar.Config();

    private SonarCallback.Step<TracerouteNodeResult> stepCallback;

    private boolean isUserStop = false;
    private TracerouteTask task;

    @Override
    public SonarResult<TracerouteResult> start(SonarRequest request) {
        if(request.getIp() == null){
            return new SonarResult<>(SonarType.TRACEROUTE, new Exception("request ip is null"));
        }

        long startTime = System.currentTimeMillis();
        SonarLog.d(TAG,  "run thread:" + Thread.currentThread().getId() + " name:" + Thread.currentThread().getName());
        isUserStop = false;
        List<TracerouteNodeResult> nodeResults = new ArrayList<>();
        int countUnreachable = 0;
        long timestamp = System.currentTimeMillis() / 1000;
        long start = SystemClock.elapsedRealtime();
        for (int i = 1; i <= config.maxHop && !isUserStop; i++) {
            task = new TracerouteTask(request.getIp(), i, config.countPerRoute, stepCallback);
            TracerouteNodeResult node = task.run();
            SonarLog.d(TAG, String.format("[thread]:%d, [trace node]:%s",
                    Thread.currentThread().getId(),
                    (node == null ? "null" : node.toString()))
            );
            if (node == null) {
                continue;
            }
            nodeResults.add(node);
            if (node.isFinalRoute())
                break;

            if (TextUtils.equals("*", node.getRouteIp())) {
                countUnreachable++;
            } else {
                countUnreachable = 0;
            }

            if (countUnreachable == 5) {
                break;
            }
        }

        TracerouteResult result = new TracerouteResult(
                request.getIp(),
                timestamp,
                isUserStop ? CommandStatus.CMD_STATUS_USER_STOP : CommandStatus.CMD_STATUS_SUCCESSFUL,
                request.getHost(),
                System.currentTimeMillis() - startTime
        );
        result.getTracerouteNodeResults().addAll(nodeResults);
        return new SonarResult<>(SonarType.TRACEROUTE, result);
    }

    @Override
    public void stop() {
        isUserStop = true;
        if (task != null)
            task.stop();
    }

    public static class Config {
        private int maxHop;
        private int countPerRoute;

        public Config() {
            this.maxHop = 32;
            this.countPerRoute = 3;
        }

        public int getMaxHop() {
            return maxHop;
        }

        public Config setMaxHop(int maxHop) {
            this.maxHop = Math.max(1, Math.min(maxHop, 128));
            return this;
        }

        public int getCountPerRoute() {
            return countPerRoute;
        }

        public Config setCountPerRoute(int countPerRoute) {
            this.countPerRoute = Math.max(1, Math.min(countPerRoute, 3));
            return this;
        }
    }
}
