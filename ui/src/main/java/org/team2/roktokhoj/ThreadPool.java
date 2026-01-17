package org.team2.roktokhoj;

import java.util.*;
import java.util.concurrent.*;

public class ThreadPool {
    private final static Object guard = new Object();
    private static ThreadPool instance = null;

    public static ThreadPoolExecutor getExecutor() {
        return instance.executor;
    }

    public static void init(int poolSize) throws Exception {
        if (poolSize <= 0) {
            poolSize = Runtime.getRuntime().availableProcessors();
        }
        synchronized (guard) {
            if (instance != null) {
                throw new Exception("Instance is already initialized");
            }
            instance = new ThreadPool(poolSize);
        }
    }

    public static void close() throws Exception {
        synchronized (guard) {
            if (instance == null) {
                throw new Exception("Instance was not initialized");
            }
            instance.executor.shutdown();
        }
    }

    private final ThreadPoolExecutor executor;

    ThreadPool(int poolSize) {
        executor = new ScheduledThreadPoolExecutor(poolSize);
    }

}
