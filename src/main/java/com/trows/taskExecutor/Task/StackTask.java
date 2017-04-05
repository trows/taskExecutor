package com.trows.taskExecutor.Task;

import com.trows.taskExecutor.Exception.IllegalRunnableException;

/**
 * Created by pengruoying on 2017/4/5.
 */
public class StackTask implements Task {

    public boolean doTask() throws IllegalRunnableException {
        return false;
    }

    public boolean doTask(int availabilityThread) throws IllegalRunnableException {
        return false;
    }

    public void doTaskAsyn() throws IllegalRunnableException {

    }

    public void doTaskAsyn(int availabilityThread) throws IllegalRunnableException {

    }
}
