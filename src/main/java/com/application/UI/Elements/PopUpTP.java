package com.application.UI.Elements;

import com.application.Game.Level.LevelElements.Layer1.Warp;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class PopUpTP extends Alert {
    TextField levelName = new TextField();
    TextField xField = new TextField("0");
    TextField yField = new TextField("0");
    ButtonType confirm = new ButtonType("confirm");
    ButtonType cancel = new ButtonType("cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

    /**
     * Unique constructor implementing the behaviour
     */
    public PopUpTP(){
        super(AlertType.CONFIRMATION);
        VBox root = new VBox();
        levelName.setPromptText("destination (level name)");
        xField.setPromptText("X :");
        yField.setPromptText("Y : ");
        root.getChildren().add(new HBox(levelName));
        root.getChildren().add(new Label("position to TP"));
        root.getChildren().add(new HBox(xField,yField));
        this.getDialogPane().setContent(root);
        this.getButtonTypes().setAll(confirm,cancel);
    }

    /**
     * parse and get
     * @return return a warp by reading the fields
     */
    public Warp getTP(){
        this.hide();
        this.close();
        return new Warp(levelName.getText(),Integer.parseInt(xField.getText()),Integer.parseInt(yField.getText()));
    }
}
