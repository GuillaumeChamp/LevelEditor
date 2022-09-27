package com.application.Game.Level.LevelElements.Layer1;

public class Encounter extends OverTile{
    /**
     * Allow to create an overTile which the id represent a set of possible event (described in another file)
     * @param id unique identifier of the events set (must be between 0 and 99)
     * @throws InvalidIdException if the index is protected
     */
    public Encounter(int id){
        if (id<0||id>99) throw new InvalidIdException();
        this.id = id;
    }
}