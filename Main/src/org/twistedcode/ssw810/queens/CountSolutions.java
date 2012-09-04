package org.twistedcode.ssw810.queens;

import com.apple.jobjc.Utils;

import java.util.concurrent.ForkJoinPool;

/**
 * Created with IntelliJ IDEA.
 * User: tracyde
 * Date: 9/1/12
 * Time: 12:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class CountSolutions implements Runnable {
    final Broker broker;
    final int gridSize;

    CountSolutions(Broker broker, int gridSize) {
        this.broker = broker;
        this.gridSize = gridSize;
    }

    @Override
    public void run() {
        ForkJoinPool pool = new ForkJoinPool();
        QueensGrid grid = new QueensGrid(broker, gridSize, new int[0], true);
        pool.invoke(grid);
        pool.shutdown();
    }

    public static void main(String[] args) {
        Broker b = new Broker();
        Runnable task = new CountSolutions(b, 8);
        Thread worker = new Thread(task);
        worker.setName("CountSolutions");
        worker.start();
        try {
            worker.join();
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        System.out.println(b.totalSolutions());
    }
}
