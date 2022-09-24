package com.application.Game.OutDoor.LevelElements;


import com.application.UI.Elements.MovingAnimatedImage;
import com.application.UI.Graphic_Const;

public class PNJ {
    protected String name;
    protected MovingAnimatedImage entity;
    protected String[] dialogue;
    protected int state = 0;
    protected int[] startX;
    protected int[] startY;

    //TODO add multi sprite option
    public PNJ(String name, String skinName, String[] dialogue, int[] startX, int[] startY) {
        this.name = name;
        this.entity = new MovingAnimatedImage(skinName,1, Graphic_Const.SPRITE_WIDTH,Graphic_Const.SPRITE_HEIGHT,0);
        this.dialogue = dialogue;
        this.startX = startX;
        this.startY = startY;
    }

    public String talk() {
        try {
            return dialogue[state];
        }catch (ArrayIndexOutOfBoundsException e){
            return "Error";
        }
    }

    public void changeState(int state){
        this.state=state;
    }

    public int getStartX() {
        try {
            return startX[state];
        }catch (ArrayIndexOutOfBoundsException e){
            System.out.println(name + " " + state + "not exist for X pos");
            return startX[0];
        }
    }

    public int getStartY() {
        try {
            return startY[state];
        }catch (ArrayIndexOutOfBoundsException e){
            System.out.println(name + " " + state + "not exist for Y pos");
            return startY[0];
        }
    }
}