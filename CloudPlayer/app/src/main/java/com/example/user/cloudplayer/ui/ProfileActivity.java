package com.example.user.cloudplayer.ui;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
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
    private Button addButton;
    private Button deleteButton;
    private EditText edit;
    private int clickedPos = -1;
    private ArrayList<PlayList> currentPlayLists;
    private App app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        addButton = (Button)findViewById(R.id.activity_profile_button_add);
        deleteButton = (Button)findViewById(R.id.activity_profile_button_delete);
        edit = (EditText)findViewById(R.id.activity_profile_edit_text);

        list = (ListView)findViewById(R.id.activity_profile_list_view);

        app = (App)getApplication();
        app.addListener(this);

        App.getCloudStorage().downloadUsersPlaylists();

        if(savedInstanceState != null){ // if screen was rotated
            clickedPos = savedInstanceState.getInt("CLICKED_POS");
            edit.setText(savedInstanceState.getString("EDIT"));
        }

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
        if(playList == null){
            sendToast("PlayList could not be added");
        } else {
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
        if(playList == null){
            sendToast("PlayList could not be deleted");
        } else {
            int index = findPlayListByID(playList.getID());
            if(index != -1){
                currentPlayLists.remove(index);
                adapter.updateListView(currentPlayLists);
            } else {
                sendToast("PlayList was already deleted...");
            }
        }
    }

    @Override
    public void onUsersPlayListsDownloaded(ArrayList<PlayList> playLists) {
        if(playLists == null){
            sendToast("PlayLists could not be downloaded");
        } else {
            currentPlayLists = playLists;
            adapter = new ProfileActivityAdapter(this,currentPlayLists);
            list.setAdapter(adapter);
        }
    }

    @Override
    public void onCommentsDownloaded(ArrayList<Comment> comments) {
        sendToast("Comments could not be downloaded");
    }

    @Override
    public void onLikesDownloaded(ArrayList<Like> likes) {
        sendToast("Authors of likes could not be downloaded");
    }

    @Override
    public void onCommentAdded(Comment comment) {
        sendToast("Comment could not be added");
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

    private void sendToast(CharSequence text){
        Context context = this;
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        app.removeListener(this);
    }

    @Override
    public void onSaveInstanceState (Bundle outState) {
        outState.putString("EDIT",edit.getText().toString());
        outState.putInt("CLICKED_POS",clickedPos);
    }

}
