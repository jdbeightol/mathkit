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
    protected boolean wasEdit = false;
    
    public static class Point
    {
        int i, j;
        
        public Point()
        {    i = j = 0;    }
        
        public Point(int i, int j)
        {   this.i = i; this.j = j;   }
    }
    
    public static class TableauModel extends DefaultTableModel
    {
        private boolean isTableEditable = true;
        
        public TableauModel()
        {   super();    }
        
        public TableauModel(Object[][] data, Object[] columnNames)
        {    super(data, columnNames);  }
        
        public TableauModel(int rowCount, int columnCount)
        {    super(rowCount, columnCount);  }
            
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
        this.setTableHeader(null);
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
    
    private void initTabBehavior() 
    {
        getActionMap().put(getInputMap(
                Tableau.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).get(
                        KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0)),
                new AbstractAction() {
            
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