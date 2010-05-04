package brickBreaker;

import javax.swing.*;
import java.awt.event.*;
/**
 * PRPanel is an extension of the JPanel class that includes pause(), resume(), start() and stop() methods
 */
public class PRPanel extends JPanel implements KeyListener
{
    public void pause() { }
    public void resume() { }
    public void stop() {}
    public void start() {}
    
    // Methods for passing in key events from the mother JFrame
    public void keyPressed(KeyEvent e) {}
    public void keyReleased(KeyEvent e) {}
    public void keyTyped(KeyEvent e) {}
}
