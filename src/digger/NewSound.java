package digger;

import general.sound.ResourceTunePlayer;
import general.sound.MusicNotes;
import general.sound.NoteSequenceTunePlayer;
import general.sound.NoteSeriallizer;
import general.sound.TunePlayer;
import general.ui.GaugableResource;

/**
 * Digger ME Sound System
 * 
 * I have tried to keep this as closely similar to the original sounds as possible, but 
 * it is impossible to make it exact.
 * 
 * @author Haim Avron
 */
public class NewSound implements GaugableResource {

    /* Music of digger */
    static private class DIGGER_SOUND_DATA extends MusicNotes {

        static final byte NORMAL_BACKGROUND[] = {
            D4, NE, C4, NE,
            D4, NE, A3, NE, F3, NE, A3, NE, D3, NQ, D4, NE, C4, NE,
            D4, NE, A3, NE, F3, NE, A3, NE, D3, NQ, D4, NE, E4, NE,
            F4, NE, E4, NE, F4, NE, D4, NE, E4, NE, D4, NE, E4, NE, C4, NE,
            D4, NE, C4, NE, D4, NE, AS3, NE, D4, NQ, D4, NE, C4, NE,
            D4, NE, A3, NE, F3, NE, A3, NE, D3, NQ, D4, NE, C4, NE,
            D4, NE, A3, NE, F3, NE, A3, NE, D3, NQ, D4, NE, E4, NE,
            F4, NE, E4, NE, F4, NE, D4, NE, E4, NE, D4, NE, E4, NE, C4, NE,
            D4, NE, C4, NE, D4, NE, E4, NE, F4, NQ, A4, NE, G4, NE,
            A4, NE, F4, NE, C4, NE, F4, NE, A3, NQ, A4, NE, G4, NE,
            A4, NE, F4, NE, C4, NE, F4, NE, A3, NQ, A4, NE, B4, NE,
            C5, NE, B4, NE, C5, NE, A4, NE, B4, NE, A4, NE, B4, NE, G4, NE,
            A4, NE, G4, NE, A4, NE, F4, NE, A4, NQ, A4, NE, G4, NE,
            A4, NE, F4, NE, C4, NE, F4, NE, A3, NQ, A4, NE, G4, NE,
            A4, NE, F4, NE, C4, NE, F4, NE, A3, NQ, A4, NE, B4, NE,
            C5, NE, B4, NE, C5, NE, A4, NE, B4, NE, A4, NE, B4, NE, G4, NE,
            A4, NE, G4, NE, A4, NE, F4, NE, A4, NQ, D5, NE, C5, NE,
            A4, NE, F4, NE, C4, NE, F4, NE, A3, NQ, A4, NE, G4, NE,
            A4, NE, F4, NE, C4, NE, F4, NE, A3, NQ, A4, NE, B4, NE,
            C5, NE, B4, NE, C5, NE, A4, NE, B4, NE, A4, NE, B4, NE, G4, NE,
            A4, NE, G4, NE, F4, NE, G4, NE, A4, NQ
        };
        static final byte BONUS_BACKGROUND[] = {
            C4, NE, C4, NE, C4, NQ, C4, NE, C4, NE, C4, NQ, C4, NE, C4, NE, F4, NQ, G4, NQ, A4, NQ,
            C4, NE, C4, NE, C4, NQ, C4, NE, C4, NE, C4, NQ, F4, NE, A4, NE, G4, NQ, E4, NQ, C4, NQ,
            C4, NE, C4, NE, C4, NQ, C4, NE, C4, NE, C4, NQ, C4, NE, C4, NE, F4, NQ, G4, NQ, A4, NQ,
            F4, NE, A4, NE, C5, 10, AS4, NE, A4, NE, G4, NE, F4, NQ, A4, NQ, F4, NQ,
            C4, NE, C4, NE, C4, NQ, C4, NE, C4, NE, C4, NQ, C4, NE, C4, NE, F4, NQ, G4, NQ, A4, NQ,
            C4, NE, C4, NE, C4, NQ, C4, NE, C4, NE, C4, NQ, F4, NE, A4, NE, G4, NQ, E4, NQ, C4, NQ,
            C4, NE, C4, NE, C4, NQ, C4, NE, C4, NE, C4, NQ, C4, NE, C4, NE, F4, NQ, G4, NQ, A4, NQ,
            F4, NE, A4, NE, C5, 10, AS4, NE, A4, NE, G4, NE, F4, NQ, A4, NQ, F4, NQ,
            A4, NE, A4, NE, A4, NQ, A4, NE, A4, NE, A4, NQ, A4, NE, A4, NE, A4, NQ,
            D5, NQ, A4, NQ, D5, NQ, A4, NQ, D5, NQ, A4, NQ, G4, NQ, F4, NQ, E4, NQ, D4, NQ,
            A4, NE, A4, NE, A4, NQ, A4, NE, A4, NE, A4, NQ, A4, NE, A4, NE, A4, NQ,
            D5, NQ, A4, NQ, D5, NQ, A4, NQ, D5, NQ, C5, NQ, B4, NQ, C5, NQ, B4, NQ, C5, NQ,
            A4, NE, A4, NE, A4, NQ, A4, NE, A4, NE, A4, NQ, A4, NE, A4, NE, A4, NQ,
            D5, NQ, A4, NQ, D5, NQ, A4, NQ, D5, NQ, A4, NQ, G4, NQ, F4, NQ, E4, NQ, D4, NQ,
            A4, NE, A4, NE, A4, NQ, A4, NE, A4, NE, A4, NQ, A4, NE, A4, NE, A4, NQ,
            D5, NQ, A4, NQ, D5, NQ, A4, NQ, D5, NQ, C5, NQ, B4, NQ, C5, NQ, B4, NQ, C5, NQ
        };
        static final byte DIGGER_DEATH[] = {
            REST, NE, C4, 6, C4, NQ, C4, NE, C4, 6, DS4, NQ, D4, NE, D4, NQ, C4, NE,
            C4, NQ, B3, NE, C4, 12
        };
        static final byte NEW_LEVEL[] = {C5, NS, C5, NS, C5, NS, C5, NS, E5, NS, E5, NS, E5, NS, E5, NS,
            G5, NS, G5, NS, G5, NS, G5, NS, D5, NS, D5, NS, D5, NS, D5, NS,
            F5, NS, F5, NS, F5, NS, F5, NS, A5, NS, A5, NS, A5, NS, A5, NS,
            E5, NS, E5, NS, E5, NS, E5, NS, G5, NS, G5, NS, G5, NS, G5, NS,
            B5, NS, B5, NS, B5, NS, B5, NS, C5, NS, C5, NS, C5, NS, C5, NS
        };
        static final String NORMAL_MIDI_FILE = "/audiofiles/popcorn.mid";
        static final String BONUS_MIDI_FILE = "/audiofiles/williamtell.mid";
        static final String DEATH_MIDI_FILE = "/audiofiles/funeral.mid";
        static final String NEW_LEVEL_MIDI_FILE = "/audiofiles/level.mid";
        static final String BONUS_PULSE_MIDI_FILE = "/audiofiles/bonus_pulse.mid";
        static final int TEMPO_NORMAL_BACKGROUND = 11;
        static final int TEMPO_BONUS_BACKGROUND = 30;
        static final int TEMPO_DIGGER_DEATH = 8;
        static final int TEMPO_NEW_LEVEL = 25;
        static final byte EMERALD_EAT_NOTES[] = {D5, E5, F5, G5, A5, B5, C6, C5};
        static final int EMERALD_NOTE_LENGTH = 100;
        static final byte BAG_BREAK_NOTE = E2;
        static final int BAG_BREAK_NOTE_LENGTH = 70;
        static final int MONEY_MIDI_LOW = D4;
        static final int MONEY_MIDI_HIGH = MONEY_MIDI_LOW + 30;
        static final int MONEY_JUMP = 3;
        static final byte MONEY_NOTE_LENGTH = MusicNotes.NS;
        static final int MONEY_TEMPO = 60;
        static final byte[] EAT_MONSTER_TUNE = {D5, NS, D6, NS, D7, NS, D5, NS, D6, NS, D7, NS, D5, NS, D6, NS, D7, NS};
        static final int EAT_MONSTER_TEMPO = 60;
        static final byte[] EXPLODE_TUNE = {G5, NS, GS5, NS, A5, NS, AS5, NS, B5, NS, C6, NS, CS6, NS, D6, NS, DS6, NS};
        static final int EXPLODE_TEMPO = 60;
        static final byte BONUS_NOTE_A = B5;
        static final byte BONUS_NOTE_B = G5;
        static final int BONUS_NOTE_LENGTH = 60;
        static final byte FALL_START_NOTE = D6;
        static final int FALL_NOTE_LENGTH = 30;
        static final int FALL_MAX_NOTE = FALL_START_NOTE + 7;
        static final byte FIRE_START_NOTE = D7;
        static final int FIRE_NOTE_LENGTH = 30;
        static final int FIRE_MIN_NOTE = DS1;
        // Priority system relevent for overlaying sound and switchovers
        static final int DEATH_PRIORITY = 10;    /* We want it to sound as quickly as possible */

        static final int NEW_LEVEL_PRIORITY = 9;
        static final int NORMAL_BACKGROUND_PRIORITY = 0;
        static final int BONUS_BACKGROUND_PRIORITY = 1; /* To make background switch more quickly */

        static final int LOW_PRIORITY_EFFECTS = 2;
        static final int EFFECTS_PRIORITY = 3;
    }
    private boolean muteFlag = false;
    public static final int NO_MUSIC_MODE = 0;
    public static final int MIDI_MUSIC_MODE = 1;
    public static final int TONE_MUSICE_MODE = 2;
    private int musicMode = NO_MUSIC_MODE;
    private TunePlayer normalBackgroundPlayer = null;
    private TunePlayer bonusBackgroundPlayer = null;
    private TunePlayer deathPlayer = null;
    private TunePlayer newLevelPlayer = null;
    private TunePlayer bonusPulseMidiPlayer = null;
    private final TunePlayer eatMonsterPlayer =
            new NoteSequenceTunePlayer(DIGGER_SOUND_DATA.EAT_MONSTER_TUNE,
            DIGGER_SOUND_DATA.EAT_MONSTER_TEMPO);
    private final TunePlayer explodePlayer =
            new NoteSequenceTunePlayer(DIGGER_SOUND_DATA.EXPLODE_TUNE,
            DIGGER_SOUND_DATA.EXPLODE_TEMPO);
    private final TunePlayer moneyEatPlayer;
    private final NoteSeriallizer noteSerializer = NoteSeriallizer.getInstance();
    private int emeraldEatingCount = 0;
    private int volume = 100;

    public NewSound() {
        setMusicMode(MIDI_MUSIC_MODE);

        // Background is of lowest priority
        normalBackgroundPlayer.setPriority(DIGGER_SOUND_DATA.NORMAL_BACKGROUND_PRIORITY);
        bonusBackgroundPlayer.setPriority(DIGGER_SOUND_DATA.BONUS_BACKGROUND_PRIORITY);

        /// Sounding a new level is imporatant
        newLevelPlayer.setPriority(DIGGER_SOUND_DATA.NEW_LEVEL_PRIORITY);

        // Death is of highest priority 
        deathPlayer.setPriority(DIGGER_SOUND_DATA.DEATH_PRIORITY);

        // Create money player
        byte[] moneySeq = new byte[(DIGGER_SOUND_DATA.MONEY_MIDI_HIGH - DIGGER_SOUND_DATA.MONEY_MIDI_LOW) * 2 / DIGGER_SOUND_DATA.MONEY_JUMP];
        byte low = DIGGER_SOUND_DATA.MONEY_MIDI_LOW;
        byte high = DIGGER_SOUND_DATA.MONEY_MIDI_HIGH;
        int pos = 0;
        boolean toggle = true;
        while (low < high) {
            moneySeq[pos] = toggle ? low : high;
            moneySeq[pos + 1] = DIGGER_SOUND_DATA.MONEY_NOTE_LENGTH;
            pos += 2;
            if (toggle) {
                low += DIGGER_SOUND_DATA.MONEY_JUMP;
            } else {
                high -= DIGGER_SOUND_DATA.MONEY_JUMP;
            }
            toggle = !toggle;
        }
        moneyEatPlayer = new NoteSequenceTunePlayer(moneySeq, DIGGER_SOUND_DATA.MONEY_TEMPO);
        moneyEatPlayer.setPriority(DIGGER_SOUND_DATA.EFFECTS_PRIORITY);

        eatMonsterPlayer.setPriority(DIGGER_SOUND_DATA.EFFECTS_PRIORITY);
        explodePlayer.setPriority(DIGGER_SOUND_DATA.EFFECTS_PRIORITY);

        setVolume(volume);
    }

    public void killAll() {
        normalBackgroundPlayer.stopPlaying();
        bonusBackgroundPlayer.stopPlaying();
        deathPlayer.stopPlaying();
        fallEnd();
        fireEnd();
        newLevelPlayer.stopPlaying();
        endBonusPulse();
    }

    // All the dealing with pause here is a bit quick-and-dirty. 
    private boolean pauseState = false;
    private boolean deathAtPause = false;

    public synchronized void changePauseState(boolean newPauseState) {
        if (newPauseState && !pauseState) {
            deathAtPause = deathPlayer.isPlaying();
            normalBackgroundPlayer.pausePlaying();
            bonusBackgroundPlayer.pausePlaying();
            deathPlayer.pausePlaying();
            fallEnd();
            fireEnd();
            newLevelPlayer.stopPlaying();
            endBonusPulse();
            pauseState = true;
        }

        if (!newPauseState && pauseState) {
            if (normalMusicMode) {
                startNormalBackgroundMusic();
            }
            if (bonusMusicMode) {
                startBonusBackgroundMusic();
            }
            if (deathAtPause) {
                playDeath();
            }
            pauseState = false;

        }

    }
    private boolean normalMusicMode = false;

    public void startNormalBackgroundMusic() {
        normalMusicMode = true;
        if (!muteFlag) {
            normalBackgroundPlayer.playInLoop();
        }
    }

    public void stopNormalBackgroundMusic() {
        normalMusicMode = false;
        if (!muteFlag) {
            normalBackgroundPlayer.stopPlaying();
        }
    }
    private boolean bonusMusicMode = false;

    public void startBonusBackgroundMusic() {
        bonusMusicMode = true;
        if (!muteFlag) {
            bonusBackgroundPlayer.playInLoop();
        }
    }

    public void stopBonusBackgroundMusic() {
        bonusMusicMode = false;
        if (!muteFlag) {
            bonusBackgroundPlayer.stopPlaying();
        }
    }

    public void playDeath() {
        if (!muteFlag) {
            deathPlayer.playSingle();
        }
    }

    /**
     * Play emerald eating tune.
     * @param b
     */
    void playEatEmerald(boolean resetCount) {
        if (muteFlag || musicMode != TONE_MUSICE_MODE) {
            return;
        }
        byte note;
        if (resetCount) {
            emeraldEatingCount = 1;
            note = DIGGER_SOUND_DATA.EMERALD_EAT_NOTES[0];
        } else {
            note = DIGGER_SOUND_DATA.EMERALD_EAT_NOTES[emeraldEatingCount];
            emeraldEatingCount++;
            if (emeraldEatingCount == DIGGER_SOUND_DATA.EMERALD_EAT_NOTES.length) {
                emeraldEatingCount = 0;
            }
        }

        noteSerializer.playNote(note, DIGGER_SOUND_DATA.EMERALD_NOTE_LENGTH, volume, 2);
    }

    public void playLooseLevel(int wt) {
        if (muteFlag || musicMode != TONE_MUSICE_MODE) {
            return;
        // Dont't ask me why the notes are set according to the switch.
        // The original code is aweful and this quick-and-dirty patch will do it.
        }
        switch (wt) {
            case 15:
                noteSerializer.playNote(MusicNotes.D4, 30, volume, DIGGER_SOUND_DATA.LOW_PRIORITY_EFFECTS); // D4

                break;
            case 11:
                noteSerializer.playNote(MusicNotes.AS3, 30, volume, DIGGER_SOUND_DATA.LOW_PRIORITY_EFFECTS);  // AS2

                break;
            case 7:
                noteSerializer.playNote(MusicNotes.G3, 30, volume, DIGGER_SOUND_DATA.LOW_PRIORITY_EFFECTS); // G2

                break;

            case 3:
                noteSerializer.playNote(MusicNotes.AS3, 30, volume, DIGGER_SOUND_DATA.LOW_PRIORITY_EFFECTS); // AS2 

                break;
        }
    }

    public void playBagBreak() {
        if (!muteFlag && musicMode == TONE_MUSICE_MODE) {
            noteSerializer.playNote(DIGGER_SOUND_DATA.BAG_BREAK_NOTE, DIGGER_SOUND_DATA.BAG_BREAK_NOTE_LENGTH, volume, 2);
        }
    }

    public void playMoneyEat() {
        if (!muteFlag && musicMode == TONE_MUSICE_MODE) {
            moneyEatPlayer.playSingle();
        }
    }

    public void playMonsterEat() {
        if (!muteFlag && musicMode == TONE_MUSICE_MODE) {
            eatMonsterPlayer.playSingle();
        }
    }
    private boolean falling = false;

    public synchronized void fallStart() {
        if (muteFlag || musicMode != TONE_MUSICE_MODE) {
            return;
        }
        if (!falling) {
            falling = true;
            Thread fallingSoundThread = new Thread() {

                private int nextNote = DIGGER_SOUND_DATA.FALL_START_NOTE;
                private boolean toggle = false;

                public void run() {
                    try {
                        while (falling) {
                            int waitPeriod = noteSerializer.playNote(nextNote, DIGGER_SOUND_DATA.FALL_NOTE_LENGTH, volume, DIGGER_SOUND_DATA.LOW_PRIORITY_EFFECTS);
                            if (waitPeriod == -1) {
                                waitPeriod = DIGGER_SOUND_DATA.FALL_NOTE_LENGTH;
                            }
                            if (toggle) {
                                nextNote = Math.min(nextNote + 1, DIGGER_SOUND_DATA.FALL_MAX_NOTE);
                            }
                            toggle = !toggle;
                            Thread.sleep(waitPeriod);
                        }
                    // EXIT!!!
                    } catch (InterruptedException ex) {
                        // Just exit
                    }
                }
            };
            fallingSoundThread.start();
        }

    }

    public synchronized void fallEnd() {
        falling = false;
    }
    private boolean firing = false;

    public synchronized void fireStart() {
        if (muteFlag || musicMode != TONE_MUSICE_MODE) {
            return;
        }
        if (!firing) {
            firing = true;
            Thread firingSoundThread = new Thread() {

                private int nextNote = DIGGER_SOUND_DATA.FIRE_START_NOTE;
                private boolean toggle = false;

                public void run() {
                    try {
                        while (firing) {
                            int waitPeriod = noteSerializer.playNote(nextNote + 1 - nextNote % 3, DIGGER_SOUND_DATA.FIRE_NOTE_LENGTH, volume, DIGGER_SOUND_DATA.LOW_PRIORITY_EFFECTS);
                            if (waitPeriod == -1) {
                                waitPeriod = DIGGER_SOUND_DATA.FIRE_NOTE_LENGTH;
                            }
                            if (toggle) {
                                nextNote = nextNote - 1;
                            }
                            if (nextNote < DIGGER_SOUND_DATA.FIRE_MIN_NOTE) {
                                break;
                            }
                            toggle = !toggle;
                            Thread.sleep(waitPeriod);
                        }
                    // EXIT!!!
                    } catch (InterruptedException ex) {
                        // Just exit
                    }
                }
            };
            firingSoundThread.start();
        }

    }

    public synchronized void fireEnd() {
        firing = false;
    }

    public void playExplode() {
        if (!muteFlag && musicMode == TONE_MUSICE_MODE) {
            explodePlayer.playSingle();
        }
    }

    public void playNewLevel() {
        if (muteFlag) {
            return;
        }
        try {
            newLevelPlayer.playSingle();

            // Quick-and-dirty wait till playing ends. I am too lazy to put listeners on TunePlayer :0
            while (newLevelPlayer.isPlaying()) {
                Thread.sleep(100);
            }
        } catch (InterruptedException e) {
        }
    }
    private boolean bonusPulsePlaying = false;
    private Thread bonusPulsePlayingThread = null;

    public void startBonusPulse() {
        if (muteFlag) {
            return;
        }

        if (musicMode == MIDI_MUSIC_MODE) {
            bonusPulseMidiPlayer.playInLoop();
        }
        if (musicMode == TONE_MUSICE_MODE) {
            if (bonusPulsePlayingThread == null) {
                bonusPulsePlaying = true;
                bonusPulsePlayingThread = new Thread() {

                    public void run() {
                        try {
                            while (true) {
                                boolean toggle = true;


                                while (bonusPulsePlaying) {
                                    int waitPeriod;
                                    if (toggle) {
                                        waitPeriod = noteSerializer.playNote(DIGGER_SOUND_DATA.BONUS_NOTE_A, DIGGER_SOUND_DATA.BONUS_NOTE_LENGTH, volume, DIGGER_SOUND_DATA.EFFECTS_PRIORITY);
                                    } else {
                                        waitPeriod = noteSerializer.playNote(DIGGER_SOUND_DATA.BONUS_NOTE_B, DIGGER_SOUND_DATA.BONUS_NOTE_LENGTH, volume, DIGGER_SOUND_DATA.EFFECTS_PRIORITY);
                                    }
                                    if (waitPeriod == -1) {
                                        waitPeriod = DIGGER_SOUND_DATA.BONUS_NOTE_LENGTH;
                                    }
                                    Thread.sleep(waitPeriod);
                                }

                                // Assumed thread safety due to infrequent changes to bonusPulsePlaying
                                synchronized (bonusPulsePlayingThread) {
                                    bonusPulsePlayingThread.wait();
                                }
                            }
                        } catch (InterruptedException e) {
                            // JUST EXIT.
                        }
                    }
                };

                bonusPulsePlayingThread.start();
            } else if (bonusPulsePlaying == false) {
                bonusPulsePlaying = true;
                synchronized (bonusPulsePlayingThread) {
                    bonusPulsePlayingThread.notify();
                }
            }
        }
    }

    public void endBonusPulse() {
        bonusPulsePlaying = false;
        if (musicMode == MIDI_MUSIC_MODE) {
            bonusPulseMidiPlayer.stopPlaying();
        }
    }

    public void setMuteState(boolean mute) {
        if (mute == true) {
            killAll();
            muteFlag = true;
        } else {
            muteFlag = false;
        }
    }

    public boolean isMute() {
        return muteFlag;
    }

    public void setMusicMode(int newMusicMode) {
        if (musicMode == newMusicMode) {
            return;
        }

        if (musicMode != NO_MUSIC_MODE) {
            killAll();
            normalBackgroundPlayer.close();
            bonusBackgroundPlayer.close();
            deathPlayer.close();
            newLevelPlayer.close();
            bonusPulseMidiPlayer.close();
        }

        switch (newMusicMode) {
            case TONE_MUSICE_MODE:
                normalBackgroundPlayer =
                        new NoteSequenceTunePlayer(DIGGER_SOUND_DATA.NORMAL_BACKGROUND,
                        DIGGER_SOUND_DATA.TEMPO_NORMAL_BACKGROUND);
                bonusBackgroundPlayer =
                        new NoteSequenceTunePlayer(DIGGER_SOUND_DATA.BONUS_BACKGROUND,
                        DIGGER_SOUND_DATA.TEMPO_BONUS_BACKGROUND);
                deathPlayer =
                        new NoteSequenceTunePlayer(DIGGER_SOUND_DATA.DIGGER_DEATH,
                        DIGGER_SOUND_DATA.TEMPO_DIGGER_DEATH);
                newLevelPlayer =
                        new NoteSequenceTunePlayer(DIGGER_SOUND_DATA.NEW_LEVEL,
                        DIGGER_SOUND_DATA.TEMPO_NEW_LEVEL);
                bonusPulseMidiPlayer = null;
                break;

            case MIDI_MUSIC_MODE:
                normalBackgroundPlayer = new ResourceTunePlayer(DIGGER_SOUND_DATA.NORMAL_MIDI_FILE);
                bonusBackgroundPlayer = new ResourceTunePlayer(DIGGER_SOUND_DATA.BONUS_MIDI_FILE);
                deathPlayer = new ResourceTunePlayer(DIGGER_SOUND_DATA.DEATH_MIDI_FILE);
                newLevelPlayer = new ResourceTunePlayer(DIGGER_SOUND_DATA.NEW_LEVEL_MIDI_FILE);
                bonusPulseMidiPlayer = new ResourceTunePlayer(DIGGER_SOUND_DATA.BONUS_PULSE_MIDI_FILE);
                bonusPulseMidiPlayer.setVolume(volume);
                break;
        }

        normalBackgroundPlayer.setVolume(volume);
        bonusBackgroundPlayer.setVolume(volume);
        deathPlayer.setVolume(volume);
        newLevelPlayer.setVolume(volume);
        musicMode = newMusicMode;
    }

    public int getMusicMode() {
        return musicMode;
    }

    public void setVolume(int volume) {
        this.volume = volume;

        normalBackgroundPlayer.setVolume(volume);
        bonusBackgroundPlayer.setVolume(volume);
        newLevelPlayer.setVolume(volume);
        deathPlayer.setVolume(volume);
        moneyEatPlayer.setVolume(volume);
        eatMonsterPlayer.setVolume(volume);
        explodePlayer.setVolume(volume);

    }

    public int getGaugeValue() {
        return volume;
    }

    public void increaseVolume() {
        setVolume((volume + 10) % 100);
    }

    public void decreaseVolume() {
        setVolume(Math.max(volume, volume - 10));
    }
}
