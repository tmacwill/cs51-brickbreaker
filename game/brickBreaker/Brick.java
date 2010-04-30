package brickBreaker;

import java.awt.*;
import java.io.Serializable;

/**
 * Abstract class Brick - write a description of the class here
 * 
 * @author (your name here)
 * @version (version number or date here)
 */
public abstract class Brick implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private int points;
    private boolean destructible = true;
    private int hitsToRemove;
    private boolean removed = false;
    private Color color;
    private int x, y, width, height;    // Location of box

    public Brick(int pts, int hits, Color c) {
        points = pts;
        if (hits == -1) destructible = false;
        hitsToRemove = hits;
        color = c;
    }
    
    public void setLoc(int x, int y, int wid, int hgt) {
        this.x = x;
        this.y = y;
        width = wid;
        height = hgt;
    }
    
    public int bounce(Ball ball)
    {
        powerUp(ball);
        if (!destructible) return 0;
        else {
            hitsToRemove--;
            if (hitsToRemove == 0) {
                removed = true;
                return (int)(points*ball.ptMultiplier);
            }
            else return 0;
        }
    }
    
    public boolean removed() { return removed; }
    public Color color() { return color; }
    
    public int[] getLoc() {
        int[] loc = {x, y, width, height};
        return loc;
    }
    
    public void powerUp(Ball b) {}
    
    public boolean permanent() { return false; }
    
    public void draw(Graphics g) {
        g.setColor(color);
        int border = GamePanel.BORDER;
        g.fillRect(x+border+2, y+border+2, width-3, height-3);
    }
}
