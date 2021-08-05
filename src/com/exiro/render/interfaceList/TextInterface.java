package com.exiro.render.interfaceList;

import java.awt.*;

public class TextInterface {

    String text;
    Font font;
    int x, y;

    public TextInterface(String text, Font font, int x, int y) {
        this.text = text;
        this.font = font;
        this.x = x;
        this.y = y;
    }

    public void Render(Graphics g, int offx, int offy) {
        g.setFont(getFont());
        g.drawString(text, x + offx, y + offy);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
