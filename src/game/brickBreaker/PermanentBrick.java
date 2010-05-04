package brickBreaker;

import java.awt.*;

/**
 * Represents a permanent brick object, which can never be removed.
 * 
 * @author Jacob Pritt
 * @version 4/30/10
 * @file PermanentBrick.java
 * @see Brick.java
 */
public class PermanentBrick extends Brick
{
    
    /**
     * Constructor
     */
    public PermanentBrick()
    {
        super(0, -1, new Color(100,150,240));
    }

    /**
     * Always returns true, since this brick is by definition permanent.
     */
    public boolean permanent() { return true; }
}
