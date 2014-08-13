package mathtoolkit.maximization;

import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.table.DefaultTableModel;

import mathtoolkit.base.Rational;

public class Tableau extends JTable
{  
    public static class TableauModel extends DefaultTableModel
    {
        private boolean             isTableEditable;
        private final String[][]    mxVar = new String[2][],
                                    mnVar = new String[2][];
        private String[]            vrOrd = null;
        
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
                mxVar[0] = new String[data.length];
                mxVar[1] = new String[(data.length > 0)?data[0].length:0];
                mnVar[0] = new String[data.length];
                mnVar[1] = new String[(data.length > 0)?data[0].length:0];

                vrOrd = new String[(data.length > 0)?data[0].length:0 + data.length];
            }
        }
        
        public TableauModel(int rowCount, int columnCount)
        {
            super(rowCount, columnCount);
            isTableEditable = true;
            
            mxVar[0] = new String[columnCount];
            mxVar[1] = new String[rowCount];
            mnVar[0] = new String[columnCount];
            mnVar[1] = new String[rowCount];
            
            vrOrd = new String[rowCount + columnCount];
        }
        
        public String[][] getMaxVariables()
        {   return mxVar;    }

        public String[][] getMinVariables()
        {   return mnVar;    }
        
        public String[] getVariableOrder()
        {   return vrOrd;    }
        
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
        
        setTableHeader(null);
        setCellEditor(new TableauCellEditor());
        initKeyBindings();
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
    
    public void create(int variables, int constraints, String[] varX, String[] varY)
    {
        DataSet d = new DataSet(new Rational[constraints + 1][variables + 1]);        
        String[] maxVariables, maxSlackVars, minVariables, minSlackVars;
        
        if(varX != null && !varX[0].equals(""))
        {
            maxVariables = new String[variables + 1];
            maxSlackVars = new String[constraints + 1];
            
            System.arraycopy(varX, 0, maxVariables, 0, varX.length);
            
            if(varX.length < variables)
            {
                maxVariables[varX.length - 1] += "1";
                
                for(int i = varX.length; i < variables; i++)
                    maxVariables[i] = varX[varX.length - 1] + (2 + i - varX.length);
            }
            
            maxVariables[variables] = "-1";
            
            for(int i = 0; i < constraints; i++)    
                maxSlackVars[i] = "t" + (1 + i);
            
            maxSlackVars[constraints] = "f";
            d.setMax(maxVariables, maxSlackVars);
        }
        
        if(varY != null && !varY[0].equals(""))
        {
            minVariables = new String[constraints + 1];
            minSlackVars = new String[variables + 1];
            
            System.arraycopy(varY, 0, minVariables, 0, varY.length);
            
            if(varY.length < constraints)
            {
                minVariables[varY.length - 1] += "1";
                
                for(int i = varY.length; i < constraints; i++)
                    minVariables[i] = varY[varY.length - 1] + (2 + i - varY.length);
            }
            
            minVariables[constraints] = "-1";
            
            for(int i = 0; i < variables; i++)
                minSlackVars[i] = "s" + (1 + i);
            
            minSlackVars[variables] = "g";
    
            d.setMin(minVariables, minSlackVars);
        }
        
        setData(d);
    }
    
    public void debugFill()
    {
        TableauModel t = (TableauModel)getModel();

        for(int i = 0; i < t.getRowCount(); i++)
            for(int j = 0; j < t.getColumnCount(); j++)
                t.setValueAt("1", i, j);
    }
    
    public DataSet getData() throws NumberFormatException
    {
        DataSet td = new DataSet();
        TableauModel t = (TableauModel)getModel();
        
        Rational[][] tData = new Rational[t.getRowCount()][t.getColumnCount()];
        
        for(int i = 0 ; i < t.getRowCount() ; i++)
            for (int j = 0 ; j < t.getColumnCount(); j++)
                    tData[i][j] = new Rational(
                            (t.getValueAt(i, j) == null
                                    || ((String)t.getValueAt(i, j)).equals(""))
                                    ?"0":(String)t.getValueAt(i, j)
                    );
        
        td.setData(tData);
        
        td.setMax(t.mxVar[0], t.mxVar[1]);
        td.setMin(t.mnVar[0], t.mnVar[1]);
        td.setVariableOrder(t.vrOrd);
        
        return td;
    }
    
    public void setData(DataSet tableauData)
    {
        int columns = tableauData.getData()[0].length;
        TableauModel t;
        
        t = new Tableau.TableauModel(new Object[][] {}, new String[columns]);
        
        t.vrOrd = tableauData.getVariableOrder();
        
        t.mxVar[0] = tableauData.getMaxVarArray();
        t.mxVar[1] = tableauData.getMaxSlackArray();
        t.mnVar[0] = tableauData.getMinVarArray();
        t.mnVar[1] = tableauData.getMinSlackArray();
        
        for(Rational[] r : tableauData.getData())
        {
            String s[] = new String[r.length];
            
            for(int n = 0; n < r.length; n++)
                s[n] = (r[n] != null)?r[n].toString():"";
            
            t.addRow(s);
        }
        
        this.setModel(t);
    }
    
    public void removeRow(int rowNumber)
    {
        int x;
        
        DataSet ds = getData();
        
        Rational[][] data = ds.getData();
        Rational[][] newData = new Rational[data.length - 1][data[0].length];
        
        String[] maxSlacks = ds.getMaxSlackArray();
        String[] minVars = ds.getMinVarArray();
        
        String[] newSlacks = new String[maxSlacks.length - 1];
        String[] newVars = new String[minVars.length - 1];
        
        for(int i = x = 0; i < data.length; i++)
            if(i != rowNumber)
                System.arraycopy(data[i], 0, newData[x++], 0, data.length);
        
        System.arraycopy(maxSlacks, 0, newSlacks, 0, rowNumber);
        System.arraycopy(maxSlacks, rowNumber, newSlacks, 0, maxSlacks.length - rowNumber - 1);
        
        System.arraycopy(minVars, 0, newVars, 0, rowNumber);
        System.arraycopy(minVars, rowNumber, newVars, 0, minVars.length - rowNumber - 1);
        
        ds.setData(newData);
        
        ds.setMax(ds.getMaxVarArray(), newSlacks);
        ds.setMin(newVars, ds.getMinSlackArray());
        
        setData(ds);
    }
    
    public void removeColumn(int colNumber)
    {
        DataSet ds = getData();
        
        setData(ds);
    }
        
    private void initKeyBindings() 
    {
        getActionMap().put(getInputMap(
                Tableau.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
                .get(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0)),
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
                Tableau.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
                .get(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 
                        InputEvent.SHIFT_DOWN_MASK)),
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