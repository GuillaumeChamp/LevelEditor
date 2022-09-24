package com.application.UI;

import com.application.Game.Level.Level;
import com.application.Game.Level.LevelElements.Layer0.Tile;
import com.application.Game.Level.LevelElements.Layer1.OverTile;
import com.application.Game.Level.LevelElements.TileTyped;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollPane;
import javafx.scene.paint.Color;


public class LevelEditorScene extends ScrollPane {
    static LevelEditorScene levelEditorScene;
    protected Canvas canvas;
    private Level level;

    private LevelEditorScene(double width, double height) {
        this.setWidth(width);
        this.setHeight(height);
        level = new Level(160,160);
        this.canvas = new Canvas(this.getWidth(),this.getHeight());
        canvas.setOnMouseClicked(e->{

            double x = e.getX();
            double y = e.getY();
            if (EditorPanel.getPanel().selectedTile==null) return;
            modifyTileAt(x,y,EditorPanel.getPanel().getSelectedTile());
            paint();
        });
        this.setContent(canvas);
        Tile.initTiles(level.getTiles());
        paint();
    }

    public static LevelEditorScene getLevelEditorScene(double width,double height) {
        if (levelEditorScene==null) levelEditorScene=new LevelEditorScene(width, height);
        return levelEditorScene;
    }

    public void newLevelRequest() {
        this.level = new Level(level.getTiles().length,level.getTiles()[0].length);
        Tile.initTiles(level.getTiles());
        paint();
    }

    public void paint(){
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        int tileSize = Graphic_Const.TILES_SIZE;
        double ratio = Graphic_Const.ratio;
        Tile[][] tiles = level.getTiles();
        OverTile[][] overTiles = level.getOverTiles();
        for(int i=0;i<tiles.length;i++){
            for (int j=0;j<tiles[0].length;j++){
                if (tiles[i][j]!=null) {
                    gc.drawImage(tiles[i][j].getSkin(), i * tileSize * ratio, j * tileSize * ratio, tileSize * ratio, tileSize * ratio);
                }
                if (overTiles[i][j]!= null){
                    gc.setFill(Color.color(0.5,0.5,0.5,0.5));
                    gc.fillRect( i * tileSize * ratio, j * tileSize * ratio, tileSize * ratio, tileSize * ratio);
                }
                gc.strokeRect(i * tileSize * ratio, j * tileSize * ratio, tileSize * ratio, tileSize * ratio);
            }
        }
    }
    public void modifyTileAt(double x, double y, TileTyped newTile){
        double ratio = Graphic_Const.ratio;
        int tileSize = Graphic_Const.TILES_SIZE;
        try {
            double xIndex = Math.floor(x / (tileSize * ratio));
            double yIndex = Math.floor(y / (tileSize * ratio));
            if (newTile.getClass()==Tile.class) level.getTiles()[(int) xIndex][(int) yIndex] = (Tile) newTile;
            if (OverTile.class.isAssignableFrom(newTile.getClass())) {
                System.out.println("ok");
                level.getOverTiles()[(int) xIndex][(int) yIndex] = (OverTile) newTile;
            }
        }catch (Exception e){
            System.out.println("Miss Click");
        }
    }
    public void changeSize(int x,int y){
        level.changeSize(x,y);
        paint();
    }
    public void saveLevel() throws Exception {
        Saver.saveLevel(level);
    }
    public void rename(String name){
        level.setName(name);
    }

    public void setLevel(Level loadLevel) {
        this.level=loadLevel;
        paint();
    }
}
