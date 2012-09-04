package org.twistedcode.ssw810.queens;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created with IntelliJ IDEA.
 * User: tracyde
 * Date: 9/3/12
 * Time: 9:22 AM
 * To change this template use File | Settings | File Templates.
 */
public class WelcomeDialog extends JDialog implements ActionListener {

    private int queens;

    public WelcomeDialog(Frame parent, String title) {
        super(parent, title, true);
        if (parent != null) {
            Dimension parentSize = parent.getSize();
            Point p = parent.getLocation();
            setLocation(p.x + parentSize.width / 4, p.y + parentSize.height / 4);
        }

        JPanel messagePane = new JPanel();
        messagePane.add(new JLabel("How many Queens would you like?"));

        Integer value = new Integer(0);
        Integer min = new Integer(0);
        Integer max = new Integer(100);
        Integer step = new Integer(1);
        final SpinnerNumberModel spinnerModel = new SpinnerNumberModel(value, min, max, step);
        messagePane.add(new JSpinner(spinnerModel));

        getContentPane().add(messagePane);

        JPanel buttonPane = new JPanel();
        JButton exitButton = new JButton("Exit");
        JButton startButton = new JButton("Start");
        buttonPane.add(exitButton);
        exitButton.addActionListener(this);
        buttonPane.add(startButton);
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                spinnerModel.getNumber().intValue();
            }
        });

        getContentPane().add(buttonPane, BorderLayout.SOUTH);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        setVisible(false);
        dispose();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Queens");
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setLayout(new FlowLayout());

        //Board board = new Board(size);
        //frame.add(board);

        frame.setSize(500, 500);
        frame.setResizable(false);
        frame.setVisible(true);

        WelcomeDialog dlg = new WelcomeDialog(frame, "Welcome to NQueens");

    }

}
