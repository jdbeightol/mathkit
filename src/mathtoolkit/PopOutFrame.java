package mathtoolkit;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.beans.PropertyVetoException;

import java.util.Arrays;

import javax.swing.JFrame;

public class PopOutFrame extends javax.swing.JInternalFrame
{
    private JFrame popoutWindow = null;
    private boolean popped = false;

    public PopOutFrame()
    {    super();    }
    
    public PopOutFrame(String t, boolean a, boolean b, boolean c, boolean d)
    {    super(t, a, b, c, d);    }
    
    public boolean isPopped()
    {
        return popped;
    }
    
    public void bringForward()
    {
        if(popped)
            popoutWindow.setVisible(true);
        
        else
            try
            {
                if(isIcon())
                    setIcon(false);

                moveToFront();
                setSelected(true);
            }
            
            catch(PropertyVetoException ex)
            {
                System.err.println(Arrays.toString(ex.getStackTrace()));
            }
    }
    
    public void popout()
    {
        if(this.isVisible())
        {
            if(popoutWindow == null)
                popoutWindow = new JFrame();
            
            popoutWindow.setLocation(getLocation());
            popoutWindow.setSize(getSize());
            popoutWindow.setTitle(title);
            
            popoutWindow.addWindowListener(new WindowAdapter() 
            {
                @Override
                public void windowClosing(WindowEvent e) 
                {
                    popin();
                }
            });
            
            popoutWindow.setContentPane(this.getContentPane());
            popoutWindow.setJMenuBar(getJMenuBar());
            
            setVisible(false);
            popoutWindow.setVisible(true);
            popped = true;
        }
    }
    
    private void popin()
    {
        if(popoutWindow.isVisible())
        {
            setContentPane(popoutWindow.getContentPane());
            setJMenuBar(getJMenuBar());
            popoutWindow.setVisible(false);
            setVisible(true);
            popped = false;
        }
    }
}