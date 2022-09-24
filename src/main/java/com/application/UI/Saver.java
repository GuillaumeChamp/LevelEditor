package com.application.UI;

import com.application.Game.OutDoor.Level;
import com.application.Game.OutDoor.LevelElements.Tile;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javax.imageio.ImageIO;
import java.io.*;
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
    private static ArrayList<Tile> saveTile(Tile[][] tiles,String levelName) throws IOException {
        int tileSize = Graphic_Const.TILES_SIZE;
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
        return toSave;
    }
    public static void saveLevel(Level level) throws Exception{
        Tile[][] tiles = level.getTiles();
        String name = level.getName();
        ArrayList<Tile> toSave = saveTile(tiles, level.getName());
        int[][] tilesIndex = new int[tiles.length][tiles[0].length];
        for(int i=0;i<tiles.length;i++)
            for(int j=0;j<tiles[0].length;j++)
                tilesIndex[i][j] = toSave.indexOf(tiles[i][j]);
        File file = new File("."+File.separator+"saved"+File.separator+name+".level");
        file.createNewFile();
        ObjectOutputStream oot = new ObjectOutputStream(new FileOutputStream(file));
        oot.writeObject(tilesIndex);
        oot.flush();
        oot.close();
    }
    public static Level loadLevel(File levelFile) throws Exception{
        ObjectInputStream oot = new ObjectInputStream(new FileInputStream(levelFile));
        int[][] tilesIndex = (int[][]) oot.readObject();
        oot.close();
        int height = tilesIndex.length;
        int width = tilesIndex[0].length;
        int tileSize = Graphic_Const.TILES_SIZE;

        Level level = new Level(height,width);

        String name = levelFile.getName();
        name = name.replace(".level","");
        level.setName(name);
        String texturePath = levelFile.getPath().replace(".level",".png");
        Image image = new Image(texturePath);
        PixelReader reader = image.getPixelReader();
        ArrayList<Tile> SavedTile = new ArrayList<>();
        for (int j = 0; j < image.getWidth()/16; j++) {
            Image tileSkin = new WritableImage(reader, tileSize*j, 0, tileSize, tileSize);
            SavedTile.add(new Tile(tileSkin, false));
        }
        for(int i=0;i<height;i++)
            for(int j=0;j<width;j++)
                level.getTiles()[i][j] = SavedTile.get(tilesIndex[i][j]);
        return level;
    }

}
