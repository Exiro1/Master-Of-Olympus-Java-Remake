package com.exiro.render;

import com.exiro.buildingList.Building;
import com.exiro.constructionList.Construction;
import com.exiro.environment.Environment;
import com.exiro.object.Case;
import com.exiro.object.CityMap;
import com.exiro.object.ObjectClass;
import com.exiro.object.Player;
import com.exiro.sprite.Sprite;
import com.exiro.systemCore.GameManager;
import com.exiro.systemCore.GameThread;
import com.exiro.terrainList.Elevation;
import com.exiro.utils.Point;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

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

        ArrayList<ObjectClass> allobj;
        ObjectClass[] oc;

        ArrayList<ObjectClass> terrainObj = new ArrayList<>();

        //rendering Terrain
        synchronized (p.getPlayerCities().get(0).getMap().getCases()) {
            for (Case c : p.getPlayerCities().get(0).getMap().getCases()) {
                if (c.getObject() == null) {
                    if (c.getTerrain().isFloor()) {
                        c.getTerrain().Render(g, CameraPosx, CameraPosy);
                    } else {
                        terrainObj.add(c.getTerrain());
                    }
                } else if (c.getObject() instanceof Environment && c.isMainCase()) {
                    if (((Environment) c.getObject()).isFloor()) {
                        c.getTerrain().Render(g, CameraPosx, CameraPosy);
                        c.getObject().Render(g, CameraPosx, CameraPosy);
                    } else {
                        terrainObj.add(c.getObject());
                    }
                } else if (c.getObject() instanceof Construction) {
                    if (((Construction) c.getObject()).isFloor() && !(c.getTerrain() instanceof Elevation)) {
                        c.getObject().Render(g, CameraPosx, CameraPosy);
                    } else {
                        terrainObj.add(c.getObject());
                    }
                }
            }
        }



        synchronized (p.getPlayerCities().get(0).getBuildings()) {

            allobj = new ArrayList<>(p.getPlayerCities().get(0).getBuildings());

            allobj.addAll(terrainObj);

            synchronized (p.getPlayerCities().get(0).getConstructions()) {
                for (Construction c : p.getPlayerCities().get(0).getConstructions()) {
                    if (!c.isFloor())
                        allobj.add(c);
                }

            }


            for (Building b : p.getPlayerCities().get(0).getBuildings()) {
                synchronized (b.getMovingSprites()) {
                    allobj.addAll(b.getMovingSprites());
                }
            }

        }
        oc = allobj.toArray(new ObjectClass[0]);

        Arrays.sort(oc, (o1, o2) -> Integer.compare(o1.getXB() + o1.getYB(), o2.getXB() + o2.getYB()));
        for (ObjectClass obj : oc) {
            if (obj instanceof Building || obj instanceof Environment || obj instanceof Construction) {
                if (obj.getMainCase().getTerrain() instanceof Elevation)
                    obj.getMainCase().getTerrain().Render(g, CameraPosx, CameraPosy);

                obj.Render(g, CameraPosx, CameraPosy);
            } else {
                obj.Render(g, CameraPosx, CameraPosy);
            }
        }
        /*
        synchronized (p.getPlayerCities().get(0).getSprites()) {
            for (Sprite s : p.getPlayerCities().get(0).getSprites()) {
                Point p = IsometricRender.TwoDToIsoTexture(new Point(s.getX(), (s.getY())), s.getWidth(), s.getHeight(), 1);
                g.drawImage(s.getCurrentFrame(), CameraPosx + (int) p.getX() + s.getOffsetX(), CameraPosy + (int) p.getY() + s.getOffsetY(), null);
            }
        }
        */
        Point p2 = null;
        if (getMousePosition() != null) {
            try {
                p2 = new Point((float) getMousePosition().getX(), (float) getMousePosition().getY());
                lastP = p2;
            } catch (NullPointerException r) {
                r.printStackTrace();
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
                    boolean isNew = true;
                    for (ObjectClass obj1 : EntityRender.toBuild) {
                        if (obj1.getXB() == c.getxPos() && obj1.getYB() == c.getyPos()) {
                            isNew = false;
                            break;
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
                g.drawString(case1.getObject().getBuildingType().name(), case1.getxPos() * 50, case1.getyPos() * 50 + 25);

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
