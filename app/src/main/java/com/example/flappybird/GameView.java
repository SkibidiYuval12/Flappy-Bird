package com.example.flappybird;


import static androidx.core.content.ContextCompat.startActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
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
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

public class GameView extends SurfaceView implements Runnable
{
    private int SCREEN_WIDTH,SCREEN_HEIGHT;
    private int stepsCount=0,maxSteps=100;    //indicates when to make new tubes (100)
    private double gapSize;
    private Paint bgPaint;
    private Bird bird;
    private PipesView topPipe,bottomPipe;
    private ArrayList<PipesView> pipesOnScreen;
    private Bitmap bitmapBird , backgroundBitmap;  // Bitmap for the bird and the background;
    private Random rnd;
    private SurfaceHolder holder;
    private Canvas canvas;
    private int interval = 15; // (15)
    private Thread thread;
    private boolean isRunning=true,gameOver=false;
    public GameView(Context context, int screenWidth, int screenHeight)
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

        // checks to see if player changed bird skin, if not it uses the deafult skin
        if(BirdSelectionActivity.indicateBirdSelectionActivity)
           bitmapBird=BirdSelectionActivity.selectedBird;
        else
            bitmapBird = BitmapFactory.decodeResource(getResources(),R.drawable.bird);       // load the selected bird image
        bird = new Bird(bitmapBird, SCREEN_WIDTH, SCREEN_HEIGHT);

        gapSize=bitmapBird.getHeight()/1.5;    // gap size

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
            bird.draw(canvas);
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

            // ending the game if bird falls of the map
            if(bird.getBirdY()>=SCREEN_HEIGHT-bird.getBirdHeight())
                EndGame();

            // ending the game if there is collision
            if(IsCollision())
                EndGame();

            // moves the bird and counts up when to generate new tubes
            bird.move();
            stepsCount++;
            if(stepsCount>=maxSteps)
            {
                CreatePipes();
                stepsCount=0;

                // add point
                GravityMode.scoreCount++;

                // get the main thread's Looper
                Handler mainHandler = new Handler(Looper.getMainLooper());
                mainHandler.post(new Runnable()
                {
                    @Override
                    // update the score on the main thread
                    public void run() {GravityMode.score.setText(Integer.toString(GravityMode.scoreCount));}
                });
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
    public boolean IsCollision()
    {

        Rect topPipeRect=null;
        Rect bottomPipeRect=null;

        // Bird's rectangle
        int rectConst=20;   // (30) the image is a bit too big so it shrinks the borders of the rect
        Rect birdRect = new Rect(bird.getBirdX()+rectConst, bird.getBirdY()+rectConst, bird.getBirdX() + bird.getBirdWidth()-rectConst, bird.getBirdY() + bird.getBirdHeight()-rectConst);

        // assigning each pipe to its position
        PipesView pipe=pipesOnScreen.get(0);
        topPipeRect = new Rect(pipe.getPipeX(), pipe.getPipeY(), pipe.getPipeX() + pipe.getPipeWidth(), pipe.getPipeY() + pipe.getPipeHeight());
        pipe=pipesOnScreen.get(1);
        bottomPipeRect = new Rect(pipe.getPipeX(), pipe.getPipeY(), pipe.getPipeX() + pipe.getPipeWidth(), pipe.getPipeY() + pipe.getPipeHeight());

        // true if bird touches any pipe
        return (birdRect.intersect(topPipeRect) || birdRect.intersect(bottomPipeRect));

    }
    public synchronized void EndGame()
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
                GravityMode.gameOver.setVisibility(VISIBLE);

                // show the Name Input Dialog first before the Game Over dialog
                showNameEnterDialog(new NameInputCallback()
                {
                    @Override
                    // after name is entered, show the Game Over dialog
                    public void onNameEntered(String name) {showGameOverDialog(name);}
                });
            }
        });
    }

    private void showNameEnterDialog(final NameInputCallback callback)
    {
        final EditText input = new EditText(this.getContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT);

        // create the AlertDialog to enter name
        new AlertDialog.Builder(this.getContext())
                .setTitle("Enter Name Below:")
                .setMessage("Enter your name to track score")
                .setCancelable(false)
                .setView(input)  // add the EditText to the dialog
                .setPositiveButton("OK", (dialog, which) ->
                {
                    String userName = input.getText().toString();
                    callback.onNameEntered(userName);  // pass the name to the callback
                })
                .show();
    }

    private void showGameOverDialog(String playerName)
    {
        // ensure the UI thread is being used for the dialog creation
        ((Activity) getContext()).runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                new AlertDialog.Builder(getContext())
                        .setTitle("Game Over")
                        .setCancelable(false)
                        .setPositiveButton("Restart", (dialog, which) -> restartGame())
                        .setNegativeButton("ScoreBoard", (dialog, which) ->
                        {
                            updateScoreboard(playerName);

                            // show Scoreboard Activity
                            Intent myIntent = new Intent(getContext(), ScoreBoardActivity.class);
                            getContext().startActivity(myIntent);
                            restartGame();      // restart after showing scoreboard
                        })
                        .setNeutralButton("Return Home", (dialog, which) ->
                        {
                            GravityMode.scoreCount = 0;
                            GravityMode.score.setText("0");
                            Intent myIntent = new Intent(getContext(), MainActivity.class);
                            getContext().startActivity(myIntent);
                        })
                        .show();
            }
        });
    }

    private void updateScoreboard(String playerName)
    {
        // update the scoreboard with the name and score
        boolean inserted = false;

        for (int i = 0; i < GravityMode.scoreBoardList.size(); i++) {
            if (GravityMode.scoreBoardList.get(i) < GravityMode.scoreCount)
            {
                // insert the score at the correct position
                GravityMode.scoreBoardList.add(i, GravityMode.scoreCount);
                GravityMode.scoreBoardListNames.add(i, playerName + " : " + GravityMode.scoreCount);
                inserted = true;
                break;
            }
        }

        // if the score was not inserted, it should be added at the end of the list
        if (!inserted)
        {
            GravityMode.scoreBoardList.add(GravityMode.scoreCount);
            GravityMode.scoreBoardListNames.add(playerName + " : " + GravityMode.scoreCount);
        }
    }
    public interface NameInputCallback { void onNameEntered(String name);}

    public void restartGame()
    {
        // restart the points and remove GameOver sign
        GravityMode.scoreCount=0;
        GravityMode.score.setText("0");
        GravityMode.gameOver.setVisibility(INVISIBLE);

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
            bird.jump();
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
