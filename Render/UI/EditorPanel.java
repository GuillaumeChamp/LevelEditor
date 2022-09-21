package UI;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


public class EditorPanel extends VBox {
    static EditorPanel panel;
    FileChooser load;
    Button picker;
    private EditorPanel(Stage stage,double width,double height) {
        this.setWidth(width);
        this.setHeight(height);
         load =new FileChooser();
         picker =new Button("Import TileSet");
         picker.setOnContextMenuRequested(e->{
             load.showOpenDialog(stage);
             System.out.println(load.getTitle());
         });
         this.getChildren().add(picker);
         this.getChildren().add(new Label("Editor"));
    }

    public static EditorPanel getPanel(Stage stage,double width,double height) {
        if (panel==null) panel=new EditorPanel(stage,width,height);
        return panel;
    }
}
