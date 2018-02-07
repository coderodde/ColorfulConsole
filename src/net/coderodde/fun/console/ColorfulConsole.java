package net.coderodde.fun.console;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.JPanel;

/**
 * This class implements a simple colorful console.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 (Feb 3, 2018)
 */
public final class ColorfulConsole extends JPanel {

    /**
     * The minimum width of the console in character tiles.
     */
    private static final int MINIMUM_WIDTH = 1;
    
    /**
     * The minimum height of the console in character tiles.
     */
    private static final int MINIMUM_HEIGHT = 1;
    
    /**
     * The default width of the console in characters.
     */
    private static final int DEFAULT_WIDTH = 80;
    
    /**
     * The default height of the console in characters.
     */
    private static final int DEFAULT_HEIGHT = 24;
    
    /**
     * The default foreground color.
     */
    private static final Color DEFAULT_FOREGROUND_COLOR = Color.WHITE;
    
    /**
     * The default background color.
     */
    private static final Color DEFAULT_BACKGROUND_COLOR = Color.BLACK;
    
    /**
     * The default character width in pixels.
     */
    private static final int DEFAULT_CHARACTER_WIDTH = 7;
    
    /**
     * The default character height in pixels.
     */
    private static final int DEFAULT_CHARACTER_HEIGHT = 15;
    
    /**
     * The default blink duration in milliseconds.
     */
    private static final int DEFAULT_BLINK_DURATION = 500;
    
    /**
     * The initial value of the <code>x</code>-coordinate of the tile cursor.
     */
    private static final int INITIAL_CURSOR_X = 0;
    
    /**
     * The initial value of the <code>y</code>-coordinate of the tile cursor.
     */
    private static final int INITIAL_CURSOR_Y = 0;
    
    /**
     * The width of the colorful console in characters.
     */
    private final int width;
    
    /**
     * The height of the colorful console in characters.
     */
    private final int height;
    
    /**
     * The current font.
     */
    private Font font;
    
    /**
     * The current bold font settings.
     */
    private boolean boldFontOn;
    
    /**
     * The <code>x</code>-coordinate of the cursor in character tiles.
     */
    private final AtomicInteger cursorX = new AtomicInteger(INITIAL_CURSOR_X);
    
    /**
     * The <code>y</code>-coordinate of the cursor in character tiles.
     */
    private final AtomicInteger cursorY = new AtomicInteger(INITIAL_CURSOR_Y);
    
    /**
     * Set to {@code true} whenever there is a need to recalculate the panel
     * dimensions. Set to {@code true} by default so that the very first paint
     * request sets correctly the character width and height in pixels.
     */
    private boolean recalculatePanelDimension = true;
    
    /**
     * A flag for rendering the entire console upon creation. Is then set to 
     * {@code false} and the console redraws only updated character tiles.
     */
    private boolean initializeGraphics = true;
    
    /**
     * The character tile matrix.
     */
    private final CharacterTile[][] characterTiles;
    
    /**
     * The current width of a character tile in pixels.
     */
    private int characterTileWidth = DEFAULT_CHARACTER_WIDTH;
    
    /**
     * The current height of a character tile in pixels.
     */
    private int characterTileHeight = DEFAULT_CHARACTER_HEIGHT;
    
    
    /**
     * Used for drawing text.
     */
    private final char[] characterArray = new char[1];
    
    /**
     * The blink duration in milliseconds.
     */
    private volatile int blinkDuration = DEFAULT_BLINK_DURATION;
    
    /**
     * The blinked thread.
     */
    private final CursorBlinkThread cursorBlinkThread;
    
    /**
     * This inner static class implements a single character tile.
     */
    private static final class CharacterTile {
        
        /**
         * The character in this tile.
         */
        private volatile char character;
        
        /**
         * The foreground color of this tile.
         */
        private volatile Color foregroundColor;
        
        /**
         * The background color of this tile.
         */
        private volatile Color backgroundColor;
        
        /**
         * The boldness of the character of this tile.
         */
        private volatile boolean boldFont;
        
        /**
         * If set to {@code true}, this character tile is blinking, or, in other
         * words, it paints the background with the foreground color and it
         * paints the foreground with the background color.
         */
        private volatile boolean isBlinking;
    }
    
    private final class CursorBlinkThread extends Thread {
        
        @Override
        public void run() {
            while (true) {
                // Invert the foreground and background colors:
                int x = cursorX.get();
                int y = cursorY.get();
                CharacterTile tile = characterTiles[y][x];
                tile.isBlinking = true;
                repaint();
                sleep(blinkDuration);
                
                // Now restore the actual colors:
                tile.isBlinking = false;
                repaint();
                sleep(blinkDuration);
            } 
        }
        
        private void sleep(int milliseconds) {
            try {
                Thread.sleep(milliseconds);
            } catch (InterruptedException ex) {}
        }
    }
    
    public ColorfulConsole() {
        this(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }
    
    public ColorfulConsole(int width, int height) {
        this.font = new Font(Font.MONOSPACED, Font.PLAIN, 12);
        this.width = checkWidth(width);
        this.height = checkHeight(height);
        this.characterTiles = new CharacterTile[height][width];
        this.setForeground(DEFAULT_FOREGROUND_COLOR);
        this.setBackground(DEFAULT_BACKGROUND_COLOR);
        populateCharacterTiles();
        repaint();
        setSize(width * characterTileWidth,
                height * characterTileHeight);
        this.cursorBlinkThread = new CursorBlinkThread();
        this.cursorBlinkThread.start();   
    }
    
    /**
     * Returns the width of the console in characters.
     * 
     * @return the width of the console in characters.
     */
    public int getConsoleWidth() {
        return width;
    }
    
    /**
     * Returns the height of the console in characters.
     * 
     * @return the height of the console in characters.
     */
    public int getConsoleHeight() {
        return height;
    }
    
    /**
     * Prints a single character at the current console cursor.
     * 
     * @param character the character to print.
     */
    public void print(char character) {
        int x = cursorX.get();
        int y = cursorY.get();
        characterTiles[y][x].character = character;
        characterTiles[y][x].foregroundColor = getForeground();
        characterTiles[y][x].backgroundColor = getBackground();
        characterTiles[y][x].boldFont = boldFontOn;
    }
    
    /**
     * Prints the input text starting from the current console cursor. If the 
     * cursor goes outside the console, it is reset to the leftmost tile of the
     * next row.
     * 
     * @param text the text to print.
     */
    public void print(String text) {
        for (int i = 0; i < text.length(); i++) {
            print(text.charAt(i));
        }
    }
    
    @Override
    public Dimension getSize() {
        return new Dimension(width * characterTileWidth, 
                             height * characterTileHeight);
    }
    
    @Override
    public Dimension getPreferredSize() {
        return getSize();
    }
    
    public void setBoldText(boolean bold) {
        this.boldFontOn = bold;
    }
    
    public void setFontSize(int size) {
        font = new Font(Font.MONOSPACED,
                        boldFontOn ? Font.BOLD : Font.PLAIN,
                        size);
        
        recalculatePanelDimension = true;
        repaint();
    }
    
    private void updateConsoleSize(Graphics g) {
        FontMetrics fontMetrics = g.getFontMetrics(font);
        int charHeight = fontMetrics.getAscent() + fontMetrics.getDescent();
        int charWidth = fontMetrics.charWidth('A');
        characterTileWidth = charWidth;
        characterTileHeight = charHeight;
        int panelWidth = width * charWidth;
        int panelHeight = height * charHeight;
        setSize(panelWidth, panelHeight);
    }
    
    /**
     * Returns the current console cursor position.
     * 
     * @return the current console cursor position.
     */
    public Point getConsoleCursorPosition() {
        return new Point(cursorX.get(), cursorY.get());
    }
    
    /**
     * Sets the cursor to a requested position.
     * 
     * @param x the x-coordinate of the cursor.
     * @param y the y-coordinate of the cursor.
     */
    public void setConsoleCursorPosition(int x, int y) {
        checkX(x);
        checkY(y);
        cursorX.set(x);
        cursorY.set(y);
        repaint();
    }
    
    @Override
    public void paintComponent(Graphics g) {
        if (recalculatePanelDimension) {
            recalculatePanelDimension = false;
            updateConsoleSize(g);
        }
        
        if (initializeGraphics) {
            initializeGraphics = false;
            g.clearRect(0, 
                        0, 
                        characterTileWidth * width,
                        characterTileHeight * height);
        } else {
            int x = cursorX.get();
            int y = cursorY.get();
            CharacterTile tile = characterTiles[y][x];
            Font tileFont = new Font(Font.MONOSPACED,
                                     tile.boldFont ? Font.BOLD : Font.PLAIN,
                                     font.getSize());
            
            // Draw the background:
            g.setColor(tile.isBlinking ? 
                       tile.foregroundColor :
                       tile.backgroundColor);
            g.fillRect(x * characterTileWidth,
                       y * characterTileHeight,
                       characterTileWidth,
                       characterTileHeight);
            // Draw the foreground:
            g.setColor(tile.isBlinking ? 
                       tile.backgroundColor :
                       tile.foregroundColor);
            characterArray[0] = tile.character;
            g.setFont(tileFont);
            g.drawChars(characterArray, 
                        0, 
                        1, 
                        x * characterTileWidth,
                        y * characterTileHeight + 10);
        }
    }
    
    @Override
    public void setForeground(Color foregroundColor) {
        if (foregroundColor != null) {
            super.setForeground(foregroundColor);
            repaint();
        }
    }
    
    @Override
    public void setBackground(Color backgroundColor) {
        if (backgroundColor != null) {
            super.setBackground(backgroundColor);
            repaint();
        }
    }
    
    /**
     * Populates the character tile matrix with default values.
     */
    private void populateCharacterTiles() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                CharacterTile characterTile = new CharacterTile();
                characterTile.foregroundColor = getForeground();
                characterTile.backgroundColor = getBackground();
                characterTile.character = ' ';
                // Font boldness and blinking is set to 'false' by the VM.
                characterTiles[y][x] = characterTile;
            }
        }
    }
    
    /**
     * Checks the validity of the given <code>x</code>-coordinate. Returns 
     * silently upon success.
     * 
     * @param x the <code>x</code>-coordinate to check.
     * @throws IndexOutOfBoundsException if the check failed.
     */
    private void checkX(int x) {
        if (x < 0) {
            throw new IndexOutOfBoundsException(
                    "The x-coordinate is negative: " + x + ".");
        }
        
        if (x >= width) {
            throw new IndexOutOfBoundsException(
                    "The x-coordinate is too large: " + x + ". Must be at " +
                    "most " + (width - 1) + ".");
        }
    }
    
    /**
     * Checks the validity of the given <code>y</code>-coordinate. Returns 
     * silently upon success.
     * 
     * @param y the <code>y</code>-coordinate to check.
     * @throws IndexOutOfBoundsException if the check failed.
     */
    private void checkY(int y) {
        if (y < 0) {
            throw new IndexOutOfBoundsException(
                    "The y-coordinate is negative: " + y + ".");
        }
        
        if (y >= height) {
            throw new IndexOutOfBoundsException(
                    "The y-coordinate is too large: " + y + ". Must be at " +
                    "most " + (height - 1) + ".");
        }
    }
    
    /**
     * Checks the requested width.
     * 
     * @param width the requested width.
     * @return the requested width upon success.
     * @throws IllegalArgumentException if the requested width is invalid.
     */
    private int checkWidth(int width) {
        if (width < MINIMUM_WIDTH) {
            throw new IllegalArgumentException(
                    "The console width is too small (" + width + "). Must be " +
                    "at least " + MINIMUM_WIDTH + ".");
        }
        
        return width;
    }
    
    /**
     * Checks the requested height.
     * 
     * @param height the requested height.
     * @return the requested height upon success.
     * @throws IllegalArgumentException if the requested height is invalid.
     */
    private int checkHeight(int height) {
        if (height < MINIMUM_HEIGHT) {
            throw new IllegalArgumentException(
                    "The console height is too small (" + height + "). Must " +
                    "be at least " + MINIMUM_HEIGHT + ".");
        }
        
        return height;
    }
}
