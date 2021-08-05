package com.exiro.render.interfaceList;

import com.exiro.depacking.TileImage;
import com.exiro.fileManager.FontLoader;
import com.exiro.fileManager.ImageLoader;
import com.exiro.render.Button;

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

    ArrayList<TextInterface> texts;


    public BuildingInterface(int x, int y, int w, int h, ArrayList<Button> buttons) {
        super(x, y, w, h, buttons);
        top = new ArrayList<>();
        left = new ArrayList<>();
        right = new ArrayList<>();
        bot = new ArrayList<>();
        fill = new ArrayList<>();
        texts = new ArrayList<>();

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
    }

    public void addText(String text, String font, float size, int x, int y) {
        texts.add(new TextInterface(text, FontLoader.getFont(font).deriveFont(size), x, y));
    }


    public void RenderBg(Graphics g) {
        int nbrw = (w - corn1.getW() - corn2.getW()) / top.get(0).getW();
        int nbrh = (w - corn1.getH() - corn3.getH()) / left.get(0).getH();

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
    public void Render(Graphics g) {
        if (isOpen) {
            RenderBg(g);
            if (buttons != null) {
                for (Button b : buttons) {
                    b.Render(g, x, y);
                }
            }
            if (texts != null) {
                for (TextInterface t : texts) {
                    t.Render(g, x, y);
                }
            }
        }
    }
}
