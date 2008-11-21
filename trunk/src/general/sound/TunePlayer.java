/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package general.sound;

/**
 *
 * @author haima
 */
public interface TunePlayer {

    void playInLoop();

    void playSingle();

    void stopPlaying();
    
    void pausePlaying();
    
    void rewind();
    
    void setVolume(int volume);
    
    void setPriority(int priority);
    
    boolean isPlaying();
    
    void close();
}
