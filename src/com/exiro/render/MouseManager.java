package com.exiro.render;

import com.exiro.moveRelated.Path;
import com.exiro.object.Case;
import com.exiro.object.City;
import com.exiro.utils.Point;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MouseManager implements MouseListener {

    public static boolean build = true;
    static public Boolean pressing = false;
    public static Path pato;
    static Boolean click = false;
    /**
     * Invoked when the mouse button has been clicked (pressed
     * and released) on a component.
     *
     * @param e
     */
    Case c1;
    Case c2;
    private final GameWindow win;
    private City actualCity;

    public MouseManager(GameWindow win) {
        this.win = win;
    }

    @Override
    public void mouseClicked(MouseEvent e) {


        Case c = IsometricRender.getCase(new Point(e.getX(), e.getY()), win.p.getPlayerCities().get(0));


        if (e.getButton() == MouseEvent.BUTTON1) {
            click = true;
            if (build) {

            } else {
                c1 = c;
            }
        }
        if (e.getButton() == MouseEvent.BUTTON3) {
            if (build) {
                if (win.p.getPlayerCities().get(0).getMap().getCase(c.getxPos(), c.getyPos()).getObject() != null)
                    win.p.getPlayerCities().get(0).getMap().getCase(c.getxPos(), c.getyPos()).getObject().delete();
            }
        }


    }

    /**
     * Invoked when a mouse button has been pressed on a component.
     *
     * @param e
     */
    @Override
    public void mousePressed(MouseEvent e) {
        pressing = true;
    }

    /**
     * Invoked when a mouse button has been released on a component.
     *
     * @param e
     */
    @Override
    public void mouseReleased(MouseEvent e) {
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
