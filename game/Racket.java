import java.awt.*;

/**
 * Write a description of class Racket here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Racket
{
    private int arenaWidth;
    private int width;  // width of racket
    private double posY;    // Should be at the bottom of the screen
    private double posX;    // x-coordinate of the middle of the racket
    private double speed = 8.0;
    private Color color = Color.blue;

    /**
     * Constructor for objects of class Racket
     */
    public Racket(int screenW, int screenH, int racketW)
    {
        arenaWidth = screenW;
        width = racketW;
        posY = screenH - 30;
        posX = screenW/2.0;
    }

    public void moveLeft()
    {
        posX  = Math.max(width/2, posX-speed);
    }
    
    public void moveRight()
    {
        posX = Math.min(arenaWidth-width/2, posX+speed);
    }
    
    public void draw(Graphics g) {
        g.setColor(color);
        int border = GamePanel.BORDER;
        g.fillRect(getLeft()+border, getY()+border+5, width, 5);
    }
    
    public int getY() { return Math.round((long)posY); }
    public int getX() { return Math.round((long)posX); }
    public int getLeft() { return Math.round((long)(posX - width/2.0)); }
    public int getRight() { return Math.round((long)(posX + width/2.0)); }
    public int getWidth() { return width; }
}
