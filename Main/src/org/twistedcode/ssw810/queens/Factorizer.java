package org.twistedcode.ssw810.queens;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * Created with IntelliJ IDEA.
 * User: tracyde
 * Date: 8/19/12
 * Time: 1:20 AM
 * To change this template use File | Settings | File Templates.
 */
public class Factorizer extends RecursiveTask<Long> {
    static ForkJoinPool fjPool = new ForkJoinPool();
    static final int sequentialThreshold = 10000;

    private long number, low, high;

    Factorizer(long number, long low, long high) {
        this.number = number; this.low = low; this.high = high;
    }

    private long factorize() {
        if ((number % 2) == 0) {
            return 2;
        }
        // ensures i is odd (we already know number is not even)
        long i = ((low % 2) == 0) ? low + 1: low;
        for (/**/; i < high; i+=2) {
            if ((number % i) == 0) {
                return i;
            }
        }
        return number;
    }

    @Override
    protected Long compute() {

        // ugly debug statement counts active threads
        System.err.println(Thread.enumerate(
                new Thread[Thread.activeCount()*2]));

        if (high - low <= sequentialThreshold) {
            return factorize();
        } else {
            long mid = low + (high - low) / 2;
            Factorizer left = new Factorizer(number, low, mid);
            Factorizer right = new Factorizer(number, mid, high);
            left.fork();
            return Math.min(right.compute(), left.join());
        }
    }

    static long factorize(long num) {
        return fjPool.invoke(new Factorizer(num, 2, (long)Math.sqrt(num+1)));
    }

    public static void main(String[] args) {
//        Factorizer.factorize(71236789143834319L);
        Factorizer.factorize(Long.MAX_VALUE-1);
    }
}