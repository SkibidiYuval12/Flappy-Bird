package com.example.flappybird;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class BackgroungSelectionActivity extends AppCompatActivity
{

    public static Bitmap selectedBackground;
    public static boolean indicateBackgroundSelectionActivity=false;   // checks if the player entered the activity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_backgroung_selection);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) ->
        {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        indicateBackgroundSelectionActivity=true;
    }
    public void ClickedBackground1 (View view) {selectedBackground = BitmapFactory.decodeResource(getResources(), R.drawable.skybackground);finish();}
    public void ClickedBackground2 (View view) {selectedBackground = BitmapFactory.decodeResource(getResources(), R.drawable.darkbackground);finish();}
    public void ClickedBackground3 (View view) {selectedBackground = BitmapFactory.decodeResource(getResources(), R.drawable.racetrack);finish();}
    public void ClickedBackground4 (View view) {selectedBackground = BitmapFactory.decodeResource(getResources(), R.drawable.spacebackground);finish();}
}