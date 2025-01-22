package com.example.flappybird;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

public class BirdSelectionActivity extends AppCompatActivity
{
    public static Bitmap selectedBird;
    public static boolean indicateBirdSelectionActivity=false;   // checks if the player entered the activity
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bird_selection);
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

        indicateBirdSelectionActivity=true;
    }
    public void ReturnHome (View view) {finish();}

    // assign the birds images if chosen
    public void ClickedBird1 (View view){selectedBird = BitmapFactory.decodeResource(getResources(), R.drawable.bird);finish();  }
    public void ClickedBird2 (View view){selectedBird = BitmapFactory.decodeResource(getResources(), R.drawable.cryingbird);finish();}
    public void ClickedBird3 (View view){selectedBird = BitmapFactory.decodeResource(getResources(), R.drawable.greenbirdwithhat);finish();}
    public void ClickedBird4 (View view){selectedBird = BitmapFactory.decodeResource(getResources(), R.drawable.nonchalant);finish();}
    public void ClickedBird5 (View view){selectedBird = BitmapFactory.decodeResource(getResources(), R.drawable.oldbirdwithhat);finish();}
    public void ClickedBird6 (View view){selectedBird = BitmapFactory.decodeResource(getResources(), R.drawable.pinkbird);finish();}
    public void ClickedBird7 (View view){selectedBird = BitmapFactory.decodeResource(getResources(), R.drawable.pinkbirdcrying);finish();}
    public void ClickedBird8 (View view){selectedBird = BitmapFactory.decodeResource(getResources(), R.drawable.yellowbird);finish();}
    public void ClickedBird9 (View view){selectedBird = BitmapFactory.decodeResource(getResources(), R.drawable.brownishbird);finish();}
}