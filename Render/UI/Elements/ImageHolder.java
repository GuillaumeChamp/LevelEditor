package UI.Elements;

import javafx.scene.image.Image;

import java.util.ArrayList;

public class ImageHolder {
    static ArrayList<Image> images = new ArrayList<>();
    static ArrayList<String> tag = new ArrayList<>();
    public static Image getImage(String name){
        if (tag.contains(name)){
            return images.get(tag.indexOf(name));
        }
        Image im = new Image(name,16,16,false,false);
        images.add(im);
        tag.add(name);
        return im;
    }
}
