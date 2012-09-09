package org.twistedcode.ssw810.queens;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.plaf.ProgressBarUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.util.concurrent.ForkJoinPool;

/**
 * Created with IntelliJ IDEA.
 * User: tracyde
 * Date: 8/4/12
 * Time: 3:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class Main implements ActionListener {
    private final Broker countBroker;
    private final Broker solutionsBroker;
    private Board board;
    private Integer size;
    private int solutionsViewed;
    private int[] solution;
    private final JFrame frame;
    private final JProgressBar progressBar;
    private final JLabel statusLabel;
    private final JLabel totalLabel;
    private final JLabel viewLabel;

    public Main(Integer gridSize) {
        Thread countThread = null;
        Thread solutionThread = null;

        this.size = gridSize;
        this.solution = new int[this.size];

        countBroker = new Broker();
        solutionsBroker = new Broker();

        Dimension framePrefSize = new Dimension(500, 500);

        frame = new JFrame("Queens");
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setPreferredSize(framePrefSize);
        frame.setSize(framePrefSize);
//        frame.setResizable(false);
        frame.setLayout(new BorderLayout());

        // Create topPanel that holds a scrollPane and the Board
        JPanel topPanel = new JPanel(new BorderLayout());
        this.board = new Board(this.solution);
        topPanel.add(this.board, BorderLayout.CENTER);

        frame.add(topPanel, BorderLayout.NORTH);

        JPanel bottomPanel = new JPanel(new BorderLayout()); // Holds progressBar, buttons, status, statusBar

        JPanel progressPanel = new JPanel(new BorderLayout());
        progressBar = new JProgressBar(JProgressBar.HORIZONTAL, 0, 100);
        progressPanel.add(progressBar, BorderLayout.CENTER);
        bottomPanel.add(progressBar, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2));
        JButton exitButton = new JButton("Exit");
        final Thread finalCountThread = countThread;
        final Thread finalSolutionThread = solutionThread;
        ActionListener exitListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateStatusLabel("Shutdown Requested...");
                if (finalCountThread != null)
                    if (finalCountThread.isAlive())
                        finalCountThread.interrupt();

                if (finalSolutionThread != null)
                    if (finalSolutionThread.isAlive())
                        finalSolutionThread.interrupt();

                System.exit(0);
            }
        };
        exitButton.addActionListener(exitListener);

        JButton nextButton = new JButton("Next");
        ActionListener nextListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (solutionsBroker.solutionFound()) {
                    try {
                        int[] solution = solutionsBroker.getSolution();
                        printGrid(solution);
                        board.update(solution);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                    frame.repaint();
                    solutionsViewed++;
                    updateViewLabel();
                }
            }
        };
        nextButton.addActionListener(nextListener);

        buttonPanel.add(exitButton);
        buttonPanel.add(nextButton);
        bottomPanel.add(buttonPanel, BorderLayout.WEST);

        JSeparator bottomSep = new JSeparator(JSeparator.VERTICAL);
        bottomPanel.add(bottomSep, BorderLayout.CENTER);

        JPanel updatePanel = new JPanel();
        updatePanel.setLayout(new GridLayout(10, 2));
        totalLabel = new JLabel("Total Solutions: 0");
        totalLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        viewLabel = new JLabel("Solutions Viewed: 0");
        viewLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        updatePanel.add(totalLabel);
        updatePanel.add(viewLabel);
        bottomPanel.add(updatePanel, BorderLayout.EAST);

        JPanel statusPanel = new JPanel();
        statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
        statusPanel.setPreferredSize(new Dimension(frame.getWidth(), 20));
        statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
        statusLabel = new JLabel("status");
        statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
        statusPanel.add(statusLabel);

        bottomPanel.add(statusPanel, BorderLayout.SOUTH);

        frame.add(bottomPanel);
        frame.setVisible(true);

        // Run countSolutions to update progressBar and status
        countThread = new Thread() {
            @Override
            public void run() {
                ForkJoinPool pool = new ForkJoinPool();
                QueensGrid grid = new QueensGrid(countBroker, size, new int[0], true);
                updateStatusLabel("Started counting solutions...");
                pool.invoke(grid);
                pool.shutdown();
                updateStatusLabel("Done counting solutions.");
                setProgressBarMax(countBroker.totalSolutions());
            }
        };
        countThread.start();

        // Run computeSolutions to update progressBar and status
        solutionThread = new Thread() {
            @Override
            public void run() {
                ForkJoinPool pool = new ForkJoinPool();
                QueensGrid grid = new QueensGrid(solutionsBroker, size, new int[0], false);
                updateStatusLabel("Started computing solutions...");
                pool.invoke(grid);
                pool.shutdown();
                updateStatusLabel("Finished computing solutions.");
            }
        };
        solutionThread.start();

        int speed = 100;
        int pause = 2000;
        Timer timer = new Timer(speed, this);
//        timer.setInitialDelay(pause);
        timer.start();

    }

    private void updateStatusLabel(final String statusText) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                statusLabel.setText(statusText);
                statusLabel.repaint();
            }
        });
    }

    private void setProgressBarMax(final int maxInt) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                progressBar.setMaximum(maxInt);
            }
        });
    }

    public void setVisible(boolean visible) {
        frame.setVisible(visible);
    }

    private void updateTotalLabel() {
        final String text = "Total Solutions: " + countBroker.totalSolutions();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                totalLabel.setText(text);
            }
        });
    }

    private void updateViewLabel() {
        // ToDo implement update logic
        final String text = "Solutions Viewed: " + solutionsViewed;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                viewLabel.setText(text);
                progressBar.setValue(solutionsViewed);
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Status update logic here
        updateTotalLabel();
        updateViewLabel();
        frame.repaint();
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

    public static void main(String[] args) {
        WelcomeDialog dlg = new WelcomeDialog("Welcome to NQueens");
    }
}
