/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package general.sound;

/**
 *
 * @author Haim Avron
 */
public class NotePlayingException extends Throwable {
    private Throwable cause;
    
    public NotePlayingException() {
        this.cause = null;
    }
    
    public NotePlayingException(Throwable cause) {
        this.cause = cause;
    }
    
    public Throwable getCause() {
        return cause;
    }
}
