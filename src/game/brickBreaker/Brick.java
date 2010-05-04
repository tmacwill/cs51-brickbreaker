package brickBreaker;

import java.awt.*;
import java.io.Serializable;

/**
 * Abstract class Brick - represents a brick object on the screen
 * 
 * @author Jacob Pritt
 * @version 4/30/10
 * @file Brick.java
 * @see StandardBrick.java, PowerBrick.java, PermanentBrick.java
 */
public abstract class Brick implements Serializable
{
    private static final long serialVersionUID = 1L;
	
    private int points;
    private boolean destructible = true;
    private int hitsToRemove;
    private boolean removed = false;
    private Color color;
    private int x, y, width, height;    // Location of box

    /**
     * Constructor.
     *
     * @param pts Points earned for destroying this brick
     * @param hits Number of hits needed to remove this brick
     * @param c Color of brick
     */
    public Brick(int pts, int hits, Color c) {
        points = pts;
        if (hits == -1) destructible = false;
        hitsToRemove = hits;
        color = c;
    }

    /**
     * Sets the location and dimensions of the brick to the new parameters.
     *
     * @param x New x-coordinate of upper left corner
     * @param y New y-coordinate of upper left corner
     * @param wid New width
     * @param hgt New height
     */
    public void setLoc(int x, int y, int wid, int hgt) {
        this.x = x;
        this.y = y;
        width = wid;
        height = hgt;
    }

    /**
     * Updates this brick after the ball has bounced off it.
     * @param ball The ball that bounced off the brick
     * @return The number of points earned
     */
    public int bounce(Ball ball)
    {
        powerUp(ball);
        if (!destructible) return 0;
        else {
            hitsToRemove--;
            if (hitsToRemove == 0) {
                removed = true;
                return (int)(points*ball.ptMultiplier);
            }
            else return 0;
        }
    }

    /**
     * @return Returns whether or not the brick has been removed
     */
    public boolean removed() { return removed; }

    /**
     * Returns the location and dimensions of the brick as an int array
     * @return Returns an array containing, in order, the x and y coordinates of the upper left corner and the width and height
     */
    public int[] getLoc() {
        int[] loc = {x, y, width, height};
        return loc;
    }

    /**
     * Powers up the ball (e.g. faster speed, higher point multiplier, etc).
     * @param b The ball to be updated
     */
    public void powerUp(Ball b) {}

    /**
     * @return Returns true if the brick cannot be removed.  Default = false
     */
    public boolean permanent() { return false; }

    /**
     * Draws the brick on the screen.
     * @param g The graphics object with which to draw.
     */
    public void draw(Graphics g) {
        g.setColor(color);
        int border = GamePanel.BORDER;
        g.fillRect(x+border+2, y+border+2, width-3, height-3);
    }
}
