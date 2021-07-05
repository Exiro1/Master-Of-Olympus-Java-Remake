package com.exiro.render;

import java.awt.*;
import java.util.ArrayList;

public class GameLayout implements LayoutManager {

    public static final String TOOLBAR = "TOOLBAR";
    public static final String GAMEVIEW = "GAMEVIEW";
    public static final String INTERFACE = "INTERFACE";
    private final int TOOLBAR_HEIGHT = 30;
    private final ArrayList<Component> toolbarComponents = new ArrayList<>();
    private final ArrayList<Component> GameComponents = new ArrayList<>();
    private final ArrayList<Component> InterfaceComponents = new ArrayList<>();

    @Override   // Called when JFrame.add() is used  //
    public void addLayoutComponent(String constraints, Component component) {

        switch (constraints) {
            case "TOOLBAR":
                toolbarComponents.add(component);
                break;
            case "GAMEVIEW":
                GameComponents.add(component);
                break;
            case "INTERFACE":
                InterfaceComponents.add(component);
                break;
        }
    }


    @Override   //  Sets the bounds for each component  //
    public void layoutContainer(Container parent) {
        //  Toolbar  //
        int currentWidth = 0;
        for (Component c : toolbarComponents) {
            int width = 800;
            c.setBounds(currentWidth, 0, width, TOOLBAR_HEIGHT);
            currentWidth = width + currentWidth;
        }

        //  Body  //
        int currentHeight = TOOLBAR_HEIGHT;
        for (Component c : GameComponents) {
            int height = (1080 - TOOLBAR_HEIGHT) / GameComponents.size();
            c.setBounds(0, currentHeight, 1600, height);
            currentHeight += height;
        }

        for (Component c : InterfaceComponents) {
            int height = 1080;
            c.setBounds(1600, 0, 1920 - 1600, 1080);
            currentHeight += height;
        }

    }


    @Override
    public Dimension minimumLayoutSize(Container parent) {
        return preferredLayoutSize(parent);
    }


    @Override   //  Called when JFrame.pack() is called  //
    public Dimension preferredLayoutSize(Container parent) {
        return new Dimension(1920, 1080);
    }


    @Override
    public void removeLayoutComponent(Component comp) {
    }

}
