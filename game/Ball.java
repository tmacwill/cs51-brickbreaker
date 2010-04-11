
/**
 * Write a description of class Ball here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Ball
{
    private double posX, posY;  // position relative to the upper left corner of the playing arena
    private double velX, velY;      // x and y components of the ball's velocity vector
    private double speed;
    private int radius;
    
    private boolean inBounds = true;
    
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
        posY = 0;
        posX = level.WIDTH*Math.random();
        
        velX = 1+2*Math.random();   // Random value between 1 & 3
        velY = 1+2*Math.random();   // Random value between 1 & 3
        speed = Math.sqrt(velX*velX + velY*velY);
    }
    
    public void updatePosition()
    {
        posX += velX;
        posY += velY;
        
        //  If the ball is out of bounds, move it back inbounds and change the direction of its velocity
        if (posX < radius) {
            posX = -posX;
            velX = -velX;
        }
        else if (posX > (level.WIDTH-radius)) {
            posX = 2*(level.WIDTH-radius)-posX;
            velX = -velX;
        }
        if (posY < radius) {
            posY = -posY;
            velY = -velY;
        }
        else if (posY >= (racket.getY()-radius)) {
            if (posX >= racket.getLeft() && posX <= racket.getRight()) {
                posY = 2*(racket.getY()-radius) - posY;
                velY = -velY;
            }
            else inBounds = false;
        }
    }
    
    public int getX() { return Math.round((long)posX); }
    public int getY() { return Math.round((long)posY); }
    public int getRad() { return radius; }
    
    public boolean inBounds() { return inBounds; }
    public void reset() { initRandom(); }
}
