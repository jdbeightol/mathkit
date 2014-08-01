package mathtoolkit;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;

import java.beans.PropertyVetoException;

import java.util.Arrays;
import java.util.TreeMap;

import javax.swing.JInternalFrame;
import javax.swing.JMenuItem;

import mathtoolkit.tableau.Form_NewTableau;
import mathtoolkit.tableau.Form_Tableau;

public class Form_Main extends javax.swing.JFrame
{
    private static Form_Console _CONSOLE;
    private static boolean _DEBUG = false;
    
    public Form_Main()
    {
        setExtendedState(getExtendedState()|javax.swing.JFrame.MAXIMIZED_BOTH);
        initComponents();
        
        desktopPane.addContainerListener(new ContainerListener()
        {
            @Override
            public void componentAdded(ContainerEvent e)
            {
                updateWindowList();
            }
            
            @Override
            public void componentRemoved(ContainerEvent e)
            {
                updateWindowList();
            }
        });
        
        setLocationRelativeTo(null);
        
        _CONSOLE = new Form_Console();
        desktopPane.add(_CONSOLE);
        _CONSOLE.setVisible(true);
        
        if(_DEBUG)
        {
            String[] X = {"x"},
                     Y = {"y"};
            
            Form_Tableau test = new Form_Tableau(2, 2, X, Y);
            desktopPane.add(test);
            test.setVisible(true);
        }
        
        System.out.println("To begin, choose an item from the menu above.");
    }
    
    private void updateWindowList()
    {
        jMenu3.removeAll();
        
        TreeMap<String, JInternalFrame> tm = new TreeMap<>();
        
        for(final JInternalFrame f : desktopPane.getAllFrames())
            tm.put(f.getTitle(), f);
        
        for(String s : tm.keySet())
        {
            final JInternalFrame f = tm.get(s);
            JMenuItem winMenu = new JMenuItem();
            
            winMenu.setText(s);
            
            winMenu.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
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
            
            jMenu3.add(winMenu);
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
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenu4 = new javax.swing.JMenu();
        jMenuItem3 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Jimkit");

        jMenu1.setText("Math");

        jMenuItem1.setText("Simplex Algorithm");
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

        jMenuItem2.setText("Pop Out Console");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem2);

        jMenu3.setText("Window List");
        jMenu2.add(jMenu3);

        menuBar.add(jMenu2);

        jMenu4.setText("Debug");

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
        _CONSOLE.popout();
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jMenuItem3ActionPerformed
    {//GEN-HEADEREND:event_jMenuItem3ActionPerformed
        for(JInternalFrame f : desktopPane.getAllFrames())
            if(PopOutFrame.class.isAssignableFrom(f.getClass()))
                ((PopOutFrame)f).popout();
        
        setExtendedState(getExtendedState()|javax.swing.JFrame.ICONIFIED);
    }//GEN-LAST:event_jMenuItem3ActionPerformed
    
    public static boolean debug()
    {   return _DEBUG;    }
    
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
                _DEBUG = true;
        
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
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuBar menuBar;
    // End of variables declaration//GEN-END:variables
}