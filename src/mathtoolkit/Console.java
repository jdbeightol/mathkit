package mathtoolkit;

import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.swing.AbstractAction;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.DefaultCaret;
import mathtoolkit.tableau.Tableau;

public class Console extends JTextArea
{
    private boolean _INPUTMODE;
    
    private class ConsoleOutputStream extends OutputStream 
    {
        private final JTextArea console;
        
        public ConsoleOutputStream(JTextArea output)
        {   console = output;    }
        
        @Override
        public void write(int in) throws IOException
        {    console.append(String.valueOf((char)in));    }
    }
    
    public Console()
    {
        super();
        _INPUTMODE = false;
        
        initProperties();
        initListeners();
        initKeyBindings();
        setSystemOut();     
    }
    
    private void initProperties()
    {
        ((DefaultCaret)getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);        
        setEditable(false);
//        setColumns(20);
//        setRows(5);
        setFont(new java.awt.Font("Monospaced", 1, 12)); // NOI18N
    }
    
    private void setSystemOut()
    {
        PrintStream st = new PrintStream(new ConsoleOutputStream(this));
        System.setOut(st);
    }
    private void initListeners()
    {
        getDocument().addDocumentListener(new DocumentListener()
        {
            @Override
            public void insertUpdate(DocumentEvent e)
            {
                setCaretPosition(getText().length());
            }

            @Override
            public void removeUpdate(DocumentEvent e){}

            @Override
            public void changedUpdate(DocumentEvent e){}
        });
        
        addKeyListener(new KeyListener()
        {
            @Override
            public void keyTyped(KeyEvent e)
            {
                if(_INPUTMODE)
                    System.out.print(e.getKeyChar());
            }

            @Override
            public void keyPressed(KeyEvent e){}

            @Override
            public void keyReleased(KeyEvent e){}
        });        
    }
    
    private void initKeyBindings()
    {
        getActionMap().put(getInputMap(
                Console.WHEN_FOCUSED)
                .get(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_QUOTE, 0)),
                new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if(!_INPUTMODE)
                {
                    _INPUTMODE = true;
                    System.out.print(">");
                }
            }
        });
        
        getActionMap().put(getInputMap(
                Console.WHEN_FOCUSED)
                .get(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0)),
                new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if(_INPUTMODE)
                {
                    _INPUTMODE = false;
                    System.out.println();
                }
            }
        });

        getActionMap().put(getInputMap(
                Console.WHEN_FOCUSED)
                .get(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0)),
                new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if(_INPUTMODE)
                {
                    _INPUTMODE = false;
                    System.out.println("^C");
                }
            }
        });

        getActionMap().put(getInputMap(
                Console.WHEN_FOCUSED)
                .get(KeyStroke.getKeyStroke(KeyEvent.VK_C,
                        InputEvent.CTRL_DOWN_MASK)),
                new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if(_INPUTMODE)
                {
                    _INPUTMODE = false;
                    System.out.println("^C");
                }
            }
        });
    }
}
