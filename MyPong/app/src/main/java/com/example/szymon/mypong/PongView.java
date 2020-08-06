package com.example.szymon.mypong;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

/**
 * Created by Szymon on 26/03/2018.
 */

public class PongView extends SurfaceView implements Runnable {
    private   int bar;
    Thread gameThread = null;


    SurfaceHolder ourHolder;


    volatile boolean playing;


    boolean paused = true;


    Canvas canvas;
    Paint paint;


    long fps;


    int screenX;
    int screenY;


    Bat bat;


    Ball ball;


    SoundPool sp;
    int beep1ID = -1;
    int beep2ID = -1;
    int beep3ID = -1;
    int loseLifeID = -1;
    int explodeID=-1;


    int score = 0;


    int lives = 3;
    public PongView(Context context, int x, int y) {


        super(context);


        screenX = x;
        screenY = y;


        ourHolder = getHolder();
        paint = new Paint();
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            bar= resources.getDimensionPixelSize(resourceId);
        }
        else bar=0;
        bat = new Bat(screenX, screenY,bar);


        ball = new Ball(screenX, screenY -bar);



            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();

            sp = new SoundPool.Builder()
                    .setMaxStreams(5)
                    .setAudioAttributes(audioAttributes)
                    .build();


        try{

            AssetManager assetManager = context.getAssets();
            AssetFileDescriptor descriptor;

            descriptor = assetManager.openFd("beep1.ogg");
            beep1ID = sp.load(descriptor, 0);

            descriptor = assetManager.openFd("beep2.ogg");
            beep2ID = sp.load(descriptor, 0);

            descriptor = assetManager.openFd("beep3.ogg");
            beep3ID = sp.load(descriptor, 0);

            descriptor = assetManager.openFd("loseLife.ogg");
            loseLifeID = sp.load(descriptor, 0);

            descriptor = assetManager.openFd("explode.ogg");
            explodeID = sp.load(descriptor, 0);

        }catch(IOException e){
            // Print an error message to the console
            Log.e("error", "failed to load sound files");
        }

        setupAndRestart();

    }
    public void setupAndRestart() {


        ball.reset(screenX, screenY /2);


        if (lives == 0) {
            score = 0;
            lives = 3;
        }
    }
    @Override
    public void run() {
        while (playing) {

            // Capture the current time in milliseconds in startFrameTime
            long startFrameTime = System.currentTimeMillis();

            // Update the frame
            // Update the frame
            if(!paused){
                update();
            }

            // Draw the frame
            draw();

        /*
            Calculate the FPS this frame
            We can then use the result to
            time animations in the update methods.
        */
            long timeThisFrame = System.currentTimeMillis() - startFrameTime;
            if (timeThisFrame >= 1) {
                fps = 1000 / timeThisFrame;
            }

        }

    }
    public void update() {


        bat.update(fps);

        ball.update(fps);

        if (RectF.intersects(bat.getRect(), ball.getRect())) {
            ball.reverseYVelocity();
            ball.clearObstacleY(bat.getRect().top - 2);

            score++;
            ball.increaseVelocity();

            sp.play(beep1ID, 1, 1, 0, 0, 1);
        }

        if (ball.getRect().bottom >= screenY -bar+ bat.getRect().height()) {
            ball.reverseYVelocity();
            ball.clearObstacleY(screenY - 2-bar+ bat.getRect().height());


            lives--;
            sp.play(loseLifeID, 1, 1, 0, 0, 1);
        }
        if (lives == 0) {
            paused = true;
            Activity activity= (Activity) getContext();
            activity.finish();
        }

        if (ball.getRect().top <=0) {
            ball.reverseYVelocity();
            ball.clearObstacleY(12);

            sp.play(beep2ID, 1, 1, 0, 0, 1);
        }

        if (ball.getRect().left <=0) {
            ball.reverseXVelocity();
            ball.clearObstacleX(2);

            sp.play(beep3ID, 1, 1, 0, 0, 1);
        }

        if (ball.getRect().right >= screenX) {
            ball.reverseXVelocity();
            ball.clearObstacleX(screenX - 2);

            sp.play(beep3ID, 1, 1, 0, 0, 1);
        }
    }
    public void draw() {


        if (ourHolder.getSurface().isValid()) {


            canvas = ourHolder.lockCanvas();



            canvas.drawColor(Color.argb(255, 123, 232, 76));


            paint.setColor(Color.argb(255, 33, 113, 242));

            canvas.drawRect(bat.getRect(), paint);


            canvas.drawRect(ball.getRect(), paint);



            paint.setColor(Color.argb(255, 255, 255, 255));


            paint.setTextSize(screenY /50);
            canvas.drawText("Score: " + score + "   Lives: " + lives, 10, 50, paint);

            ourHolder.unlockCanvasAndPost(canvas);
        }

    }

    public void pause() {
        playing = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            Log.e("Error:", "joining thread");
        }

    }


    public void resume() {
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {

        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {


            case MotionEvent.ACTION_DOWN:

                paused = false;


                if(motionEvent.getX() > screenX / 2){
                    bat.setMovementState(bat.RIGHT);
                }
                else{
                    bat.setMovementState(bat.LEFT);
                }

                break;

            case MotionEvent.ACTION_UP:

                bat.setMovementState(bat.STOPPED);
                break;
        }
        return true;
        }
    }
