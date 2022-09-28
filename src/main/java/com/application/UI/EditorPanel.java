package com.application.UI;

import com.application.Game.Level.LevelElements.Layer0.Tile;
import com.application.Game.Level.LevelElements.Layer1.Collision;
import com.application.Game.Level.LevelElements.Layer1.Encounter;
import com.application.Game.Level.LevelElements.Layer1.OverTile;
import com.application.Game.Level.LevelElements.Layer1.Warp;
import com.application.Game.Level.LevelElements.TileTyped;
import com.application.UI.Elements.PopUpTP;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Optional;

public class EditorPanel extends VBox {
    private static EditorPanel panel;
    private ScrollPane tilePane;
    private ScrollPane overTilePane;
    private Canvas canvasTile;
    private Canvas canvasOverTile;
    private final ArrayList<Tile> tileSet =new ArrayList<>();
    private final ArrayList<OverTile> overTileSet =new ArrayList<>();
    private TileTyped selectedTile;
    private final TextField levelName= new TextField("levelName");

    /**
     * Singleton behaviour
     * May produces an error if call before the stage start
     * @return the unique instance
     */
    public static EditorPanel getPanel(){
        return panel;
    }

    /**
     * Singleton with creation if not exist yet
     * @param stage the stage to attach this
     * @return the unique instance
     */
    public static EditorPanel getPanel(Stage stage) {
        if (panel==null) panel=new EditorPanel(stage);
        return panel;
    }

    /**
     * Create the editor panel
     * @param stage primary stage
     */
    private EditorPanel(Stage stage) {
        this.setWidth(Graphic_Const.DEFAULT_WIDTH);
        this.setHeight(Graphic_Const.DEFAULT_HEIGHT);

        FileChooser load =new FileChooser();
        Button picker =new Button("Import TileSet");
        picker.setOnAction(e->{
            File file = load.showOpenDialog(stage);
            createTileSet(file);
        });
        createCanvas();
        //Adding behaviour
        buildOverTiles();
        Button addTP = new Button("add TP");
        addTP.setOnAction(e->{
            PopUpTP popUpTP = new PopUpTP();
            Optional<ButtonType> ans = popUpTP.showAndWait();
            assert ans.isPresent();
            if (ans.get().getText().equals("confirm"))
                overTileSet.add(popUpTP.getTP());
            paintOverTileSet();
        });

        //Adding elements
        this.getChildren().add(new VBox(new Label("level dimension"),createTextField()));
        this.getChildren().add(new VBox(new Label("level Name"),levelName));
        this.getChildren().addAll(createCheckBox(),picker,tilePane,addTP,overTilePane);
    }

    /**
     * Create the checkBox to hide/show layers
     * @return an HBox containing the checkboxes
     */
    private HBox createCheckBox() {
        CheckBox showCalc = new CheckBox("show calc");
        CheckBox showGrid = new CheckBox("show grid");
        showCalc.setSelected(true);
        showCalc.setOnAction(e->{
            Graphic_Const.SHOW_CALC =showCalc.isSelected();
            LevelEditorScene.getLevelEditorScene().paint();
        });
        showGrid.setSelected(true);
        showGrid.setOnAction(e->{
            Graphic_Const.SHOW_GRID = showGrid.isSelected();
            LevelEditorScene.getLevelEditorScene().paint();
        });
        return new HBox(showCalc,showGrid);
    }

    /**
     * Create text field
     * @return HBox with TextField on it
     */
    private HBox createTextField(){
        TextField widthField = new TextField("200");
        TextField heightField = new TextField("200");
        widthField.setTextFormatter(new TextFormatter<>(new NumberStringConverter()));
        heightField.setTextFormatter(new TextFormatter<>(new NumberStringConverter()));
        widthField.setText("200");
        heightField.setText("200");
        widthField.setOnKeyPressed(e->{
            if (e.getCode().equals(KeyCode.ENTER)) {
                LevelEditorScene.getLevelEditorScene().changeSize(Integer.parseInt(widthField.getText()),Integer.parseInt(heightField.getText()));
            }
        });
        heightField.setOnKeyPressed(e->{
            if (e.getCode().equals(KeyCode.ENTER)) {
                LevelEditorScene.getLevelEditorScene().changeSize(Integer.parseInt(widthField.getText()),Integer.parseInt(heightField.getText()));
            }
        });

        levelName.setOnKeyPressed(e-> LevelEditorScene.getLevelEditorScene().rename(levelName.getText()));

        return new HBox(widthField,heightField);
    }

    /**
     * Create the two canvas
     */
    private void createCanvas() {
        canvasTile = new Canvas(this.getWidth(),this.getWidth());
        canvasOverTile =  new Canvas(this.getWidth(),this.getWidth());
        tilePane = new ScrollPane(canvasTile);
        tilePane.resize(this.getWidth(),this.getWidth());
        overTilePane = new ScrollPane(canvasOverTile);
        overTilePane.resize(this.getWidth(),this.getWidth());
        tilePane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        overTilePane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        canvasOverTile.setOnMouseClicked(e->{
            double x = e.getX();
            double y = e.getY();
            try {
                selectedTile = getOverTileAt(x,y);
            } catch (Exception ex) {
                System.out.println("Please create an over tileset");
                return;
            }
            paintOverTileSet();
            paintTileSet();
        });
        canvasTile.setOnMouseClicked(e->{
            double x = e.getX();
            double y = e.getY();
            try {
                selectedTile = getTileAt(x,y);
            } catch (Exception ex) {
                System.out.println("Please select a tileset");
                return;
            }
            paintTileSet();
            paintOverTileSet();
        });
    }

    /**
     * Create the base overTiles set
     */
    private void buildOverTiles() {
        for (int i = 0; i < 10; i++) {
            overTileSet.add(new Encounter(i));
        }
        overTileSet.add(new Collision());
        paintOverTileSet();
    }

    /**
     * Calculate the overTile at a precise coordinate
     * @param x xPos of the coordinate
     * @param y yPos of the coordinate
     * @return the tile at where you click
     * @throws IndexOutOfBoundsException if click is out of the tile set
     */
    private TileTyped getOverTileAt(double x, double y) throws IndexOutOfBoundsException {
        double ratio = Graphic_Const.ratio;
        int tileSize = Graphic_Const.TILES_SIZE;
        double tilesPerLine = Math.round(tilePane.getWidth()/(tileSize*ratio));
        int index = (int) (Math.floor(y/(ratio*tileSize))*tilesPerLine+Math.floor(x/(tileSize*ratio)));
        return overTileSet.get(index);
    }

    /**
     * Use to resize the scroll boxes in case of resize of this panel
     * @param width width of the Editor Pane
     */
    public void resizeOptions(double width){
        tilePane.setMaxWidth(width);
        overTilePane.setMaxWidth(width);
        if (!tileSet.isEmpty()) paintTileSet();
        if (!overTileSet.isEmpty()) paintOverTileSet();
    }

    /**
     * Load the tile set from an image splitting it on tiles (size in Graphic_Const) and add them in the tile set
     * @param file image at the png format (ignore the last row/column if not 16nx16k)
     */
    public void createTileSet(File file){
        try {
            int tileSize = Graphic_Const.TILES_SIZE;
            Image image = new Image(file.toURI().toString());
            PixelReader reader = image.getPixelReader();
            double width = image.getWidth();
            double height = image.getHeight();
            long hTile = Math.round(width / tileSize);
            long vTile = Math.round(height / tileSize);
            for (int j = 0; j < vTile; j++)
                for (int i = 0; i < hTile; i++) {
                        Image tileSkin = new WritableImage(reader, tileSize * i, tileSize * j, tileSize, tileSize);
                        tileSet.add(new Tile(tileSkin));
                    }
            paintTileSet();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    /**
     * Paint the tile set in the tile pane
     */
    private void paintTileSet() {
        GraphicsContext gc = canvasTile.getGraphicsContext2D();
        gc.clearRect(0, 0, canvasTile.getWidth(), canvasTile.getHeight());
        double x = 0;
        double y = 0;
        double ratio = Graphic_Const.ratio;
        int tileSize = Graphic_Const.TILES_SIZE;
        double tilesPerLine = Math.round(tilePane.getWidth()/(tileSize*ratio));
        double xMax = (tilesPerLine)*tileSize;
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);
        for (Tile t: tileSet) {
            gc.strokeRect(x*ratio,y*ratio,tileSize*ratio,tileSize*ratio);
            gc.drawImage(t.getSkin(),x*ratio,y*ratio,tileSize*ratio,tileSize*ratio);
            x = x+tileSize;
            if (x>=xMax){
                x = 0;
                y = y+tileSize;
            }
        }
        if (selectedTile==null || selectedTile.getClass()!=Tile.class) return;
        int index = tileSet.indexOf(selectedTile);
        gc.setLineWidth(5);
        gc.setStroke(Color.RED);
        double xx = (index%tilesPerLine)*tileSize;
        double yy =  Math.floor(index/tilesPerLine)*tileSize;
        gc.strokeRect(xx*ratio,yy*ratio,tileSize*ratio,tileSize*ratio);

    }

    /**
     * paint the overTile Set in the overTilePane
     */
    private void paintOverTileSet() {
        GraphicsContext gc = canvasOverTile.getGraphicsContext2D();
        gc.clearRect(0, 0, canvasTile.getWidth(), canvasTile.getHeight());
        double x = 0;
        double y = 0;
        double ratio = Graphic_Const.ratio;
        int tileSize = Graphic_Const.TILES_SIZE;
        double tilesPerLine = Math.round(overTilePane.getWidth()/(tileSize*ratio));
        double xMax = (tilesPerLine)*tileSize;
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);

        for (OverTile t: overTileSet) {
            if (t==null) return;
            if(t.getClass()== Warp.class) gc.setFill(Color.color(0.5,0.5,0.5,0.3));
            if (t.getClass()==Encounter.class) gc.setFill(Color.color(0.8,0,0,0.3));
            if (t.getClass()== Collision.class) gc.setFill(Color.color(0,0,0,0.8));
            gc.strokeRect(x*ratio,y*ratio,tileSize*ratio,tileSize*ratio);
            gc.fillText(String.valueOf(t.getId()),(x+tileSize/2.0)*ratio,(y+tileSize/2.0)*ratio);
            gc.fillRect(x*ratio,y*ratio,tileSize*ratio,tileSize*ratio);
            x = x+tileSize;
            if (x>=xMax){
                x = 0;
                y = y+tileSize;
            }
        }
        if (selectedTile==null || selectedTile.getClass() == Tile.class) return;
        int index = overTileSet.indexOf((OverTile) selectedTile);
        gc.setLineWidth(5);
        gc.setStroke(Color.RED);
        double xx = (index%tilesPerLine)*tileSize;
        double yy =  Math.floor(index/tilesPerLine)*tileSize;
        gc.strokeRect(xx*ratio,yy*ratio,tileSize*ratio,tileSize*ratio);
    }

    /**
     * Simple getter
     * @return selected tile
     */
    public TileTyped getSelectedTile() {
        return selectedTile;
    }
    /**
     * Calculate the tile at a precise coordinate
     * @param x xPos of the coordinate
     * @param y yPos of the coordinate
     * @return the tile at where you click
     * @throws IndexOutOfBoundsException if click is out of the tile set
     */
    private Tile getTileAt(double x, double y) throws IndexOutOfBoundsException{
        double ratio = Graphic_Const.ratio;
        int tileSize = Graphic_Const.TILES_SIZE;
        double tilesPerLine = Math.round(tilePane.getWidth()/(tileSize*ratio));
        int index = (int) (Math.floor(y/(ratio*tileSize))*tilesPerLine+Math.floor(x/(tileSize*ratio)));
        return tileSet.get(index);
    }

    /**
     * Used during the loading process
     * @param name name of the saved level that
     */
    public void setLevelName(String name){
        levelName.setText(name);
    }

    /**
     * Load overTile set from saved level
     * @param file levelFile.level1
     * @throws IOException if reading exceptions
     * @throws ClassNotFoundException if you use an older version format
     */
    public void loadOverTiles(File file) throws IOException, ClassNotFoundException {
        ArrayList<OverTile> arrayList = overTileSet;
        ObjectInputStream oot1 = new ObjectInputStream(Files.newInputStream(file.toPath()));
        OverTile[][] overTiles = (OverTile[][]) oot1.readObject();
        for(OverTile[] tiles : overTiles){
            for (OverTile tile : tiles){
                if (!arrayList.contains(tile) && tile!=null) arrayList.add(tile);
            }
        }
        paintOverTileSet();
    }
}
