package com.exiro.render.interfaceList;

import com.exiro.depacking.TileImage;
import com.exiro.fileManager.FontLoader;
import com.exiro.fileManager.ImageLoader;
import com.exiro.object.ObjectClass;
import com.exiro.render.Button;
import com.exiro.render.ButtonType;
import com.exiro.render.HoverButton;
import com.exiro.utils.Point;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

public class Interface {


    boolean requestUpdate;
    int x, y, h, w;
    ArrayList<Button> buttons;
    ArrayList<InterfaceLayout> childrens;

    ArrayList<InterfaceObject> obj = new ArrayList<>();

    boolean isOpen;
    ObjectClass caller;
    public Interface(int x, int y, int w, int h, ArrayList<Button> buttons) {
        childrens = new ArrayList<>();
        this.x = x;
        this.y = y;
        this.h = h;
        this.w = w;
        if(buttons == null){
            buttons = new ArrayList<>();
        }
        this.buttons = buttons;

        this.isOpen = true;
    }
    public Interface(int x, int y, int w, int h, ArrayList<Button> buttons, ArrayList<InterfaceLayout> childrens) {
        this.x = x;
        this.y = y;
        this.h = h;
        this.w = w;
        if(buttons == null){
            buttons = new ArrayList<>();
        }
        this.buttons = buttons;

        this.isOpen = true;
        this.childrens = childrens;
    }
    public Interface(int x, int y, int w, int h, ArrayList<Button> buttons, ObjectClass caller) {
        this.x = x;
        this.y = y;
        this.h = h;
        this.w = w;
        if(buttons == null){
            buttons = new ArrayList<>();
        }
        this.buttons = buttons;
        this.isOpen = true;
        childrens = new ArrayList<>();
        this.caller = caller;
    }

    public int getH() {
        return h;
    }

    public int getW() {
        return w;
    }

    public void Render(Graphics g, Point lastP) {
        if (isOpen) {
            for (InterfaceLayout child : childrens) {
                child.Render(g,x,y);
            }
            for (Button b : buttons) {
                b.Render(g, x, y,lastP);
            }
        }
    }

    public void addChildLayout(InterfaceLayout child){
        childrens.add(child);
    }

    public void addText(String text, String font, float size, int x, int y) {
        x+=16;
        y+=16;
        obj.add(new TextInterface(text, FontLoader.getFont(font).deriveFont(size), x, y));
    }
    public void addText(String text, float size, int x, int y) {
        x+=16;
        y+=16;
        obj.add(new TextInterface(text, FontLoader.getFont("Zeus.ttf").deriveFont(size), x, y));
    }
    public void addCenteredText(String text, String font, float size, int y) {
        y+=16;
        Font f = FontLoader.getFont(font).deriveFont(size);
        FontRenderContext frc = new FontRenderContext(new AffineTransform(), true, true);
        int textwidth = (int)(f.getStringBounds(text, frc).getWidth());
        obj.add(new TextInterface(text, f, (getW()-textwidth)/2 + 16, y));
    }
    public void addCenteredText(String text, float size, int y) {
        y+=16;
        Font f = FontLoader.getFont("Zeus.ttf").deriveFont(size);
        FontRenderContext frc = new FontRenderContext(new AffineTransform(), true, true);
        int textwidth = (int)(f.getStringBounds(text, frc).getWidth());
        obj.add(new TextInterface(text, f, (getW()-textwidth)/2 + 16, y));
    }

    public void addHoverButton(HoverButton button){
        buttons.add(button);
    }

    public void addImage(TileImage img, int x,int y){
        obj.add(new InterfaceImage(img,x,y));
    }
    public void addImage(String filename,int bitmap,int id, int x,int y){
        obj.add(new InterfaceImage(ImageLoader.getImage(filename, bitmap, id),x,y));
    }
    public void addImage(ImageLoader.FilePath filepath, int bitmap, int id, int x, int y){
        obj.add(new InterfaceImage(ImageLoader.getImage(filepath, bitmap, id),x,y));
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
                        if(caller!=null)
                            caller.buttonClickedEvent(b.getType(),b.getID());
                        return b.getType();
                    }
                }

            } else {
                return ButtonType.NONE;
            }
        }
        return ButtonType.NONE;
    }

    public void requestUpdate(){
        requestUpdate = true;
    }

    public boolean updateRequested() {
        return requestUpdate;
    }
}
