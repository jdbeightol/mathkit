package mathtoolkit.tableau;

import mathtoolkit.base.Rational;

public class DataSet
{
    public String[] maxVariables,
                    maxSlackVars,
                    minVariables,
                    minSlackVars;
    
    Rational[][]    data;
    
    public DataSet()
    {
        data = null;
        maxVariables = null;
        minVariables = null;
        maxSlackVars = null;
        minSlackVars = null;
    }
    
    public DataSet(Rational[][] in)
    {
        this();
        data = in;
    }
    
    public DataSet(Rational[][] in, String[] maxVars, String[] maxSlacks, String[] minVars, String[] minSlacks)
    {
        data = in;
        maxVariables = maxVars;
        minVariables = maxSlacks;
        maxSlackVars = minVars;
        minSlackVars = minSlacks;
    }
    
    public boolean isDataNull()
    {    return data == null;    }
    
    public boolean isMaxNull()
    {    return maxVariables == null || maxSlackVars == null;    }
    
    public boolean isMinNull()
    {    return minVariables == null || minSlackVars == null;    }
}