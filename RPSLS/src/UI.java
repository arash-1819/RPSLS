import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;

public class UI {
    /**
     * Variables
     */
    // variables for score board, SB
        private static int rock;
        private static int paper;
        private static int scissors;
        private static int spock;
        private static int lizard;
    // variables for round board, RB
        private static int ROCK = 0;
        private static int PAPER = 0;
        private static int SCISSORS = 0;
        private static int SPOCK = 0;
        private static int LIZARD =0;
    private static final int NoP = Integer.parseInt(config.prop.getProperty("numberOfPieces"));
    private final static Color UIColor = Color.decode(config.prop.getProperty("UIColor"));
    private final static Font SBFont = new Font("Monospaced", Font.ITALIC, 15);
    private static final Font ResultsFont = new Font("Monospaced", Font.ITALIC, 30);
    private static String SBtext;
    private static String RBtext = "Rock: " + ROCK + "    Paper: " + PAPER + "    Scissors: " + SCISSORS + "    spock: " + SPOCK + "    lizard: " + LIZARD;

    // properties of the display
        private static final int EDGE = Integer.parseInt(config.prop.getProperty("EDGE"));
        private static final int MARGIN = Integer.parseInt(config.prop.getProperty("MARGIN"));
        private static final Rectangle DISPLAY = new Rectangle(0, 0, EDGE, EDGE);
    private static final Rectangle SBRect = new Rectangle(MARGIN, EDGE - MARGIN, EDGE - 2 * MARGIN, MARGIN);
    private static final Rectangle RBRect = new Rectangle(MARGIN, 0, EDGE - 2 * MARGIN, MARGIN);

    /**
     * draws the rectangle of the boundaries of the simulation and the Score Board(SB)
     */
    public static void draw(Graphics g) {
        g.setColor(UIColor);

        // hurling squares of each type
//        g.drawRect(EDGE/2-hurlEdge/2, MARGIN, hurlEdge, hurlEdge);
//
//        g.drawRect(MARGIN, 4*EDGE/11, hurlEdge, hurlEdge);
//        g.drawRect(EDGE-MARGIN-hurlEdge, 4*EDGE/11, hurlEdge, hurlEdge);
//
//        g.drawRect(MARGIN+EDGE/6, EDGE-MARGIN-hurlEdge, hurlEdge, hurlEdge);
//        g.drawRect(EDGE-MARGIN-EDGE/6-hurlEdge, EDGE-MARGIN-hurlEdge, hurlEdge, hurlEdge);

        // boundary of the simulation
        g.drawRect(MARGIN, MARGIN, EDGE-2*MARGIN, EDGE-2*MARGIN);

        // draws the updated Score Board
        updateSB();
        drawCenteredString(g, SBtext, SBRect, SBFont);
        // draws the round board which gets updated in resetGame()
        drawCenteredString(g, RBtext, RBRect, SBFont);
    } // end draw()

    /**
     * updated the score and the score board text by counting the number of each type in the Pieces array in the Simulation
     */
    public static void updateSB() throws RuntimeException {
        rock = 0;
        paper = 0;
        scissors = 0;
        spock = 0;
        lizard = 0;

        for (Piece p : Simulation.Pieces) {
            switch (p.getType().getID()) {
                case 0 -> rock++;
                case 1 -> paper++;
                case 2 -> scissors++;
                case 3 -> spock++;
                case 4 -> lizard++;
            } // end switch
        }

        SBtext = "Rock: " + rock + "    Paper: " + paper + "    Scissors: " + scissors + "    spock: " + spock + "    lizard: " + lizard;
    } // end updateSB()

    /**
     * checks if the game has ended an announces the winner if so and updates the round board
     * @return true or false based on the results
     */
    public static boolean resetGame(Graphics g) throws InterruptedException {
        if (rock==NoP) {
            ROCK++;
            RBtext = "Rock: " + ROCK + "    Paper: " + PAPER + "    Scissors: " + SCISSORS + "    spock: " + SPOCK + "    lizard: " + LIZARD;
            return announceWinner(g, "Rock");
        }
        else if (paper==NoP) {
            PAPER++;
            RBtext = "Rock: " + ROCK + "    Paper: " + PAPER + "    Scissors: " + SCISSORS + "    spock: " + SPOCK + "    lizard: " + LIZARD;
            return announceWinner(g, "Paper");
        }
        else if (scissors==NoP) {
            SCISSORS++;
            RBtext = "Rock: " + ROCK + "    Paper: " + PAPER + "    Scissors: " + SCISSORS + "    spock: " + SPOCK + "    lizard: " + LIZARD;
            return announceWinner(g, "Scissors");
        }
        else if (spock==NoP) {
            SPOCK++;
            RBtext = "Rock: " + ROCK + "    Paper: " + PAPER + "    Scissors: " + SCISSORS + "    spock: " + SPOCK + "    lizard: " + LIZARD;
            return announceWinner(g, "Spock");
        }
        else if (lizard==NoP) {
            LIZARD++;
            RBtext = "Rock: " + ROCK + "    Paper: " + PAPER + "    Scissors: " + SCISSORS + "    spock: " + SPOCK + "    lizard: " + LIZARD;
            return announceWinner(g, "Lizard");
        }
        else // simulation not finished
            return false;
    } // end resetGame()

    /**
     * resets the score of each Piece
     */
    public static void resetSB() {
        rock = 0;
        paper = 0;
        scissors = 0;
        spock = 0;
        lizard = 0;
    } // end resetSB

    /**
     * announces the text input as the winner
     * @param s wiiner
     * @return true for resetGame
     * this method is only called when the game is being rested
     */
    private static boolean announceWinner(Graphics g, String s) {
        drawCenteredString(g, s + " won!", DISPLAY, ResultsFont);
        return true;
    } // end announceWinner()

    /**
     * draws a text with a specific font in center of a rectangle
     * @param g the graphics class to draw the player on
     * @param t text to draw in the Rectangle r
     * @param r the rectangle to draw the tex in its center
     * @param f font
     */
    public static void drawCenteredString(Graphics g, String t, Rectangle r, Font f) {
        // get the bound of the text written with the font
        FontRenderContext frc = new FontRenderContext(null, true, true);
        Rectangle2D fRect = f.getStringBounds(t, frc);

        // get the values of the rectangle to write in
        int rWidth = (int) Math.round(fRect.getWidth());
        int rHeight = (int) Math.round(fRect.getHeight());
        int rX = (int) Math.round(fRect.getX());
        int rY = (int) Math.round(fRect.getY());

        // calculate the point to draw the string from
        int a = (r.width / 2) - (rWidth / 2) - rX + r.x;
        int b = (r.height / 2) - (rHeight / 2) - rY + r.y;

        // draw the test
        g.setFont(f);
        g.drawString(t, a, b);
    } // end drawCenteredString()
}