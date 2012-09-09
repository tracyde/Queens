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
public class WelcomeDialog extends JFrame implements ActionListener {

    public WelcomeDialog(String title) {
        super(title);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        JPanel messagePane = new JPanel();
        messagePane.add(new JLabel("How many Queens would you like?"));

        Integer value = new Integer(8);
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
                setVisible(false);
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        Main app = new Main(spinnerModel.getNumber().intValue());
                        app.setVisible(true);
                    }
                });
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

}
