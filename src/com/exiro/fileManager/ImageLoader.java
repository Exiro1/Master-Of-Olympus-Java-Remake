package com.exiro.fileManager;

import com.exiro.depacking.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;
import java.util.HashMap;

public class ImageLoader {

    static HashMap<Integer, TileImage> AssetsMap;
    static HashMap<Integer, Integer> AssetsMapUse;


    public static void initLoader() {
        AssetsMap = new HashMap<>();
        AssetsMapUse = new HashMap<>();
    }


    public static void unloadImage(String filename, int bitmapID, int imgID) {
        String hash = filename + "" + bitmapID + "" + imgID;
        int globalID = hash.hashCode();
        AssetsMapUse.replace(globalID, AssetsMapUse.get(globalID) - 1);
        if (AssetsMapUse.get(globalID) == 0) {
            AssetsMap.remove(globalID);
            AssetsMapUse.remove(globalID);
        }
    }


    public static TileImage getImage(String filename, int bitmapID, int imgID) {

        String hash = filename + "" + bitmapID + "" + imgID;
        int globalID = hash.hashCode();
        if (!AssetsMap.containsKey(globalID)) {
            sgFile sg = null;
            try {
                sg = new sgFile("Assets/DATA/" + filename + ".sg3");
            } catch (IOException e) {
                e.printStackTrace();
            }
            sgBitmap bitmap = sg.getBitmaps()[bitmapID];
            String filename555 = "Assets/DATA/" + filename + ".555";
            sgImageData sgData = null;
            try {
                sgData = sgImage.sg_load_image_data(bitmap.getImages().get(imgID), filename555);
            } catch (IOException e) {
                e.printStackTrace();
            }
            assert sgData != null;
            if (sgData.getWidth() == 0 || sgData.getHeight() == 0)
                return null;
            //BufferedImage imgg = new BufferedImage(sgData.getWidth(),sgData.getHeight(), BufferedImage.TYPE_INT_ARGB);
            GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
            GraphicsDevice device = env.getDefaultScreenDevice();
            GraphicsConfiguration config = device.getDefaultConfiguration();
            BufferedImage img = config.createCompatibleImage(sgData.getWidth(), sgData.getHeight(), BufferedImage.TYPE_INT_ARGB);

            BufferedImage imgg = config.createCompatibleImage(sgData.getWidth(), sgData.getHeight(), Transparency.BITMASK);
            final int[] a = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();
            System.arraycopy(sgData.getData(), 0, a, 0, sgData.getData().length);
            //imgg.getGraphics().drawImage(img,0,0,null);
            Graphics g = imgg.createGraphics();
            g.drawImage(img, 0, 0, null);
            g.dispose();
            sgData.setData(null);
            AssetsMap.put(globalID, new TileImage(imgg, sgData.getWidth(), sgData.getHeight(), bitmapID, imgID));
            AssetsMapUse.put(globalID, 1);
        } else {
            AssetsMapUse.replace(globalID, AssetsMapUse.get(globalID) + 1);
        }
        return AssetsMap.get(globalID);
    }


    //Ne pas faire (utilise beaucoup trop de place)
    /*
    public static void loadAll() throws IOException {
        imgDir = new ArrayList<>();
        sgFile sg = new sgFile("G:\\SteamLibrary\\steamapps\\common\\Zeus + Poseidon\\DATA\\Zeus_Terrain.sg3");
        int bitmaps = sg.getBitmaps_n();
        int numimg = sg.getImages_n();
        int i =0;
        int total = 0;
        for (; i < bitmaps; i++) {
            sgBitmap bitmap = sg.getBitmaps()[i];
            int images = bitmap.getImages_n();

            String filename = "G:\\SteamLibrary\\steamapps\\common\\Zeus + Poseidon\\DATA\\Zeus_Terrain.555";

            imgDir.add(new ImageDirectory(bitmap.getBitmapId(),bitmap.getImages_n(),filename));

            for (int n = 0; n < images; n++) {
                sgImageData sgData = sgImage.sg_load_image_data(bitmap.getImages().get(n),filename);
                System.out.println(sgData.getHeight()+"*"+sgData.getWidth()+"\n");
                if(sgData.getWidth()==0 || sgData.getHeight()==0)
                    continue;
                BufferedImage imgg = new BufferedImage(sgData.getWidth(),sgData.getHeight(), BufferedImage.TYPE_INT_ARGB);
                //int[] outputImagePixelData = ((DataBufferInt) imgg.getRaster().getDataBuffer()).getData() ;
                final int[] a = ( (DataBufferInt)imgg.getRaster().getDataBuffer() ).getData();
                System.arraycopy(sgData.getData(), 0, a, 0, sgData.getData().length);
                imgDir.get(imgDir.size()-1).addImage(new TileImage(imgg,sgData.getWidth(),sgData.getHeight(),n, TileImage.ImageType.ZEUS_SYSTEM));
            }
        }
    }
    */


}
