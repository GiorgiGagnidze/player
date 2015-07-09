package com.example.user.cloudplayer.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
    private PlayList playlist;
    private int isLiked;
    private boolean checker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);
        playlist=(PlayList)getIntent().getExtras().get(this.getResources().getString(R.string.key_playlistID));
        final Activity a = this;

        final ParseUser user=ParseUser.getCurrentUser();
        comment = (Button) findViewById(R.id.comment);
        like = (Button) findViewById(R.id.like_button);
        addSong = (Button) findViewById(R.id.add_button);
        numLikes = (TextView) findViewById(R.id.num_likes);
        list = (ListView) findViewById(R.id.song_list);
        App.getCloudStorage().hasLiked(playlist.getID());
        if(savedInstanceState!=null){
            isLiked=savedInstanceState.getInt(getResources().getString(R.string.key_for_int));
            checker=savedInstanceState.getBoolean(getResources().getString(R.string.key_for_bool));


        }else {
            isLiked = -1;
            checker = true;

        }
        if(isLiked!=-1) numLikes.setText(isLiked + getResources().getString(R.string.like_text));
        else numLikes.setText(playlist.getNumLikes() + getResources().getString(R.string.like_text));
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(a, PlayerActivity.class);
                intent.putExtra(a.getResources().getString(R.string.key_song),
                        currentPlayList.get(position));
                startActivity(intent);
            }
        });
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
                Like l = new Like(playlist.getID(), user.getString(getResources().getString(R.string.name_col)));
                if(checker) {
                    if (like.getText().toString().equals(UNLIKED)) {
                        App.getCloudStorage().addLike(l);

                    } else {
                        App.getCloudStorage().unLike(l);
                    }
                    checker=false;
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
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(getResources().getString(R.string.key_for_int),isLiked);
        outState.putBoolean(getResources().getString(R.string.key_for_bool),checker);

    }


    @Override
    public void onPlayListAdded(PlayList playList) {

    }

    @Override
    public void onPlayListDeleted(PlayList playList) {
        if(playlist==null){

        }else if(playlist.getID().equals(this.playlist.getID())){
            finish();
        }
    }

    @Override
    public void onUsersPlayListsDownloaded(ArrayList<PlayList> playLists) {

    }

    @Override
    public void onCommentsDownloaded(ArrayList<Comment> comments) {
        if(comments == null) {
            Toast.makeText(this,getResources().getString(R.string.on_comments_downloaded), Toast.LENGTH_LONG)
                    .show();
        }
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
        if(song!=null){
            Log.i("Irakli","SHemovedi");
            currentPlayList.add(song);
            adapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onSongsDownloaded(ArrayList<Song> songs) {
        if(songs==null){
            currentPlayList=new ArrayList<Song>();
        }else{
            currentPlayList=songs;
        }

        adapter=new SongAdapter(this,currentPlayList);
        list.setAdapter(adapter);
    }

    @Override
    public void onLiked(Like like) {
        if(like==null){
            Toast.makeText(getApplicationContext(),"Network Error",Toast.LENGTH_SHORT);

        }
        else{
            if(isLiked==-1){
                isLiked=playlist.getNumLikes()+1;
            }
            else{
                isLiked++;
            }
            numLikes.setText(isLiked+getResources().getString(R.string.like_text));
            this.like.setText("Unlike");
        }
        checker=true;
    }

    @Override
    public void onHasLiked(Boolean bool) {
        if(bool==true)  like.setText(LIKED);
        else if(bool==false)  like.setText(UNLIKED);
        else if(bool==null){

        }
    }

    @Override
    public void onUnLiked(Like like) {
        if(like==null){
            Toast.makeText(getApplicationContext(),"Network Error",Toast.LENGTH_SHORT).show();

        }
        else{
            if(isLiked==-1){
                isLiked=playlist.getNumLikes()-1;
            }else{
                isLiked--;
            }
            numLikes.setText(isLiked+" people like this.");
            this.like.setText("Like");
        }
        checker=true;
    }

    @Override
    public void onSongDeleted(Song song) {
        if(song==null){

        }else{
            if(currentPlayList.contains(song)){
                currentPlayList.remove(song);
                adapter.notifyDataSetChanged();
            }


        }
    }
}
