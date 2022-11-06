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

public class MenuBar extends JPanel {

    private final GameManager gm;
    private Font font;
    private final BufferedImage background1;

    public MenuBar(GameManager gm) {
        this.gm = gm;
        File f = new File("Assets/Fonts/Zeus.ttf");

        background1 = ImageLoader.getImage("Zeus_Interface", 7, 0).getImg();

        try {
            font = Font.createFont(Font.PLAIN, f);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isClicked(int xc, int yc) {
        return xc > getBounds().x && xc < getBounds().x + getBounds().width && yc > getBounds().y && yc < getBounds().y + getBounds().height;
    }

    protected void paintComponent(Graphics g) {

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, (int) (800 * GameFrame.FWRATIO), (int)(30* GameFrame.FHRATIO));
        g.drawImage(background1, 0, 0, (int)(800* GameFrame.FWRATIO), (int)(30* GameFrame.FHRATIO), null);


        g.setColor(Color.BLACK);

        g.setFont(font.deriveFont(16f));

        g.drawString("Fichier      Vitesse       Son", 10, 20);


    }


    public void clickManager(MouseEvent e) {
    }
}
