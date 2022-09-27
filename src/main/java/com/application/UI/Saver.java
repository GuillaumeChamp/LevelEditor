package com.application.UI;

import com.application.Game.Level.Level;
import com.application.Game.Level.LevelElements.Layer0.Tile;
import com.application.Game.Level.LevelElements.Layer1.OverTile;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javax.imageio.ImageIO;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Saver {
    final static String rep = "."+File.separator+"saved"+File.separator;
    final static String extension0 = ".level0";
    final static String extension1 = ".level1";
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
        File file = new File(rep+levelName+".png");
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
    @SuppressWarnings("All")
    public static void saveLevel(Level level) throws Exception{
        Tile[][] tiles = level.getTiles();
        OverTile[][] overTiles = level.getOverTiles();
        String name = level.getName();
        ArrayList<Tile> toSave = saveTile(tiles, level.getName());
        int[][] tilesIndex = new int[tiles.length][tiles[0].length];
        for(int i=0;i<tiles.length;i++)
            for(int j=0;j<tiles[0].length;j++)
                tilesIndex[i][j] = toSave.indexOf(tiles[i][j]);
        File file = new File(rep+name+extension0);
        file.createNewFile();
        ObjectOutputStream oot = new ObjectOutputStream(new FileOutputStream(file));
        oot.writeObject(tilesIndex);
        oot.flush();
        oot.close();

        File file1 = new File(rep+name+extension1);
        file1.createNewFile();
        ObjectOutputStream oot1 = new ObjectOutputStream(new FileOutputStream(file1));
        oot1.writeObject(overTiles);
        oot1.flush();
        oot1.close();
    }
    public static Level loadLevel(File levelFile) throws Exception{
        ObjectInputStream oot = new ObjectInputStream(Files.newInputStream(levelFile.toPath()));
        ObjectInputStream oot1 = new ObjectInputStream(Files.newInputStream(Paths.get(levelFile.getPath().replace(extension0, extension1))));
        int[][] tilesIndex = (int[][]) oot.readObject();
        OverTile[][] overTiles = (OverTile[][]) oot1.readObject();
        oot.close();
        oot1.close();
        int height = tilesIndex.length;
        int width = tilesIndex[0].length;
        int tileSize = Graphic_Const.TILES_SIZE;

        Level level = new Level(height,width);

        String name = levelFile.getName();
        name = name.replace(extension0,"");
        level.setName(name);
        String texturePath = levelFile.toURI().toString().replace(extension0,".png");
        Image image = new Image(texturePath);
        PixelReader reader = image.getPixelReader();
        ArrayList<Tile> SavedTile = new ArrayList<>();
        level.setOverTiles(overTiles);
        for (int j = 0; j < image.getWidth()/16; j++) {
            Image tileSkin = new WritableImage(reader, tileSize*j, 0, tileSize, tileSize);
            SavedTile.add(new Tile(tileSkin));
        }
        for(int i=0;i<height;i++)
            for(int j=0;j<width;j++)
                level.getTiles()[i][j] = SavedTile.get(tilesIndex[i][j]);
        return level;
    }


}
