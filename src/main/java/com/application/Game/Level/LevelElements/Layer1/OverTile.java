package com.application.Game.Level.LevelElements.Layer1;

import com.application.Game.Level.LevelElements.TileTyped;

import java.io.Serializable;

/**
 * This class is the base of all tiles of layer 1
 */
public abstract class OverTile implements TileTyped, Serializable {
    protected int id;

    /**
     * Simple getter
     * @return the id of this overTile which is used to display
     */
    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "OverTile{" +
                "id=" + id +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof OverTile)
            return ((OverTile) obj).id == this.id;
        return false;
    }
}
