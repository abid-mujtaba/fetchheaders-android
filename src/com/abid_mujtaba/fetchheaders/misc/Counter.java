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
