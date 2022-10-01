package com.application.Game.Level;

import com.application.Game.Level.LevelElements.Layer0.Tile;
import com.application.Game.Level.LevelElements.Layer1.OverTile;
import com.application.UI.Graphic_Const;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

import javax.imageio.ImageIO;
import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;

public class Saver {
    final static String extension0 = ".level0";
    final static String extension1 = ".level1";
    final static String extensionImage = ".png";

    /**
     * Use to select all texture to save keeping only usefully textures
     * @param tiles the whole tile set
     * @return reduced tiles set in an array list
     */
    private static ArrayList<Tile> selectTiles(Tile[][] tiles){
        ArrayList<Tile> selected = new ArrayList<>();
        for (Tile[] tile : tiles) {
            for (int j = 0; j < tiles[0].length; j++) {
                if (!selected.contains(tile[j])) selected.add(tile[j]);
            }
        }
        return selected;
    }

    /**
     * Save tiles
     * @param tiles all tiles
     * @param levelName name of the output file
     * @return minimal list of tiles
     * @throws IOException file security or write issues
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    private static ArrayList<Tile> saveTile(Tile[][] tiles,String levelName,String path) throws IOException {
        int tileSize = Graphic_Const.TILES_SIZE;
        ArrayList<Tile> toSave = selectTiles(tiles);
        File file = new File(path+levelName+extensionImage);
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

    /**
     * Save the whole level (including tiles, overTiles and tile's skin)
     * @param level level to save
     * @throws Exception file and files stream exceptions
     * produce three files in the app repository
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void saveLevel(Level level,File rep) throws Exception{
        String path = rep.getAbsolutePath()+File.separator;
        Tile[][] tiles = level.getTiles();
        OverTile[][] overTiles = level.getOverTiles();
        String name = level.getName();
        ArrayList<Tile> toSave = saveTile(tiles,name,path);
        int[][] tilesIndex = new int[tiles.length][tiles[0].length];
        for(int i=0;i<tiles.length;i++)
            for(int j=0;j<tiles[0].length;j++)
                tilesIndex[i][j] = toSave.indexOf(tiles[i][j]);
        File file = new File(path+name+extension0);
        file.createNewFile();
        ObjectOutputStream oot = new ObjectOutputStream(Files.newOutputStream(file.toPath()));
        oot.writeObject(tilesIndex);
        oot.flush();
        oot.close();

        File file1 = new File(path+name+extension1);
        file1.createNewFile();
        ObjectOutputStream oot1 = new ObjectOutputStream(Files.newOutputStream(file1.toPath()));
        oot1.writeObject(overTiles);
        oot1.flush();
        oot1.close();
    }

    /**
     * Load a level from file
     * @param levelFile can be .level0 ,.level1 or .png
     * @return the loaded level
     * @throws Exception if one of the three files is missing
     */
    public static Level loadLevel(File levelFile) throws Exception{
        String name = levelFile.getName().replace(extension0,"");
        String path = levelFile.getAbsolutePath().replace(levelFile.getName(),"");
        ObjectInputStream oot = new ObjectInputStream(Files.newInputStream(new File(path + name + extension0).toPath()));
        ObjectInputStream oot1 = new ObjectInputStream(Files.newInputStream(new File(path + name + extension1).toPath()));
        int[][] tilesIndex = (int[][]) oot.readObject();
        OverTile[][] overTiles = (OverTile[][]) oot1.readObject();
        oot.close();
        oot1.close();
        int height = tilesIndex.length;
        int width = tilesIndex[0].length;
        int tileSize = Graphic_Const.TILES_SIZE;

        Level level = new Level(height,width);

        level.setName(name);
        String texturePath = path+name+extensionImage;
        Image image = new Image(new File(texturePath).toURI().toString());
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
