package net.coderodde.fun.console;

import java.awt.Dimension;
import javax.swing.JFrame;

/**
 * This class implements a simple colorful console application.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 (Feb 3, 2018)
 */
public final class ColorfulConsoleApp extends JFrame {
    
    public ColorfulConsoleApp() {
        super("ColorfulConsoleApp");
        ColorfulConsole colorfulConsole = new ColorfulConsole();
        getContentPane().add(colorfulConsole);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(colorfulConsole.getSize());
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);
        colorfulConsole.print("hello all mothafockas!");
    }
    
    public static void main(String[] args) {
        new ColorfulConsoleApp();
    }
}
