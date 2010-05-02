package brickBreaker;

import java.awt.*;
import java.io.Serializable;

/**
 * Abstract class Racket - represents a racket object, which can be moved back and forth by the user to keep the ball in play.
 * Currently only two classes extend Racket: HorizontalRacket and VerticalRacket
 * 
 * @author Jacob Pritt
 * @version 4/30/10
 * @file Racket.java
 * @see HorizontalRacket.java, VerticalRacket.java
 */
public abstract class Racket implements Serializable
{
    private static final long serialVersionUID = 1L;
	
    protected int range;  // Size of the area over which the racket can move
    protected int width;  // Size of racket
    
    // Position of the center of the racket
    // Coordinates are relative to the racket (i.e. in side rackets, coordinates are switched
    protected double posX, posY;
    public static final double maxSpeed = 8.0;
    private double speed = 0;
    protected Color color = Color.blue;
    public static int thickness = 5;
    
    boolean moving = false;

    /**
     * Increases the speed of the racket to the left.
     * speed starts from zero and increases to maxSpeed in 2 steps.
     */
    public void setMovingLeft() {
        if (!moving) {
            speed = -maxSpeed/2;
            moving = true;
        }
        else speed = -maxSpeed;
    }

    /**
     * Increases the speed of the racket to the right.
     * speed starts from zero and increases to maxSpeed in 2 steps.
     */

    public void setMovingRight() {
        if (!moving) {
            speed = 3*maxSpeed/4;
            moving = true;
        }
        else speed = maxSpeed;
    }

    /**
     * Stops the racket movement.
     */
    public void stop() {
        moving = false;
        speed = 0;
    }

    /**
     * Updates the position of the racket.
     */
    public void updatePos() {
        if (moving) {
            posX += speed;
            if (posX < width/2) posX = width/2;
            if (posX > (range-width/2)) posX = range-width/2;
        }
    }

    /**
     * Returns the position of the left end of the racket, rounded to an int.
     */
    public int getLeft() { return Math.round((long)(posX-width/2.)); }

    /**
     * Returns the position of the right end of the racket, rounded to an int.
     */
    public int getRight() { return Math.round((long)(posX+width/2)); }

    /**
     * Returns the x-coordinate of the center of the racket, rounded to an int.
     */
    public int getX() { return Math.round((long)posX); }

    /**
     * Returns the y-coordinate of the center of the racket, rounded to an int.
     */
    public int getY() { return Math.round((long)posY); }

    /**
     * Returns the width of the racket.
     */
    public int getWidth() { return width; }
    
    /**
     * Draw the racket on the screen.
     * @param g Graphics object to draw with
     */
    public abstract void draw(Graphics g);

    /**
     * Checks if the ball collides with the racket and updates the ball
     *
     * @param ball Ball object to check
     * @return Returns the updated ball
     */
    public abstract Ball checkCollision(Ball ball);

    /**
     * Checks if the ball is past the racket (and thus out of play).
     *
     * @param ball The Ball object to check
     * @return Returns true if the ball has passed the racket
     */
    public abstract boolean checkPast(Ball ball);

    /**
     * Returns true if the racket is on the left side of the screen (false by default)
     */
    public boolean isLeft() { return false; }

    /**
     * Returns true if the racket is on the right side of the screen (false by default)
     */
    public boolean isRight() { return false; }

    /**
     * Returns true if the racket is on the top of the screen (false by default)
     */
    public boolean isTop() { return false; }

    /**
     * Returns true if the racket is on the bottom of the screen (false by default)
     */
    public boolean isBottom() { return false; }
}
