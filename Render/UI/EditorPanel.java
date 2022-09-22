package UI;

import Game.OutDoor.LevelElements.Tile;
import javafx.geometry.Bounds;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;


public class EditorPanel extends VBox {
    static EditorPanel panel;
    GraphicsContext gc;
    ScrollPane tilePane;
    Canvas canvas;
    ArrayList<Tile> tileSet =new ArrayList<>();

    private EditorPanel(Stage stage,double width,double height) {
        //Sizing
        this.setWidth(width);
        this.setHeight(height);
        //Creating component
        FileChooser load =new FileChooser();
        Button picker =new Button("Import TileSet");
        canvas = new Canvas(this.getWidth(),this.getWidth());
        tilePane = new ScrollPane(canvas);
        tilePane.resize(this.getWidth(),this.getWidth());
        gc = canvas.getGraphicsContext2D();
        //Adding behaviour
        tilePane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        picker.setOnAction(e->{
            File file = load.showOpenDialog(stage);
            createTileSet(file);
            paintTileSet();
        });
        //Adding elements
        this.getChildren().add(new Label("Editor"));
        this.getChildren().add(picker);
        this.getChildren().add(tilePane);
    }
    public void resizeOptions(double width,double height){
        tilePane.setMaxWidth(width);
        if (!tileSet.isEmpty()) paintTileSet();
    }

    private void createTileSet(File file){
        Image image = new Image(file.getAbsolutePath());
        PixelReader reader = image.getPixelReader();
        double width = image.getWidth();
        double height = image.getHeight();
        long hTile = Math.round(width/16);
        long vTile = Math.round(height/16);
        for (int i = 0; i < hTile; i++) {
            for (int j = 0; j < vTile; j++) {
                Image tileSkin = new WritableImage(reader,16*i,16*j,16,16);
                tileSet.add(new Tile(tileSkin,"tile"+(i+j),false));
            }
        }
    }

    private void paintTileSet() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        double x = 0;
        double y = 0;
        double ratio = Graphic_Const.ratio;
        int tileSize = Graphic_Const.H_TILES_SIZE;
        double tilesPerLine = Math.round(tilePane.getWidth()/(tileSize*ratio));
        double xMax = (tilesPerLine-1)*tileSize;
        //canvas.setHeight(tilesPerRow*tileSize*ratio);
        gc.setStroke(Color.BLACK);
        for (Tile t: tileSet) {
            gc.drawImage(t.getSkin(),x*ratio,y*ratio,tileSize*ratio,tileSize*ratio);
            gc.strokeRect(x*ratio,y*ratio,tileSize*ratio,tileSize*ratio);
            x = x+tileSize;
            if (x>=xMax){
                x = 0;
                y = y+tileSize;
            }
        }

    }


    public static EditorPanel getPanel(Stage stage,double width,double height) {
        if (panel==null) panel=new EditorPanel(stage,width,height);
        return panel;
    }
}
