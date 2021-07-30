package com.exiro.render;


import com.exiro.moveRelated.FreeState;
import com.exiro.moveRelated.Path;
import com.exiro.object.Case;
import com.exiro.object.CityMap;
import com.exiro.object.ObjectClass;
import com.exiro.object.ObjectType;
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


    public static void setEntityRender(ObjectType type) {
        defaultObject = getDefault(type);
        //img = type.getImg();
        img = defaultObject.getImg();
        heigth = defaultObject.getHeight();
        width = defaultObject.getWidth();
        size = defaultObject.getSize();


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
        obj.setXB(0);
        obj.setYB(0);
        toBuild.add(obj);
    }


    public static ObjectClass getDefault(ObjectType type) {
        return type.getDefault();
    }

    public static void addBuilding(Point p) {
        CityMap map = GameManager.currentCity.getMap();

        Path path = GameManager.currentCity.getPathManager().getPathTo(map.getCase(toBuild.get(0).getXB(), toBuild.get(0).getYB()), map.getCase((int) p.x, (int) p.y), ((FreeState.BUILDABLE.getI()) | FreeState.ALL_ROAD.getI()));
        if (path != null) {
            toBuild.clear();
            for (Case c : path.getPath()) {
                ObjectClass obj = null;
                obj = getDefault(defaultObject.getBuildingType());
                obj.setXB(c.getxPos());
                obj.setYB(c.getyPos());
                toBuild.add(obj);
            }
        }
    }

    public static Point getStartPoint() {
        return new Point(x, y);
    }

    public static void buildAll() {

        for (ObjectClass obj : toBuild) {
            obj.build(obj.getXB(), obj.getYB()); // construit tout
        }
        EntityRender.setEntityRender(defaultObject.getBuildingType());


    }


}
