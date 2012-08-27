package org.twistedcode.ssw810.queens;

import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

/**
 * Created with IntelliJ IDEA.
 * User: tracyde
 * Date: 8/25/12
 * Time: 9:20 AM
 * To change this template use File | Settings | File Templates.
 */
public class QueensGrid extends RecursiveAction {
    private int gridSize;
    private Broker broker;
    private final int[] sofar;
    private QueensGrid nextSubtask; // to link subtasks
    private int solutions;

    public QueensGrid(Broker broker, int size, int[] array) {
        this.broker = broker;
        this.gridSize = size;
        this.sofar = array;
    }

    @Override
    protected void compute() {
        QueensGrid subtasks;
        int gs = gridSize;
        if (sofar.length >= gs) {
            broker.addSolution(sofar);
            solutions = 1;
        } else if ((subtasks = this.explore(sofar, gs)) != null) {
            solutions = this.processSubtasks(subtasks);
        }
    }

    private QueensGrid explore(int[] array, int gs) {
        int row = array.length;
        QueensGrid qg = null; // subtask list
        outer:
        for (int q = 0; q < gs; ++q) {
            for (int i = 0; i < row; i++) {
                int p = array[i];
                if (q == p || q == p - (row - i) || q == p + (row - i))
                    continue outer; // conflict found
            }
            QueensGrid first = qg; // lag forks to ensure 1 kept
            if (first != null)
                first.fork();
            int[] next = Arrays.copyOf(array, row + 1);
            next[row] = q;
            QueensGrid subtask = new QueensGrid(this.broker, gs, next);
            subtask.nextSubtask = first;
            qg = subtask;
        }
        return qg;
    }

    private int processSubtasks(QueensGrid qg) {
        // Always run first the task held instead of forked
        qg.compute();
        int ns = qg.solutions;
        qg = qg.nextSubtask;
        // Then the unstolen ones
        while (qg != null && qg.tryUnfork()) {
            qg.compute();
            ns += qg.solutions;
            qg = qg.nextSubtask;
        }
        // Then wait for the stolen ones
        while (qg != null) {
            qg.join();
            ns += qg.solutions;
            qg = qg.nextSubtask;
        }
        return ns;
    }

    public static void main(String[] args) {

        final int minBoardSize = 8;
        final int maxBoardSize = 15;

        Broker broker = new Broker();

        int[] expectedSolutions = new int[]{
                0, 1, 0, 0, 2, 10, 4, 40, 92, 352, 724, 2680, 14200,
                73712, 365596, 2279184, 14772512, 95815104, 666090624
        }; // see http://www.durangobill.com/N_Queens.html

        final long NPS = (1000L * 1000L * 1000L);

        ForkJoinPool pool = new ForkJoinPool();
        QueensGrid grid = new QueensGrid(broker, 16, new int[0]);

        long start = System.nanoTime();

        pool.invoke(grid);
        int solutions = grid.solutions;

        long time = System.nanoTime() - start;

        double secs = (double) time / NPS;

        if (solutions != expectedSolutions[grid.gridSize])
            throw new Error();

        // Shutdown the threadpool
        pool.shutdown();

        broker.printSolutions();
        System.out.printf("QueensGrid %3d", grid.gridSize);
        System.out.printf(" Solutions %3d", solutions);
        System.out.printf(" Time: %7.3f", secs);
        System.out.println();
        System.out.println("Broker Solutions: " + broker.totalSolutions());
    }
}
