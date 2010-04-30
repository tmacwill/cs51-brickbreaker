package brickBreaker;

import java.awt.*;
import java.io.Serializable;

/**
 * Abstract class Racket - write a description of the class here
 * 
 * @author (your name here)
 * @version (version number or date here)
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
    
    public void setMovingLeft() {
        if (!moving) {
            speed = -maxSpeed/2;
            moving = true;
        }
        else speed = -maxSpeed;
    }
    
    public void setMovingRight() {
        if (!moving) {
            speed = 3*maxSpeed/4;
            moving = true;
        }
        else speed = maxSpeed;
    }
    
    public void stop() {
        moving = false;
        speed = 0;
    }
    
    public void updatePos() {
        if (moving) {
            posX += speed;
            if (posX < width/2) posX = width/2;
            if (posX > (range-width/2)) posX = range-width/2;
        }
    }
    
    // Move the racket left/up
    public  void moveLeft() {
        posX = Math.max(width/2., posX-speed);
    }
    
    // Move the racket right/down
    public  void moveRight() {
        posX = Math.min(range-width/2., posX+speed);
    }
    
    public int getLeft() { return Math.round((long)(posX-width/2.)); }
    public int getRight() { return Math.round((long)(posX+width/2)); }
    public int getX() { return Math.round((long)posX); }
    public int getY() { return Math.round((long)posY); }
    public int getWidth() { return width; }
    
    // Draw the racket on the screen
    public abstract void draw(Graphics g);
    
    // Checks if the ball collides with the racket and updates the ball
    public abstract Ball checkCollision(Ball ball);
    
    // Checks if the ball is past the racket (and thus out of play)
    public abstract boolean checkPast(Ball ball);
    
    public boolean isLeft() { return false; }
    public boolean isRight() { return false; }
    public boolean isTop() { return false; }
    public boolean isBottom() { return false; }
}
