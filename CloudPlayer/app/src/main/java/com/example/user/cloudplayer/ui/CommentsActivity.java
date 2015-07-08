package com.example.user.cloudplayer.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.user.cloudplayer.App;
import com.example.user.cloudplayer.R;
import com.example.user.cloudplayer.adapters.CommentsActivityAdapter;
import com.example.user.cloudplayer.model.Comment;
import com.example.user.cloudplayer.model.Like;
import com.example.user.cloudplayer.model.PlayList;
import com.example.user.cloudplayer.model.Song;
import com.example.user.cloudplayer.transport.NetworkEventListener;
import com.parse.ParseUser;

import java.util.ArrayList;

public class CommentsActivity extends Activity implements NetworkEventListener {

    private ListView list;
    private CommentsActivityAdapter adapter;
    private String playListID;
    private ArrayList<Comment> currentComments;
    private App app;
    private EditText edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        edit = (EditText)findViewById(R.id.activity_comments_edit_text);
        list = (ListView)findViewById(R.id.activity_comments_list_view);
        playListID = getIntent().getExtras().getString(getResources().getString(R.string.key_playlistID));
        Button button = (Button)findViewById(R.id.activity_comments_button);
        app = (App)getApplication();
        app.addListener(this);
        if(savedInstanceState != null){ // if screen was rotated
            edit.setText(savedInstanceState.getString("EDIT"));
        } else {
            currentComments = new ArrayList<Comment>();
        }

        App.getCloudStorage().getComments(playListID);

        button.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        String userName = (String) ParseUser.getCurrentUser().get(getResources().getString(R.string.name_col));
                        String text = edit.getText().toString();
                        Comment comment = new Comment(playListID, userName,text);
                        App.getCloudStorage().addComment(comment);
                        edit.setText("");
                    }
                }
        );
    }

    @Override
    public void onPlayListAdded(PlayList playList) {
        sendToast(getResources().getString(R.string.on_playList_added));
    }

    @Override
    public void onPlayListDeleted(PlayList playList) {
        sendToast(getResources().getString(R.string.on_playList_deleted));
    }

    @Override
    public void onUsersPlayListsDownloaded(ArrayList<PlayList> playLists) {
        sendToast(getResources().getString(R.string.on_playLists_downloaded));
    }

    @Override
    public void onCommentsDownloaded(ArrayList<Comment> comments) {
        if(comments == null){
            sendToast(getResources().getString(R.string.on_comments_downloaded));
        } else {
            //Log.i("blabla","pizdeeec");
            currentComments = comments;
            adapter = new CommentsActivityAdapter(this,currentComments);
            list.setAdapter(adapter);
        }
    }

    @Override
    public void onLikesDownloaded(ArrayList<Like> likes) {
        sendToast(getResources().getString(R.string.on_likes_downloaded));
    }

    @Override
    public void onCommentAdded(Comment comment) {
        if(comment == null){
            sendToast(getResources().getString(R.string.on_comment_added));
        } else {
            currentComments.add(comment);
            adapter.updateListView(currentComments);
        }
    }

    @Override
    public void onSearchResultDownloaded(ArrayList<PlayList> playLists) {

    }

    @Override
    public void onTopTenDownloaded(ArrayList<PlayList> playLists) {
        sendToast(getResources().getString(R.string.top_ten_alert));
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
        outState.putString("EDIT", edit.getText().toString());
    }

}
