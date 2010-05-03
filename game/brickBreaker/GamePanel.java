package brickBreaker;

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
public class GamePanel extends PRPanel implements ActionListener, KeyListener
{
    public static final int PWIDTH = 1200;  // Size of panel
    public static final int PHEIGHT = 700;
    public static final int BORDER = 10;
    public static final int ARENA_WIDTH = PWIDTH - 2*BORDER;
    public static final int ARENA_HEIGHT = PHEIGHT - 2*BORDER;
    
    private static final String GAMEOVER = "GAME OVER";
    private static final String WIN = "YOU WIN";
    private static String MESSAGE = "";
    
    private static final int delay = 10; // milliseconds
    private javax.swing.Timer clock;
    private boolean running  = false;   // stops the animation
    private volatile boolean isPaused = false;
    private volatile boolean gameOver = false;  // for game termination
    
    private Graphics dbg;
    private Image dbImage = null;
    
    private Start main;
    private LevelPlayer levelPlayer;
    private int totalScore;
    
    public GamePanel(Level lev, Start main)
    {
        setBackground(Color.white);
        setPreferredSize(new Dimension(PWIDTH, PHEIGHT));
        
        this.main = main;
        levelPlayer = new LevelPlayer(lev);
    } 

    public void reset (Level lev)
    {
        levelPlayer = new LevelPlayer(lev);
    }
    
    /* Wait for the JPanel to be added to the
     * JFrame before starting.    */
    public void init()
    {
        super.addNotify();  // creates the peer
    }
    
    // initialize and start the thread
    public void start()
    {
        Ball.resetVars();
        totalScore = 0;
        if (clock == null) 
            clock = new javax.swing.Timer(delay,this);
        isPaused = false;
        gameOver = false;
        running = true;
        clock.start();
        
        gameRender();
        paintScreen();
    }
    
    // called by the user to stop execution
    public void stop()
    {   
        running = false;
        gameOver = true;
        clock.stop();
    }
    
    public void pause()
    {   isPaused = true;    }
    
    public void resume()
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
            if (!levelPlayer.ballsInBounds()) {
                gameOver = true;
                MESSAGE = GAMEOVER;
                stop();
                gameRender();
                paintScreen();
                try {
                    Thread.sleep(1000);
                }
                catch (Exception e) {
                    main.endGame(levelPlayer.getLevel(), totalScore);
                }
                main.endGame(levelPlayer.getLevel(), totalScore);
            }
            if (levelPlayer.cleared()) {
                gameOver = true;
                MESSAGE = WIN;
                gameRender();
                paintScreen();
                try {
                    Thread.sleep(1000);
                }
                catch (Exception e) {
                    main.endGame(levelPlayer.getLevel(), totalScore);
                }
                main.endGame(levelPlayer.getLevel(), totalScore);
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
        
        if (gameOver)
            gameOverMessage(dbg);
    }   // end of gameRender()
    
    // actively render the buffer image to the screen
    private void paintScreen()
    {
        Graphics g;
        try {
            g = getGraphics();  // get panel's graphic content
            if ((g != null) && (dbImage != null))
                g.drawImage(dbImage, 0, 0, null);
            Toolkit.getDefaultToolkit().sync();     // sync the display on some systems
            g.dispose();
        }
        catch (Exception e)
        {   //System.out.println("GamePanel: Graphics context error: " + e); 
        }
    }   // end of paintScreen()
    
    // print  the game-over message
    private void gameOverMessage(Graphics g)
    {   
        int x = (int)(PWIDTH/2 - 100);
        int y = (int)(PHEIGHT/2 + 50);
        g.drawString (MESSAGE, x, y);
    }
    
    /**
     * Methods for processing keyboard input
     */
    public void keyPressed(KeyEvent e)
    {
        if (running) {
            int code = e.getKeyCode();
            if (code == KeyEvent.VK_F1 || code == KeyEvent.VK_F2)
                start();
            else if (code == KeyEvent.VK_ESCAPE)
                main.endGame(levelPlayer.getLevel(), 0);
            else
                levelPlayer.keyPressed(code);
        }
    }
    
    public void keyReleased(KeyEvent e)
    {
        if (running) {
            int code = e.getKeyCode();
            levelPlayer.keyReleased(code);
        }
    }
    
    public void keyTyped(KeyEvent e) { }
}