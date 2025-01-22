package com.example.flappybird;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.widget.RelativeLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MultiplayerActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_multiplayer);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) ->
        {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        RelativeLayout relativeLayout = findViewById(R.id.main);
        BitmapDrawable drawable = new BitmapDrawable(getResources(), BackgroungSelectionActivity.selectedBackground);
        if(BackgroungSelectionActivity.indicateBackgroundSelectionActivity)
            relativeLayout.setBackground(drawable);
    }

}