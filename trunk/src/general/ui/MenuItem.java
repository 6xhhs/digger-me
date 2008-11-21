/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package general.ui;

import javax.microedition.lcdui.Graphics;

/**
 *
 * @author Haim Avron
 */
public interface MenuItem {
    
    /**
     * The coordinates are in the middle of where the item should be drawn
     * @param g
     * @param x
     * @param y
     */
    public void draw(Graphics g, int x, int y);
    
    /**
     * 
     * @param g
     * @return The width that will be used to draw the item, including buffers to the right and left (to make
     *  it look good).
     */
    public int getWidth(Graphics g);

}
