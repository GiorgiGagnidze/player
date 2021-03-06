package com.example.user.cloudplayer.ui;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.graphics.Palette;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.user.cloudplayer.App;
import com.example.user.cloudplayer.R;
import com.example.user.cloudplayer.fragments.AddToMyPlaylistDialogFragment;
import com.example.user.cloudplayer.model.Comment;
import com.example.user.cloudplayer.model.Like;
import com.example.user.cloudplayer.model.PlayList;
import com.example.user.cloudplayer.model.Song;
import com.example.user.cloudplayer.transport.Music;
import com.example.user.cloudplayer.transport.NetworkEventListener;
import com.example.user.cloudplayer.transport.PlayerListener;
import com.parse.ParseUser;

import java.util.ArrayList;

public class PlayerActivity extends Activity implements NetworkEventListener,PlayerListener {

    private App app;
    private Music music;
    private Song song;
    private static final int SECOND = 1000;
    private SeekBar seekBar;
    private Handler mHandler = new Handler();
    private TextView name;
    private boolean isMe=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        final LinearLayout linearLayout = (LinearLayout)findViewById(R.id.player);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.images);
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                int mutedColor = palette.getMutedColor(R.attr.colorPrimary);
                linearLayout.setBackgroundColor(mutedColor);
            }
        });
        app = (App)getApplication();
        final Activity a=this;
        app.addListener(this);
        music = app.getMusic();
        music.setListener(this);
        seekBar = (SeekBar)findViewById(R.id.seekBar);
        seekBar.setMax(music.getDuration()/SECOND);
        name = (TextView)findViewById(R.id.song_name);
        if (savedInstanceState == null)
            song = (Song)getIntent().getExtras().get(getResources().getString(R.string.key_song));
        else
            song = (Song)savedInstanceState.getSerializable(getResources().getString(R.string.key_song));
        name.setText(song.getName());
        ImageButton button=(ImageButton)findViewById(R.id.activity_player_delete_add);
        final ParseUser user= ParseUser.getCurrentUser();
        String userId=getIntent().getExtras().getString(getResources().getString(R.string.key_user));
        if(userId.equals(user.getObjectId())){
            isMe=true;
            button.setImageResource(android.R.drawable.ic_delete);
        }else{
            button.setImageResource(android.R.drawable.ic_input_add);
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isMe){
                    App.getCloudStorage().deleteSong(song);
                }else{
                    AddToMyPlaylistDialogFragment dial=new AddToMyPlaylistDialogFragment();
                    Bundle args = new Bundle();
                    args.putString(a.getResources().getString(R.string.key_user), user.getObjectId());
                    args.putSerializable(a.getResources().getString(R.string.key_song),song);
                    dial.setArguments(args);
                    dial.show(getFragmentManager(), getResources().getString(R.string.tag));
                }
            }
        });
        PlayerActivity.this.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                int mCurrentPosition = music.getCurrentPosition() / SECOND;
                seekBar.setProgress(mCurrentPosition);
                mHandler.postDelayed(this, SECOND);
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(b){
                    music.seekTo(i*SECOND);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        music.start();
        ImageButton previous = (ImageButton)findViewById(R.id.activity_player_previous);
        ImageButton next = (ImageButton)findViewById(R.id.activity_player_next);
        final ImageButton stopResume = (ImageButton)findViewById(R.id.activity_player_stop_resume);
        final ImageButton loop = (ImageButton)findViewById(R.id.activity_player_loop);
        if(music.getLooping()){
            loop.setBackgroundResource(R.drawable.round_button_1);
        } else {
            loop.setBackgroundResource(R.drawable.round_button);
        }

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                music.moveBackward();
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                music.moveForward();
            }
        });


        stopResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(music.isPlaying()){
                    music.pause();
                    stopResume.setImageResource(android.R.drawable.ic_media_play);
                } else {
                    music.start();
                    stopResume.setImageResource(android.R.drawable.ic_media_pause);
                }
            }
        });

        loop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(music.getLooping()){
                    music.setLooping(false);
                    loop.setBackgroundResource(R.drawable.round_button);
                } else {
                    music.setLooping(true);
                    loop.setBackgroundResource(R.drawable.round_button_1);
                }
            }
        });


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

    @Override
    public void onLiked(Like like) {
    }

    @Override
    public void onHasLiked(Boolean bool) {
    }

    @Override
    public void onUnLiked(Like like) {
    }

    @Override
    public void onSongDeleted(Song song) {
        if(song!=null){
            if(song.getID().equals(this.song.getID())){
                finish();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        app.removeListener(this);
        music.setListener(null);
    }

    @Override
    public void onSongChanged(Song song) {
        this.song = song;
        name.setText(song.getName());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(getResources().getString(R.string.key_song), song);
    }
}
