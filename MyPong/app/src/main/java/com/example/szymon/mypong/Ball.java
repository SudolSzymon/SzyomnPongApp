package com.example.szymon.mypong;

import android.graphics.RectF;

import java.util.Random;

/**
 * Created by Szymon on 26/03/2018.
 */

public class Ball {
    private RectF rect;
    private float xVelocity;
    private float yVelocity;
    private float ballWidth;
    private float ballHeight;
    public Ball(int screenX, int screenY) {
        ballWidth =screenX/100;
        ballHeight = ballWidth;
        xVelocity =screenY/4;
        yVelocity = xVelocity;
       rect =new RectF();
    }
    public RectF getRect(){
        return rect;
    }
    void clearObstacleX(float x){
        rect.left = x;
        rect.right = x + ballWidth;
    }
    void  clearObstacleY(float y){
        rect.bottom=y;
        rect.top=y-ballHeight;
    }
    public void reset(int x, int y){
        rect.left = x / 2;
        rect.top = y ;
        rect.right = x / 2 + ballWidth;
        rect.bottom = y+ ballHeight;
    }

    public void reverseXVelocity() {
        this.xVelocity=-xVelocity;
    }

    public void reverseYVelocity() {
        this.yVelocity=-yVelocity;
    }


    public void increaseVelocity(){
        xVelocity = xVelocity + xVelocity / 10;
        xVelocity = yVelocity + yVelocity / 10;
    }

    public void update(long fps){
        rect.left = rect.left + (xVelocity / fps);
        rect.top = rect.top + (yVelocity / fps);
        rect.right = rect.left + ballWidth;
        rect.bottom = rect.top - ballHeight;
    }
}
