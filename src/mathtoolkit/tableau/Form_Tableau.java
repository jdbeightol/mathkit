package mathtoolkit.tableau;

import java.util.Arrays;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import mathtoolkit.MathKit;
import mathtoolkit.base.Point;

public class Form_Tableau extends mathtoolkit.PopOutFrame
{
    private final static Logger LOG = Logger.getLogger(MathKit.class.getName());

    private static int FRAMECOUNT = 0;
    
    private Stack<DataSet> _history;
    private DataSet _original;   
    
    private abstract class ErrorCheck
    {
        public abstract void event();
        
        public void doEvents()
        {
            try
            {
                event();
            }

            catch(java.lang.NumberFormatException e)
            {
                System.err.println("[Error] " + e.getLocalizedMessage());
                JOptionPane.showMessageDialog(null, "Tableau entries can only "
                        + "contain numbers, decimals, or front slashes (/).",
                        "Invalid Tableau Entry",
                        JOptionPane.ERROR_MESSAGE);
            }
            
            catch(Exception e)
            {
                LOG.log(Level.SEVERE, e.toString(), e);
                System.out.println("Something bad happened. :(");
                System.err.println(Arrays.toString(e.getStackTrace()));
            }
        }
    }
    
    public Form_Tableau()
    {
        super("Simplex Tableau " + ++FRAMECOUNT, true, true, true, true);
        _history = new Stack<>();
        initComponents();
        setLocation(45 * (FRAMECOUNT % 10), 45 * (FRAMECOUNT % 10));
    }
    
    public Form_Tableau(DataSet ds)
    {
        this();
        tableau1.setData(ds);
    }
    
    public Form_Tableau(int variables, int constraints, String[] varX, String[] varY)
    {
        this();
        tableau1.create(variables, constraints, varX, varY);
        
        if(MathKit.isDebug())
            tableau1.debugFill();        
    }
    
    private void revertToOriginal()
    {
        addHistory(tableau1.getData());
        
        if(_original != null)
            tableau1.setData(_original);
        
        else
            System.out.println("There is no original tableau to which to revert.");
    }
    
    private void undo()
    {
        if(!_history.isEmpty())
            tableau1.setData(_history.pop());
        
        else
            System.out.println("There is no history to undo tableau to which to revert.");
        
        if(_history.isEmpty())
            undo.setEnabled(false);
    }
    
    private void addHistory(DataSet ds)
    {
        _history.add(ds);

        if(_original == null)
            _original = ds;
        
        revert.setEnabled(true);
        
        if(!_history.isEmpty())
            undo.setEnabled(true);
    }
    
    private void checkTableauState()
    {
        new ErrorCheck()
        {
            @Override
            public void event()
            {
                SimplexAlgorithm.printTableau("Current Tableau", tableau1.getData());
                SimplexAlgorithm.checkState(tableau1.getData());
            }
        }.doEvents();
    }
    
    private void pivot()
    {
        new ErrorCheck()
        {
            @Override
            public void event()
            {
                DataSet tData = tableau1.getData();
                tableau1.setData(SimplexAlgorithm.pivotTransform(tData, new Point(
                        tableau1.getSelectedRow(), tableau1.getSelectedColumn())
                ));
                addHistory(tData);                
            }
        }.doEvents();
    }
    
    private void findIdealPivot()
    {
        new ErrorCheck()
        {
            @Override
            public void event()
            {
                DataSet tData = tableau1.getData();
                Point piv = new Point(-1, -1);
                
                if(!SimplexAlgorithm.isBSO(tData.getData()))
                    if(SimplexAlgorithm.isMBF(tData.getData()))
                    {
                        if(!SimplexAlgorithm.isUnbounded(tData.getData()))
                            piv = SimplexAlgorithm.findIdealMBFPivot(tData);
                    }
                    
                    else
                        if(!SimplexAlgorithm.isInfeasible(tData.getData()))
                            piv = SimplexAlgorithm.findIdealMBPivot(tData);
                
                if(piv.i >= 0 && piv.j >= 0)
                    System.out.printf("The best probable pivot is the %s at "
                            + "(i=%d, j=%d).\n", 
                            tableau1.getValueAt(piv.i, piv.j).toString(), 
                            piv.i + 1, piv.j + 1);
                
                else
                    SimplexAlgorithm.checkState(tableau1.getData());
            }
        }.doEvents();
    }
    
    private void convertToMBF()
    {
        new ErrorCheck()
        {
            @Override
            public void event()
            {
                DataSet tData = tableau1.getData();
                tableau1.setData(SimplexAlgorithm.convertToMBF(tData));

                addHistory(tData);
                SimplexAlgorithm.checkState(tableau1.getData());                
            }
        }.doEvents();
    }
    
    private void negativeTranspose()
    {
        new ErrorCheck()
        {
            @Override
            public void event()
            {
                DataSet tData = tableau1.getData();
                tableau1.setData(SimplexAlgorithm.negativeTranspose(tData));

                addHistory(tData);
                SimplexAlgorithm.checkState(tableau1.getData());
            }
        }.doEvents();
    }
    
    private void solve(final boolean min)
    {
        new ErrorCheck()
        {
            @Override
            public void event()
            {
                DataSet tData = tableau1.getData();
                tableau1.setData(SimplexAlgorithm.dantzigSimplexAlgorithm(tData, min));

                addHistory(tData);
            }
        }.doEvents();
    }
    
    private void showPivotMenu(java.awt.event.MouseEvent e)
    {
        if(e.isPopupTrigger())
        {
            pivot.setText("Pivot on " + tableau1.getValueAt(
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
        pivot = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        findPivot = new javax.swing.JMenuItem();
        checkState = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JPopupMenu.Separator();
        jMenu1 = new javax.swing.JMenu();
        solveMax = new javax.swing.JMenuItem();
        solveMin = new javax.swing.JMenuItem();
        negTranspose = new javax.swing.JMenuItem();
        convertMBF = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        undo = new javax.swing.JMenuItem();
        revert = new javax.swing.JMenuItem();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableau1 = new mathtoolkit.tableau.Tableau();

        pivot.setText("Pivot Here");
        pivot.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                pivotActionPerformed(evt);
            }
        });
        jPopupMenu1.add(pivot);
        jPopupMenu1.add(jSeparator2);

        findPivot.setText("Find Ideal Pivot");
        findPivot.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                findPivotActionPerformed(evt);
            }
        });
        jPopupMenu1.add(findPivot);

        checkState.setText("Check Current State");
        checkState.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                checkStateActionPerformed(evt);
            }
        });
        jPopupMenu1.add(checkState);
        jPopupMenu1.add(jSeparator3);

        jMenu1.setText("Solve...");

        solveMax.setText("as Maximum Tableau");
        solveMax.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                solveMaxActionPerformed(evt);
            }
        });
        jMenu1.add(solveMax);

        solveMin.setText("as Minimum Tableau");
        solveMin.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                solveMinActionPerformed(evt);
            }
        });
        jMenu1.add(solveMin);

        jPopupMenu1.add(jMenu1);

        negTranspose.setText("Negative Transpose");
        negTranspose.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                negTransposeActionPerformed(evt);
            }
        });
        jPopupMenu1.add(negTranspose);

        convertMBF.setText("Convert to MBF");
        convertMBF.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                convertMBFActionPerformed(evt);
            }
        });
        jPopupMenu1.add(convertMBF);
        jPopupMenu1.add(jSeparator1);

        undo.setText("Undo");
        undo.setEnabled(false);
        undo.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                undoActionPerformed(evt);
            }
        });
        jPopupMenu1.add(undo);

        revert.setText("Revert to Original");
        revert.setEnabled(false);
        revert.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                revertActionPerformed(evt);
            }
        });
        jPopupMenu1.add(revert);

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);

        tableau1.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        tableau1.setRowHeight(48);
        tableau1.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mousePressed(java.awt.event.MouseEvent evt)
            {
                tableau1MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt)
            {
                tableau1MouseReleased(evt);
            }
        });
        jScrollPane1.setViewportView(tableau1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
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
                
    private void pivotActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_pivotActionPerformed
    {//GEN-HEADEREND:event_pivotActionPerformed
        pivot();
    }//GEN-LAST:event_pivotActionPerformed

    private void tableau1MousePressed(java.awt.event.MouseEvent evt)//GEN-FIRST:event_tableau1MousePressed
    {//GEN-HEADEREND:event_tableau1MousePressed
        if(evt.getButton() == java.awt.event.MouseEvent.BUTTON3)
            tableau1.changeSelection(
                    tableau1.rowAtPoint(evt.getPoint()), 
                    tableau1.columnAtPoint(evt.getPoint()), false, false, false);
        showPivotMenu(evt);
    }//GEN-LAST:event_tableau1MousePressed

    private void solveMaxActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_solveMaxActionPerformed
    {//GEN-HEADEREND:event_solveMaxActionPerformed
        solve(false);
    }//GEN-LAST:event_solveMaxActionPerformed

    private void checkStateActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_checkStateActionPerformed
    {//GEN-HEADEREND:event_checkStateActionPerformed
        checkTableauState();
    }//GEN-LAST:event_checkStateActionPerformed

    private void convertMBFActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_convertMBFActionPerformed
    {//GEN-HEADEREND:event_convertMBFActionPerformed
        convertToMBF();
    }//GEN-LAST:event_convertMBFActionPerformed

    private void findPivotActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_findPivotActionPerformed
    {//GEN-HEADEREND:event_findPivotActionPerformed
        findIdealPivot();
    }//GEN-LAST:event_findPivotActionPerformed

    private void negTransposeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_negTransposeActionPerformed
    {//GEN-HEADEREND:event_negTransposeActionPerformed
        negativeTranspose();
    }//GEN-LAST:event_negTransposeActionPerformed

    private void revertActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_revertActionPerformed
    {//GEN-HEADEREND:event_revertActionPerformed
        revertToOriginal();
    }//GEN-LAST:event_revertActionPerformed

    private void tableau1MouseReleased(java.awt.event.MouseEvent evt)//GEN-FIRST:event_tableau1MouseReleased
    {//GEN-HEADEREND:event_tableau1MouseReleased
        showPivotMenu(evt);        
    }//GEN-LAST:event_tableau1MouseReleased

    private void undoActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_undoActionPerformed
    {//GEN-HEADEREND:event_undoActionPerformed
        undo();
    }//GEN-LAST:event_undoActionPerformed

    private void solveMinActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_solveMinActionPerformed
    {//GEN-HEADEREND:event_solveMinActionPerformed
        solve(true);
    }//GEN-LAST:event_solveMinActionPerformed
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem checkState;
    private javax.swing.JMenuItem convertMBF;
    private javax.swing.JMenuItem findPivot;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JMenuItem negTranspose;
    private javax.swing.JMenuItem pivot;
    private javax.swing.JMenuItem revert;
    private javax.swing.JMenuItem solveMax;
    private javax.swing.JMenuItem solveMin;
    private mathtoolkit.tableau.Tableau tableau1;
    private javax.swing.JMenuItem undo;
    // End of variables declaration//GEN-END:variables
}