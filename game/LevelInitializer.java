import java.awt.*;
/**
 * Includes methods for initializing a number of default levels for 1, 2 or 4 players
 */
public class LevelInitializer
{
    public static final double pi = Math.PI;
    public static final int width = GamePanel.ARENA_WIDTH;
    public static final int height = GamePanel.ARENA_HEIGHT;

    public static Level[] generateLevels(int numPlayers)
    {
        if (numPlayers == 1) return generateOnePlayer();
        else if (numPlayers == 2) return generateTwoPlayers();
        else if (numPlayers == 4) return generateFourPlayers();
        else {
            System.out.println("Error!  Currently only supported for 1, 2 & 4 players!");
            return null;
        }
    }
    
    public static Level[] generateOnePlayer() {
        Level[] levels = new Level[2];
        
        Racket[] racks1 = { new HorizontalRacket(width, height, 80, false) };
        Ball[] balls1 = { new Ball(racks1, 8) };
        balls1[0].setLoc(width/2, height/2+50, Math.random()*pi/4+pi/8);
        
        Brick[][] bricks1 = new Brick[25][25];
        for (int i = 0; i < bricks1.length; i++) {
            for (int j = 0; j < bricks1[0].length; j++) {
               if (j > 1 && j < 4) bricks1[i][j] = new PowerBrick();
                else if (j > 1 && j < 11) bricks1[i][j] = new StandardBrick();
            }
        }
        levels[0] = new Level(bricks1, balls1, racks1);
        
        
        Racket[] racks2 = { new HorizontalRacket(width, height, 80, false) };
        Ball[] balls2 = { new Ball(racks2, 8) };
        balls2[0].setLoc(width/2, height/2+50, Math.random()*pi/4+pi/8);
        Brick[][] bricks2 = new Brick[25][25];
        for (int i = 0; i < bricks2.length; i++) {
            for (int j = 0; j < bricks2[0].length; j++) {
                if (j > 1 && j < 4) {
                    if (i == 8 || i == 15) bricks2[i][j] = new PermanentBrick();
                    else bricks2[i][j] = new PowerBrick();
                }
                else if (j > 1 && j < 11) {
                    if (i == 8 || i == 15) bricks2[i][j] = new PermanentBrick();
                    else bricks2[i][j] = new StandardBrick();
                }
            }
        }
        levels[1] = new Level(bricks2, balls2, racks2);
        
        return levels;
    }
    
    public static Level[] generateTwoPlayers() {
        Level[] levels = new Level[1];
        
        Racket[] racks = 
            { new HorizontalRacket(width, height, 80, false),
              new HorizontalRacket(width, height, 80, true)};
        Ball[] balls = { new Ball(racks, 8), new Ball(racks, 8) };
        balls[0].setLoc(width/2, height/2+50, Math.random()*pi/4+pi/8);
        balls[1].setLoc(width/2, height/2-50, Math.random()*pi/4+9*pi/8);
        
        
        int wid = 26;
        Brick[][] levelBoxes = new Brick[wid][wid];
        for (int x = 0; x < levelBoxes.length; x++) {
            for (int y = 0; y < levelBoxes[0].length; y++) {
                if (y >= x && x+y <= wid && y > 1 && y < (wid-2)) levelBoxes[x][y] = new StandardBrick();
                else if (x >= wid/2 && levelBoxes[wid-x][y] != null) levelBoxes[x][y] = new StandardBrick();
            }
        }
        levels[0] = new Level(levelBoxes, balls, racks);
        
        return levels;
    }

    public static Level[] generateFourPlayers() {
        return null;
    }
}
