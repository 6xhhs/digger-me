/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package general.sound;

import javax.microedition.media.Manager;
import javax.microedition.media.MediaException;

/**
 * A note player that uses the capabilities of the Mobile Media API (JSR-135).
 * The main drawback of this note player is that it is non-blocking.
 * @author Haim Avron
 */
public final class MobileMediaAPINotePlayer implements NotePlayer {

    private final int ASSUMED_DELAY = 10;
    
    private MobileMediaAPINotePlayer() {
        
    }
    
    private static MobileMediaAPINotePlayer instance = new MobileMediaAPINotePlayer();
    public static MobileMediaAPINotePlayer getInstance() {
      return instance;
    }
    
    public boolean isBlocking() {
        return false;
    }

    public int assumedDelay() {
        return ASSUMED_DELAY;
    }

    public void playNote(int note, int duration, int volume) throws NotePlayingException {
        try {
            Manager.playTone(note, duration, volume);
        } catch (MediaException ex) {
            throw new NotePlayingException(ex);
        }
    }

}
