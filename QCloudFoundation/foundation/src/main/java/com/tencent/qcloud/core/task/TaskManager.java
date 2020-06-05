package com.tencent.qcloud.core.task;

import android.util.Log;
import com.tencent.qcloud.core.logger.QCloudLogger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 *
 * Created by wjielai on 2017/11/27.
 * Copyright 2010-2017 Tencent Cloud. All Rights Reserved.
 */

public final class TaskManager {

    static final String TASK_LOG_TAG = "QCloudTask";

    private Map<String, QCloudTask> taskPool;

    private static volatile TaskManager instance;

    public static TaskManager getInstance() {
        if (instance == null) {
            synchronized (TaskManager.class) {
                if (instance == null) {
                    instance = new TaskManager();
                }
            }
        }

        return instance;
    }

    private TaskManager() {
        taskPool = new ConcurrentHashMap<>(30);
    }

    void add(QCloudTask task) {
        taskPool.put(task.getIdentifier(), task);
        QCloudLogger.d(TASK_LOG_TAG, "[Pool] ADD %s, %d cached", task.getIdentifier(), taskPool.size());
    }

    void remove(QCloudTask task) {
        if (taskPool.remove(task.getIdentifier()) != null) {
            QCloudLogger.d(TASK_LOG_TAG, "[Pool] REMOVE %s, %d cached",
                    task.getIdentifier(), taskPool.size());
        }
    }

    public QCloudTask get(String identifier) {
        return taskPool.get(identifier);
    }

    public List<QCloudTask> snapshot() {
        return new ArrayList<>(taskPool.values());
    }

    void evict() {
        QCloudLogger.d(TASK_LOG_TAG, "[Pool] CLEAR %d", taskPool.size());
        taskPool.clear();
    }
}
