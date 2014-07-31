package mathtoolkit.base;

public final class Rational
{
    private long num, den;
    
    public static final Rational parseFraction(String str)
    {
        return new Rational(str);
    }
    
    public static final Rational parseDouble(double n)
    {
        return new Rational(n);
    }
    
    public Rational()
    {    this(0, 1);    }
    
    public Rational(long number)
    {    this(number, 1);    }
    
    public Rational(long numerator, long denominator)
    {
        num = numerator;
        den = denominator;
        balance();
    }
    
    public Rational(Rational fraction)
    {
        num = fraction.num;
        den = fraction.den;
    }
    
    public Rational(String str)
    {
        internalParse(str);
    }
    
    public Rational(double n)
    {
        internalParse(n);
    }
    
    @Override
    public String toString()
    {
        if(den == 1 || num == 0)
            return Long.toString(num);
        
        return num + "/" + den;
    }
    
    public Rational getInverse()
    {
        return new Rational(den, num);
    }
    
    public double getValue()
    {
        return (double)num / den;
    }
    
    public Rational add(Rational fraction)
    {
        long    lcm = getLCM(den, fraction.den),
                a = num * lcm / den,
                b = fraction.num * lcm / fraction.den;
                
        return new Rational(a + b, lcm);
    }
    
    public Rational add(long number)
    {
        long    a = num,
                b = number * den;
        
        return new Rational(a + b, den);
    }
    
    public Rational subtract(Rational fraction)
    {
        return add(fraction.multiply(-1));
    }
    
    public Rational subtract(long number)
    {
        return add(-1 * number);
    }
    
    public Rational multiply(Rational fraction)
    {
        return new Rational(num * fraction.num, den * fraction.den);
    }
    
    public Rational multiply(long number)
    {
        return new Rational(num * number, den);
    }
    
    public Rational divide(Rational fraction)
    {
        return new Rational(num * fraction.den, den * fraction.num);
    }
    
    public Rational divide(long number)
    {
        return new Rational(num, number * den);
    }
    
    public boolean equals(Rational fraction)
    {
        return (num == fraction.num && den == fraction.den);
    }
    
    public boolean equals(double number)
    {
        return getValue() == number;
    }
    
    public boolean equals(long number)
    {
        return (den == 1 && num == number);
    }
    
    private void balance()
    {
        if(den == 0)
        {
            den = num;
            num = 0;
        }
        
        else
        {
            long gcd = getGCD(den, num);
            
            if(den < 0)
            {
                den *= -1;
                num *= -1;
            }
        
            if(gcd != 0)
            {
                num /= gcd;
                den /= gcd;
            }
        }
    }
    
    private void internalParse(String s) throws NumberFormatException 
    {
        int n;
        String a, b;

        n = s.indexOf("/");
        
        if(n == -1)
        {
            a = s;
            b = "1";
        }
        
        else
        {
            a = s.substring(0, n);
            b = s.substring(n + 1);
        }
        
        if(a.contains(".") && b.equals("1"))
            internalParse(Double.parseDouble(a));
        
        else if(a.contains(".") && b.contains("."))
            internalParse(Double.parseDouble(a) / Double.parseDouble(b));
        
        else
        {
            num = Long.parseLong(a.trim());
            den = Long.parseLong(b.trim());
        }
        
        balance();
    }
    
    private void internalParse(double n)
    {
        String str = Double.toString(n);
        int pow = str.length() - str.indexOf(".") - 1;
        
        num = Long.parseLong(str.replace(".", ""));
        
        if(pow <= str.length())
            den = (long)Math.pow(10, pow);
        
        else
            den = 1;
        
        balance();
    }
    
    private long getLCM(long a, long b)
    {
        long gcd = getGCD(a, b);
        
        if(gcd == 0)
            return 0;
        
        return Math.abs(a * b) / gcd;
    }
    
    private long getGCD(long a, long b)
    {
        long    big     = Math.max(Math.abs(a), Math.abs(b)),
                small   = Math.min(Math.abs(a), Math.abs(b));
        
        if(a == 0 || b == 0)
            return 0;
        
        if(big % small == 0)
            return small;
        
        else
            return getGCD(small, big % small);
    }
}