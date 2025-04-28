/*
 * Copyright (c) 2010-2020 Tencent Cloud. All rights reserved.
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */

package com.tencent.qcloud.core.task;

import com.tencent.qcloud.core.logger.COSLogger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
        COSLogger.dProcess(TASK_LOG_TAG, "[Pool] ADD %s, %d cached", task.getIdentifier(), taskPool.size());
    }

    void remove(QCloudTask task) {
        if (taskPool.remove(task.getIdentifier()) != null) {
            COSLogger.dProcess(TASK_LOG_TAG, "[Pool] REMOVE %s, %d cached",
                    task.getIdentifier(), taskPool.size());
        }
    }

    public int getPoolCount(){
        return taskPool.size();
    }

    public QCloudTask get(String identifier) {
        return taskPool.get(identifier);
    }

    public List<QCloudTask> snapshot() {
        return new ArrayList<>(taskPool.values());
    }

    void evict() {
        COSLogger.dProcess(TASK_LOG_TAG, "[Pool] CLEAR %d", taskPool.size());
        taskPool.clear();
    }
}
