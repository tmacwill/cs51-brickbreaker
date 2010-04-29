package brickBreaker;

import java.awt.*;

/**
 * Write a description of class VerticalRacket here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class VerticalRacket extends Racket
{
    boolean left;

    /**
     * Constructor for objects of class SideRacket
     */
    public VerticalRacket(int arenaW, int arenaH, int racketW, boolean left)
    {
        range = arenaH;
        width = racketW;
        posX = range/2.;
        this.left = left;
        
        if (left) posY = 50;
        else posY = arenaW - 50;
    }

    /**
     * Draws the racket on the screen
     */
    public void draw(Graphics g) {
        g.setColor(color);
        int border = GamePanel.BORDER;
        g.fillRect(getY()+border, getLeft()+border, thickness, width);
    }
    
    /**
     * Checks if the ball collides with the racket and returns the updated ball
     */
    public Ball checkCollision(Ball ball) {
        int x = ball.getX();
        int y = ball.getY();
        int r = ball.getRad();
        if (y >= getLeft() && y <= getRight()) {
            if (left && x < (posY+thickness+r) && x >= posY) {
                ball.bounce(getY()+thickness+r, false);
                if (movingLeft || movingRight) {
                    double newAngle = ball.getAngle() - Math.PI/6.0*speed/maxSpeed;
                    
                    // Make sure the ball bounces back down
                    if (newAngle > Math.PI && newAngle < 3*Math.PI/2+0.15) newAngle = 3*Math.PI/2+0.15;
                    else if (newAngle < Math.PI && newAngle > Math.PI/2-0.15) newAngle = Math.PI/2-0.15;
                    ball.changeAngle(newAngle);
                }
            }
            else if (!left && x > (posY-r) && x <= (posY+thickness)) {
                ball.bounce(getY()-r, false);
                if (movingLeft || movingRight) {
                    double newAngle = ball.getAngle() + Math.PI/6.0*speed/maxSpeed;
                    
                    // Make sure the ball bounces back up
                    if (newAngle < Math.PI/2+0.15) newAngle = Math.PI/2+0.15;
                    else if (newAngle > 3*Math.PI/2-0.15) newAngle = 3*Math.PI/2-0.15;
                    ball.changeAngle(newAngle);
                }
            }
        }
        return ball;
    }
    
    // Checks if the ball is past the racket (and thus out of play)
    public boolean checkPast(Ball ball) {
        int x = ball.getX();
        if (left && x < posY) return true;
        else if (!left && x > (posY+thickness)) return true;
        return false;
    }
    
    public boolean isLeft() { return left; }
    public boolean isRight() { return !left; }
}
