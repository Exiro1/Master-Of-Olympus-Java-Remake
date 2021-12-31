package com.exiro.render.interfaceList;

import com.exiro.depacking.TileImage;
import com.exiro.fileManager.FontLoader;
import com.exiro.fileManager.ImageLoader;
import com.exiro.object.ObjectClass;
import com.exiro.object.Orders;
import com.exiro.render.Button;
import com.exiro.render.ButtonType;
import com.exiro.render.HoverButton;
import com.exiro.utils.Point;

import java.awt.*;
import java.util.ArrayList;

public class BuildingInterface extends Interface {

    TileImage corn1;
    TileImage corn2;
    TileImage corn3;
    TileImage corn4;
    ArrayList<TileImage> top;
    ArrayList<TileImage> left;
    ArrayList<TileImage> right;
    ArrayList<TileImage> bot;
    ArrayList<TileImage> fill;
    ObjectClass b;


    public BuildingInterface(int x, int y, int w, int h, ArrayList<Button> buttons, ObjectClass b) {
        super(x, y, w, h, buttons,b);
        top = new ArrayList<>();
        left = new ArrayList<>();
        right = new ArrayList<>();
        bot = new ArrayList<>();
        fill = new ArrayList<>();
        obj = new ArrayList<>();

        corn1 = ImageLoader.getImage("Zeus_Interface", 1, 139);
        for (int i = 0; i < 10; i++) {
            top.add(ImageLoader.getImage("Zeus_Interface", 1, 140 + i));
        }
        corn2 = ImageLoader.getImage("Zeus_Interface", 1, 150);
        for (int i = 0; i < 10; i += 12) {
            left.add(ImageLoader.getImage("Zeus_Interface", 1, 151 + i));
        }
        for (int i = 0; i < 10; i += 12) {
            right.add(ImageLoader.getImage("Zeus_Interface", 1, 162 + i));
        }
        corn3 = ImageLoader.getImage("Zeus_Interface", 1, 271);

        for (int i = 0; i < 10; i++) {
            bot.add(ImageLoader.getImage("Zeus_Interface", 1, 272 + i));
        }

        corn4 = ImageLoader.getImage("Zeus_Interface", 1, 282);

        for (int i = 0; i < 10; i++) {
            fill.add(ImageLoader.getImage("Zeus_Interface", 1, 152 + i));
        }

        int nbrw = (w - corn1.getW() - corn2.getW()) / top.get(0).getW();
        int nbrh = (h - corn1.getH() - corn3.getH()) / left.get(0).getH();
        this.w = (nbrw)*top.get(0).getW()+corn1.getW()+corn2.getW();
        this.h = (nbrh)*left.get(0).getH()+corn1.getH()+corn3.getH();
        this.buttons.add(new Button(w-55,h-55,24,24,1,336,ButtonType.INTERFACE_OK));
        this.buttons.add(new Button(15,h-55,24,24,1,332,ButtonType.INTERFACE_HELP));
        this.b = b;
    }



    @Override
    public int getH() {
        return super.getH()-32;
    }

    @Override
    public int getW() {
        return super.getW()-32;
    }


    public void addStockItem(int stocked, String type, Orders order, int stockLimit, int y, String font, int ID){
        x=30;
        x+=16;
        y+=16;

        obj.add(new TextInterface(stocked+"   "+type,FontLoader.getFont(font).deriveFont(16f), x, y));
        addHoverButton(new HoverButton(x+270-6,y-16,100,22,ButtonType.INTERFACE_ORDER,ID));
        obj.add(new TextInterface(order.toString(),FontLoader.getFont(font).deriveFont(16f), x+270, y));
        obj.add(new TextInterface(stockLimit+"",FontLoader.getFont(font).deriveFont(16f), x+443, y));
        buttons.add((new Button(x+470,y-14,17,17,1,377, ButtonType.INTERFACE_DOWN,ID)));
        buttons.add((new Button(x+488,y-14,17,17,1,375, ButtonType.INTERFACE_UP,ID)));
    }




    public void addLine(int x,int y,int w){
        obj.add(new LineInterface(x+16,y+16,w));
    }

    public void RenderBg(Graphics g) {
        int nbrw = (w - corn1.getW() - corn2.getW()) / top.get(0).getW();
        int nbrh = (h - corn1.getH() - corn3.getH()) / left.get(0).getH();


        g.drawImage(corn1.getImg(), x, y, null);
        for (int i = 1; i < nbrw; i++) {
            g.drawImage(top.get(i % top.size()).getImg(), x + i * 16, y, null);
        }
        g.drawImage(corn2.getImg(), x + nbrw * 16, y, null);
        for (int j = 1; j < nbrh; j++) {
            g.drawImage(left.get(j % left.size()).getImg(), x, y + j * 16, null);
            for (int i = 1; i < nbrw; i++) {
                g.drawImage(fill.get((i * j + i + j) % fill.size()).getImg(), x + i * 16, y + j * 16, null);
            }
            g.drawImage(right.get(j % right.size()).getImg(), x + nbrw * 16, y + j * 16, null);
        }
        g.drawImage(corn3.getImg(), x, y + nbrh * 16, null);
        for (int i = 1; i < nbrw; i++) {
            g.drawImage(bot.get(i % bot.size()).getImg(), x + i * 16, y + nbrh * 16, null);
        }
        g.drawImage(corn4.getImg(), x + nbrw * 16, y + nbrh * 16, null);
    }


    @Override
    public void Render(Graphics g, Point lastP) {
        if (isOpen) {
            RenderBg(g);
            for(InterfaceLayout child : childrens){
                child.Render(g,x,y);
            }
            if (buttons != null) {
                for (Button b : buttons) {
                    b.Render(g, x, y, lastP);
                }
            }
            if (obj != null) {
                for (InterfaceObject t : obj) {
                    t.Render(g, x, y);
                }
            }
        }
    }
}
