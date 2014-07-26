package mathtoolkit;

import mathtoolkit.Tableau.Point;
import javax.swing.JOptionPane;

public class Form_Tableau extends javax.swing.JInternalFrame
{
    private static int frameCount = 0;
    
    public Form_Tableau()
    {    this(2, 2);    }
    
    public Form_Tableau(int variables, int constraints)
    {
        super("Simplex Tableau " + ++frameCount, true, true, true, true);
        initComponents();
        initTableau(variables, constraints);
                
        setLocation(45 * (frameCount % 10), 45 * (frameCount % 10));
    }
    
    private void initTableau(int variables, int constraints)
    {
        Tableau.TableauModel model;
        String[] str = new String[variables + 1];
        Object[] obj = new Object[variables + 1];
        
        for(int i = 0; i <= variables; i++)
            if(i == variables)
                str[i] = "-1";
            
            else
                str[i] = "x"+(i+1);
        
        model = new Tableau.TableauModel(new Object[][] {}, str);
        
        for(int i = 0; i < constraints; i++)
            model.addRow(obj);
        
        model.addRow(obj);
        tableau1.setModel(model);
    }
    
    private void findIdealPivot()
    {
        Point piv = MathKit.findIdealMBFPivot(tableau1.getData());
        
        if(piv.i >= 0 && piv.j >= 0)
            System.out.printf("A best probable pivot is %s at i=%d j=%d\n", 
                    tableau1.getValueAt(piv.i, piv.j).toString(), piv.i + 1,
                    piv.j + 1);
        
        else
            System.out.println("Ideal MBF pivot is not implemented yet.");
    }
    
    private void convertToMBF()
    {
        tableau1.setData(MathKit.convertToMBF(tableau1.getData()));
    }
    
    private void negativeTranspose()
    {
        tableau1.setData(MathKit.negativeTranspose(tableau1.getData()));
    }
    
    private void checkForKlingons()
    {
        Rational[][] tab = tableau1.getData();
                
        if(!MathKit.isBSO(tab))
            if(!MathKit.isMBF(tab))
                if(MathKit.isInfeasible(tab))
                    System.out.println("The tableau is infeasible.");
                else
                    System.out.println("The tableau is not in Maximum Basic "
                            + "Feasible form.");               
            
            else
                if(MathKit.isUnbounded(tab))
                    System.out.println("The tableau is unbounded.");
                
                else
                    System.out.println("The problem is in Maximum Basic "
                            + "Feasible form and is bounded.");
        
        else
            System.out.println("The basic solution of the current tableau is "
                    + "optimal.");
    }
    
    private void solve()
    {
        try
        {
            tableau1.setData(MathKit.dantzigSimplexAlgorithm(tableau1.getData(), false));
        }
         
        catch(java.lang.NumberFormatException e)
        {
            System.err.println("[Error] " + e.getLocalizedMessage());
            JOptionPane.showMessageDialog(null, "Tableau entries can only "
                    + "contain numbers, decimals, or front slashes (/).",
                    "Invalid Tableau Entry",
                    JOptionPane.ERROR_MESSAGE);
        }
        
        catch(java.lang.NullPointerException e)
        {
            System.err.println("[Error] " + e.getLocalizedMessage());
            JOptionPane.showMessageDialog(null, "Please enter a value in each"
                    + " tableau entry.",
                    "Empty Cell", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void showPivotMenu(java.awt.event.MouseEvent e)
    {
        if(e.isPopupTrigger())
        {
            jMenuItem1.setText("Pivot on " + tableau1.getValueAt(
                    tableau1.rowAtPoint(e.getPoint()), 
                    tableau1.columnAtPoint(e.getPoint())));
        
            jPopupMenu1.show(e.getComponent(), e.getX(), e.getY());
        }
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        jPopupMenu1 = new javax.swing.JPopupMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableau1 = new mathtoolkit.Tableau();

        jMenuItem1.setText("Pivot Here");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItem1);

        jMenuItem2.setText("Negative Transpose");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItem2);

        jMenuItem3.setText("Find Ideal Pivot");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItem3);

        jMenuItem4.setText("Convert to MBF");
        jMenuItem4.setToolTipText("");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItem4);

        jMenuItem5.setText("Check State");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItem5);

        jMenuItem6.setText("Solve");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItem6);

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);

        tableau1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String []
            {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tableau1.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        tableau1.setRowHeight(48);
        tableau1.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mousePressed(java.awt.event.MouseEvent evt)
            {
                tableau1MousePressed(evt);
            }
        });
        jScrollPane1.setViewportView(tableau1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 465, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 319, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
                
    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jMenuItem1ActionPerformed
    {//GEN-HEADEREND:event_jMenuItem1ActionPerformed
        tableau1.setData(MathKit.pivotTransform(
                new Point(tableau1.getSelectedRow(),
                        tableau1.getSelectedColumn()), tableau1.getData()));
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void tableau1MousePressed(java.awt.event.MouseEvent evt)//GEN-FIRST:event_tableau1MousePressed
    {//GEN-HEADEREND:event_tableau1MousePressed
        if(evt.getButton() == java.awt.event.MouseEvent.BUTTON3)
            tableau1.changeSelection(
                    tableau1.rowAtPoint(evt.getPoint()), 
                    tableau1.columnAtPoint(evt.getPoint()), false, false, false);
        showPivotMenu(evt);
    }//GEN-LAST:event_tableau1MousePressed

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jMenuItem6ActionPerformed
    {//GEN-HEADEREND:event_jMenuItem6ActionPerformed
        solve();
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jMenuItem5ActionPerformed
    {//GEN-HEADEREND:event_jMenuItem5ActionPerformed
        checkForKlingons();
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jMenuItem4ActionPerformed
    {//GEN-HEADEREND:event_jMenuItem4ActionPerformed
        convertToMBF();
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jMenuItem3ActionPerformed
    {//GEN-HEADEREND:event_jMenuItem3ActionPerformed
        findIdealPivot();
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jMenuItem2ActionPerformed
    {//GEN-HEADEREND:event_jMenuItem2ActionPerformed
        negativeTranspose();
    }//GEN-LAST:event_jMenuItem2ActionPerformed
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane1;
    private mathtoolkit.Tableau tableau1;
    // End of variables declaration//GEN-END:variables
}