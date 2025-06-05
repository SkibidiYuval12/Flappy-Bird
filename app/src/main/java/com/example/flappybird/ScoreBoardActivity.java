package com.example.flappybird;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ScoreBoardActivity extends AppCompatActivity
{
    private ArrayList<Player> playersList = new ArrayList<>();
    private ListView listView;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mGameRef;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_score_board);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) ->
        {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // change the background
        RelativeLayout relativeLayout = findViewById(R.id.main);
        BitmapDrawable drawable = new BitmapDrawable(getResources(), BackgroungSelectionActivity.selectedBackground);
        if(BackgroungSelectionActivity.indicateBackgroundSelectionActivity)
            relativeLayout.setBackground(drawable);

        mDatabase=FirebaseDatabase.getInstance();
        mGameRef=mDatabase.getReference("players");

        // add all players in firebase to the list
        mGameRef.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                for (DataSnapshot snap : snapshot.getChildren())
                {
                    Player player=new Player(snap.getValue(Player.class).getName(),snap.getValue(Player.class).getScore());
                    playersList.add(player);
                }
                CreateList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

    }

    public void CreateList()
    {
        // sort the list by scores
        int maxScore=-1;
        int maxIndex=0;
        ArrayList<Player> winnerList=new ArrayList<>();
        // show only top 10 players
        for(int j=0;j<10;j++)
        {
        {
            for (int i=0;i<playersList.size();i++)
            {
                if(playersList.get(i).getScore()>maxScore)
                {
                    maxIndex=i;
                    maxScore=playersList.get(i).getScore();
                }
            }
            winnerList.add(playersList.get(maxIndex));
            maxScore=-1;
            playersList.remove(maxIndex);
        }
        }

        // create the list and add color to the text
//        ArrayAdapter <Player> adapter;
//        adapter = new ArrayAdapter<Player> (this,android.R.layout.simple_list_item_1, winnerList);
        ArrayAdapter<Player> adapter = new ArrayAdapter<Player>(this, android.R.layout.simple_list_item_1, winnerList)
        {
            @Override
            public View getView(int position, View convertView, ViewGroup parent)
            {
                View view = super.getView(position, convertView, parent);

                TextView textView = (TextView) view.findViewById(android.R.id.text1);
                textView.setTextColor(Color.RED);

                return view;
            }
        };
        listView = (ListView) findViewById(R.id.listViewScores);
        listView.setAdapter(adapter);
    }
    public void ReturnToGameButton(View view)
    {
        Intent myIntent=new Intent(ScoreBoardActivity.this,MainActivity.class);
        startActivity(myIntent);
    }
}



