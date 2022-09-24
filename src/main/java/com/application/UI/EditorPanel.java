package com.application.UI;

import com.application.Game.OutDoor.LevelElements.Tile;
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
import java.util.ArrayList;


public class EditorPanel extends VBox {
    static EditorPanel panel;
    GraphicsContext gc;
    ScrollPane tilePane;
    Canvas canvas;
    ArrayList<Tile> tileSet =new ArrayList<>();
    Tile selectedTile;

    private EditorPanel(Stage stage,double width,double height) {
        //Sizing
        this.setWidth(width);
        this.setHeight(height);
        //Creating component
        TextField widthField = new TextField("200");
        TextField heightField = new TextField("200");
        TextField levelName = new TextField("untitled");
        FileChooser load =new FileChooser();
        Button picker =new Button("Import TileSet");
        canvas = new Canvas(this.getWidth(),this.getWidth());
        tilePane = new ScrollPane(canvas);
        tilePane.resize(this.getWidth(),this.getWidth());
        gc = canvas.getGraphicsContext2D();
        //Adding behaviour
        widthField.setTextFormatter(new TextFormatter<>(new NumberStringConverter()));
        heightField.setTextFormatter(new TextFormatter<>(new NumberStringConverter()));
        tilePane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        canvas.setOnMouseClicked(e->{
            double x = e.getX();
            double y = e.getY();
            try {
                selectedTile = getTileAt(x,y);
            } catch (Exception ex) {
                System.out.println("Please select a tileset");
                return;
            }
            paintTileSet();
        });
        widthField.setText("200");
        heightField.setText("200");
        widthField.setOnKeyPressed(e->{
            if (e.getCode().equals(KeyCode.ENTER)) {
                LevelEditorScene.getLevelEditorScene(0,0).changeSize(Integer.parseInt(widthField.getText()),Integer.parseInt(heightField.getText()));
            }
        });
        heightField.setOnKeyPressed(e->{
            if (e.getCode().equals(KeyCode.ENTER)) {
                LevelEditorScene.getLevelEditorScene(0,0).changeSize(Integer.parseInt(widthField.getText()),Integer.parseInt(heightField.getText()));
            }
        });
        levelName.setOnAction(e-> LevelEditorScene.getLevelEditorScene(0,0).rename(levelName.getText()));
        picker.setOnAction(e->{
            File file = load.showOpenDialog(stage);
            createTileSet(file);
        });
        //Adding elements
        this.getChildren().add(new HBox(widthField,heightField));
        this.getChildren().add(new VBox(new Label("level Name"),levelName));
        this.getChildren().add(new Label("main.java.com.application.Editor"));
        this.getChildren().add(picker);
        this.getChildren().add(tilePane);
    }


    public void resizeOptions(double width,double height){
        tilePane.setMaxWidth(width);
        if (!tileSet.isEmpty()) paintTileSet();
    }

    public void createTileSet(File file){
        try {
            int tileSize = Graphic_Const.TILES_SIZE;
            Image image = new Image(file.getAbsolutePath());
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
        }catch (Exception ignored){}
    }

    private void paintTileSet() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
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
        if (selectedTile!=null){
            int index = tileSet.indexOf(selectedTile);
            gc.setLineWidth(5);
            gc.setStroke(Color.RED);
            double xx = (index%tilesPerLine)*tileSize;
            double yy =  Math.floor(index/tilesPerLine)*tileSize;
            gc.strokeRect(xx*ratio,yy*ratio,tileSize*ratio,tileSize*ratio);
        }
    }

    public Tile getSelectedTile() {
        return selectedTile;
    }

    private Tile getTileAt(double x, double y) throws IndexOutOfBoundsException{
        double ratio = Graphic_Const.ratio;
        int tileSize = Graphic_Const.TILES_SIZE;
        double tilesPerLine = Math.round(tilePane.getWidth()/(tileSize*ratio));
        int index = (int) (Math.floor(y/(ratio*tileSize))*tilesPerLine+Math.floor(x/(tileSize*ratio)));
        return tileSet.get(index);
    }

    public static EditorPanel getPanel(Stage stage,double width,double height) {
        if (panel==null) panel=new EditorPanel(stage,width,height);
        return panel;
    }
    public static EditorPanel getPanel(){
        return panel;
    }
}
