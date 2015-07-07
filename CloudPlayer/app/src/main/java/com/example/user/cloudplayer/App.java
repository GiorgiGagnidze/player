package com.example.user.cloudplayer;

import android.app.Application;

import com.example.user.cloudplayer.model.Comment;
import com.example.user.cloudplayer.model.Like;
import com.example.user.cloudplayer.model.PlayList;
import com.example.user.cloudplayer.model.Song;
import com.example.user.cloudplayer.storage.CloudStorage;
import com.example.user.cloudplayer.transport.NetworkEventListener;
import com.parse.Parse;
import com.parse.ParseACL;

import java.util.ArrayList;
import java.util.HashSet;

public class App extends Application implements NetworkEventListener {
    private HashSet<NetworkEventListener> listeners;
    private static CloudStorage cloudStorage;

    public static CloudStorage getCloudStorage(){
        return cloudStorage;
    }

    public void addListener(NetworkEventListener networkEventListener) {
        if (!listeners.contains(networkEventListener))
            listeners.add(networkEventListener);
    }

    public void removeListener(NetworkEventListener listener){
        if (listeners.contains(listener))
            listeners.remove(listener);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initApp();
    }

    private void initApp() {
        listeners = new HashSet<NetworkEventListener>();
        cloudStorage = new CloudStorage();
        cloudStorage.setListener(this);
        Parse.initialize(this, getResources().getString(R.string.application_id),
                getResources().getString(R.string.client_key));
        ParseACL acl = new ParseACL();
        acl.setPublicReadAccess(true);
        ParseACL.setDefaultACL(acl,true);
        cloudStorage.setResources(getResources());
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
