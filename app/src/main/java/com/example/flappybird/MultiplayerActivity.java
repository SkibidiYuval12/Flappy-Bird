package com.example.flappybird;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.UUID;

public class MultiplayerActivity extends AppCompatActivity
{
    private FirebaseDatabase mDatabase;
    public static boolean isPlayer2=false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer);

        RelativeLayout relativeLayout = findViewById(R.id.main);
        BitmapDrawable drawable = new BitmapDrawable(getResources(), BackgroungSelectionActivity.selectedBackground);
        if (BackgroungSelectionActivity.indicateBackgroundSelectionActivity)
            relativeLayout.setBackground(drawable);

        mDatabase = FirebaseDatabase.getInstance();

        // show the initial dialog
        showCreateOrJoinDialog();
    }
    public void StartGame()
    {
        DatabaseReference gameRef = mDatabase.getReference("game");
        gameRef.removeValue();
        Intent myIntent=new Intent(MultiplayerActivity.this,GravityModeMultiplayer.class);
        startActivity(myIntent);
    }
    private void showCreateOrJoinDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Action")
                .setPositiveButton("Create Game", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        createNewGame();
                    }
                })
                .setNegativeButton("Join Game", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        joinGame();
                    }
                })
                .setCancelable(false)
                .show();
    }

    private void createNewGame()
    {
        // create a new game
        DatabaseReference gameRef = mDatabase.getReference("game");

        // check if no game started else start new one
        gameRef.child("status").addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                String status = dataSnapshot.getValue(String.class);
                if(status!=null)
                {
                    if (status.equals("waiting") || status.equals("playing"))
                    {
                        Toast.makeText(MultiplayerActivity.this, "Game already started", Toast.LENGTH_SHORT).show();
                        showCreateOrJoinDialog();
                    }
                }
                else
                {
                    Toast.makeText(MultiplayerActivity.this, "Game created successfully", Toast.LENGTH_SHORT).show();
                    isPlayer2=false;
                    gameRef.child("status").setValue("waiting");
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        // wait till status changes to playing to indicate player 2 joined
        gameRef.child("status").addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.getValue(String.class)!=null && dataSnapshot.getValue(String.class).equals("playing"))
                    StartGame();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void joinGame()
    {
        DatabaseReference gameRef = mDatabase.getReference("game");

        // check if the game exists and if it is waiting for player 2
        gameRef.child("status").addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                String status = dataSnapshot.getValue(String.class);
                if ("waiting".equals(status))
                {
                    gameRef.child("status").setValue("playing");
                    isPlayer2=true;
                    StartGame();
                }
                else
                {
                    Toast.makeText(MultiplayerActivity.this, "No Game Exists", Toast.LENGTH_SHORT).show();
                    showCreateOrJoinDialog();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }
}
