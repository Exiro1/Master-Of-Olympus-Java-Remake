package com.exiro.render.interfaceList;

import com.exiro.render.Button;
import com.exiro.render.ButtonType;

import java.awt.*;
import java.util.ArrayList;

public class Interface {


    int x, y, h, w;
    ArrayList<Button> buttons;
    boolean isOpen;

    public Interface(int x, int y, int w, int h, ArrayList<Button> buttons) {
        this.x = x;
        this.y = y;
        this.h = h;
        this.w = w;
        this.buttons = buttons;
        this.isOpen = true;
    }

    public void Render(Graphics g) {
        if (isOpen) {
            for (Button b : buttons) {
                b.Render(g, x, y);
            }
        }
    }

    public void close() {
        isOpen = false;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public ButtonType clicked(int xc, int yc) {
        if (isOpen) {
            if (xc > x && xc < x + w && yc > y && yc < yc + h) {
                for (Button b : buttons) {
                    if (b.clicked(xc - x, yc - y)) {
                        return b.getType();
                    }
                }
            } else {
                return ButtonType.NONE;
            }
        }
        return ButtonType.NONE;
    }

}
