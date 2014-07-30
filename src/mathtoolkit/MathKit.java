package mathtoolkit;

import java.util.Arrays;
import java.util.TreeMap;

public class MathKit
{
    public static boolean isMBF(Rational[][] in)
    {
        for(int i = 0; i < in.length - 1; i++)
            if(in[i][in[i].length - 1].getValue() < 0)
                return false;
        
        return true;
    }
    
    public static boolean isBSO(Rational[][] in)
    {
        if(!isMBF(in))
            return false;
        
        for(int j = 0; j < in[in.length - 1].length - 1; j++)
            if(in[in.length - 1][j].getValue() > 0)
                return false;
        
        return true;
    }
    
    public static boolean isInfeasible(Rational[][] in)
    {
        for(int i = 0; i < in.length - 1; i++)
            if (in[i][in[i].length - 1].getValue() < 0)
            {
                boolean allValuesZeroOrPositive = true;
             
                for(int j = 0; j < in[i].length - 1; j++)
                    if(in[i][j].getValue() < 0)
                        allValuesZeroOrPositive = false;
                
                if(allValuesZeroOrPositive)
                    return true;                        
            }
        
        return false;
    }
    
    public static boolean isUnbounded(Rational[][] in)
    {
        for(int i = 0; i < in[in.length - 1].length - 1; i++)
            if(in[in.length - 1][i].getValue() > 0)
            {
                boolean allValuesZeroOrNegative = true;
             
                for(int j = 0; j < in.length - 1; j++)
                    if(in[j][i].getValue() > 0)
                        allValuesZeroOrNegative = false;
                
                if(allValuesZeroOrNegative)
                    return true;
            }
        
        return false;
    }
    
    public static void checkState(Rational[][] in)
    {
        if(!isBSO(in))
            if(!isMBF(in))
                if(isInfeasible(in))
                    System.out.println("The tableau is infeasible.");
                
                else
                    System.out.println("The tableau is not in Maximum Basic "
                            + "Feasible form.");               
            
            else
                if(isUnbounded(in))
                    System.out.println("The tableau is unbounded.");
                
                else
                    System.out.println("The problem is in Maximum Basic "
                            + "Feasible form and is bounded.");
        
        else
            System.out.println("The basic solution of the current tableau is "
                    + "optimal.");

    }
    
    public static DataSet dantzigSimplexAlgorithm(DataSet in, boolean min)
    {
        int cycleCount = 50;
        DataSet out = in;
        
        // If the problem is exclusively a min problem, get the negative 
        // transposition of it.
        if(min)
            out = negativeTranspose(out);
        
        // Convert the tableau to maximum basic feasible form, if it isn't 
        // already.  Infeasibility is detected here.
        if(!isMBF(out.data))
            out = convertToMBF(out);  
        
        // If the tableau is not in BSO form, check pivot until it is.  
        // Unboundedness is detected here.
        if(!isInfeasible(out.data))
            while(!isBSO(out.data) && findIdealMBFPivot(out.data).i >= 0)
                if(cycleCount-- <= 0)
                {
                    System.out.println("The tableau appears to be cycling.");
                    break;
                }
                
                else
                    out = pivotTransform(out, findIdealMBFPivot(out.data));
        
        checkState(out.data);
        
        return out;
    }
    
    public static DataSet negativeTranspose(DataSet in)
    {
        DataSet out = in;
        
        if(!in.isMinNull())
        {
            out = new DataSet(new Rational[in.data[0].length][in.data.length]);

            out.maxVariables = in.minVariables;
            out.maxSlackVars = in.minSlackVars;

            out.minVariables = null;
            out.minSlackVars = null;

            out.maxSlackVars[out.maxSlackVars.length - 1] = "-" 
                    + out.maxSlackVars[out.maxSlackVars.length - 1];

            for(int i = 0; i < in.data.length; i++)
                for(int j = 0; j < in.data[i].length; j++)
                    out.data[j][i] = in.data[i][j].multiply(-1);

            printTableauTransform("Negative Transposition", in, out);
        }
        
        else
            System.out.println("Unable to transpose the tableau as there are no"
                    + " minimum variables..");
        
        return out;
    }
    
    public static DataSet convertToMBF(DataSet in)
    {
        DataSet out = in;
        Point pv = findIdealMBPivot(in.data);
        int cycleCount = 50;
        
        while(!isMBF(out.data) && pv.i >= 0 && pv.j >= 0)
        {
            if(cycleCount-- > 0)
            {
                out = pivotTransform(out, pv);
                pv = findIdealMBPivot(out.data);
            }
            
            else
            {
                System.out.println("The tableau appears to be cycling.");
                break;
            }
        }
        
        return out;
    }
    
    public static Point findIdealMBPivot(Rational[][] in)
    {
        Point pos = new Point(-1, -1);
        
        // (1) The current tableau is a maximum tableau.
        
        // (2) If b1, b2, ... bm >= 0, go to step 6.        
        if(!isMBF(in))
        {
            // (3) Choose a bi < 0 such that i is maximal.
            int i = in.length - 2;
            
            while(i > 0 && in[i][in[i].length - 1].getValue() > 0)
                i--;
            
            // (4) If ai1, ai2, ... ain >= 0 STOP; the maximization problem is
            // infeasible.  Otherwise, continue.
            if(!isInfeasible(in))
            {
                // (5) If i = m, choose amj < 0, pivot on amj, and go to (1)
                if(i == in.length - 2)
                {
                    int j = 0;
                    
                    while(in[i][j].getValue() >= 0 && j < in[i].length - 1)
                        j++;
                    
                    return new Point(i, j);
                }
                
                // (5 Cont.) If i < m, choose aij < 0, compute 
                //          min({bi/aij}U{bk/akj:akj > 0} = bp/apj
                // pivot on apj, and go to (1).
                else
                {
                    int p, j = 0;
                    Rational minR;

                    while(j < in[i].length - 1 && in[i][j].getValue() >= 0)
                        j++;
                    
                    minR = in[i][p=j];
                    
                    for(int k = 0; k < in.length - 1; k++)
                        if(in[k][in[k].length - 1].divide(in[k][j]).getValue() 
                                < minR.getValue() && in[k][j].getValue() > 0)
                        {
                            p = k;
                            minR = in[k][j].divide(in[k][j]);
                        }
                    
                    return new Point(p, j);
                }
            }
            
            else
                System.out.println("The Tableau is infeasible.");
        }
        
        return pos;
    }
    
    public static Point findIdealMBFPivot(Rational[][] in)
    {
        TreeMap<Integer, Rational> possibleCValues = new TreeMap<>();
        Rational minR = new Rational(Long.MAX_VALUE, 1);
        Point pos = new Point(-1, -1);
        
        // (1) The current tableau is maximum basic feasible.
        if(isMBF(in))
        {
            // (2) If c1, c2, c3 <= 0, STOP; the basic solution of the current
            // maximum tableau is optimal.  Otherwise, continue.
            if(!isBSO(in))
            {
                // (3) Choose a c > 0;
                for(int a = 0; a < in[in.length - 1].length - 1; a++)
                    if(in[in.length - 1][a].getValue() > 0)
                        possibleCValues.put(a, in[in.length - 1][a]);
                
                // (4) If a1j, a2j, a3j <= 0, STOP; the maximization problem is
                // unbounded. Otherwise, continue.
                if(!isUnbounded(in))
                {
                    // (5) Compute min{bi/aij:aij > 0} = bp/apj
                    // pivot on apj, and go to (1)
                    for(int j : possibleCValues.keySet())
                        for(int i = 0; i < in.length - 1; i++)
                            if(in[i][j].getValue() > 0 
                                    && minR.getValue() > in[i][in[i].length - 1]
                                            .divide(in[i][j]).getValue())
                            {
                                minR = in[i][in[i].length - 1].divide(in[i][j]);
                                pos.i = i;
                                pos.j = j;
                            }
                }
                
                else
                    System.out.println("The Tableau is unbounded.");
            }
        }
        
        return pos;
    }
    
    public static DataSet pivotTransform(DataSet in, Point pv)
    {
        DataSet out = new DataSet(new Rational[in.data.length][in.data[0].length]);
        
        if(pv.i < 0 || pv.j < 0)
            return in;
        
        // (1) Choose a nonzero pivot entry p inside the tableau, but not in the
        // objective function row/column or the -1 column/row.
        if(pv.i != in.data.length - 1 && pv.j != in.data[in.data.length - 1].length - 1 
                && in.data[pv.i][pv.j].getValue() != 0.0)
        {
            // (2) Interchange the variables corresponding to p's row and column,
            // leaving the signs behind.
            if(!in.isMaxNull())
            {
                out.maxVariables = Arrays.copyOf(in.maxVariables, in.maxVariables.length);
                out.maxSlackVars = Arrays.copyOf(in.maxSlackVars, in.maxSlackVars.length);
                
                out.maxVariables[pv.j] = in.maxSlackVars[pv.i];
                out.maxSlackVars[pv.i] = in.maxVariables[pv.j];
            }

            if(!in.isMinNull())
            {
                out.minVariables = Arrays.copyOf(in.minVariables, in.minVariables.length);
                out.minSlackVars = Arrays.copyOf(in.minSlackVars, in.minSlackVars.length);
                
                out.minVariables[pv.j] = in.minSlackVars[pv.i];
                out.minSlackVars[pv.i] = in.minVariables[pv.j];
            }
            
            // (3) Replace p by 1/p.
            out.data[pv.i][pv.j] = in.data[pv.i][pv.j].getInverse();
            
            // (4) Replace every entry q in the same row as p by q / p.
            for(int q = 0; q < in.data[pv.i].length; q++)
                if(q != pv.j)
                    out.data[pv.i][q] = in.data[pv.i][q].divide(in.data[pv.i][pv.j]);
            
            // (5) Replace every entry r in the same column as p by -r / p
            for(int r = 0; r < in.data.length; r++)
                if(r != pv.i)
                    out.data[r][pv.j] = in.data[r][pv.j].multiply(-1).divide(in.data[pv.i][pv.j]);
            
            // (6) Every entry s not in the same row and not in the same column
            // as p determines a unique entry q in the same row as p and in the
            // same column as s and a unique entry r in the same column as p and
            // in the same row as s.
            // Replace s by (ps - qr)/p
            for(int a = 0; a < in.data.length; a++)
                if(a != pv.i)
                    for(int b = 0; b < in.data[a].length; b++)
                        if(b != pv.j)
                        {
                            Rational    p = in.data[pv.i][pv.j],
                                        q = in.data[pv.i][b],
                                        r = in.data[a][pv.j],
                                        s = in.data[a][b];
                            
                            out.data[a][b] = p.multiply(s).subtract(q.multiply(r)).divide(p);
                        }
        }
        
        else
         {
            if(in.data[pv.i][pv.j].getValue() == 0) 
                System.out.println("Cannot pivot on a 0.");
            
            return in;
        }
        
        printTableauTransform("Pivot Transformation", in, out, pv);
        
        return out;        
    }

    public static void printTableauTransform(String title, Rational[][] in, Rational[][] out)
    {   printTableauTransform(title, in, out, new Point(-1, -1));    }
    
    public static void printTableauTransform(String title, Rational[][] in, Rational[][] out, Point p)
    {   printTableauTransform(title, new DataSet(in), new DataSet(out), p);    }
    
    public static void printTableauTransform(String title, DataSet in, DataSet out)
    {   printTableauTransform(title, in, out, new Point(-1, -1));    }
    
    public static void printTableauTransform(String title, DataSet in, DataSet out, Point p)
    {
        int lines = Math.max(in.data.length, out.data.length) + 2,
            inLength = 0,
            outLength = 0,
            varLength = 0;

        String[] output = new String[lines];
        String inFormat, outFormat, varFormatL, varFormatR, arrFormat;
        
        // Calculate optimal widths.
        for(Rational[] n : in.data)
            for(Rational r : n)
                inLength = Math.max(inLength, r.toString().length());

        for(Rational[] t : out.data)
            for(Rational r : t)
                outLength = Math.max(outLength, r.toString().length());
        
        if(!in.isMaxNull())
        {
            for(String s : in.maxVariables)
                varLength = Math.max(varLength, s.length());

            for(String s : in.maxSlackVars)
                varLength = Math.max(varLength, s.length());
        }
        
        if(!in.isMinNull())
        {
            for(String s : in.minVariables)
                varLength = Math.max(varLength, s.length());
            
            for(String s : in.minSlackVars)
                varLength = Math.max(varLength, s.length());
        }
        
        varLength += 3;
        
        inFormat = String.format("%%%ds", Math.max(varLength, ++inLength));
        outFormat = String.format("%%%ds", Math.max(varLength, ++outLength));
        varFormatL = String.format("%%-%ds", (varLength > 0)?varLength:1);
        varFormatR = String.format("%%%ds", (varLength > 0)?varLength:1);
        
        arrFormat = "       %2s       ";
        
        // Generate the maximum tableau's variables.
        output[0] = "";
        
        if(!in.isMaxNull() || !out.isMaxNull())
        {
            output[0] += "  ";

            if(!in.isMinNull())
                output[0] += String.format(varFormatR, " ");
            
            else
                output[0] += " ";
        }

        if(!in.isMaxNull())
        {
            
            for(int k = 0; k < in.data[0].length; k++)
                if(k < in.maxVariables.length)
                    output[0] += String.format(inFormat, in.maxVariables[k]);
                
                else
                    output[0] += String.format(inFormat, " ");
        }

        else if(!in.isMaxNull() || !out.isMaxNull())
            for(int k = 0; k < in.data[0].length; k++)
                    output[0] += String.format(inFormat, " ");
        
        if(!in.isMaxNull() || !out.isMaxNull())
        {
            output[0] += "     ";

            output[0] += String.format(varFormatL, " ");

            output[0] += String.format(arrFormat, " ");

            if(!out.isMinNull())
                output[0] += String.format(varFormatR, " ");
        }
        
        if(!out.isMaxNull())
        {
            for(int k = 0; k < out.data[0].length; k++)
                if(k < out.maxVariables.length)
                    output[0] += String.format(outFormat, out.maxVariables[k]);
                
                else
                    output[0] += String.format(outFormat, " ");
        }
        
        else if(!in.isMaxNull() || !out.isMaxNull())
            for(int k = 0; k < out.data[0].length; k++)
                output[0] += String.format(outFormat, " ");
        
        // Generate the tableau's data.
        for(int i = 0; i < lines - 2; i++)
        {
            int lineNumber = i + 1;
            if(i < in.data.length)
            {
                output[lineNumber] = ((!in.isMinNull())?String.format(varFormatR,
                        in.minVariables[i]):" ") + " [ ";
                
                for(int j = 0; j < in.data[i].length; j++)
                    output[lineNumber] += String.format(inFormat, 
                            in.data[i][j].toString()
                        + (( i == p.i && j == p.j)?"*":" "));
                
                output[lineNumber] += "] " 
                        + ((!in.isMaxNull())?
                        String.format(varFormatL, "="
                                + ((i < in.data.length - 1)?"-":"") 
                                + in.maxSlackVars[i]):" ");
            }
            
            else
            {
                output[lineNumber] += "   " + String.format(varFormatR, " ");
                
                for(Rational unused : in.data[0])
                    output[lineNumber] += String.format(inFormat, " ");
            }
            
            output[lineNumber] += String.format(arrFormat,
                    (i == lines / 2 - 1)?"->":"  ");
            
            if(i < out.data.length)
            {
                output[lineNumber] += ((!out.isMinNull())?String.format(varFormatR,
                        out.minVariables[i]):"") + " [ ";
                
                for(Rational r : out.data[i])
                    output[lineNumber] += String.format(outFormat, r.toString() 
                            + " ");
                
                output[lineNumber] += "] " 
                        + ((!out.isMaxNull())?
                        String.format(varFormatL, "="
                                + ((i < out.data.length - 1)?"-":"") 
                                + out.maxSlackVars[i]):" ");
                
            }
            
            else
            {
                output[lineNumber] += "   " + String.format(varFormatL, " ");
                
                for(Rational unused : out.data[0])
                    output[lineNumber] += String.format(outFormat, " ");
            }
        }
        
        // Generate the minimum tableau's slack variables.
        output[lines - 1] = "";
        
        if(!in.isMinNull() || !out.isMinNull())
        {
            output[lines - 1] += "  ";

            if(!in.isMinNull())
                output[lines - 1] += String.format(varFormatR, " ");
        }

        if(!in.isMinNull())
        {
            
            for(int k = 0; k < in.data[0].length; k++)
                if(k < in.minVariables.length)
                    output[lines - 1] += String.format(outFormat, "=" + in.minSlackVars[k]);
                
                else
                    output[lines - 1] += String.format(inFormat, " ");
        }

        else if(!in.isMinNull() || !out.isMinNull())
            for(int k = 0; k < in.data[0].length; k++)
                    output[lines - 1] += String.format(inFormat, " ");
        
        if(!in.isMinNull() || !out.isMinNull())
        {
            output[lines - 1] += "     ";

            output[lines - 1] += String.format(varFormatL, " ");

            output[lines - 1] += String.format(arrFormat, " ");

            if(!out.isMinNull())
                output[lines - 1] += String.format(varFormatR, " ");
        }
        
        if(!out.isMinNull())
        {
            for(int k = 0; k < out.data[0].length; k++)
                if(k < out.minVariables.length)
                    output[lines - 1] += String.format(outFormat, "=" + out.minSlackVars[k]);
                
                else
                    output[lines - 1] += String.format(outFormat, " ");
        }
        
        else if(!in.isMinNull() || !out.isMinNull())
            for(int k = 0; k < out.data[0].length; k++)
                output[lines - 1] += String.format(outFormat, " ");
        
        
        // Print the generated data.
        System.out.printf("%s:\n", title);
        
        for(String o : output)
            if(!o.equals(""))
                System.out.println(o);

        System.out.println();
    }
}