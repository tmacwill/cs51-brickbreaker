import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
//import java.awt.image.*;
import java.util.*;


/**
 * Write a description of class GamePanel here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class GamePanel extends JPanel implements Runnable, ActionListener, KeyListener
{
    private static final int PWIDTH = 1200;  // Size of panel
    private static final int PHEIGHT = 700;
    
    private static final String GAMEOVER = "GAME OVER";
    
    private static final int delay = 10; // milliseconds
    private javax.swing.Timer clock;
    private boolean running  = false;   // stops the animation
    private volatile boolean isPaused = false;
    private volatile boolean gameOver = false;  // for game termination

    private Graphics dbg;
    private Image dbImage = null;

    private Level lev;
    private Racket rack;
    private Ball ball;
    
    public GamePanel()
    {
        setBackground(Color.white);
        setPreferredSize(new Dimension(PWIDTH, PHEIGHT));
        addKeyListener(this);
        
        setFocusable(true);
        requestFocus();    // the JPanel now has focus, so receives key events
//        readyForTermination();    // Why won't this work???
        
        lev  = new Level(PWIDTH, PHEIGHT, 20);
        rack = new Racket(PWIDTH, PHEIGHT, 80);
        ball = new Ball(lev, rack, 15);
        
    }   // end of GamePanel()

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
        if (clock == null) 
            clock = new javax.swing.Timer(delay,this);
        running = true;
        clock.start();
    }
    
    // called by the user to stop execution
    public void stopGame()
    {   running = false; }
    
    public void pauseGame()
    {   isPaused = true;    }
    
    public void resumeGame()
    {   isPaused = false;   }
    
    public void actionPerformed (ActionEvent e)
    {
        if (running) {
            gameUpdate();
            gameRender();
            paintScreen();
        }
    }
    
    // Repeatedly update, render, sleep
    public void run()
    {
        running = true;
        while (running) {
            gameUpdate();   // game state is updated
            gameRender();   // render to a buffer
            paintScreen();     // paint with the buffer
            
            try {
                Thread.sleep(20);
            }
            catch(InterruptedException ex) {}
        }
        System.exit(0);     // So enclosing JFrame/JApplet exits
    }   // end of run()
    
    private void gameUpdate()
    {
        if (!isPaused && !gameOver) {
            ball.updatePosition();
            if (!ball.inBounds()) stopGame();
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
        
        
        dbg.setColor(Color.blue);
        dbg.fillRect(rack.getLeft(), rack.getY(), rack.getWidth(), 5);
        
        dbg.setColor(Color.red);
        dbg.fillOval(ball.getX(), ball.getY(), ball.getRad(), ball.getRad());        
        
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
        int y = (int)(PHEIGHT/2 - 50);
        g.drawString (GAMEOVER, x, y);
    }
    
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        if (dbImage != null)
            g.drawImage(dbImage, 0, 0, null);
    }
    
    /**
     * Methods for processing keyboard input
     */
    public void keyPressed(KeyEvent e)
    {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_RIGHT) {
            rack.moveRight();
        }
        else if (code == KeyEvent.VK_LEFT) {
            rack.moveLeft();
        }
    }
    
    public void keyReleased(KeyEvent e) { }
    public void keyTyped(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_RIGHT) {
            rack.moveRight();
        }
        else if (code == KeyEvent.VK_LEFT) {
            rack.moveLeft();
        }
    }
}
