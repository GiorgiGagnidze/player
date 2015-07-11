package com.example.user.cloudplayer.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.user.cloudplayer.App;
import com.example.user.cloudplayer.R;
import com.example.user.cloudplayer.adapters.ProfileActivityAdapter;
import com.example.user.cloudplayer.model.Comment;
import com.example.user.cloudplayer.model.Like;
import com.example.user.cloudplayer.model.PlayList;
import com.example.user.cloudplayer.model.Song;
import com.example.user.cloudplayer.transport.NetworkEventListener;

import java.util.ArrayList;


public class ProfileActivity extends Activity implements NetworkEventListener{

    private ListView list;
    private ProfileActivityAdapter adapter;
    private EditText edit;
    private int clickedPos = -1;
    private ArrayList<PlayList> currentPlayLists;
    private App app;
    private static final String edit_key = "EDIT";
    private static final String click_key = "CLICKED_POS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Button addButton = (Button)findViewById(R.id.activity_profile_button_add);
        Button deleteButton = (Button)findViewById(R.id.activity_profile_button_delete);
        edit = (EditText)findViewById(R.id.activity_profile_edit_text);

        list = (ListView)findViewById(R.id.activity_profile_list_view);

        app = (App)getApplication();
        app.addListener(this);
        if(savedInstanceState != null){ // if screen was rotated
            clickedPos = savedInstanceState.getInt(click_key);
            edit.setText(savedInstanceState.getString(edit_key));
        } else {
            currentPlayLists = new ArrayList<PlayList>();
        }
        App.getCloudStorage().downloadUsersPlaylists();
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                clickedPos = position;
                edit.setText(currentPlayLists.get(clickedPos).getName());
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if(edit.getText().length() > 0){
                    PlayList playList = new PlayList(edit.getText().toString(),0);
                    App.getCloudStorage().addNewPlaylist(playList);
                    edit.setText("");
                    clickedPos = -1;
                }
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if(clickedPos != -1){
                    App.getCloudStorage().deletePlayList(currentPlayLists.get(clickedPos));
                    edit.setText("");
                    clickedPos = -1;
                }
            }
        });

    }

    @Override
    public void onPlayListAdded(PlayList playList) {
        if(playList != null){
            currentPlayLists.add(playList);
            adapter.updateListView(currentPlayLists);
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
                adapter.updateListView(currentPlayLists);
            }
        }
    }

    @Override
    public void onUsersPlayListsDownloaded(ArrayList<PlayList> playLists) {
        if(playLists != null){
            currentPlayLists = playLists;
            adapter = new ProfileActivityAdapter(this,currentPlayLists);
            list.setAdapter(adapter);
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
        outState.putInt(click_key, clickedPos);
    }

    @Override
    protected void onResume() {
        super.onResume();
        App.getCloudStorage().downloadUsersPlaylists();
    }

}
