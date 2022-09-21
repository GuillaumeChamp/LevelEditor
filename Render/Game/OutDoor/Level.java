package Game.OutDoor;

import Game.OutDoor.LevelElements.Tile;
import Game.OutDoor.LevelElements.Warp;
import UI.Graphic_Const;

import java.util.ArrayList;

public class Level {
    public static String path="Level//";
    private String name;
    private final double sizeX;
    private final double sizeY;
    private final Tile[][] tiles;
    private final boolean peaceful;

    private ArrayList<Warp> tp;

    public Level(boolean peaceful,int hTile,int vTile){
        this.peaceful = peaceful;
        tiles = new Tile[hTile][vTile];
        sizeY = vTile*Graphic_Const.V_TILES_SIZE;
        sizeX = hTile*Graphic_Const.H_TILES_SIZE;
    }


    public ArrayList<Warp> getTp() {
        return tp;
    }

    public void setTp(ArrayList<Warp> tp) {
        this.tp = tp;
    }
    public void changeTile(Tile tile,int x, int y){
        tiles[x][y] = tile;
    }

    public boolean isPeaceful() {
        return peaceful;
    }

    public double getSizeX() {
        return sizeX;
    }

    public double getSizeY() {
        return sizeY;
    }

    public String getName() {
        return name;
    }

    public Tile[][] getTiles() {
        return tiles;
    }
    public void initTiles(){
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[0].length; j++) {
                changeTile(new Tile(),i,j);
            }
        }

    }
}
