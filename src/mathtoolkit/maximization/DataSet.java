package mathtoolkit.maximization;

import java.util.Arrays;

import mathtoolkit.MathKit;
import mathtoolkit.base.Rational;

public class DataSet
{
    private String[]        variableOrder,
                            maxVariables,
                            maxSlackVars,
                            minVariables,
                            minSlackVars;
    
    private Rational[][]    data;
    
    public DataSet()
    {
        data = null;
        variableOrder = null;
        maxVariables = null;
        minVariables = null;
        maxSlackVars = null;
        minSlackVars = null;
    }
    
    public DataSet(Rational[][] in)
    {
        this();
        
        if(in != null)
        {
            data = new Rational[in.length][in[0].length];

            for(int i = 0; i < in.length; i++)
                System.arraycopy(in[i], 0, data[i], 0, in[i].length);
        }
    }
    
    public DataSet(DataSet ds)
    {
        this();
        
        if(ds != null)
        {
            if(!ds.isDataNull())
            {
                data = new Rational[ds.data.length][ds.data[0].length];

                for(int i = 0; i < ds.data.length; i++)
                    System.arraycopy(ds.data[i], 0, data[i], 0, ds.data[i].length);
            }

            if(!ds.isOrderNull())
            {
                variableOrder = new String[ds.variableOrder.length];

                System.arraycopy(ds.variableOrder, 0, variableOrder, 0, ds.variableOrder.length);
            }

            if(!ds.isMaxNull())
            {
                maxVariables = new String[ds.maxVariables.length];
                maxSlackVars = new String[ds.maxSlackVars.length];

                System.arraycopy(ds.maxVariables, 0, maxVariables, 0, ds.maxVariables.length);
                System.arraycopy(ds.maxSlackVars, 0, maxSlackVars, 0, ds.maxSlackVars.length);
            }

            if(!ds.isMinNull())
            {
                minVariables = new String[ds.minVariables.length];
                minSlackVars = new String[ds.minSlackVars.length];

                System.arraycopy(ds.minVariables, 0, minVariables, 0, ds.minVariables.length);
                System.arraycopy(ds.minSlackVars, 0, minSlackVars, 0, ds.minSlackVars.length);
            }
        }
    }
    
    public void generateVariableOrder()
    {
        if(!isMaxNull())
        {
            variableOrder = new String[maxVariables.length + maxSlackVars.length - 2];
            
            System.arraycopy(maxVariables, 0, variableOrder, 0, maxVariables.length - 1);
            System.arraycopy(maxSlackVars, 0, variableOrder, maxVariables.length - 1, maxSlackVars.length - 1);
        }
        
        else
            variableOrder = null;
    }
    
    public void setData(Rational[][] in)
    {
        if(in != null)
        {
            data = new Rational[in.length][in[0].length];

            for(int i = 0; i < in.length; i++)
                System.arraycopy(in[i], 0, data[i], 0, in[i].length);
        }
    }
    
    public void setVariableOrder(String[] s)
    {
        if(s != null)
        {
            variableOrder = new String[s.length];

            System.arraycopy(s, 0, variableOrder, 0, s.length);
        }
    }
    
    public void setMax(String[] variables, String[] slacks)
    {
        if(variables != null && slacks != null)
        {
            maxVariables = new String[variables.length];
            maxSlackVars = new String[slacks.length];
            
            System.arraycopy(variables, 0, maxVariables, 0, variables.length);
            System.arraycopy(slacks, 0, maxSlackVars, 0, slacks.length);
        }
        
        else
        {
            maxVariables = null;
            maxSlackVars = null;
        }
        
        if(variableOrder == null)
            generateVariableOrder();
    }
    
    public void setMin(String[] variables, String[] slacks)
    {
        if(variables != null && slacks != null)
        {
            minVariables = new String[variables.length];
            minSlackVars = new String[slacks.length];
            
            System.arraycopy(variables, 0, minVariables, 0, variables.length);
            System.arraycopy(slacks, 0, minSlackVars, 0, slacks.length);
        }
        
        else
        {
            minVariables = null;
            minSlackVars = null;
        }
    }
    
    public Rational[][] getData()
    {   return data;    }
    
    public String[] getVariableOrder()
    {   return variableOrder;    }

    public String[] getMaxVarArray()
    {    return maxVariables;    }
    
    public String[] getMaxSlackArray()
    {    return maxSlackVars;    }

    public String[] getMinVarArray()
    {    return minVariables;    }

    public String[] getMinSlackArray()
    {    return minSlackVars;    }
    
    public boolean isDataNull()
    {    return data == null;    }
    
    public boolean isOrderNull()
    {   
        if(MathKit.isDebug()) 
            System.out.printf("Order Array: %s\n", Arrays.toString(variableOrder));
    
        return variableOrder == null;
    }
    
    public boolean isMaxNull()
    {    return maxVariables == null || maxSlackVars == null;    }
    
    public boolean isMinNull()
    {    return minVariables == null || minSlackVars == null;    }
}