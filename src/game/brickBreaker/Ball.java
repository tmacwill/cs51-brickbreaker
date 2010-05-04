package brickBreaker;

import java.awt.*;
import java.io.Serializable;

/**
 * Represents a ball object, which bounces around the screen bouncing off walls, bricks and rackets.
 * 
 * @author Jacob Pritt
 * @version 4/30/10
 * @file Ball.java
 */
public class Ball implements Serializable
{
    private static final long serialVersionUID = 1L;
	
    public static final double pi = Math.PI;
    private double posX, posY;  // position of center of the ball
    private double angle;   // Measured from the horizontal line (+x axis) clockwise
    public static double speed;
    public static int powerLevel = 0;
    public static double ptMultiplier = 1;
    
    // x and y components of the ball's velocity vector (calculated from angle & speed)
    private double velX, velY;
    private int radius;
    private Color color = Color.red;
    
    private boolean inBounds = true;
    private boolean inPlay = true;
    
    private Racket[] rackets;
    private LevelPlayer level;

    /**
     * Constructor.
     *
     * @param r An array of all rackets in the game
     * @param radius The radius of the ball, in pixels
     */
    public Ball(Racket[] r, int radius)
    {
        rackets = r;
        this.radius = radius;
    }

    /**
     * Constructor.
     *
     * @param l The LevelPlayer in which the ball exists
     * @param r An array of all rackets in the game
     * @param radius The radius of the ball, in pixels
     */
    public Ball(LevelPlayer l, Racket[] r, int radius)
    {
        level = l;
        rackets = r;
        this.radius = radius;
    }

    /**
     * Sets the ball's location and direction to the given coordinates.
     *
     * @param x New x-coordinate
     * @param y New y-coordinate
     * @param angle New Angle
     */
    public void setLoc(double x, double y, double angle) {
        posX = x;
        posY = y;
        this.angle = angle;
        speed = 4.5;
        velX = speed*Math.cos(angle);
        velY = speed*Math.sin(angle);
        
        inBounds = true;
        inPlay = true;
    }

    /**
     * Changes the LevelPlayer in which the ball exists.
     * Usually called after the first constructor, in which the LevelPlayer is not specified.
     *
     * @param lev New LevelPlayer
     */
    public void setLevel(LevelPlayer lev) {
        level = lev;
    }
    
    /**
     * Updates the position of the ball and calculates the number of points earned.
     *
     * @return Returns the number of points earned during this time step.
     */
    public int updatePosition()
    {
        posX += velX;
        posY += velY;
        
        //  If the ball is out of bounds, move it back inbounds and change the direction of its velocity
        if (posX < radius) {
            if (!inPlay) inBounds = false;
            else bounce(radius, false);
        }
        else if (posX > (level.WIDTH-radius))  {
            if (!inPlay) inBounds = false;
            else bounce(level.WIDTH-radius, false);
        }
        if (posY < radius)  {
            if (!inPlay) inBounds = false;
            else bounce (radius, true);
        }
        else if (posY > (level.HEIGHT-radius))  {
            if (!inPlay) inBounds = false;
            bounce(level.HEIGHT-radius, true);
        }
            
        if (inPlay) {
            for (int i = 0; i < rackets.length; i++) {
                rackets[i].checkCollision(this);
                if (rackets[i].checkPast(this)) inPlay = false;
            }
        }
        return level.checkCollision(this);
    }

    /**
     * Draws the ball on the screen.
     *
     * @param g The graphics object with which to draw
     */
    public void draw(Graphics g) {
        g.setColor(color);
        int border = GamePanel.BORDER;
        g.fillOval(getX()-radius+border, getY()-radius+border, 2*radius, 2*radius);  
    }

    /**
     * @return Returns the current x-coordinate, rounded to the nearest int
     */
    public int getX() { return Math.round((long)posX); }

    /**
     * @return Returns the current y-coordinate, rounded to the nearest int
     */
    public int getY() { return Math.round((long)posY); }

    /**
     * @return Returns the ball's radius, in pixels
     */
    public int getRad() { return radius; }

    /**
     * Sets the x-coordinate to the new value.
     * @param x The new x-coordinate
     */
    public void setX(double x) { posX = x; }

    /**
     * Sets the y-coordinate to the new value.
     * @param y The new y-coordinate
     */
    public void setY(double y) { posY = y; }

    /**
     * @return Returns true if the ball is still in play
     */
    public boolean inBounds() { return inBounds; }
    
    /**
     * Resets all the static variables in this class to their initial values.
     */
    public static void resetVars() {
        powerLevel = 0;
        ptMultiplier = 1;
    }

    /**
     * Changes the ball's angle to the new value.
     * @param newAngle The new angle
     */
    public void changeAngle(double newAngle) {
        angle = newAngle % (2*pi);
        while (angle < 0) angle += 2*pi;
        velX = speed*Math.cos(angle);
        velY=  speed*Math.sin(angle);
    }

    /**
     * Returns the ball's current angle.
     */
    public double getAngle() { return angle; }
    
    /**
     * Checks if the ball is colliding with the given object
     * If a collision exists and changeDir is true, it changes the ball's direction
     *
     * @param loc A block to be tested, represented by an int array containing, in order, x, y, width and height of the brick
     * @return Returns true if the ball hit the object, false if not
     */
    public boolean checkCollision(int[] loc) {
        int x = loc[0];
        int y = loc[1];
        int w = loc[2];
        int h = loc[3];
        if ( posY > (y-radius) && posY < (y+h/2) && posX >= x && posX <= (x+w) ) {
            bounce(y-radius, true);
            return true;
        }
        else if ( posY < (y+h+radius) && posY > (y+h/2) && posX >= x && posX <= (x+w) ) {
            bounce(y+h+radius, true);
            return true;
        }
        else if ( posX > (x-radius) && posX < (x+w/2) && posY >= y && posY <= (y+h) ) {
            bounce(x-radius, false);
            return true;
        }
        else if ( posX < (x+w+radius) && posX > (x+w/2) && posY >= y && posY <= (y+h) ) {
            bounce(x+w+radius, false);
            return true;
        }
        return false;
    }
    
    /**
     * Bounces this ball off the given wall. The wall is either horizontal or vertical, specified by param horizontal.
     * @param x The location of the wall
     * @param horizontal True if the wall is horizontal, false if it is vertical
     */
    public void bounce(int x, boolean horizontal)
    {
        if (horizontal) {
            posY = 2*x - posY;
            changeAngle(-angle);
        }
        else {
            posX = 2*x - posX;
            changeAngle(pi-angle);
        }
        changeAngle(angle + 0.2*Math.random()-0.1);
    }
        
}
