import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;


/**
 * Write a description of class GamePanel here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class GamePanel extends JPanel implements ActionListener, KeyListener
{
    public static final int PWIDTH = 1200;  // Size of panel
    public static final int PHEIGHT = 700;
    public static final int BORDER = 10;
    public static final int ARENA_WIDTH = PWIDTH - 2*BORDER;
    public static final int ARENA_HEIGHT = PHEIGHT - 2*BORDER;
    
    private static final String GAMEOVER = "GAME OVER";
    
    private static final int delay = 10; // milliseconds
    private javax.swing.Timer clock;
    private boolean running  = false;   // stops the animation
    private volatile boolean isPaused = false;
    private volatile boolean gameOver = false;  // for game termination
    
    private Graphics dbg;
    private Image dbImage = null;

    private Level[] levels;
    private LevelPlayer levelPlayer;
    private int currLevel;
    
    private int totalScore;
    
    public GamePanel(int numPlayers)
    {
        setBackground(Color.white);
        setPreferredSize(new Dimension(PWIDTH, PHEIGHT));
        addKeyListener(this);
        
        setFocusable(true);
        requestFocus();    // the JPanel now has focus, so receives key events
//        readyForTermination();    // Why won't this work???
        
        initGame(numPlayers);
    }   // end of GamePanel()
    
    private void initGame(int numPlayers) {
        levels = LevelInitializer.generateLevels(numPlayers);
        currLevel = 0;
        levelPlayer = new LevelPlayer(levels[0]);
    }

    /* Wait for the JPanel to be added to the
     * JFrame/JAplet before starting.    */
    public void addNotify()
    {
        super.addNotify();  // creates the peer
        startGame();            // start the thread
    }
    
    // initialize and start the thread
    private void startGame()
    {
        Ball.resetVars();
        totalScore = 0;
        if (clock == null) 
            clock = new javax.swing.Timer(delay,this);
        isPaused = false;
        gameOver = false;
        running = true;
        clock.start();
    }
    
    // called by the user to stop execution
    public void stopGame()
    {   
        running = false;
        gameOver = true;
        clock.stop();
    }
    
    public void pauseGame()
    {   isPaused = true;    }
    
    public void resumeGame()
    {   isPaused = false;   }
    
    // Repeatedly update, render, sleep
    public void actionPerformed (ActionEvent e)
    {
        if (running) {
            gameUpdate();
            gameRender();
            paintScreen();
        }
    }
    
    private void gameUpdate()
    {
        if (!isPaused && !gameOver) {
            totalScore += levelPlayer.update();
            if (!levelPlayer.ballsInBounds()) stopGame();
            if (levelPlayer.cleared()) {
                Ball.resetVars();
                currLevel = (currLevel+1)%levels.length;
                levelPlayer = new LevelPlayer(levels[currLevel]);
            }
        }
    }
    
    private void gameRender()
    {
        if (dbImage == null) {  // create the buffer
            dbImage = createImage(PWIDTH, PHEIGHT);
            if (dbImage == null) {
                System.out.println("dbImage is null");
                return;
            }
            else
                dbg = dbImage.getGraphics();
        }
        
        // clear the background
        dbg.setColor(Color.white);
        dbg.fillRect(0, 0, PWIDTH, PHEIGHT);
        
        // Draws a thick black border around the playing arena
        dbg.setColor(Color.black);
        dbg.drawRect(BORDER,BORDER, PWIDTH-2*BORDER, PHEIGHT-2*BORDER);
        dbg.drawRect(BORDER-1,BORDER-1, PWIDTH-2*BORDER+2, PHEIGHT-2*BORDER+2);
        dbg.drawRect(BORDER-2,BORDER-2, PWIDTH-2*BORDER+4, PHEIGHT-2*BORDER+4);
        
        levelPlayer.drawComponents(dbg);
        
        dbg.setColor(Color.blue);
        dbg.setFont(new Font("Arial", Font.BOLD, 20));
        dbg.drawString( Integer.toString(totalScore), PWIDTH-100, BORDER+20);
        
        dbg.drawString( "Level " + (currLevel+1), 50, BORDER+30);
        
        if (gameOver)
            gameOverMessage(dbg);
    }   // end of gameRender()
    
    // actively render the buffer image to the screen
    private void paintScreen()
    {
        Graphics g;
        try {
            g = this.getGraphics();  // get panel's graphic content
            if ((g != null) && (dbImage != null))
                g.drawImage(dbImage, 0, 0, null);
            Toolkit.getDefaultToolkit().sync();     // sync the display on some systems
            g.dispose();
        }
        catch (Exception e)
        {   System.out.println("Graphics context error: " + e); }
    }   // end of paintScreen()
    
    // print  the game-over message
    private void gameOverMessage(Graphics g)
    {   
        int x = (int)(PWIDTH/2 - 100);
        int y = (int)(PHEIGHT/2 + 50);
        g.drawString (GAMEOVER, x, y);
    }
    
    /**
     * Methods for processing keyboard input
     */
    public void keyPressed(KeyEvent e)
    {
        int code = e.getKeyCode();
        
        if (code == KeyEvent.VK_F1) {
            initGame(1);
            startGame();
        }
        else if (code == KeyEvent.VK_F2) {
            initGame(2);
            startGame();
        }
        
        else levelPlayer.keyPressed(code);
    }
    
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        levelPlayer.keyReleased(code);
    }
    public void keyTyped(KeyEvent e) { }
}