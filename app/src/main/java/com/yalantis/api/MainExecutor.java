package com.yalantis.api;

import com.yalantis.api.task.ApiTask;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by: Dmitriy Dovbnya
 * Date: 08.10.13 18:27
 */
public class MainExecutor {

    private final Map<QueueType, ApiTaskExecutor> executors = new HashMap<>();

    public MainExecutor() {
        executors.put(QueueType.MAIN, new ApiTaskExecutor());
        executors.put(QueueType.OTHER, new ApiTaskExecutor());
    }

    public void execute(ApiTask task) {
        task.run();
    }

    public void execute(QueueType type, ApiTask task) {
        if (type == QueueType.NO_QUEUE) {
            execute(task);
        } else {
            executors.get(type).execute(task);
        }
    }

    public enum QueueType {
        NO_QUEUE, MAIN, OTHER
    }

    public void clear() {
        synchronized (executors) {
            for (ApiTaskExecutor executor : executors.values()) {
                executor.clear();
            }
        }
    }

}
