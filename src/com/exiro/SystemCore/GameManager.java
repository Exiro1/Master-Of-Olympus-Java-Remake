package com.exiro.SystemCore;

import com.exiro.Object.City;
import com.exiro.Object.Player;
import com.exiro.Render.*;

public class GameManager {

    public static City currentCity;
    GameFrame frame;
    GameWindow GameView;
    MenuBar menuBar;
    Player player;
    private InterfaceRender it;
    private GameInfo gi;

    public GameManager(Player player, City currentCity) {
        this.player = player;
        this.currentCity = currentCity;
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
        this.currentCity = currentCity;
    }
}
