package com.exiro.render.layout;

import com.exiro.fileManager.ImageLoader;
import com.exiro.render.GameFrame;
import com.exiro.systemCore.GameManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GameInfo extends JPanel {

    private final GameManager gm;
    private Font font;
    public static int HEIGHT;
    private final BufferedImage drachme;
    private final BufferedImage people;
    private final BufferedImage background1;

    public GameInfo(GameManager gm) {
        this.gm = gm;
        File f = new File("Assets/Fonts/Zeus.ttf");
        //File imgdrachme = new File("Assets/Interface.java/Icon/Drachmes.png");

        background1 = ImageLoader.getImage("Zeus_Interface", 7, 0).getImg();
        drachme = ImageLoader.getImage("Zeus_Interface", 2, 18).getImg();
        people = ImageLoader.getImage("Zeus_Interface", 1, 340).getImg();

        try {
            font = Font.createFont(Font.PLAIN, f);
            font = font.deriveFont(Font.BOLD);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }

    }

    public boolean isClicked(int xc, int yc) {
        return xc > getBounds().x && xc < getBounds().x + getBounds().width && yc > getBounds().y && yc < getBounds().y + getBounds().height;
    }

    // What the last paint showed; the bar only needs repainting when one of these changes.
    private String paintedDate;
    private int paintedMoney = Integer.MIN_VALUE, paintedPop = Integer.MIN_VALUE;

    public boolean needsRepaint() {
        return paintedDate == null
                || (int) gm.getPlayer().getMoney() != paintedMoney
                || gm.getCurrentCity().getPopulation() != paintedPop
                || !gm.getTimeManager().toString().equals(paintedDate);
    }

    public void paintComponent(Graphics g) {
        //   super.paintComponent(g);
        paintedDate = gm.getTimeManager().toString();
        paintedMoney = (int) gm.getPlayer().getMoney();
        paintedPop = gm.getCurrentCity().getPopulation();

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());

        g.drawImage(background1, 0, 0, getWidth(), getHeight(), null);


        g.setColor(Color.BLACK);

        g.setFont(font.deriveFont(16f));

        g.drawString(gm.getTimeManager().toString(), 1400, 20);
        String money = (int) gm.getPlayer().getMoney() + "";
        g.drawString(money, GameFrame.scalingW(800) - 12 * money.length(), GameFrame.scalingH(20));
        g.drawImage(drachme, GameFrame.scalingW(800) - 12 * money.length() - 35, 5, null);
        String pop = gm.getCurrentCity().getPopulation() + "";
        g.drawString(pop, GameFrame.scalingW(1000) - 12 * pop.length(), 20);
        g.drawImage(people, GameFrame.scalingW(1000) - 12 * pop.length() - 30, 0, GameFrame.scalingW(19), GameFrame.scalingH(31), null);

    }
    
    public void clickManager(MouseEvent e) {
    }
}
