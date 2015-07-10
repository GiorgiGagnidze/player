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
import com.example.user.cloudplayer.transport.Music;
import com.example.user.cloudplayer.transport.NetworkEventListener;
import com.parse.ParseUser;

import java.util.ArrayList;


public class PlayListActivity extends Activity implements NetworkEventListener {
    private ListView list;
    private SongAdapter adapter;
    private Button like;
    private TextView numLikes;
    private ArrayList<Song> currentPlayList;
    private PlayList playlist;
    private int isLiked;
    private boolean checker;
    private App app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);
        playlist=(PlayList)getIntent().getExtras().get(this.getResources().getString(R.string.key_playlistID));
        final Activity a = this;
        final ParseUser user=ParseUser.getCurrentUser();
        Button comment = (Button) findViewById(R.id.comment);
        like = (Button) findViewById(R.id.like_button);
        Button addSong = (Button) findViewById(R.id.add_button);
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
        app = (App)getApplication();
        final Music music=app.getMusic();
        if(isLiked!=-1) numLikes.setText(isLiked + " "+getResources().getString(R.string.like_text));
        else numLikes.setText(playlist.getNumLikes() +" "+ getResources().getString(R.string.like_text));
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(a, PlayerActivity.class);
                intent.putExtra(a.getResources().getString(R.string.key_song),
                        currentPlayList.get(position));
                intent.putExtra(a.getResources().getString(R.string.key_user),
                        playlist.getUserID());
                music.setPlaylist(currentPlayList,position);
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
                    if (like.getText().toString().equals(getResources().getString(R.string.like))) {
                        App.getCloudStorage().addLike(l);
                        checker=false;

                    } else if (like.getText().toString().equals(getResources().getString(R.string.unlike))) {
                        App.getCloudStorage().unLike(l);
                        checker=false;
                    }

                }
            }
        });
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
        if(playList == null) {
            Toast.makeText(this,getResources().getString(R.string.on_playList_added), Toast.LENGTH_LONG)
                    .show();
        }
    }

    @Override
    public void onPlayListDeleted(PlayList playList) {
        if(playlist==null){
            Toast.makeText(this,getResources().getString(R.string.on_playList_deleted), Toast.LENGTH_LONG)
                    .show();
        }else if(playlist.getID().equals(this.playlist.getID())){
            finish();
        }
    }

    @Override
    public void onUsersPlayListsDownloaded(ArrayList<PlayList> playLists) {
        if(playLists == null) {
            Toast.makeText(this,getResources().getString(R.string.on_playLists_downloaded), Toast.LENGTH_LONG)
                    .show();
        }
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
        if(likes == null) {
            Toast.makeText(this,getResources().getString(R.string.on_likes_downloaded), Toast.LENGTH_LONG)
                    .show();
        }
    }

    @Override
    public void onCommentAdded(Comment comment) {
        if(comment == null){
            Toast.makeText(this,getResources().getString(R.string.on_comment_added), Toast.LENGTH_LONG)
                    .show();
        }
    }

    @Override
    public void onSearchResultDownloaded(ArrayList<PlayList> playLists) {
        if(playLists == null){
            Toast.makeText(this,getResources().getString(R.string.search_alert), Toast.LENGTH_LONG)
                    .show();
        }
    }

    @Override
    public void onTopTenDownloaded(ArrayList<PlayList> playLists) {
        if(playLists == null) {
            Toast.makeText(this,getResources().getString(R.string.top_ten_alert), Toast.LENGTH_LONG)
                    .show();
        }
    }

    @Override
    public void onSongAdded(Song song) {
        if(song!=null){
            if (playlist.getID().equals(song.getPlayListID())) {
                currentPlayList.add(song);
                adapter.notifyDataSetChanged();
            }
        } else {
            Toast.makeText(this,getResources().getString(R.string.song_add_alert), Toast.LENGTH_LONG)
                    .show();
        }
    }

    @Override
    public void onSongsDownloaded(ArrayList<Song> songs) {
        if(songs==null){
            Toast.makeText(this,getResources().getString(R.string.songs_download_alert), Toast.LENGTH_LONG)
                    .show();
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
            Toast.makeText(this,getResources().getString(R.string.like_alert), Toast.LENGTH_LONG)
                    .show();
        }else{
            if(isLiked==-1){
                isLiked=playlist.getNumLikes()+1;
            }
            else{
                isLiked++;
            }
            numLikes.setText(isLiked+" "+getResources().getString(R.string.like_text));
            this.like.setText(getResources().getString(R.string.unlike));
        }
        checker=true;
    }

    @Override
    public void onHasLiked(Boolean bool) {
        if(bool)
            like.setText(getResources().getString(R.string.unlike));
        else
            like.setText(getResources().getString(R.string.like));
        // tu nullia ra xdeba, ra knopka gamovides
    }

    @Override
    public void onUnLiked(Like like) {
        if(like==null){
            Toast.makeText(getApplicationContext(),getResources().getString(R.string.unlike_alert),
                    Toast.LENGTH_SHORT).show();
        }
        else{
            if(isLiked==-1){
                isLiked=playlist.getNumLikes()-1;
            }else{
                isLiked--;
            }
            numLikes.setText(isLiked+" "+getResources().getString(R.string.like_text));
            this.like.setText(getResources().getString(R.string.like));
        }
        checker=true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        app.removeListener(this);
    }

    @Override
    public void onSongDeleted(Song song) {
        if(song==null){
            Toast.makeText(getApplicationContext(),getResources().getString(R.string.song_delete_alert),
                    Toast.LENGTH_SHORT).show();
        }else{
            if(currentPlayList.contains(song)){
                currentPlayList.remove(song);
                adapter.notifyDataSetChanged();
            }
        }
    }
}
