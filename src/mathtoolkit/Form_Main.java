package mathtoolkit;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;

import java.beans.PropertyVetoException;

import java.util.Arrays;
import java.util.TreeMap;

import javax.swing.JInternalFrame;
import javax.swing.JMenuItem;

import mathtoolkit.base.Rational;
import mathtoolkit.tableau.DataSet;
import mathtoolkit.tableau.Form_NewTableau;
import mathtoolkit.tableau.Form_Tableau;

public class Form_Main extends javax.swing.JFrame
{
    private static Form_Console _console;
    
    public Form_Main()
    {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setExtendedState(getExtendedState()|javax.swing.JFrame.MAXIMIZED_BOTH);
        initComponents();
        
        desktopPane.addContainerListener(new ContainerListener()
        {
            @Override
            public void componentAdded(ContainerEvent e)
            {    updateWindowList();    }
            
            @Override
            public void componentRemoved(ContainerEvent e)
            {    updateWindowList();    }
        });
        
        setLocationRelativeTo(null);
        
        _console = new Form_Console();
        desktopPane.add(_console);
        _console.setVisible(true);
        _console.setSize(3 * screenSize.width / 5, 3 * screenSize.height / 5);
        
        if(!MathKit.isDebug())
            jMenu4.setVisible(false);
        
        System.out.println("To begin, choose an item from the menu above.");
    }
    
    private void updateWindowList()
    {
        jMenu2.removeAll();
        
        // For sorting purposes.
        TreeMap<String, JInternalFrame> tm = new TreeMap<>();
        
        for(final JInternalFrame f : desktopPane.getAllFrames())
            tm.put(f.getTitle(), f);
        
        for(String s : tm.keySet())
        {
            final JInternalFrame f = tm.get(s);
            JMenuItem winItem = new JMenuItem();
            
            winItem.setText(s);
            
            winItem.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    if(PopOutFrame.class.isAssignableFrom(f.getClass()))
                            ((PopOutFrame)f).bringForward();
                    
                    else
                        
                        try
                        {
                            if(f.isIcon())
                                f.setIcon(false);

                            f.moveToFront();
                            f.setSelected(true);
                        }
                        
                        catch(PropertyVetoException ex)
                        {
                            System.err.println(Arrays.toString(ex.getStackTrace()));
                        }
                }
            });
            
            jMenu2.add(winItem);
        }
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        desktopPane = new javax.swing.JDesktopPane();
        menuBar = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenu4 = new javax.swing.JMenu();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Jimkit");

        jMenu1.setText("Math");

        jMenuItem1.setText("New Simplex Tableau");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        menuBar.add(jMenu1);

        jMenu2.setText("Window");
        menuBar.add(jMenu2);

        jMenu4.setText("Debug");

        jMenuItem5.setText("2X2 Max Test Tableau");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem5);

        jMenuItem4.setText("4X3 Max Cycling Tableau");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem4);
        jMenu4.add(jSeparator1);

        jMenuItem2.setText("Pop Out Console");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem2);

        jMenuItem3.setText("Pop Out All Windows");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem3);

        menuBar.add(jMenu4);

        setJMenuBar(menuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(desktopPane, javax.swing.GroupLayout.DEFAULT_SIZE, 536, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(desktopPane, javax.swing.GroupLayout.DEFAULT_SIZE, 365, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jMenuItem1ActionPerformed
    {//GEN-HEADEREND:event_jMenuItem1ActionPerformed
        new Form_NewTableau(this, this.desktopPane, false).setVisible(true);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jMenuItem2ActionPerformed
    {//GEN-HEADEREND:event_jMenuItem2ActionPerformed
        _console.popout();
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jMenuItem3ActionPerformed
    {//GEN-HEADEREND:event_jMenuItem3ActionPerformed
        for(JInternalFrame f : desktopPane.getAllFrames())
            if(PopOutFrame.class.isAssignableFrom(f.getClass()))
                ((PopOutFrame)f).popout();
        
        setExtendedState(getExtendedState()|javax.swing.JFrame.ICONIFIED);
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jMenuItem4ActionPerformed
    {//GEN-HEADEREND:event_jMenuItem4ActionPerformed
        DataSet ds = new DataSet();
        Form_Tableau f;
        Rational[][] r;
        String[][] cycler =
        {
            {"1/4", "-8", "-1", "9", "0"},
            {"1/2", "-12", "-1/2", "3", "0"},
            {"0", "0", "1", "0", "1"},
            {"3/4", "-20", "1/2", "-6", "0"}
        };
        
        r = new Rational[cycler.length][cycler[0].length];
        
        for(int i = 0; i < cycler.length; i++)
            for(int j = 0; j < cycler[i].length; j++)
                r[i][j] = new Rational(cycler[i][j]);
        
        String[] vars = {"x1", "x2", "x3", "x4", "-1"};
        String[] slks = {"t1", "t2", "t3", "f"};
        
        ds.setData(r);
        ds.setMax(vars, slks);
        
        f = new Form_Tableau(ds);
        desktopPane.add(f);
        f.setVisible(true);
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jMenuItem5ActionPerformed
    {//GEN-HEADEREND:event_jMenuItem5ActionPerformed
        DataSet ds = new DataSet();
        Form_Tableau f;
        Rational[][] r;
        String[][] tableau =
        {
            {"1", "2", "3"},
            {"4", "5", "6"},
            {"7", "8", "9"}
        };
        
        r = new Rational[tableau.length][tableau[0].length];
        
        for(int i = 0; i < tableau.length; i++)
            for(int j = 0; j < tableau[i].length; j++)
                r[i][j] = new Rational(tableau[i][j]);
        
        String[] vars = {"x1", "x2", "-1"};
        String[] slks = {"t1", "t2", "f"};
        
        ds.setData(r);
        ds.setMax(vars, slks);
        
        f = new Form_Tableau(ds);
        desktopPane.add(f);
        f.setVisible(true);        
    }//GEN-LAST:event_jMenuItem5ActionPerformed
    
    public static void main(String args[]) {
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Form_Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Form_Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Form_Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Form_Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        for(String s : args)
            if(s.equals("--debug"))
                MathKit.setDebugMode();
        
        java.awt.EventQueue.invokeLater(new Runnable() 
        {
            @Override
            public void run() 
            {
                new Form_Main().setVisible(true);
            }
        });
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JDesktopPane desktopPane;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JMenuBar menuBar;
    // End of variables declaration//GEN-END:variables
}