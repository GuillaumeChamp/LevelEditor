package com.application.Game.Level;

import com.application.Game.Level.LevelElements.Layer0.Tile;
import com.application.Game.Level.LevelElements.Layer1.OverTile;


public class Level {
    private String name="untiled";
    private Tile[][] groundLayerTiles;
    private Tile[][] details;
    private OverTile[][] behaviourTiles;

    /**
     * Create a level with the determinate size
     * @param hTile number of column
     * @param vTile number of row
     */
    public Level(int hTile, int vTile){
        groundLayerTiles = new Tile[hTile][vTile];
        details = new Tile[hTile][vTile];
        behaviourTiles = new OverTile[hTile][vTile];
        Tile.initTiles(groundLayerTiles);
    }

    /**
     * Change the level size copping older tiles and overTiles
     * @param x new width
     * @param y new height
     */
    public void changeSize(int x,int y){
        Tile[][] newTiles = new Tile[x][y];
        OverTile[][] newOverTiles = new OverTile[x][y];
        Tile[][] newDetails = new Tile[x][y];
        int lineToCopy = Math.min(x, groundLayerTiles.length);
        int columnToCopy = Math.min(y, groundLayerTiles[0].length);
        Tile.initTiles(newTiles);

        for (int i = 0; i < lineToCopy; i++) {
            System.arraycopy(groundLayerTiles[i], 0, newTiles[i], 0, columnToCopy);
            System.arraycopy(behaviourTiles[i], 0, newOverTiles[i], 0, columnToCopy);
            System.arraycopy(details[i], 0, newDetails[i], 0, columnToCopy);
        }
        groundLayerTiles =newTiles;
        behaviourTiles = newOverTiles;
        details=newDetails;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setBehaviourTiles(OverTile[][] behaviourTiles) {
        this.behaviourTiles = behaviourTiles;
    }

    public String getName() {
        return name;
    }
    public OverTile[][] getBehaviourTiles() {
        return behaviourTiles;
    }
    public Tile[][] getGroundLayerTiles() {
        return groundLayerTiles;
    }

    public Tile[][] getDetails() {
        return details;
    }

    public void setGroundLayerTiles(Tile[][] ground) {
        this.groundLayerTiles = ground;
    }

    public void setDetails(Tile[][] details) {
        this.details = details;
    }
}
