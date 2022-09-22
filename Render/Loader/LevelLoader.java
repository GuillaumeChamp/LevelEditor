package Loader;

import Game.OutDoor.Level;

public class LevelLoader extends Loader<Level> {
    @Override
    public Level load(String name) {
        return new Level(128,72);
    }
}
