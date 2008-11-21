/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package general.ui;

import java.util.Hashtable;
import javax.microedition.lcdui.game.GameCanvas;

/**
 *
 * @author Haim Avron
 */
public class ExtendedGameCanvas extends GameCanvas {
    
    public static final int LSK = 900;
    public static final int RSK = 901;
    
    private final Hashtable keyCodes = new Hashtable();

    public ExtendedGameCanvas(boolean arg0) {
        super(arg0);
        
        String type = getDeviceType();
        
 
        if (type.startsWith("SonyEricsson")) {
            keyPut(-6, LSK);
            keyPut(-7, RSK);
        }
        
        if (type.startsWith("Nokia")) {
            keyPut(-6, LSK);
            keyPut(-7, RSK);
        }
        
    }

    /**
     * Returns 0 if no game action, does not throw exception.
     * @param keyCode
     * @return
     */
    public int getGameAction(int keyCode) {
        try {
            int superAction  = super.getGameAction(keyCode);
            if (superAction == 0)
                return keyGet(keyCode);
            else
                return superAction;
        } catch (IllegalArgumentException e) {
            return keyGet(keyCode);
        }
    }

    public int getKeyCode(int gameAction) {
        return super.getKeyCode(gameAction);
    }
    
    public boolean canDetect(int gameAction) {
        return keyCodes.contains(new Integer(gameAction));
    }
    
    protected String getDeviceType() {
        return System.getProperty("microedition.platform");
    }
    
    private void keyPut(int code, int action) {
        keyCodes.put(new Integer(code), new Integer(action));
    }
    
    private int keyGet(int code) {
        Object actionObject = keyCodes.get(new Integer(code));
        if (actionObject == null)
            return 0;
        return ((Integer)actionObject).intValue();
    }
    
    


}
