package mathtoolkit.maximization;

import java.util.ArrayList;
import java.util.TreeMap;
import mathtoolkit.base.Point;
import mathtoolkit.base.Rational;

public class SimplexAlgorithm
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
    
    public static void checkState(DataSet in)
    {
        if(!isBSO(in.getData()))
            if(!isMBF(in.getData()))
                if(isInfeasible(in.getData()))
                    System.out.println("The tableau is infeasible.");
                
                else
                    System.out.println("The tableau is not in Maximum Basic "
                            + "Feasible form.");               
            
            else
                if(isUnbounded(in.getData()))
                    System.out.println("The tableau is unbounded.");
                
                else
                    System.out.println("The tableau is assumed to be in Maximum "
                            + "Basic form and is currently both feasible and "
                            + "bounded.");
        
        else
        {
            String sol = formatMaxSolution(in);
            
            if(!sol.equals(""))
                sol += "\n";
            
            System.out.printf("The basic solution of the current tableau is "
                    + "optimal.\n%s", sol);
        }
    }
    
    public static DataSet dantzigSimplexAlgorithm(DataSet in, boolean min)
    {
        Point pivot;
        int cycleCount = 50;
        DataSet out = in;
        
        // If the problem is exclusively a min problem, get the negative 
        // transposition of it.
        if(min)
            out = negativeTranspose(out);
        
        // Convert the tableau to maximum basic feasible form, if it isn't 
        // already.  Infeasibility is detected here.
        if(!isMBF(out.getData()))
            out = convertToMBF(out);  
        
        // If the tableau is not in BSO form, pivot until it is.  
        // Unboundedness is detected here.
        if(!isInfeasible(out.getData())) //IDEAL PIVOT RUNNING TWICE. WTF?
            while(!isBSO(out.getData()) 
                    && (pivot = findIdealMBFPivot(out)).i >= 0 && pivot.j >= 0)
                if(cycleCount-- <= 0)
                {
                    System.out.println("The tableau appears to be cycling.");
                    break;
                }
                
                else
                    out = pivotTransform(out, pivot);
        
        checkState(out);
        
        return out;
    }
    
    public static DataSet negativeTranspose(DataSet in)
    {
        DataSet out = new DataSet();
        Rational[][] inData = in.getData(),
                     outData = new Rational[inData[0].length][inData.length];
        
        if(!in.isMinNull())
        {
            String[] mxSlack;
            
            out.setMax(in.getMinVarArray(), in.getMinSlackArray());
            out.setMin(null, null);
            
            mxSlack = out.getMaxSlackArray();
            mxSlack[mxSlack.length - 1] = "-" + mxSlack[mxSlack.length - 1];
        }
        
        else
        {
            out.setMax(null, null);
            out.setMin(null, null);
            System.out.println("No minimum variables were present, thus all "
                    + "variables have been removed.");
        }
        
        out.generateVariableOrder();
        
        for(int i = 0; i < inData.length; i++)
            for(int j = 0; j < inData[i].length; j++)
                outData[j][i] = inData[i][j].multiply(-1);
        
        out.setData(outData);

        printTableauTransform("Negative Transposition", in, out);

        return out;
    }
    
    public static DataSet convertToMBF(DataSet in)
    {
        DataSet out = in;
        Point pv = findIdealMBPivot(in);
        int cycleCount = 50;
        
        while(!isMBF(out.getData()) && pv.i >= 0 && pv.j >= 0)
        {
            if(cycleCount-- > 0)
            {
                out = pivotTransform(out, pv);
                pv = findIdealMBPivot(out);
            }
            
            else
            {
                System.out.println("The tableau appears to be cycling.");
                break;
            }
        }
        
        return out;
    }
    
    public static Point findIdealMBPivot(DataSet in)
    {
        Rational[][] tab = in.getData();
        Point pos = new Point(-1, -1);
        
        // (1) The current tableau is a maximum tableau.
        
        // (2) If b1, b2, ... bm >= 0, go to step 6.        
        if(!isMBF(tab))
        {
            // (3) Choose a bi < 0 such that i is maximal.
            int i = tab.length - 2;
            
            while(i > 0 && tab[i][tab[i].length - 1].getValue() > 0)
                i--;
            
            // (4) If ai1, ai2, ... ain >= 0 STOP; the maximization problem is
            // infeasible.  Otherwise, continue.
            if(!isInfeasible(tab))
            {
                // (5) If i = m, choose amj < 0, pivot on amj, and go to (1)
                if(i == tab.length - 2)
                {
                    int j = 0;
                    
                    while(tab[i][j].getValue() >= 0 && j < tab[i].length - 1)
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

                    while(j < tab[i].length - 1 && tab[i][j].getValue() >= 0)
                        j++;
                    
                    minR = tab[i][p=j];
                    
                    for(int k = 0; k < tab.length - 1; k++)
                        if(tab[k][tab[k].length - 1].divide(tab[k][j]).getValue() 
                                < minR.getValue() && tab[k][j].getValue() > 0)
                        {
                            p = k;
                            minR = tab[k][j].divide(tab[k][j]);
                        }
                    
                    return new Point(p, j);
                }
            }
            
            else
                System.out.println("The Tableau is infeasible.");
        }
        
        return pos;
    }
    
    public static Point findIdealMBFPivot(DataSet in)
    {
        Rational[][] tab = in.getData();
        TreeMap<Integer, Rational> possibleCValues = new TreeMap<>();
        TreeMap<Double, ArrayList<Point>> possiblePivots = new TreeMap<>();
        Point pos = new Point(-1, -1);
        int c = -1;
        
        // (1) The current tableau is maximum basic feasible.
        if(isMBF(tab))
        {
            // (2) If c1, c2, c3 <= 0, STOP; the basic solution of the current
            // maximum tableau is optimal.  Otherwise, continue.
            if(!isBSO(tab))
            {
                // (3) Choose a c > 0;
                for(int a = 0; a < tab[tab.length - 1].length - 1; a++)
                    if(tab[tab.length - 1][a].getValue() > 0)
                        possibleCValues.put(a, tab[tab.length - 1][a]);
                
                // Rule #2 (Determination of pivot column).  Whenever there 
                // is more than one possible choice of pivot column in 
                // accordance with the simplex algorithm, choose the column
                // corresponding to the variable that appears nearest the 
                // top (or front) of the list.
                if(!in.isMaxNull())
                    if(possibleCValues.keySet().size() > 0)
                    {
                        RULE2:
                        for(String s : in.getVariableOrder())
                            for(int l1 : possibleCValues.keySet())
                                if(s.equals(in.getMaxVarArray()[l1]))
                                {
                                    c = l1;
                                    break RULE2;
                                }
                    }
                    
                    else
                        return pos;

                else
                    c = (int)possibleCValues.keySet().toArray()[0];
                
                // (4) If a1j, a2j, a3j <= 0, STOP; the maximization problem is
                // unbounded. Otherwise, continue.
                if(!isUnbounded(tab))
                {
                    // (5) Compute min{bi/aij:aij > 0} = bp/apj
                    // pivot on apj, and go to (1)
                    for(int i = 0; i < tab.length -1; i++)
                        if(tab[i][pos.j = c].getValue() > 0)
                            {
                                // We must now gather a list of all possible 
                                // pivot rows.
                                if(!possiblePivots.containsKey(
                                        tab[i][tab[i].length - 1]
                                                .divide(tab[i][c]).getValue()))
                                    possiblePivots.put(tab[i][tab[i].length - 1]
                                            .divide(tab[i][c]).getValue(),
                                            new ArrayList<Point>());
                                    
                                possiblePivots.get(tab[i][tab[i].length - 1]
                                        .divide(tab[i][c]).getValue()).add(
                                                new Point(i, c));
                            }
                    
                    ArrayList<Point> l = possiblePivots
                            .get(possiblePivots.firstKey());
                    
                    // Rule #1 (Determination of pivot row).  Whenever there is
                    // more than one possible choice of pivot row in accordance 
                    // with the simplex algorithm, choose the row corresponding
                    // to the variable that appears nearest the top (or front)
                    // of the list.
                    if(!in.isMaxNull())
                    {
                        RULE1:
                        for(String s : in.getVariableOrder())
                            for(Point l1 : l)
                                if(s.equals(in.getMaxSlackArray()[l1.i]))
                                {
                                    pos.i = l1.i;
                                    break RULE1;
                                }                            
                    }
                    
                    else
                        pos.i = l.get(0).i;                    
                }
                
                else
                    System.out.println("The Tableau is unbounded.");
            }
        }
        
        return pos;
    }
    
    public static DataSet pivotTransform(DataSet in, Point pv)
    {
        DataSet out = new DataSet(new Rational[in.getData().length][in.getData()[0].length]);
        
        if(pv.i < 0 || pv.j < 0)
            return in;
        
        // (1) Choose a nonzero pivot entry p inside the tableau, but not in the
        // objective function row/column or the -1 column/row.
        if(pv.i != in.getData().length - 1 && pv.j != in.getData()[in.getData().length - 1].length - 1 
                && in.getData()[pv.i][pv.j].getValue() != 0.0)
        {
            // (2) Interchange the variables corresponding to p's row and column,
            // leaving the signs behind.
            if(!in.isMaxNull())
            {
                out.setMax(in.getMaxVarArray(), in.getMaxSlackArray());
                
                out.getMaxVarArray()[pv.j] = in.getMaxSlackArray()[pv.i];
                out.getMaxSlackArray()[pv.i] = in.getMaxVarArray()[pv.j];
            }

            if(!in.isMinNull())
            {
                out.setMin(in.getMinVarArray(), in.getMinSlackArray());
                
                out.getMinVarArray()[pv.i] = in.getMinSlackArray()[pv.j];
                out.getMinSlackArray()[pv.j] = in.getMinVarArray()[pv.i];
            }
            
            // Note: It is important to preserve the original order of the 
            // variables to prevent cycling.
            if(!in.isOrderNull())
                out.setVariableOrder(in.getVariableOrder());
            
            // (3) Replace p by 1/p.
            out.getData()[pv.i][pv.j] = in.getData()[pv.i][pv.j].getInverse();
            
            // (4) Replace every entry q in the same row as p by q / p.
            for(int q = 0; q < in.getData()[pv.i].length; q++)
                if(q != pv.j)
                    out.getData()[pv.i][q] = in.getData()[pv.i][q].divide(in.getData()[pv.i][pv.j]);
            
            // (5) Replace every entry r in the same column as p by -r / p
            for(int r = 0; r < in.getData().length; r++)
                if(r != pv.i)
                    out.getData()[r][pv.j] = in.getData()[r][pv.j].multiply(-1).divide(in.getData()[pv.i][pv.j]);
            
            // (6) Every entry s not in the same row and not in the same column
            // as p determines a unique entry q in the same row as p and in the
            // same column as s and a unique entry r in the same column as p and
            // in the same row as s.
            // Replace s by (ps - qr)/p
            for(int a = 0; a < in.getData().length; a++)
                if(a != pv.i)
                    for(int b = 0; b < in.getData()[a].length; b++)
                        if(b != pv.j)
                        {
                            Rational    p = in.getData()[pv.i][pv.j],
                                        q = in.getData()[pv.i][b],
                                        r = in.getData()[a][pv.j],
                                        s = in.getData()[a][b];
                            
                            out.getData()[a][b] = p.multiply(s).subtract(q.multiply(r)).divide(p);
                        }
        }
        
        else
         {
            if(in.getData()[pv.i][pv.j].getValue() == 0) 
                System.out.println("Cannot pivot on a 0.");
            
            return in;
        }
        
        printTableauTransform("Pivot Transformation", in, out, pv);
        
        return out;        
    }
    
    private static String formatMaxEquation(DataSet in, int row)
    {
        String output = "";
        
        for(int i = 0; i < in.getData()[row].length; i++)
        {
            String  sign = (in.getData()[row][i].getValue() > 0)?"+":"-",
                    value = (in.getData()[row][i].getValue() > 0)?
                        in.getData()[row][i].toString():in.getData()[row][i].multiply(-1).toString();
            
            if(i == 0)
                output = String.format("%s ", in.getData()[row][i].toString());
            
            else if(i == in.getData()[row].length - 1)
                output += String.format("= %s", in.getData()[row][i].toString());
            
            else
                output += String.format("%s %s ", sign, value);
        }
        
        return output;
    }
    
    public static void printMaxEquation(DataSet in, int row)
    {    System.out.println(formatMaxEquation(in, row));    }
    
    private static String formatMaxSolution(DataSet in)
    {
        String output = "";
        
        if(!in.isMaxNull())
        {
            for(int i = 0; i < in.getMaxVarArray().length - 1; i++)
                output += String.format("%s = ", in.getMaxVarArray()[i]);
            
            output += "0, ";
            
            for(int i = 0; i < in.getMaxSlackArray().length - 1; i++)
                output += String.format("%s = %s, ", in.getMaxSlackArray()[i], 
                        in.getData()[i][in.getData()[i].length - 1].toString());
            
            output += String.format("max(%s) = %s", in.getMaxSlackArray()[in.getData().length - 1],
                    in.getData()[in.getData().length - 1][in.getData()[in.getData().length - 1].length - 1]
                            .multiply(-1).toString());
        }
        
        return output;
    }
    
    public static void printMaxSolution(DataSet in)
    {    System.out.println(formatMaxSolution(in));    }
    
    private static String[] formatTableau(DataSet in)
    {   return formatTableau(in, new Point(-1, -1));    }
    
    private static String[] formatTableau(DataSet in, Point p)
    {
        int lines = in.getData().length + 2,
            inLength = 0,
            varLength = 0;

        String[] output = new String[lines];
        String inFormat, varFormatL, varFormatR;
        
        // Calculate optimal widths.
        for(Rational[] n : in.getData())
            for(Rational r : n)
                inLength = Math.max(inLength, r.toString().length());

        if(!in.isMaxNull())
        {
            for(String s : in.getMaxVarArray())
                varLength = Math.max(varLength, s.length());

            for(String s : in.getMaxSlackArray())
                varLength = Math.max(varLength, s.length());
        }
        
        if(!in.isMinNull())
        {
            for(String s : in.getMinVarArray())
                varLength = Math.max(varLength, s.length());
            
            for(String s : in.getMinSlackArray())
                varLength = Math.max(varLength, s.length());
        }
        
        varLength += 3;
        
        inFormat = String.format("%%%ds", Math.max(varLength, ++inLength));
        varFormatL = String.format("%%-%ds", (varLength > 0)?varLength:1);
        varFormatR = String.format("%%%ds", (varLength > 0)?varLength:1);
                
        // Generate the maximum tableau's variables.
        output[0] = "  ";
        
        if(!in.isMinNull())
            output[0] += String.format(varFormatR, " ");
        
        else
            output[0] += " ";

            
        if(!in.isMaxNull())
        {
            for(int k = 0; k < in.getData()[0].length; k++)
                if(k < in.getMaxVarArray().length)
                    output[0] += String.format(inFormat, in.getMaxVarArray()[k]);
                
                else
                    output[0] += String.format(inFormat, " ");
            
            output[0] += String.format(varFormatL, " ");            
        }

        else
        {
            for(Rational unused : in.getData()[0])
                output[0] += String.format(inFormat, " ");
            
            output[0] += " ";
        }
        
        output[0] += "   ";
        
        // Generate the tableau's data.
        for(int i = 0; i < lines - 2; i++)
        {
            int lineNumber = i + 1;
            if(i < in.getData().length)
            {
                output[lineNumber] = ((!in.isMinNull())?String.format(varFormatR,
                        in.getMinVarArray()[i]):" ") + " [ ";
                
                for(int j = 0; j < in.getData()[i].length; j++)
                    output[lineNumber] += String.format(inFormat, 
                            in.getData()[i][j].toString()
                        + (( i == p.i && j == p.j)?"*":" "));
                
                output[lineNumber] += "] " 
                        + ((!in.isMaxNull())?
                        String.format(varFormatL, "="
                                + ((i < in.getData().length - 1)?"-":"") 
                                + in.getMaxSlackArray()[i]):" ");
            }
            
            else
            {
                output[lineNumber] += "   " + String.format(varFormatR, " ");
                
                for(Rational unused : in.getData()[0])
                    output[lineNumber] += String.format(inFormat, " ");
            }            
        }
        
        // Generate the minimum tableau's slack variables.
        output[lines - 1] = "  ";
        
        if(!in.isMinNull())
        {
            output[lines - 1] += String.format(varFormatR, " ");
            
            for(int k = 0; k < in.getData()[0].length; k++)
                if(k < in.getMinSlackArray().length)
                    output[lines - 1] += String.format(inFormat, 
                            "=" + in.getMinSlackArray()[k]);
                
                else
                    output[lines - 1] += String.format(inFormat, " ");            
        }

        else
        {
            for(Rational unused : in.getData()[0])
                output[lines - 1] += String.format(inFormat, " ");
            
            output[lines - 1] += " ";
        }

        if(!in.isMaxNull())
            output[lines - 1] += String.format(varFormatL, " ");
        
        else
            output[lines - 1] += " ";

        output[lines - 1] += "   ";
        
        return output;
    }
    
    public static void printTableau(String title, DataSet in)
    {
        String[] output = formatTableau(in);
        
        System.out.println(title);
        
        for(String o : output)
            System.out.println(o);

        System.out.println();
    }

    public static void printTableauTransform(String title, DataSet in, DataSet out)
    {   printTableauTransform(title, in, out, new Point(-1, -1));    }
    
    public static void printTableauTransform(String title, DataSet in, DataSet out, Point p)
    {
        String[] output, inTab, outTab;
        String inFormat, outFormat, arrFormat;
        int lines;
        
        inTab = formatTableau(in, p);
        outTab = formatTableau(out);
        
        lines = Math.max(inTab.length, outTab.length);
        
        inFormat = String.format("%%%ds", inTab.length);
        outFormat = String.format("%%%ds", outTab.length);
        
        output = new String[lines];
        
        arrFormat = "       %2s       ";
        
        for(int i = 0; i < lines; i++)
        {
            output[i] = "";
            
            if(i < inTab.length)
                output[i] += inTab[i];
            
            else
                output[i] += String.format(inFormat, "");
            
            output[i] += String.format(arrFormat, (i == lines / 2)?"->":"");
            
            if(i < outTab.length)
                output[i] += outTab[i];
            
            else
                output[i] += String.format(outFormat, "");
        }
        
        System.out.println(title);
        
        for(String o : output)
            if(!o.equals(""))
                System.out.println(o);
        
        System.out.println();
    }
}