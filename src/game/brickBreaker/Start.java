package brickBreaker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import brickBreaker.local.FilesystemFailureException;
import brickBreaker.web.*;

/**
 * The overarching JFrame which contains and controls the panels used for game play and level editing.
 *
 * @author Jacob Pritt
 * @version 4/30/10
 * @file Start.java
 */
public class Start extends JFrame implements WindowListener
{
    public static int WIDTH = 1000;
    public static int HEIGHT = 700;
    private boolean loggedIn = false;
    
    private GamePanel gp;
    private IdlePanel ip;
    private LevelEditor le;
    private PRPanel currPanel;
    Container c = getContentPane();

    /**
     * Default constructor
     */
    public Start () {
        super("Brick Breaker");
        setBackground(Color.black);
        setPreferredSize(new Dimension(WIDTH+20, HEIGHT+30));

        ip = new IdlePanel(this);
        gp = new GamePanel(LevelInitializer.generateLevels(1)[0], this);
        gp.setVisible(false);
        le = new LevelEditor(this);
        le.setVisible(false);
        
        c.add(ip, BorderLayout.CENTER);
        
        currPanel = ip;
        currPanel.start();
        gp.init();
        
        //addWindowListener(this);
        pack();
        setResizable(false);
        setVisible(true);
        
        // Receive key events from all windows
        setFocusable(true);
        requestFocusInWindow(); 
        //addKeyListener(ip);
        addKeyListener(gp);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }  // end of Start() constructor

    /**
     * Additional Constructor
     * @param loggedIn Whether or not the user has logged in.
     */
    public Start(boolean loggedIn) {
        this();
        this.loggedIn = loggedIn;
    }

    /**
     * Moves the GamePanel to the front of the screen and starts a new game with the given level.
     * @param lev Level to be played
     */
    public void startGame(Level lev) {
         currPanel.pause();
         removeKeyListener(currPanel);
         currPanel.setVisible(false);
         
         gp.reset(lev);
         currPanel = gp;
         currPanel.setVisible(true);
         addKeyListener(currPanel);
         currPanel.start();
         c.add(gp, BorderLayout.CENTER);
    }

    /**
     * Uploads the score to the website if the user was logged in, and returns the IdlePanel to the front of the screen.
     * @param lev Level that was just played
     * @param score Score earned on this level
     */
    public void endGame(Level lev, int score) {
        if (score > 0 && loggedIn) {
            try {
                WebService.submitScore(lev, score);
            }
            catch (Exception e) { e.printStackTrace(); }
        }
        currPanel.stop();
        removeKeyListener(currPanel);
        currPanel.setVisible(false);

        ip.reset();
        currPanel = ip;
        currPanel.setVisible(true);
        addKeyListener(currPanel);
        currPanel.start();
    }

    /**
     * Starts the LevelEditor and moves it to the front of the screen.
     */
    public void startEditor() {
        currPanel.pause();
        removeKeyListener(currPanel);
        currPanel.setVisible(false);

        currPanel = le;
        currPanel.setVisible(true);
        c.add(currPanel, BorderLayout.CENTER);
        currPanel.start();
    }

    /**
     * Returns to the IdlePanel and moves it to the front of the screen.
     */
    public void exitEditor() {
        currPanel.pause();
        removeKeyListener(currPanel);
        currPanel.setVisible(false);

        currPanel = ip;
        currPanel.setVisible(true);
        currPanel.start();
        c.add(ip, BorderLayout.CENTER);
    }

    // ----------------- window listener methods -------------
    /**
     * Resumes action in the top panel when the window is brought to the front.
     * @param e Event generated when the window is moved to the front
     */
    public void windowActivated(WindowEvent e) 
    { if (currPanel != null) currPanel.resume(); }

    /**
     * Pauses action in the top panel when the window is hidden by another window
     * @param e Event generated when the window is covered by another window
     */
    public void windowDeactivated(WindowEvent e) 
    {  if (currPanel != null) currPanel.pause(); }


    /**
     * Resumes action in the top panel when the window is deiconified.
     * @param e Event generated when the window is deiconified
     */
    public void windowDeiconified(WindowEvent e) 
    {  if (currPanel != null) currPanel.resume(); }

    /**
     * Pauses action in the top panel when the window is iconified.
     * @param e Event generated when the window is iconified
     */
    public void windowIconified(WindowEvent e) 
    {  if (currPanel != null) currPanel.pause(); }

    /**
     * Stops all action in the top panel when the window is closed.
     * @param e Event generated when the window is closed
     */
    public void windowClosing(WindowEvent e)
    {  
        if (currPanel != null) currPanel.stop();
    }

    public void windowOpened(WindowEvent e) {}
    public void edit() { }

  // ----------------------------------------------------

    /**
     * Main method.
     * First creates a PasswordBox, allowing the user to log in.  Then creates a new Start frame to begin game play.
     * @param args
     */
    public static void main(String args[]) {
        WebConfig.getInstance().setHost("cloud.cs50.net");
        WebConfig.getInstance().setPath("/~tmacwill/brickbreaker/index.php");
        //WebConfig.getInstance().setHost("localhost");
        //WebConfig.getInstance().setPath("/brickbreaker");

        
        try {
			System.out.println( "Loaded " + EncryptionUtil.init() + " keys" );
			PasswordBox pass = new PasswordBox();

	        // wait to get result from password box
	        while (!pass.checkLogin() && !pass.checkSkipped()) {
	            try {
	                Thread.sleep(200);
	            }
	            catch (Exception e) { }
	        }
	        // destroy password window
	        pass.setVisible(false);

	        // user has been logged in
	        if (pass.checkLogin())
	            new Start(true);
	        else
	            new Start(false);
		} catch (EncryptionFailureException e) {
			// can't login
			new Start(false);
		} catch (FilesystemFailureException e) {
			// can't load public keys
			new Start(false);
		}
        
    }

} // end of Main class
