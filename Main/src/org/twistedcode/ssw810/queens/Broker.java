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
    private AtomicBoolean finished;

    public void addSolution(int[] grid, boolean countOnly) {
        total.incrementAndGet();
        if (!countOnly) {
            try {
                solutions.put(grid);
            } catch (InterruptedException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }

    public Boolean isFinished() {
        return finished.get();
    }

    public Broker() {
        this.total = new AtomicInteger(0);
//        this.solutions = new ConcurrentHashMap<>();
        this.solutions = new ArrayBlockingQueue<int[]>(1);
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

//    public void printSolutions() {
//        for (int[] grid : solutions.values()) {
//            printGrid(grid);
//        }
//    }

//    public boolean hasValue(Integer idx) {
//        return solutions.containsKey(idx);
//    }

//    public int[] getValue(Integer idx) {
//        if (solutions.containsKey(idx))
//            return solutions.get(idx);
//        else
//            return null;
//    }
}
