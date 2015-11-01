package com.example.user.cloudplayer.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;

import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.cloudplayer.App;
import com.example.user.cloudplayer.R;

import com.example.user.cloudplayer.adapters.SongAdapter;
import com.example.user.cloudplayer.adapters.SongRecyclerAdapter;
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


public class PlayListActivity extends ActionBarActivity implements NetworkEventListener {
    private RecyclerView recyclerView;
    private SongRecyclerAdapter adapter;
    private TextView numLikes;
    private ArrayList<Song> currentPlayList;
    private PlayList playlist;
    private int isLiked;
    private boolean checker;
    private App app;
    private boolean songAdd;
    private FloatingActionButton actionButton;
    private View.OnClickListener onClickListener;
    private int mutedColor = R.attr.colorPrimary;
    private CollapsingToolbarLayout collapsingToolbar;
    private int likeResource = -1000;
    private FloatingActionButton addSong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_material);
        playlist=(PlayList)getIntent().getExtras().get(this.getResources().getString(R.string.key_playlistID));
        final Activity activity = this;
        songAdd=true;
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(playlist.getName());
        final ParseUser user=ParseUser.getCurrentUser();
        Toolbar toolbar = (Toolbar) findViewById(R.id.anim_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        addSong = (FloatingActionButton) findViewById(R.id.add);
        numLikes = (TextView) findViewById(R.id.num_likes);
        recyclerView = (RecyclerView)findViewById(R.id.scrollableview);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.mixer);
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                mutedColor = palette.getDarkMutedColor(R.attr.colorPrimary);
                recyclerView.setBackgroundColor(palette.getMutedColor(R.attr.colorPrimary));
                collapsingToolbar.setContentScrimColor(mutedColor);
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int itemPosition = recyclerView.getChildAdapterPosition(view);
                Intent intent = new Intent(activity, PlayListActivity.class);
                intent.putExtra(activity.getResources().getString(R.string.key_playlistID),
                        currentPlayList.get(itemPosition));
                startActivity(intent);
            }
        };

        actionButton = (FloatingActionButton)findViewById(R.id.floating);
        actionButton.setRippleColor(getResources().getColor(R.color.primary_500));
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
        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = recyclerView.getChildAdapterPosition(view);
                Intent intent = new Intent(activity, PlayerActivity.class);
                intent.putExtra(activity.getResources().getString(R.string.key_song),
                        currentPlayList.get(position));
                intent.putExtra(activity.getResources().getString(R.string.key_user),
                        playlist.getUserID());
                music.setPlaylist(currentPlayList,position);
                startActivity(intent);
            }
        };
        numLikes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LikesDialogFragment dial=new LikesDialogFragment();
                Bundle args = new Bundle();
                args.putString(activity.getResources().getString(R.string.key_playlistID), playlist.getID());
                dial.setArguments(args);
                dial.show(getFragmentManager(), getResources().getString(R.string.tag));

            }
        });
        addSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(songAdd) {
                    AddSongDialogFragment addSong = new AddSongDialogFragment();
                    Bundle args = new Bundle();
                    args.putString(activity.getResources().getString(R.string.key_playlistID), playlist.getID());
                    addSong.setArguments(args);
                    addSong.show(getFragmentManager(), getResources().getString(R.string.tag));
                }else
                    Toast.makeText(activity.getApplicationContext(),getResources().getString(R.string.add_song_twice),Toast.LENGTH_SHORT);
            }
        });

        if(playlist.getUserID().equals(user.getObjectId())){
             addSong.setVisibility(View.VISIBLE);
        }
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Like l = new Like(playlist.getID(), user.getString(getResources().getString(R.string.name_col)));
                if(checker) {
                    if (likeResource == R.drawable.like) {
                        App.getCloudStorage().addLike(l);
                        checker=false;

                    } else if (likeResource == R.drawable.dislike) {
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
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id == android.R.id.home) {
            Intent intent = new Intent(this, CommentsActivity.class);
            intent.putExtra(this.getResources().getString(R.string.key_playlistID), playlist.getID());
            startActivity(intent);
            return true;
        } else if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onPlayListAdded(PlayList playList) {
    }

    @Override
    public void onPlayListDeleted(PlayList playList) {
        if(playList!= null && playlist.getID().equals(this.playlist.getID())){
            finish();
        }
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
        if(song!=null){
            if (playlist.getID().equals(song.getPlayListID())) {
                currentPlayList.add(song);
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onSongsDownloaded(ArrayList<Song> songs) {

        if(songs==null){
            currentPlayList=new ArrayList<Song>();
        }else{
            currentPlayList=songs;
        }

        adapter=new SongRecyclerAdapter(this,currentPlayList,onClickListener);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onLiked(Like like) {
        if(like!=null){
            if(isLiked==-1){
                isLiked=playlist.getNumLikes()+1;
            }
            else{
                isLiked++;
            }
            numLikes.setText(isLiked+" "+getResources().getString(R.string.like_text));
            actionButton.setImageResource(R.drawable.dislike);
            likeResource = R.drawable.dislike;
        }
        checker=true;
    }

    @Override
    public void onHasLiked(Boolean bool) {
        if(bool != null && bool) {
            actionButton.setImageResource(R.drawable.dislike);
            likeResource = R.drawable.dislike;
        }else{
            actionButton.setImageResource(R.drawable.like);
            likeResource = R.drawable.like;
        }
    }

    @Override
    public void onUnLiked(Like like) {
        if(like!=null){
            if(isLiked==-1){
                isLiked=playlist.getNumLikes()-1;
            }else{
                isLiked--;
            }
            numLikes.setText(isLiked+" "+getResources().getString(R.string.like_text));
            actionButton.setImageResource(R.drawable.like);
            likeResource = R.drawable.like;
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
        if(song!=null){
            App.getCloudStorage().getSongs(playlist.getID());
        }
    }
}
