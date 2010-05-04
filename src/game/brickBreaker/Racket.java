package brickBreaker;

import java.awt.*;
import java.io.Serializable;

/**
 * Represents a racket on one side of the screen, controlled by the user to keep the ball in the air.
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
    protected double speed = 0;
    protected Color color = Color.blue;
    public static int thickness = 5;
    protected boolean movingRight, movingLeft;

    /**
     * Sets variable movingLeft to true.
     */
    public void setMovingLeft() {
        movingLeft = true;
        movingRight = false;
    }

    /**
     * Sets variable movingRight to true.
     */
    public void setMovingRight() {
        movingRight = true;
        movingLeft = false;
    }

    /**
     * Stops the racket from moving altogether.
     */
    public void stop() {
        movingLeft = false;
        movingRight = false;
        speed = 0;
    }

    /**
     * Updates the racket's position on the screen, making sure it doesn't move outside the bounds of the screen.
     */
    public void updatePos() {
        if (movingRight) speed = Math.min(speed+2, maxSpeed);
        else if (movingLeft) speed = Math.max(speed-2, -maxSpeed);
        if (movingLeft || movingRight) {
            posX += speed;
            if (posX < width/2) posX = width/2;
            if (posX > (range-width/2)) posX = range-width/2;
        }
    }

    /**
     * Moves the racket left (up for VerticalRackets)
     */
    public  void moveLeft() {
        posX = Math.max(width/2., posX-speed);
    }

    /**
     * Moves the racket right (down for VerticalRackets)
     */
    public  void moveRight() {
        posX = Math.min(range-width/2., posX+speed);
    }

    /**
     * Returns the coordinate of the left end of the racket.
     */
    public int getLeft() { return Math.round((long)(posX-width/2.)); }

    /**
     * Returns the coordinate of the right end of the racket.
     */
    public int getRight() { return Math.round((long)(posX+width/2)); }

    /**
     * Returns the x-coordinate of the center of the racket.
     */
    public int getX() { return Math.round((long)posX); }

    /**
     * Returns the y-coordinate of the center of the racket.
     * @return
     */
    public int getY() { return Math.round((long)posY); }

    /**
     * Returns the width of the racket, in pixels.
     * @return
     */
    public int getWidth() { return width; }

    /**
     * Draws the racket on the screen.
     * @param g Graphics object with which to draw.
     */
    public abstract void draw(Graphics g);

    /**
     * Checks if the ball collides with the racket and updates the ball.
     * @param ball Ball object to check
     */
    public abstract Ball checkCollision(Ball ball);

    /**
     * Checks if the ball is past the racket (and thus out of play).
     * @param ball Ball object to check
     */
    public abstract boolean checkPast(Ball ball);

    /**
     * Returns true if the racket is on the left side of the screen.
     */
    public boolean isLeft() { return false; }

    /**
     * Returns true if the racket is on the right side of the screen.
     */
    public boolean isRight() { return false; }

    /**
     * Returns true if the racket is on the top of the screen.
     */
    public boolean isTop() { return false; }

    /**
     * Returns true if the racket is on the bottom of the screen.
     */
    public boolean isBottom() { return false; }
}
