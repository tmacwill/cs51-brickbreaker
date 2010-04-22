import java.awt.*;

/**
 * Write a description of class PermanentBrick here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class PermanentBrick extends Brick
{
    
    /**
     * Constructor for objects of class StandardBrick
     */
    public PermanentBrick()
    {
        super(0, -1, new Color(100,150,240));
    }

    public boolean permanent() { return true; }
}
