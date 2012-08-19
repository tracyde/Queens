package org.twistedcode.ssw810.queens;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created with IntelliJ IDEA.
 * User: tracyde
 * Date: 8/15/12
 * Time: 9:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class GridForkJoin extends RecursiveAction {
    private int level_threshold;
    private int size;
    private List<int[]> solutions;
    int[] grid;
    int row;
    private Broker broker;

    public GridForkJoin(Broker broker, int[] grid, int row) {
        List<int[]> s = new ArrayList<>();
        this.broker = broker;
        this.size = grid.length;
        this.level_threshold = grid.length / 2;
        this.grid = copyGrid(grid);
        this.solutions = s;
        this.row = row;
    }
    
    public GridForkJoin(Broker broker, int[] grid, int row, List<int[]> solutions) {
        this.broker = broker;
        this.size = grid.length;
        this.level_threshold = grid.length / 2;
        this.grid = copyGrid(grid);
        // Initialize the solutions List to the absolute maximum number of values
        // the actual final size will be much lower but there is no way to predict it.
        // This should save on lots of array copies when adding new solutions.
        //this.solutions = new ArrayList<int[]>(this.size^this.size);
        this.solutions = solutions;
        this.row = row;
    }

    private int[] copyGrid(int[] grid) {
        int[] nGrid = new int[grid.length];
        System.arraycopy(grid, 0, nGrid, 0, nGrid.length);
        return nGrid;
    }

    public int totalSolutions() {
        return this.solutions.size();
    }

    /*
     *  Returns true if the queen placement at grid[n] does not
     *  conflict with the other placed queens in the grid grid[0] through grid[n-1]
     */
    private boolean noConflicts(int[] grid, int row) {
        for (int i = 0; i < row; i++) {
            if (grid[i] == grid[row]) return false; // same column
            if ((grid[i] - grid[row]) == (row - i)) return false; // same major diagonal
            if ((grid[row] - grid[i]) == (row - i)) return false; // same minor diagonal
        }
        return true;
    }

    @Override
    protected void compute() {
        for (int i = 0; i < grid.length; ++i) {
            grid[row] = i;
            if (noConflicts(grid, row)) {
                if (row < level_threshold) {
                    int[] nGrid = new int[grid.length];
                    for (int j = 0; j <= row; ++j) {
                        nGrid[j] = grid[j];
                    }
                    //int[] nGrid = copyGrid(grid);
                    invokeAll(new GridForkJoin(this.broker, nGrid, row + 1, this.solutions)); // generate task
                } else {
                    enumerate(grid, row+1); // no more tasks, work from here
                }
            }
        }
    }

    // This method uses back tracking to find solutions
    private void enumerate(int[] grid, int row) {
        int N = grid.length;
        if (row == N) {
            // Solution found, add to solutions collection
            solutions.add(grid);
            this.broker.addSolution(grid);
            // printGrid(grid);
        } else {
            for (int i = 0; i < N; i++) {
                grid[row] = i;
                if (noConflicts(grid, row)) enumerate(grid, row+1);
            }
        }
    }

    // Suppressing the warning as I think it is easier to understand using the long
    // for loop invocation
    @SuppressWarnings("ForLoopReplaceableByForEach")
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

    /*
     *  Main method to test class with
     */
    public static void main(String[] args) {

        int[] g = new int[8];
        Broker b = new Broker();
        GridForkJoin forkJoinProcess = new GridForkJoin(b, g, 0);
        ForkJoinPool pool = new ForkJoinPool();

        long startTime = System.currentTimeMillis();

        pool.invoke(forkJoinProcess);
//        pool.execute(forkJoinProcess);
//        while (pool.isShutdown()) {
//            System.out.println(pool.toString());
//        }

        long endTime = System.currentTimeMillis();
        double totalTime = (endTime - startTime) / 1000d;
        System.out.println("Took " + totalTime + " seconds...");
        //System.out.println(grid.totalSolutions() + " solutions found.");
        System.out.println(b.totalSolutions() + " solutions found.");
    }

}
