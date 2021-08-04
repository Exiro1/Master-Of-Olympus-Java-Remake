package com.exiro.render.layout;

import com.exiro.fileManager.ImageLoader;
import com.exiro.systemCore.GameManager;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GameInfo extends JPanel {

    private final GameManager gm;
    private Font font;
    private BufferedImage drachme, background1;


    public GameInfo(GameManager gm) {
        this.gm = gm;
        File f = new File("Assets/Fonts/Zeus.ttf");
        File imgdrachme = new File("Assets/Interface/Icon/Drachmes.png");

        background1 = ImageLoader.getImage("Zeus_Interface", 7, 0).getImg();

        try {
            font = Font.createFont(Font.PLAIN, f);
            drachme = ImageIO.read(imgdrachme);
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
        g.fillRect(0, 0, 800, getHeight());

        g.drawImage(background1, 0, 0, 838, 30, null);


        g.setColor(Color.BLACK);

        g.setFont(font.deriveFont(16f));

        g.drawString("Date 13 Juillet 345 BC", 550, 20);
        String money = (int) gm.getPlayer().getMoney() + "";
        g.drawString(money, 500 - 12 * money.length(), 20);
        g.drawImage(drachme, 500, 5, null);

    }

    public void clickManager(MouseEvent e) {
    }
}
