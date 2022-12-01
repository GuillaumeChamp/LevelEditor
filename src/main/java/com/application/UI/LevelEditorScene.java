package com.application.UI;

import com.application.Game.Level.Level;
import com.application.Game.Level.LevelElements.Layer0.Tile;
import com.application.Game.Level.LevelElements.Layer1.Collision;
import com.application.Game.Level.LevelElements.Layer1.Encounter;
import com.application.Game.Level.LevelElements.Layer1.OverTile;
import com.application.Game.Level.LevelElements.Layer1.Warp;
import com.application.Game.Level.LevelElements.TileTyped;
import com.application.IO.Saver;
import com.application.UI.Elements.PopUpName;
import javafx.geometry.Orientation;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ScrollBar;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.io.File;
import java.util.Optional;

public class LevelEditorScene extends Pane {
    static LevelEditorScene levelEditorScene;
    ScrollBar horizontalBar = new ScrollBar();
    ScrollBar verticalBar = new ScrollBar();
    protected Canvas canvas;
    private Level level;
    private double ratio = Graphic_Const.ratioEditor;

    /**
     * Private constructor
     */
    private LevelEditorScene() {
        this.setWidth(Graphic_Const.DEFAULT_WIDTH);
        //this.setPrefWidth(Graphic_Const.DEFAULT_WIDTH);
        this.setHeight(Graphic_Const.DEFAULT_HEIGHT);
        verticalBar.setLayoutX(this.getWidth() - 20);
        verticalBar.setOrientation(Orientation.VERTICAL);
        verticalBar.setPrefHeight(this.getHeight());
        level = new Level(200,200);
        this.canvas = new Canvas(this.getWidth(),this.getHeight());
        canvas.setOnMouseClicked(e->{
            double x = (e.getX()) + (horizontalBar.getValue());
            double y = (e.getY()) + (verticalBar.getValue());
            if (EditorPanel.getPanel().getSelectedTile()==null) return;
            modifyTileAt(x,y,EditorPanel.getPanel().getSelectedTile());
            paint();
        });
        this.setOnScroll(e->{
            if (e.isControlDown()){
                if (e.getDeltaX()+e.getDeltaY()>0) ratio=ratio+0.2;
                else {
                    ratio=ratio-0.2;
                    if (ratio<0.8) ratio=0.8;
                }
                paint();
                this.setBound(level.getTiles()[0].length,level.getTiles().length);
                return;
            }
            if(horizontalBar.getValue()-e.getDeltaX()<horizontalBar.getMax())
                if (horizontalBar.getValue()-e.getDeltaX()>horizontalBar.getMin())
                    horizontalBar.setValue(horizontalBar.getValue()-e.getDeltaX()*1.1);
            if(verticalBar.getValue()-e.getDeltaY()<verticalBar.getMax())
                if (verticalBar.getValue()-e.getDeltaY()>verticalBar.getMin())
                    verticalBar.setValue(verticalBar.getValue()-e.getDeltaY()*1.1);
            paint();
        });



        canvas.setOnMouseDragged(e->{
            double x = e.getX() + horizontalBar.getValue();
            double y = e.getY() + verticalBar.getValue();
            if (EditorPanel.getPanel().getSelectedTile()==null) return;
            modifyTileAt(x,y,EditorPanel.getPanel().getSelectedTile());
            paint();
        });
        this.getChildren().add(canvas);
        this.setBound(200,200);
        paint();
    }

    /**
     * This methode to adjust slider to allow the canvas to slide in the whole level
     * @param hTiles number or horizontal tiles
     * @param vTiles number of vertical tiles
     */
    private void setBound(int hTiles,int vTiles) {
        final int horizontalOverFlow=1;
        final int verticalOverFlow=1;
        this.horizontalBar.setMax((hTiles+horizontalOverFlow)*Graphic_Const.TILES_SIZE*ratio -this.getWidth());
        if (horizontalBar.getValue()>horizontalBar.getMax()) this.horizontalBar.setValue(horizontalBar.getMax());
        this.verticalBar.setMax((vTiles+verticalOverFlow)*Graphic_Const.TILES_SIZE*ratio -this.getHeight());
        if (verticalBar.getValue()>verticalBar.getMax()) this.verticalBar.setValue(verticalBar.getMax());
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
        PopUpName popUpName = new PopUpName();
        Optional<ButtonType> ans = popUpName.showAndWait();
        assert ans.isPresent();
        if (ans.get().getText().equals("confirm"))
            this.renameLevel(popUpName.getName());
        this.level = new Level(level.getTiles()[0].length,level.getTiles().length);
        this.setBound(level.getTiles()[0].length,level.getTiles().length);
        paint();
    }

    /**
     * Paint the level (tiles and overTiles)
     */
    public void paint(){
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        int tileSize = Graphic_Const.TILES_SIZE;
        Tile[][] tiles = level.getTiles();
        OverTile[][] overTiles = level.getOverTiles();
        double sliderVerticalOffSet =  (verticalBar.getValue()/ratio/tileSize);
        double sliderHorizontalVOffSet = (horizontalBar.getValue()/ratio/tileSize);
        for(int i = (int) sliderHorizontalVOffSet; i<Math.min(sliderHorizontalVOffSet+this.getWidth()/tileSize/ratio,tiles.length); i++){
            for (int j = (int) sliderVerticalOffSet; j<Math.min(sliderVerticalOffSet+this.getHeight()/tileSize/ratio,tiles[0].length); j++){
                double x = (i-sliderHorizontalVOffSet)*tileSize;
                double y = (j-sliderVerticalOffSet)*tileSize;
                if (tiles[i][j]!=null) {
                    gc.drawImage(tiles[i][j].getSkin(), x * ratio, y * ratio, tileSize * ratio, tileSize * ratio);
                }
                if (overTiles[i][j]!= null && Graphic_Const.SHOW_CALC){
                    if(overTiles[i][j] instanceof Warp) gc.setFill(Color.color(0.5,0.5,0.5,0.3));
                    if (overTiles[i][j] instanceof Encounter) gc.setFill(Color.color(0.8,0,0,0.3));
                    if (overTiles[i][j] instanceof Collision) gc.setFill(Color.color(0,0,0,0.8));
                    gc.fillRect( x * ratio, y * ratio, tileSize * ratio, tileSize * ratio);
                    gc.setFill(Color.color(1,0,0,1));
                    gc.fillText(String.valueOf(overTiles[i][j].getId()),(x+tileSize/2.0)*ratio,(y+tileSize/2.0)*ratio);
                }
                if (Graphic_Const.SHOW_GRID) gc.strokeRect(x * ratio, y * ratio, tileSize * ratio, tileSize * ratio);
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
        int tileSize = Graphic_Const.TILES_SIZE;
        try {
            double xIndex = Math.floor(x / (tileSize * ratio));
            double yIndex = Math.floor(y / (tileSize * ratio));
            if (newTile.getClass()==Tile.class) level.getTiles()[(int) xIndex][(int) yIndex] = (Tile) newTile;
            if (OverTile.class.isAssignableFrom(newTile.getClass())) {
                level.getOverTiles()[(int) xIndex][(int) yIndex] = (OverTile) newTile;
            }
        }catch (Exception e){
            System.out.println("Miss Click at x=" +x+" and y=" + y);
        }
    }

    /**
     * Transitive call
     * @param x new width
     * @param y new height
     */
    public void changeLevelSize(int x, int y){
        level.changeSize(x,y);
        this.setBound(x,y);
        paint();
    }

    /**
     * Transitive call
     * @throws Exception pass the exception
     */
    public void saveLevel(File rep) throws Exception {
        Saver.saveLevel(level,rep);
    }

    /**
     * Transitive call
     * @param name new name
     */
    public void renameLevel(String name){
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

    public void resizeOptions(double width) {
        this.setPrefWidth(width);
        this.setBound(level.getTiles()[0].length,level.getTiles().length);
        paint();
    }
}
