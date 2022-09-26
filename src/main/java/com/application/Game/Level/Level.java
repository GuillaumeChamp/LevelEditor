package com.application.Game.Level;

import com.application.Game.Level.LevelElements.Layer0.Tile;
import com.application.Game.Level.LevelElements.Layer1.OverTile;


public class Level {
    private String name="untiled";
    private Tile[][] tiles;
    private OverTile[][] overTiles;


    public Level(int hTile, int vTile){
        tiles = new Tile[hTile][vTile];
        overTiles = new OverTile[hTile][vTile];
    }

    public void setOverTiles(OverTile[][] overTiles) {
        this.overTiles = overTiles;
    }

    public Tile[][] getTiles() {
        return tiles;
    }

    public void changeSize(int x,int y){
        Tile[][] newTiles = new Tile[x][y];
        OverTile[][] newOverTiles = new OverTile[x][y];
        int lineToCopy = Math.min(x,tiles.length);
        int columnToCopy = Math.min(x,tiles[0].length);
        Tile.initTiles(newTiles);

        for (int i = 0; i < lineToCopy; i++) {
            System.arraycopy(tiles[i], 0, newTiles[i], 0, columnToCopy);
            System.arraycopy(overTiles[i], 0, newOverTiles[i], 0, columnToCopy);
        }
        tiles=newTiles;
        overTiles= newOverTiles;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public OverTile[][] getOverTiles() {
        return overTiles;
    }
}