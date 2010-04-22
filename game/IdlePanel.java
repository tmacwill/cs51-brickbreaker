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
public class IdlePanel extends JPanel implements ActionListener, KeyListener
{
    public static final int PWIDTH = Start.WIDTH;  // Size of panel
    public static final int PHEIGHT = Start.HEIGHT;
    
    private static final int delay = 10; // milliseconds
    private javax.swing.Timer clock;
    
    boolean startGame = false;
    boolean enterEditor = false;
    boolean upload = false;
    boolean download = false;

    public IdlePanel() {
        setBackground(Color.black);
        setPreferredSize(new Dimension(PWIDTH, PHEIGHT));
        addKeyListener(this);
        
        setFocusable(true);
        requestFocus();    // the JPanel now has focus, so receives key events
        startClock();
    }        
    
    private void startClock() {
        if (clock == null) 
            clock = new javax.swing.Timer(delay,this);
        clock.start();
    }

    public void actionPerformed (ActionEvent e) {
        if (startGame)
            Start.startGame();
        else if (enterEditor)
            Start.edit();
    }
    
    public void keyPressed(KeyEvent e)
    {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_F1) enterEditor = true;
        else if (code == KeyEvent.VK_F2) startGame = true;
    }
    
    public void keyReleased(KeyEvent e) {  }
    public void keyTyped(KeyEvent e) { }
}
