/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package general.sound;

import java.io.IOException;
import javax.microedition.media.Manager;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;
import javax.microedition.media.control.ToneControl;

/**
 *
 * @author Haim and Yifat
 */
public class SoundUtils {

    /**
     * Play the note for duration millis.
     * Actually Manager.playTone should do it, but at least on my Sony Ericsson 530i it stops the other sequences.
     * 
     * @param noteone Note to be played. Use constants in MusicNotes.
     * @param duration Duration, in millis, of the note.
     */
    static public void playNote(byte note, final int duration) {
        // TODO calculate duration modifier
        byte durationModifier = 4;
        
        final byte seq[] = {ToneControl.VERSION, 1, ToneControl.TEMPO, 10, note, durationModifier};
        new Thread(new Runnable() {

            public void run() {
                Player player = null;
                try {
                    player = Manager.createPlayer(Manager.TONE_DEVICE_LOCATOR);
                    player.setLoopCount(1);
                    player.realize();
                    ToneControl tc = (ToneControl) player.getControl("javax.microedition.media.control.ToneControl");
                    tc.setSequence(seq);
                    player.start();  
                    Thread.sleep(duration); 
                    player.close();
                } catch (InterruptedException ex) {
                    
                } catch (IOException ex) {
                   // Ignore
                } catch (MediaException ex) {
                    // Ignore
                } finally {
                    if (player != null)
                        player.close();
                }
            };
        }).start();

    }
}
