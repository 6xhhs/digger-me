/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package general.ui;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

/**
 * The type of the command and the priority is ignored. I use the "Command" class so that the same object and 
 * listener can be used with this class and with JavaME library classes.
 * 
 * @author Haim Avron
 */
public class MenuScreen {
    private String title;
    
    private final MenuScreenCanvas canvas;
    
    private Displayable oldDisplayable;
    private Display display;
    private boolean isOpen;
        
    // Commands
    private final int itemsNumber;
    private final MenuItem[] items;
    private MenuListener menuListener;

    // Colors
    private int backgroundColor;
    private int menuBackgroundColor;
    private int menuBackgroundOpacity;
    private int itemsColor;
    private int selectionColor;
    private int titleColor;
    
    private Image backgroundImage = null;

    
    /**
     * 
     */
    private class MenuScreenCanvas extends Canvas {
        private int currentSelection;

        public MenuScreenCanvas() {
            setFullScreenMode(true);
            currentSelection = 0;
        }
        
        protected void paint(Graphics graphics) {
            // Draw background
            graphics.setColor(backgroundColor);
            graphics.fillRect(0, 0, getWidth(), getHeight());
            if (backgroundImage != null)
                graphics.drawImage(backgroundImage, 0, 0, 0);
            
           
            // Calculate the box area
            Font font = graphics.getFont();
            int boxWidth = Math.max(title != null ? font.stringWidth(title): 0 , getWidth() * 2 / 3);
            for(int i = 0; i < itemsNumber; i++) {
                if (items[i] != null)
                    boxWidth = Math.max(boxWidth, items[i].getWidth(graphics));
            }
            int lineHeight = font.getHeight();
            int boxHeight = lineHeight * itemsNumber + /* margin */ + 4 * lineHeight / 10;
            if (title != null)
                boxHeight += lineHeight;
            
            if (boxWidth > getWidth())
                throw new RuntimeException("Text in menu too long!");
            if (boxHeight > getHeight())
                throw new RuntimeException("Too many menu options to fit in screen!");
          
            // Draw menu box
            int[] rgbData = new int[boxWidth * boxHeight];
            for (int i = 0; i < boxWidth * boxHeight; i++)
                rgbData[i] += menuBackgroundColor + (menuBackgroundOpacity << 24);
            Image boxmenuImage = Image.createRGBImage(rgbData, boxWidth, boxHeight, true);
            graphics.drawImage(boxmenuImage, getWidth() / 2, (getHeight() - boxHeight) / 2, Graphics.HCENTER | Graphics.TOP);
                   
            // Put actual menu items
            int heightOffset =  (getHeight() - boxHeight) / 2 + 2 * lineHeight / 10;
            if (title != null) {
                graphics.setColor(titleColor);
                graphics.drawString(title, getWidth() / 2, heightOffset, Graphics.HCENTER | Graphics.TOP);
                heightOffset += lineHeight;
            }
            for(int i = 0; i < itemsNumber; i++) {
                if (items[i] == null)
                    continue;
                
                if(i == currentSelection) 
                    graphics.setColor(selectionColor);
                else
                    graphics.setColor(itemsColor);
                items[i].draw(graphics, getWidth() / 2, heightOffset);
                heightOffset += lineHeight;
            }
        }

        protected void keyPressed(int key) {
            try {
                switch (getGameAction(key)) {
                case DOWN:
                    do {
                        currentSelection = (currentSelection + 1) % itemsNumber;
                    } while (items[currentSelection] == null);
                    repaint();
                    break;
                    
                case UP:
                    do {
                        currentSelection--;
                        if (currentSelection == -1)
                            currentSelection = itemsNumber - 1;
                    } while (items[currentSelection] == null);
                    repaint();
                    break;
                 
                case FIRE: 
                    menuListener.onMenuEvent(items[currentSelection], MenuEvent.SELECT_EVENT, oldDisplayable);
                    if (isOpen())
                        repaint();
                    break;
                case RIGHT: 
                    menuListener.onMenuEvent(items[currentSelection], MenuEvent.RIGHT_EVENT, oldDisplayable);
                    if (isOpen())
                        repaint();
                    break;
                case LEFT: 
                    menuListener.onMenuEvent(items[currentSelection], MenuEvent.LEFT_EVENT, oldDisplayable);
                    if (isOpen())
                        repaint();
                    break;
                }
            } catch (IllegalArgumentException e) {
              // Not a game key then it is not interesting.  
            }
        }
        
        public void setCurrentSelection(int newSelection) {
            currentSelection = newSelection;
        }
            
     
    }
    
    /**
     * 
     * @param commandNumber
     */
    public MenuScreen(int itemsNumber) {
        this.title = null;
        this.canvas = new MenuScreenCanvas();
        setBackgroundColor(0, 0, 0);    // Black
        setMenuBackgroundColor(0, 0, 0); // Black;
        setItemsColor(255, 255, 255);   // White
        setSelectionColor(255, 0, 0);   // Red
        setTitleColor(0, 0, 255);       // Blue
        this.itemsNumber = itemsNumber;
        this.items = new MenuItem[itemsNumber];
        isOpen = false;
    }
    

    /**
     * 
     * @param display
     */
    public void open(Display display) {
        isOpen = true;
        canvas.setCurrentSelection(0);
        this.display = display;
        oldDisplayable = display.getCurrent();
        display.setCurrent(canvas);        
    }
    
    public void close() {
        if (isOpen) {
            display.setCurrent(oldDisplayable);
            isOpen = false;
        }
    }
    
    public void setMenuListener(MenuListener menuListener) {
        this.menuListener = menuListener;
    }
    
    public void setMenuItem(int itemNumber, MenuItem menuItem) {
        if (itemNumber >= itemsNumber)
            throw new IllegalArgumentException("Invalid item number.");
        items[itemNumber] = menuItem;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }

    public void setBackgroundColor(int red, int green, int blue) {
        backgroundColor = blue + (green << 8) + (red << 16);
    }

    public void setItemsColor(int red, int green, int blue) {
        itemsColor = blue + (green << 8) + (red << 16);
    }

    public void setSelectionColor(int red, int green, int blue) {
        selectionColor = blue + (green << 8) + (red << 16);
    }

    public void setTitleColor(int red, int green, int blue) {
        titleColor = blue + (green << 8) + (red << 16);
    }

    public void setMenuBackgroundColor(int red, int green, int blue) {
        setMenuBackgroundColor(red, green, blue, 254);
        menuBackgroundColor = blue + (green << 8) + (red << 16);
    }

    public void setMenuBackgroundColor(int red, int green, int blue, int opacity) {
        menuBackgroundOpacity = opacity;
        menuBackgroundColor = blue + (green << 8) + (red << 16);
    }
    
    public boolean isOpen() {
        return isOpen;
    }
    
    public void setBackgroundImage(Image backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

}
