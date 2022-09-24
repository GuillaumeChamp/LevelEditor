package Game.OutDoor.LevelElements;

import UI.Elements.ImageHolder;
import javafx.scene.image.Image;

public class Tile {
    Image skin;

    Boolean collision;

    public Tile() {
        skin=ImageHolder.getImage("Skin/player.png");
        collision=true;
    }

    public Tile(Image skin, Boolean collision) {
        this.skin = skin;
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

    public void setSkin(Image skin) {
        this.skin = skin;
    }

    public Image getSkin() {
        return skin;
    }

}
