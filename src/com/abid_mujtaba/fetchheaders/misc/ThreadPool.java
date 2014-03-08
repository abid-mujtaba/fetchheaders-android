/*
 *  Copyright 2013 Abid Hasan Mujtaba
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

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