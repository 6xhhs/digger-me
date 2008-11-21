/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package general.sound;

/**
 * A @{link TunePlayer} for a sequence of notes (i.e. a byte array with pairs 
 * of note and duration in a given tempo and resultion) directly using 
 * Manager.playTone.
 * 
 * @author Haim Avron
 */
public class NoteSequenceTunePlayer implements TunePlayer {

    // Input variables
    private final int tempo;
    private final int resolution;
    private final byte[] sequence;
    
    private int priority; 
    private int volume;
    private int position;
    
    private boolean playing;
    private boolean loop;
    private boolean closed;
     
    private Thread playingThread;
    private Object lock = new Object();
    
    
    private final Runnable runner = new Runnable() {
                public void run() {
                    playingThreadRun();
                }
            };


    private void playingThreadRun() {
        try {
            while (!closed) {
                while (playing) {
                    int waitPeriod = 0; 
                    
                    synchronized (lock) {
                        // It is possible for playing to turn false 
                        // between check and lock
                        if (!playing)
                            break;

                        // Play note.
                        byte note = sequence[position];
                        byte noteDuration = sequence[position + 1];

                        int timeDuration = (int) noteDuration * 60 * 1000 / (resolution * tempo);
                        
                        if (note != MusicNotes.REST) {
                            waitPeriod = NoteSeriallizer.getInstance().playNote(note, timeDuration, volume, priority);
                            if (waitPeriod == -1)
                                waitPeriod = timeDuration;
                        } else
                            waitPeriod = timeDuration;
                        position = (position + 2) % sequence.length;
                        if (position == 0 && !loop)
                            playing = false;
                    }
                    
                    if (waitPeriod > 0)
                        Thread.sleep(waitPeriod);                  
                }

                // Normally playing should be false, 
                // but race can cause it to be true.
                synchronized (lock) {
                    if (!playing)
                        lock.wait();
                }
            }
        } catch (InterruptedException ex) {
            // Shouldn't really happen
        }
    }
               
    public NoteSequenceTunePlayer(byte[] sequence, int tempo) {
         this(sequence, tempo, 64);
    }
    
    public NoteSequenceTunePlayer(byte[] sequence, int tempo, int resolution) {
        this.tempo = tempo;
        this.resolution = resolution;
        this.sequence = sequence;
        this.volume = 50;
        this.position = 0;
        this.priority = 0;
        this.playing = false;
        this.closed = false;
        this.loop = false;
        this.playingThread = new Thread(runner);
        this.playingThread.start();
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }
    
    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void playInLoop() {
        synchronized (lock) {
            loop = true;
            if (playing == false) {
                playing = true;
                lock.notify();
            }
        }
    }

    public void playSingle() {
        synchronized (lock) {
            loop = false;
            if (playing == false) {
                playing = true;
                lock.notify();
            }
        }
    }

    public void stopPlaying() {
        synchronized (lock) {
            playing = false;
            position = 0;
        }
    }

    public void pausePlaying() {
        synchronized (lock) {
            playing = false;
        }
    }

    public void rewind() {
        synchronized (lock) {
            position = 0;
        }
    }

    public void close() {
        synchronized (lock) {
            playing = false;
            closed = true;
            if (playing == true)
                playing = false;
            else
                lock.notify();
        }
    }

    public boolean isPlaying() {
        return playing;
    }
}
