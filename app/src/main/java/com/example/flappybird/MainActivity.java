package com.example.flappybird;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) ->
        {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



    }
    public void startButtonClicked(View view)
    {
        Intent myIntent=new Intent(MainActivity.this,GravityMode.class);
        startActivity(myIntent);
    }
    public void multiplayerButtonClicked(View view)
    {
        Intent myIntent=new Intent(MainActivity.this,MultiplayerActivity.class);
        startActivity(myIntent);
    }
    public void birdSelectionButtonClicked(View view)
    {
        Intent myIntent=new Intent(MainActivity.this,BirdSelectionActivity.class);
        startActivity(myIntent);
    }
    public void backgroungSelectionButtonClicked(View view)
    {
        Intent myIntent=new Intent(MainActivity.this,BackgroungSelectionActivity.class);
        startActivity(myIntent);
    }
}