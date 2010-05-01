package brickBreaker;

import java.awt.*;
import java.awt.event.*;

/**
 * Controls the updating and interation of different components in a Level object.
 * 
 * @author Jacob Pritt
 * @version 4/30/10
 * @file LevelPlayer.java
 * @see Level.java
 */
public class LevelPlayer
{
    private Level level;
    private Racket[] rackets;
    private Ball[] balls;
    private Brick[][] bricks;
    private int brickWidth, brickHeight;
    private int numRows, numCols;
    public static int WIDTH, HEIGHT;

    /**
     * Constructor
     * @param lev The Level object containing the components used by this class
     */
    public LevelPlayer(Level lev)
    {
        level = lev;
        rackets = level.getRackets();
        balls = level.getBalls();
        for (int i = 0; i < balls.length; i++) balls[i].setLevel(this);
        
        bricks = level.getBricks();
        brickWidth = level.brickWidth();
        brickHeight = level.brickHeight();
        numRows = level.numRows();
        numCols = level.numCols();
        WIDTH = level.WIDTH;
        HEIGHT = level.HEIGHT;
    }
    
    
    /**
     * Updates the components in the level and returns the score earned.
     * @return Returns the number of points earned in this timestep
     */
    public int update() {
        for (int i = 0; i < rackets.length; i++) {
            rackets[i].updatePos();
        }
        int score = 0;
        for (int i = 0; i < balls.length; i++) {
            score += balls[i].updatePosition();
        }
        return score;
    }

    /**
     * Tests whether all balls are still in play.
     * @return Returns true if all the balls are still in bounds
     */
    public boolean ballsInBounds() { 
        for (int i = 0; i < balls.length; i++)
            if (!balls[i].inBounds())return false;
        return true;
    }
    
    /**
     * Tests whether the position [x][y] (in the 2d array of bricks) is occupied by a brick.
     *
     * @param x The x-coordinate of the position in the brick grid
     * @param y The y-coordinate of the position in the brick grid
     * @return Returns true if the given position is occupied by a brick, false if it is empty
     */
    public boolean isFilled (int x, int y) {
        if (x < 0 || y < 0 || y >= numRows || x >= numCols) return false;
        return (bricks[x][y] != null);
    }
    
    /**
     * Removes the box at [x][y] in the array of bricks.
     *
     * @param x The x-coordinate of the position in the brick grid
     * @param y The y-coordinate of the position in the brick grid
     * @param b The ball which just bounced against the brick
     * @return Returns the number of points earned by removing this brick
     */
    public int removeBrick (int x, int y, Ball b) {
        if (x < 0 || y < 0 || y >= numRows || x >= numCols) return 0;
        if (bricks[x][y] == null) return 0;
        else {
            int pts = bricks[x][y].bounce(b);
            if (bricks[x][y].removed()) bricks[x][y] = null;
            return pts;
        }
    }

    /**
     * Checks whether the given ball is colliding with any brick, and updates the bricks if needed.
     *
     * @param b The ball to be tested
     * @return Returns the number of points earned (zero if no bricks were removed)
     */
    public int checkCollision(Ball b) {
        int boxX = (int)b.getX()/brickWidth;
        int boxY= (int)b.getY()/brickHeight;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (isFilled(boxX+i,boxY+j)) {
                    if (b.checkCollision(bricks[boxX+i][boxY+j].getLoc()))
                        return removeBrick(boxX+i,boxY+j, b);                    
                }
            }
        }
        return 0;
    }

    /**
     * Tests whether all of the bricks have been cleared from the level.
     * @return Returns true if all bricks have been removed (ignoring permanent bricks)
     */
    public boolean cleared() {
        for (int i = 0; i < bricks.length; i++) {
            for (int j = 0; j < bricks[0].length; j++) {
                if (bricks[i][j] != null && !bricks[i][j].permanent()) return false;
            }
        }
        return true;
    }
                
    /**
     * Returns true if there is a racket on the left side of the screen
     */
    public boolean racketLeft() {
        for (int i = 0; i < rackets.length; i++) if (rackets[i].isLeft()) return true;
        return false;
    }
    
    /**
     * Returns true if there is a racket on the right side of the screen
     */
    public boolean racketRight() {
        for (int i = 0; i < rackets.length; i++) if (rackets[i].isRight()) return true;
        return false;
    }
    
    /**
     * Returns true if there is a racket on the top of the screen
     */
    public boolean racketTop() {
        for (int i = 0; i < rackets.length; i++) if (rackets[i].isTop()) return true;
        return false;
    }
    
    /**
     * Returns true if there is a racket on the bottom of the screen
     */
    public boolean racketBottom() {
        for (int i = 0; i < rackets.length; i++) if (rackets[i].isBottom()) return true;
        return false;
    }

    /**
     * Draws all of the level components on the screen.
     * @param g Graphics object to draw with
     */
    public void drawComponents(Graphics g) {
        for (int x = 0; x < bricks.length; x++) {
            for (int y = 0; y < bricks[0].length; y++) {
                if (bricks[x][y] != null) bricks[x][y].draw(g);
            }
        }
        for (int i = 0; i < rackets.length; i++) rackets[i].draw(g);
        for (int i = 0; i < balls.length; i++) balls[i].draw(g);
    }

    /**
     * Actions to be performed when a key is pressed
     * @param code Int key code
     */
    public void keyPressed(int code) {
        if (code == KeyEvent.VK_LEFT) { rackets[0].setMovingLeft(); }
        else if (code == KeyEvent.VK_RIGHT) { rackets[0].setMovingRight(); }
        else if (code == KeyEvent.VK_A && rackets.length > 1) { rackets[1].setMovingLeft(); }
        else if (code == KeyEvent.VK_S && rackets.length > 1) { rackets[1].setMovingRight(); }
    }

    /**
     * Actions to be performed when a key is released
     * @param code Int key code
     */
    public void keyReleased(int code) {
        if (code == KeyEvent.VK_LEFT || code == KeyEvent.VK_RIGHT) { 
            rackets[0].stop(); }
        else if ((code == KeyEvent.VK_A || code == KeyEvent.VK_S) && rackets.length > 1) {
            rackets[1].stop(); }
    }    
}
