package com.exiro.render;

import com.exiro.systemCore.GameManager;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MenuBar extends JPanel {

    private final GameManager gm;
    private Font font;
    private BufferedImage background1;

    public MenuBar(GameManager gm) {
        this.gm = gm;
        File f = new File("Assets/Fonts/Zeus.ttf");


        File background1f = new File("Assets/Interface/Zeus_Interface_New_parts_001382.png");


        try {
            font = Font.createFont(Font.PLAIN, f);
            background1 = ImageIO.read(background1f);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
    }

    protected void paintComponent(Graphics g) {

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, 800, getHeight());
        g.drawImage(background1, 0, 0, 800, 30, null);


        g.setColor(Color.BLACK);

        g.setFont(font.deriveFont(16f));

        g.drawString("Fichier      Vitesse       Son", 10, 20);


    }


}
