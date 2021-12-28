package com.exiro.render.layout;

import com.exiro.constructionList.Construction;
import com.exiro.object.*;
import com.exiro.render.ButtonType;
import com.exiro.render.EntityRender;
import com.exiro.render.GameFrame;
import com.exiro.render.IsometricRender;
import com.exiro.render.interfaceList.Interface;
import com.exiro.sprite.Sprite;
import com.exiro.systemCore.GameManager;
import com.exiro.terrainList.Elevation;
import com.exiro.utils.Point;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

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
        CameraPosy = (int) (-(Math.sqrt(2)/2)*gm.getCurrentCity().getMap().getWidth()*30 - getHeight()/2f + 45);
    }

    public boolean pressing;

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


        Case[] oc;
        ArrayList<Case> allcase;

        if (p.getPlayerCities().get(0).getMap().getCaseSorted() == null) {
            synchronized (p.getPlayerCities().get(0).getMap().getCases()) {

                allcase = new ArrayList<>(p.getPlayerCities().get(0).getMap().getNotNullCases());
            }
            oc = allcase.toArray(new Case[0]);
            sortRender(oc);
            p.getPlayerCities().get(0).getMap().setCaseSorted(oc);
        }

        sortRender(p.getPlayerCities().get(0).getMap().getCaseSorted());

        int xvisibleMin = IsometricRender.getCase(new Point(0, 0), gm.getCurrentCity()).getxPos();
        int xvisibleMax = IsometricRender.getCase(new Point(1400, 0), gm.getCurrentCity()).getxPos()+44;
        int yvisibleMin = IsometricRender.getCase(new Point(0, 0), gm.getCurrentCity()).getyPos()-30;
        int yvisibleMax = IsometricRender.getCase(new Point(0, 800), gm.getCurrentCity()).getyPos()+15;

        for (Case obj : p.getPlayerCities().get(0).getMap().getCaseSorted()) {
            if(!(obj.getxPos() <xvisibleMax && obj.getxPos() > xvisibleMin && obj.getyPos() <yvisibleMax && obj.getyPos() >yvisibleMin))
                continue;
            if (obj.getObject() == null)
                obj.getTerrain().Render(g, CameraPosx, CameraPosy);
            if (obj.getObject() != null && (obj.isMainCase())) {
                obj.getObject().Render(g, CameraPosx, CameraPosy);
            }
            if (obj.getTerrain() instanceof Elevation && ((Elevation) obj.getTerrain()).isHasRoad())
                obj.getTerrain().Render(g, CameraPosx, CameraPosy);

            for (Sprite s : obj.getSprites()) {
                s.Render(g, CameraPosx, CameraPosy);
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
            if (lastP.y > GameFrame.FHEIGHT - 2 && CameraPosy > -(Math.sqrt(2)/2)*gm.getCurrentCity().getMap().getWidth()*30 - getHeight()/2f + 45) {
                CameraPosy = CameraPosy - 5;
            } else if (lastP.y < 1 && CameraPosy < -(Math.sqrt(2)/4)*gm.getCurrentCity().getMap().getWidth()*30 -45) {
                CameraPosy = CameraPosy + 5;
            }
            if (lastP.x > GameFrame.FWIDTH - 2 && CameraPosx > -(Math.sqrt(2)/4)*gm.getCurrentCity().getMap().getWidth()*58 + getWidth() +87) {
                CameraPosx = CameraPosx - 5;
            } else if (lastP.x < 1 && CameraPosx < (Math.sqrt(2)/4)*gm.getCurrentCity().getMap().getWidth()*58 -87) {
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
                    if (obj.canBuild) {
                        g.drawImage(EntityRender.tint(EntityRender.img, Color.GREEN), CameraPosx + (int) p.getX(), CameraPosy + (int) p.getY(), null);
                    } else {
                        g.drawImage(EntityRender.tint(EntityRender.img, Color.RED), CameraPosx + (int) p.getX(), CameraPosy + (int) p.getY(), null);
                    }
                }
            }
        }
        //Voir si deplacable


        if (gameInterface != null)
            gameInterface.Render(g);

        g.setColor(Color.BLACK);

        g.drawString("X " + getCameraPosx(), 1200, 10);
        g.drawString("Y " + getCameraPosy(), 1200, 30);

    }

    ObjectClass interfaceCaller;

    public void clickManager(MouseEvent e) {

        if (e.getButton() == MouseEvent.BUTTON1) {
            if (gameInterface != null && gameInterface.isOpen()) {
                ButtonType type = gameInterface.clicked(e.getX(), e.getY() - GameLayout.TOOLBAR_HEIGHT);
                if (type != ButtonType.NONE) {
                    buttonManager(type);
                    if(gameInterface != null && gameInterface.updateRequested())
                        updateInterface();
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
                    interfaceCaller = c.getObject();
                } else {
                    gameInterface = c.getTerrain().getInterface();
                    interfaceCaller = c.getTerrain();
                }
            } else {
                showEntity = false;
            }
        }
    }

    public boolean deleting = false;

    public void sortRender(Case[] a) {
        Arrays.sort(a,
                (o1, o2) -> {

                    return Integer.compare(o1.getxPos() + o1.getyPos(), o2.getxPos() + o2.getyPos()) == 0 ? Integer.compare(o1.getxPos(), o2.getxPos()) : Integer.compare(o1.getxPos() + o1.getyPos(), o2.getxPos() + o2.getyPos());
                });
    }

    public void buttonManager(ButtonType type) {

        showEntity = true;
        gm.getGameView().deleting = false;
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
                EntityRender.setEntityRender(ObjectType.SHEEP);
                break;
            case BREEDING_DAIRY:
                EntityRender.setEntityRender(ObjectType.DAIRY);
                break;
            case BREEDING_GOAT:
                EntityRender.setEntityRender(ObjectType.GOAT);
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
            case INTERFACE_OK:
                showEntity = false;
                break;
            default:
                showEntity = false;
                return;
        }

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

    public void updateInterface(){
        gameInterface = interfaceCaller.getInterface();
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
