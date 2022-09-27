package com.application.Game.Level.LevelElements.Layer1;

public class InvalidIdException extends ArrayIndexOutOfBoundsException{
    public InvalidIdException() {
        super("Invalid Id for an OverTile of this type");
    }
    public InvalidIdException(String s) {
        super(s);
    }
}
