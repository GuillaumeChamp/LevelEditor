package Game.OutDoor;

import Game.OutDoor.LevelElements.PNJ;
import Game.OutDoor.LevelElements.Warp;
import UI.Elements.MovingAnimatedImage;
import UI.Graphic_Const;
import javafx.scene.image.*;

import java.util.ArrayList;

public class Level {
    public static String path="Level//";
    private String name;
    private final Image background;
    private final double sizeX;
    private final double sizeY;
    private final boolean peaceful;
    private MovingAnimatedImage[] Object; //list of all object display of the level display on the screen
    private PNJ[] npc;
    private ArrayList<Warp> tp;
    //TODO : delete this
    public Level(boolean peaceful){
        this.peaceful = peaceful;
        background = new Image(path+"town_land.png");
        name="town_land.png";
        sizeY = background.getHeight();
        sizeX = background.getWidth();
    }
    public Level(boolean peaceful,int hTile,int vTile){
        this.peaceful = peaceful;
        sizeY = vTile*Graphic_Const.V_TILES_SIZE;
        sizeX = hTile*Graphic_Const.H_TILES_SIZE;
        background = new Image(path+"town_land.png",sizeX,sizeY,false,false);
    }

    public ArrayList<Warp> getTp() {
        return tp;
    }

    public void setTp(ArrayList<Warp> tp) {
        this.tp = tp;
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

    public Image getBackground() {
        return background;
    }

    public String getName() {
        return name;
    }
}
