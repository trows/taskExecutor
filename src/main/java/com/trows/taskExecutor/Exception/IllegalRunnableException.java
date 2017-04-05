package com.trows.taskExecutor.Exception;

/**
 * Created by pengruoying on 2017/4/5.
 * 非法Runnable异常
 */
public class IllegalRunnableException extends Exception {
    public IllegalRunnableException() {
    }

    public IllegalRunnableException(String message) {
        super(message);
    }
}
