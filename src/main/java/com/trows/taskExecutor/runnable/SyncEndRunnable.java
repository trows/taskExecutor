package com.trows.taskExecutor.runnable;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created by pengruoying on 2017/4/5.
 */
public class SyncEndRunnable implements BaseRunnable, Comparable<SyncEndRunnable>, Cloneable {

    private long priority;
    private BaseRunnable runnable;
    private List<Future> list;


    public SyncEndRunnable(BaseRunnable runnable, List<Future> list) {
        this.priority = System.currentTimeMillis();
        this.runnable = runnable;
        this.list = list;
    }

    public long getPriority() {
        return this.priority;
    }

    public void before() {

    }

    public void after() {
        try {
            for (Future future : list) {
                future.get();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        runnable.after();
    }

    public int compareTo(SyncEndRunnable runnable) {
        if (this.getPriority() > runnable.getPriority())
            return -1;
        if (this.getPriority() < runnable.getPriority())
            return 1;
        return 0;
    }

    public void run() {
        after();
    }
}
