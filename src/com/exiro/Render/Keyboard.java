package com.exiro.Render;

import com.exiro.BuildingList.BuildingType;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Keyboard implements KeyListener {

    static boolean aaa = false;
    int x = 0;
    private GameWindow win;


    public Keyboard(GameWindow win) {
        this.win = win;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        char key = e.getKeyChar();
        if (key == 'd') {
            win.p.getPlayerCities().get(0).getBuildings().get(0).delete();
        }
        if (key == 'c') {
            EntityRender.setEntityRender(BuildingType.HOUSE);
        }
        if (key == 'r') {
            EntityRender.setEntityRender(BuildingType.ROAD);
        }
        if (key == 's') {
            EntityRender.setEntityRender(BuildingType.STOCK);
        }
        if (key == 'a') {
            GameWindow.index++;
            if (GameWindow.index > win.p.getPlayerCities().get(0).getPathManager().getPaths().size() - 1) {
                GameWindow.index = 0;
            }
        }
        if (key == 'g') {
            MouseManager.build = !MouseManager.build;
        }
        if (key == 'z')
            System.exit(0);


    }

    /**
     * Invoked when a key has been pressed.
     * See the class description for {@link KeyEvent} for a definition of
     * a key pressed event.
     *
     * @param e
     */
    @Override
    public void keyPressed(KeyEvent e) {

    }

    /**
     * Invoked when a key has been released.
     * See the class description for {@link KeyEvent} for a definition of
     * a key released event.
     *
     * @param e
     */
    @Override
    public void keyReleased(KeyEvent e) {

    }
}
