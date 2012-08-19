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
        setLayout(new GridLayout(size,size));
        setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
        boolean flag = true;

        for (int x = 0; x < size; x++)
        {
            for (int y = 0; y < size; y++) {
                JPanel pan = new JPanel();
                pan.setPreferredSize(new Dimension(30,30));
                pan.setBorder(BorderFactory.createLineBorder(Color.GRAY));

                if (flag){
                    pan.setBackground(square1);
                    flag = !flag;
                } else {
                    pan.setBackground(square2);
                    flag = !flag;
                }
                add(pan);
            }

            flag = !flag;
        }

    }
}
