/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * StartIdle.java
 *
 * Created on Apr 24, 2010, 3:46:02 PM
 */

package brickBreaker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 *
 * @author robert
 */
public class StartIdle extends javax.swing.JFrame {

    private IdlePanel l;
    private Start start = new Start();

    public StartIdle() {
        super("Level Editor");
        makeGUI();
        pack();
        //l.drawGrid();
        //initComponents();
    }

    private void makeGUI() {
        Container c = getContentPane();
        l = new IdlePanel(start);
        c.add(l, "Center");
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new StartIdle().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

}
