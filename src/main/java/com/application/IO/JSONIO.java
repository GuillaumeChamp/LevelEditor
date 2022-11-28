package com.application.IO;

import com.application.Game.Level.LevelElements.Layer1.Collision;
import com.application.Game.Level.LevelElements.Layer1.Encounter;
import com.application.Game.Level.LevelElements.Layer1.OverTile;
import com.application.Game.Level.LevelElements.Layer1.Warp;
import org.json.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public interface JSONIO {
     static void saveOverTiles(OverTile[][] overTiles, String path, String levelName) throws IOException {
         FileWriter json = new FileWriter(path);
         JSONObject topLevel = new JSONObject();
         topLevel.put("level",levelName);
         topLevel.put("width",overTiles[0].length);
         topLevel.put("length",overTiles.length);
         JSONArray array = new JSONArray();
         for(int y=0;y<overTiles.length;y++){
            for (int x=0;x<overTiles[y].length;x++){
                if (overTiles[y][x] == null) continue;
                JSONObject object = new JSONObject();
                object.put("id",overTiles[y][x].getId());
                object.put("x",x);
                object.put("y",y);
                if (overTiles[y][x].getClass().equals(Warp.class)) {
                    object.put("destination",((Warp) overTiles[y][x]).getExit());
                    object.put("xDestination",((Warp) overTiles[y][x]).getxDes());
                    object.put("yDestination",((Warp) overTiles[y][x]).getyDes());
                }
                array.put(object);
            }
         }
         topLevel.put("overTile", array);
         json.write(topLevel.toString());
         json.flush();
         json.close();
     }
     static OverTile[][] LoadOverTiles(String path) throws IOException {
         InputStream is = Files.newInputStream(Paths.get(path));
         JSONTokener token = new JSONTokener(is);
         JSONObject object = new JSONObject(token);
         OverTile[][] overTiles = new OverTile[object.getInt("length")][object.getInt("width")];
         JSONArray array = object.getJSONArray("overTile");
         for (int i = 0; i < array.length(); i++) {
             JSONObject tile = (JSONObject) array.get(i);
             try {
                 tile.get("destination");
                 overTiles[tile.getInt("y")][ tile.getInt("x")] =
                         new Warp(tile.getString("destination"),tile.getInt("xDestination"),tile.getInt("yDestination"));
             }catch (JSONException keyNotFound){
                 int id = tile.getInt("id");
                 if (id>0) overTiles[ tile.getInt("y")][ tile.getInt("x")] =
                         new Encounter( tile.getInt("id"));
                 else overTiles[ tile.getInt("y")][ tile.getInt("x")] = new Collision();
             }
         }
         return overTiles;
     }
}
