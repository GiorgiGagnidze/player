package com.example.user.cloudplayer.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        final EditText edit = (EditText)findViewById(R.id.activity_comments_edit_text);
        list = (ListView)findViewById(R.id.activity_comments_list_view);
        playListID = getIntent().getExtras().getString(getResources().getString(R.string.key_playlistID));
        App.getCloudStorage().getComments(playListID);
        Button button = (Button)findViewById(R.id.activity_comments_button);

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
        sendToast("PlayList could not be added");
    }

    @Override
    public void onPlayListDeleted(PlayList playList) {
        sendToast("PlayList could not be deleted");
    }

    @Override
    public void onUsersPlayListsDownloaded(ArrayList<PlayList> playLists) {
        sendToast("PlayLists could not be downloaded");
    }

    @Override
    public void onCommentsDownloaded(ArrayList<Comment> comments) {
        if(comments == null){
            sendToast("Comments for this playList could not be downloaded");
        } else {
            currentComments = comments;
            adapter = new CommentsActivityAdapter(this,currentComments);
            list.setAdapter(adapter);
        }
    }

    @Override
    public void onLikesDownloaded(ArrayList<Like> likes) {
        sendToast("Authors of likes could not be downloaded");
    }

    @Override
    public void onCommentAdded(Comment comment) {
        if(comment == null){
            sendToast("Comment could not be posted");
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

}
