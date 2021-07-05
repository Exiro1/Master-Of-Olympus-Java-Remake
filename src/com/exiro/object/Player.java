package com.exiro.object;

import java.util.ArrayList;

public class Player {

    private float money;
    private int populaionTotal;
    private final String name;
    private ArrayList<City> playerCities;
    private ArrayList<Player> friendship;

    public Player(float money, int populaionTotal, String name, ArrayList<City> playerCities, ArrayList<Player> friendship) {
        this.money = money;
        this.populaionTotal = populaionTotal;
        this.name = name;
        this.playerCities = playerCities;
        this.friendship = friendship;
    }

    public Player(float money, int populaionTotal, String name) {
        this.money = money;
        this.populaionTotal = populaionTotal;
        this.name = name;
        this.friendship = new ArrayList<>();
        this.playerCities = new ArrayList<>();
        this.playerCities.add(new City(this.name + "'s city", this));
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public void pay(int amount) {
        this.money = this.money - amount;
    }

    public int getPopulaionTotal() {
        return populaionTotal;
    }

    public void setPopulaionTotal(int populaionTotal) {
        this.populaionTotal = populaionTotal;
    }

    public ArrayList<City> getPlayerCities() {
        return playerCities;
    }

    public void setPlayerCities(ArrayList<City> playerCities) {
        this.playerCities = playerCities;
    }

    public ArrayList<Player> getFriendship() {
        return friendship;
    }

    public void setFriendship(ArrayList<Player> friendship) {
        this.friendship = friendship;
    }
}
