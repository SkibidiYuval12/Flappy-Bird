package com.example.flappybird;

import static android.view.View.VISIBLE;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.widget.Chronometer;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class GravityModeMultiplayer extends AppCompatActivity
{
    private FrameLayout frm;
    private Bitmap bitmap1,bitmap2;
    private GameViewMultiplayer GameViewMultiplayer;
    public static ImageView gameOver;
    private TextView playerInfo;

    public static Chronometer chronometer;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gravity_mode_multiplayer);

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

        // change the sign to show player 2
        playerInfo=findViewById(R.id.playerInfo);
        if(MultiplayerActivity.isPlayer2)
        {
            playerInfo.setText("Player 2");
            playerInfo.setTextColor(0xFF0000FF);
        }
        playerInfo.setVisibility(VISIBLE);

        chronometer = findViewById(R.id.chronometer);

    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus)
    {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus)
        {
            int w = frm.getWidth();
            int h = frm.getHeight();
            GameViewMultiplayer = new GameViewMultiplayer(this,h,w);
            frm.addView(GameViewMultiplayer);
        }
    }
}