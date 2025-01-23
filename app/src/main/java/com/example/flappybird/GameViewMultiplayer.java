package com.example.flappybird;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Random;

public class GameViewMultiplayer extends SurfaceView implements Runnable
{
    private int SCREEN_WIDTH,SCREEN_HEIGHT;
    private int stepsCount=0,maxSteps=100;    //indicates when to make new tubes (100)
    private double gapSize;
    private Paint bgPaint;
    private Bird bird1,bird2;
    private PipesView topPipe,bottomPipe;
    private ArrayList<PipesView> pipesOnScreen;
    private Bitmap bitmapBird1,bitmapBird2 , backgroundBitmap;  // Bitmap for the bird and the background;
    private Random rnd;
    private SurfaceHolder holder;
    private Canvas canvas;
    private int interval = 15; // (15)
    private Thread thread;
    private boolean isRunning=true,gameOver=false;
    public GameViewMultiplayer(Context context, int screenWidth, int screenHeight)
    {
        super(context);
        SCREEN_WIDTH = screenWidth;
        SCREEN_HEIGHT = screenHeight;
        bgPaint = new Paint();
        bgPaint.setColor(Color.WHITE);

        pipesOnScreen=new ArrayList<PipesView>();  // list of all the pipes on the screen

        // checks to see if player changed background, if not it uses the deafult
        if(BackgroungSelectionActivity.indicateBackgroundSelectionActivity)
            backgroundBitmap = BackgroungSelectionActivity.selectedBackground;
        else
            backgroundBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.skybackground);       // load the selected bird image
        backgroundBitmap = Bitmap.createScaledBitmap(backgroundBitmap,SCREEN_WIDTH, SCREEN_HEIGHT, true);  // strech the image

        bitmapBird1 = BitmapFactory.decodeResource(getResources(),R.drawable.birdplayerone);
        bitmapBird2 = BitmapFactory.decodeResource(getResources(),R.drawable.birdplayertwo);
        bird1 = new Bird(bitmapBird1, SCREEN_WIDTH, SCREEN_HEIGHT);
        bird2 = new Bird(bitmapBird2, SCREEN_WIDTH, SCREEN_HEIGHT);
        bird2.setBirdY(bird2.getBirdY()+50);

        gapSize = bitmapBird1.getHeight()/1.5;    // gap size

        CreatePipes();   // creating the first pipes

        holder=getHolder();
        thread = new Thread(this);
        thread.start();
    }
    public void CreatePipes()
    {
        // random place of gap
        // the gap indicates the bottom of the top pipe
        rnd=new Random();
        int gap=rnd.nextInt(SCREEN_HEIGHT-(int)(gapSize)-50)+50;  // add (50) so the gap wont be at the edges

        //create top pipe
        Bitmap bitmapTopPipe = BitmapFactory.decodeResource(getResources(), R.drawable.pipetop);
        topPipe=new PipesView(bitmapTopPipe, SCREEN_WIDTH, SCREEN_HEIGHT,gap,1);      // 1 indicates bottom pipe
        pipesOnScreen.add(topPipe);

        //create bottom pipe
        Bitmap bitmapBottomPipe = BitmapFactory.decodeResource(getResources(), R.drawable.pipebottom);
        bottomPipe=new PipesView(bitmapBottomPipe, SCREEN_WIDTH, SCREEN_HEIGHT,gap+(int)(gapSize),2);    // 2 indicates bottom pipe
        pipesOnScreen.add(bottomPipe);
    }
    public void DrawSurface()   // drawing the game (bird,pipes, background)
    {
        if (holder.getSurface().isValid())
        {
            // draws the bird and the pipes
            canvas = holder.lockCanvas();
            canvas.drawPaint(bgPaint);
            canvas.drawBitmap(backgroundBitmap, 0, 0, null);
            bird1.draw(canvas);
            bird2.draw(canvas);
            for (PipesView pipe : pipesOnScreen)
                pipe.draw(canvas);

            // code for viewing the rectangles on screen

//            Paint paint = new Paint();
//            paint.setColor(Color.RED);
//            paint.setStyle(Paint.Style.STROKE); // Set the style to STROKE (only outline)
//            paint.setStrokeWidth(5); // Set stroke width (optional, adjust as needed)
//            Rect topPipeRect=null;
//            Rect bottomPipeRect=null;
//            int rectConst=30;
//            Rect birdRect = new Rect(bird.getBirdX()+rectConst, bird.getBirdY()+rectConst, bird.getBirdX() + bird.getBirdWidth()-rectConst, bird.getBirdY() + bird.getBirdHeight()-rectConst); // Bird's rectangle
//            for (PipesView pipe : pipesOnScreen)
//            {
//                if(pipe.getPipeY()<=0)
//                    topPipeRect = new Rect(pipe.getPipeX(), pipe.getPipeY(), pipe.getPipeX() + pipe.getPipeWidth(), pipe.getPipeY() + pipe.getPipeHeight()); // Top pipe's rectangle
//                else
//                    bottomPipeRect = new Rect(pipe.getPipeX(), pipe.getPipeY(), pipe.getPipeX() + pipe.getPipeWidth(), pipe.getPipeY() + pipe.getPipeHeight()); // Bottom pipe's rectangle
//            }
//            canvas.drawRect(birdRect, paint);
//            canvas.drawRect(topPipeRect, paint);
//            canvas.drawRect(bottomPipeRect, paint);

            holder.unlockCanvasAndPost(canvas);

        }
    }
    public void run()
    {
        gameOver=false;
        while (isRunning)
        {
            DrawSurface();

            // ending the game if one of the birds bird falls of the map
            // sending the player who lost
            if(bird1.getBirdY()>=SCREEN_HEIGHT-bird1.getBirdHeight())
                EndGame("two");
            if(bird2.getBirdY()>=SCREEN_HEIGHT-bird2.getBirdHeight())
                EndGame("one");

            // ending the game if there is collision
            if(IsCollision()!="")
                EndGame(IsCollision());

            // moves the bird and counts up when to generate new tubes
            bird1.move();
            bird2.move();
            stepsCount++;
            if(stepsCount>=maxSteps)
            {
                CreatePipes();
                stepsCount=0;
                soundPoint(this.getContext());   // sound when make point
            }

            // moves every pipe on screen
            for (PipesView pipe:pipesOnScreen)
                pipe.move();

            // removes pipes after they leave the screen
            for (int i=0;i<pipesOnScreen.size();i++)
                if(pipesOnScreen.get(i).getPipeX()+pipesOnScreen.get(i).getPipeWidth()<0)
                    pipesOnScreen.remove(i);

            synchronized (this)
            {
                try {wait(interval);}
                catch (InterruptedException e) {e.printStackTrace();}
            }
        }
    }
    public String IsCollision()
    {

        Rect topPipeRect=null;
        Rect bottomPipeRect=null;

        // Bird's rectangle
        int rectConst=20;   // (20) the image is a bit too big so it shrinks the borders of the rect
        Rect birdRect1 = new Rect(bird1.getBirdX()+rectConst, bird1.getBirdY()+rectConst, bird1.getBirdX() + bird1.getBirdWidth()-rectConst, bird1.getBirdY() + bird1.getBirdHeight()-rectConst);
        Rect birdRect2 = new Rect(bird2.getBirdX()+rectConst, bird2.getBirdY()+rectConst, bird2.getBirdX() + bird2.getBirdWidth()-rectConst, bird2.getBirdY() + bird2.getBirdHeight()-rectConst);

        // assigning each pipe to its position
        PipesView pipe=pipesOnScreen.get(0);
        topPipeRect = new Rect(pipe.getPipeX(), pipe.getPipeY(), pipe.getPipeX() + pipe.getPipeWidth(), pipe.getPipeY() + pipe.getPipeHeight());
        pipe=pipesOnScreen.get(1);
        bottomPipeRect = new Rect(pipe.getPipeX(), pipe.getPipeY(), pipe.getPipeX() + pipe.getPipeWidth(), pipe.getPipeY() + pipe.getPipeHeight());

        // return the player who lost
        if(birdRect2.intersect(topPipeRect) || birdRect2.intersect(bottomPipeRect))
            return "one";
        else if (birdRect1.intersect(topPipeRect) || birdRect1.intersect(bottomPipeRect))
            return "two";
        else
            return "";

    }
    public synchronized void EndGame(String player)
    {
        // indicates game is over, and stops the running of the thread
        if (gameOver) return;
        gameOver = true;
        isRunning = false;

        // ensure the following code runs on the UI thread
        ((Activity) getContext()).runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                // show the game over image
                MultiplayerActivity.gameOver.setVisibility(VISIBLE);
                showGameOverDialog(player);
            }
        });
    }

    private void showGameOverDialog(String player)
    {
        // ensure the UI thread is being used for the dialog creation
        ((Activity) getContext()).runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                new AlertDialog.Builder(getContext())
                        .setTitle("Game Over")
                        .setMessage("Player "+ player + " Won")
                        .setCancelable(false)
                        .setPositiveButton("Restart", (dialog, which) -> restartGame())
                        .setNeutralButton("Return Home", (dialog, which) ->
                        {
                            Intent myIntent = new Intent(getContext(), MainActivity.class);
                            getContext().startActivity(myIntent);
                        })
                        .show();
            }
        });
    }
    public void restartGame()
    {
        // remove GameOver sign
        MultiplayerActivity.gameOver.setVisibility(INVISIBLE);

//        gameOver = false;
//        isRunning = true;
//        bird.restartBird();
//        // Clear all pipes on screen
//        while(pipesOnScreen.size()>0)
//            pipesOnScreen.remove(0);
//        CreatePipes();
//        stepsCount = 0;
//
//        // Stop the old thread safely
//        if (thread != null && thread.isAlive())
//        {
//            isRunning = false;  // Stop the game loop
//            try
//            {
//                thread.interrupt();  // Interrupt the thread to stop it
//                thread.join();       // Wait for the thread to finish
//            }
//            catch (InterruptedException e) {e.printStackTrace();}
//        }
//
//        // Start a new thread
//        isRunning = true;
//        holder=getHolder();
//        thread = new Thread(this); // Restart the thread
//        thread.start();
    }

    //makes bird jump when touch on screen
    public boolean onTouchEvent(MotionEvent event)
    {
        if (event.getAction() == MotionEvent.ACTION_DOWN)
        {
            bird1.jump();
            bird2.jump();
            //soundJump(this.getContext());
        }
        return super.onTouchEvent(event);  // Call the default handler for other touch events
    }
    public void soundJump(Context context)
    {
        MediaPlayer mp = MediaPlayer.create(context, R.raw.movesound);
        mp.start();
    }
    public void soundPoint(Context context)
    {
        MediaPlayer mp = MediaPlayer.create(context, R.raw.scoresound);
        mp.start();
    }
}
