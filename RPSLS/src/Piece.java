import java.awt.*;
import java.awt.image.ImageObserver;
import java.util.HashMap;
import java.util.Random;

import static java.lang.Math.abs;

public class Piece {
    /**Variables*/
    private Type type;
    private Point position;
    private int xSpeed;
    private int ySpeed;
    private final static int speedConstant = Integer.parseInt(config.prop.getProperty("speedConstant"));
    private final static int minStartingSpeed = Integer.parseInt(config.prop.getProperty("minStartingSpeed"))*speedConstant;
    private final static int maxStartingSpeed = Integer.parseInt(config.prop.getProperty("maxStartingSpeed"))*speedConstant;

    // properties of the display
        private static final int EDGE = Integer.parseInt(config.prop.getProperty("EDGE"));
        private static final int MARGIN = Integer.parseInt(config.prop.getProperty("MARGIN"));
    /** types of pieces */
        private static final Type Rock = new Type(0,"Rock");
        private static final Type Paper = new Type(1, "Paper");
        private static final Type Scissors = new Type(2, "Scissors");
        private static final Type Spock = new Type(3, "Spock");
        private static final Type Lizard = new Type(4, "Lizard");
    /** hurling Variables */
        private static final int hurlEdge = Integer.parseInt(config.prop.getProperty("hurlEdge"));
        private static final int hurlMargin = Integer.parseInt(config.prop.getProperty("hurlMargin"));
        private static final HashMap<Integer, Point> hurlTypePointMap = new HashMap<>();

    /**
     * CONSTRUCTOR
     * @param t type of the Pieces
     * @param p hurl point
     */
    public Piece(Type t, Point p) {
        type = t;
        position = p;

        xSpeed = new Random().nextInt(2*maxStartingSpeed)-maxStartingSpeed;
        ySpeed = new Random().nextInt(2*maxStartingSpeed)-maxStartingSpeed;
    } // end Piece()

    /**
     * @return type of the Piece
     */
    public Type getType() {
        return type;
    } // end getType

    /**
     * @return the length of the edge of the Piece which is the length of the edge of the icon in its type
     */
    public int getEdge() {
        return type.getEdge();
    } // end getEdge()

    /**
     * @return the ID number of the type of the Piece
     */
    public int getID() {
        return type.getID();
    } // end getID()

    /**
     * @return the X coordinate
     */
    public int getX() {
        return position.x;
    } // end getX()

    /**
     * @return the Y coordinate
     */
    public int getY() {
        return position.y;
    } // end getY()

    /**
     * @return xSpeed
     */
     int getXSpeed() {
        return xSpeed;
    } // end getXSpeed()

    /**
     * @return getYSpeed()
     */
    public int getYSpeed() {
        return ySpeed;
    } // end getYSpeed

    /**
     * @return the shape of the Piece as a Rectangle
     */
    public Rectangle shape() {
        return new Rectangle(position.x, position.y, getEdge(), getEdge());
    } // end shape()

    /**
     * updates the Type of the Piece
     */
    public void setType(Type type) {
        this.type = type;
    } // end setType()

    /**
     * updates the position of the Piece
     * @param x X Coordinates
     * @param y Y Coordinates
     */
    public void setPosition(int x, int y) {
        position = new Point(x, y);
    } // end setPosition()

    /**
     * updates the X Coordinate
     */
    public void setX(int x) {
        position.x=x;
    } // end setX()

    /**
     * updates the Y Coordinate
     */
    public void setY(int y) {
        position.y=y;
    } // end setY()

    /**
     * updates xSpeed
     */
    public void setXSpeed(int xS) {
        xSpeed = xS;
    } // ebd setXSpeed()

    /**
     * updates ySpeed
     */
    public void setYSpeed(int yS) {
        ySpeed = yS;
    } // end setYSpeed()

    /**
     * assigns a type and a position to each Piece
     * @param p an array of Pieces
     */
    public static void hurlPieces(Piece[] p) {
        // starting Coordinates for Pieces for each type
        hurlTypePointMap.put(0, new Point(EDGE / 2 - hurlEdge / 2, MARGIN));
        hurlTypePointMap.put(1, new Point(MARGIN, 4 * EDGE / 11));
        hurlTypePointMap.put(2, new Point(EDGE - MARGIN - hurlEdge, 4 * EDGE / 11));
        hurlTypePointMap.put(3, new Point(MARGIN + EDGE /5, EDGE - MARGIN - hurlEdge + hurlMargin));
        hurlTypePointMap.put(4, new Point(EDGE + MARGIN - EDGE / 5 - hurlEdge, EDGE - MARGIN - hurlEdge + hurlMargin));

        Type t = null;
        for (int i = 0; i < p.length; i++) { // for every element in the array
            switch (i / (p.length / 5)) {
                case (0) -> t = Rock;
                case (1) -> t = Paper;
                case (2) -> t = Scissors;
                case (3) -> t = Spock;
                case (4) -> t = Lizard;
            } // end switch

            // the position is based on the type and the index of the element in the array
            p[i] = new Piece(t,
                    new Point((hurlTypePointMap.get(i / (p.length / 5))).x + ((i % (p.length / 5)) % 5) * hurlMargin,
                            (hurlTypePointMap.get(i / (p.length / 5))).y + ((i % (p.length / 5)) % 4) * hurlMargin)
            );
        } // end for
    } // end hurlPieces()

    /**
     * draws the Pieces on the Canvas
     */
    private void draw(Graphics g, ImageObserver observer) {
        g.drawImage(type.getIcon(), position.x, position.y, observer);
    } // end draw()

    /**
     * draw all the Pieces in and array of Pieces
     * @param p array of Pieces
     */
     static void drawPieces(Graphics g, ImageObserver observer, Piece[] p) {
        for (Piece piece : p) piece.draw(g, observer);
    } // end drawPieces()

    /**
     * calculates and updates the speed and position of each Piece in the Pieces array
     * tilts the Pieces toward the center and same type Pieces away from each other
     * @param p array of Pieces
     */
    public static void movePieces(Piece[] p) {
        int t1; // temp integer variable
        int t2; // temp integer variable

        // makes an array of x and y speeds of all Pieces
        int[] xSpeed = new int[p.length];
        for (int i=0; i<p.length; i++)
            xSpeed[i]=p[i].getXSpeed();

        int[] ySpeed = new int[p.length];
        for (int i=0; i<p.length; i++)
            ySpeed[i]=p[i].getYSpeed();

        /**
         * Calculates the updated type, xSpeed, and ySPeed of each Piece.
         * checks all the Pieces to see if it is touching another Piece.
         * if true, changes the type of the pieces if it is needed.
         * then, determines the speed of x and the y speed of the first Piece. The new values are written in the arrays.
         */
        for (int i=0; i<p.length; i++) {
            for (int j = 0; j < p.length; j++) {
                if (i != j && (p[i].shape()).intersects(p[j].shape())) { // if the Pieces collide
                    /**
                     *   Rock
                     *   0
                     *   Paper
                     *   1
                     *   Scissors
                     *   2
                     *   Spock
                     *   3
                     *   Lizard
                     *   4
                     *
                     *           --------
                     *
                     *   Rock-Paper
                     *           -1 Paper
                     *   Rock-Scissors
                     *           -2 Rock
                     *   Rock-Spock
                     *           -3 Spock
                     *   Rock-Lizard
                     *           -4 Rock
                     *
                     *   Paper-Rock
                     *   1  Paper
                     *   Paper-Scissors
                     *           -1 Scissors
                     *   Paper-Spock
                     *           -2 Paper
                     *   Paper-Lizard
                     *           -3 Lizard
                     *
                     *   Scissors-Rock
                     *   2  Rock
                     *   Scissors-Paper
                     *   1  Scissors
                     *   Scissors-Spock
                     *           -1 Spock
                     *   Scissors-Lizard
                     *           -2 Scissors
                     *
                     *   Spock-Rock
                     *   3  Spock
                     *   Spock-Paper
                     *   2  Paper
                     *   Spock-Scissors
                     *   1  Spock
                     *   Spock-Lizard
                     *           -1 Lizard
                     *
                     *   Lizard-Rock
                     *   4  Rock
                     *   Lizard-Paper
                     *   3  Lizard
                     *   Lizard-Scissors
                     *   2  Scissors
                     *   Lizard-Spock
                     *   1  Lizard
                     *
                     *           --------
                     *
                     *   W: -4, -2, 1, 3
                     *   L: -3, -1, 2, 4
                     */
                    t1 = p[i].getID() - p[j].getID();
                    if (t1 == -3 || t1 == -1 || t1 == 2 || t1 == 4) // changing the type of the first Piece if it has lost
                            p[i].setType(p[j].getType());

                    // sets the new XSpeed of the Piece based on how it collided with the other Piece
                    if ((p[i].getXSpeed() > 0 && p[j].getXSpeed() > 0) || (p[i].getXSpeed() < 0 && p[j].getXSpeed() < 0))
                        xSpeed[i] = (xSpeed[i] + p[j].getXSpeed()) / 2;
                    else if ((p[i].xSpeed > 0 && p[j].getXSpeed() < 0) || (p[i].xSpeed < 0 && p[j].getXSpeed() > 0))
                        xSpeed[i] = p[j].getXSpeed();
                    // sets the new YSpeed of the Piece based on how it collided with the other Piece
                    if ((p[i].ySpeed > 0 && p[j].getYSpeed() > 0) || (p[i].ySpeed < 0 && p[j].getYSpeed() < 0))
                        ySpeed[i] = (ySpeed[i] + p[j].getYSpeed()) / 2;
                    else if ((p[i].ySpeed > 0 && p[j].getYSpeed() < 0) || (p[i].ySpeed < 0 && p[j].getYSpeed() > 0))
                        ySpeed[i] = p[j].getYSpeed();
                }
            } // end for j
        } // end for i

        // updates the speed and position based on the calculated speed, display boundaries, and speed limits
        for (int i=0; i<p.length; i++) { /* for each piece in the Pieces array: */
            // the xSpeed and the ySpeed are updated from the arrays
            p[i].setXSpeed(xSpeed[i]);
            p[i].setYSpeed(ySpeed[i]);

            // checking if the Piece has crosses its x and y bounds
            if (p[i].getX() < MARGIN) { // left Edge
                p[i].setX(MARGIN);
                p[i].setXSpeed(-p[i].getXSpeed());
            } else if (p[i].getX() > EDGE-MARGIN-p[i].getEdge()) { // right Edge
                p[i].setX(EDGE-MARGIN-p[i].getEdge());
                p[i].setXSpeed(-p[i].getXSpeed());
            }
            if (p[i].getY() < MARGIN) { // top Edge
                p[i].setY(MARGIN);
                p[i].setYSpeed(-p[i].getYSpeed());
            } else if (p[i].getY() > EDGE-MARGIN-p[i].getEdge()) { // bottom Edge
                p[i].setY(EDGE-MARGIN-p[i].getEdge());
                p[i].setYSpeed(-p[i].getYSpeed());
            }

            // if the y or x Speed has gone below the minimum, they will be updated to a random speed between min and max speeds.
            if (minStartingSpeed > abs(p[i].getXSpeed()))
                p[i].setXSpeed(new Random().nextInt(2*maxStartingSpeed)-maxStartingSpeed);
            if (minStartingSpeed > abs(p[i].getYSpeed()))
                p[i].setYSpeed(new Random().nextInt(2*maxStartingSpeed)-maxStartingSpeed);

            // update the positions based on the updated speed
            p[i].setPosition(p[i].getX()+p[i].getXSpeed(), p[i].getY()+p[i].getYSpeed());
        } // end for i

        // updated the position, so the pieces do not overlap, and push the pieces towards the center
        for (int i=0; i<p.length; i++) {
            for (int j = 0; j < p.length; j++) {
                if (i != j && (p[i].shape()).intersects(p[j].shape())) { // if the Pieces touch

                    t1 = p[i].getX() - p[j].getX(); // the difference of the x valued of each Piece
                    if (abs(t1)<(hurlMargin+1) && t1>=0) {
                        // move the Pieces away from each other with
                        p[i].setX(p[i].getX() + (int) Math.ceil(((double) (hurlMargin - t1))/2) + 2);
                        p[j].setX(p[j].getX() - (int) Math.ceil(((double) (hurlMargin - t1))/2) - 2);

                        t2 = (p[i].getX() + p[j].getX())/2; // the average value of the x values of the Pieces
                        if (t2>3*EDGE/4) { // if they are too close to the right edge
                            p[i].setX(p[i].getX() - 3);
                            p[j].setX(p[j].getX() - 3);
                        } else if (t2<EDGE/4) { // if they are too close the left edge
                            p[i].setX(p[i].getX() + 3);
                            p[j].setX(p[j].getX() + 3);
                        }
                    }

                    t1 = p[i].getY() - p[j].getY(); // the difference of the y valued of each Piece
                    if (abs(t1)<(hurlMargin+1) && t1>=0) {
                        p[i].setY(p[i].getY() + (int) Math.ceil(((double) (hurlMargin - t1))/2) +2);
                        p[j].setY(p[j].getY() - (int) Math.ceil(((double) (hurlMargin - t1))/2) -2);

                        t2 = (p[i].getY() + p[j].getY())/2; // the average value of the y values of the Pieces
                        if (t2>3*EDGE/4) { // if they are too close to the bottom edge
                            p[i].setY(p[i].getY() - 3);
                            p[j].setY(p[j].getY() - 3);
                        } else if (t2<EDGE/4) { // if they are too close to the top edge
                            p[i].setY(p[i].getY() + 3);
                            p[j].setY(p[j].getY() + 3);
                        }
                    }
                }
            } // end for j
        } // end for i
    } // end movePieces()
} // end Class Piece
