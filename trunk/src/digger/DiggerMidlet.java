/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package digger;

import general.ui.MenuEvent;
import general.ui.MenuItem;
import general.ui.MenuListener;
import general.ui.MenuScreen;
import general.ui.TextMenuItem;
import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;

/**
 * @author Haim Avron
 */
public class DiggerMidlet extends MIDlet implements CommandListener, MenuListener {

    static private String WARRENTY_TEXT_1 = "" +
"1. BECAUSE THE PROGRAM IS LICENSED FREE OF CHARGE, THERE IS NO WARRANTY " +
"FOR THE PROGRAM, TO THE EXTENT PERMITTED BY APPLICABLE LAW. EXCEPT WHEN " +
"OTHERWISE STATED IN WRITING THE COPYRIGHT HOLDERS AND/OR OTHER PARTIES " +
"PROVIDE THE PROGRAM 'AS IS' WITHOUT WARRANTY OF ANY KIND, EITHER EXPRESSED " +
"OR IMPLIED, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF " +
"MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE ENTIRE RISK AS " +
"TO THE QUALITY AND PERFORMANCE OF THE PROGRAM IS WITH YOU. SHOULD THE " +
"PROGRAM PROVE DEFECTIVE, YOU ASSUME THE COST OF ALL NECESSARY SERVICING, " +
"REPAIR OR CORRECTION. ";
    
    static private String WARRENTY_TEXT_2 = "" + 
"2. IN NO EVENT UNLESS REQUIRED BY APPLICABLE LAW OR AGREED TO IN WRITING " +
"WILL ANY COPYRIGHT HOLDER, OR ANY OTHER PARTY WHO MAY MODIFY AND/OR " +
"REDISTRIBUTE THE PROGRAM AS PERMITTED ABOVE, BE LIABLE TO YOU FOR DAMAGES, " +
"INCLUDING ANY GENERAL, SPECIAL, INCIDENTAL OR CONSEQUENTIAL DAMAGES ARISING " +
"OUT OF THE USE OR INABILITY TO USE THE PROGRAM (INCLUDING BUT NOT LIMITED " +
"TO LOSS OF DATA OR DATA BEING RENDERED INACCURATE OR LOSSES SUSTAINED BY " +
"YOU OR THIRD PARTIES OR A FAILURE OF THE PROGRAM TO OPERATE WITH ANY OTHER " +
"PROGRAMS), EVEN IF SUCH HOLDER OR OTHER PARTY HAS BEEN ADVISED OF THE " +
"POSSIBILITY OF SUCH DAMAGES.";

    private DiggerCanvas gameCanvas;
    private static DiggerMidlet midlet;

    public static DiggerMidlet getMidlet() {
        return midlet;
    }
    
    
    // Commands in "regular menus"
    private final Command acceptCommand = new Command("Accept", Command.ITEM, 0);
    private final Command exitCommand = new Command("Quit", Command.ITEM, 0);
    private final Command menuCommand = new Command("Menu", Command.HELP, 0);
    
    // Menu items
    private final TextMenuItem restartMenuItem = new TextMenuItem("Restart");
    private final TextMenuItem muteMenuItem = new TextMenuItem("Mute: OFF");
    private final TextMenuItem soundModeMenuItem = new TextMenuItem("Music: TONE");
    private final TextMenuItem rotateMenuItem = new TextMenuItem("Rotate: OFF");
    private final TextMenuItem activeAreaMenuItem = new TextMenuItem("Drawing Area: FULL");
    private final TextMenuItem settingsMenuItem = new TextMenuItem("Settings");
    private final TextMenuItem backMenuItem = new TextMenuItem("Back");
    private final TextMenuItem exitOptionsMenuItem = new TextMenuItem("Exit Menu");
    private final TextMenuItem exitMenuItem = new TextMenuItem("Quit");
    
    boolean restrictingActiveArea = false;

    // Menu screens
    private final MenuScreen optionsMenu = new MenuScreen(4);
    private final MenuScreen settingsMenu = new MenuScreen(5);

    public DiggerMidlet() {
        //optionsMenu.setTitle("Options");
        optionsMenu.setMenuListener(this);

        optionsMenu.setBackgroundColor(255, 255, 255);
        optionsMenu.setMenuBackgroundColor(255, 255, 255, 200);
        optionsMenu.setItemsColor(0, 0, 0);
        optionsMenu.setMenuItem(0, settingsMenuItem);
        optionsMenu.setMenuItem(1, restartMenuItem);
        optionsMenu.setMenuItem(2, exitOptionsMenuItem);
        optionsMenu.setMenuItem(3, exitMenuItem);

        //settingsMenu.setTitle("Settings");
        settingsMenu.setMenuListener(this);

        settingsMenu.setBackgroundColor(255, 255, 255);
        settingsMenu.setMenuBackgroundColor(255, 255, 255, 200);
        settingsMenu.setItemsColor(0, 0, 0);
        settingsMenu.setMenuItem(0, activeAreaMenuItem);
        settingsMenu.setMenuItem(1, rotateMenuItem);
        settingsMenu.setMenuItem(2, soundModeMenuItem);
        settingsMenu.setMenuItem(3, muteMenuItem);
        settingsMenu.setMenuItem(4, backMenuItem);
    }

    public void startApp() {
        midlet = this;
        if (ScoreStorage.scoreStorageExists()) {
            startGame();
        } else {
            Form warnForm = new Form("No-Warrenty Terms");

            Item text1 = new StringItem(null, "Please read the following no-warrenty terms carefully. You must accept them to use the game. They will be displayed only on the first run.");
            text1.setLayout(Item.LAYOUT_LEFT);
            warnForm.append(text1);
            Item text2 = new StringItem(null, WARRENTY_TEXT_1);
            text2.setLayout(Item.LAYOUT_LEFT);
            warnForm.append(text2);
            Item text3 = new StringItem(null, WARRENTY_TEXT_2);
            text3.setLayout(Item.LAYOUT_LEFT);
            warnForm.append(text3);
            warnForm.addCommand(acceptCommand);
            warnForm.addCommand(exitCommand);
            warnForm.setCommandListener(this);  
            Display.getDisplay(this).setCurrent(warnForm);
        }
    }
    
    public void startGame() {
        this.gameCanvas = new DiggerCanvas();
        if (gameCanvas.canCatchMenu()) 
            gameCanvas.setMenuPressAction(this, menuCommand);
        else {
            gameCanvas.addCommand(menuCommand);
            gameCanvas.setCommandListener(this);
        }
        new Thread(gameCanvas).start();
        Display.getDisplay(this).setCurrent(gameCanvas);
    }

    public void pauseApp() {

    }

    public void destroyApp(boolean unconditional) {
        this.gameCanvas.stop();
    }

    public void commandAction(Command cmd, Displayable dsply) {

        // Accept warrenty
        if (cmd == acceptCommand) {
            startGame();
        }
            
        if (cmd == exitCommand) {
            notifyDestroyed();
        }


        
        if (cmd == menuCommand) {
            optionsMenu.setBackgroundImage(gameCanvas.Pc.displayUpdater.getAsImage());
            settingsMenu.setBackgroundImage(gameCanvas.Pc.displayUpdater.getAsImage());
            optionsMenu.open(Display.getDisplay(this));
        }
    }

    public void onMenuEvent(MenuItem item, MenuEvent event, Displayable dsply) {
        
        if (item == exitMenuItem) {
            notifyDestroyed();
        }
            
        if (item == restartMenuItem) {
            gameCanvas.endgame();
            if (optionsMenu.isOpen()) {
                optionsMenu.close();
            }
        }

        if (item == muteMenuItem && (event == MenuEvent.LEFT_EVENT || event == MenuEvent.RIGHT_EVENT)) {
            boolean newMuteState = !gameCanvas.newSound.isMute();
            gameCanvas.newSound.setMuteState(newMuteState);
            muteMenuItem.setText("Mute: " + (newMuteState ? "ON" : "OFF"));

        }

        if (item == soundModeMenuItem && (event == MenuEvent.LEFT_EVENT || event == MenuEvent.RIGHT_EVENT)) {
            int newMusicMode = NewSound.MIDI_MUSIC_MODE;
            String modeString = "MIDI";
            switch(gameCanvas.newSound.getMusicMode()) {
            case NewSound.MIDI_MUSIC_MODE:
                newMusicMode = NewSound.TONE_MUSICE_MODE;
                modeString = "TONE";
                break;
            case NewSound.TONE_MUSICE_MODE: 
                newMusicMode = NewSound.MIDI_MUSIC_MODE;
                modeString = "MIDI";
                break;
            }
            gameCanvas.newSound.setMusicMode(newMusicMode);
            soundModeMenuItem.setText("Music: " + modeString);
        }

        if (item == rotateMenuItem && (event == MenuEvent.LEFT_EVENT || event == MenuEvent.RIGHT_EVENT)) {
            boolean newRotation = !gameCanvas.getRotation();
            gameCanvas.setRotation(newRotation);
            Image newImage = gameCanvas.Pc.displayUpdater.getAsImage();
            optionsMenu.setBackgroundImage(newImage);
            settingsMenu.setBackgroundImage(newImage);
            rotateMenuItem.setText("Rotate: " + (newRotation ? "ON" : "OFF"));
        }
        
        if (item == activeAreaMenuItem && (event == MenuEvent.LEFT_EVENT || event == MenuEvent.RIGHT_EVENT)) {
            String d;
            if (!restrictingActiveArea) {
                if (gameCanvas.getWidth() >= 220) {
                    gameCanvas.changeDrawingArea(220, 176);
                } else if (gameCanvas.getHeight() >= 220) {
                    gameCanvas.changeDrawingArea(176, 220);
                } else if (gameCanvas.getHeight() >= 176 && gameCanvas.getWidth() >= 176) {
                    gameCanvas.changeDrawingArea(176, 176);
                } else { // With lower do nothing.
                    return;
                } 
                restrictingActiveArea = true;
                d = "SMALL";
            } else {
                gameCanvas.changeDrawingArea(gameCanvas.getWidth(), gameCanvas.getHeight());
                restrictingActiveArea = false;
                d = "FULL";
            }
            
            Image newImage = gameCanvas.Pc.displayUpdater.getAsImage();
            optionsMenu.setBackgroundImage(newImage);
            settingsMenu.setBackgroundImage(newImage);
            activeAreaMenuItem.setText("Drawing Area: " + d);
        }


        if (item == settingsMenuItem) {
            settingsMenu.open(Display.getDisplay(this));
        }

        if (item == backMenuItem) {
            settingsMenu.close();
        }

        if (item == exitOptionsMenuItem) {
            optionsMenu.close();
        }
    }
}
