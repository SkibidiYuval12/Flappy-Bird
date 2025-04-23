package com.example.flappybird;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class BackgroundView
{
    private Bitmap background;
    private int SCREEN_HEIGHT, SCREEN_WIDTH;
    private float xPosition,yPosition;
    public BackgroundView(Bitmap bitmap, int screenWidth, int screenHeight, int xStart, int yStart)
    {
        background=bitmap;
        SCREEN_HEIGHT=screenHeight;
        SCREEN_WIDTH=screenWidth;
        xPosition=(float)xStart;
        yPosition=(float)yStart;
        startX();
    }
    public void draw(Canvas canvas)
    {
        canvas.drawBitmap(background,xPosition,yPosition,null);
    }
    public void startX()
    {
       xPosition=0;
    }
    public void move()
    {
        xPosition-=10.48;   // adjust to phone width
        if(xPosition<=(-1*SCREEN_WIDTH))
            startX();
    }
}
