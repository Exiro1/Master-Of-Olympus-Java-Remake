package com.exiro.systemCore;

import com.exiro.object.City;
import com.exiro.object.Player;
import com.exiro.render.*;

public class GameManager {

    public static City currentCity;
    GameFrame frame;
    GameWindow GameView;
    MenuBar menuBar;
    Player player;
    private final InterfaceRender it;
    private final GameInfo gi;

    public GameManager(Player player, City currentCity) {
        this.player = player;
        GameManager.currentCity = currentCity;
        frame = new GameFrame("lol", this);
        this.GameView = frame.getWindow();
        this.menuBar = frame.getMenu();
        this.gi = frame.getGi();
        this.it = frame.getIt();
    }


    public GameFrame getFrame() {
        return frame;
    }

    public void setFrame(GameFrame frame) {
        this.frame = frame;
    }

    public GameWindow getGameView() {
        return GameView;
    }

    public void setGameView(GameWindow gameView) {
        GameView = gameView;
    }

    public MenuBar getMenuBar() {
        return menuBar;
    }

    public void setMenuBar(MenuBar menuBar) {
        this.menuBar = menuBar;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public City getCurrentCity() {
        return currentCity;
    }

    public void setCurrentCity(City currentCity) {
        GameManager.currentCity = currentCity;
    }
}
