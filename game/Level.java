import java.awt.*;
import java.awt.event.*;

/**
 * Write a description of class Level here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Level
{
    public static int WIDTH = GamePanel.ARENA_WIDTH;
    public static int HEIGHT = GamePanel.ARENA_HEIGHT;
    private int numRows, numCols;
    private int brickWidth, brickHeight;
    private Brick [][] bricks; // true indicates a box is present
    private Racket[] rackets;
    private Ball[] balls;

    /**
     * Default constructor: takes arguments for the width and height of the screen and the 
     */
    public Level(Brick [][] bricks, int numPlayers)
    {
        numRows = bricks.length;
        numCols = bricks[0].length;
        brickWidth = WIDTH/numCols;
        brickHeight = HEIGHT/numRows;
        
        initBricks(bricks);
        
        if (numPlayers < 1 || numPlayers > 4) {
            System.out.print("Error! only 1-4 players allowed!");
            if (numPlayers < 1) {
                System.out.println("  Creating 1 player");
                numPlayers = 1;
            }
            else {
                System.out.println("  Creating 4 players");
                numPlayers = 4;
            }
        }
        rackets = new Racket[numPlayers];
        rackets[0] = new HorizontalRacket(WIDTH, HEIGHT, 80, false);
        if (numPlayers > 1) rackets[1] = new HorizontalRacket(WIDTH, HEIGHT, 80, true);
        if (numPlayers > 2) rackets[2] = new VerticalRacket(WIDTH, HEIGHT, 80, false);
        if (numPlayers > 3) rackets[3] = new VerticalRacket(WIDTH, HEIGHT, 80, true);
        
        balls = new Ball[2];
        balls[0] = new Ball(rackets, 8);
        balls[0].setLoc(WIDTH/2, HEIGHT/2-100, Math.random()*Math.PI/2 + 5*Math.PI/4);
        balls[1] = new Ball(rackets, 8);
        balls[1].setLoc(WIDTH/2, HEIGHT/2+50, Math.random()*Math.PI/2 + Math.PI/4);
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
    
    public Racket[] getRackets() { return rackets; }
    public Ball[] getBalls() { return balls; }
    public Brick[][] getBricks() { return bricks; }
    public int brickWidth() { return brickWidth; }    
    public int brickHeight() { return brickHeight; }
    public int numCols() { return numCols; }
    public int numRows() { return numRows; }
    
    
    
    
        
}
