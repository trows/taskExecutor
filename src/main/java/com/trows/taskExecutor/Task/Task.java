package com.trows.taskExecutor.Task;

import com.trows.taskExecutor.Exception.IllegalRunnableException;

/**
 * Created by pengruoying on 2017/4/5.
 * task接口
 */
public interface Task {
    boolean doTask() throws IllegalRunnableException;

    boolean doTask(int availabilityThread) throws IllegalRunnableException;

    void doTaskAsyn() throws IllegalRunnableException;

    void doTaskAsyn(int availabilityThread) throws IllegalRunnableException;
}
