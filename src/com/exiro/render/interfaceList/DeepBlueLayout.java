package com.exiro.render.interfaceList;

import com.exiro.reader.TileImage;
import com.exiro.fileManager.ImageLoader;
import com.exiro.render.Button;

import java.awt.*;
import java.util.ArrayList;

public class DeepBlueLayout extends InterfaceLayout{




    public DeepBlueLayout(int x, int y, int w,int h){

        super(x, y, w, h);

        corn1 = ImageLoader.getImage("Zeus_Interface", 1, 283);
        for (int i = 0; i < 5; i++) {
            top.add(ImageLoader.getImage("Zeus_Interface", 1, 284 + i));
        }
        corn2 = ImageLoader.getImage("Zeus_Interface", 1, 289);
        for (int i = 0; i < 5*7; i += 7) {
            left.add(ImageLoader.getImage("Zeus_Interface", 1, 290 + i));
        }
        for (int i = 0; i < 5*7; i += 7) {
            right.add(ImageLoader.getImage("Zeus_Interface", 1, 296 + i));
        }
        corn3 = ImageLoader.getImage("Zeus_Interface", 1, 325);

        for (int i = 0; i < 5; i++) {
            bot.add(ImageLoader.getImage("Zeus_Interface", 1, 326 + i));
        }

        corn4 = ImageLoader.getImage("Zeus_Interface", 1, 331);

        for (int i = 0; i < 5; i++) {
            fill.add(ImageLoader.getImage("Zeus_Interface", 1, 291 + i));
        }
        for (int i = 0; i < 5; i++) {
            fill.add(ImageLoader.getImage("Zeus_Interface", 1, 298 + i));
        }

        int nbrw = (w - corn1.getW() - corn2.getW()) / top.get(0).getW();
        int nbrh = (h - corn1.getH() - corn3.getH()) / left.get(0).getH();
        this.w = (nbrw)*top.get(0).getW()+corn1.getW()+corn2.getW();
        this.h = (nbrh)*left.get(0).getH()+corn1.getH()+corn3.getH();

    }

}
