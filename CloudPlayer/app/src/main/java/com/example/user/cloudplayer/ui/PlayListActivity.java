package com.example.user.cloudplayer.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.user.cloudplayer.R;
import com.example.user.cloudplayer.adapters.ProfileActivityAdapter;
import com.example.user.cloudplayer.adapters.SongAdapter;
import com.example.user.cloudplayer.model.PlayList;
import com.example.user.cloudplayer.model.Song;
import com.example.user.cloudplayer.transport.NetworkEventListener;

import java.util.ArrayList;


public class PlayListActivity extends Activity implements NetworkEventListener {
    private ListView list;
    private SongAdapter adapter;
    private Button comment;
    private Button like;
    private TextView numLikes;
    private ArrayList<Song> currentPlayList;
    private Button addSong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);
        final int playlistId = 1;
        final Activity a = this;
        comment = (Button) findViewById(R.id.comment);
        like = (Button) findViewById(R.id.like_button);
        addSong = (Button) findViewById(R.id.add_button);
        numLikes = (TextView) findViewById(R.id.num_likes);
        list = (ListView) findViewById(R.id.song_list);

        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(a, CommentsActivity.class);
                intent.putExtra(a.getResources().getString(R.string.key_playlistID), playlistId);
                startActivity(intent);
            }
        });
        addSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        //if(cloudStorage.getUser()==me){
        // like.setText("unlike"));
        //addSong.setVisisibility(visible);
        //}
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //App.onLikeButtonClicked();
            }
        });
        currentPlayList=new ArrayList<>();
        for(int i=0;i<20;i++){
            currentPlayList.add(new Song());
        }
        adapter=new SongAdapter(this,currentPlayList);
        list.setAdapter(adapter);


    }
}
