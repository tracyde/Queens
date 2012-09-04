package org.twistedcode.ssw810.queens;

import javax.swing.*;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: tracyde
 * Date: 8/4/12
 * Time: 3:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class Board extends JPanel {
    private static final Color square1 = new Color(50, 143, 13);
    private static final Color square2 = new Color(215, 213, 0);
    private int size;

    Square[][] grid;

    public void setSize(int size) {
        this.size = size;
    }

    public Board(int size) {
//        setLayout(new GridLayout(size,size));
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
//        c.gridwidth = size;
//        c.gridheight = size;
//        c.gridx = 0;
//        c.gridy = 0;

        setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        boolean flag = true;

        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                JPanel panel = new JPanel();
                panel.setPreferredSize(new Dimension(30, 30));
                //panel.setBorder(BorderFactory.createLineBorder(Color.GRAY));

                if (flag) {
                    panel.setBackground(square1);
                    flag = !flag;
                } else {
                    panel.setBackground(square2);
                    flag = !flag;
                }
                c.gridx = x;
                c.gridy = y;
                add(panel, c);
            }

            flag = !flag;
        }

    }
}
