package com.example.user.cloudplayer.transport;

import android.media.MediaPlayer;

import com.example.user.cloudplayer.model.Song;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Music {
    private PlayerListener listener;
    private List<Song> songs;
    private int index;
    private MediaPlayer mediaPlayer;
    private boolean looping = false;
    private final Object object = new Object();

    public synchronized void moveForward(){
        mediaPlayer.stop();
        index++;
        if (index == songs.size())
            index = 0;
        if (listener != null)
            listener.onSongChanged(songs.get(index));
        setSource();
        start();
    }

    public synchronized void moveBackward(){
        mediaPlayer.stop();
        index--;
        if (index == -1)
            index = songs.size()-1;
        if (listener != null)
            listener.onSongChanged(songs.get(index));
        setSource();
        start();
    }

    public void setListener(PlayerListener listener) {
        this.listener = listener;
    }

    public void start(){
        mediaPlayer.start();
    }

    public void pause(){
        mediaPlayer.pause();
    }

    public boolean isPlaying(){
        return mediaPlayer.isPlaying();
    }

    private void setSource(){
        if (mediaPlayer != null)
            mediaPlayer.stop();
        mediaPlayer = new MediaPlayer();
        setLooping(looping);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                if (!looping){
                    moveForward();
                }
            }
        });
        try {
            mediaPlayer.setDataSource(songs.get(index).getUrl());
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setPlaylist(ArrayList<Song> songs, int index){
        boolean toSet = true;
        if (this.songs != null && this.songs.get(this.index).getID().equals(songs.get(index).getID())) {
            toSet = false;
        }
        this.index = index;
        this.songs = Collections.synchronizedList(songs);
        if (toSet)
            setSource();
    }

    public int getDuration(){
        return mediaPlayer.getDuration();
    }

    public int getCurrentPosition(){
        return mediaPlayer.getCurrentPosition();
    }

    public void seekTo(int time){
        mediaPlayer.seekTo(time);
    }

    public void setLooping(boolean looping){
        this.looping = looping;
        mediaPlayer.setLooping(looping);
    }

    public boolean getLooping(){
        return looping;
    }

    public void onSongDeleted(Song song){
        if (songs!= null && song.getPlayListID().equals(songs.get(index).getPlayListID())){
            if (song.getID().equals(songs.get(index).getID())){
                mediaPlayer.stop();
                songs =null;
            } else {
                synchronized (object) {
                    for (int i = 0; i < songs.size(); i++) {
                        if (song.getID().equals(songs.get(i).getID())) {
                            songs.remove(index);
                            break;
                        }
                    }
                }
            }
        }
    }

    public void onSongAdded(Song song){
        if (songs!= null && song.getPlayListID().equals(songs.get(index).getPlayListID())){
            songs.add(song);
        }
    }
}
