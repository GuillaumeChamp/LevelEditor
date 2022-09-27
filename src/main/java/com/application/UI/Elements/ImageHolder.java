package com.application.UI.Elements;

import javafx.scene.image.Image;

import java.util.ArrayList;

/**
 * This class must be call instead of create new image
 */
public class ImageHolder {
    static ArrayList<Image> images = new ArrayList<>();
    static ArrayList<String> tag = new ArrayList<>();

    /**
     * get an image using is memory or create it if not yet saved
     * @param name path of the image (like on javafx image)
     * @return the requested image
     */
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
