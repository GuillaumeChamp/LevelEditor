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

        Group root = new Group();
        int borderXSize = 0;
        int borderYSize = 70;

        final double defaultWidth = Screen.getPrimary().getBounds().getWidth()-borderXSize;
        final double defaultHeight = Screen.getPrimary().getBounds().getHeight()-borderYSize;
        Canvas canvas = new Canvas(defaultWidth,defaultHeight);

        root.getChildren().add(canvas);

        theStage.setOnCloseRequest(e->{});

        splitPane = new SplitPane(
                EditorPanel.getPanel(theStage,defaultWidth*0.4,defaultHeight),new LevelEditorScene(canvas,defaultWidth*0.6,defaultHeight));
        splitPane.setOrientation(Orientation.HORIZONTAL);
        theStage.showingProperty().addListener(e-> splitPane.setDividerPosition(0,0.4));

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