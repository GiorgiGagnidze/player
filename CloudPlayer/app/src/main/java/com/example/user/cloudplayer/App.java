package com.example.user.cloudplayer;

import android.app.Application;

import com.example.user.cloudplayer.model.Comment;
import com.example.user.cloudplayer.model.Like;
import com.example.user.cloudplayer.model.PlayList;
import com.example.user.cloudplayer.model.Song;
import com.example.user.cloudplayer.storage.CloudStorage;
import com.example.user.cloudplayer.transport.Music;
import com.example.user.cloudplayer.transport.NetworkEventListener;
import com.parse.Parse;
import com.parse.ParseACL;

import java.util.ArrayList;
import java.util.HashSet;

public class App extends Application implements NetworkEventListener {
    private HashSet<NetworkEventListener> listeners;
    private static CloudStorage cloudStorage;
    private Music music;

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

    public Music getMusic() {
        return music;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initApp();
    }

    private void initApp() {
        listeners = new HashSet<NetworkEventListener>();
        cloudStorage = new CloudStorage();
        music = new Music();
        cloudStorage.setListener(this);
        Parse.initialize(this, getResources().getString(R.string.application_id),
                getResources().getString(R.string.client_key));
        ParseACL acl = new ParseACL();
        acl.setPublicReadAccess(true);
        acl.setPublicWriteAccess(true);
        ParseACL.setDefaultACL(acl,true);
        cloudStorage.setResources(getResources());
    }

    @Override
    public void onPlayListAdded(PlayList playList) {
        for (NetworkEventListener listener: listeners)
            listener.onPlayListAdded(playList);
    }

    @Override
    public void onPlayListDeleted(PlayList playList) {
        for (NetworkEventListener listener: listeners)
            listener.onPlayListDeleted(playList);
    }

    @Override
    public void onUsersPlayListsDownloaded(ArrayList<PlayList> playLists) {
        for (NetworkEventListener listener: listeners)
            listener.onUsersPlayListsDownloaded(playLists);
    }

    @Override
    public void onCommentsDownloaded(ArrayList<Comment> comments) {
        for (NetworkEventListener listener: listeners)
            listener.onCommentsDownloaded(comments);
    }

    @Override
    public void onLikesDownloaded(ArrayList<Like> likes) {
        for (NetworkEventListener listener: listeners)
            listener.onLikesDownloaded(likes);
    }

    @Override
    public void onCommentAdded(Comment comment) {
        for (NetworkEventListener listener: listeners)
            listener.onCommentAdded(comment);
    }

    @Override
    public void onSearchResultDownloaded(ArrayList<PlayList> playLists) {
        for (NetworkEventListener listener: listeners)
            listener.onSearchResultDownloaded(playLists);
    }

    @Override
    public void onTopTenDownloaded(ArrayList<PlayList> playLists) {
        for (NetworkEventListener listener: listeners)
            listener.onTopTenDownloaded(playLists);
    }

    @Override
    public void onSongAdded(Song song) {
        if (song != null)
            music.onSongAdded(song);
        for (NetworkEventListener listener: listeners)
            listener.onSongAdded(song);
    }

    @Override
    public void onSongsDownloaded(ArrayList<Song> songs) {
        for (NetworkEventListener listener: listeners)
            listener.onSongsDownloaded(songs);
    }

    @Override
    public void onLiked(Like like) {
        for (NetworkEventListener listener: listeners)
            listener.onLiked(like);
    }

    @Override
    public void onHasLiked(Boolean bool) {
        for (NetworkEventListener listener: listeners)
            listener.onHasLiked(bool);
    }

    @Override
    public void onUnLiked(Like like) {
        for (NetworkEventListener listener: listeners)
            listener.onUnLiked(like);
    }

    @Override
    public void onSongDeleted(Song song) {
        if (song != null)
            music.onSongDeleted(song);
        for (NetworkEventListener listener: listeners)
            listener.onSongDeleted(song);
    }
}
