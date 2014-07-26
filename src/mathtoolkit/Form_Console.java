package mathtoolkit;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;

public class Form_Console extends javax.swing.JInternalFrame
{
    private class ConsoleOutputStream extends OutputStream 
    {
        private final JTextArea console;
        
        public ConsoleOutputStream(JTextArea output)
        {   console = output;    }
        
        @Override
        public void write(int in) throws IOException
        {    console.append(String.valueOf((char)in));    }  
    }
    
    public Form_Console()
    {
        initComponents();
        ((DefaultCaret)jTextArea1.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);        
        
        PrintStream st = new PrintStream(new ConsoleOutputStream(jTextArea1));
        
        System.setOut(st);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();

        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Console");

        jTextArea1.setEditable(false);
        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 777, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 478, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    // End of variables declaration//GEN-END:variables
}
