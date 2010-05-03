package brickBreaker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import brickBreaker.web.*;


public class Start extends JFrame implements WindowListener
{
    public static int WIDTH = 1200;
    public static int HEIGHT = 700;
    private boolean loggedIn = false;
    
    private GamePanel gp;
    private IdlePanel ip;
    private LevelEditor le;
    private PRPanel currPanel;
    Container c = getContentPane();
    
    public Start () {
        super("Brick Breaker");
        setBackground(Color.black);
        setPreferredSize(new Dimension(WIDTH+20, HEIGHT+30));

        ip = new IdlePanel(this);
        gp = new GamePanel(LevelInitializer.generateLevels(1)[0], this);
        gp.setVisible(false);
        le = new LevelEditor(this);
        le.setVisible(false);
        
        //Container c = getContentPane();
        //c.add(gp, BorderLayout.CENTER);
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

    public Start(boolean loggedIn) {
        this();
        this.loggedIn = loggedIn;
    }
  
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
    
    public void endGame(Level lev, int score) {
        if (score > 0 && loggedIn) {
            WebService.submitScore(lev, score);
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

    public void startEditor() {
         currPanel.pause();
         removeKeyListener(currPanel);
         currPanel.setVisible(false);

         currPanel = le;
         currPanel.setVisible(true);
         currPanel.start();
         c.add(le, BorderLayout.CENTER);
    }

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
    public void windowActivated(WindowEvent e) 
    { if (currPanel != null) currPanel.resume(); }
  
    public void windowDeactivated(WindowEvent e) 
    {  if (currPanel != null) currPanel.pause(); }

  
    public void windowDeiconified(WindowEvent e) 
    {  if (currPanel != null) currPanel.resume(); }

    public void windowIconified(WindowEvent e) 
    {  if (currPanel != null) currPanel.pause(); }

    public void windowClosing(WindowEvent e)
    {  
        if (currPanel != null) currPanel.stop();
    }

    public void windowClosed(WindowEvent e) {}
    public void windowOpened(WindowEvent e) {}
    public void edit() { }

  // ----------------------------------------------------

  public static void main(String args[])
  {
	EncryptionUtil.init( );
    PasswordBox pass = new PasswordBox();
    String name = pass.getResult();
    pass.setVisible(false);

    if (name.equals("")) new Start(false);
    else new Start(true);
  }

} // end of Main class
