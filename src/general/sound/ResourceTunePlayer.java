/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package general.sound;

import java.io.IOException;
import java.io.InputStream;
import javax.microedition.media.Manager;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;
import javax.microedition.media.control.VolumeControl;

/**
 *
 * Notes: <br>
 * 1. On many devices only one file based tune can be used at a time. This class enforces this.
 * 2. On some devices there is a minimum wait between tune playing. This class enforces this.
 * 2. On most devices and no tones can be used while playing a file based tune. <br>
 * 2. Not thread safe. <br>
 * 3. On some devices pausePlay will not work correctly (it will work exactly like stopPlay). <br>
 * <br>
 * @author Haim Avron
 */
public class ResourceTunePlayer implements TunePlayer {
    static final private int MINIMUM_TIME_BETWEEN_PLAY = 250; // milli
    
    static private long lastTimePlayerActivated = 0;
    static private ResourceTunePlayer activePlayer = null;
    
    private final Player player;
    private int volume;
    
    private boolean nonZeroPosition = false;
    
    /**
     * Tries to find the type of a given file, based on it's name.
     * Not thread-safe.
     * 
     * @param fileName
     * @return
     */
    private static String findType(String fileName){
        String type;

        // some simple test for the content type
        if (fileName.endsWith("wav")) {
            type = "audio/x-wav";
        } else if (fileName.endsWith("jts")) {
            type = "audio/x-tone-seq";
        } else if (fileName.endsWith("mid")) {
            type = "audio/midi";
        } else {
            throw new RuntimeException("Don't know the type of " + fileName);
        }
        return type;
    }
    
    public ResourceTunePlayer(String fileName) {
        this(fileName, -1);
    }
    
    public ResourceTunePlayer(String fileName, int volume) {
        Player builtPlayer = null;
        
        try {
            InputStream is = getClass().getResourceAsStream(fileName);
            String type = findType(fileName);
            builtPlayer = Manager.createPlayer(is, type);
            builtPlayer.realize();
        } catch (MediaException e) {
            builtPlayer = null;
        } catch (IOException e) {
            builtPlayer = null;
        }
        this.player = builtPlayer;
        this.volume = volume;

    }

    public void playInLoop() {
        if (isPlaying())
            return;
        
        try {
            prepareToPlay();
            player.setLoopCount(-1);
            nonZeroPosition = true;
            player.start();
        } catch (InterruptedException ex) {
            // DO nothing
        } catch (MediaException e) {

        }
    }

    public void playSingle() {
        if (isPlaying())
            return;
        
        try {
            prepareToPlay();
            player.setLoopCount(1);
            nonZeroPosition = true;
            player.start();
        } catch (InterruptedException ex) {
            // DO nothing
        } catch (MediaException ex) {
            
        }
    }

    public void stopPlaying() {
        try {
            if (isPlaying())
                player.stop();
            rewind();
        } catch (MediaException e) {

        }
    }

    public void pausePlaying() {
        try {
            if (!isPlaying())
                return;
            player.stop();
        } catch (MediaException e) {

        }
    }

    public void rewind() {
        try {
            if (nonZeroPosition)
                player.setMediaTime(0);
            nonZeroPosition = false;
        } catch (MediaException ex) {

        }
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public void setPriority(int priority) {
        // IGNORED!!! NOT SUPPORTED!!!
    }

    public boolean isPlaying() {
        return(player.getState() == player.STARTED);
    }
    
    public void close() {
        player.close();
    }
    
    public boolean isClosed() {
        return (player.getState() == player.CLOSED);
    }

    private void activate() throws InterruptedException, MediaException {
        if (activePlayer == this)
            return;
        if (activePlayer != null && !activePlayer.isClosed()) {
            activePlayer.deactivate();

            if (System.currentTimeMillis() - lastTimePlayerActivated < MINIMUM_TIME_BETWEEN_PLAY) {
                Thread.sleep(lastTimePlayerActivated + MINIMUM_TIME_BETWEEN_PLAY - System.currentTimeMillis());
            }
        }
        activePlayer = this;
        lastTimePlayerActivated = System.currentTimeMillis();
        player.prefetch();
    }

    private void deactivate() throws MediaException {
        if (isPlaying())
            throw new RuntimeException("Cannot deactivate a playing resource tune player.");
        player.deallocate();
        player.realize();    /// Always keep them realized.
    }
    
    private void setupVolume() {
        if (volume != -1) {
            VolumeControl volumeControl = (VolumeControl) player.getControl("VolumeControl");
            volumeControl.setLevel(volume);
        }
    }
    
    private void prepareToPlay() throws InterruptedException, MediaException {
        activate();
        setupVolume();
    }
}

