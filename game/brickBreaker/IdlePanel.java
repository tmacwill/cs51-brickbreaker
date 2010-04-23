package brickBreaker;



import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

/**
 * Write a description of class IdlePanel here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class IdlePanel extends PRPanel implements ActionListener, KeyListener
{
    public static final int PWIDTH = Main.WIDTH;  // Size of panel
    public static final int PHEIGHT = Main.HEIGHT;
    private Main main;
    
    private static final int delay = 200; // milliseconds
    private javax.swing.Timer clock;
    int step = -1;
    
    private String[] msgs = { "F1..........New 1-player Game",
                                                "F2..........New 2-player Game",
                                                "F3..........Enter Level Editor",
                                                "F4..........Upload/Download Levels" };

    public IdlePanel(Main m) {
        main = m;
        
        setBackground(Color.black);
        setPreferredSize(new Dimension(PWIDTH, PHEIGHT));
        addKeyListener(this);
        
        setFocusable(true);
        requestFocus();    // the JPanel now has focus, so receives key events
    }
    
    public void start()
    {
        super.addNotify();  // creates the peer
        if (clock == null) 
            clock = new javax.swing.Timer(delay,this);
        clock.start();
    }
    
    public void pause() { 
        if (clock != null) clock.stop(); }
    public void resume() {
        if (clock != null) clock.start(); }
    public void stop() {
        if (clock != null) clock.stop();
        step = -1;
    } 
    
    public void actionPerformed (ActionEvent e) {
        step++;
        if (step >= msgs.length) {
            try { 
                Thread.sleep(500);
                step = -1;
                paintScreen();
                Thread.sleep(500);
            }    
            catch (InterruptedException ex) { System.out.println("Thread interrupted"); }
        }
        paintScreen();
    }
    
    private void paintScreen()
    {
        Graphics g;
        try {
            g = this.getGraphics();  // get panel's graphic content
            
            if (g != null) {
                g.setColor(Color.black);
                g.fillRect(0,0,PWIDTH,PHEIGHT);
                for (int i = 0; i <= step; i++) {
                    g.setFont(new Font("Arial", Font.BOLD, 20));
                    g.setColor(Color.white);
                    g.drawString(msgs[i], PWIDTH/2-100, PHEIGHT/2-100 + 30*i);
                }
            }
            Toolkit.getDefaultToolkit().sync();     // sync the display on some systems
            g.dispose();
        }
        catch (Exception e)
        {   System.out.println("Graphics context error: " + e); }
    }   // end of paintScreen()
    
    public void keyPressed(KeyEvent e)
    {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_F1) { }
    }
    
    public void keyReleased(KeyEvent e) {  }
    public void keyTyped(KeyEvent e) { }
}
