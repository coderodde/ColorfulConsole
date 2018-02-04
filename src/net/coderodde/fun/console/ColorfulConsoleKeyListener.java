package net.coderodde.fun.console;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Objects;

/**
 * Implements a keyboard listener.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 (Feb 3, 2018)
 */
public final class ColorfulConsoleKeyListener implements KeyListener {

    private final ColorfulConsole console;
    
    public ColorfulConsoleKeyListener(ColorfulConsole console) {
        this.console = 
                Objects.requireNonNull(console, "The input console is null.");
    }
    
    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        System.out.println(e);
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                Point cursor = console.getConsoleCursorPosition();
                
                if (cursor.y > 0) {
                    console.setConsoleCursorPosition(cursor.x, cursor.y - 1);
                }
                
                break;
                
            case KeyEvent.VK_DOWN:
                cursor = console.getConsoleCursorPosition();
                
                if (cursor.y < console.getConsoleHeight() - 1) {
                    console.setConsoleCursorPosition(cursor.x, cursor.y + 1);
                }
                
                break;
                
            case KeyEvent.VK_LEFT:
                cursor = console.getConsoleCursorPosition();
                
                if (cursor.x > 0) {
                    console.setConsoleCursorPosition(cursor.x - 1, cursor.y);
                }
                
                break;
                
            case KeyEvent.VK_RIGHT:
                cursor = console.getConsoleCursorPosition();
                
                if (cursor.x < console.getConsoleWidth() - 1) {
                    console.setConsoleCursorPosition(cursor.x + 1, cursor.y);
                }
                
                break;
                
            default:
                console.print(e.getKeyChar());
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}
}
