package com.example.user.cloudplayer.ui;

import android.app.Activity;

import com.example.user.cloudplayer.model.Comment;
import com.example.user.cloudplayer.model.Like;
import com.example.user.cloudplayer.model.PlayList;
import com.example.user.cloudplayer.model.Song;
import com.example.user.cloudplayer.transport.NetworkEventListener;

import java.util.ArrayList;

public class PlayerActivity extends Activity implements NetworkEventListener {
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
