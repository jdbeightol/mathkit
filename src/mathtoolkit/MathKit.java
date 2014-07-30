package mathtoolkit;

import mathtoolkit.Tableau.Point;
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
    
    public static Rational[][] dantzigSimplexAlgorithm(Rational[][] in, boolean min)
    {
        int cycleCount = 50;
        Rational[][] out = in;
        
        // If the problem is exclusively a min problem, get the negative 
        // transposition of it.
        if(min)
            out = negativeTranspose(out);
        
        // Convert the tableau to maximum basic feasible form, if it isn't 
        // already.  Infeasibility is detected here.
        if(!isMBF(out))
            out = convertToMBF(out);  
        
        // If the tableau is not in BSO form, check pivot until it is.  
        // Unboundedness is detected here.
        if(!isInfeasible(in))
            while(!isBSO(out) && findIdealMBFPivot(out).i >= 0)
                if(cycleCount-- <= 0)
                {
                    System.out.println("The tableau appears to be cycling.");
                    break;
                }
                
                else
                    out = pivotTransform(findIdealMBFPivot(out), out);
        
        checkState(out);
        
        return out;
    }
    
    public static Rational[][] negativeTranspose(Rational[][] in)
    {
        Rational[][] out = new Rational[in[0].length][in.length];
        
        for(int i = 0; i < in.length; i++)
            for(int j = 0; j < in[i].length; j++)
                out[j][i] = in[i][j].multiply(-1);

        printTableauTransform("Negative Transposition", in, out);
        
        return out;
    }
    
    public static Rational[][] convertToMBF(Rational[][] in)
    {
        Rational[][] out = in;
        Point pv = findIdealMBPivot(in);
        
        while(!isMBF(out) && pv.i >= 0 && pv.j >= 0)
        {
            out = pivotTransform(pv, out);
            pv = findIdealMBPivot(out);
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
        
    public static Rational[][] pivotTransform(Point pivot, Rational[][] in)
    {   return MathKit.pivotTransform(pivot, in, null, null);    }    
    
    public static Rational[][] pivotTransform(Point pv, Rational[][] in, String[] v, String[] t)
    {
        Rational[][] out = new Rational[in.length][in[0].length];
        
        if(pv.i < 0 || pv.j < 0)
            return in;
        
        // (1) Choose a nonzero pivot entry p inside the tableau, but not in the
        // objective function row/column or the -1 column/row.
        if(pv.i != in.length - 1 && pv.j != in[in.length - 1].length - 1 
                && in[pv.i][pv.j].getValue() != 0.0)
        {
            // (2) Interchange the variables corresponding to p's row and column,
            // leaving the signs behind.
            if(v != null && t != null)
            {
                String temp = v[pv.j];
                
                v[pv.j] = t[pv.i];
                t[pv.i] = temp;
            }

            // (3) Replace p by 1/p.
            out[pv.i][pv.j] = in[pv.i][pv.j].getInverse();
            
            // (4) Replace every entry q in the same row as p by q / p.
            for(int q = 0; q < in[pv.i].length; q++)
                if(q != pv.j)
                    out[pv.i][q] = in[pv.i][q].divide(in[pv.i][pv.j]);
            
            // (5) Replace every entry r in the same column as p by -r / p
            for(int r = 0; r < in.length; r++)
                if(r != pv.i)
                    out[r][pv.j] = in[r][pv.j].multiply(-1).divide(in[pv.i][pv.j]);
            
            // (6) Every entry s not in the same row and not in the same column
            // as p determines a unique entry q in the same row as p and in the
            // same column as s and a unique entry r in the same column as p and
            // in the same row as s.
            // Replace s by (ps - qr)/p
            for(int a = 0; a < in.length; a++)
                if(a != pv.i)
                    for(int b = 0; b < in[a].length; b++)
                        if(b != pv.j)
                        {
                            Rational    p = in[pv.i][pv.j],
                                        q = in[pv.i][b],
                                        r = in[a][pv.j],
                                        s = in[a][b];
                            
                            out[a][b] = p.multiply(s).subtract(q.multiply(r)).divide(p);
                        }
        }
        
        else
         {
            if(in[pv.i][pv.j].getValue() == 0) 
                System.out.println("Cannot pivot on a 0.");
            
            return in;
        }
        
        printTableauTransform("Pivot Transformation", in, out, pv);
        
        return out;        
    }

    public static void printTableauTransform(String title, Rational[][] in, Rational[][] out)
    {   printTableauTransform(title, in, out, new Point(-1, -1));    }
    
    public static void printTableauTransform(String title, Rational[][] in, Rational[][] out, Point p)
    {
        int lines = Math.max(in.length, out.length),
            inLength = 0,
            outLength = 0;
        String[] output = new String[lines];
        String inFormat, outFormat;
        
        for(Rational[] n : in)
            for(Rational r : n)
                inLength = Math.max(inLength, r.toString().length());

        for(Rational[] t : out)
            for(Rational r : t)
                outLength = Math.max(outLength, r.toString().length());
        
        inFormat = String.format("%%%ds", inLength + 1);
        outFormat = String.format("%%%ds", outLength + 1);

        for(int i = 0; i < lines; i++)
        {
            if(i < in.length)
            {
                output[i] = "[ ";
                
                for(int j = 0; j < in[i].length; j++)
                    output[i] += String.format(inFormat, in[i][j].toString()
                        + (( i == p.i && j == p.j)?"*":" "));
                
                output[i] += "]";
            }
            
            else
            {
                output[i] = "   ";
                
                for(int j = 0; j < in[0].length; j++)
                    output[i] += String.format(inFormat, " ");
            }
            
            output[i] += String.format("       %2s       ", (i == lines / 2)?"->":"  ");
            
            if(i < out.length)
            {
                output[i] += "[ ";
                
                for(Rational r : out[i])
                    output[i] += String.format(outFormat, r.toString() + " ");
                
                output[i] += "]";
            }
               
            else
            {
                output[i] += "   ";
                
                for(int j = 0; j < out[0].length; j++)
                    output[i] += String.format(outFormat, " ");
            }
        }
        
        System.out.printf("%s:\n", title);
        
        for(String o : output)
            System.out.println(o);

        System.out.println();
    }
    
    public static void printTableau(String title, Rational[][] in)
    {   printTableau(title, in, new Point(-1, -1));    }
    
    public static void printTableau(String title, Rational[][] in, Point p)
    {
        int length = 0;
        String[] output = new String[in.length];
        String format;
        
        for(Rational[] n : in)
            for(Rational r : n)
                length = Math.max(length, r.toString().length());

        format = String.format("%%%ds ", length);

        for(int i = 0; i < in.length; i++)
        {
            output[i] = "[ ";

            for(int j = 0; j < in[i].length; j++)
                output[i] += String.format(format, in[i][j].toString()
                    + (( i == p.i && j == p.j)?"*":" "));

            output[i] += "]";
        }
        
        System.out.printf("%s:\n", title);
        
        for(String o : output)
            System.out.println(o);
        
        System.out.println();
    }
}