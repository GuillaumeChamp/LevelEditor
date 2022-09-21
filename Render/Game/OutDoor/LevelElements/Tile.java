package Game.OutDoor.LevelElements;

import UI.Elements.ImageHolder;
import javafx.scene.image.Image;

public class Tile {
    Image skin = ImageHolder.getImage("Skin/player.png");
    String type="void";
    Boolean collision=false;

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
