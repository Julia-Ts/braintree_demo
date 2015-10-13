package com.yalantis.api;

import com.yalantis.api.task.ApiTask;
import com.yalantis.interfaces.QueuedExecutorCallback;

public class ApiTaskExecutor implements QueuedExecutorCallback {

    private ApiTaskQueue taskQueue = new ApiTaskQueue();

    public synchronized void execute(ApiTask task) {
        task.setCallback(this);
        taskQueue.add(task);
        nextTask();
    }

    private synchronized void nextTask() {
        if (!taskQueue.isActive()) {
            ApiTask task;
            if ((task = taskQueue.poll()) != null) {
                taskQueue.activate();
                new Thread(task).start();
            }
        }
    }

    @Override
    public void finished() {
        taskQueue.deactivate();
        nextTask();
    }

    public void clear() {
        taskQueue.clear();
    }

}
