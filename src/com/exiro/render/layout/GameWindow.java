package com.exiro.render.layout;

import com.exiro.buildingList.Building;
import com.exiro.constructionList.Construction;
import com.exiro.constructionList.Road;
import com.exiro.object.*;
import com.exiro.render.ButtonType;
import com.exiro.render.EntityRender;
import com.exiro.render.GameFrame;
import com.exiro.render.IsometricRender;
import com.exiro.render.interfaceList.Interface;
import com.exiro.sprite.MovingSprite;
import com.exiro.sprite.Sprite;
import com.exiro.systemCore.GameManager;
import com.exiro.terrainList.Elevation;
import com.exiro.terrainList.Terrain;
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

    public boolean showEntity = true;
    Interface gameInterface;

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
                    //if (c.getTerrain().isFloor()) {
                    //     c.getTerrain().Render(g, CameraPosx, CameraPosy);
                    // } else {
                    terrainObj.add(c.getTerrain());
                    // }
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
                    //if (!c.isFloor())
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
        sortRender(oc);

        for (ObjectClass obj : oc) {
            if (obj instanceof Building || obj instanceof Construction) {
                if (obj.getMainCase().getTerrain() instanceof Elevation) {
                    obj.getMainCase().getTerrain().Render(g, CameraPosx, CameraPosy);
                    if (obj instanceof Road)
                        continue;
                }
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
            }
            if (lastP.x > GameFrame.FWIDTH - 2) {
                CameraPosx = CameraPosx - 5;
            } else if (lastP.x < 1) {
                CameraPosx = CameraPosx + 5;
            }
        }


        //Voir si deplacable
        if (showEntity) {
            buildManager((int) lastP.x, (int) lastP.y);
            if (EntityRender.img != null) {
                for (ObjectClass obj : EntityRender.toBuild) {
                    int lvl = 0;
                    if (gm.getCurrentCity().getMap().getCase(obj.getXB(), obj.getYB()) != null)
                        lvl = gm.getCurrentCity().getMap().getCase(obj.getXB(), obj.getYB()).getZlvl();
                    Point p = IsometricRender.TwoDToIsoTexture(new Point(obj.getXB() - lvl, obj.getYB() - lvl), obj.getWidth(), obj.getHeight(), obj.getSize());
                    g.drawImage(EntityRender.img, CameraPosx + (int) p.getX(), CameraPosy + (int) p.getY(), null);
                }
            }
        }
        //Voir si deplacable


        if (gameInterface != null)
            gameInterface.Render(g);

        /*
        g.setColor(Color.BLACK);
        g.drawString("argent : " + p.getMoney(), 1200, 20);
        g.drawString("chomeurs :" + p.getPlayerCities().get(0).getBuildingManager().getUnemployed(), 1200, 50);
        g.drawString("Habitant :" + p.getPlayerCities().get(0).getPopulation(), 1200, 80);
        g.drawString("Arrivant :" + p.getPlayerCities().get(0).getPopInArrvial(), 1200, 110);
        g.drawString("FPS " + 1 / GameThread.deltaTime, 1200, 10);
         */
    }

    public void clickManager(MouseEvent e) {

        if (e.getButton() == MouseEvent.BUTTON1) {
            if (gameInterface != null && gameInterface.isOpen()) {
                ButtonType type = gameInterface.clicked(e.getX(), e.getY() - GameLayout.TOOLBAR_HEIGHT);
                if (type != ButtonType.NONE) {
                    buttonManager(type);
                }
            } else {

            }
        }
        if (e.getButton() == MouseEvent.BUTTON3) {
            if (gameInterface != null) {
                gameInterface.close();
                gameInterface = null;
            } else if (!showEntity) {
                Case c = IsometricRender.getCase(new Point(MouseInfo.getPointerInfo().getLocation().x, MouseInfo.getPointerInfo().getLocation().y), gm.getCurrentCity());
                if (c.getObject() != null) {
                    gameInterface = c.getObject().getInterface();
                } else {
                    gameInterface = c.getTerrain().getInterface();
                }
            } else {
                showEntity = false;
            }
        }
    }

    public boolean deleting = false;

    public void sortRender(ObjectClass[] a) {
        Arrays.sort(a,
                (o1, o2) -> {
                    if (o1 instanceof MovingSprite && ((o2 instanceof Construction && ((Construction) o2).isFloor()) || (o2 instanceof Terrain && ((Terrain) o2).isFloor()))) {
                        if (Math.pow(((MovingSprite) o1).getX() - (o2.getXB() - 0.5), 2) + Math.pow(((MovingSprite) o1).getY() - (o2.getYB() - 0.5), 2) <= 3)
                            return 1;
                    } else if (o2 instanceof MovingSprite && ((o1 instanceof Construction && ((Construction) o1).isFloor()) || (o1 instanceof Terrain && ((Terrain) o1).isFloor()))) {
                        if (Math.pow((o1.getXB() - 0.5) - ((MovingSprite) o2).getX(), 2) + Math.pow((o1.getYB() - 0.5) - ((MovingSprite) o2).getY(), 2) <= 3)
                            return -1;
                    }
                    return Integer.compare(o1.getXB() + o1.getYB(), o2.getXB() + o2.getYB());
                });
    }

    public void buttonManager(ButtonType type) {
        switch (type) {
            case CULTURE_GYMNASIUM:
                EntityRender.setEntityRender(ObjectType.GYMNASIUM);
                break;
            case FARM_WHEAT:
                EntityRender.setEntityRender(ObjectType.FARM, Resource.CORN);
                break;
            case FARM_CARROT:
                EntityRender.setEntityRender(ObjectType.FARM, Resource.CARROT);
                break;
            case FARM_ONION:
                EntityRender.setEntityRender(ObjectType.FARM, Resource.ONION);
                break;
            case BREEDING_SHEEPHOLD:
                EntityRender.setEntityRender(ObjectType.SHEEPFOLD);
                break;
            case BREEDING_SHEEP:
                //EntityRender.setEntityRender(ObjectType.FARM);
                break;
            case BREEDING_DAIRY:
                EntityRender.setEntityRender(ObjectType.DAIRY);
                break;
            case BREEDING_GOAT:
                //EntityRender.setEntityRender(ObjectType.FARM);
                break;
            case FISHING_FISHERY:
                EntityRender.setEntityRender(ObjectType.FISHERY);
                break;
            case FISHING_HUNTING:
                EntityRender.setEntityRender(ObjectType.HUNTINGHOUSE);
                break;
            case VITICULTURE_SMALLHOLDING:
                EntityRender.setEntityRender(ObjectType.SMALLHOLDING);
                break;
            case VITICULTURE_OLIVETREE:
                EntityRender.setEntityRender(ObjectType.OLIVETREE);
                break;
            case VITICULTURE_GRAPE:
                //EntityRender.setEntityRender(ObjectType.FARM);
                break;
            case AGORA_AGORA:
                EntityRender.setEntityRender(ObjectType.AGORA);
                break;
            case AGORA_FOOD:
                EntityRender.setEntityRender(ObjectType.AGORAFOOD);
                break;
            case AGORA_WOOL:
                EntityRender.setEntityRender(ObjectType.AGORAWOOL);
                break;
            case AGORA_OIL:
                EntityRender.setEntityRender(ObjectType.AGORAOIL);
                break;
            case INDUSTRY_FOUNDRY:
                EntityRender.setEntityRender(ObjectType.FOUNDRY);
                break;
            case INDUSTRY_GUILD:
                break;
            case INDUSTRY_MARBLE:
                EntityRender.setEntityRender(ObjectType.MARBLE_QUARRY);
                break;
            case INDUSTRY_MINT:
                EntityRender.setEntityRender(ObjectType.MINT);
                break;
            case INDUSTRY_OLIVE:
                EntityRender.setEntityRender(ObjectType.OLIVE_PRESS);
                break;
            case INDUSTRY_SAWMILL:
                EntityRender.setEntityRender(ObjectType.SAWMILL);
                break;
            case INDUSTRY_SCULPTURE:
                EntityRender.setEntityRender(ObjectType.SCULPTURE_STUDIO);
                break;
            case INDUSTRY_WINERY:
                EntityRender.setEntityRender(ObjectType.WINERY);
                break;
            case CULTURE_PODIUM:
                EntityRender.setEntityRender(ObjectType.PODIUM);
                break;
            case CULTURE_COLLEGE:
                EntityRender.setEntityRender(ObjectType.COLLEGE);
                break;
            case CULTURE_THEATER:
                EntityRender.setEntityRender(ObjectType.THEATER);
                break;
            case CULTURE_SCHOOLTHEATER:
                EntityRender.setEntityRender(ObjectType.THEATERSCHOOL);
                break;
        }
        showEntity = true;
        gm.getGameView().deleting = false;
        gameInterface.close();
        gameInterface = null;
    }

    public void buildManager(int x, int y) {
        if (isClicked(x, y)) {
            if (gameInterface == null || !gameInterface.isOpen()) {
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
                    if (deleting) {
                        Case currCase = IsometricRender.getCase(new Point(x, y), gm.getCurrentCity());
                        EntityRender.EraseAll(currCase);
                    }
                    if (!deleting)
                        EntityRender.buildAll();
                    startCaseBuild = null;
                    build = false;
                } else {
                    Case currCase = IsometricRender.getCase(new Point(x, y), gm.getCurrentCity());
                    EntityRender.setStart(new Point(currCase.getxPos(), currCase.getyPos()));
                }
            }
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
