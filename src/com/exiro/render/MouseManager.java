package com.exiro.render;

import com.exiro.object.Case;
import com.exiro.object.City;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MouseManager implements MouseListener {

    static public Boolean pressing = false;
    static public Boolean click = false;

    /**
     * Invoked when the mouse button has been clicked (pressed
     * and released) on a component.
     *
     * @param e
     */
    Case c1;
    Case c2;
    private final GameFrame frame;
    private City actualCity;

    public MouseManager(GameFrame frame) {
        this.frame = frame;
    }


    @Override
    public void mouseClicked(MouseEvent e) {

        if (frame.getIt().isClicked(e.getX(), e.getY())) {
            frame.getIt().clickManager(e);
        } else if (frame.getWindow().isClicked(e.getX(), e.getY())) {
            frame.getWindow().clickManager(e);
        } else if (frame.getGi().isClicked(e.getX(), e.getY())) {
            frame.getGi().clickManager(e);
        } else if (frame.getMenu().isClicked(e.getX(), e.getY())) {
            frame.getMenu().clickManager(e);
        }
    }

    /**
     * Invoked when a mouse button has been pressed on a component.
     *
     * @param e
     */
    @Override
    public void mousePressed(MouseEvent e) {
        if (frame.getWindow().isClicked(e.getX(), e.getY())) {
            frame.getWindow().pressing = true;
        }
        pressing = true;
    }

    /**
     * Invoked when a mouse button has been released on a component.
     *
     * @param e
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        if (frame.getWindow().isClicked(e.getX(), e.getY())) {
            frame.getWindow().pressing = false;
        }
        pressing = false;
        click = false;
    }


    /**
     * Invoked when the mouse enters a component.
     *
     * @param e
     */
    @Override
    public void mouseEntered(MouseEvent e) {

    }

    /**
     * Invoked when the mouse exits a component.
     *
     * @param e
     */
    @Override
    public void mouseExited(MouseEvent e) {

    }
}
