package mathtoolkit.base;

public class Point
{
    public int i, j;

    public Point()
    {    i = j = 0;    }
    
    public Point(Point p)
    {   i = p.i; j = p.j;    }

    public Point(int i, int j)
    {   this.i = i; this.j = j;   }
    
    @Override
    public String toString()
    {
        return String.format("(%d, %d)", i, j);
    }
}