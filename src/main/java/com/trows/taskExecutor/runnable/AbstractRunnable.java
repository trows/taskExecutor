package com.trows.taskExecutor.runnable;

import org.apache.log4j.Logger;


/**
 * Created by pengruoying on 2017/3/28.
 * 简历导出线程方法
 */
public abstract class AbstractRunnable implements BaseRunnable, Comparable<AbstractRunnable>,Cloneable {


    private static final Logger logger = Logger.getLogger(AbstractRunnable.class);
    private long priority;

    public AbstractRunnable() {
        this.priority = System.currentTimeMillis();
    }

    public long getPriority() {
        return this.priority;
    }

    public abstract void before();

    public abstract void run();

    public abstract void after();

    public int compareTo(AbstractRunnable runnable) {
        if (this.getPriority() > runnable.getPriority())
            return -1;
        if (this.getPriority() < runnable.getPriority())
            return 1;
        return 0;
    }
}
