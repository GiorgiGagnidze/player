package com.example.user.cloudplayer.ui;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.cloudplayer.App;
import com.example.user.cloudplayer.R;
import com.example.user.cloudplayer.model.Comment;
import com.example.user.cloudplayer.model.Like;
import com.example.user.cloudplayer.model.PlayList;
import com.example.user.cloudplayer.model.Song;
import com.example.user.cloudplayer.transport.Music;
import com.example.user.cloudplayer.transport.NetworkEventListener;
import com.example.user.cloudplayer.transport.PlayerListener;

import java.util.ArrayList;

public class PlayerActivity extends Activity implements NetworkEventListener,PlayerListener {

    private App app;
    private Music music;
    private Song song;
    private static final int SECOND = 1000;
    private SeekBar seekBar;
    private Handler mHandler = new Handler();
    private TextView name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        app = (App)getApplication();
        app.addListener(this);
        music = app.getMusic();
        music.setListener(this);
        seekBar = (SeekBar)findViewById(R.id.seekBar);
        seekBar.setMax(music.getDuration()/SECOND);
        name = (TextView)findViewById(R.id.song_name);
        song = (Song)getIntent().getExtras().get(getResources().getString(R.string.key_song));
        name.setText(song.getName());
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
        if(playList == null) {
            Toast.makeText(this, getResources().getString(R.string.on_playList_added), Toast.LENGTH_LONG)
                    .show();
        }
    }

    @Override
    public void onPlayListDeleted(PlayList playList) {
        if(playList == null) {
            Toast.makeText(this,getResources().getString(R.string.on_playList_deleted), Toast.LENGTH_LONG)
                    .show();
        } else {
            // tu es simgera am playlistshia finish()
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
        if(comments == null){
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
        if (song == null){
            Toast.makeText(this,getResources().getString(R.string.song_add_alert), Toast.LENGTH_LONG)
                    .show();
        } else {
            // aq sheidzleba dachirdes shemdeg simgerad gachitva damatebuli simgeris
        }
    }

    @Override
    public void onSongsDownloaded(ArrayList<Song> songs) {
    }

    @Override
    public void onLiked(Like like) {
        if(like==null){
            Toast.makeText(this,getResources().getString(R.string.like_alert), Toast.LENGTH_LONG)
                    .show();
        }
    }

    @Override
    public void onHasLiked(Boolean bool) {
    }

    @Override
    public void onUnLiked(Like like) {
        if(like==null){
            Toast.makeText(getApplicationContext(),getResources().getString(R.string.unlike_alert),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onSongDeleted(Song song) {
        if(song==null){
            Toast.makeText(getApplicationContext(),getResources().getString(R.string.song_delete_alert),
                    Toast.LENGTH_SHORT).show();
        } else {
            // tavis pasuxi aq, shemowmeba da gatishva
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
}
