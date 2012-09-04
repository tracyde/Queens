package org.twistedcode.ssw810.queens;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.plaf.ProgressBarUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

    private final Integer size;
    private final JFrame frame;
    private final JProgressBar progressBar;
    private final JLabel statusLabel;
    private final JLabel totalLabel;
    private final JLabel viewLabel;

    private Main(Integer gridSize) {
        this.size = gridSize;
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
        Board board = new Board(size);
        topPanel.add(board, BorderLayout.CENTER);

        frame.add(topPanel, BorderLayout.NORTH);

        JPanel bottomPanel = new JPanel(new BorderLayout()); // Holds progressBar, buttons, status, statusBar

        JPanel progressPanel = new JPanel(new BorderLayout());
        progressBar = new JProgressBar(JProgressBar.HORIZONTAL, 0, 100);
        progressPanel.add(progressBar, BorderLayout.CENTER);
        bottomPanel.add(progressBar, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(2, 2));
        JButton acceptButton = new JButton("Accept");
        JButton nextButton = new JButton("Next");
        JButton pauseExitButton = new JButton("");
        buttonPanel.add(acceptButton);
        buttonPanel.add(nextButton);
        buttonPanel.add(pauseExitButton);
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
        Thread countThread = new Thread() {
            @Override
            public void run() {
                ForkJoinPool pool = new ForkJoinPool();
                QueensGrid grid = new QueensGrid(countBroker, size, new int[0], true);
                updateStatusLabel("Counting Solutions...");
                pool.invoke(grid);
                pool.shutdown();
                updateStatusLabel("Done counting solutions.");
            }
        };
        countThread.start();

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
            }
        });
    }

    private void setVisible(boolean visible) {
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
        final String text = "Solutions Viewed: 0";
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                viewLabel.setText(text);
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Main app = new Main(16);
                app.setVisible(true);
            }
        });
    }
}
