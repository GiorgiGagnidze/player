package com.example.user.cloudplayer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.example.user.cloudplayer.adapters.PlaylistRecyclerAdapter;
import com.example.user.cloudplayer.fragments.LoginDialogFragment;
import com.example.user.cloudplayer.model.Comment;
import com.example.user.cloudplayer.model.Like;
import com.example.user.cloudplayer.model.PlayList;
import com.example.user.cloudplayer.model.Song;
import com.example.user.cloudplayer.storage.CloudStorage;
import com.example.user.cloudplayer.transport.NetworkEventListener;
import com.example.user.cloudplayer.ui.PlayListActivity;
import com.example.user.cloudplayer.ui.ProfileActivity;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity implements NetworkEventListener {
    private App app;
    private EditText editText;
    private RecyclerView recyclerView;
    private ArrayList<PlayList> playLists;
    private CloudStorage cloudStorage;
    private CollapsingToolbarLayout collapsingToolbar;
    private View.OnClickListener onClickListener;
    private int mutedColor = R.attr.colorPrimary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_material);
        if (savedInstanceState == null)
            login();
        Toolbar toolbar = (Toolbar) findViewById(R.id.anim_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);

        app = (App)getApplication();
        final Activity activity = this;
        app.addListener(this);
        recyclerView = (RecyclerView)findViewById(R.id.scrollableview);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.wall);

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
                        playLists.get(itemPosition));
                startActivity(intent);
            }
        };
        editText = (EditText)findViewById(R.id.search);
        cloudStorage = App.getCloudStorage();
        if (savedInstanceState == null)
            cloudStorage.getTopTen();
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                if (charSequence.toString().equals(""))
                    cloudStorage.getTopTen();
                else
                    cloudStorage.getSearchResult(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        String keyWord = editText.getText().toString();
        if (keyWord.equals(""))
            cloudStorage.getTopTen();
        else
            cloudStorage.getSearchResult(keyWord.toString());
    }

    private void login(){
        SharedPreferences prefs = this.getSharedPreferences(
                getResources().getString(R.string.key_app), Context.MODE_PRIVATE);
        String defValue = "";
        String account = prefs.getString(getResources().getString(R.string.key_account),defValue);
        if (account.equals(defValue)){
            LoginDialogFragment login = new LoginDialogFragment();
            login.show(getFragmentManager(), getResources().getString(R.string.tag));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id == android.R.id.home) {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(getResources().getString(R.string.key_search),editText.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        editText.setText(savedInstanceState.getString(getResources().getString(R.string.key_search)));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        app.removeListener(this);
    }

    @Override
    public void onPlayListAdded(PlayList playList) {
        final int count = 10;
        if (playList == null){
            Toast.makeText(this,getResources().getString(R.string.playlist_add_alert), Toast.LENGTH_LONG)
                    .show();
            return;
        }
        String keyWord = editText.getText().toString();
        if (keyWord.equals("")){
            if (playLists.size() < count){
                playLists.add(playList);
                fillListView(playLists);
            }
        } else {
            if (playList.getName().contains(keyWord)){
                playLists.add(playList);
                fillListView(playLists);
            }
        }
    }

    @Override
    public void onPlayListDeleted(PlayList playList) {
        if(playList == null) {
            Toast.makeText(this,getResources().getString(R.string.on_playList_deleted), Toast.LENGTH_LONG)
                    .show();
        }
    }

    @Override
    public void onUsersPlayListsDownloaded(ArrayList<PlayList> playLists) {
        if(playLists == null){
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
        if (playLists != null) {
            collapsingToolbar.setTitle(getResources().getString(R.string.search_title));
            fillListView(playLists);
        }else
            Toast.makeText(this,getResources().getString(R.string.search_alert), Toast.LENGTH_LONG)
                    .show();
    }

    @Override
    public void onTopTenDownloaded(ArrayList<PlayList> playLists) {
        if (playLists != null) {
            collapsingToolbar.setTitle(getResources().getString(R.string.top_ten_title));
            fillListView(playLists);
        }else
            Toast.makeText(this,getResources().getString(R.string.top_ten_alert), Toast.LENGTH_LONG)
                    .show();
    }

    @Override
    public void onSongAdded(Song song) {
        if (song == null){
            Toast.makeText(this,getResources().getString(R.string.song_add_alert), Toast.LENGTH_LONG)
                    .show();
        }
    }

    @Override
    public void onSongsDownloaded(ArrayList<Song> songs) {
        if(songs==null){
            Toast.makeText(this,getResources().getString(R.string.songs_download_alert), Toast.LENGTH_LONG)
                    .show();
        }
    }

    @Override
    public void onLiked(Like like) {
        String keyWord = editText.getText().toString();
        if (like != null){
            if (keyWord.equals(""))
                cloudStorage.getTopTen();
        } else
            Toast.makeText(this,getResources().getString(R.string.like_alert), Toast.LENGTH_LONG)
                    .show();
    }

    @Override
    public void onHasLiked(Boolean bool) {
    }

    @Override
    public void onUnLiked(Like like) {
        String keyWord = editText.getText().toString();
        if (like != null){
            if (keyWord.equals(""))
                cloudStorage.getTopTen();
        } else
            Toast.makeText(getApplicationContext(),getResources().getString(R.string.unlike_alert),
                    Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSongDeleted(Song song) {
        if(song==null){
            Toast.makeText(getApplicationContext(),getResources().getString(R.string.song_delete_alert),
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void fillListView(ArrayList<PlayList> playLists){
        this.playLists = playLists;
        recyclerView.setAdapter(new PlaylistRecyclerAdapter(this,playLists,onClickListener));
    }

}
