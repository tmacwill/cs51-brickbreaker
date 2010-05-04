package brickBreaker;

import java.awt.*;

/**
 * Represents a racket that moves horizontally, either at the top or bottom of the screen.
 * See class Racket for more information.
 * 
 * @author Jacob Pritt
 * @version 4/30/10
 * @file HorizontalRacket.java
 * @see Racket.java
 */
public class HorizontalRacket extends Racket
{
    private boolean top;

    /**
     * Initializes the HorizontalRacket on the screen.
     *
     * @param arenaW The width of the screen on which play occurs
     * @param arenaH The height of the screen on which play occurs
     * @param racketW The width of the racket, in pixels
     * @param top True if the racket is at the top of the screen, false if it is at the bottom
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
     * Draws the racket on the screen.
     *
     * @param g The graphics object on which to draw
     */
    public void draw(Graphics g) {
        g.setColor(color);
        int border = GamePanel.BORDER;
        g.fillRect(getLeft()+border, getY()+border, width, thickness);
    }
    
    /**
     * Checks if the ball collides with the racket and updates the ball's position and direction.
     * 
     * @param ball The ball object to be tested
     * @return Returns the updated ball object
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
    
    /**
     * Checks if the ball is past the racket (and thus out of play).
     *
     * @param ball The ball object to be tested.
     * @return Returns true if the ball has passed the racket, false if it is still in play.
     */
    public boolean checkPast(Ball ball) {
        int y = ball.getY();
        if (top && y < posY) {
            return true;
        }
        else if (!top && y > (posY+thickness)) return true;
        return false;
    }

    /**
     * @return Returns true if the racket is at the top of the screen.
     */
    public boolean isTop() { return top; }

    /**
     * @return Returns true if the racket is at the bottom of the screen.
     */
    public boolean isBottom() { return !top; }
}
