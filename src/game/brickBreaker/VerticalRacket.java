package brickBreaker;



import java.awt.*;

/**
 * Represents a racket that moves vertically, either at the right or left side of the screen.
 * See class Racket for more information.
 * 
 * @author Jacob Pritt
 * @version 4/30/10
 * @file VerticalRacket.java
 * @see Racket.java
 */
public class VerticalRacket extends Racket
{
    boolean left;

    /**
     * Constructor
     *
     * @param arenaW The width of the screen on which play occurs
     * @param arenaH The height of the screen on which play occurs
     * @param racketW The width of the racket, in pixels
     * @param left True if the racket is at the left side of the screen, false if it is at the right
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
     * Draws the racket on the screen.
     *
     * @param g Graphics object used to draw
     */
    public void draw(Graphics g) {
        g.setColor(color);
        int border = GamePanel.BORDER;
        g.fillRect(getY()+border, getLeft()+border, thickness, width);
    }
    
    /**
     * Checks if the ball collides with the racket and returns the updated ball
     *
     * @param ball Ball object to be tested
     * @return Returns the updated Ball object
     */
    public Ball checkCollision(Ball ball) {
        int x = ball.getX();
        int y = ball.getY();
        int r = ball.getRad();
        if (y >= getLeft() && y <= getRight()) {
            if (left && x < (posY+thickness+r) && x >= posY) {
                ball.bounce(getY()+thickness+r, false);
            }
            else if (!left && x > (posY-r) && x <= (posY+thickness)) {
                ball.bounce(getY()-r, false);
            }
        }
        return ball;
    }

    /**
     * Checks if the ball is past the racket (and thus out of play)
     *
     * @param ball Ball to be tested
     * @return Returns true if the ball is past the racket, false if not
     */
    public boolean checkPast(Ball ball) {
        int x = ball.getX();
        if (left && x < posX) return true;
        else if (!left && x > (posX+thickness)) return true;
        return false;
    }

    /**
     * Returns true if the racket is on the left side of the screen.
     */
    public boolean isLeft() { return left; }

    /**
     * Returns true if the racket is on the right side of the screen.
     */
    public boolean isRight() { return !left; }
}
