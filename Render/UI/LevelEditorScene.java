package UI;

import Game.OutDoor.Level;
import Game.OutDoor.LevelElements.Tile;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollPane;


public class LevelEditorScene extends ScrollPane {
    protected Canvas canvas;
    Level level;

    public LevelEditorScene(double width, double height) {
        this.setWidth(width);
        this.setHeight(height);
        level = new Level(true,160,160);
        this.canvas = new Canvas(this.getWidth(),this.getHeight());
        canvas.setOnMouseClicked(e->{

            double x = e.getX();
            double y = e.getY();
            if (EditorPanel.getPanel().selectedTile==null) return;
            modifyTileAt(x,y,EditorPanel.getPanel().getSelectedTile());
            paint();
        });
        this.setContent(canvas);
        level.initTiles();
        paint();

    }

    public void paint(){
        GraphicsContext gc = canvas.getGraphicsContext2D();
        int tileSize = Graphic_Const.H_TILES_SIZE;
        double ratio = Graphic_Const.ratio;
        Tile[][] tiles = level.getTiles();
        for(int i=0;i<tiles.length;i++){
            for (int j=0;j<tiles[0].length;j++){
                if (tiles[i][j]==null) continue;
                gc.drawImage(tiles[i][j].getSkin(),i*tileSize*ratio,j*tileSize*ratio,tileSize*ratio,tileSize*ratio);
                gc.strokeRect(i*tileSize*ratio,j*tileSize*ratio,tileSize*ratio,tileSize*ratio);
            }
        }
    }
    public void modifyTileAt(double x, double y,Tile newTile){
        double ratio = Graphic_Const.ratio;
        int tileSize = Graphic_Const.H_TILES_SIZE;
        level.getTiles()[(int) Math.floor(x/(tileSize*ratio))][(int) Math.floor(y/(tileSize*ratio))] = newTile;
    }

}
