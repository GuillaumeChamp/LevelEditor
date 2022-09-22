import UI.EditorPanel;
import UI.LevelEditorScene;
import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
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

        splitPane = new SplitPane(
                panel,new LevelEditorScene(defaultWidth,defaultHeight));
        splitPane.setOrientation(Orientation.HORIZONTAL);
        theStage.showingProperty().addListener(e-> splitPane.setDividerPosition(0,0.4));
        splitPane.getDividers().get(0).positionProperty().addListener(e-> panel.resizeOptions(splitPane.getDividers().get(0).getPosition()*theStage.getWidth(),theStage.getHeight()));

        // place splitPane as center
        BorderPane borderPane = new BorderPane(splitPane);

        borderPane.setTop(new MenuBar(new Menu("File")));

        Scene scene = new Scene(borderPane, defaultWidth, defaultHeight);
        theStage.setScene(scene);
        theStage.show();
    }

    public static void main(String[] args) {
        Editor.launch();
    }
}