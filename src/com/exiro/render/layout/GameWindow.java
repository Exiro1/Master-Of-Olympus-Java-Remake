package com.exiro.render.layout;

import com.exiro.buildingList.Building;
import com.exiro.constructionList.Construction;
import com.exiro.environment.Environment;
import com.exiro.object.Case;
import com.exiro.object.CityMap;
import com.exiro.object.ObjectClass;
import com.exiro.object.Player;
import com.exiro.render.EntityRender;
import com.exiro.render.GameFrame;
import com.exiro.render.IsometricRender;
import com.exiro.sprite.Sprite;
import com.exiro.systemCore.GameManager;
import com.exiro.systemCore.GameThread;
import com.exiro.terrainList.Elevation;
import com.exiro.utils.Point;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
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

    public boolean pressing;

    public void draw(CityMap map, ArrayList<Sprite> sprites) {

        for (int x = map.getWidth(); x > 0; x--) {

        }
    }

    Case c1;


    Case startCaseBuild;
    Case lastCaseBuild;
    boolean build;

    public static int getCameraPosx() {
        return CameraPosx;
    }

    public static int getCameraPosy() {
        return CameraPosy;
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
        lastP = null;
        if (MouseInfo.getPointerInfo() != null) {
            try {
                p2 = new Point((float) MouseInfo.getPointerInfo().getLocation().x, (float) MouseInfo.getPointerInfo().getLocation().y);
                lastP = p2;
            } catch (NullPointerException r) {
                r.printStackTrace();
            }
        }
        if (lastP != null) {
            if (lastP.y > GameFrame.FHEIGHT - 2) {
                CameraPosy = CameraPosy - 5;
            } else if (lastP.y < 1) {
                CameraPosy = CameraPosy + 5;
            } else if (lastP.x > GameFrame.FWIDTH - 2) {
                CameraPosx = CameraPosx - 5;
            } else if (lastP.x < 1) {
                CameraPosx = CameraPosx + 5;
            }
        }


        //Voir si deplacable
        buildManager((int) lastP.x, (int) lastP.y);
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

        i = 0;
    }

    public void clickManager(MouseEvent e) {

        if (e.getButton() == MouseEvent.BUTTON1) {

        }
        if (e.getButton() == MouseEvent.BUTTON3) {

        }
    }

    public void buildManager(int x, int y) {


        if (pressing) {
            Case currCase = IsometricRender.getCase(new Point(x, y), gm.getCurrentCity());
            if (startCaseBuild == null) {
                startCaseBuild = currCase;
                EntityRender.setStart(new Point(startCaseBuild.getxPos(), startCaseBuild.getyPos()));
                build = true;
            } else if (lastCaseBuild != currCase) {
                lastCaseBuild = IsometricRender.getCase(new Point(x, y), gm.getCurrentCity());
                EntityRender.addBuilding(new Point(lastCaseBuild.getxPos(), lastCaseBuild.getyPos()));
            }
        } else if (build) {
            EntityRender.buildAll();
            startCaseBuild = null;
            build = false;
        } else {
            Case currCase = IsometricRender.getCase(new Point(x, y), gm.getCurrentCity());
            EntityRender.setStart(new Point(currCase.getxPos(), currCase.getyPos()));
        }
    }

    public boolean isClicked(int xc, int yc) {
        if (xc > getBounds().x && xc < getBounds().x + getBounds().width && yc > getBounds().y && yc < getBounds().y + getBounds().height) {
            return true;
        }
        return false;
    }

    public Player getP() {
        return p;
    }
}
