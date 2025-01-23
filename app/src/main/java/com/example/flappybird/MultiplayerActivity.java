package com.example.flappybird;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MultiplayerActivity extends AppCompatActivity
{
    private FrameLayout frm;
    private Bitmap bitmap1,bitmap2;
    private GameViewMultiplayer GameViewMultiplayer;
    public static ImageView gameOver;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer);

        RelativeLayout relativeLayout = findViewById(R.id.main);
        BitmapDrawable drawable = new BitmapDrawable(getResources(), BackgroungSelectionActivity.selectedBackground);
        if(BackgroungSelectionActivity.indicateBackgroundSelectionActivity)
            relativeLayout.setBackground(drawable);

        frm = findViewById(R.id.frmLayout);
        bitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.birdplayerone);
        bitmap1 = Bitmap.createScaledBitmap(bitmap1, 30, 40, false);
        bitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.birdplayertwo);
        bitmap2 = Bitmap.createScaledBitmap(bitmap2, 30, 40, false);
        gameOver = findViewById(R.id.GameOver);

    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus)
    {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus)
        {
            int w = frm.getWidth();
            int h = frm.getHeight();
            GameViewMultiplayer = new GameViewMultiplayer(this,w,h);
            frm.addView(GameViewMultiplayer);
        }
    }

}