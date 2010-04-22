import java.awt.*;

/**
 * Write a description of class PowerBrick here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class PowerBrick extends StandardBrick
{
    // Level of the power up
    // Each higher level increases the point multiplier, speed, etc.
    private int level = 1;

    public PowerBrick()
    {
        super(50,1,new Color(250,130,10));
    }
    
    public void powerUp(Ball b)
    {
        if (level > b.powerLevel) {
            b.powerLevel = level;
            b.speed *= 1.5;
            b.ptMultiplier *= 2;
        }
    }
}
