package com.application.UI.Elements;

import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class PopUpName extends Alert {
    TextField levelName = new TextField();
    ButtonType confirm = new ButtonType("confirm");
    ButtonType cancel = new ButtonType("cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

    public PopUpName() {
        super(AlertType.CONFIRMATION);
        this.setTitle("New Level");
        VBox root = new VBox();
        levelName.setPromptText("Level Name");
        root.getChildren().add(new HBox(levelName));
        this.getDialogPane().setContent(root);
        this.getButtonTypes().setAll(confirm,cancel);
    }
    public String getName(){
        this.close();
        return levelName.getText();
    }
}
