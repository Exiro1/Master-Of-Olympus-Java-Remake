package com.exiro.terrainGenerator;

import com.exiro.fileManager.MapSettings;

import javax.swing.*;
import java.awt.*;

public class MapCreatorFrame extends JFrame {

    public MapCreatorFrame() {
        initUI();
    }

    private void initUI() {



        setTitle("Points");
        setSize(1300, 1000);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        setLayout(new FlowLayout());


        MapSettings mapSettings = MapSettings.loadSettings("Assets/savedMap.map");


        JPanel sliderp = new JPanel();
        JLabel j1 = new JLabel("Water End",SwingConstants.CENTER);
        JSlider s1 = new JSlider(0,100,mapSettings.getThreshold()[0]);
        s1.setPreferredSize(new Dimension( 300, 10 ));
        JLabel j2 = new JLabel("Level 0 End",SwingConstants.CENTER);
        JSlider s2 = new JSlider(0,100,mapSettings.getThreshold()[1]);
        JLabel j3 = new JLabel("Level 1 End",SwingConstants.CENTER);
        JSlider s3 = new JSlider(0,100,mapSettings.getThreshold()[2]);
        JLabel j4 = new JLabel("Level 2 End",SwingConstants.CENTER);
        JSlider s4 = new JSlider(0,100,mapSettings.getThreshold()[3]);
        JLabel j5 = new JLabel("Other",SwingConstants.CENTER);
        JSlider s5 = new JSlider(0,100,mapSettings.getThreshold()[4]);

        JLabel j6 = new JLabel("Perlin Noise size",SwingConstants.CENTER);
        JSlider s6 = new JSlider(0,300,50);
        JLabel j7 = new JLabel("Resolution",SwingConstants.CENTER);
        JSlider s7 = new JSlider(0,30,mapSettings.getResolution());
        JLabel j8 = new JLabel("Factor",SwingConstants.CENTER);
        JSlider s8 = new JSlider(0,300, (int) (mapSettings.getFactor()*100));
        JLabel j9 = new JLabel("Forest Start",SwingConstants.CENTER);
        JSlider s9 = new JSlider(0,100,mapSettings.getMinForest());
        JLabel j10 = new JLabel("Forest End",SwingConstants.CENTER);
        JSlider s10 = new JSlider(0,100,mapSettings.getMaxForest());
        JLabel j11 = new JLabel("Meadow Start",SwingConstants.CENTER);
        JSlider s11 = new JSlider(0,100,mapSettings.getMinMeadow());
        JLabel j12 = new JLabel("Meadow End",SwingConstants.CENTER);
        JSlider s12 = new JSlider(0,100,mapSettings.getMaxMeadow());

        JTextField seed = new JTextField("12345");

        JButton export = new JButton("Export settings");
        JButton reset = new JButton("Reset");
        JButton exportMap = new JButton("Export Map");
        JSlider[] sliders = new JSlider[]{s1,s2,s3,s4,s5,s6,s7,s8,s9,s10,s11,s12};
        final MapCreatorPanel surface = new MapCreatorPanel(sliders, seed, mapSettings.getSeed());
        sliderp.setLayout(new GridLayout(28,1,0,5));
        sliderp.add(j1);
        sliderp.add(s1);
        sliderp.add(j2);
        sliderp.add(s2);
        sliderp.add(j3);
        sliderp.add(s3);
        sliderp.add(j4);
        sliderp.add(s4);
        sliderp.add(j5);
        sliderp.add(s5);
        sliderp.add(j9);
        sliderp.add(s9);
        sliderp.add(j10);
        sliderp.add(s10);
        sliderp.add(j11);
        sliderp.add(s11);
        sliderp.add(j12);
        sliderp.add(s12);
        sliderp.add(j6);
        sliderp.add(s6);
        sliderp.add(j7);
        sliderp.add(s7);
        sliderp.add(j8);
        sliderp.add(s8);
        sliderp.add(reset);
        sliderp.add(export);
        sliderp.add(exportMap);
        sliderp.add(seed);

        s1.addChangeListener(surface::applyTreshold);
        s2.addChangeListener(surface::applyTreshold);
        s3.addChangeListener(surface::applyTreshold);
        s4.addChangeListener(surface::applyTreshold);
        s5.addChangeListener(surface::applyTreshold);
        s9.addChangeListener(surface::applyTreshold);
        s10.addChangeListener(surface::applyTreshold);
        s11.addChangeListener(surface::applyTreshold);
        s12.addChangeListener(surface::applyTreshold);

        reset.addActionListener(surface::newMap);
        export.addActionListener(surface::export);
        exportMap.addActionListener(surface::exportMap);
        seed.addActionListener(surface::seedChanged);


        surface.setPreferredSize(new Dimension( 1000, 1000 ));
        add(surface);
        add(sliderp);

    }


}
