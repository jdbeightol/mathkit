package mathtoolkit;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.table.DefaultTableModel;

public class Tableau extends JTable
{
    protected boolean wasEdit;
    
    public static class TableauModel extends DefaultTableModel
    {
        private boolean             isTableEditable;
        private final String[][]    maxVariables = new String[2][],
                                    minVariables = new String[2][];
        
        public TableauModel()
        {
            super();
            isTableEditable = true;
        }
        
        public TableauModel(Object[][] data)
        {
            this(data, null);   
        }

        public TableauModel(Object[][] data, Object[] columnNames)
        {
            super(data, columnNames);
            isTableEditable = true;
            
            if(data != null)
            {
                maxVariables[0] = new String[data.length];
                maxVariables[1] = new String[(data.length > 0)?data[0].length:0];
                minVariables[0] = new String[data.length];
                minVariables[1] = new String[(data.length > 0)?data[0].length:0];
            }
        }
        
        public TableauModel(int rowCount, int columnCount)
        {
            super(rowCount, columnCount);
            isTableEditable = true;
            
            maxVariables[0] = new String[columnCount];
            maxVariables[1] = new String[rowCount];
            minVariables[0] = new String[columnCount];
            minVariables[1] = new String[rowCount];
        }
        
        public String[][] getMaxVariables()
        {   return maxVariables;    }

        public String[][] getMinVariables()
        {   return minVariables;    }

        public void setTableEditable(boolean editable)
        {    isTableEditable = editable;    }
        
        @Override
        public boolean isCellEditable(int row, int column) 
        {    return isTableEditable;    }
    }
    
    public static class TableauCellEditor extends DefaultCellEditor
    {
        public TableauCellEditor()
        {    super(new JTextField());    }
        
        @Override
        public java.awt.Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column )
        {
            ((JTextField)editorComponent).setText(value.toString());
            ((JTextField)editorComponent).selectAll();
            
            return editorComponent;
        }
    }
    
    public Tableau()
    {
        super();
        
        wasEdit = false;
        setTableHeader(null);
        setCellEditor(new TableauCellEditor());
        initTabBehavior();
    }
        
    @Override
    public void changeSelection(final int row, final int column, boolean toggle, boolean extend)
    {    changeSelection(row, column, toggle, extend, false);    }
    
    public void changeSelection(final int row, final int column, boolean toggle, boolean extend, boolean edit)
    {
        super.changeSelection(row, column, toggle, extend);
        
        if(edit)
        {
            editCellAt(row, column);
            
            if(getCellEditor(row, column) != null)
                ((JTextField)cellEditor.getTableCellEditorComponent(this, 
                        getValueAt(row, column), true, row, column))
                        .selectAll();
        }
        
        transferFocus();
    }
    
    public Rational[][] getData() throws NumberFormatException, NullPointerException
    {
        TableauModel t = (TableauModel)getModel();
        
        Rational[][] tableauData = new Rational[t.getRowCount()][t.getColumnCount()];
        
        for(int i = 0 ; i < t.getRowCount() ; i++)
            for (int j = 0 ; j < t.getColumnCount(); j++)
                    tableauData[i][j] = new Rational((String)t.getValueAt(i, j));
        
        return tableauData;
    }
    
    public void setData(Rational[][] tableauData)
    {
        TableauModel model;
        int columns = tableauData[0].length;
        
        model = new Tableau.TableauModel(new Object[][] {}, new String[columns]);
        
        for(Rational[] r : tableauData)
        {
            String s[] = new String[r.length];
            
            for(int n = 0; n < r.length; n++)
                s[n] = r[n].toString();
                
            model.addRow(s);
        }
        
        this.setModel(model);
    }
    
    public String[][] getAllMaxVariables()
    {   return ((TableauModel)getModel()).getMaxVariables();    }
    
    public String[] getMaxVariables()
    {   return ((TableauModel)getModel()).getMaxVariables()[0];    }

    public String[] getMaxSlackVariables()
    {   return ((TableauModel)getModel()).getMaxVariables()[1];    }

    public String[][] getAllMinVariables()
    {   return ((TableauModel)getModel()).getMinVariables();    }
    
    public String[] minMaxVariables()
    {   return ((TableauModel)getModel()).getMinVariables()[0];    }

    public String[] getMinSlackVariables()
    {   return ((TableauModel)getModel()).getMinVariables()[1];    }

        private void initTabBehavior() 
    {
        getActionMap().put(getInputMap(
                Tableau.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).get(
                        KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0)),
                new AbstractAction() 
        {    
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                int row = getSelectedRow();
                int col = getSelectedColumn();
                
                if (++col >= getModel().getColumnCount())
                {
                    col = 0;
                    row++;
                }

                if(row >= getModel().getRowCount())
                    row = 0;

                if(row >= 0 && row < getModel().getRowCount()
                        && col >= 0 && col < getModel().getColumnCount())
                    changeSelection(row, col, false, false, true);
            }            
        });
        
        getActionMap().put(getInputMap(
                Tableau.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).get(
                        KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 
                        java.awt.event.InputEvent.SHIFT_DOWN_MASK)),
                new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                int row = getSelectedRow();
                int col = getSelectedColumn();
                

                if(--col < 0)
                {
                    col = getModel().getColumnCount() - 1;
                    row--;
                }

                if(row < 0)
                    row = getModel().getRowCount() - 1;                

                if(row >= 0 && row < getModel().getRowCount()
                        && col >= 0 && col < getModel().getColumnCount())
                    changeSelection(row, col, false, false, true);
            }
        });
    }
}