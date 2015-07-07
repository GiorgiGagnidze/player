package com.example.user.cloudplayer.ui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.example.user.cloudplayer.R;
import com.example.user.cloudplayer.adapters.CommentsActivityAdapter;
import com.example.user.cloudplayer.model.Comment;
import com.example.user.cloudplayer.model.Like;
import com.example.user.cloudplayer.model.PlayList;
import com.example.user.cloudplayer.model.Song;
import com.example.user.cloudplayer.transport.NetworkEventListener;

import java.util.ArrayList;

public class CommentsActivity extends Activity implements NetworkEventListener {

    private ListView list;
    private CommentsActivityAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        list = (ListView)findViewById(R.id.activity_comments_list_view);
        ArrayList<Comment> comments = new ArrayList<Comment>();
        for(int i = 0; i < 50; i++){
            comments.add(new Comment());
        }
        adapter = new CommentsActivityAdapter(this,comments);
        list.setAdapter(adapter);

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
}
