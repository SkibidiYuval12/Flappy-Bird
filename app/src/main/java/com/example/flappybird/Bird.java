package com.example.flappybird;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class Bird
{
    private Bitmap bird;
    private int SCREEN_HEIGHT, SCREEN_WIDTH;
    private int birdY,birdX;
    private int birdHeight,birdWidth;
    private int birdVelocity;
    private final double GRAVITY = 2;  // gravity effect on the bird  (2)
    private final int BIRD_JUMP = -30;  // jump effect on the bird  (-30)
    private float currentAngle = 0;  // angle of the bird

    public Bird(Bitmap bitmap, int screenWidth, int screenHeight)
    {
        bird = bitmap;
        birdHeight = 200;  // (200)
        birdWidth = 150;   // (150)
        bird = Bitmap.createScaledBitmap(bitmap,birdWidth,birdHeight,false);
        this.SCREEN_WIDTH = screenWidth;
        this.SCREEN_HEIGHT = screenHeight;
        birdX=SCREEN_WIDTH/2-250;   // (-250)
        birdY=0;
    }

    public void draw(Canvas canvas)
    {
        // draw the bird at an angle

        Matrix matrix = new Matrix();
        matrix.postTranslate(-birdWidth / 2f, -birdHeight / 2f);  // pivot to center
        matrix.postRotate(currentAngle);
        matrix.postTranslate(birdX + birdWidth / 2f, birdY + birdHeight / 2f);  // move to position

        canvas.drawBitmap(bird,matrix,null);
    }
    public void move()
    {
        birdVelocity += GRAVITY;
        birdY += birdVelocity;

        // angle change
        if (birdVelocity < 0)
        {
            currentAngle = -30; // angle on jump
        }
        else
        {
            currentAngle += 3; // gradually tilt down
            if (currentAngle > 90) currentAngle = 90; // max angle of tilt
        }


        // prevent the bird from falling below the ground
        if (birdY > SCREEN_HEIGHT-100 )
            birdY=SCREEN_HEIGHT - 100;
        else if (birdY < 0)
        {
            birdY = 0;
            birdVelocity = 0;
        }
    }
    public void jump()
    {
        birdVelocity = BIRD_JUMP;
        birdY += birdVelocity;
        currentAngle = -30; // reset rotation upward

        // Prevent the bird from falling below the ground
        if (birdY > SCREEN_HEIGHT - 100)
        {
            birdY = SCREEN_HEIGHT-100;
            birdVelocity = 0;
        }
        else if (birdY < 0)
        {
            birdY = 0;
            birdVelocity = 0;
        }
    }
    public void restartBird()
    {
        birdY=0;
        birdVelocity=0;
        birdX=SCREEN_WIDTH/2-250;
    }

    public int getBirdY() {return this.birdY;}
    public int getBirdHeight(){return this.birdHeight;}
    public int getBirdWidth(){return this.birdWidth;}
    public int getBirdX(){return this.birdX;}
    public void setBirdY(int birdY){this.birdY = birdY;}
}
