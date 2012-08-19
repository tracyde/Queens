package org.twistedcode.ssw810.queens;

import javax.swing.*;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: tracyde
 * Date: 8/4/12
 * Time: 3:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class Square extends JButton {
    private static final Dimension d = new Dimension(10, 10);
    public Square(Color color) {
        this.setBackground(color);
        this.setSize(d);
        this.setMinimumSize(d);
        this.setPreferredSize(d);
        this.setMaximumSize(d);
    }
}
