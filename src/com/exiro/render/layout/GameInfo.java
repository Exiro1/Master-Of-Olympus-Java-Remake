package com.exiro.render.layout;

import com.exiro.fileManager.ImageLoader;
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
    private BufferedImage drachme, people, background1;

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
        if (xc > getBounds().x && xc < getBounds().x + getBounds().width && yc > getBounds().y && yc < getBounds().y + getBounds().height) {
            return true;
        }
        return false;
    }

    public void paintComponent(Graphics g) {
        //   super.paintComponent(g);
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());

        g.drawImage(background1, 0, 0, getWidth(), getHeight(), null);


        g.setColor(Color.BLACK);

        g.setFont(font.deriveFont(16f));

        g.drawString("Date 13 Juillet 345 BC", 1400, 20);
        String money = (int) gm.getPlayer().getMoney() + "";
        g.drawString(money, 800 - 12 * money.length(), 20);
        g.drawImage(drachme, 800 - 12 * money.length() - 35, 5, null);
        String pop = (int) gm.getCurrentCity().getPopulation() + "";
        g.drawString(pop, 1000 - 12 * pop.length(), 20);
        g.drawImage(people, 1000 - 12 * pop.length() - 30, 0, 19, 31, null);

    }

    public void clickManager(MouseEvent e) {
    }
}
