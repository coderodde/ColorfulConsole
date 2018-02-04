package net.coderodde.fun.console;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JColorChooser;
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
    private static final String FOREGROUND_COLOR_BUTTON_TEXT = "Foreground";
    private static final String BACKGROUND_COLOR_BUTTON_TEXT = "Background";
    private static final String CHOOSE_FOREGROUND_COLOR =
            "Choose the foreground color";
    private static final String CHOOSE_BACKGROUND_COLOR = 
            "Choose the background color";
    
    private final JPanel mainPanel = new JPanel();
    private final JPanel controlsPanel = new JPanel();
    private final JTextField fontSizeTextField = new JTextField();
    private final ColorfulConsole console = new ColorfulConsole();
    private final JButton boldOnOffButton = new JButton(BOLD_BUTTON_BOLD_OFF);
    private final JButton foregroundButton = 
            new JButton(FOREGROUND_COLOR_BUTTON_TEXT);
    private final JButton backgroundButton = 
            new JButton(BACKGROUND_COLOR_BUTTON_TEXT);
    
    public ColorfulConsoleApp() {
        super(APP_TITLE);
        
        controlsPanel.setLayout(new GridLayout(1, 4));
        controlsPanel.add(boldOnOffButton);
        controlsPanel.add(foregroundButton);
        controlsPanel.add(backgroundButton);
        controlsPanel.add(fontSizeTextField);
        
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(console, BorderLayout.CENTER);
        mainPanel.add(controlsPanel, BorderLayout.SOUTH);
        
        boldOnOffButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (boldOnOffButton.getText().equals(BOLD_BUTTON_BOLD_OFF)) {
                    console.setBoldText(false);
                    boldOnOffButton.setText(BOLD_BUTTON_BOLD_ON);
                } else {
                    console.setBoldText(true);
                    boldOnOffButton.setText(BOLD_BUTTON_BOLD_OFF );
                }
            }
        });
        
        foregroundButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Color newForegroundColor =
                        JColorChooser.showDialog(
                                foregroundButton, 
                                CHOOSE_FOREGROUND_COLOR, 
                                console.getForeground());
                
                if (newForegroundColor != null) {
                    console.setForeground(newForegroundColor);
                }
            }
        });
        
        backgroundButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Color newBackgroundColor =
                        JColorChooser.showDialog(
                                backgroundButton, 
                                CHOOSE_BACKGROUND_COLOR, 
                                console.getBackground());
                
                if (newBackgroundColor != null) {
                    console.setBackground(newBackgroundColor);
                }
            }
        });
        
        mainPanel.setFocusable(true);
        mainPanel.addKeyListener(new ColorfulConsoleKeyListener(console));
        mainPanel.requestFocus();
        
        getContentPane().add(mainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        pack();
        setResizable(false);
        setVisible(true);
        
        
    }
    
    public static void main(String[] args) {
        new ColorfulConsoleApp();
    }
}
