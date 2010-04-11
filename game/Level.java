
/**
 * Write a description of class Level here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Level
{
    public static int WIDTH, HEIGHT;
    private final int numRows, numCols;
    private boolean [][] boxes; // true indicates a box is present

    /**
     * Constructor for objects of class Level
     */
    public Level(int width, int height, int boxSize)
    {
        WIDTH = width;
        HEIGHT = height;
        numCols = (int)width/boxSize;
        numRows = (int)height/(2*boxSize);  // Boxes fill the upper half of the screen
        
        boxes = new boolean[numRows][numCols];
    }

}
