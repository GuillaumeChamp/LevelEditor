package Loader;

import Game.OutDoor.LevelElements.Tile;
import UI.Graphic_Const;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Saver {
    private static ArrayList<Tile> selectTiles(Tile[][] tiles){
        ArrayList<Tile> selected = new ArrayList<>();
        for (Tile[] tile : tiles) {
            for (int j = 0; j < tiles[0].length; j++) {
                if (!selected.contains(tile[j])) selected.add(tile[j]);
            }
        }
        return selected;
    }
    @SuppressWarnings("All")
    public static void saveTile(Tile[][] tiles,String levelName) throws IOException {
        int tileSize = Graphic_Const.H_TILES_SIZE;
        ArrayList<Tile> toSave = selectTiles(tiles);
        File file = new File("."+File.separator+"saved"+File.separator+levelName+".png");
        file.mkdirs();
        file.createNewFile();
        WritableImage image = new WritableImage(toSave.size()* tileSize,tileSize);
        PixelWriter writer = image.getPixelWriter();
        PixelReader reader;
        for (int i=0;i<toSave.size();i++) {
            reader = toSave.get(i).getSkin().getPixelReader();
            writer.setPixels(tileSize * i, 0, tileSize, tileSize, reader, 0, 0);
        }
        ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
    }
}
