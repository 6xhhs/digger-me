/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package general.sound;

/**
 *
 * @author Haim Avron
 */
final public class NoteSeriallizer {
    
    private boolean haveNoteWaiting = false;
    
    // Data of last note
    private int lastNote = 0;
    private int lastDuration = 0;
    private int lastVolume = 0;
    
    // Last scheduled note priority.
    private int lastPriority = 0;
    
    private Object lock = new Object();
    
    // Default note player is based on Mobile Media API (JSR-135)
    NotePlayer notePlayer = MobileMediaAPINotePlayer.getInstance();
    
    private void consumeNotes() {
        try {
            while (true) {
                int noteToPlay;
                int durationToPlay;
                int volumeToPlay;

                synchronized (lock) {
                    if (!haveNoteWaiting)
                        lock.wait();

                    noteToPlay = lastNote;
                    durationToPlay = lastDuration;
                    volumeToPlay = lastVolume;

                    haveNoteWaiting = false;
                }
                notePlayer.playNote(noteToPlay, durationToPlay, volumeToPlay);
                if (!notePlayer.isBlocking()) {
                    Thread.sleep(lastDuration + notePlayer.assumedDelay());
                }
            }
        } catch (NotePlayingException e) {
        // Probably no media, next are ignored.
        } catch (InterruptedException e) {
        // Shouldn't really happen
        }

    }
    
    private NoteSeriallizer() {
        Thread consumerThread = new Thread(new Runnable() {

            public void run() {
                consumeNotes();
            }
        });
        consumerThread.start();
    }
    
    /////// SINGLETON  
    private static NoteSeriallizer instance = new NoteSeriallizer();
    
    public static NoteSeriallizer getInstance() {
        return instance;
    }
    
    ////// Chaing the note player
    public void setNotePlayer(NotePlayer notePlayer) {
        this.notePlayer = notePlayer;
    }
    
    ////// Scheduling
    static public final int DEFAULT_PRIORITY = 0;
    
    public int playNote(int note, int duration, int volume) {
        return playNote(note, duration, volume, DEFAULT_PRIORITY);
    }
    
    public int playNote(int note, int duration, int volume, int priority) {
        synchronized(lock) {
            // Simple case - not scheduled note
            if (!haveNoteWaiting) {
                lastNote = note;
                lastDuration = duration;
                lastVolume = volume;
                lastPriority = priority;
                haveNoteWaiting = true;
                lock.notify();
                return duration + notePlayer.assumedDelay();
            } else {
                // Have note, need to check which one too keep
                if (priority < lastPriority) 
                    return -1;
                
                // OK, we kept this one.
                lastNote = note;
                lastDuration = duration;
                lastVolume = volume;
                lastPriority = priority;   
                return duration + notePlayer.assumedDelay();
            }
        }
    }
    
    

}
