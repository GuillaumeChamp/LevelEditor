package com.application.Game.OutDoor.LevelElements;

import com.application.Game.OutDoor.Level;
import javafx.geometry.Rectangle2D;

public class Warp {
    private final Level exit;
    private Rectangle2D hitBox;
    private final double x;
    private final double y;

    /**
     * Build the specified exit
     * @param exit Level where teleported
     * @param x upper left corner
     * @param y upper left corner
     * @param width width of the hit box
     * @param height height of the hit box
     */
    public Warp(Level exit, int x, int y, int width, int height,double xdes, double ydes) {
        this.exit = exit;
        this.hitBox = new Rectangle2D(x,y,width,height);
        this.x = xdes;
        this.y = ydes;
    }
    public Level teleport(){
        return exit;
    }
}