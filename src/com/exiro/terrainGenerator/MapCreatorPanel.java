package com.exiro.terrainGenerator;

import com.exiro.fileManager.CaseInfo;
import com.exiro.fileManager.MapSettings;
import com.exiro.fileManager.MapUtils;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

public class MapCreatorPanel extends JPanel{

    float threshold = 40;
    PerlinNoise pn;
    int twoDSpace = 600;
    int gradSize = 200;
    float factor = 1f;
    int twoDSquareSize = 6;
    JSlider[] sliders;
    JTextField seedText;

    public MapCreatorPanel(JSlider[] sliders,JTextField seedt, int seed) {
        factor = ((float)twoDSpace)/((float) gradSize);
        this.sliders = sliders;
        seedText = seedt;
        this.seed = seed;
        seedText.setText(seed+"");
        setSize(1000,1000);
        applyTreshold(null);
    }
    MapSettings settings;
    int[][] map;
    int seed = 32970;
    public void newMap(ActionEvent e)
    {
        Random r = new Random();
        seed = r.nextInt(100000);
        applyTreshold(null);
        seedText.setText(seed+"");
    }
    BufferedImage img;
    public void applyTreshold(ChangeEvent e){
        settings = new MapSettings(new int[]{sliders[0].getValue(), sliders[1].getValue(), sliders[2].getValue(), sliders[3].getValue(), sliders[4].getValue()},sliders[10].getValue(),sliders[11].getValue(),18,sliders[8].getValue(),sliders[9].getValue(),sliders[7].getValue()/100f,sliders[6].getValue(),150);
        map = MapUtils.createMap(seed, settings);
        img = createImg();
        this.repaint();
    }


    public BufferedImage createImg(){

        BufferedImage img = new BufferedImage(map.length*twoDSquareSize,map.length*twoDSquareSize,BufferedImage.TYPE_INT_RGB);
        Graphics g2d = img.getGraphics();


        int demisize = map.length/2;
        for(int l=0;l<map.length;l++) {
            for (int k = 0; k < map.length; k++) {

                if(l<demisize){
                    if(demisize+l < k || demisize-l > k){
                        continue;
                    }
                }else{
                    if(3*demisize-l < k|| -demisize+l >k){
                        continue;
                    }
                }


                int v = map[l][k];
                if(CaseInfo.compareTerrain(v,CaseInfo.WATER)){
                    g2d.setColor(Color.BLUE);
                }else if(CaseInfo.compareTerrain(v,CaseInfo.LVL0)){
                    g2d.setColor(Color.GREEN);
                }else if(CaseInfo.compareTerrain(v,CaseInfo.LVL1)){
                    g2d.setColor(Color.GRAY);
                }else if(CaseInfo.compareTerrain(v,CaseInfo.LVL2)){
                    g2d.setColor(Color.DARK_GRAY);
                }else if(CaseInfo.compareTerrain(v,CaseInfo.LVL3)){
                    g2d.setColor(Color.BLACK);
                }else if(CaseInfo.compareTerrain(v,CaseInfo.LVL4)){
                    g2d.setColor(Color.WHITE);
                }

                g2d.fillRect(k*twoDSquareSize,l*twoDSquareSize,twoDSquareSize,twoDSquareSize);
                if(CaseInfo.compareEnv(v,CaseInfo.FOREST)) {
                    g2d.setColor(Color.BLACK);
                    g2d.drawLine(k * twoDSquareSize, l * twoDSquareSize, (k + 1) * twoDSquareSize, (l + 1) * twoDSquareSize);
                    //g2d.drawLine((k+1) * twoDSquareSize, l * twoDSquareSize, (k) * twoDSquareSize, (l + 1) * twoDSquareSize);
                }

                if(CaseInfo.compareEnv(v,CaseInfo.MEADOW)) {
                    g2d.setColor(Color.MAGENTA);
                    //g2d.drawLine(k * twoDSquareSize, l * twoDSquareSize, (k + 1) * twoDSquareSize, (l + 1) * twoDSquareSize);
                    g2d.drawLine((k + 1) * twoDSquareSize, l * twoDSquareSize, (k) * twoDSquareSize, (l + 1) * twoDSquareSize);
                }

                if(CaseInfo.compareEnv(v,CaseInfo.FISH)) {
                    g2d.setColor(new Color(10, 114, 239));
                    g2d.drawLine(k * twoDSquareSize, l * twoDSquareSize, (k + 1) * twoDSquareSize, (l + 1) * twoDSquareSize);
                    g2d.drawLine((k + 1) * twoDSquareSize, l * twoDSquareSize, (k) * twoDSquareSize, (l + 1) * twoDSquareSize);
                }
                //g2d.setColor(Color.RED);
                //g2d.drawRect(k * twoDSquareSize,l * twoDSquareSize,twoDSquareSize,twoDSquareSize);
            }
        }


        int w = img.getWidth();
        int h = img.getHeight();
        BufferedImage rotated = new BufferedImage(w, h, img.getType());
        Graphics2D graphic = rotated.createGraphics();
        graphic.rotate(Math.toRadians(45), w/2f, h/2f);
        graphic.drawImage(img, null, 0, 0);
        graphic.dispose();

        img = rotated.getSubimage((int) (w-(w/(Math.sqrt(2))))/2+5,(int) (w-w/(Math.sqrt(2)))/2,(int) (w/(Math.sqrt(2))),(int) (w/(Math.sqrt(2))));
        //Image tmp = img.getScaledInstance(getWidth()-20, getWidth()-20, Image.SCALE_SMOOTH);
        //BufferedImage dimg = new BufferedImage(getWidth()-20, getWidth()-20, BufferedImage.TYPE_INT_ARGB);
        //g2d = dimg.createGraphics();
        //g2d.drawImage(tmp, 0, 0, null);
        //g2d.dispose();
        return img;
    }


    private void doDrawing(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        g2d.setPaint(Color.blue);

        int w = getWidth();
        int h = getHeight();

        g.drawImage(img,0,0,null);

        ArrayList<Point> meadow = new ArrayList<>();

    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawing(g);
    }

    public void export(ActionEvent actionEvent) {
        MapSettings.SaveSettings(settings,"Assets/settings.stg");
    }

    public void exportMap(ActionEvent actionEvent) {
        settings.setSeed(seed);
        MapSettings.SaveSettings(settings,"Assets/savedMap.map");
    }

    public void seedChanged(ActionEvent actionEvent) {
        JTextField jTextField = (JTextField) actionEvent.getSource();
        seed = Integer.parseInt(jTextField.getText());
        applyTreshold(null);
    }
}

