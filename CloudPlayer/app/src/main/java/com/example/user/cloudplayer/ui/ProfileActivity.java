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
import android.view.View;
import android.widget.EditText;

import com.example.user.cloudplayer.App;
import com.example.user.cloudplayer.R;
import com.example.user.cloudplayer.adapters.ProfileRecyclerAdapter;
import com.example.user.cloudplayer.model.Comment;
import com.example.user.cloudplayer.model.Like;
import com.example.user.cloudplayer.model.PlayList;
import com.example.user.cloudplayer.model.Song;
import com.example.user.cloudplayer.transport.NetworkEventListener;

import java.util.ArrayList;


public class ProfileActivity extends ActionBarActivity implements NetworkEventListener{
    private RecyclerView recyclerView;
    private ProfileRecyclerAdapter recyclerAdapter;
    private EditText edit;
    private ArrayList<PlayList> currentPlayLists;
    private App app;
    private static final String edit_key = "EDIT";
    private int mutedColor = R.attr.colorPrimary;
    private View.OnClickListener onClickListener;
    private CollapsingToolbarLayout collapsingToolbar;
    private FloatingActionButton actionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_material);
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);

        edit = (EditText)findViewById(R.id.activity_profile_edit_text);
        actionButton = (FloatingActionButton)findViewById(R.id.floating);

        recyclerView = (RecyclerView)findViewById(R.id.scrollableview);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.mixer);
        final Activity activity = this;

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
                        currentPlayLists.get(itemPosition));
                startActivity(intent);
            }
        };


        app = (App)getApplication();
        app.addListener(this);
        if(savedInstanceState != null){ // if screen was rotated
            edit.setText(savedInstanceState.getString(edit_key));
        } else {
            currentPlayLists = new ArrayList<PlayList>();
        }

        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edit.getText().length() > 0){
                    PlayList playList = new PlayList(edit.getText().toString(),0);
                    App.getCloudStorage().addNewPlaylist(playList);
                    edit.setText("");
                }
            }
        });

    }

    @Override
    public void onPlayListAdded(PlayList playList) {
        if(playList != null){
            currentPlayLists.add(playList);
            recyclerAdapter.updateListView(currentPlayLists);
        }
    }

    private int findPlayListByID(String id){
        for(int i = 0; i < currentPlayLists.size(); i++){
            if(currentPlayLists.get(i).getID().equals(id)){
                return i;
            }
        }
        return -1;
    }


    @Override
    public void onPlayListDeleted(PlayList playList) {
        if(playList != null){
            int index = findPlayListByID(playList.getID());
            if(index != -1){
                currentPlayLists.remove(index);
                recyclerAdapter.updateListView(currentPlayLists);
            }
        }
    }

    @Override
    public void onUsersPlayListsDownloaded(ArrayList<PlayList> playLists) {
        if(playLists != null){
            currentPlayLists = playLists;
            recyclerAdapter = new ProfileRecyclerAdapter(this,currentPlayLists,onClickListener);
            recyclerView.setAdapter(recyclerAdapter);
        }
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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        app.removeListener(this);
    }

    @Override
    public void onSaveInstanceState (Bundle outState) {
        outState.putString(edit_key, edit.getText().toString());
    }

    @Override
    protected void onResume() {
        super.onResume();
        App.getCloudStorage().downloadUsersPlaylists();
    }

}
