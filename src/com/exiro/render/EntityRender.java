package com.exiro.render;


import com.exiro.buildingList.ResourceGenerator;
import com.exiro.moveRelated.FreeState;
import com.exiro.moveRelated.Path;
import com.exiro.object.*;
import com.exiro.systemCore.GameManager;
import com.exiro.utils.Point;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

//dessine le batiment selectionner pour construire
public class EntityRender {


    public static ArrayList<ObjectClass> toBuild = new ArrayList<>();
    public static BufferedImage img;
    public static int x, y, width, heigth, size;
    public static ObjectType Btype;
    static public ObjectClass defaultObject;
    static int startX, startY;
    static Resource resource;

    public static void setStart(Point p) {
        startX = (int) p.x;
        startY = (int) p.y;
        toBuild.get(0).setXB(startX);
        toBuild.get(0).setYB(startY);
        ArrayList<Case> place;
        place = defaultObject.getPlace(startX, startY, defaultObject.getSize(), defaultObject.getSize(), GameManager.currentCity);
        if (place == null) {
            toBuild.get(0).canBuild = false;
        } else {
            toBuild.get(0).canBuild = place.size() == defaultObject.getXlen() * defaultObject.getYlen();
        }
    }

    public static void setEntityRender(ObjectType type, Resource r) {
        setEntityRender(type);
        resource = r;
    }

    public static void setEntityRender(ObjectType type) {
        defaultObject = getDefault(type);
        //img = type.getImg();
        img = defaultObject.getImg();
        heigth = defaultObject.getHeight();
        width = defaultObject.getWidth();
        size = defaultObject.getSize();

        resource = Resource.NULL;


        BufferedImage target = new BufferedImage(img.getWidth(), img.getHeight(), Transparency.TRANSLUCENT);

        // Get the images graphics
        Graphics2D g = target.createGraphics();
        // Set the Graphics composite to Alpha
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) 0.6));

        // Draw the image into the prepared reciver image
        g.drawImage(img, 0, 0, null);

        // let go of all system resources in this Graphics
        g.dispose();
        // Return the image
        img = target;
        toBuild.clear();
        Btype = type;
        //Exemple

        ObjectClass obj = null;
        obj = getDefault(defaultObject.getBuildingType());
        obj.setXB(-100);
        obj.setYB(-100);
        toBuild.add(obj);
    }


    public static void addBuilding(Point p) {
        CityMap map = GameManager.currentCity.getMap();
        if (Btype == ObjectType.ROAD) {
            Path path = GameManager.currentCity.getPathManager().getPathTo(map.getCase(startX, startY), map.getCase((int) p.x, (int) p.y), (FreeState.ALL_ROAD.getI() | FreeState.BUILDABLE_ROAD.getI()));
            if (path != null) {
                toBuild.clear();
                for (Case c : path.getPath()) {
                    ObjectClass obj = null;
                    obj = getDefault(defaultObject.getBuildingType());
                    obj.setXB(c.getxPos());
                    obj.setYB(c.getyPos());
                    obj.canBuild = true;
                    toBuild.add(obj);
                }
            }
        } else {
            toBuild.clear();
            if (startX < p.getX()) {
                for (int i = startX; i < p.getX(); i += defaultObject.getSize()) {
                    if (startY < p.getY()) {
                        for (int j = startY; j < p.getY(); j += defaultObject.getSize()) {
                            Case c = map.getCase(i, j);
                            if(c == null)
                                continue;
                            ArrayList<Case> place;
                            place = defaultObject.getPlace(i, j, defaultObject.getSize(), defaultObject.getSize(), GameManager.currentCity);
                            if (place.size() == defaultObject.getSize() * defaultObject.getSize()) {
                                ObjectClass obj = null;
                                obj = getDefault(defaultObject.getBuildingType());
                                if (resource != Resource.NULL && obj instanceof ResourceGenerator) {
                                    ((ResourceGenerator) obj).setResource(resource);
                                }
                                obj.canBuild = true;
                                obj.setXB(c.getxPos());
                                obj.setYB(c.getyPos());
                                toBuild.add(obj);
                            } else {
                                ObjectClass obj = null;
                                obj = getDefault(defaultObject.getBuildingType());
                                if (resource != Resource.NULL && obj instanceof ResourceGenerator) {
                                    ((ResourceGenerator) obj).setResource(resource);
                                }
                                obj.setXB(c.getxPos());
                                obj.setYB(c.getyPos());
                                obj.canBuild = false;
                                toBuild.add(obj);
                            }
                            /*
                            if (!c.isOccuped() && c.getTerrain().isConstructible()) {
                                ObjectClass obj = null;
                                obj = getDefault(defaultObject.getBuildingType());
                                if (resource != Resource.NULL && obj instanceof ResourceGenerator) {
                                    ((ResourceGenerator) obj).setResource(resource);
                                }
                                obj.setXB(c.getxPos());
                                obj.setYB(c.getyPos());
                                toBuild.add(obj);
                            }*/

                        }
                    } else {
                        for (int j = (int) p.getY(); j < startY; j += defaultObject.getSize()) {
                            Case c = map.getCase(i, j);

                            ArrayList<Case> place;
                            place = defaultObject.getPlace(i, j, defaultObject.getSize(), defaultObject.getSize(), GameManager.currentCity);
                            if (place.size() == defaultObject.getSize() * defaultObject.getSize()) {
                                ObjectClass obj = null;
                                obj = getDefault(defaultObject.getBuildingType());
                                if (resource != Resource.NULL && obj instanceof ResourceGenerator) {
                                    ((ResourceGenerator) obj).setResource(resource);
                                }
                                obj.setXB(c.getxPos());
                                obj.setYB(c.getyPos());
                                obj.canBuild = true;
                                toBuild.add(obj);
                            } else {
                                ObjectClass obj = null;
                                obj = getDefault(defaultObject.getBuildingType());
                                if (resource != Resource.NULL && obj instanceof ResourceGenerator) {
                                    ((ResourceGenerator) obj).setResource(resource);
                                }
                                obj.setXB(c.getxPos());
                                obj.setYB(c.getyPos());
                                obj.canBuild = false;
                                toBuild.add(obj);
                            }
                            /*
                            if (!c.isOccuped() && c.getTerrain().isConstructible()) {
                                ObjectClass obj = null;
                                obj = getDefault(defaultObject.getBuildingType());
                                if (resource != Resource.NULL && obj instanceof ResourceGenerator) {
                                    ((ResourceGenerator) obj).setResource(resource);
                                }
                                obj.setXB(c.getxPos());
                                obj.setYB(c.getyPos());
                                toBuild.add(obj);
                            }*/
                        }
                    }
                }
            } else {
                for (int i = (int) p.getX(); i < startX; i += defaultObject.getSize()) {
                    if (startY < p.getY()) {
                        for (int j = startY; j < p.getY(); j += defaultObject.getSize()) {
                            Case c = map.getCase(i, j);
                            ArrayList<Case> place;
                            place = defaultObject.getPlace(i, j, defaultObject.getSize(), defaultObject.getSize(), GameManager.currentCity);
                            if (place.size() == defaultObject.getSize() * defaultObject.getSize()) {
                                ObjectClass obj = null;
                                obj = getDefault(defaultObject.getBuildingType());
                                if (resource != Resource.NULL && obj instanceof ResourceGenerator) {
                                    ((ResourceGenerator) obj).setResource(resource);
                                }
                                obj.setXB(c.getxPos());
                                obj.setYB(c.getyPos());
                                obj.canBuild = true;
                                toBuild.add(obj);
                            } else {
                                ObjectClass obj = null;
                                obj = getDefault(defaultObject.getBuildingType());
                                if (resource != Resource.NULL && obj instanceof ResourceGenerator) {
                                    ((ResourceGenerator) obj).setResource(resource);
                                }
                                obj.setXB(c.getxPos());
                                obj.setYB(c.getyPos());
                                obj.canBuild = false;
                                toBuild.add(obj);
                            }
                        }
                    } else {
                        for (int j = (int) p.getY(); j < startY; j += defaultObject.getSize()) {
                            Case c = map.getCase(i, j);
                            ArrayList<Case> place;
                            place = defaultObject.getPlace(i, j, defaultObject.getSize(), defaultObject.getSize(), GameManager.currentCity);
                            if (place.size() == defaultObject.getSize() * defaultObject.getSize()) {
                                ObjectClass obj = null;
                                obj = getDefault(defaultObject.getBuildingType());
                                if (resource != Resource.NULL && obj instanceof ResourceGenerator) {
                                    ((ResourceGenerator) obj).setResource(resource);
                                }
                                obj.canBuild = true;
                                obj.setXB(c.getxPos());
                                obj.setYB(c.getyPos());
                                toBuild.add(obj);
                            } else {
                                ObjectClass obj = null;
                                obj = getDefault(defaultObject.getBuildingType());
                                if (resource != Resource.NULL && obj instanceof ResourceGenerator) {
                                    ((ResourceGenerator) obj).setResource(resource);
                                }
                                obj.setXB(c.getxPos());
                                obj.setYB(c.getyPos());
                                obj.canBuild = false;
                                toBuild.add(obj);
                            }
                        }
                    }

                }
            }
        }

    }

    public static BufferedImage tint(BufferedImage image, Color color) {

        BufferedImage bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), Transparency.TRANSLUCENT);
        bimage.getGraphics().drawImage(image, 0, 0, null);

        for (int x = 0; x < bimage.getWidth(); x++) {
            for (int y = 0; y < bimage.getHeight(); y++) {
                Color pixelColor = new Color(bimage.getRGB(x, y), true);
                int r = (pixelColor.getRed() + color.getRed()) / 2;
                int g = (pixelColor.getGreen() + color.getGreen()) / 2;
                int b = (pixelColor.getBlue() + color.getBlue()) / 2;
                int a = pixelColor.getAlpha();
                int rgba = (a << 24) | (r << 16) | (g << 8) | b;
                bimage.setRGB(x, y, rgba);
            }
        }
        return bimage;
    }

    public static ObjectClass getDefault(ObjectType type) {
        return type.getDefault();
    }

    public static Point getStartPoint() {
        return new Point(x, y);
    }


    public static void EraseAll(Case currCase) {


        Case cs = GameManager.currentCity.getMap().getCase(startX, startY);
        if (cs.getObject() != null) {
            cs.getObject().delete();
        }
        for (int i = (int) Math.min(startX, currCase.getxPos()); i < Math.max(startX, currCase.getxPos()); i += defaultObject.getSize()) {
            for (int j = Math.min(startY, currCase.getyPos()); j < Math.max(startY, currCase.getyPos()); j += defaultObject.getSize()) {
                Case c = GameManager.currentCity.getMap().getCase(i, j);
                if (c != null && c.getObject() != null) {
                    c.getObject().delete();
                }
            }
        }
        EntityRender.setEntityRender(defaultObject.getBuildingType());
    }

    public static void buildAll() {

        if (toBuild.size() == 0) {
            Case c = GameManager.currentCity.getMap().getCase(startX, startY);
            if(c != null) {
                ObjectClass obj = null;
                obj = getDefault(defaultObject.getBuildingType());
                if (resource != Resource.NULL && obj instanceof ResourceGenerator) {
                    ((ResourceGenerator) obj).setResource(resource);
                }
                obj.setXB(c.getxPos());
                obj.setYB(c.getyPos());
                toBuild.add(obj);
            }
        }

        for (ObjectClass obj : toBuild) {
            obj.build(obj.getXB(), obj.getYB()); // construit tout
        }
        EntityRender.setEntityRender(defaultObject.getBuildingType());


    }

    public void setStart(int x, int y) {
        startX = x;
        startY = y;
    }


}
