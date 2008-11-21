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
public class GaugeMenuItem implements MenuItem {

    private final GaugableResource resource;

    public GaugeMenuItem(GaugableResource resource) {
        this.resource = resource;
    }
    
    
    public void draw(Graphics g, int x, int y) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getWidth(Graphics g) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
