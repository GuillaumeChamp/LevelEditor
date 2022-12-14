package com.application;

import com.application.Game.Level.Level;
import com.application.UI.EditorPanel;
import com.application.UI.Graphic_Const;
import com.application.UI.LevelEditorScene;
import com.application.IO.Saver;
import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;
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


        BorderPane borderPane = new BorderPane(splitPane);

        borderPane.setTop(CreateMenu(theStage,level,panel));

        Scene scene = new Scene(borderPane, Graphic_Const.DEFAULT_WIDTH, Graphic_Const.DEFAULT_HEIGHT);
        theStage.setScene(scene);
        theStage.show();
        splitPane.getDividers().get(0).positionProperty().addListener(e-> {
            panel.resizeOptions(splitPane.getDividers().get(0).getPosition()*theStage.getWidth());
            level.resizeOptions(theStage.getWidth()-panel.getWidth());
        });
    }

    /**
     * Load a level
     * @param theStage current stage (to attach alert)
     * @param level levelEditorScene (to attach the level)
     * @param panel LevelEditorPanel (to change prompted information)
     */
    private void loadLevel(Stage theStage, LevelEditorScene level,EditorPanel panel){
        try {
            File file = new FileChooser().showOpenDialog(theStage);
            Level level1 = Saver.loadLevel(file);
            level.setLevel(level1);
            panel.updatePanel(level1);
            panel.createTileSet(new File(file.getAbsolutePath().replace(".level0",".png")));
            panel.loadOverTiles(new File(file.getAbsolutePath().replace(".level0", ".level1")));
        } catch (Exception ignored) {
        }
    }

    /**
     * Create the menu bar
     * @param theStage current stage (to attach alert)
     * @param level levelEditorScene (to attach the level)
     * @param panel LevelEditorPanel (to change prompted information)
     * @return the initialized menuBar
     */
    private MenuBar CreateMenu(Stage theStage,LevelEditorScene level,EditorPanel panel){
        MenuBar menuBar = new MenuBar();
        Menu edit = new Menu("File");
        MenuItem newLevel = new MenuItem("new level");
        edit.getItems().add(newLevel);
        newLevel.setOnAction(e-> level.newLevelRequest());
        MenuItem save = new MenuItem("save");
        save.setOnAction(e->{
            try {
                DirectoryChooser chooser = new DirectoryChooser();
                File rep = chooser.showDialog(theStage);
                level.saveLevel(rep);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        MenuItem load = new MenuItem("load");
        load.setOnAction(e-> loadLevel(theStage,level,panel));
        edit.getItems().add(save);
        edit.getItems().add(load);
        menuBar.getMenus().add(edit);
        return menuBar;
    }

    public static void main(String[] args) {
        Editor.launch();
    }
}