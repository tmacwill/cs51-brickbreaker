package brickBreaker;

import java.awt.*;

/**
 * Represents a brick that offers a "Power Up" when it is hit by a ball, increasing game speed, point multipliers, etc.
 * 
 * @author Jacob Pritt
 * @version 4/30/10
 * @file PowerBrick.java
 * @see Brick.java
 */
public class PowerBrick extends StandardBrick
{
    // Level of the power up
    // Each higher level increases the point multiplier, speed, etc.
    private int level = 1;

    /**
     * Constructor
     */
    public PowerBrick()
    {
        super(50,1,new Color(250,130,10));
    }

    /**
     * Increases the speed and point multiplier of the given ball.
     * @param b The ball object to be updated
     */
    public void powerUp(Ball b)
    {
        if (level > b.powerLevel) {
            b.powerLevel = level;
            b.speed *= 1.5;
            b.ptMultiplier *= 2;
        }
    }
}
