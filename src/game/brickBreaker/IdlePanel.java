package brickBreaker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import brickBreaker.local.*;
import brickBreaker.web.*;

 /**
 * class IdlePanel - This is the panel from which the user can enter a game, the level editor, or interact with the online database.
 *
 * @author Robert Nishihara
 * @version 5/02/10
 * @file IdlePanel.java
 */
public class IdlePanel extends PRPanel { //implements ActionListener, KeyListener {

    public static final int PWIDTH = 1000; //Main.WIDTH;  // Size of panel
    public static final int PHEIGHT = 700; //Main.HEIGHT;
    private Start start;
    private boolean running;

    private static final int DISPLAYX = 350;
    private static final int DISPLAYY = 200;
    private static final int DISPLAYWIDTH = 300;
    private static final int DISPLAYHEIGHT = 300;

    private java.util.List<Level> levelObjectList;
    private java.util.List<OnlineLevel> onlineLevelList;

    /**
     * Constructor.
     *
     * @param s JFrame which IdlePanel is a part of
     */
    public IdlePanel(Start s) {
        start = s;

        setBackground(Color.black);
        setPreferredSize(new Dimension(PWIDTH, PHEIGHT));

        setFocusable(true);
        requestFocus();    // the JPanel now has focus, so receives key events
        initComponents();
        initLevelList();
        /**WebConfig.getInstance().setHost("brickbreaker.zxq.net");
        UserConfig.getInstance().setUsername("robert");
        UserConfig.getInstance().setPassword("123");
         * **/
        //WebConfig.getInstance().setHost("localhost");
        //WebConfig.getInstance().setPath("/brickbreaker");
        //UserConfig.getInstance().setUsername("test");
        //UserConfig.getInstance().setPassword("test");
    }

    /**
     * Starts IdlePanel
     *
     */
    public void start() {
        running = true;
        reset();
    }

    /**
     * Pauses IdlePanel
     */
    public void pause() {
        running = false;
    }

    /**
     * Resumes IdlePanel
     */
    public void resume() {
        running = true;
    }

    /**
     * Stops IdlePanel
     */
    public void stop() {
        running = false;
    }

    /**
     * Loads levels from the disk and adds them to the displayed list
     */
    private void initLevelList() {
        try {
			LevelCatalog.getInstance().reset();
		} catch (FilesystemFailureException e) { }
        levelObjectList = LevelCatalog.getInstance().getLevels();
        levelList.removeAll();
        for (Level l : levelObjectList) {
            levelList.add(l.getName());
        }
    }

    /**
     * Retrieves online levels and displays them
     */
    private void initOnlineLevelList() {
        onlineLevelListDisplay.removeAll();
        try {
			onlineLevelList = WebService.getOnlineLevels();
		} catch (Exception e) { }
        for (OnlineLevel l : onlineLevelList) {
            onlineLevelListDisplay.add(l.getTitle());
        }
    }

    /**
     * Clears any displayed level and resets the list of levels
     */
    public void reset() {
        initLevelList();
        Graphics g;
        try {
            g = start.getGraphics();
            if (g != null) {
                // clear level display
                g.setColor(Color.black);
                g.fillRect(DISPLAYX, DISPLAYY, DISPLAYWIDTH + 5, DISPLAYHEIGHT + 5); //5 is arbitrary. I just want to make sure we wipe the display
                g.dispose();
            }
        }
        catch (Exception e) {
            System.out.println("Graphics context error: " + e);
        }
    }

    /**
     * Displays a level
     * @param l the level to be displayed
     */
    private void displayLevel(Level l) {
        Graphics g;
            try {
                g = start.getGraphics();
                if (g != null) {

                    // initialize values
                    int rows = l.numRows();
                    int cols = l.numCols();
                    double boxWidth = (float) (DISPLAYWIDTH) / cols;
                    double boxHeight = (float) (DISPLAYHEIGHT) / rows;
                    Brick[][] b = l.getBricks();

                    // clear and draw border
                    g.setColor(Color.black);
                    g.fillRect(DISPLAYX, DISPLAYY, (int) (cols * boxWidth), (int) (rows * boxHeight));
                    g.setColor(Color.white);
                    g.drawRect(DISPLAYX, DISPLAYY, (int) (cols * boxWidth), (int) (rows * boxHeight));

                    // draw boxes
                    for (int r = 0; r < rows; r++) {
                        for (int c = 0; c < cols; c++) {
                            if (b[c][r] != null) {
                                int xTemp = (int) (c * boxWidth) + 1;
                                int yTemp = (int) (r * boxHeight) + 1;
                                int x2Temp = (int) ((c + 1) * boxWidth) - 1;
                                int y2Temp = (int) ((r + 1) * boxHeight) - 1;
                                if (b[c][r] instanceof StandardBrick) {
                                    g.setColor(Color.pink);
                                    g.fillRect(DISPLAYX + xTemp, DISPLAYY + yTemp, x2Temp - xTemp, y2Temp - yTemp);
                                }
                                if (b[c][r] instanceof PermanentBrick) {
                                    g.setColor(Color.gray);
                                    g.fillRect(DISPLAYX + xTemp, DISPLAYY + yTemp, x2Temp - xTemp, y2Temp - yTemp);
                                }
                            }
                        }
                    }
                    g.dispose();
                }
            }
            catch (Exception e) {
                System.out.println("Graphics context error: " + e);
            }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        levelList = new java.awt.List();
        label2 = new java.awt.Label();
        beginLevelEditor = new java.awt.Button();
        onlineLevelListDisplay = new java.awt.List();
        onlineLevelLabel = new java.awt.Label();
        browseOnlineLevelsButton = new java.awt.Button();
        label3 = new java.awt.Label();
        label4 = new java.awt.Label();
        label5 = new java.awt.Label();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(0, 0, 0));
        setPreferredSize(new java.awt.Dimension(1200, 700));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        levelList.setBackground(new java.awt.Color(1, 1, 1));
        levelList.setForeground(new java.awt.Color(255, 255, 255));
        levelList.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                levelListKeyPressed(evt);
            }
        });
        add(levelList, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 200, 250, 290));

        label2.setFont(new java.awt.Font("Dialog", 1, 18));
        label2.setForeground(new java.awt.Color(255, 255, 255));
        label2.setText("Levels");
        add(label2, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 160, -1, -1));

        beginLevelEditor.setBackground(new java.awt.Color(0, 0, 0));
        beginLevelEditor.setFont(new java.awt.Font("Dialog", 1, 12));
        beginLevelEditor.setForeground(new java.awt.Color(255, 255, 255));
        beginLevelEditor.setLabel("Enter Level Editor");
        beginLevelEditor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                beginLevelEditorActionPerformed(evt);
            }
        });
        add(beginLevelEditor, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 610, -1, -1));

        onlineLevelListDisplay.setBackground(new java.awt.Color(0, 0, 0));
        onlineLevelListDisplay.setForeground(new java.awt.Color(255, 255, 255));
        onlineLevelListDisplay.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                onlineLevelListDisplayKeyPressed(evt);
            }
        });
        add(onlineLevelListDisplay, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 200, 250, 290));

        onlineLevelLabel.setBackground(new java.awt.Color(0, 0, 0));
        onlineLevelLabel.setFont(new java.awt.Font("Dialog", 1, 18));
        onlineLevelLabel.setForeground(new java.awt.Color(255, 255, 255));
        onlineLevelLabel.setName(""); // NOI18N
        onlineLevelLabel.setText("Online Levels");
        add(onlineLevelLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 160, -1, -1));

        browseOnlineLevelsButton.setBackground(new java.awt.Color(0, 0, 0));
        browseOnlineLevelsButton.setFont(new java.awt.Font("Dialog", 1, 12));
        browseOnlineLevelsButton.setForeground(new java.awt.Color(255, 255, 255));
        browseOnlineLevelsButton.setLabel("Browse Online Levels");
        browseOnlineLevelsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                browseOnlineLevelsButtonActionPerformed(evt);
            }
        });
        add(browseOnlineLevelsButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 610, -1, -1));

        label3.setFont(new java.awt.Font("Dialog", 1, 14));
        label3.setForeground(new java.awt.Color(255, 255, 255));
        label3.setText("Enter - Select Level");
        add(label3, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 540, -1, -1));

        label4.setFont(new java.awt.Font("Dialog", 1, 14));
        label4.setForeground(new java.awt.Color(255, 255, 255));
        label4.setText("F1 - Upload Level");
        add(label4, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 570, -1, -1));

        label5.setFont(new java.awt.Font("Dialog", 1, 14));
        label5.setForeground(new java.awt.Color(255, 255, 255));
        label5.setText("F2 - Download Level");
        add(label5, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 500, -1, -1));

        jLabel1.setBackground(new java.awt.Color(0, 0, 0));
        jLabel1.setFont(new java.awt.Font("DejaVu Sans", 1, 14));
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Right Arrow - Display Level");
        add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 510, -1, -1));

        jLabel2.setBackground(new java.awt.Color(0, 0, 0));
        jLabel2.setFont(new java.awt.Font("DejaVu Sans", 1, 36));
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("BRICK BREAKER");
        add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 90, -1, -1));
    }// </editor-fold>//GEN-END:initComponents

    private void levelListKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_levelListKeyPressed
        if (running) {
            int code = evt.getKeyCode();
            if (code == KeyEvent.VK_RIGHT) {
                int i = levelList.getSelectedIndex();
                Level l = levelObjectList.get(i);
                displayLevel(l);
            }
            else if (code == KeyEvent.VK_ENTER) {
                int i = levelList.getSelectedIndex();
                Level lev = levelObjectList.get(i);
                start.startGame(lev);
            }
           else if (code == KeyEvent.VK_F1) {
               int i = levelList.getSelectedIndex();
               Level lev = levelObjectList.get(i);
               try {
				WebService.uploadLevel(lev, lev.getName());
			} catch (Exception e) { }
           }
        }
    }//GEN-LAST:event_levelListKeyPressed

    private void beginLevelEditorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_beginLevelEditorActionPerformed
        if (running) {
            start.startEditor();
        }
    }//GEN-LAST:event_beginLevelEditorActionPerformed

    private void browseOnlineLevelsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_browseOnlineLevelsButtonActionPerformed
        if (running) {
            initOnlineLevelList();
        }
    }//GEN-LAST:event_browseOnlineLevelsButtonActionPerformed

    private void onlineLevelListDisplayKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_onlineLevelListDisplayKeyPressed
        if (running) {
            int code = evt.getKeyCode();
            if (code == KeyEvent.VK_F2) {
                int i = onlineLevelListDisplay.getSelectedIndex();
                OnlineLevel ol = onlineLevelList.get(i);
                String levelID = ol.getLevelID();
                try {
                    Level l = WebService.downloadLevel(levelID);
					LevelCatalog.getInstance().addLevel(l);
				} catch (Exception e) { }
                reset();
            }
        }
    }//GEN-LAST:event_onlineLevelListDisplayKeyPressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private java.awt.Button beginLevelEditor;
    private java.awt.Button browseOnlineLevelsButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private java.awt.Label label2;
    private java.awt.Label label3;
    private java.awt.Label label4;
    private java.awt.Label label5;
    private java.awt.List levelList;
    private java.awt.Label onlineLevelLabel;
    private java.awt.List onlineLevelListDisplay;
    // End of variables declaration//GEN-END:variables

}
