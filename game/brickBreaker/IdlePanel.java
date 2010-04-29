package brickBreaker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import brickBreaker.local.*;

public class IdlePanel extends PRPanel { //implements ActionListener, KeyListener {

    public static final int PWIDTH = 1200; //Main.WIDTH;  // Size of panel
    public static final int PHEIGHT = 700; //Main.HEIGHT;
    private Start start;

    private static final int DISPLAYX = 600;
    private static final int DISPLAYY = 200;
    private static final int DISPLAYWIDTH = 400;
    private static final int DISPLAYHEIGHT = 300;

    private java.util.List<Level> levelObjectList;

    private String[] msgs = { "F1..........New 1-player Game",
                                                "F2..........New 2-player Game",
                                                "F3..........Enter Level Editor",
                                                "F4..........Upload/Download Levels" };

    public IdlePanel(Start s) {
        start = s;

        setBackground(Color.black);
        setPreferredSize(new Dimension(PWIDTH, PHEIGHT));
        //addKeyListener(this);

        setFocusable(true);
        requestFocus();    // the JPanel now has focus, so receives key events
        initComponents();
        initLevelList();
    }

    /*public IdlePanelTemp() {
        initComponents();
    }*/

    public void start()
    {
        super.addNotify();  // creates the peer
        /*if (clock == null)
            clock = new javax.swing.Timer(delay,this);
        clock.start();*/
    }

    public void pause() {}
    public void resume() {}
    public void stop() {}

    private void initLevelList() {
        levelObjectList = LevelCatalog.getInstance().getLevels();
        levelList.clear();
        for (Level l : levelObjectList) {
            levelList.add(l.getName());
        }
    }

    public void reset() {
        Graphics g;
        try {
            g = this.getGraphics();
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

    private void displayLevel(Level l) {
        Graphics g;
            try {
                g = this.getGraphics();
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

    /*public void keyPressed(KeyEvent e)
    {
        //int code = e.getKeyCode();
        //if (code == KeyEvent.VK_F1) { }
    }

    public void keyReleased(KeyEvent e) {  }
    public void keyTyped(KeyEvent e) { } */

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        label1 = new java.awt.Label();
        levelList = new java.awt.List();
        label2 = new java.awt.Label();

        setBackground(new java.awt.Color(0, 0, 0));
        setPreferredSize(new java.awt.Dimension(1200, 700));
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                formKeyTyped(evt);
            }
        });

        label1.setFont(new java.awt.Font("Dialog", 1, 18));
        label1.setForeground(new java.awt.Color(255, 255, 255));
        label1.setText("F1 - Select Level : F2 - Enter Level Editor : F3 - Synchronize Levels");

        levelList.setBackground(new java.awt.Color(1, 1, 1));
        levelList.setForeground(new java.awt.Color(255, 255, 255));
        levelList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                levelListMouseClicked(evt);
            }
        });
        levelList.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                levelListKeyPressed(evt);
            }
        });

        label2.setFont(new java.awt.Font("Dialog", 1, 18));
        label2.setForeground(new java.awt.Color(255, 255, 255));
        label2.setText("Levels");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(265, 265, 265)
                        .addComponent(label1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(339, 339, 339)
                        .addComponent(label2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(242, 242, 242)
                        .addComponent(levelList, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(305, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(225, Short.MAX_VALUE)
                .addComponent(label2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(levelList, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(153, 153, 153)
                .addComponent(label1, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed

    }//GEN-LAST:event_formKeyPressed

    private void formKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyTyped

    }//GEN-LAST:event_formKeyTyped

    private void levelListKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_levelListKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_RIGHT) {
            int i = levelList.getSelectedIndex();
            Level l = levelObjectList.get(i);
            displayLevel(l);
        }
    }//GEN-LAST:event_levelListKeyPressed

    private void levelListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_levelListMouseClicked
        /*int i = levelList.getSelectedIndex();
        Level l = levelObjectList.get(i);
        displayLevel(l);*/
    }//GEN-LAST:event_levelListMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private java.awt.Label label1;
    private java.awt.Label label2;
    private java.awt.List levelList;
    // End of variables declaration//GEN-END:variables

}
