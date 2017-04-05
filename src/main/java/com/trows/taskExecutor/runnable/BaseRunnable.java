package com.trows.taskExecutor.runnable;

/**
 * Created by pengruoying on 2017/4/5.
 * base
 */
public interface BaseRunnable extends Runnable {

    void before();

    void after();

}
