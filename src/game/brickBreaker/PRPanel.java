package brickBreaker;

import javax.swing.*;
import java.awt.event.*;
/**
 * PRPanel is an extension of the JPanel class that includes pause(), resume(), start() and stop() methods
 *
 * @author Jacob Pritt
 * @version 4/30/10
 * @file PRPanel.java
 */
public class PRPanel extends JPanel implements KeyListener
{
    /**
     * Pause the panel's current action.
     */
    public void pause() { }

    /**
     * Resume the panel's current action.
     */
    public void resume() { }

    /**
     * Stop the panel's action completely.
     */
    public void stop() {}

    /**
     * Refresh the panel to it's starting state
     */
    public void start() {}
    
    /**
     * Inherited from KeyListener
     */
    public void keyPressed(KeyEvent e) {}

    /**
     * Inherited from KeyListener
     */
    public void keyReleased(KeyEvent e) {}

    /**
     * Inherited from KeyListener
     */
    public void keyTyped(KeyEvent e) {}
}
