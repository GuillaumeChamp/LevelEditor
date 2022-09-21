package UI;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;

import java.util.ArrayList;

public class LevelEditorScene extends VBox {
    static protected Canvas canvas;

    public LevelEditorScene(Parent parent,Canvas canvas,double width,double height) {

        this.setWidth(width);
        this.setHeight(height);

        this.getChildren().add(new Label("the place to draw"));
        LevelEditorScene.canvas = canvas;

    }

    public void paint(){
        GraphicsContext gc = canvas.getGraphicsContext2D();
    }

}
