package com.example.flappybird;

import static java.security.AccessController.getContext;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

public class MainActivity extends AppCompatActivity
{
    public static String playerName=" ";
    RelativeLayout relativeLayout;
    BitmapDrawable drawable;

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

        if(playerName.equals(" "))
        {
            Random rnd=new Random();
            playerName= "Player "+rnd.nextInt(10000);
        }

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
    public void RegisterButtonClicked(View view)     // an alert dialog that saves the players name
    {
        final EditText editText = new EditText(this);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Enter your name")
                .setCancelable(false)
                .setView(editText)  // Add EditText to the dialog
                .setPositiveButton("Save", (dialog, which) ->
                {
                    String name = editText.getText().toString();
                    playerName=name.toString();
                })
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
}

