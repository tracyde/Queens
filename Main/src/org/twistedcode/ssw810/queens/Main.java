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
public class Main {
    private static final Integer size = 8;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Queens");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLayout(new FlowLayout());

        Board board = new Board(size);
        frame.add(board);

        frame.setSize(500, 500);
        frame.setResizable(false);
        frame.setVisible(true);
    }
}
