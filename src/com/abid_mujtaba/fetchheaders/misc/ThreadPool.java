package com.abid_mujtaba.fetchheaders.misc;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * This implements a ThreadPoolExecutor which accepts generic runnables and executes them concurrently in the specified number of threads.
 */

public class ThreadPool {

    static final int INITIAL_THREAD_POOL_COUNT = 3;
    static final int MAX_THREAD_POOL_COUNT = 3;
    static final int KEEP_ALIVE_TIME = 1;
    static final TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;

    static ThreadPool sInstance = new ThreadPool();     // Single instance of manager kept and controlled locally

    private final BlockingQueue<Runnable> mWorkQueue = new LinkedBlockingQueue<Runnable>();
    private ThreadPoolExecutor mThreadPoolExecutor = new ThreadPoolExecutor(INITIAL_THREAD_POOL_COUNT, MAX_THREAD_POOL_COUNT, KEEP_ALIVE_TIME, KEEP_ALIVE_TIME_UNIT, mWorkQueue);


    private ThreadPool() {}       // Empty and Private default constructor (not accessible from outside).

    static public void executeTask(Runnable _runnable)
    {
        sInstance.mThreadPoolExecutor.execute(_runnable);
    }
}