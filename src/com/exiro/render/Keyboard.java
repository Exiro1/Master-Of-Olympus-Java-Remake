package com.exiro.render;

import com.exiro.object.ObjectType;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Keyboard implements KeyListener {

    static boolean aaa = false;
    int x = 0;
    private final GameWindow win;


    public Keyboard(GameWindow win) {
        this.win = win;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        char key = e.getKeyChar();
        if (key == 'd') {
            //win.p.getPlayerCities().get(0).getBuildings().get(0).delete();
        }
        if (key == 'q') {
            EntityRender.setEntityRender(ObjectType.FISHERY);
        }
        if (key == 'c') {
            EntityRender.setEntityRender(ObjectType.HOUSE);
        }
        if (key == 'j') {
            EntityRender.setEntityRender(ObjectType.OLIVETREE);
        }
        if (key == 'k') {
            EntityRender.setEntityRender(ObjectType.DAIRY);
        }
        if (key == 'h') {
            EntityRender.setEntityRender(ObjectType.HUNTINGHOUSE);
        }
        if (key == 'l') {
            EntityRender.setEntityRender(ObjectType.SHEEPFOLD);
        }
        if (key == 'f') {
            EntityRender.setEntityRender(ObjectType.FARM);
        }
        if (key == 'r') {
            EntityRender.setEntityRender(ObjectType.ROAD);
        }
        if (key == 's') {
            EntityRender.setEntityRender(ObjectType.STOCK);
        }
        if (key == 'w') {
            EntityRender.setEntityRender(ObjectType.WATERWELL);
        }
        if (key == 'm') {
            EntityRender.setEntityRender(ObjectType.GRANARY);
        }
        if (key == 'o') {
            EntityRender.setEntityRender(ObjectType.SMALLHOLDING);
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
