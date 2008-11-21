/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package general.display;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.GameCanvas;

/**
 * This class manages a display that is backed-up by a virtual memory background display.
 * The background display consists of an array of pixel values, each value is an index in a pallete.
 * The updater is notified of changes to this array and will update the actual display based on the
 * pixels values. <br><br>
 * 
 * This class is inteded for use when porting old-games that update the display using actual pixel values.
 * 
 * @author Haim Avron
 */
public class MemoryDisplayUpdater {    
    private final GameCanvas c;
    private final Graphics grph;
    private final int width;
    private final int height;
    
    private boolean rotated;
    private int drawingAreaWidth;
    private int drawingAreaHeight;
    private int activeWidth;
    private int activeHeight;
    private int[] pixels;
    private int[] activePixels;
    private int[] rgbData;

    // The following are kept in int * 1000 to save float operations
    private int ratioX;
    private int ratioY;
    
    // Translation map from coordinates in screenPixels to pixels. This is because double is VERY SLOW.
    private int[] translate;
    
    private final int[][] rgbMaps;
    private int[] currentRgbMap;

    /**
     * 
     * @see MemoryDisplayUpdater(GameCanvas c, Graphics grph,  boolean flushOnNewPixels, boolean rotated, int[] pixels, int width, int height, int activeWidth, int activeHeight, int numberPalletes)
     */
    public MemoryDisplayUpdater(GameCanvas c, Graphics grph, boolean rotated, int[] pixels, int width, int height, int numberPalletes) {
        this(c, grph, rotated, pixels, width, height, c.getWidth(), c.getHeight(), numberPalletes);
    }
    
    /**
     * 
     * @param c
     * @param grph
     * @param rotated
     * @param pixels
     * @param width
     * @param height
     * @param activeWidth
     * @param activeHeight
     * @param numberPalletes
     */
    public MemoryDisplayUpdater(GameCanvas c, Graphics grph, boolean rotated, int[] pixels, int width, int height, int drawingAreaWidth, int drawingAreaHeight, int numberPalletes) {        
        this.rotated = rotated;
        this.c = c;
        this.grph = grph;
        this.width = width;
        this.height = height;
        this.pixels = pixels;
        this.rgbMaps = new int[numberPalletes][];
        this.currentRgbMap = null;
        this.drawingAreaWidth = drawingAreaWidth;
        this.drawingAreaHeight = drawingAreaHeight;
        initActiveAreaArrays();
    }
    
    /**
     * Changes the active of the screen.
     * 
     * @param activeWidth - new active area width in pixels
     * @param activeHeight - new active area height in pixels
     */
    public synchronized void changeActiveArea(int drawingAreaWidth, int drawingAreaHeight) {
         synchronized (this) {
            this.drawingAreaWidth = drawingAreaWidth;
            this.drawingAreaHeight = drawingAreaHeight;
            initActiveAreaArrays();
            reduce(0, 0, drawingAreaWidth, drawingAreaHeight);
        }
        repaint();
    }

    /**
     * Changes the rotation status of the display
     * 
     * @param activeWidth - new active area width in pixels
     * @param activeHeight - new active area height in pixels
     */
    public void setRotation(boolean newRotationState) {
        synchronized (this) {
            rotated = newRotationState;
            initActiveAreaArrays();
            reduce(0, 0, activeWidth, activeHeight);
        }
        repaint();
    }

    /**
     * This function is called when a portion of the display has been invalidated.
     * Here we just repaint everything (these cases should be rerare).
     */
    public void repaint() {
        synchronized (this) {
            grph.setColor(255, 255, 255);
            grph.fillRect(0, 0, c.getWidth(), c.getHeight());
            if (currentRgbMap != null)
                draw(0, 0, activeWidth, activeHeight);
        }
        c.flushGraphics();
    }
    
    /**
     * Sets the color values of a pallete.
     * 
     * @param index - index of the pallete to change.
     * @param r - red values for pallete.
     * @param g - green values for pallete.
     * @param b - blue values for pallete.
     */
    public void setPallete(int index, int[] r, int[] g, int[] b) {
        boolean changingActivePallete;
        synchronized (this) {
            changingActivePallete = (rgbMaps[index] == currentRgbMap);

            rgbMaps[index] = new int[r.length];
            for (int i = 0; i < r.length; i++) {
                rgbMaps[index][i] = b[i] + (g[i] << 8) + (r[i] << 16);
            }
            if (currentRgbMap == null) {
                currentRgbMap = rgbMaps[index];
            }
        }
        if (changingActivePallete) 
            newPixels(true);
    }
    
    /**
     * Switches the currently active pallete.
     * 
     * @param newPalleteIndex - new pallete to be active.
     */
    public void switchPallete(int newPalleteIndex) {
        synchronized (this) {
            if (rgbMaps[newPalleteIndex] == null) {
                throw new RuntimeException("Pallete " + newPalleteIndex + " is undefined.");
            }
            currentRgbMap = rgbMaps[newPalleteIndex];
            draw(0, 0, activeWidth, activeHeight);
        }
        flushGraphics(0, 0, activeWidth, activeHeight);
    }

    /**
     * Notify the updater of new pixel values for all the display.
     * 
     * @see newPixels(int x, int y, int w, int h)
     */
    public void newPixels(boolean immediateFlush) {
        newPixels(immediateFlush, 0, 0, width, height);
    }

    /**
     * Notify the update of new pixels values for a rectangular region of the pixels.
     * 
     * @param immediateFlush - whether to flush the display immediatly
     * @param x - the x value of the top-left corner of the region.
     * @param y - the y value of the top-left corner of the region.
     * @param w - the region's width.
     * @param h - the region's height.
     */
    public void newPixels(boolean immediateFlush,int x, int y, int w, int h) {
        int activeX0, activeY0, activeH, activeW;

        synchronized (this) {
            if (currentRgbMap == null) {
                throw new RuntimeException("Need to add at least one pallete.");
            }
            // Resolution reduce
            activeX0 = (x * ratioX) / 1000;
            activeY0 = (y * ratioY) / 1000;
            activeW = Math.min(((x + w) * ratioX) / 1000 - activeX0 + 2, activeWidth - activeX0);
            activeH = Math.min(((y + h) * ratioY) / 1000 - activeY0 + 2, activeHeight - activeY0);
            reduce(activeX0, activeY0, activeW, activeH);
            draw(activeX0, activeY0, activeW, activeH);
        }
        if (immediateFlush)
            flushGraphics(activeX0, activeY0, activeW, activeH);
    }
    
    /**
     * 
     */
    public void flush() {
        flushGraphics(0, 0, activeWidth, activeHeight);
    }
    
    /**
     * Get the current display as an Image.
     * 
     * @return
     */
    public Image getAsImage() {
        if (!rotated)
            return Image.createRGBImage(rgbData, activeWidth, activeHeight, false);
        else 
            return Image.createRGBImage(rgbData, activeHeight, activeWidth, false);
        
    }
    /////////////////// Privates - used by the methods above

    private void initActiveAreaArrays() {

        if (!rotated) {
            this.activeHeight = drawingAreaHeight;
            this.activeWidth = drawingAreaWidth;
        } else {
            this.activeHeight = drawingAreaWidth;
            this.activeWidth = drawingAreaHeight;
        }

        this.activePixels = new int[activeWidth * activeHeight];
        this.ratioX = (int) (((double) activeWidth / (double) width) * 1000.0);
        this.ratioY = (int) (((double) activeHeight / (double) height) * 1000.0);
        this.translate = new int[activeWidth * activeHeight];
        for (int i = 0; i < activeHeight; i++) {
            for (int j = 0; j < activeWidth; j++) {
                int y = (i * 1000) / ratioY;
                if (i * 1000 - y * ratioY > 500) {
                    y++;
                }
                int x = (j * 1000) / ratioX;
                if (j * 1000 - x * ratioX > 500) {
                    x++;
                }
                this.translate[i * activeWidth + j] = y * width + x;
            }
        }
        this.rgbData = new int[activeWidth * activeHeight];
    }
    
    private void reduce(int activeX0, int activeY0, int activeW, int activeH) {
        for (int y = activeY0; y < activeY0 + activeH; y++) {
            for (int x = activeX0; x < activeX0 + activeW; x++) {
                int index = y * activeWidth + x;
                activePixels[index] = pixels[translate[index]];
            }
        }
    }

    
    private void draw(int activeX0, int activeY0, int activeW, int activeH) {       
        if (!rotated) {
            for (int i = 0; i < activeH; i++) {
                for (int j = 0; j < activeW; j++) {
                    rgbData[(activeY0 + i) * activeWidth + activeX0 + j] = currentRgbMap[activePixels[(activeY0 + i) * activeWidth + activeX0 + j]];
                }
            }
            grph.drawRGB(rgbData, activeY0 * activeWidth + activeX0, activeWidth, activeX0, activeY0, activeW, activeH, false);
            
        } else {
           for (int i = 0; i < activeH; i++) {
                for (int j = 0; j < activeW; j++) {
                    rgbData[(activeX0 + j) * activeHeight + activeHeight - activeY0 - i - 1] = currentRgbMap[activePixels[(activeY0 + i) * activeWidth + activeX0 + j]];
                }
            }
            grph.drawRGB(rgbData,  activeX0 * activeHeight + (activeHeight - activeY0 - activeH), activeHeight, activeHeight - activeY0 - activeH, activeX0, activeH, activeW, false);            
        }        
    }
    
    private void flushGraphics(int activeX0, int activeY0, int activeW, int activeH) {
        if (!rotated)
            c.flushGraphics(activeX0, activeY0, activeW, activeH);
        else
            c.flushGraphics(activeHeight - activeY0 - activeH, activeX0, activeH, activeW);        
    }
}
