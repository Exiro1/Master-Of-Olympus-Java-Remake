package com.exiro.render.interfaceList;

import com.exiro.reader.TileImage;
import com.exiro.render.layout.InterfaceRender;

import java.awt.*;

public class InterfaceImage extends InterfaceObject {


    TileImage img;
    int x,y;

    public InterfaceImage(TileImage img, int x, int y) {
        this.img = img;
        this.x = x;
        this.y = y;
    }

    @Override
    public void Render(Graphics g, int offx, int offy) {
        g.drawImage(img.getImg(),x+offx,y+offy,null);
    }


}
