package com.example.szymon.mypong;

import android.graphics.RectF;

/**
 * Created by Szymon on 26/03/2018.
 */

public class Bat {


    private RectF rect;


    private float length;
    private float height;
    private int batSpeed;



    private float xCoord;


    private float yCoord;


    public final int STOPPED = 0;
    public final int LEFT = 1;
    public final int RIGHT = 2;


    private int batMoving = STOPPED;


    private int screenX;
    private int screenY;

    public Bat(int x, int y, int bar) {

        screenX = x;
        screenY = y;


        length = screenX / 8;


        height = screenY / 50;


        xCoord = screenX / 2;
        yCoord = screenY - bar;

        rect = new RectF(xCoord, yCoord, xCoord + length, yCoord + height);


        batSpeed = screenX;
    }

    public RectF getRect(){
        return rect;
    }

    public void setMovementState(int state){
        batMoving = state;
    }

    public void update(long fps){

        if(batMoving == LEFT){
            xCoord = xCoord - batSpeed / fps;
        }

        if(batMoving == RIGHT){
            xCoord = xCoord + batSpeed / fps;
        }

        if(rect.left < 0){ xCoord = 0; } if(rect.right > screenX){
            xCoord = screenX - length;
        }


        rect.left = xCoord;
        rect.right = xCoord + length;
    }

}
