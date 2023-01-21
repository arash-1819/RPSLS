/**
 * Rock Paper Scissors Lizard Spock:z
 * A game designed by Sheldon Cooper from Big Bang Theory
 * 12/30/2022
 * Arash Khavaran
 */

import javax.swing.*;

public class Main {
    /**
     * loads the simulation into a JFrame
     */
    private static void initWindow() {
        JFrame f = new JFrame("RPSLS");

        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Simulation s = new Simulation();
        f.add(s);
        f.addKeyListener(s);

        f.setResizable(false);
        f.pack();
        f.setLocationRelativeTo(null);

        f.setVisible(true);
    }

    public static void main(String[] args) {
        config.initializePropertyFile();
        SwingUtilities.invokeLater(Main::initWindow);
    }
}