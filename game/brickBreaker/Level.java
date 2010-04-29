package brickBreaker;



import java.awt.*;
import java.awt.event.*;
import java.io.Serializable;

/**
 * Write a description of class Level here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Level implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	public static int WIDTH = GamePanel.ARENA_WIDTH;
    public static int HEIGHT = GamePanel.ARENA_HEIGHT;
    private int numRows, numCols;
    private int brickWidth, brickHeight;
    private Brick [][] bricks; // true indicates a box is present
    private Racket[] rackets;
    private Ball[] balls;
    private String name = "Unnamed";


    public Level(Brick [][] bricks, Ball[] balls, Racket[] rack, String name)
    {
	this(bricks, balls, rack);
	this.name = name;
    }
    
    public Level(Brick [][] bricks, Ball[] balls, Racket[] rack)
    {
        numRows = bricks.length;
        numCols = bricks[0].length;
        brickWidth = WIDTH/numCols;
        brickHeight = HEIGHT/numRows;
        
        initBricks(bricks);
        rackets = rack;
        this.balls = balls;
    }
    
    public void initBricks(Brick[][] bricks) {
        this.bricks = bricks;
        for (int i = 0; i < bricks.length; i++) {
            for (int j = 0; j < bricks[0].length; j++) {
                Brick b = bricks[i][j];
                if (b != null) b.setLoc(brickWidth*i, brickHeight*j, brickWidth, brickHeight);
            }
        }
    }

    public void setName(String name) { this.name = name; }
    
    public Racket[] getRackets() { return rackets; }
    public Ball[] getBalls() { return balls; }
    public Brick[][] getBricks() { return bricks; }
    public int brickWidth() { return brickWidth; }    
    public int brickHeight() { return brickHeight; }
    public int numCols() { return numCols; }
    public int numRows() { return numRows; }
    public String getName() { return name; }
    
    
    
    
        
}
