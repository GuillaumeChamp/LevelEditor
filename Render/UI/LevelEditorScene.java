package UI;

import Game.OutDoor.Level;
import Game.OutDoor.LevelElements.Tile;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;


public class LevelEditorScene extends ScrollPane {
    static protected Canvas canvas;
    Level level;

    public LevelEditorScene(Canvas canvas,double width,double height) {
        this.setWidth(width);
        this.setHeight(height);
        level = new Level(true,160,160);
        LevelEditorScene.canvas = canvas;
        this.setContent(canvas);
        level.initTiles();
        paint();

    }

    public void paint(){
        GraphicsContext gc = canvas.getGraphicsContext2D();
        Tile[][] tiles = level.getTiles();
        for(int i=0;i<tiles.length;i++){
            for (int j=0;j<tiles[0].length;j++){
                if (tiles[i][j]==null) continue;
                gc.drawImage(tiles[i][j].getSkin(),i*16,j*16);
            }
        }
    }

}
