package org.twistedcode.ssw810.queens;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created with IntelliJ IDEA.
 * User: tracyde
 * Date: 8/18/12
 * Time: 11:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class Broker {
    private AtomicInteger total;

    public void addSolution(int[] grid) {
        this.total.incrementAndGet();
    }

    public Broker() {
        this.total = new AtomicInteger(0);
    }

    public int totalSolutions() {
        return this.total.get();
    }
}
