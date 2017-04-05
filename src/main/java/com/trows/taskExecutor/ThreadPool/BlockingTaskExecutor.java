package com.trows.taskExecutor.ThreadPool;


import java.util.concurrent.*;

/**
 * Created by pengruoying on 2017/3/28.
 * 动态core 无限队列线程池
 */
public final class BlockingTaskExecutor {

    private static int corePoolSize = Runtime.getRuntime().availableProcessors() * 2;
    private static int maximumPoolSize = Math.round(corePoolSize * 1.5F);//其实是摆设
    private static long keepAliveTime = 60;
    private static TimeUnit timeUnit = TimeUnit.SECONDS;
    private static PriorityBlockingQueue runnableTaskQueue = new PriorityBlockingQueue();
    private static RejectedExecutionHandler handler = new ThreadPoolExecutor.AbortPolicy();

    private static ExecutorService executorService
            = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, timeUnit, runnableTaskQueue, handler);

    private BlockingTaskExecutor() {
    }

    public static ExecutorService getExecutorService() {
        return executorService;
    }
}