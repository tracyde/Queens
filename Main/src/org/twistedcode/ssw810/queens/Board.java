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
    private static final Color queen = new Color(232, 45, 52);
    private int size;

    public void update(int[] grid) {
        this.removeAll();
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        boolean flag = true; // needed to alternate checkerboard color (stagger it)

        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                JPanel panel = new JPanel();
                panel.setPreferredSize(new Dimension(30, 30));

                if (flag) {
                    panel.setBackground(square1);
                    flag = !flag;
                } else {
                    panel.setBackground(square2);
                    flag = !flag;
                }

                if (grid[y] == x) {
                    panel.setBackground(queen);
                }

                c.gridx = x;
                c.gridy = y;
                add(panel, c);
            }

            flag = !flag;
        }
//        this.repaint();
    }

    public Board(int[] grid) {
        size = grid.length;
//        setLayout(new GridLayout(size,size));
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
//        c.gridwidth = size;
//        c.gridheight = size;
//        c.gridx = 0;
//        c.gridy = 0;

        setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        boolean flag = true; // needed to alternate checkerboard color (stagger it)

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
