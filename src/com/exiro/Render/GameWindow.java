package com.exiro.Render;

import com.exiro.BuildingList.House;
import com.exiro.Object.Case;
import com.exiro.Object.CityMap;
import com.exiro.Object.ObjectClass;
import com.exiro.Object.Player;
import com.exiro.Sprite.Sprite;
import com.exiro.SystemCore.GameManager;
import com.exiro.SystemCore.GameThread;
import com.exiro.Utils.Point;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GameWindow extends JPanel {

    public static int index = 0;
    static int CameraPosx = 0;
    static int CameraPosy = 0;
    Player p;
    GameManager gm;
    Point lastP;
    boolean lastClickState = false; //false : non préssé , true : préssé
    Case lastCase = new Case(0, 0, null, null);

    public GameWindow(GameManager gm) {
        this.gm = gm;
        this.p = gm.getPlayer();
    }

    public void paintComponent(Graphics g) {

        g.setColor(Color.white);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        g.setColor(Color.BLACK);
        int x = 0;
        synchronized (p.getPlayerCities().get(0).getMap().getCases()) {

            for (Case c : p.getPlayerCities().get(0).getMap().getCases()) {

                g.setColor(Color.BLACK);
                if (c.getObject() != null && !c.getObject().isActive()) {
                    g.setColor(Color.RED);
                }

           /* if(c.getBuildingType()==BuildingType.EMPTY)
                continue;
*/

                if (c.isMainCase()) {
                    Point p = IsometricRender.TwoDToIsoTexture(new Point(c.getxPos(), (c.getyPos())), c.getWidth(), c.getHeight(), c.getSize());
                    g.drawImage(c.getImg(), CameraPosx + (int) p.getX(), CameraPosy + (int) p.getY(), null);
                    if (c.getObject() instanceof House) {
                        x++;
                        g.drawString(((House) c.getObject()).getPop() + " " + ((House) c.getObject()).getLevel(), 800, x * 25 + 100);
                    }
                }


                // g.drawRect(c.getxPos()*50,c.getyPos()*50,50,50);

                //System.out.println(c.getBuildingType().name());
            }
        }
        synchronized (p.getPlayerCities().get(0).getSprites()) {
            for (Sprite s : p.getPlayerCities().get(0).getSprites()) {
                Point p = IsometricRender.TwoDToIsoTexture(new Point(s.getX(), (s.getY())), s.getWidth(), s.getHeight(), 1);
                g.drawImage(s.getCurrentFrame(), CameraPosx + (int) p.getX(), CameraPosy + (int) p.getY(), null);
            }
        }
        Point p2 = null;
        if (getMousePosition() != null) {
            try {
                p2 = new Point(getMousePosition().getX(), getMousePosition().getY());
                lastP = p2;
            } catch (NullPointerException r) {
            }
        }
        if (lastP != null) {
            if (lastP.y > this.getHeight() - 30) {
                CameraPosy = CameraPosy - 5;
            } else if (lastP.y < 30) {
                CameraPosy = CameraPosy + 5;
            } else if (lastP.x > this.getWidth() - 30) {
                CameraPosx = CameraPosx - 5;
            } else if (lastP.x < 30) {
                CameraPosx = CameraPosx + 5;
            }
        }

        //Voir si deplacable
        if (p2 != null) {
            Case c = IsometricRender.getCase(p2, p.getPlayerCities().get(0));
            if (MouseManager.pressing) {
                if (lastCase != c) {
                    Boolean isNew = true;
                    for (ObjectClass obj1 : EntityRender.toBuild) {
                        if (obj1.getXB() == c.getxPos() && obj1.getYB() == c.getyPos()) {
                            isNew = false;
                        }
                    }
                    if (isNew) {
                        EntityRender.addBuilding(new Point(c.getxPos(), c.getyPos()));
                    }
                }
            } else if (lastClickState) {//a relaché donc veut construire
                EntityRender.buildAll();
            } else {
                EntityRender.toBuild.get(0).setXB(c.getxPos());
                EntityRender.toBuild.get(0).setYB(c.getyPos());
                EntityRender.x = c.getxPos();
                EntityRender.y = c.getyPos();
            }
            lastCase = c;
        }
        //Voir si deplacable

        if (EntityRender.img != null) {
            for (ObjectClass obj : EntityRender.toBuild) {
                Point p = IsometricRender.TwoDToIsoTexture(new Point(obj.getXB(), obj.getYB()), obj.getWidth(), obj.getHeight(), obj.getSize());
                g.drawImage(EntityRender.img, CameraPosx + (int) p.getX(), CameraPosy + (int) p.getY(), null);
            }
        }


        g.setColor(Color.BLACK);
        g.drawString("argent : " + p.getMoney(), 1200, 20);
        g.drawString("chomeurs :" + p.getPlayerCities().get(0).getBuildingManager().getUnemployed(), 1200, 50);
        g.drawString("Habitant :" + p.getPlayerCities().get(0).getPopulation(), 1200, 80);
        g.drawString("Arrivant :" + p.getPlayerCities().get(0).getPopInArrvial(), 1200, 110);
        g.drawString("FPS " + 1 / GameThread.deltaTime, 1200, 10);
        int i = 0;

        if (!MouseManager.build && MouseManager.pato != null) {
            g.setColor(Color.RED);
            for (Case case1 : MouseManager.pato.getPath()) {

                g.drawRect(case1.getxPos() * 50, case1.getyPos() * 50, 50, 50);
                g.drawString(case1.getBuildingType().name(), case1.getxPos() * 50, case1.getyPos() * 50 + 25);

            }
        }


        i = 0;
      /*  for(Building b : p.getPlayerCities().get(0).getBuildings()) {
            i++;
           // g.drawString(b.toString(), 1200, 140+i*30);
        }*/

        // g.drawRect(10,10,20,20);
        lastClickState = MouseManager.pressing;
    }

    public void draw(CityMap map, ArrayList<Sprite> sprites) {

        for (int x = map.getWidth(); x > 0; x--) {

        }
    }


}
