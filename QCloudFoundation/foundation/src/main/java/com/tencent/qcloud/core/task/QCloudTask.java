package com.tencent.qcloud.core.task;

import android.support.annotation.NonNull;

import com.tencent.qcloud.core.common.QCloudClientException;
import com.tencent.qcloud.core.common.QCloudProgressListener;
import com.tencent.qcloud.core.common.QCloudResultListener;
import com.tencent.qcloud.core.common.QCloudServiceException;
import com.tencent.qcloud.core.common.QCloudTaskStateListener;
import com.tencent.qcloud.core.logger.QCloudLogger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;

import bolts.CancellationToken;
import bolts.CancellationTokenSource;
import bolts.Continuation;
import bolts.ExecutorException;
import bolts.Task;

import static com.tencent.qcloud.core.task.TaskManager.TASK_LOG_TAG;

/**
 * <p>
 * </p>
 * Created by wjielai on 2017/11/27.
 * Copyright 2010-2017 Tencent Cloud. All Rights Reserved.
 */

public abstract class QCloudTask<T> implements Callable<T> {

    // 任务正在排队
    public static final int STATE_QUEUEING = 1;
    // 任务正在执行
    public static final int STATE_EXECUTING = 2;
    // 任务执行结束
    public static final int STATE_COMPLETE = 3;

    public static final int PRIORITY_LOW = 1;
    protected static final int PRIORITY_NORMAL = 2;
    public static final int PRIORITY_HIGH = 3;

    public static final int WEIGHT_LOW = 0;
    public static final int WEIGHT_NORMAL = 1;
    public static final int WEIGHT_HIGH = 2;

    private final String identifier;
    private final Object tag;

    private TaskManager taskManager;

    private Task<T> mTask;
    private CancellationTokenSource mCancellationTokenSource;
    private int mState;

    private int weight = WEIGHT_LOW; //

    private Executor observerExecutor;
    private Executor workerExecutor;

    private Set<QCloudResultListener<T>> mResultListeners = new HashSet<>(2);
    private Set<QCloudProgressListener> mProgressListeners = new HashSet<>(2);
    private Set<QCloudTaskStateListener> mStateListeners = new HashSet<>(2);

    private OnRequestWeightListener onRequestWeightListener;

    public QCloudTask(String identifier, Object tag) {
        this.identifier = identifier;
        this.tag = tag;
        taskManager = TaskManager.getInstance();
    }

    public final Task<T> cast() {
        return mTask;
    }

    public final T executeNow() throws QCloudClientException, QCloudServiceException {
        executeNowSilently();

        Exception exception = getException();
        if (exception != null) {
            if (exception instanceof QCloudClientException) {
                throw (QCloudClientException) exception;
            } else if (exception instanceof QCloudServiceException) {
                throw (QCloudServiceException) exception;
            } else {
                throw new QCloudClientException(exception);
            }
        }

        return getResult();
    }

    public final void executeNowSilently() {
        taskManager.add(this);
        onStateChanged(STATE_QUEUEING);
        mTask = Task.call(this);
    }

    protected QCloudTask<T> scheduleOn(Executor executor,
                                       CancellationTokenSource cancellationTokenSource) {
        return scheduleOn(executor, cancellationTokenSource, PRIORITY_NORMAL);
    }

    protected QCloudTask<T> scheduleOn(Executor executor,
                                       CancellationTokenSource cancellationTokenSource,
                                       int priority) {
        taskManager.add(this);
        onStateChanged(STATE_QUEUEING);
        workerExecutor = executor;
        mCancellationTokenSource = cancellationTokenSource;
        if (priority <= 0) {
            priority = PRIORITY_NORMAL;
        }

        mTask = callTask(this, executor, mCancellationTokenSource != null ?
                mCancellationTokenSource.getToken() : null, priority);
        mTask.continueWithTask(new Continuation<T, Task<Void>>() {
            @Override
            public Task<Void> then(Task<T> task) throws Exception {
                if (task.isFaulted() || task.isCancelled()) {
                    if (observerExecutor == null) {
                        onFailure();
                        return null;
                    } else {
                        return Task.call(new Callable<Void>() {
                            @Override
                            public Void call() throws Exception {
                                try {
                                    onFailure();
                                }catch (Exception e){ // 用户处理回调时引起的异常，直接 error 掉, 不走回调了
                                    throw new Error(e);
                                }
                                return null;
                            }
                        }, observerExecutor);
                    }
                } else {
                    if (observerExecutor == null) {
                        onSuccess();
                        return null;
                    } else {
                        return Task.call(new Callable<Void>() {
                            @Override
                            public Void call() throws Exception {
                                try {
                                    onSuccess();
                                } catch (Exception e) {// 用户处理回调时引起的异常，直接 error 掉, 不走回调了
                                    throw new Error(e);
                                }
                                return null;
                            }
                        }, observerExecutor);
                    }
                }

            }
        });
        return this;
    }

    private static <TResult> Task<TResult> callTask(final Callable<TResult> callable, Executor executor,
                                                    final CancellationToken ct, int priority) {
        final bolts.TaskCompletionSource<TResult> tcs = new bolts.TaskCompletionSource<>();
        try {
            executor.execute(new AtomTask<TResult>(tcs, ct, callable, priority));
        } catch (Exception e) {
            tcs.setError(new ExecutorException(e));
        }

        return tcs.getTask();
    }

    public void cancel() {
        QCloudLogger.d(TASK_LOG_TAG, "[Call] %s cancel", this);
        if (mCancellationTokenSource != null) {
            mCancellationTokenSource.cancel();
        }
    }

    /**
     * 任务是否已经取消
     *
     * @return true 表示任务已取消
     */
    public final boolean isCanceled() {
        return mCancellationTokenSource != null && mCancellationTokenSource.isCancellationRequested();
    }

    /**
     * 任务是否正在执行
     *
     * @return true 表示正在执行
     */
    public final boolean isExecuting() {
        return getState() == STATE_EXECUTING;
    }

    /**
     * 任务是否执行完成
     *
     * @return true 表示完成
     */
    public final boolean isCompleted() {
        return getState() == STATE_COMPLETE;
    }

    /**
     * 获取任务执行状态
     *
     * @return 任务状态，，有以下几种状态：
     * {@link QCloudTask#STATE_QUEUEING};
     * {@link QCloudTask#STATE_EXECUTING};
     * {@link QCloudTask#STATE_COMPLETE}
     */
    public final synchronized int getState() {
        return mState;
    }

    @Override
    public T call() throws Exception {
        try {
            QCloudLogger.d(TaskManager.TASK_LOG_TAG, "[Task] %s start testExecute", getIdentifier());
            onStateChanged(STATE_EXECUTING);
            return execute();
        } finally {
            QCloudLogger.d(TaskManager.TASK_LOG_TAG, "[Task] %s complete", getIdentifier());
            onStateChanged(STATE_COMPLETE);
            taskManager.remove(QCloudTask.this);
        }
    }

    protected abstract T execute() throws QCloudClientException, QCloudServiceException;

    public final QCloudTask<T> observeOn(Executor executor) {
        observerExecutor = executor;
        return this;
    }

    public final QCloudTask<T> addResultListener(QCloudResultListener<T> resultListener) {
        if (resultListener != null) {
            mResultListeners.add(resultListener);
        }
        return this;
    }

    public final QCloudTask<T> addResultListeners(List<QCloudResultListener<T>> resultListeners) {
        if (resultListeners != null) {
            mResultListeners.addAll(resultListeners);
        }
        return this;
    }

    public final QCloudTask<T> removeResultListener(QCloudResultListener<T> resultListener) {
        if (resultListener != null) {
            mResultListeners.remove(resultListener);
        }
        return this;
    }

    public final void removeAllListeners() {
        mResultListeners.clear();
        mProgressListeners.clear();
    }

    public final List<QCloudResultListener<T>> getAllResultListeners() {
        return new ArrayList<>(mResultListeners);
    }

    public final List<QCloudProgressListener> getAllProgressListeners() {
        return new ArrayList<>(mProgressListeners);
    }

    public final List<QCloudTaskStateListener> getAllStateListeners() {
        return new ArrayList<>(mStateListeners);
    }

    public final QCloudTask<T> addProgressListener(QCloudProgressListener progressListener) {
        if (progressListener != null) {
            mProgressListeners.add(progressListener);
        }
        return this;
    }

    public final QCloudTask<T> addProgressListeners(List<QCloudProgressListener> progressListeners) {
        if (progressListeners != null) {
            mProgressListeners.addAll(progressListeners);
        }
        return this;
    }

    public final QCloudTask<T> removeProgressListener(QCloudProgressListener progressListener) {
        if (progressListener != null) {
            mProgressListeners.remove(progressListener);
        }
        return this;
    }

    public final QCloudTask<T> addStateListener(QCloudTaskStateListener stateListener) {
        if (stateListener != null) {
            mStateListeners.add(stateListener);
        }
        return this;
    }

    public final QCloudTask<T> addStateListeners(List<QCloudTaskStateListener> stateListeners) {
        if (stateListeners != null) {
            mStateListeners.addAll(stateListeners);
        }
        return this;
    }

    public final QCloudTask<T> removeStateListener(QCloudTaskStateListener stateListener) {
        if (stateListener != null) {
            mStateListeners.remove(stateListener);
        }
        return this;
    }

    public T getResult() {
        return mTask.getResult();
    }

    public Exception getException() {
        return mTask.isFaulted() ? mTask.getError() : mTask.isCancelled() ?
                new QCloudClientException("canceled") : null;
    }

    protected void onSuccess() {
        if (mResultListeners.size() > 0) {
            List<QCloudResultListener<T>> listeners = new ArrayList<>(mResultListeners);
            for (QCloudResultListener<T> resultListener : listeners) {
                resultListener.onSuccess(getResult());
            }
        }
    }

    protected void onFailure() {
        Exception exception = getException();
        if (exception != null && mResultListeners.size() > 0) {
            List<QCloudResultListener<T>> listeners = new ArrayList<>(mResultListeners);
            for (QCloudResultListener resultListener : listeners) {
                if (exception instanceof QCloudClientException) {
                    resultListener.onFailure((QCloudClientException) exception, null);
                } else {
                    resultListener.onFailure(null, (QCloudServiceException) exception);
                }
            }
        }
    }

    protected void onProgress(final long complete, final long target) {
        if (mProgressListeners.size() > 0) {
            executeListener(new Runnable() {
                @Override
                public void run() {
                    List<QCloudProgressListener> listeners = new ArrayList<>(mProgressListeners);
                    for (QCloudProgressListener progressListener : listeners) {
                        progressListener.onProgress(complete, target);
                    }
                }
            });
        }
    }

    protected void onStateChanged(int newState) {
        setState(newState);
        if (mStateListeners.size() > 0) {
            executeListener(new Runnable() {
                @Override
                public void run() {
                    List<QCloudTaskStateListener> listeners = new ArrayList<>(mStateListeners);
                    for (QCloudTaskStateListener listener : listeners) {
                        listener.onStateChanged(identifier, mState);
                    }
                }
            });
        }
    }

    private synchronized void setState(int newState) {
        mState = newState;
    }

    private void executeListener(Runnable callback) {
        if (observerExecutor != null) {
            observerExecutor.execute(callback);
        } else {
            callback.run();
        }
    }

    public final String getIdentifier() {
        return identifier;
    }

    public final Object getTag() {
        return tag;
    }

    public int getWeight() {
        return onRequestWeightListener != null ? onRequestWeightListener.onWeight() :
                WEIGHT_LOW;
    }

    public void setOnRequestWeightListener(OnRequestWeightListener onRequestWeightListener) {
        this.onRequestWeightListener = onRequestWeightListener;
    }

    private static class AtomTask<TResult> implements Runnable, Comparable<Runnable> {

        private bolts.TaskCompletionSource<TResult> tcs;
        private CancellationToken ct;
        private Callable<TResult> callable;
        private int priority;
        private static AtomicInteger increment = new AtomicInteger(0);
        private int taskIdentifier;

        public AtomTask(bolts.TaskCompletionSource<TResult> tcs, CancellationToken ct,
                        Callable<TResult> callable, int priority) {
            this.tcs = tcs;
            this.ct = ct;
            this.callable = callable;
            this.priority = priority;
            this.taskIdentifier = increment.addAndGet(1);
        }

        @Override
        public void run() {
            if (ct != null && ct.isCancellationRequested()) {
                tcs.setCancelled();
                return;
            }

            try {
                tcs.setResult(callable.call());
            } catch (CancellationException e) {
                tcs.setCancelled();
            } catch (Exception e) {
                tcs.setError(e);
            }
        }

        @Override
        public int compareTo(@NonNull Runnable o) {
            if (o instanceof AtomTask) {
                int priorityDelta =  ((AtomTask) o).priority - priority;
                if (priorityDelta != 0) {
                    return priorityDelta;
                } else {
                    return taskIdentifier - ((AtomTask) o).taskIdentifier;
                }
            }

            return 0;
        }
    }

    public interface OnRequestWeightListener {
        int onWeight();
    }
}
