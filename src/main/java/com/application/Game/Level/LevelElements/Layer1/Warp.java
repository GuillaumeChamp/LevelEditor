package com.application.Game.Level.LevelElements.Layer1;

public class Warp extends OverTile {
    private final String exit;
    private final int xDes;
    private final int yDes;
    private static int ctn;

    /**
     * Create a warp holding values used by the game engine to create an in game warp
     * @param exit name of the next level
     * @param xDes xPosition (in game pixel)
     * @param yDes yPosition (in game pixel)
     * @throws InvalidIdException if the max number of warp is rush
     */
    public Warp(String exit, int xDes, int yDes) {
        this.exit = exit;
        this.xDes = xDes;
        this.yDes = yDes;
        this.id=100+ctn;
        ctn++;
        if (ctn+100>= Integer.MAX_VALUE) throw new InvalidIdException("Already to much Encounter");
    }

    @Override
    public String toString() {
        return "Warp{" +
                "exit='" + exit + '\'' +
                ", xDes=" + xDes +
                ", yDes=" + yDes +
                '}';
    }
    public int getxDes() {
        return xDes;
    }

    public int getyDes() {
        return yDes;
    }

    public String getExit() {
        return exit;
    }
}