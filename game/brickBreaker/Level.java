package brickBreaker;

import java.awt.*;
import java.awt.event.*;
import java.io.Serializable;
import java.util.Arrays;

/**
 * Contains all of the data necessary to initialize a level.  This object is passed to LevelPlayer to play a level.
 * 
 * @author Jacob Pritt
 * @version 4/30/10
 * @file Level.java
 * @see LevelPlayer.java
 */
public class Level implements Serializable, Cloneable
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


    /**
     * Constructor
     *
     * @param bricks 2-dimensional array of bricks
     * @param balls All existing balls
     * @param rack All existing rackets
     * @param name String identifier (used in user interface)
     */
    public Level(Brick [][] bricks, Ball[] balls, Racket[] rack, String name)
    {
        this(bricks, balls, rack);
        this.name = name;
    }

    /**
     * Constructor
     *
     * @param bricks 2-dimensional array of bricks
     * @param balls All existing balls
     * @param rack All existing rackets
     */
    public Level(Brick [][] bricks, Ball[] balls, Racket[] rack)
    {
        //numRows = bricks.length;
        //numCols = bricks[0].length;
        numRows = bricks[0].length;
        numCols = bricks.length;
        brickWidth = WIDTH/numCols;
        brickHeight = HEIGHT/numRows;
        
        initBricks(bricks);
        rackets = rack;
        this.balls = balls;
    }

    /**
     * Constructor
     *
     * @param bricks 2-dimensional array of bricks
     * @param players Number of players
     * @param name String identifier (used in user interface)
     */
    public Level(Brick[][] bricks, int players, String name) {
        this.name = name;
        //numRows = bricks.length;
        //numCols = bricks[0].length;
        numRows = bricks[0].length;
        numCols = bricks.length;
        brickWidth = WIDTH/numCols;
        brickHeight = HEIGHT/numRows;
        initBricks(bricks);
        
        double pi = Math.PI;
        if (players == 1) {
            rackets = new Racket[1];
            rackets[0] = new HorizontalRacket(WIDTH, HEIGHT, 80, false);
            balls = new Ball[1];
            balls[0] = new Ball(rackets, 8);
            balls[0].setLoc(WIDTH/2, HEIGHT/2+50, Math.random()*pi/4+pi/8);
        }
        else if (players == 2) {
            rackets = new Racket[2];
            rackets[0] = new HorizontalRacket(WIDTH, HEIGHT, 80, false);
            rackets[1] = new HorizontalRacket(WIDTH, HEIGHT, 80, true);
            balls=  new Ball[2];
            balls[0] = new Ball(rackets, 8);
            balls[1] = new Ball(rackets, 8);
            balls[0].setLoc(WIDTH/2, HEIGHT/2, Math.random()*pi/4+pi/8);
            balls[1].setLoc(WIDTH/2, HEIGHT/2, Math.random()*pi/4+9*pi/8);
        }
        else System.out.println("Error!  Default constructor only supports 1 or 2 players");
    }        

    /**
     * Sets the location of each brick in the level.
     * @param bricks 2d array of all existing bricks
     */
    public void initBricks(Brick[][] bricks) {
        this.bricks = bricks;
        for (int i = 0; i < bricks.length; i++) {
            for (int j = 0; j < bricks[0].length; j++) {
                Brick b = bricks[i][j];
                if (b != null) b.setLoc(brickWidth*i, brickHeight*j, brickWidth, brickHeight);
            }
        }
    }
    
    /**
     * Returns a deep clone of the Level.
     */
    @Override
    public Object clone( ) {
    	// Copy each of the arrays
    	Brick[][] bricksCopy = new Brick[bricks.length][bricks[0].length];
    	for( int i = 0; i < bricks.length; i++ ) {
    		bricksCopy[i] = Arrays.copyOf( bricks[i], bricks[i].length );
    	}
    	Racket[] racketsCopy = Arrays.copyOf( rackets, rackets.length );
    	Ball[] ballsCopy = Arrays.copyOf( balls, balls.length );
    	
    	return new Level( bricksCopy, ballsCopy, racketsCopy, name );
    }
    
    /**
     * Sets the level's name to the new string
     * @param name New name
     */
    public void setName(String name) { this.name = name; }

    /**
     * @return The array of rackets
     */
    public Racket[] getRackets() { return rackets; }

    /**
     * @return The array of balls
     */
    public Ball[] getBalls() { return balls; }

    /**
     * @return The 2d array of bricks
     */
    public Brick[][] getBricks() { return bricks; }

    /**
     * @return The width of each brick (in pixels)
     */
    public int brickWidth() { return brickWidth; }

    /**
     * @return The height of each brick (in pixels)
     */
    public int brickHeight() { return brickHeight; }

    /**
     * @return The number of columns of bricks across the screen
     */
    public int numCols() { return numCols; }

    /**
     * @return The number of rows of bricks down the screen
     */
    public int numRows() { return numRows; }

    /**
     * @return The name of this level
     */
    public String getName() { return name; }
    
}
