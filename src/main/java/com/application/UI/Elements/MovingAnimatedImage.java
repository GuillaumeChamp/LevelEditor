package com.application.UI.Elements;

public class MovingAnimatedImage extends AnimatedImage {
    protected double positionX;
    protected double positionY;
    protected double velocityX;
    protected double velocityY;
    protected static double XLim;
    protected static double YLim;

    public MovingAnimatedImage(String path, double duration,int width,int length,int animationId) {
        super(path,width,length,animationId);
        positionX = 0;
        positionY = 0;
        this.duration = duration;
    }

    public double getPositionX() {
        return positionX;
    }

    public double getPositionY() {
        return positionY;
    }

    public void setVelocity(double velocityX,double velocityY) {
        this.velocityX = velocityX;
        this.velocityY = velocityY;
    }
    public void addVelocity(double velocityX,double velocityY) {
        this.velocityX += velocityX;
        this.velocityY += velocityY;
    }
    public void update() {
        //TODO : must follow the patrol of the npc
        positionY += velocityY;
        positionX += velocityX;
        if (0 > positionX) positionX = 0;
        if (0 > positionY) positionY = 0;
        if (positionX > XLim) positionX = XLim;
        if (positionY > YLim) positionY = YLim;
    }
}