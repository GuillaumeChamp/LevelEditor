package com.application.UI;

import com.application.Game.Level.Level;
import com.application.Game.Level.LevelElements.Layer0.Tile;
import com.application.Game.Level.LevelElements.Layer1.Collision;
import com.application.Game.Level.LevelElements.Layer1.Encounter;
import com.application.Game.Level.LevelElements.Layer1.OverTile;
import com.application.Game.Level.LevelElements.Layer1.Warp;
import com.application.Game.Level.LevelElements.TileTyped;
import com.application.Game.Level.Saver;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollPane;
import javafx.scene.paint.Color;

public class LevelEditorScene extends ScrollPane {
    static LevelEditorScene levelEditorScene;
    protected Canvas canvas;
    private Level level;

    /**
     * Private constructor
     */
    private LevelEditorScene() {
        this.setWidth(Graphic_Const.DEFAULT_WIDTH);
        this.setHeight(Graphic_Const.DEFAULT_HEIGHT);
        level = new Level(160,160);
        this.canvas = new Canvas(this.getWidth(),this.getHeight());
        canvas.setOnMouseClicked(e->{
            double x = e.getX();
            double y = e.getY();
            if (EditorPanel.getPanel().getSelectedTile() ==null) return;
            modifyTileAt(x,y,EditorPanel.getPanel().getSelectedTile());
            paint();
        });
        this.setContent(canvas);
        paint();
    }

    /**
     * Singleton
     * @return unique instance
     */
    public static LevelEditorScene getLevelEditorScene() {
        if (levelEditorScene==null) levelEditorScene=new LevelEditorScene();
        return levelEditorScene;
    }

    /**
     * Create a new level and reset all tiles
     */
    public void newLevelRequest() {
        this.level = new Level(level.getTiles().length,level.getTiles()[0].length);
        paint();
    }

    /**
     * Paint the level (tiles and overTiles)
     */
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
                if (overTiles[i][j]!= null && Graphic_Const.SHOW_CALC){
                    if(overTiles[i][j].getClass()== Warp.class) gc.setFill(Color.color(0.5,0.5,0.5,0.3));
                    if (overTiles[i][j].getClass()== Encounter.class) gc.setFill(Color.color(0.8,0,0,0.3));
                    if (overTiles[i][j].getClass()== Collision.class) gc.setFill(Color.color(0,0,0,0.8));
                    double x = i*tileSize;
                    double y = j*tileSize;
                    gc.fillRect( x * ratio, y * ratio, tileSize * ratio, tileSize * ratio);
                    gc.setFill(Color.color(1,0,0,1));
                    gc.fillText(String.valueOf(overTiles[i][j].getId()),(x+tileSize/2.0)*ratio,(y+tileSize/2.0)*ratio);
                }
                if (Graphic_Const.SHOW_GRID) gc.strokeRect(i * tileSize * ratio, j * tileSize * ratio, tileSize * ratio, tileSize * ratio);
            }
        }
    }

    /**
     * Change the clicked tile
     * @param x coordinate of the click on the canvas
     * @param y coordinate of the click on the canvas
     * @param newTile current tile
     */
    public void modifyTileAt(double x, double y, TileTyped newTile){
        double ratio = Graphic_Const.ratio;
        int tileSize = Graphic_Const.TILES_SIZE;
        try {
            double xIndex = Math.floor(x / (tileSize * ratio));
            double yIndex = Math.floor(y / (tileSize * ratio));
            if (newTile.getClass()==Tile.class) level.getTiles()[(int) xIndex][(int) yIndex] = (Tile) newTile;
            if (OverTile.class.isAssignableFrom(newTile.getClass())) {
                level.getOverTiles()[(int) xIndex][(int) yIndex] = (OverTile) newTile;
            }
        }catch (Exception e){
            System.out.println("Miss Click");
        }
    }

    /**
     * Transitive call
     * @param x new width
     * @param y new height
     */
    public void changeSize(int x,int y){
        level.changeSize(x,y);
        paint();
    }

    /**
     * Transitive call
     * @throws Exception pass the exception
     */
    public void saveLevel() throws Exception {
        Saver.saveLevel(level);
    }

    /**
     * Transitive call
     * @param name new name
     */
    public void rename(String name){
        level.setName(name);
    }

    /**
     * Use to load a saved level
     * @param loadLevel new level already build
     */
    public void setLevel(Level loadLevel) {
        this.level=loadLevel;
        paint();
    }
}
