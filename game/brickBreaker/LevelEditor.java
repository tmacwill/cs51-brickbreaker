package brickBreaker;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import brickBreaker.local.*;

/**
 *
 * @author robert
 */
public class LevelEditor extends PRPanel {

    public static final int PWIDTH = 1200;  // Size of panel
    public static final int PHEIGHT = 700;
    public static final int BORDER = 10;
    public static final int ARENA_WIDTH = PWIDTH - 2*BORDER;
    public static final int ARENA_HEIGHT = PHEIGHT - 2*BORDER;

    Start start;
    int controlsWidth = 200;
    int PWIDTHgrid = PWIDTH - controlsWidth;
    int PHEIGHTgrid = PHEIGHT;
    int width = PWIDTHgrid - 2 * BORDER;
    int height = PHEIGHTgrid - 2 * BORDER;
    int cornerX = BORDER;
    int cornerY = BORDER;
    int minRows = 1;
    int maxRows = 30;
    int minCols = 1;
    int maxCols = 30;
    int r;
    int c;
    int players;
    String name = "untitled-level";
    double boxHeight;
    double boxWidth;
    boolean gridGenerated = false;
    boolean mouseDown;
    int mx1 = -1, my1 = -1, mx2 = -1, my2 = -1; //coordinates of last two places where blocks where put by dragging mouse
    boolean[][] blocks;
    Brick[][] bricks;

    public LevelEditor(Start s) {
        start = s;
        initComponents();
    }

    /*public LevelEditor(Main m) {
        main = m;
        initComponents();
    }*/

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        generateGrid = new javax.swing.JButton();
        colInput = new javax.swing.JTextField();
        rowInput = new javax.swing.JTextField();
        numPlayers = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        saveDesign = new javax.swing.JButton();
        testLevel = new javax.swing.JButton();
        label1 = new java.awt.Label();
        levelName = new java.awt.TextField();

        setBackground(new java.awt.Color(255, 255, 255));
        setPreferredSize(new java.awt.Dimension(1200, 700));
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                LevelEditor.this.mouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                formMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                formMouseReleased(evt);
            }
        });
        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                formMouseDragged(evt);
            }
        });

        jLabel1.setText("rows");

        jLabel2.setText("columns");

        generateGrid.setText("Generate Grid");
        generateGrid.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                generateGridActionPerformed(evt);
            }
        });

        colInput.setText("20");

        rowInput.setText("20");

        numPlayers.setText("1");
        numPlayers.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                numPlayersActionPerformed(evt);
            }
        });

        jLabel3.setText("players");

        saveDesign.setText("Save Design");
        saveDesign.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveDesignActionPerformed(evt);
            }
        });

        testLevel.setText("Test Level");

        label1.setText("level name");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(1048, 1048, 1048)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(levelName, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 102, Short.MAX_VALUE)
                    .addComponent(testLevel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 102, Short.MAX_VALUE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel3)
                                .addComponent(jLabel2)
                                .addComponent(jLabel1))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(numPlayers, javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(rowInput, javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(colInput, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addComponent(generateGrid, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(label1, javax.swing.GroupLayout.DEFAULT_SIZE, 102, Short.MAX_VALUE)
                        .addComponent(saveDesign, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rowInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(colInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(4, 4, 4)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(numPlayers, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(generateGrid)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(levelName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(label1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(saveDesign)
                .addGap(18, 18, 18)
                .addComponent(testLevel)
                .addContainerGap(414, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private boolean checkGridInput() {
        r = Integer.parseInt(rowInput.getText());
        c = Integer.parseInt(colInput.getText());
        players = Integer.parseInt(numPlayers.getText());
        try {
            if (minRows <= r && r <= maxRows && minCols <= c && c <= maxCols && 1 <= players && players <= 4)
                return true;
            else
                return false;
        }
        catch (Exception e) {
            return false;
        }
    }

    private boolean inBounds(int x, int y) {
        if (cornerX < x && x < cornerX + width && cornerY < y && y < cornerY + height)
            return true;
        else
            return false;
    }

    private boolean inBoundsBlock(int blockX, int blockY) {
        if (gridGenerated) {
            if (0 <= blockX && blockX < c && 0 <= blockY && blockY < r)
                return true;
        }
        return false;
    }

    private int blockX(int x) {
        return (int) ((x - cornerX) / boxWidth);
    }

    private int blockY(int y) {
        return (int) ((y - cornerY) / boxHeight);
    }

    private void paintRect(int x, int y, Color color) {
        int blockX = blockX(x);
        int blockY = blockY(y);
        paintRectGivenBlockNumber(blockX, blockY, color);
    }

    private void paintRectGivenBlockNumber(int blockX, int blockY, Color color) {
        if (inBoundsBlock(blockX, blockY) && gridGenerated) {
            Graphics g;
            try {
                g = this.getGraphics();
                if (g != null) {
                    g.setColor(color);
                    g.fillRect((int) (cornerX + blockX * boxWidth) + 1, (int) (cornerY + blockY * boxHeight) + 1, (int) ((blockX + 1) * boxWidth) - (int) (blockX * boxWidth) - 1, (int) ((blockY + 1) * boxHeight) - (int) (blockY * boxHeight) - 1);
                    g.dispose();
                }
            }
            catch (Exception e) {
                System.out.println("Graphics context error");
            }
        }
    }

    private void insertBrickGivenBlockNumber(int blockX, int blockY) {
        if (!blocks[blockX][blockY]) {
            paintRectGivenBlockNumber(blockX, blockY, Color.magenta);
            blocks[blockX][blockY] = true;
            bricks[blockX][blockY] = new StandardBrick();
        }
    }

    private void removeBrickGivenBlockNumber(int blockX, int blockY) {
        if (blocks[blockX][blockY]) {
            paintRectGivenBlockNumber(blockX, blockY, Color.white);
            blocks[blockX][blockY] = false;
            bricks[blockX][blockY] = null;
        }
    }

    private void reverseBlockGivenBlockNumber(int blockX, int blockY) {
        if (!blocks[blockX][blockY])
            insertBrickGivenBlockNumber(blockX, blockY);
        else
            removeBrickGivenBlockNumber(blockX, blockY);
    }

    private void mouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mouseClicked
        if (gridGenerated) {
            int x = evt.getX();
            int y = evt.getY();
            if (inBounds(x, y)) {
                int blockX = blockX(x);
                int blockY = blockY(y);
                reverseBlockGivenBlockNumber(blockX, blockY);
            }
        }
    }//GEN-LAST:event_mouseClicked

    private void generateGridActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_generateGridActionPerformed
        if (checkGridInput()) {
            blocks = new boolean[c][r];
            bricks = new Brick[c][r];
            gridGenerated = true;
            Graphics g;
            try {
                g = this.getGraphics();
                if (g != null) {

                    /*//clear screen
                    this.remove(jLabel1);
                    this.remove(jLabel2);
                    this.remove(rowInput);
                    this.remove(colInput);
                    this.remove(generateGrid);
                    g.setColor(Color.white);
                    g.fillRect(0, 0, PWIDTHgrid, PHEIGHTgrid);*/
                    
                    //clear grid and draw border
                    g.setColor(Color.white);
                    g.fillRect(0, 0, PWIDTHgrid, PHEIGHTgrid);
                    g.setColor(Color.black);
                    g.drawRect(BORDER, BORDER, PWIDTHgrid - 2 * BORDER, PHEIGHTgrid - 2 * BORDER);
                    g.drawRect(BORDER - 1, BORDER - 1, PWIDTHgrid - 2 * BORDER + 2, PHEIGHTgrid - 2 * BORDER + 2);
                    g.drawRect(BORDER - 2, BORDER - 2, PWIDTHgrid - 2 * BORDER + 4, PHEIGHTgrid - 2 * BORDER + 4);

                    //draw grid
                    boxHeight = 1. * height / r;
                    boxWidth = 1. * width / c;
                    for (int i = 1; i < r; i++) {
                        g.drawLine(cornerX, cornerY + (int) (i * boxHeight), cornerX + width, cornerY + (int) (i * boxHeight));
                    }
                    for (int i = 1; i < c; i++) {
                        g.drawLine(cornerX + (int) (i * boxWidth), cornerY, cornerX + (int) (i * boxWidth), cornerY + height);
                    }

                    g.dispose();
                }
            }
            catch (Exception e) {
                System.out.println("Graphics context error: " + e);
            }
        }
        else {
            System.out.println("Inappropriate input.");
            return;
        }
    }//GEN-LAST:event_generateGridActionPerformed

    private void formMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMousePressed
        mouseDown = true;
    }//GEN-LAST:event_formMousePressed

    private void formMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseReleased
        mouseDown = false;
    }//GEN-LAST:event_formMouseReleased

    private void formMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseDragged
        if (mouseDown && gridGenerated) {
            int x = evt.getX();
            int y = evt.getY();
            if (inBounds(x, y)) {
                int blockX = blockX(x);
                int blockY = blockY(y);
                if (mx1 == blockX && my1 == blockY)
                    return;
                else if (mx2 == blockX && my2 == blockY)
                    return;
                else {
                    mx2 = mx1;
                    my2 = my1;
                    mx1 = blockX;
                    my1 = blockY;
                    reverseBlockGivenBlockNumber(blockX, blockY);
                }

            }
        }
    }//GEN-LAST:event_formMouseDragged

    private void numPlayersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_numPlayersActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_numPlayersActionPerformed

    private void saveDesignActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveDesignActionPerformed
        name = levelName.getText(); //FIXME: sanitize input
        Level l = new Level(bricks, players, name);
        LevelCatalog.getInstance().addLevel(l);
    }//GEN-LAST:event_saveDesignActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField colInput;
    private javax.swing.JButton generateGrid;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private java.awt.Label label1;
    private java.awt.TextField levelName;
    private javax.swing.JTextField numPlayers;
    private javax.swing.JTextField rowInput;
    private javax.swing.JButton saveDesign;
    private javax.swing.JButton testLevel;
    // End of variables declaration//GEN-END:variables

}
