package Game.OutDoor;

import Game.OutDoor.LevelElements.Tile;
import Game.OutDoor.LevelElements.Warp;
import UI.Graphic_Const;

import java.util.ArrayList;

public class Level {
    public static String path="Level//";
    private String name;
    private Tile[][] tiles;
    private final boolean peaceful= false;

    private ArrayList<Warp> tp;

    public Level(int hTile, int vTile){
        tiles = new Tile[hTile][vTile];
    }


    public ArrayList<Warp> getTp() {
        return tp;
    }

    public void setTp(ArrayList<Warp> tp) {
        this.tp = tp;
    }


    public Tile[][] getTiles() {
        return tiles;
    }

    public void changeSize(int x,int y){
        Tile[][] newTiles = new Tile[x][y];
        int lineToCopy = Math.min(x,tiles.length);
        int columnToCopy = Math.min(x,tiles[0].length);
        Tile.initTiles(newTiles);

        for (int i = 0; i < lineToCopy; i++) {
            System.arraycopy(tiles[i], 0, newTiles[i], 0, columnToCopy);
        }
        tiles=newTiles;
    }
}
