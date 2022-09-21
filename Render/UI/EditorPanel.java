package UI;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;


public class EditorPanel extends VBox {
    static EditorPanel panel;
    GraphicsContext gc;
    ScrollPane tilePane;
    Canvas canvas;
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
        picker.setOnAction(e->{
            File file = load.showOpenDialog(stage);
            paintTileSet(file);
        });
        //Adding elements
        this.getChildren().add(new Label("Editor"));
        this.getChildren().add(picker);
        this.getChildren().add(tilePane);
    }
    public void resizeOptions(double width,double height){
        canvas.resize(width,height);
        tilePane.resize(width, height);
    }

    private void paintTileSet(File file) {
        Image image = new Image(file.getAbsolutePath());
        gc.drawImage(image,0,0,canvas.getWidth(),canvas.getHeight());
    }


    public static EditorPanel getPanel(Stage stage,double width,double height) {
        if (panel==null) panel=new EditorPanel(stage,width,height);
        return panel;
    }
}
