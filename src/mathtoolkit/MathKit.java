package mathtoolkit;

public class MathKit
{
    public static final String VERSION = "0.99";
    
    private static boolean debug = false;

    public static void setDebugMode()
    {
        debug = true;
    }
    
    public static boolean isDebug()
    {
        return debug;
    }
}