package brickBreaker;

import java.awt.*;

/**
 * Write a description of class VerticalRacket here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class HorizontalRacket extends Racket
{
    private boolean top;

    /**
     * Constructor for objects of class SideRacket
     */
    public HorizontalRacket(int arenaW, int arenaH, int racketW, boolean top)
    {
        range = arenaW;
        width = racketW;
        posX = range/2.;
        this.top = top;
        
        if (top) posY = 50;
        else posY = arenaH - 50;
    }

    /**
     * Draws the racket on the screen
     */
    public void draw(Graphics g) {
        g.setColor(color);
        int border = GamePanel.BORDER;
        g.fillRect(getLeft()+border, getY()+border, width, thickness);
    }
    
    /**
     * Checks if the ball collides with the racket and returns the updated ball
     */
    public Ball checkCollision(Ball ball) {
        int r = ball.getRad();
        int x = ball.getX();
        int y = ball.getY();
        if (x >= getLeft() && x <= getRight()) {
            if (top && y < (posY+thickness+r) && y >= posY) {
                ball.bounce(getY()+thickness+r, true);
                if (movingLeft || movingRight) {
                    double newAngle = ball.getAngle() - Math.PI/6.0*speed/maxSpeed;
                    
                    // Make sure the ball bounces back down
                    if (newAngle < 0.15) newAngle = 0.15;
                    else if (newAngle > Math.PI-0.15) newAngle = Math.PI-0.15;
                    ball.changeAngle(newAngle);
                }
            }
            else if (!top && y > (posY-r) && y <= (posY+thickness)) {
                ball.bounce(getY()-r, true);
                if (movingLeft || movingRight) {
                    double newAngle = ball.getAngle() + Math.PI/6.0*speed/maxSpeed;
                    
                    // Make sure the ball bounces back up
                    if (newAngle < Math.PI+0.15) newAngle = Math.PI+0.15;
                    else if (newAngle > 2*Math.PI-0.15) newAngle = 2*Math.PI-0.15;
                    ball.changeAngle(newAngle);
                }
            }
        }
        else if (x > (getLeft()-r) && x < (getRight()+r)) {
            int diff = 0;
            if (x < posX) diff = x - getLeft();
            else diff = x - getRight();
            if (top && y < (posY+thickness+Math.abs(diff)) && y >= posY) {
                ball.bounce(getY()+thickness+Math.abs(diff), true);
                ball.changeAngle(ball.getAngle() + 2*Math.asin(diff/r));
            }
            else if (!top && y > (posY-Math.abs(diff)) && y <= (posY+thickness)) {
                ball.bounce(getY()-Math.abs(diff), true);
                ball.changeAngle(ball.getAngle() + 2*Math.asin(diff/r));
            }
        }
                
        return ball;
    }
    
    // Checks if the ball is past the racket (and thus out of play)
    public boolean checkPast(Ball ball) {
        int y = ball.getY();
        if (top && y < posY) {
            return true;
        }
        else if (!top && y > (posY+thickness)) return true;
        return false;
    }
    
    public boolean isTop() { return top; }
    public boolean isBottom() { return !top; }
}
