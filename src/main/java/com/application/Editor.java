package com.application;

import com.application.Game.Level.Level;
import com.application.UI.EditorPanel;
import com.application.UI.Graphic_Const;
import com.application.UI.LevelEditorScene;
import com.application.Game.Level.Saver;
import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.File;
import java.util.Optional;

public class Editor extends Application {
    SplitPane splitPane;
    public void start(Stage theStage) {

        theStage.setTitle("Level Editor");

        int borderXSize = 0;
        int borderYSize = 70;

        Graphic_Const.DEFAULT_WIDTH = Screen.getPrimary().getBounds().getWidth()-borderXSize;
        Graphic_Const.DEFAULT_HEIGHT = Screen.getPrimary().getBounds().getHeight()-borderYSize;

        theStage.setOnCloseRequest(event -> {
            //your code goes here
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            ButtonType yes = new ButtonType("YES");
            ButtonType no = new ButtonType("no");
            confirm.setTitle("Are you sure ?");
            confirm.setHeaderText("If you leave now some of your progress might be lost");
            confirm.getButtonTypes().setAll(yes,no);
            Optional<ButtonType> ans = confirm.showAndWait();
            assert ans.isPresent();
            if (!ans.get().equals(yes)) event.consume();
        });
        EditorPanel panel = EditorPanel.getPanel(theStage);
        LevelEditorScene level = LevelEditorScene.getLevelEditorScene();

        splitPane = new SplitPane(panel,level);
        splitPane.setOrientation(Orientation.HORIZONTAL);
        theStage.showingProperty().addListener(e-> splitPane.setDividerPosition(0,0.4));
        splitPane.getDividers().get(0).positionProperty().addListener(e-> panel.resizeOptions(splitPane.getDividers().get(0).getPosition()*theStage.getWidth()));

        // place splitPane as center
        BorderPane borderPane = new BorderPane(splitPane);
        MenuBar menuBar = new MenuBar();
        Menu edit = new Menu("File");
        MenuItem newLevel = new MenuItem("new level");
        edit.getItems().add(newLevel);
        newLevel.setOnAction(e-> level.newLevelRequest());
        MenuItem save = new MenuItem("save");
        save.setOnAction(e->{
            try {
                level.saveLevel();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        MenuItem load = new MenuItem("load");
        load.setOnAction(e->{
            try {
                File file = new FileChooser().showOpenDialog(theStage);
                Level level1 = Saver.loadLevel(file);
                level.setLevel(level1);
                panel.setLevelName(level1.getName());
                panel.createTileSet(new File(file.getAbsolutePath().replace(".level0",".png")));
                panel.loadOverTiles(new File(file.getAbsolutePath().replace(".level0", ".level1")));
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        edit.getItems().add(save);
        edit.getItems().add(load);
        menuBar.getMenus().add(edit);
        borderPane.setTop(menuBar);

        Scene scene = new Scene(borderPane, Graphic_Const.DEFAULT_WIDTH, Graphic_Const.DEFAULT_HEIGHT);
        theStage.setScene(scene);
        theStage.show();
    }

    public static void main(String[] args) {
        Editor.launch();
    }
}