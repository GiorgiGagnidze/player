package com.example.user.cloudplayer.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.cloudplayer.App;
import com.example.user.cloudplayer.R;

import com.example.user.cloudplayer.adapters.SongAdapter;
import com.example.user.cloudplayer.fragments.AddSongDialogFragment;
import com.example.user.cloudplayer.fragments.LikesDialogFragment;

import com.example.user.cloudplayer.model.Comment;
import com.example.user.cloudplayer.model.Like;
import com.example.user.cloudplayer.model.PlayList;
import com.example.user.cloudplayer.model.Song;
import com.example.user.cloudplayer.transport.NetworkEventListener;
import com.parse.ParseUser;

import java.util.ArrayList;


public class PlayListActivity extends Activity implements NetworkEventListener {
    private ListView list;
    private SongAdapter adapter;
    private Button comment;
    private Button like;
    private TextView numLikes;
    private ArrayList<Song> currentPlayList;
    private Button addSong;
    private final String LIKED="Unlike";
    private final String UNLIKED="Like";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);
        final PlayList playlist=(PlayList)getIntent().getExtras().get(this.getResources().getString(R.string.key_playlistID));
        final Activity a = this;
        final ParseUser user=ParseUser.getCurrentUser();
        comment = (Button) findViewById(R.id.comment);
        like = (Button) findViewById(R.id.like_button);

        addSong = (Button) findViewById(R.id.add_button);
        numLikes = (TextView) findViewById(R.id.num_likes);
        list = (ListView) findViewById(R.id.song_list);

        App.getCloudStorage().hasLiked(playlist.getID(),user.getString(getResources().getString(R.string.name_col)));
        numLikes.setText(playlist.getNumLikes()+" people like this.");
        numLikes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               LikesDialogFragment dial=new LikesDialogFragment();
                Bundle args = new Bundle();
                args.putString(a.getResources().getString(R.string.key_playlistID), playlist.getID());
                dial.setArguments(args);
                dial.show(getFragmentManager(), getResources().getString(R.string.tag));
            }
        });
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
                AddSongDialogFragment addSong = new AddSongDialogFragment();
                Bundle args = new Bundle();
                args.putString(a.getResources().getString(R.string.key_playlistID), playlist.getID());
                addSong.setArguments(args);
                addSong.show(getFragmentManager(), getResources().getString(R.string.tag));
            }
        });

        if(playlist.getUserID().equals(user.getObjectId())){
             addSong.setVisibility(View.VISIBLE);
        }
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(like.getText().toString().equals(UNLIKED)) {
                    Like l = new Like(playlist.getID(), user.getString(getResources().getString(R.string.name_col)));
                    App.getCloudStorage().addLike(l);
                }
                   //App.onLikeButtonClicked();
            }
        });
        App app = (App)getApplication();
        app.addListener(this);
        currentPlayList=new ArrayList<Song>();
        App.getCloudStorage().getSongs(playlist.getID());




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
        currentPlayList.add(song);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onSongsDownloaded(ArrayList<Song> songs) {

        currentPlayList=songs;
        adapter=new SongAdapter(this,currentPlayList);
        list.setAdapter(adapter);
    }

    @Override
    public void onLiked(Like like) {
        if(like==null){
            Toast.makeText(getApplicationContext(),"Network Error",Toast.LENGTH_SHORT);
        }
        else{
            this.like.setText("Unlike");
        }
    }

    @Override
    public void onHasLiked(Boolean bool) {
        if(bool==true)  like.setText(LIKED);
        else if(bool==false)  like.setText(UNLIKED);
    }

    @Override
    public void onUnLiked(Like like) {

    }

    @Override
    public void onSongDeleted(Song song) {

    }
}
