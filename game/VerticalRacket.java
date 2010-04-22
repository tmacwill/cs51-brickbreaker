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
            }
            else if (!left && x > (posY-r) && x <= (posY+thickness)) {
                ball.bounce(getY()-r, false);
            }
        }
        return ball;
    }
    
    // Checks if the ball is past the racket (and thus out of play)
    public boolean checkPast(Ball ball) {
        int x = ball.getX();
        if (left && x < posX) return true;
        else if (!left && x > (posX+thickness)) return true;
        return false;
    }
    
    public boolean isLeft() { return left; }
    public boolean isRight() { return !left; }
}
