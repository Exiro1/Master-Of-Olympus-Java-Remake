package com.exiro.BuildingList;

import com.exiro.Object.Case;
import com.exiro.Object.City;
import com.exiro.Object.Ressource;
import com.exiro.SystemCore.GameManager;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Stock extends Building {

    static BufferedImage imgSet;
    Map<Ressource, Integer> stock;
    Map<Ressource, BufferedImage> listImage;

    static public Stock DEFAULT() {
        return new Stock(0, 0, GameManager.currentCity);
    }

    public Stock(boolean isActive, BuildingType type, String path, int width, int height, int size, BuildingCategory category, int pop, int popMax, int cost, int deleteCost, int xPos, int yPos, int yLenght, int xLenght, ArrayList<Case> cases, boolean built, City city, int ID, Map<Ressource, Integer> stock) {
        super(isActive, type, path, width, height, size, category, pop, popMax, cost, deleteCost, xPos, yPos, yLenght, xLenght, cases, built, city, ID);
        this.stock = stock;
    }

    public Stock(int xPos, int yPos, City c) {
        super(false, BuildingType.STOCK, "Assets/Building/Stock/StorInactive.png", 58, 114, 1, BuildingCategory.STOCKAGE, 0, 2, 150, 30, xPos, yPos, 3, 3, new ArrayList<>(), false, c, 0);
        listImage = new HashMap<>();

        int imagesCount = 66;
        BufferedImage images[] = new BufferedImage[imagesCount];
        int i = 0;
        int j = 0;
        int k = -1;
        int k2 = 0;
        for (int a = 0; a < images.length; a++) {
            if (i > 15) {
                i = 0;
                j++;
            }
            images[j] = imgSet.getSubimage(i * 58, j * 116, 58, 116);
            k2++;
            if (k2 > 3) {
                k++;
                k2 = 0;
            }
            if (k == 15 && k2 == 1)
                k = 16;
            Ressource rc = null;
            switch (k) {
                case -1:
                    rc = Ressource.SEA_URCHIN;
                    break;
                case 0:
                    rc = Ressource.FISH;
                    break;
                case 1:
                    rc = Ressource.MEAT;
                    break;
                case 2:
                    rc = Ressource.CHEESE;
                    break;
                case 3:
                    rc = Ressource.CARROT;
                    break;
                case 4:
                    rc = Ressource.ONION;
                    break;
                case 5:
                    rc = Ressource.CORN;
                    break;
                case 6:
                    rc = Ressource.WOOD;
                    break;
                case 7:
                    rc = Ressource.BRONZE;
                    break;
                case 8:
                    rc = Ressource.MARBLE;
                    break;
                case 9:
                    rc = Ressource.GRAPE;
                    break;
                case 10:
                    rc = Ressource.OLIVE;
                    break;
                case 11:
                    rc = Ressource.WOOL;
                    break;
                case 12:
                    rc = Ressource.ARMEMENT;
                    break;
                case 13:
                    rc = Ressource.OLIVE_OIL;
                    break;
                case 14:
                    rc = Ressource.WINE;
                    break;
                case 15:
                    rc = Ressource.SCULPTURE;
                    break;
                case 16:
                    rc = Ressource.NULL;
                    break;
                default:
                    break;

            }
            listImage.put(rc, images[j]);
            i++;
        }

    }

    public static void loadSet() {
        try {
            File imgFile = new File("Assets/Building/Stock/stock.png");
            imgSet = ImageIO.read(imgFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean build(int xPos, int yPos) {
        if (super.build(xPos, yPos)) {
            for (Case c2 : cases) {
                c2.setImg(listImage.get(Ressource.CARROT));
                c2.setMainCase(true);
                c2.setWidth(58);
                c2.setHeight(116);
                c2.setSize(1);
            }
            cases.get(6).setImg(getImg());
            cases.get(6).setWidth(58);
            cases.get(6).setHeight(76);
            return true;
        }
        return false;
    }


    @Override
    public void process(double deltaTime) {

    }

    @Override
    public void populate(double deltaTime) {

    }

    @Override
    void addPopulation() {

    }

    @Override
    public BufferedImage getRender() {




       /* BufferedImage concatImage = new BufferedImage(174, 176, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = concatImage.createGraphics();
        for(Case c : cases) {
            Point p = IsometricRender.TwoDToIsoTexture(new Point(((c.getxPos()-xPos))+getSize()-1,(c.getyPos()-yPos)),58,36,1);
            g2d.drawImage(listImage.get(Ressource.NULL),p.x-getSize()*29,p.y,null);
        }
        g2d.dispose();
        try {
            File f = new File("C:\\Users\\corentin\\OneDrive - ESTACA\\Documents\\dev\\JavaGame\\GameJava\\src\\other\\testCase.png");
           ImageIO.write(concatImage, "png", f); // export concat image
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        // getImg();

        return getImg();
    }
}
