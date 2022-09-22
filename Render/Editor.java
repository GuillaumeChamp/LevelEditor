import UI.EditorPanel;
import UI.LevelEditorScene;
import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Editor extends Application {
    SplitPane splitPane;
    public void start(Stage theStage) {

        theStage.setTitle("Render");

        int borderXSize = 0;
        int borderYSize = 70;

        final double defaultWidth = Screen.getPrimary().getBounds().getWidth()-borderXSize;
        final double defaultHeight = Screen.getPrimary().getBounds().getHeight()-borderYSize;


        theStage.setOnCloseRequest(e->{});
        EditorPanel panel = EditorPanel.getPanel(theStage,defaultWidth,defaultHeight);
        LevelEditorScene level = LevelEditorScene.getLevelEditorScene(defaultWidth,defaultHeight);

        splitPane = new SplitPane(panel,level);
        splitPane.setOrientation(Orientation.HORIZONTAL);
        theStage.showingProperty().addListener(e-> splitPane.setDividerPosition(0,0.4));
        splitPane.getDividers().get(0).positionProperty().addListener(e-> panel.resizeOptions(splitPane.getDividers().get(0).getPosition()*theStage.getWidth(),theStage.getHeight()));

        // place splitPane as center
        BorderPane borderPane = new BorderPane(splitPane);
        MenuBar menuBar = new MenuBar();
        Menu edit = new Menu("Edit");
        MenuItem newLevel = new MenuItem("new level");
        edit.getItems().add(newLevel);
        newLevel.setOnAction(e-> level.newLevelRequest());
        menuBar.getMenus().add(edit);
        borderPane.setTop(menuBar);

        Scene scene = new Scene(borderPane, defaultWidth, defaultHeight);
        theStage.setScene(scene);
        theStage.show();
    }

    public static void main(String[] args) {
        Editor.launch();
    }
}