package org.twistedcode.ssw810.queens;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: tracyde
 * Date: 8/15/12
 * Time: 9:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class Grid {
    private int size;
    private List<int[]> solutions;
    int[] grid;

    public Grid(int size) {
        this.size = size;
        this.grid = new int[this.size];
        // Initialize the solutions List to the absolute maximum number of values
        // the actual final size will be much lower but there is no way to predict it.
        // This should save on lots of array copies when adding new solutions.
        this.solutions = new ArrayList<int[]>(this.size^this.size);
    }

    public void generateGrids() {
        enumerate(grid, 0);
    }

    public int totalSolutions() {
        return this.solutions.size();
    }

    /*
     *  Returns true if the queen placement at grid[n] does not
     *  conflict with the other placed queens in the grid grid[0] through grid[n-1]
     */
    private boolean noConflicts(int[] grid, int idx) {
        for (int i = 0; i < idx; i++) {
            if (grid[i] == grid[idx]) return false; // same column
            if ((grid[i] - grid[idx]) == (idx - i)) return false; // same major diagonal
            if ((grid[idx] - grid[i]) == (idx - i)) return false; // same minor diagonal
        }
        return true;
    }

    // This method uses back tracking to find solutions
    // processes each row
    private void enumerate(int[] grid, int n) {
        int N = grid.length;
        if (n == N) {
            // Solution found, add to solutions collection
            solutions.add(grid);
            //printGrid(grid);
        } else {
            for (int i = 0; i < N; i++) {
                grid[n] = i;
                if (noConflicts(grid, n)) enumerate(grid, n+1);
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
        Grid grid = new Grid(14);
        long startTime = System.currentTimeMillis();

        grid.generateGrids();

        long endTime = System.currentTimeMillis();
        double totalTime = (endTime - startTime) / 1000d;
        System.out.println("Took " + totalTime + " seconds...");
        System.out.println(grid.totalSolutions() + " solutions found.");
    }
}
