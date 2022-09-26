package com.application.UI;

import com.application.Game.Level.LevelElements.Layer0.Tile;
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
    static EditorPanel panel;
    ScrollPane tilePane;
    ScrollPane overTilePane;
    Canvas canvasTile;
    Canvas canvasOverTile;
    ArrayList<Tile> tileSet =new ArrayList<>();
    ArrayList<OverTile> overTileSet =new ArrayList<>();
    TileTyped selectedTile;
    Button addTP = new Button("add TP");
    TextField levelName = new TextField("untitled");

    private EditorPanel(Stage stage) {
        //Sizing
        this.setWidth(Graphic_Const.DEFAULT_WIDTH);
        this.setHeight(Graphic_Const.DEFAULT_HEIGHT);
        //Creating component
        TextField widthField = new TextField("200");
        TextField heightField = new TextField("200");
        FileChooser load =new FileChooser();
        CheckBox showCalc = new CheckBox("show calc");
        CheckBox showGrid = new CheckBox("show grid");
        Button picker =new Button("Import TileSet");
        canvasTile = new Canvas(this.getWidth(),this.getWidth());
        canvasOverTile =  new Canvas(this.getWidth(),this.getWidth());
        tilePane = new ScrollPane(canvasTile);
        tilePane.resize(this.getWidth(),this.getWidth());
        overTilePane = new ScrollPane(canvasOverTile);
        overTilePane.resize(this.getWidth(),this.getWidth());

        //Adding behaviour
        widthField.setTextFormatter(new TextFormatter<>(new NumberStringConverter()));
        heightField.setTextFormatter(new TextFormatter<>(new NumberStringConverter()));
        tilePane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        overTilePane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        buildEncounter();
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
        addTP.setOnAction(e->{
            PopUpTP popUpTP = new PopUpTP();
            Optional<ButtonType> ans = popUpTP.showAndWait();
            assert ans.isPresent();
            if (ans.get().getText().equals("confirm"))
                overTileSet.add(popUpTP.getTP());
            paintOverTileSet();
        });
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
        picker.setOnAction(e->{
            File file = load.showOpenDialog(stage);
            createTileSet(file);
        });
        //Adding elements
        this.getChildren().add(new VBox(new Label("level dimension"),new HBox(widthField,heightField)));
        this.getChildren().add(new VBox(new Label("level Name"),levelName));
        this.getChildren().add(new HBox(showCalc,showGrid));
        this.getChildren().add(picker);
        this.getChildren().add(tilePane);
        this.getChildren().add(addTP);
        this.getChildren().add(overTilePane);
    }

    private void buildEncounter() {
        for (int i = 1; i < 10; i++) {
            overTileSet.add(new Encounter(i));
        }
        paintOverTileSet();
    }

    private TileTyped getOverTileAt(double x, double y) throws IndexOutOfBoundsException {
        double ratio = Graphic_Const.ratio;
        int tileSize = Graphic_Const.TILES_SIZE;
        double tilesPerLine = Math.round(tilePane.getWidth()/(tileSize*ratio));
        int index = (int) (Math.floor(y/(ratio*tileSize))*tilesPerLine+Math.floor(x/(tileSize*ratio)));
        return overTileSet.get(index);
    }


    public void resizeOptions(double width){
        tilePane.setMaxWidth(width);
        if (!tileSet.isEmpty()) paintTileSet();
        if (!overTileSet.isEmpty()) paintOverTileSet();
    }

    public void createTileSet(File file){
        try {
            int tileSize = Graphic_Const.TILES_SIZE;
            Image image = new Image(file.toURI().toString());
            PixelReader reader = image.getPixelReader();
            double width = image.getWidth();
            double height = image.getHeight();
            long hTile = Math.round(width / 16);
            long vTile = Math.round(height / 16);
            for (int j = 0; j < vTile; j++)
                for (int i = 0; i < hTile; i++) {
                        Image tileSkin = new WritableImage(reader, tileSize * i, tileSize * j, tileSize, tileSize);
                        tileSet.add(new Tile(tileSkin, false));
                    }
            paintTileSet();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

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

    public TileTyped getSelectedTile() {
        return selectedTile;
    }

    private Tile getTileAt(double x, double y) throws IndexOutOfBoundsException{
        double ratio = Graphic_Const.ratio;
        int tileSize = Graphic_Const.TILES_SIZE;
        double tilesPerLine = Math.round(tilePane.getWidth()/(tileSize*ratio));
        int index = (int) (Math.floor(y/(ratio*tileSize))*tilesPerLine+Math.floor(x/(tileSize*ratio)));
        return tileSet.get(index);
    }

    public static EditorPanel getPanel(Stage stage) {
        if (panel==null) panel=new EditorPanel(stage);
        return panel;
    }
    public static EditorPanel getPanel(){
        return panel;
    }
    public void setLevelName(String name){
        levelName.setText(name);
    }
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
