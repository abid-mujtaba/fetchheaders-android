/*
 *  Copyright 2014 Abid Hasan Mujtaba
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

/**
 * A simple counter to be used in multi-threaded applications.
 */

public class Counter
{
    private volatile int count = 0;

    public Counter(int value)       // The Counter is specified with an initial value
    {
        count = value;
    }

    public synchronized void increment()
    {
        count++;
    }

    public synchronized void decrement()
    {
        count--;
    }

    public synchronized int value()
    {
        return count;
    }
}
