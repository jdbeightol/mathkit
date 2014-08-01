package mathtoolkit;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

public class PopOutFrame extends javax.swing.JInternalFrame
{
    private JFrame popoutWindow = null;

    public PopOutFrame()
    {    super();    }
    
    public PopOutFrame(String t, boolean a, boolean b, boolean c, boolean d)
    {    super(t, a, b, c, d);    }
    
    public void popout()
    {
        if(this.isVisible())
        {
            if(popoutWindow == null)
                popoutWindow = new JFrame();

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
        }
    }
    
    private void popin()
    {
        if(popoutWindow.isVisible())
        {
            setContentPane(popoutWindow.getContentPane());
            this.setJMenuBar(getJMenuBar());
            setVisible(true);
        }
    }    
}