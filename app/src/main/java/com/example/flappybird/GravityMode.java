package com.example.flappybird;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class GravityMode extends AppCompatActivity
{

    private FrameLayout frm;
    private Bitmap bitmap;
    private GameView GameView;
    public static int scoreCount=0;
    public static TextView score;
    public static ImageView gameOver;
    public static Player player;

    public static ArrayList<String> listNames;
    public static ArrayList<Integer> listScores;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gravity_mode);

        RelativeLayout relativeLayout = findViewById(R.id.main);
        BitmapDrawable drawable = new BitmapDrawable(getResources(), BackgroungSelectionActivity.selectedBackground);
        if(BackgroungSelectionActivity.indicateBackgroundSelectionActivity)
            relativeLayout.setBackground(drawable);

        listNames=new ArrayList<String>();
        listScores=new ArrayList<Integer>();
        frm = findViewById(R.id.frmLayout);
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bird);
        bitmap = Bitmap.createScaledBitmap(bitmap, 30, 40, false);
        gameOver = findViewById(R.id.GameOver);

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus)
    {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus )
        {
            int w = frm.getWidth();
            int h = frm.getHeight();
            score=findViewById(R.id.score);  // text view of the score displayed
            score.setText(Integer.toString(scoreCount));
            GameView = new GameView(this,w,h);
            frm.addView(GameView);
        }
    }

}