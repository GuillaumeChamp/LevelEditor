package com.application.Game.Universal;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;

public class ImageCropper {
    /**
     * Auto crop an image
     * @param path path to the image
     * @param animationId Id of the animation (line)
     * @param XStep space between two sprites
     * @param YStep space between two sprites
     * @return cropped image
     */
    public static Image[] crop(String path,int animationId,int XStep,int YStep){
        Image file = new Image(path);
        int column = (int) (file.getWidth()/XStep);
        if (column<0) column=1;
        Image[] output = new Image[column];
        PixelReader reader = file.getPixelReader();
        for (int l=0;l<column;l++) {
            output[l] = new WritableImage(reader, XStep * l,YStep*animationId, XStep, YStep);
        }
        return output;
    }
}