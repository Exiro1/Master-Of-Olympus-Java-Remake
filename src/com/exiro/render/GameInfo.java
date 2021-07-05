package com.exiro.render;

import com.exiro.systemCore.GameManager;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
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

        File background1f = new File("Assets/Interface/Zeus_Interface_New_parts_00138.png");


        try {
            font = Font.createFont(Font.PLAIN, f);
            drachme = ImageIO.read(imgdrachme);
            background1 = ImageIO.read(background1f);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }

    }


    public void paintComponent(Graphics g) {
        //   super.paintComponent(g);
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, 800, getHeight());

        g.drawImage(background1, 0, 0, 800, 30, null);


        g.setColor(Color.BLACK);

        g.setFont(font.deriveFont(16f));

        g.drawString("Date 13 Juillet 345 BC", 550, 20);
        String money = (int) gm.getPlayer().getMoney() + "";
        g.drawString(money, 500 - 12 * money.length(), 20);
        g.drawImage(drachme, 500, 5, null);

    }
}
