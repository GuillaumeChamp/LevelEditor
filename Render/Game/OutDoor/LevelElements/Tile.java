package Game.OutDoor.LevelElements;

import UI.Elements.ImageHolder;
import javafx.scene.image.Image;

public class Tile {
    Image skin;
    String type;
    Boolean collision;

    public Tile() {
        skin=ImageHolder.getImage("Skin/player.png");
        type="null";
        collision=true;
    }

    public Tile(Image skin, String type, Boolean collision) {
        this.skin = skin;
        this.type = type;
        this.collision = collision;
    }
    public static void initTiles(Tile[][] tiles){
        Tile nullTile = new Tile();
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[0].length; j++) {
                tiles[i][j] = nullTile;
            }
        }
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setSkin(Image skin) {
        this.skin = skin;
    }

    public Image getSkin() {
        return skin;
    }

}
