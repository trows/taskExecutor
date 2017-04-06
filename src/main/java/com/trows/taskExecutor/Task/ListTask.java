package com.trows.taskExecutor.Task;

import com.trows.taskExecutor.Exception.IllegalRunnableException;
import com.trows.taskExecutor.ThreadPool.BlockingTaskExecutor;
import com.trows.taskExecutor.runnable.AbstractRunnable;
import com.trows.taskExecutor.runnable.BaseRunnable;
import com.trows.taskExecutor.runnable.SyncEndRunnable;
import org.apache.log4j.Logger;


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * Created by pengruoying on 2017/4/5.
 * 主任务方法
 */
public class ListTask implements Task {
    private static final Logger logger = Logger.getLogger(ListTask.class);
    private ExecutorService executorService;
    private List taskList = null;
    private Map<String, Object> params = null;
    private Class<?> clazz;
    private boolean isLegal;
    private static int corePoolSize = Runtime.getRuntime().availableProcessors() * 2;

    public ListTask(Class runnableClass) {
        this(null, runnableClass, null);
    }

    public ListTask(List taskList, Class runnableClass) {
        this(taskList, runnableClass, null);
    }

    public ListTask(List taskList, Class runnableClass, Map<String, Object> params) {
        this.taskList = taskList;
        this.executorService = BlockingTaskExecutor.getExecutorService();
        this.params = params;
        try {
            clazz = Class.forName(runnableClass.getName());
        } catch (ClassNotFoundException e) {
            logger.error(e);
        }

        try {
            if ((clazz.newInstance() instanceof AbstractRunnable))
                isLegal = true;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private BaseRunnable getRunnable(List partList) throws IllegalAccessException, InstantiationException {
        BaseRunnable runnable = (BaseRunnable) clazz.newInstance();

        if (partList != null) {
            try {
                Field field = clazz.getDeclaredField("taskList");
                field.setAccessible(true);
                field.set(runnable, partList);
            } catch (NoSuchFieldException e) {
                logger.error("No such taskList", e);
            } catch (IllegalAccessException e) {
                logger.error(e);
            }
        }

        if (params != null) {
            try {
                Field field = clazz.getDeclaredField("params");
                field.setAccessible(true);
                field.set(runnable, params);
            } catch (NoSuchFieldException e) {
                logger.error("No such params map", e);
            } catch (IllegalAccessException e) {
                logger.error(e);
            }
        }

        return runnable;
    }

    public boolean doTask() throws IllegalRunnableException {
        return doTask(corePoolSize);
    }

    public boolean doTask(int availabilityThread) throws IllegalRunnableException {
        if (!isLegal) throw new IllegalRunnableException();
        BaseRunnable baseJob;
        int max = (int) Math.ceil(taskList.size() / (availabilityThread * 1.0));
        if (max == 0) max = 1;
        int tag = (int) Math.ceil(taskList.size() / (max * 1.0));
        List<Future> list = new ArrayList<Future>(tag);
        try {
            baseJob = getRunnable(taskList);
            baseJob.before();
            distribute(max, tag, list);
        } catch (IllegalAccessException e) {
            logger.error(e);
            return false;
        } catch (InstantiationException e) {
            logger.error(e);
            return false;
        }
        return block(list, baseJob);
    }

    private boolean block(List<Future> list, BaseRunnable baseJob) {
        try {
            for (Future future : list) {
                future.get();
            }
            baseJob.after();
        } catch (InterruptedException e) {
            logger.error(e);
            return false;
        } catch (ExecutionException e) {
            logger.error(e);
            return false;
        }
        return true;
    }

    public void doTaskAsyn() throws IllegalRunnableException {
        doTaskAsyn(corePoolSize);
    }

    public void doTaskAsyn(int availabilityThread) throws IllegalRunnableException {
        if (!isLegal) throw new IllegalRunnableException();
        int max = (int) Math.ceil(taskList.size() / (availabilityThread * 1.0));
        if (max == 0) max = 1;
        int tag = (int) Math.ceil(taskList.size() / (max * 1.0));
        List<Future> list = new ArrayList<Future>(tag);
        BaseRunnable baseJob;
        try {
            baseJob = getRunnable(taskList);
            baseJob.before();
            distribute(max, tag, list);
            executorService.execute(new SyncEndRunnable(baseJob,list));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }


    private void distribute(int max, int tag, List<Future> list) throws IllegalAccessException, InstantiationException {
        for (int i = 0; i < tag; i++) {
            List partList;
            if (i == tag - 1) {
                partList = taskList.subList(i * max, taskList.size());
            } else {
                partList = taskList.subList(i * max, (i + 1) * max);
            }
            list.add(executorService.submit(getRunnable(partList)));
        }
    }
}
