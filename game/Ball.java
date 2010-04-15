import java.awt.*;

/**
 * Write a description of class Ball here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Ball
{
    public static final double pi = Math.PI;
    private double posX, posY;  // position of center of the ball
    private double angle;   // Measured from the horizontal line (+x axis) clockwise
    private double speed;
    
    // x and y components of the ball's velocity vector (calculated from angle & speed)
    private double velX, velY;
    private int radius;
    private Color color = Color.red;
    
    private boolean inBounds = true;
    private boolean inPlay = true;
    
    private Racket racket;
    private Level level;

    /**
     * Constructor for objects of class Ball
     */
    public Ball(Level l, Racket r, int radius)
    {
        level = l;
        racket = r;
        this.radius = radius;
        initRandom();
    }

    /**
     * Initializes the ball in a random position along the top of the screen, 
     * moving in a random downward direction
     */
    public void initRandom()
    {
        posY = level.HEIGHT/2;
        posX = level.WIDTH/2;
        
        angle = Math.random()*pi/2. + pi/4.;    // Random value between pi/4 & 3pi/4
        speed = 4;
        velX = speed*Math.cos(angle);
        velY = speed*Math.sin(angle);
        
        inBounds = true;
        inPlay = true;
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
            posX = 2*radius-posX;
            changeAngle(pi-angle);
        }
        else if (posX > (level.WIDTH-radius)) {
            posX = 2*(level.WIDTH-radius)-posX;
            changeAngle(pi-angle);
        }
        if (posY < radius) {
            posY = 2*radius-posY;
            changeAngle(2*pi-angle);
        }
        else if (posY > (racket.getY()-radius)) {
            if (posX >= racket.getLeft() && posX <= racket.getRight() && inPlay) {
                posY = 2*(racket.getY()-radius) - posY;
                changeAngle(2*pi-angle);
            }
            else if (posX >= racket.getLeft()-radius && posX <= racket.getRight()+radius && inPlay) {
                changeAngle(pi-angle);
                inPlay = false;
            }
            else if (posY < level.HEIGHT-radius) inPlay = false;
            else inBounds = false;
        }
        else {
            return level.checkCollision(this);
        }
        return 0;
    }

    
    public void draw(Graphics g) {
        g.setColor(color);
        int border = GamePanel.BORDER;
        g.fillOval(getX()+border, getY()+border, 2*radius, 2*radius);  
    }
    
    public int getX() { return Math.round((long)(posX-radius)); }
    public int getY() { return Math.round((long)(posY-radius)); }
    public int getRad() { return radius; }
    
    public boolean inBounds() { return inBounds; }
    public void reset() { initRandom(); }
    
    private void changeAngle(double newAngle) {
        angle = newAngle % (2*pi);
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
            posY = 2*(y-radius) - posY;
            changeAngle(-angle);
            return true;
        }
        else if ( posY < (y+h+radius) && posY > (y+h/2) && posX >= x && posX <= (x+w) ) {
            posY = 2*(y+h+radius) - posY;
            changeAngle(-angle);
            return true;
        }
        else if ( posX > (x-radius) && posX < (x+w/2) && posY >= y && posY <= (y+h) ) {
            posX = 2*(x-radius) - posX;
            changeAngle(pi-angle);
            return true;
        }
        else if ( posX < (x+w+radius) && posX > (x+w/2) && posY >= y && posY <= (y+h) ) {
            posX = 2*(x+w+radius) - posX;
            changeAngle(pi-angle);
            return true;
        }
        return false;
    }
        
}
