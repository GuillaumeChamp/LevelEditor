package Figth;

import javafx.scene.image.Image;

public abstract class Enemy extends Entity{
    public Enemy(int hp, int armor, int speed, int damage, String name, Image[] skin) {
        super(hp, armor, speed, damage, name, skin);
    }
    // Todo : add target selection
}