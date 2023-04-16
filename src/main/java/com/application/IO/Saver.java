package com.application.IO;

import com.application.Game.Level.Level;
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
    final static String extensionImage = ".png";

    /**
     * Use to select all texture to save keeping only usefully textures
     * @param tiles the whole tile set (details and ground)
     * @return reduced tiles set in an array list
     */
    private static ArrayList<Tile> selectTiles(Tile[][] tiles,Tile[][] details){
        ArrayList<Tile> selected = new ArrayList<>();
        for (Tile[] tile : tiles) {
            for (int j = 0; j < tiles[0].length; j++) {
                if (!selected.contains(tile[j])) selected.add(tile[j]);
            }
        }
        for (Tile[] tile : details) {
            for (int j = 0; j < tiles[0].length; j++) {
                if (tile[j]!=null)
                    if (!selected.contains(tile[j])) selected.add(tile[j]);
            }
        }
        return selected;
    }

    /**
     * Save texture of tiles in a png file
     * @param selectedTiles all unique tiles (use selectTiles(...))
     * @param levelName name of the output file
     * @throws IOException file security or write issues
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    private static void saveTilesSkin(ArrayList<Tile> selectedTiles, String levelName, String path) throws IOException {
        int tileSize = Graphic_Const.TILES_SIZE;
        File imageFile = new File(path+levelName+extensionImage);
        imageFile.mkdirs();
        imageFile.createNewFile();
        WritableImage image = new WritableImage(selectedTiles.size()* tileSize,tileSize);
        PixelWriter writer = image.getPixelWriter();
        PixelReader reader;
        for (int i=0;i<selectedTiles.size();i++) {
            reader = selectedTiles.get(i).getSkin().getPixelReader();
            writer.setPixels(tileSize * i, 0, tileSize, tileSize, reader, 0, 0);
        }
        ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", imageFile);
    }

    /**
     * Save the whole level (including tiles, overTiles and tile's skin)
     * @param level level to save
     * @throws Exception file and files stream exceptions
     * produce three files in the app repository
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void saveLevel(Level level, File rep) throws Exception{
        if (rep==null)return;
        String path = rep.getAbsolutePath()+File.separator;
        Tile[][] groundLayerTiles = level.getGroundLayerTiles();
        Tile[][] details = level.getDetails();
        OverTile[][] overTiles = level.getBehaviourTiles();
        String name = level.getName();
        ArrayList<Tile> uniqueTilesList = selectTiles(groundLayerTiles,details);
        saveTilesSkin(uniqueTilesList,name,path);
        int[][] groundIndex = new int[groundLayerTiles.length][groundLayerTiles[0].length];
        int[][] detailIndex = new int[details.length][details[0].length];
        for(int i=0;i<groundLayerTiles.length;i++)
            for(int j=0;j<groundLayerTiles[0].length;j++){
                groundIndex[i][j] = uniqueTilesList.indexOf(groundLayerTiles[i][j]);
                detailIndex[i][j] = uniqueTilesList.indexOf(details[i][j]);
            }

        File file = new File(path+name+extension0);
        file.createNewFile();
        ObjectOutputStream oot = new ObjectOutputStream(Files.newOutputStream(file.toPath()));
        oot.writeObject(groundIndex);
        oot.writeObject(detailIndex);
        oot.flush();
        oot.close();

        JsonIO.saveOverTiles(overTiles,path+name+".json",name);
    }

    /**
     * Load a level from file
     * @param levelFile can be .level0 ,.json or .png
     * @return the loaded level
     * @throws Exception IO Exception if one png or .level0 not found
     */
    public static Level loadLevel(File levelFile) throws Exception{
        String name = levelFile.getName().replace(extension0,"");
        name = name.replace(".json","");
        String path = levelFile.getAbsolutePath().replace(levelFile.getName(),"");
        ObjectInputStream oot = new ObjectInputStream(Files.newInputStream(new File(path + name + extension0).toPath()));
        int[][] tilesIndex = (int[][]) oot.readObject();

        int height = tilesIndex.length;
        int width = tilesIndex[0].length;
        int tileSize = Graphic_Const.TILES_SIZE;

        int[][] detailsIndex = (int[][]) oot.readObject();
        OverTile[][] overTiles;
        try{
             overTiles= JsonIO.LoadOverTiles(path + name + ".json");
        }catch (IOException e){
            overTiles=new OverTile[height][width];
        }
        Level level = new Level(height,width);

        level.setName(name);
        String texturePath = path+name+extensionImage;
        Image image = new Image(new File(texturePath).toURI().toString());
        PixelReader reader = image.getPixelReader();
        ArrayList<Tile> SavedTile = new ArrayList<>();
        level.setBehaviourTiles(overTiles);
        for (int j = 0; j < image.getWidth()/16; j++) {
            Image tileSkin = new WritableImage(reader, tileSize*j, 0, tileSize, tileSize);
            SavedTile.add(new Tile(tileSkin));
        }
        Tile[][] ground = new Tile[height][width];
        Tile[][] details = new Tile[height][width];
        for(int i=0;i<height;i++)
            for(int j=0;j<width;j++){
                ground[i][j] = SavedTile.get(tilesIndex[i][j]);
                if (detailsIndex[i][j]!=-1) details[i][j] = SavedTile.get(detailsIndex[i][j]);
            }
        level.setGroundLayerTiles(ground);
        level.setDetails(details);
        return level;
    }


}
