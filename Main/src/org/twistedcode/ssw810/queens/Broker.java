package org.twistedcode.ssw810.queens;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
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
    private BlockingQueue<int[]> solutions;
    private AtomicBoolean solutionFound;
    private AtomicBoolean finished;

    public void addSolution(int[] grid, boolean countOnly) {
        total.incrementAndGet();
        if (!countOnly) {
            try {
                solutions.put(grid);
                solutionFound.set(true);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public Boolean solutionFound() {
        return solutionFound.get();
    }

    public Boolean isFinished() {
        return finished.get();
    }

    public int[] getSolution() throws InterruptedException {
        solutionFound.set(false);
        return solutions.take();
    }

    public Broker() {
        this.total = new AtomicInteger(0);
        this.solutions = new ArrayBlockingQueue<>(1);
        this.finished = new AtomicBoolean(false);
        this.solutionFound = new AtomicBoolean(false);
    }

    public int totalSolutions() {
        return total.get();
    }

    private static void printGrid(int[] grid) {
        int N = grid.length;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (grid[i] == j) {
                    System.out.print("Q ");
                } else {
                    System.out.print("* ");
                }
            }
            System.out.println();
        }
        System.out.println();
    }
}
