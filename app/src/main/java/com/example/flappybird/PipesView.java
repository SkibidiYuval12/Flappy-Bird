package com.example.flappybird;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import java.util.Random;

public class PipesView
{
        private Bitmap pipe;
        private int SCREEN_HEIGHT, SCREEN_WIDTH;
        private int pipeX,pipeY,pipeHeight,pipeWidth;
        private int movePace=13; // moves pipes to the left  (13)

        public PipesView(Bitmap bitmap, int screenWidth, int screenHeight,int gap,int position)
        {

            this.SCREEN_WIDTH = screenWidth;
            this.SCREEN_HEIGHT = screenHeight;
            pipeX=SCREEN_WIDTH;

           if(position==1)  // 1 indicates top pipe
           {
               pipeY= -bitmap.getHeight()+gap;
               pipeHeight = bitmap.getHeight();
           }
           if(position==2)   // 2 indicates bottom pipe
           {

               pipeY=gap;
               pipeHeight = bitmap.getHeight();
           }

            pipeWidth = 270;     //setting width (270)
            pipe = bitmap;
            pipe = Bitmap.createScaledBitmap(bitmap,pipeWidth,pipeHeight,false);   //creating bitmap for the pipe
        }
        public void draw(Canvas canvas)
        {
            canvas.drawBitmap(pipe,pipeX,pipeY,null);
        }
        public void move() {pipeX-=movePace;}   //moves the pipe to the left and update the rect
    public int getPipeHeight() {return this.pipeHeight;}
    public int getPipeX() {return this.pipeX;}
    public int getPipeWidth() {return this.pipeWidth;}
    public int getPipeY() {return this.pipeY;}
}
