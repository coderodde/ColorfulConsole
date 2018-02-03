package net.coderodde.fun.console;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * This class implements a simple colorful console application.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 (Feb 3, 2018)
 */
public final class ColorfulConsoleApp extends JFrame {
    
    private static final String APP_TITLE = "ColorfulConsoleApp v1.6";
    private static final String BOLD_BUTTON_BOLD_OFF = "Bold";
    private static final String BOLD_BUTTON_BOLD_ON = "Plain";
    private static final int CONTROLS_PANEL_HEIGHT = 55;
    
    private final JPanel mainPanel = new JPanel();
    private final JPanel controlsPanel = new JPanel();
    private final JButton boldOnOffButton = new JButton(BOLD_BUTTON_BOLD_OFF);
    private final JButton foregroundColorButton = new JButton();
    private final JButton backgroundColorButton = new JButton();
    private final JTextField fontSizeTextField = new JTextField();
    private final ColorfulConsole console = new ColorfulConsole();
    
    public ColorfulConsoleApp() {
        super(APP_TITLE);
        
        controlsPanel.setLayout(new GridLayout(1, 4));
        controlsPanel.add(boldOnOffButton);
        controlsPanel.add(foregroundColorButton);
        controlsPanel.add(backgroundColorButton);
        controlsPanel.add(fontSizeTextField);
        
//        mainPanel.setLayout(new GridLayout(2, 1));
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(console, BorderLayout.CENTER);
        mainPanel.add(controlsPanel, BorderLayout.SOUTH);
        
        getContentPane().add(mainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        pack();
        Dimension dimension = console.getSize();
        controlsPanel.setSize(controlsPanel.getWidth(), CONTROLS_PANEL_HEIGHT);
        dimension.height += controlsPanel.getHeight();
        setSize(dimension);
        setVisible(true);
    }
    
    public static void main(String[] args) {
        new ColorfulConsoleApp();
    }
}
