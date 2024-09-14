package com.tencent.qcloud.network.sonar.command;

public enum CommandStatus {
    CMD_STATUS_SUCCESSFUL("success"),
    CMD_STATUS_FAILED("failed"),
    CMD_STATUS_USER_STOP("user_stop"),
    CMD_STATUS_ERROR("error"),
    CMD_STATUS_NETWORK_ERROR("network_error"),
    CMD_STATUS_ERROR_UNKNOW_HOST("unkonown_host");

    String name;
    CommandStatus(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

