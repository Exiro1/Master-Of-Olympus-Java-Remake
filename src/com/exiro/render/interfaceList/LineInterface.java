package com.exiro.render.interfaceList;

import com.exiro.depacking.TileImage;
import com.exiro.fileManager.ImageLoader;

import java.awt.*;

public class LineInterface extends InterfaceObject{

    TileImage line;
    int x,y,w;

    public LineInterface(int x, int y, int w) {
        this.x = x;
        this.y = y;
        this.w = w;
        line = ImageLoader.getImage("Zeus_Interface", 1, 356);
    }

    public void Render(Graphics g, int offx, int offy) {
        g.drawImage(line.getImg(),x+offx,y+offy,w-16,16, null);
    }

}
