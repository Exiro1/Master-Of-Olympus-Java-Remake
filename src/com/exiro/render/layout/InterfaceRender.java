package com.exiro.render.layout;

import com.exiro.fileManager.ImageLoader;
import com.exiro.render.Button;
import com.exiro.render.ButtonType;
import com.exiro.systemCore.GameManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class InterfaceRender extends JPanel {
    private final GameManager gm;

    BufferedImage bg, sideLine, test;

    int oWIDTH = 186;
    int oHEIGHT = 768;
    int WIDTH = 320;

    double fw = 0;
    double fh = 0;

    ArrayList<Button> buttons;
    ArrayList<Button> buildButtons;

    public InterfaceRender(GameManager gm) {
        this.gm = gm;
        buttons = new ArrayList<>();
        buildButtons = new ArrayList<>();
    }

    public void initGraphics() {
        bg = ImageLoader.getImage("Zeus_Interface", 7, 1).getImg();
        sideLine = ImageLoader.getImage("Zeus_Interface", 7, 3).getImg();
        test = ImageLoader.getImage("Zeus_Interface", 7, 139).getImg();
        fw = (double) WIDTH / (double) oWIDTH;
        fh = (double) getHeight() / (double) oHEIGHT;

        int yoff = 22;
        buttons.add(new Button((int) (6 * fw), (int) (yoff * fh), (int) (44 * fw), (int) (41 * fh), 7, 92, ButtonType.HOUSE));
        yoff += 41;
        buttons.add(new Button((int) (6 * fw), (int) (yoff * fh), (int) (44 * fw), (int) (41 * fh), 7, 96, ButtonType.AGRICULTURE));
        yoff += 41;
        buttons.add(new Button((int) (6 * fw), (int) (yoff * fh), (int) (44 * fw), (int) (41 * fh), 7, 100, ButtonType.INDUSTRY));
        yoff += 41;
        buttons.add(new Button((int) (6 * fw), (int) (yoff * fh), (int) (44 * fw), (int) (41 * fh), 7, 104, ButtonType.MARKET));
        yoff += 41;
        buttons.add(new Button((int) (6 * fw), (int) (yoff * fh), (int) (44 * fw), (int) (41 * fh), 7, 108, ButtonType.HEALTH));
        yoff += 41;
        buttons.add(new Button((int) (6 * fw), (int) (yoff * fh), (int) (44 * fw), (int) (41 * fh), 7, 112, ButtonType.PALACE));
        yoff += 41;
        buttons.add(new Button((int) (6 * fw), (int) (yoff * fh), (int) (44 * fw), (int) (41 * fh), 7, 116, ButtonType.CULTURE));
        yoff += 41;
        buttons.add(new Button((int) (6 * fw), (int) (yoff * fh), (int) (44 * fw), (int) (41 * fh), 7, 120, ButtonType.TEMPLE));
        yoff += 41;
        buttons.add(new Button((int) (6 * fw), (int) (yoff * fh), (int) (44 * fw), (int) (41 * fh), 7, 124, ButtonType.ARMY));
        yoff += 41;
        buttons.add(new Button((int) (6 * fw), (int) (yoff * fh), (int) (44 * fw), (int) (41 * fh), 7, 128, ButtonType.CACHET));
        yoff += 41;
        buttons.add(new Button((int) (6 * fw), (int) (yoff * fh), (int) (44 * fw), (int) (41 * fh), 7, 88, ButtonType.MAPVIEW));


    }


    public boolean isClicked(int xc, int yc) {
        if (xc > getBounds().x && xc < getBounds().x + getBounds().width && yc > getBounds().y && yc < getBounds().y + getBounds().height) {
            return true;
        }
        return false;
    }


    public void paintComponent(Graphics g) {
        //  super.paintComponent(g);


        g.drawImage(bg, 0, 0, WIDTH - 16, getHeight(), null);
        g.drawImage(sideLine, WIDTH - 16, 0, 16, getHeight(), null);
        g.drawImage(test, (int) (50 * fw), (int) (300 * fh), (int) (128 * fw), (int) (18 * fh), null);

        for (Button b : buttons) {
            b.Render(g);
        }
        for (Button b : buildButtons) {
            b.Render(g);
        }

    }

    public void clickManager(MouseEvent e) {

        for (Button b : buttons) {
            if (b.clicked(e.getX() - this.getBounds().x, e.getY() - this.getBounds().y)) {
                buttonClicked(b);
            }
        }
        for (Button b : buildButtons) {
            if (b.clicked(e.getX() - this.getBounds().x, e.getY() - this.getBounds().y)) {
                buttonClicked(b);
            }
        }
    }

    public void buttonClicked(Button b) {
        for (Button ob : buttons) {
            ob.setClicked(false);
        }
        b.setClicked(true);
        switch (b.getType()) {
            case HOUSE:
                buildButtons.clear();
                buildButtons.add(new Button((int) (50 * fw), (int) (325 * fh), (int) (58 * fw), (int) (42 * fh), 8, 3, ButtonType.HOUSE_LITTLE));
                buildButtons.add(new Button((int) (120 * fw), (int) (325 * fh), (int) (58 * fw), (int) (42 * fh), 8, 6, ButtonType.HOUSE_BIG));
                break;
            case AGRICULTURE:
                buildButtons.clear();
                buildButtons.add(new Button((int) (50 * fw), (int) (325 * fh), (int) (58 * fw), (int) (42 * fh), 8, 9, ButtonType.AGRICULTURE_FARM));
                buildButtons.add(new Button((int) (120 * fw), (int) (325 * fh), (int) (58 * fw), (int) (42 * fh), 8, 12, ButtonType.AGRICULTURE_VITICULTURE));
                buildButtons.add(new Button((int) (50 * fw), (int) (375 * fh), (int) (58 * fw), (int) (42 * fh), 8, 15, ButtonType.AGRICULTURE_BREEDING));
                buildButtons.add(new Button((int) (120 * fw), (int) (375 * fh), (int) (58 * fw), (int) (42 * fh), 8, 18, ButtonType.AGRICULTURE_FISH));
                break;
            case INDUSTRY:
                buildButtons.clear();
                buildButtons.add(new Button((int) (50 * fw), (int) (325 * fh), (int) (58 * fw), (int) (42 * fh), 8, 21, ButtonType.INDUSTRY_1));
                buildButtons.add(new Button((int) (120 * fw), (int) (325 * fh), (int) (58 * fw), (int) (42 * fh), 8, 24, ButtonType.INDUSTRY_2));

                break;
            case MARKET:
                buildButtons.clear();
                buildButtons.add(new Button((int) (50 * fw), (int) (325 * fh), (int) (58 * fw), (int) (42 * fh), 8, 30, ButtonType.MARKET_GRANARY));
                buildButtons.add(new Button((int) (120 * fw), (int) (325 * fh), (int) (58 * fw), (int) (42 * fh), 8, 33, ButtonType.MARKET_STOCK));
                buildButtons.add(new Button((int) (50 * fw), (int) (375 * fh), (int) (58 * fw), (int) (42 * fh), 8, 36, ButtonType.MARKET_AGORA));
                buildButtons.add(new Button((int) (120 * fw), (int) (375 * fh), (int) (58 * fw), (int) (42 * fh), 8, 39, ButtonType.MARKET_TRANS));
                break;
            case HEALTH:
                buildButtons.clear();
                buildButtons.add(new Button((int) (50 * fw), (int) (325 * fh), (int) (58 * fw), (int) (42 * fh), 8, 45, ButtonType.HEALTH_WATER));
                buildButtons.add(new Button((int) (120 * fw), (int) (325 * fh), (int) (58 * fw), (int) (42 * fh), 8, 51, ButtonType.HEALTH_HOSPITAL));
                buildButtons.add(new Button((int) (50 * fw), (int) (375 * fh), (int) (58 * fw), (int) (42 * fh), 8, 42, ButtonType.HEALTH_SAFETY));
                buildButtons.add(new Button((int) (120 * fw), (int) (375 * fh), (int) (58 * fw), (int) (42 * fh), 8, 48, ButtonType.HEALTH_GUARD));
                break;
            case PALACE:
                buildButtons.clear();
                buildButtons.add(new Button((int) (50 * fw), (int) (325 * fh), (int) (58 * fw), (int) (42 * fh), 8, 54, ButtonType.PALACE_PALACE));
                buildButtons.add(new Button((int) (120 * fw), (int) (325 * fh), (int) (58 * fw), (int) (42 * fh), 8, 57, ButtonType.PALACE_TAX));
                buildButtons.add(new Button((int) (50 * fw), (int) (375 * fh), (int) (58 * fw), (int) (42 * fh), 8, 60, ButtonType.PALACE_BRIDGE));
                break;
            case CULTURE:
                buildButtons.clear();
                buildButtons.add(new Button((int) (50 * fw), (int) (325 * fh), (int) (58 * fw), (int) (42 * fh), 8, 66, ButtonType.CULTURE_PHILOSOPHIA));
                buildButtons.add(new Button((int) (120 * fw), (int) (325 * fh), (int) (58 * fw), (int) (42 * fh), 8, 69, ButtonType.CULTURE_GYMNASIUM));
                buildButtons.add(new Button((int) (50 * fw), (int) (375 * fh), (int) (58 * fw), (int) (42 * fh), 8, 72, ButtonType.CULTURE_DRAMA));
                buildButtons.add(new Button((int) (120 * fw), (int) (375 * fh), (int) (58 * fw), (int) (42 * fh), 8, 75, ButtonType.CULTURE_STADIUM));
                break;
            case TEMPLE:
                buildButtons.clear();
                buildButtons.add(new Button((int) (50 * fw), (int) (325 * fh), (int) (58 * fw), (int) (42 * fh), 8, 78, ButtonType.TEMPLE_TEMPLE));
                buildButtons.add(new Button((int) (120 * fw), (int) (325 * fh), (int) (58 * fw), (int) (42 * fh), 8, 81, ButtonType.TEMPLE_HEROE));
                buildButtons.add(new Button((int) (50 * fw), (int) (375 * fh), (int) (58 * fw), (int) (42 * fh), 8, 27, ButtonType.TEMPLE_CONSTRUCT));
                break;
            case ARMY:
                buildButtons.clear();
                buildButtons.add(new Button((int) (50 * fw), (int) (325 * fh), (int) (58 * fw), (int) (42 * fh), 8, 84, ButtonType.ARMY_FORT));
                buildButtons.add(new Button((int) (120 * fw), (int) (325 * fh), (int) (58 * fw), (int) (42 * fh), 8, 87, ButtonType.ARMY_BUILDING));
                break;
            case CACHET:
                buildButtons.clear();
                buildButtons.add(new Button((int) (50 * fw), (int) (325 * fh), (int) (58 * fw), (int) (42 * fh), 8, 90, ButtonType.CACHET_SIMPLE));
                buildButtons.add(new Button((int) (120 * fw), (int) (325 * fh), (int) (58 * fw), (int) (42 * fh), 8, 93, ButtonType.CACHET_ADVANCED));
                buildButtons.add(new Button((int) (50 * fw), (int) (375 * fh), (int) (58 * fw), (int) (42 * fh), 8, 96, ButtonType.CAHCHET_MONUMENT));
                break;
            case MAPVIEW:
                buildButtons.clear();
                break;
        }
    }


}
