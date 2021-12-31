package com.exiro.render;

import com.exiro.depacking.TileImage;
import com.exiro.fileManager.ImageLoader;
import com.exiro.utils.Point;

import java.awt.*;

public class Button {


    int x, y, w, h;
    TileImage image;
    ButtonType type;
    int id = 0;
    int ID = 0;

    public Button(int x, int y, int w, int h, int bitid, int id, ButtonType type) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.image = ImageLoader.getImage("Zeus_Interface", bitid, id);
        this.type = type;
        this.id = id;
    }
    public Button(int x, int y, int w, int h, int bitid, int id, ButtonType type,int ID) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.image = ImageLoader.getImage("Zeus_Interface", bitid, id);
        this.type = type;
        this.id = id;
        this.ID = ID;
    }

    public Button(int x, int y, int w, int h, ButtonType type,int ID) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.image = null;
        this.type = type;
        this.id = id;
        this.ID = ID;
    }
    public void setClicked(boolean clicked) {
        if (clicked) {
            this.image = ImageLoader.getImage("Zeus_Interface", image.getBitID(), id + 1);
        } else {
            this.image = ImageLoader.getImage("Zeus_Interface", image.getBitID(), id);
        }
    }


    public boolean clicked(int xc, int yc) {
        if (xc > x && xc < x + w && yc > y && yc < y + h) {
            return true;
        }
        return false;
    }

    public ButtonType getType() {
        return type;
    }

    public void Render(Graphics g, int offx, int offy, Point lastP) {
        if(image != null)
            g.drawImage(image.getImg(), (x) + offx, (y) + offy, (w), (h), null);
    }

    public int getID() {
        return ID;
    }
}
