/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package general.sound;

import javax.microedition.media.MediaException;

/**
 * Interface for a generic player. This interface abstract various accesses to note playing APIs and provides 
 * information on them (whether they are blocking, what delay to assume and so forth...).
 * 
 * @author Haim Avron
 */
public interface NotePlayer {

    public boolean isBlocking();
    
    /**
     * If the player is non-blocking there is a delay between sending a play request and
     * actually playing. Waiting this period before scheduling the next tune is important
     * to avoid buffer overfill, but it will ruin tempo a bit...
     * This method returns the number of millis to assume the delay will be.
     */
    public int assumedDelay();
    
    public void playNote(int note, int duration, int volume) throws NotePlayingException, InterruptedException;
    
}
