package com.application.Game.Level.LevelElements.Layer1;

public class Warp extends OverTile {
    private final String exit;
    private final int xDes;
    private final int yDes;

    public Warp(String exit, int xDes, int yDes) {
        this.exit = exit;
        this.xDes = xDes;
        this.yDes = yDes;
        this.id=10;
    }

    @Override
    public String toString() {
        return "Warp{" +
                "exit='" + exit + '\'' +
                ", xDes=" + xDes +
                ", yDes=" + yDes +
                '}';
    }
}