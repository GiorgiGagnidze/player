package com.example.user.cloudplayer.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import android.widget.ListView;
import android.widget.TextView;

import com.example.user.cloudplayer.R;

import com.example.user.cloudplayer.adapters.SongAdapter;
import com.example.user.cloudplayer.fragments.AddSongDialogFragment;
import com.example.user.cloudplayer.fragments.LikesDialogFragment;

import com.example.user.cloudplayer.model.Comment;
import com.example.user.cloudplayer.model.Like;
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
        final PlayList playlist=(PlayList)getIntent().getExtras().get(this.getResources().getString(R.string.key_playlistID));
        final Activity a = this;
        comment = (Button) findViewById(R.id.comment);
        like = (Button) findViewById(R.id.like_button);
        addSong = (Button) findViewById(R.id.add_button);
        numLikes = (TextView) findViewById(R.id.num_likes);
        list = (ListView) findViewById(R.id.song_list);
        numLikes.setText(playlist.getNumLikes()+" people like this.");
        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(a, CommentsActivity.class);
                intent.putExtra(a.getResources().getString(R.string.key_playlistID), playlist.getID());
                startActivity(intent);
            }
        });
        addSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddSongDialogFragment login = new AddSongDialogFragment();
                login.show(getFragmentManager(), getResources().getString(R.string.tag));
            }
        });
        //if(cloudStorage.getUser()==me){
        // like.setText("unlike"));
        //addSong.setVisisibility(visible);
        //}
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LikesDialogFragment dial=new LikesDialogFragment();
                Bundle args = new Bundle();
                args.putString(a.getResources().getString(R.string.key_playlistID), playlist.getID());
                dial.setArguments(args);
                dial.show(getFragmentManager(), getResources().getString(R.string.tag));
                   //App.onLikeButtonClicked();
            }
        });
        currentPlayList=new ArrayList<Song>();
        for(int i=0;i<20;i++){
            currentPlayList.add(new Song(null,null,null,null));
        }
        adapter=new SongAdapter(this,currentPlayList);
        list.setAdapter(adapter);


    }

    @Override
    public void onPlayListAdded(PlayList playList) {

    }

    @Override
    public void onPlayListDeleted(PlayList playList) {

    }

    @Override
    public void onUsersPlayListsDownloaded(ArrayList<PlayList> playLists) {

    }

    @Override
    public void onCommentsDownloaded(ArrayList<Comment> comments) {

    }

    @Override
    public void onLikesDownloaded(ArrayList<Like> likes) {

    }

    @Override
    public void onCommentAdded(Comment comment) {

    }

    @Override
    public void onSearchResultDownloaded(ArrayList<PlayList> playLists) {

    }

    @Override
    public void onTopTenDownloaded(ArrayList<PlayList> playLists) {

    }

    @Override
    public void onSongAdded(Song song) {

    }

    @Override
    public void onSongsDownloaded(ArrayList<Song> songs) {

    }
}
