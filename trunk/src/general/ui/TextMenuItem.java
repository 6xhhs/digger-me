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
public class TextMenuItem implements MenuItem {

    private String text;

    public TextMenuItem() {
        text = "";
    }
    
    public TextMenuItem(String text) {
        this.text = text;
    }
    
    public void setText(String text) {
        this.text = text;
    }
    
    public void draw(Graphics g, int x, int y) {
        g.drawString(text, x, y, Graphics.HCENTER | Graphics.TOP);
    }

    public int getWidth(Graphics g) {
        return g.getFont().stringWidth(" " + text + " ");
    }

}
