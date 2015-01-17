package mathtoolkit;

import java.awt.Dimension;
import java.awt.Toolkit;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

public class Form_Console extends PopOutFrame
{
    private final static Logger LOG = Logger.getLogger(MathKit.class.getName());
    
    public Form_Console()
    {
        initComponents();
    }
        
    private void saveFile()
    {
        BufferedWriter bw;
        JFileChooser fc = new JFileChooser()
        {
            @Override
            public void approveSelection()
            {
                if(getSelectedFile().exists())
                {
                    int result = JOptionPane.showConfirmDialog(this,
                            "The selected file already exists. Would you like "
                                    + "to overwrite it?", "Existing file",
                            JOptionPane.YES_NO_CANCEL_OPTION);
                    
                    switch(result)
                    {
                        case JOptionPane.YES_OPTION:
                            super.approveSelection();
                            break;
                            
                        case JOptionPane.CANCEL_OPTION:
                            super.cancelSelection();
                            break;

                        case JOptionPane.CLOSED_OPTION:
                        case JOptionPane.NO_OPTION:
                            break;    
                    }
                }
                
                else
                    super.approveSelection();                    
            }
        };
        
        fc.setSelectedFile(new File("MathkitOutput.txt"));
        fc.setFileFilter(new FileFilter()
        {
            @Override
            public boolean accept(File f)
            {
                String ext;
                
                if(f.isDirectory())
                    return true;
                
                ext = getExt(f);
                
                if(ext != null)
                    if(ext.equals("txt"))
                        return true;
                
                return false;
            }
            
            @Override
            public String getDescription()
            {
                return "Text Files (*.txt)";
            }
            
            private String getExt(File f)
            {
                String extension = null,
                       filename = f.getName();
                
                int i = filename.lastIndexOf('.');
                
                if(i > 0 && i < filename.length() - 1)
                    extension = filename.substring(i + 1).toLowerCase();
                
                return extension;
            }
        });
        
        if(fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION)
        {
            try
            {
                bw = new BufferedWriter(new FileWriter(fc.getSelectedFile()));
                bw.write(console1.getText());
                bw.close();
                System.out.printf("Output saved to \"%s\"\n", 
                        fc.getSelectedFile().getAbsolutePath());
            }
            
            catch (IOException e)
            {
                System.out.println("[ERROR] Unable to save the file.");
                System.err.println(Arrays.toString(e.getStackTrace()));
                LOG.log(Level.SEVERE, e.toString(), e);
            }
        }
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        console1 = new mathtoolkit.Console();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        jMenuItem1 = new javax.swing.JMenuItem();

        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Console");

        console1.setColumns(20);
        console1.setRows(5);
        jScrollPane2.setViewportView(console1);

        jMenu2.setText("File");

        jMenuItem4.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem4.setText("Save Output");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem4);

        jMenuBar1.add(jMenu2);

        jMenu1.setText("Edit");

        jMenuItem3.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem3.setText("Copy");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem3);

        jMenuItem2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem2.setText("Select All");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);
        jMenu1.add(jSeparator1);

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_L, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem1.setText("Clear");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 709, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 383, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jMenuItem1ActionPerformed
    {//GEN-HEADEREND:event_jMenuItem1ActionPerformed
        console1.setText("");
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jMenuItem2ActionPerformed
    {//GEN-HEADEREND:event_jMenuItem2ActionPerformed
        console1.selectAll();
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jMenuItem3ActionPerformed
    {//GEN-HEADEREND:event_jMenuItem3ActionPerformed
        console1.copy();
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jMenuItem4ActionPerformed
    {//GEN-HEADEREND:event_jMenuItem4ActionPerformed
        saveFile();
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private mathtoolkit.Console console1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    // End of variables declaration//GEN-END:variables
}