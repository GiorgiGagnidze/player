package com.example.user.cloudplayer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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


public class MainActivity extends Activity implements NetworkEventListener {
    private App app;
    private EditText editText;
    private ListView listView;
    private TextView textView;
    private ArrayList<PlayList> playLists;
    private CloudStorage cloudStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null)
            login();
        app = (App)getApplication();
        final Activity activity = this;
        app.addListener(this);
        listView = (ListView)findViewById(R.id.play_lists_list_view);
        editText = (EditText)findViewById(R.id.search);
        textView = (TextView)findViewById(R.id.description);
        Button profile = (Button)findViewById(R.id.profile);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, ProfileActivity.class);
                startActivity(intent);
            }
        });
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
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(activity, PlayListActivity.class);
                intent.putExtra(activity.getResources().getString(R.string.key_playlistID),
                        playLists.get(i));
                startActivity(intent);
            }
        });
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
        if (id == R.id.action_settings) {
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
        } else {
            ArrayList<String> names = new ArrayList<String>();
            for (PlayList list : playLists)
                if (!list.getID().equals(playList.getID()))
                    names.add(list.getName());
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, names);
            listView.setAdapter(adapter);
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
            textView.setText(getResources().getString(R.string.search_title));
            fillListView(playLists);
        }else
            Toast.makeText(this,getResources().getString(R.string.search_alert), Toast.LENGTH_LONG)
                    .show();
    }

    @Override
    public void onTopTenDownloaded(ArrayList<PlayList> playLists) {
        if (playLists != null) {
            textView.setText(getResources().getString(R.string.top_ten_title));
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
        ArrayList<String> names =new ArrayList<String>();
        for (PlayList list: playLists)
            names.add(list.getName());
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,names);
        listView.setAdapter(adapter);
    }

}
