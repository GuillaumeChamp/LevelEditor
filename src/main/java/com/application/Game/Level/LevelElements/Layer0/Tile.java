package com.application.Game.Level.LevelElements.Layer0;

import com.application.Game.Level.LevelElements.TileTyped;
import com.application.UI.Elements.ImageHolder;
import javafx.scene.image.Image;

public class Tile implements TileTyped {
    Image skin;

    /**
     * Create the null tile
     */
    public Tile() {
        skin= ImageHolder.getImage("Skin/player.png");
    }

    /**
     * Create a new tile
     * @param skin image for the new tile
     */
    public Tile(Image skin) {
        this.skin = skin;
    }

    /**
     * This has to be used to init all the tiles of a level
     * @param tiles tiles to init (null tile)
     */
    public static void initTiles(Tile[][] tiles){
        Tile nullTile = new Tile();
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[0].length; j++) {
                tiles[i][j] = nullTile;
            }
        }
    }

    /**
     * Simple getter
     * @return the skin of the tile
     */
    public Image getSkin() {
        return skin;
    }

}
