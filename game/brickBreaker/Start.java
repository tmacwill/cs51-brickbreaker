package brickBreaker;



import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class Start extends JFrame implements WindowListener
{
    public static int WIDTH = 1200;
    public static int HEIGHT = 700;
  private GamePanel gp;        // where the game is drawn

  public Start ()
  { super("Running Game");
    makeGUI();

    addWindowListener( this );
    pack();
    setResizable(false);
    setVisible(true);
  }  // end of Start() constructor


  private void makeGUI()
  {
    Container c = getContentPane();    // default BorderLayout used

    gp = new GamePanel(1);
    c.add(gp, "Center");

//     JPanel ctrls = new JPanel();   // a row of textfields
//     ctrls.setLayout( new BoxLayout(ctrls, BoxLayout.X_AXIS));
// 
//     jtfBox = new JTextField("Boxes used: 0");
//     jtfBox.setEditable(false);
//     ctrls.add(jtfBox);
// 
//     jtfTime = new JTextField("Time Spent: 0 secs");
//     jtfTime.setEditable(false);
//     ctrls.add(jtfTime);
// 
//     c.add(ctrls, "South");
  }  // end of makeGUI()
  

  // ----------------- window listener methods -------------

  public void windowActivated(WindowEvent e) 
  { gp.resumeGame();  }
  
  public void windowDeactivated(WindowEvent e) 
  {  gp.pauseGame();  }

  
  public void windowDeiconified(WindowEvent e) 
  {  gp.resumeGame();  }

  public void windowIconified(WindowEvent e) 
  {  gp.pauseGame(); }

  
  public void windowClosing(WindowEvent e)
  {  gp.stopGame();  }


  public void windowClosed(WindowEvent e) {}
  public void windowOpened(WindowEvent e) {}

  // ----------------------------------------------------

  public static void main(String args[])
  { 
//     int fps = DEFAULT_FPS;
//     long period = (long) 1000.0/fps;
//     System.out.println("fps: " + fps + "; period: " + period + " ms");

    new Start();    // ms --> nanosecs 
  }

} // end of Start class
