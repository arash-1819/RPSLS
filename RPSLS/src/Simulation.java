import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import static java.lang.Integer.parseInt;
import static java.lang.Thread.sleep;

public class Simulation extends JPanel implements ActionListener, KeyListener {
    /**Variables*/
    // Properties of the display
        private final int EDGE = parseInt(config.prop.getProperty("EDGE"));
        private final Rectangle DISPLAY = new Rectangle(0, 0, EDGE, EDGE);
        private final Color backGroundColor = Color.decode((config.prop.getProperty("backGroundColor")));
    private final int DELAY =  parseInt(config.prop.getProperty("DELAY"));
    private final Timer timer;
    public static Piece[] Pieces = new Piece[parseInt(config.prop.getProperty("numberOfPieces"))];
    private boolean hurlDone = false;
    boolean paused = false;
    // UI's Variables
        private static final Color UIColor = Color.decode(config.prop.getProperty("UIColor"));
        private final Font CountDownFont = new Font("Monospaced", Font.ITALIC, 72);
        private final int restartWaitTime = parseInt(config.prop.getProperty("simulationRestartWaitTime"));
        private final int CountDown = parseInt(config.prop.getProperty("simulationStartCountDown"));
        int wait;
        int count;

    /**
     * CONSTRUCTOR
     */
    public Simulation() {
        setPreferredSize(new Dimension(EDGE, EDGE));
        setBackground(backGroundColor);

        timer = new Timer(DELAY, this);
        timer.start();

        wait = restartWaitTime;
        count = CountDown;
    } // end Simulation()


    /**
     * processes the changes in the simulation
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (!hurlDone) { // if the Pieces have not spawned yet
            Piece.hurlPieces(Pieces);
            hurlDone=true;
        }

        Piece.movePieces(Pieces);

        repaint(); // draws the new frame using paintComponent()

        // freezes the frame if wait does not equal 0
        if (wait!=0) {
            try {
                sleep(wait);
                wait=0;
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    /**
     * repaints the frame if the game is not paused
     * @param g the <code>Graphics</code> object to protect
     */
    @Override
    public void paintComponent(Graphics g) throws RuntimeException {
        if (!paused) { // if the game is not pause
            super.paintComponent(g); // erases the previous frame
            if (count > 0) { // new simulation countDown
                g.setColor(UIColor);
                UI.drawCenteredString(g, String.valueOf(count), DISPLAY, CountDownFont);
                count--;
                wait = 1000;
            } else {
                try {
                    if (UI.resetGame(g)) { // if the simulation is over
                        count = CountDown;
                        sleep(restartWaitTime/2);

                        wait = restartWaitTime/2;
                        UI.resetSB();
                        hurlDone = false;
                    }

                    if (hurlDone) { // mid simulation
                        Piece.drawPieces(g, this, Pieces);
                        UI.draw(g);
                    }
                } catch (InterruptedException e) {
                    super.paintComponent(g);
                    throw new RuntimeException(e);
                }
            }
            // makes the simulation a little smoother
            Toolkit.getDefaultToolkit().sync();
            setDoubleBuffered(true);
        }
    } // end paintComponent()


    @Override
    public void keyTyped(KeyEvent e) {} // end keyTyped()

    @Override
    public void keyPressed(KeyEvent e) {
        // "esc" will pause/unpause the simulation
        if (e.getKeyCode()==27)
            paused=!paused;
    } // end keyPressed()

    @Override
    public void keyReleased(KeyEvent e) {} // end keyReleased
}