/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package digger;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordStore;

/**
 *
 * @author haima
 */
public class ScoreStorage {

    private static String FILE_NAME = "digger.scores";

    private static byte[] scoresAsBytes(String names[], long scores[]) throws IOException {
        // names have 3 chars and there are 11 (each 2 bytes)
        // scores are 8 byte and there are 12
        int size = 6 * 11 + 8 * 12;
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream(size);
        DataOutputStream out = new DataOutputStream(byteStream);
        for (int i = 0; i < 11; i++) {
            out.writeChar(names[i].charAt(0));
            out.writeChar(names[i].charAt(1));
            out.writeChar(names[i].charAt(2));
        }
        for (int i = 0; i < 12; i++) {
            out.writeLong(scores[i]);
        }

        return byteStream.toByteArray();
    }

    /**
     * Reads the scores from storage, returnning true if the exist and false if
     * not.
     * 
     * @param names
     * @param scores
     * @return
     */
    public static boolean readFromStorage(String names[], long scores[]) {
        try {
            RecordStore store = RecordStore.openRecordStore(FILE_NAME, false);
            RecordEnumeration en = store.enumerateRecords(null, null, false);
            byte[] data = store.getRecord(en.nextRecordId());
            ByteArrayInputStream byteStream = new ByteArrayInputStream(data);
            DataInputStream in = new DataInputStream(byteStream);
            for (int i = 0; i < 11; i++) {
                char name[] = {'.', '.', '.'};
                name[0] = in.readChar();
                name[1] = in.readChar();
                name[2] = in.readChar();
                names[i] = new String(name);
            }
            for (int i = 0; i < 12; i++) {
                scores[i] = in.readLong();
            }
            store.closeRecordStore();
        } catch (Exception ex) {
        // Can't write then don't write...
        }
        return false;
    }

    /**
     * Writes updated results, overwriting old ones.
     * 
     * @param names
     * @param scores
     */
    public static void writeToStorage(String names[], long scores[]) {
        try {
            RecordStore store = RecordStore.openRecordStore(FILE_NAME, false);

            // Delete old
            RecordEnumeration en = store.enumerateRecords(null, null, false);
            store.deleteRecord(en.nextRecordId());

            // Write new
            byte[] data = scoresAsBytes(names, scores);
            store.addRecord(data, 0, data.length);
            store.closeRecordStore();
        } catch (Exception ex) {
        // Can't write then don't write...
        }
    }

    /**
     * Creates empty result set on device, deleting old one if it exists.
     */
    public static void createInStorage(String names[], long scores[]) {
        try {
            RecordStore store = RecordStore.openRecordStore(FILE_NAME, true);
            byte[] data = scoresAsBytes(names, scores);
            store.addRecord(data, 0, data.length);
            store.closeRecordStore();
        } catch (Exception ex) {
        // Can't write then don't write...
        }
    }
    
    public static boolean scoreStorageExists() {
        try {
            RecordStore store = RecordStore.openRecordStore(FILE_NAME, false);
            store.closeRecordStore();
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}
