package com.tencent.qcloud.network.sonar.traceroute;

import android.os.SystemClock;
import android.text.TextUtils;

import com.tencent.qcloud.network.sonar.SonarCallback;
import com.tencent.qcloud.network.sonar.command.CommandStatus;
import com.tencent.qcloud.network.sonar.command.NetCommandTask;
import com.tencent.qcloud.network.sonar.utils.SonarLog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

final class TracerouteTask extends NetCommandTask<TracerouteNodeResult> {
    private final String targetIp;
    private final int count;
    private final int hop;

    private SonarCallback.Step<TracerouteNodeResult> stepCallback;

    TracerouteTask(String targetIp, int hop, int count, SonarCallback.Step<TracerouteNodeResult> stepCallback) {
        this.targetIp = targetIp;
        this.hop = hop;
        this.count = count;
        this.stepCallback = stepCallback;
    }

    @Override
    protected TracerouteNodeResult run() {
        isRunning = true;

        command = String.format("ping -c 1 -W 1 -t %d %s", hop, targetIp);

        int currentCount = 0;

        List<SingleNodeResult> singleNodeList = new ArrayList<>();
        while (isRunning && (currentCount < count)) {
            try {
                long startTime = SystemClock.elapsedRealtime();
                String cmdRes = execCommand(command);
                int delay = (int) (SystemClock.elapsedRealtime() - startTime);

                /**
                 * 耗时滤波规则：
                 * 1、临时性能耗时 = 计算的平均cmd性能耗时
                 * 2、若：此次的cmd执行耗时 - 计算的平均cmd性能耗时 < 此次耗时的 10%，并且 临时性能耗时 > 计算的平均cmd性能耗时的 10%
                 *   则：临时性能耗时 自减 20%
                 * 3、最终延迟 = 此次的cmd执行耗时 - 临时性能耗时
                 */
                float tmpElapsed = COMMAND_ELAPSED_TIME;
                while ((delay - tmpElapsed) < (delay * 0.1) && tmpElapsed > (COMMAND_ELAPSED_TIME * 0.1f))
                    tmpElapsed *= 0.8;

                SonarLog.d(TAG, String.format("[traceroute delay]:%d [COMMAND_ELAPSED_TIME]:%f [tmpElapsed]%f",
                        delay, COMMAND_ELAPSED_TIME, tmpElapsed));
                delay -= tmpElapsed;

                SingleNodeResult nodeResult = parseSingleNodeInfoInput(cmdRes);
                if (!nodeResult.isFinalRoute()
                        && nodeResult.getStatus() == CommandStatus.CMD_STATUS_SUCCESSFUL)
                    nodeResult.setDelay(delay);

                singleNodeList.add(nodeResult);
            } catch (IOException | InterruptedException e) {
                SonarLog.d(TAG, String.format("traceroute[%d]: %s occur error: %s", currentCount, command, e.getMessage()));
            } finally {
                currentCount++;
            }
        }

        resultData = new TracerouteNodeResult(targetIp, hop, singleNodeList);
        if (stepCallback != null) {
            stepCallback.step(resultData);
        }
        return isRunning ? resultData : null;
    }

    protected SingleNodeResult parseSingleNodeInfoInput(String input) {
        SonarLog.d(TAG, "[hop]:" + hop + " [org data]:" + input);
        SingleNodeResult nodeResult = new SingleNodeResult(targetIp, hop);

        if (TextUtils.isEmpty(input)) {
            nodeResult.setStatus(CommandStatus.CMD_STATUS_NETWORK_ERROR);
            nodeResult.setDelay(0.f);
            return nodeResult;
        }

        Matcher matcherRouteNode = matcherRouteNode(input);

        if (matcherRouteNode.find()) {
            nodeResult.setRouteIp(getIpFromMatcher(matcherRouteNode));
            nodeResult.setStatus(CommandStatus.CMD_STATUS_SUCCESSFUL);
        } else {
            Matcher matcherTargetId = matcherIp(input);
            if (matcherTargetId.find()) {
                nodeResult.setRouteIp(matcherTargetId.group());
                nodeResult.setStatus(CommandStatus.CMD_STATUS_SUCCESSFUL);
                String time = getPingDelayFromMatcher(matcherTime(input));
                nodeResult.setDelay(Float.parseFloat(time));
            } else {
                nodeResult.setStatus(CommandStatus.CMD_STATUS_FAILED);
                nodeResult.setDelay(0.f);
            }
        }

        return nodeResult;
    }

    @Override
    protected void parseInputInfo(String input) {

    }

    @Override
    protected void parseErrorInfo(String error) {
        if (!TextUtils.isEmpty(error)) {
            SonarLog.d(TAG, "[hop]:" + hop + " [error data]:" + error);
        }
    }

    @Override
    protected void stop() {
        isRunning = false;
    }
}