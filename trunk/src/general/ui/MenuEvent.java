/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package general.ui;

/**
 *
 * @author Haim and Yifat
 */
public final class MenuEvent {
    private MenuEvent() {
        
    }
    
    // JavaME - no level 5 source
    public static MenuEvent SELECT_EVENT = new MenuEvent();
    public static MenuEvent RIGHT_EVENT = new MenuEvent();
    public static MenuEvent LEFT_EVENT = new MenuEvent();
}
