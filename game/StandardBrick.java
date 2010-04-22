import java.awt.*;

/**
 * Write a description of class StandardBrick here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class StandardBrick extends Brick
{

    /**
     * Constructor for objects of class StandardBrick
     */
    public StandardBrick()
    {
        super(10, 1, new Color(250,190,70));
    }
    
    public StandardBrick(int pts, int hits, Color c)
    {
        super(pts,hits,c);
    }
}
