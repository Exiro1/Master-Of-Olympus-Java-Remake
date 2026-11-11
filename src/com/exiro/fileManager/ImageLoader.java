package com.exiro.fileManager;

import com.exiro.reader.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

public class ImageLoader {

    // Keyed by "file|bitmap|image". The old key concatenated the three values with no separator
    // and used String.hashCode(), so e.g. (SprMain, 1, 23) and (SprMain, 12, 3) collided.
    static HashMap<String, TileImage> AssetsMap;
    static HashMap<String, Integer> AssetsMapUse;
    // Parsed .sg3 indexes, one per file. Parsing the index used to happen on every cache miss.
    static HashMap<String, sgFile> sgFiles;
    // Images that exist in the index but are 0x0; remembered so we don't reload them every call.
    static HashSet<String> emptyImages;

    public enum FilePath {ZEUS_GENERAL("Zeus_General"), SPR_MAIN("SprMain"), SPR_AMBIENT("SprAmbient"), ZEUS_TERRAIN("Zeus_Terrain"),ZEUS_INTERFACE("Zeus_Interface");
        String path;
        FilePath(String path) {
            this.path = path;
        }
        public String getPath() {
            return path;
        }
    }

    public static void initLoader() {
        AssetsMap = new HashMap<>();
        AssetsMapUse = new HashMap<>();
        sgFiles = new HashMap<>();
        emptyImages = new HashSet<>();
    }

    private static GraphicsConfiguration config;

    private static GraphicsConfiguration config() {
        if (config == null)
            config = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
        return config;
    }

    private static String key(String filename, int bitmapID, int imgID) {
        return filename + '|' + bitmapID + '|' + imgID;
    }

    private static sgFile getSgFile(String filename) throws IOException {
        sgFile sg = sgFiles.get(filename);
        if (sg == null) {
            sg = new sgFile("Assets/DATA/" + filename + ".sg3");
            sgFiles.put(filename, sg);
        }
        return sg;
    }

    public static void unloadImage(String filename, int bitmapID, int imgID) {
        String globalID = key(filename, bitmapID, imgID);
        Integer uses = AssetsMapUse.get(globalID);
        if (uses == null)
            return;
        if (uses <= 1) {
            AssetsMap.remove(globalID);
            AssetsMapUse.remove(globalID);
        } else {
            AssetsMapUse.put(globalID, uses - 1);
        }
    }
    public static TileImage getImage(FilePath filePath, int bitmapID, int imgID) {
        return getImage(filePath.getPath(),bitmapID,imgID);
    }

    public static TileImage getImage(String filename, int bitmapID, int imgID) {

        String globalID = key(filename, bitmapID, imgID);
        if (emptyImages.contains(globalID))
            return null;
        if (!AssetsMap.containsKey(globalID)) {
            sgFile sg;
            try {
                sg = getSgFile(filename);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            sgBitmap bitmap = sg.getBitmaps()[bitmapID];

            String filename555 = "Assets/DATA/" + filename + ".555";
            sgImageData sgData = null;
            try {
                sgData = SgImageReader.loadImageData(bitmap.getImages().get(imgID), filename555);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (sgData == null)
                return null;
            if (sgData.getWidth() == 0 || sgData.getHeight() == 0) {
                emptyImages.add(globalID);
                return null;
            }
            BufferedImage img = new BufferedImage(sgData.getWidth(), sgData.getHeight(), BufferedImage.TYPE_INT_ARGB);
            final int[] a = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();
            System.arraycopy(sgData.getData(), 0, a, 0, sgData.getData().length);
            // Pure red is the key colour. It used to become alpha 0x33 instead of 0, which turned
            // every image translucent, so each tile was alpha-blended in software every frame.
            for (int i = 0; i < a.length; i++)
                if ((a[i] & 0xFFFFFF) == 0xFF0000)
                    a[i] = 0;
            // Copy into an image in the screen's format with 1-bit transparency, which Java2D can
            // cache in video memory and draw without blending.
            BufferedImage imgg = config().createCompatibleImage(sgData.getWidth(), sgData.getHeight(), Transparency.BITMASK);
            Graphics2D g = imgg.createGraphics();
            g.setComposite(AlphaComposite.Src);
            g.drawImage(img, 0, 0, null);
            g.dispose();
            sgData.setData(null);
            AssetsMap.put(globalID, new TileImage(imgg, sgData.getWidth(), sgData.getHeight(), bitmapID, imgID, bitmap.getImages().get(imgID).getRecord().getOffx(), bitmap.getImages().get(imgID).getRecord().getOffy()));
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
