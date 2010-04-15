import java.awt.*;

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
    private Racket racket;
    private Ball ball;

    /**
     * Default constructor: takes arguments for the width and height of the screen and the 
     */
    public Level(Brick [][] bricks)
    {
        numRows = bricks.length;
        numCols = bricks[0].length;
        brickWidth = WIDTH/numCols;
        brickHeight = HEIGHT/numRows;
        
        initBricks(bricks);
        racket = new Racket(WIDTH, HEIGHT, 80);
        ball = new Ball(this, racket, 8);
    }
    
    /**
     * Default constructor: takes arguments for the width and height of the screen and the 
     */
    public Level(Brick [][] bricks, Ball ball, Racket rack)
    {
        numRows = bricks.length;
        numCols = bricks[0].length;
        brickWidth = WIDTH/numCols;
        brickHeight = HEIGHT/numRows;
        
        initBricks(bricks);
        this.ball = ball;
        racket = rack;
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
    
    /**
     * Updates the components in the level and returns the score earned
     */
    public int update(boolean right, boolean left) {
        if (right) racket.moveRight();
        if (left) racket.moveLeft();
        return ball.updatePosition();
    }
    
    public void drawComponents(Graphics g) {
        for (int x = 0; x < bricks.length; x++) {
            for (int y = 0; y < bricks[0].length; y++) {
                if (bricks[x][y] != null) bricks[x][y].draw(g);
            }
        }
        racket.draw(g);
        ball.draw(g);
    }

    public boolean ballInBounds() { return ball.inBounds(); }
    
    /**
     * Returns true if the box at (x,y) is filled
     */
    public boolean isFilled (int x, int y) {
        if (x < 0 || y < 0 || x >= numRows || y >= numCols) return false;
        return (bricks[x][y] != null);
    }
    
    /**
     * Removes the box at (x,y)
     * Returns the number of points earned
     */
    public int removeBrick (int x, int y) {
        if (x < 0 || y < 0 || x >= numRows || y >= numCols) return 0;
        if (bricks[x][y] == null) return 0;
        else {
            int pts = bricks[x][y].hit();
            if (bricks[x][y].removed()) bricks[x][y] = null;
            return pts;
        }
    }
    
    /**
     * Returns the coordinates of the center of the box that contains the point (x,y) 
     */
    public Point getCenter(int x, int y) {
        int xC = (int)(x/brickWidth)*brickWidth + brickWidth/2;
        int yC = (int)(y/brickHeight)*brickHeight + brickHeight/2;
        return new Point(xC,yC);
    }
    
    public int brickWidth() { return brickWidth; }    
    public int brickHeight() { return brickHeight; }
    public int numCols() { return numCols; }
    public int numRows() { return numRows; }
    
    public int checkCollision(Ball b) {
        int boxX = (int)b.getX()/brickWidth;
        int boxY= (int)b.getY()/brickHeight;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (isFilled(boxX+i,boxY+j)) {
                    if (b.checkCollision(bricks[boxX+i][boxY+j].getLoc()))
                        return removeBrick(boxX+i,boxY+j);                    
                }
            }
        }
        return 0;
    }
}
