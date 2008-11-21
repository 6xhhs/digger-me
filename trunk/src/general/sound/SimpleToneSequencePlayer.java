/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package general.sound;

import java.io.IOException;
import javax.microedition.media.Manager;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;
import javax.microedition.media.PlayerListener;
import javax.microedition.media.control.ToneControl;

/**
 * Builds a player for on a simple sequence. The playing can be started, stoped, restarted and so on...
 * 
 * @author Haim Avron
 */
public class SimpleToneSequencePlayer {

    private final byte[] toneSequence;
    Thread playingThread = null;
    
    
    // The following object is for putting a lock on chaing playingThread
    Object playingThreadLock = new Object();
    
    
    /** This listener will be used to stop the thread in case we want a single play */
    final PlayerListener playerListener =
            new PlayerListener() {

                public void playerUpdate(Player player, String event, Object eventData) {
                    if (event.equals(END_OF_MEDIA)) {
                        synchronized (playingThreadLock) {
                            // Maybe it is already dead
                            if (playingThread != null) {
                                playingThread.interrupt();
                            }
                        }
                    }

                }
            };

    public SimpleToneSequencePlayer(byte[] toneSequence) {
        this.toneSequence = toneSequence;
    }

    private Thread createPlayingThread(final boolean loop) {
        return new Thread(new Runnable() {

                    public void run() {
                        Player player = null;
                        try {
                            player = Manager.createPlayer(Manager.TONE_DEVICE_LOCATOR);
                            if (loop) {
                                player.setLoopCount(-1);
                            } else {
                                player.setLoopCount(1);
                            }
                            player.realize();
                            ToneControl tc = (ToneControl) player.getControl("javax.microedition.media.control.ToneControl");
                            tc.setSequence(toneSequence);

                            // Register a listener to monitor the end of the media and interrupt
                            if (!loop)
                                player.addPlayerListener(playerListener);

                            // Start and wait for interrupt - from outside (stop music command) or from listener
                            player.start();
                            playingThread.join();

                        } catch (IOException e) {
                        // No player??? do nothing!

                        } catch (MediaException e) {
                        // How can this happen??? Nevermind we will close player and remove listener
                                   // but we will do it outside.

                        } catch (InterruptedException ex) {
                        // Playing was stopped (either by media end or by user).
                        }

                        synchronized (playingThreadLock) {
                            player.removePlayerListener(playerListener);
                            if (player != null) {
                                player.close();
                                player = null;
                            }
                            playingThread = null;
                        }
                    }
                });
    }

    /**
     * Plays the tone in background in a loop.
     * Can be stopped only by stopPlaying().
     * If the player is already playing this method does nothing.
     */
    public void playInLoop() {
        synchronized (playingThreadLock) {
            if (playingThread != null) {
                return;
            }

            playingThread = createPlayingThread(true);
            playingThread.start();
        }
    }

    /**
     * Plays the tone in background a single time.
     * Can be stopped only by stopPlaying().
     * If the player is already playing this method does nothing.
     */
    public void playSingle() {
        synchronized (playingThreadLock) {
            if (playingThread != null) {
                return;
            }

            playingThread = createPlayingThread(false);
            playingThread.start();
        }
    }

    /** 
     * Stop playing routine
     */
    public void stopPlaying() {
        synchronized (playingThreadLock) {
            if (playingThread == null) {
                return;
            }
            playingThread.interrupt();
        }
    }
}
