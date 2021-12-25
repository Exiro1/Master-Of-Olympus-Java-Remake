package com.exiro.render.interfaceList;

import com.exiro.buildingList.stockage.Granary;
import com.exiro.depacking.TileImage;
import com.exiro.fileManager.FontLoader;
import com.exiro.fileManager.ImageLoader;
import com.exiro.render.Button;
import com.sun.corba.se.impl.orbutil.graph.Graph;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

public class InterfaceLayout {

    int w,h,x,y;

    TileImage corn1;
    TileImage corn2;
    TileImage corn3;
    TileImage corn4;
    ArrayList<TileImage> top;
    ArrayList<TileImage> left;
    ArrayList<TileImage> right;
    ArrayList<TileImage> bot;
    ArrayList<TileImage> fill;

    ArrayList<InterfaceObject> obj;

    public InterfaceLayout(int x, int y, int w,int h) {
        top = new ArrayList<>();
        left = new ArrayList<>();
        right = new ArrayList<>();
        bot = new ArrayList<>();
        fill = new ArrayList<>();
        obj = new ArrayList<>();
        this.x = x;
        this.w = w;
        this.y = y;
        this.h = h;
    }

    void RenderBg(Graphics g,int offx,int offy) {

        int nbrw = (w - corn1.getW() - corn2.getW()) / top.get(0).getW();
        int nbrh = (h - corn1.getH() - corn3.getH()) / left.get(0).getH();

        g.drawImage(corn1.getImg(), offx+x, offy + y, null);
        for (int i = 1; i < nbrw; i++) {
            g.drawImage(top.get(i % top.size()).getImg(), offx+x + i * 16, offy +y, null);
        }
        g.drawImage(corn2.getImg(), offx+x + nbrw * 16, offy +y, null);
        for (int j = 1; j < nbrh; j++) {
            g.drawImage(left.get(j % left.size()).getImg(), offx+x, offy +y + j * 16, null);
            for (int i = 1; i < nbrw; i++) {
                g.drawImage(fill.get((i * j + i + j) % fill.size()).getImg(), offx+x + i * 16, offy +y + j * 16, null);
            }
            g.drawImage(right.get(j % right.size()).getImg(), offx+x + nbrw * 16, offy +y + j * 16, null);
        }
        g.drawImage(corn3.getImg(), offx+x, offy +y + nbrh * 16, null);
        for (int i = 1; i < nbrw; i++) {
            g.drawImage(bot.get(i % bot.size()).getImg(), offx+x + i * 16, offy +y + nbrh * 16, null);
        }
        g.drawImage(corn4.getImg(), offx+x + nbrw * 16, offy +y + nbrh * 16, null);


    }

    public int getW(){
        return w-2*16;
    }

    public void addText(String text, String font, float size, int x, int y) {
        x+=16;
        y+=16;
        obj.add(new TextInterface(text, FontLoader.getFont(font).deriveFont(size), x, y));
    }
    public void addCenteredText(String text, String font, float size, int y) {
        y+=16;
        Font f = FontLoader.getFont(font).deriveFont(size);
        FontRenderContext frc = new FontRenderContext(new AffineTransform(), true, true);
        int textwidth = (int)(f.getStringBounds(text, frc).getWidth());
        obj.add(new TextInterface(text, f, (getW()-textwidth)/2 + 16, y));
    }

    public void Render(Graphics g,int offx,int offy) {
            RenderBg(g,offx,offy);
            if (obj != null) {
                for (InterfaceObject t : obj) {
                    t.Render(g, x+offx, y+offy);
                }
            }
    }

}
