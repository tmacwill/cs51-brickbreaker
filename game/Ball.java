import java.awt.*;
import java.io.Serializable;

/**
 * Write a description of class Ball here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
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

    public Ball(Racket[] r, int radius)
    {
        rackets = r;
        this.radius = radius;
    }
    
    public Ball(LevelPlayer l, Racket[] r, int radius)
    {
        level = l;
        rackets = r;
        this.radius = radius;
    }
    
    public void setLoc(double x, double y, double angle) {
        posX = x;
        posY = y;
        this.angle = angle;
        speed = 3;
        velX = speed*Math.cos(angle);
        velY = speed*Math.sin(angle);
        
        inBounds = true;
        inPlay = true;
    }
    
    public void setLevel(LevelPlayer lev) {
        level = lev;
    }
    
    /**
     * Updates the position of the ball
     * Returns the number of points earned (based on the boxes hit)
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

    public void draw(Graphics g) {
        g.setColor(color);
        int border = GamePanel.BORDER;
        g.fillOval(getX()-radius+border, getY()-radius+border, 2*radius, 2*radius);  
    }
    
    public int getX() { return Math.round((long)posX); }
    public int getY() { return Math.round((long)posY); }
    public int getRad() { return radius; }
    
    public void setX(double x) { posX = x; }
    public void setY(double y) { posY = y; }
    
    public boolean inBounds() { return inBounds; }
    
    // Resets the static variables in this class
    public static void resetVars() {
        powerLevel = 0;
        ptMultiplier = 1;
    }
    
    private void changeAngle(double newAngle) {
        angle = newAngle % (2*pi);
        while (angle < 0) angle += 2*pi;
        velX = speed*Math.cos(angle);
        velY=  speed*Math.sin(angle);
    }
    
    /**
     * Checks if the ball is colliding with the given object
     * If a collision exists and changeDir is true, it changes the ball's direction
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
    
    // Returns the updated ball after bouncing off the wall
    // The wall is either horizontal or vertical 
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
    }
        
}
