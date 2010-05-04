package brickBreaker;

import java.awt.*;

/**
 * Represents the StandardBrick object used in the game.
 * 
 * @author Jacob Pritt
 * @version 4/30/10
 * @file StandardBrick.java
 * @see Brick.java
 */
public class StandardBrick extends Brick
{

    /**
     * Default constructor
     */
    public StandardBrick()
    {
        super(10, 1, new Color(250,190,70));
    }

    /**
     * Constructor
     *
     * @param pts Points earned when the brick is removed
     * @param hits Number of hits to remove the brick
     * @param c Brick color
     */
    public StandardBrick(int pts, int hits, Color c)
    {
        super(pts,hits,c);
    }
}
