package com.example.user.cloudplayer.transport;

import android.media.MediaPlayer;

import com.example.user.cloudplayer.model.Song;

import java.io.IOException;
import java.util.ArrayList;

public class Music {
    private PlayerListener listener;
    private ArrayList<Song> songs;
    private int index;
    private MediaPlayer mediaPlayer;
    public boolean looping = false;

    public Music(){

    }

    public void moveForward(){
        mediaPlayer.stop();
        index++;
        if (index == songs.size())
            index = 0;
        if (listener != null)
            listener.onSongChanged(songs.get(index));
        setSource();
        start();
    }

    public void moveBackward(){
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

    private void setSource(){
        if (mediaPlayer != null)
            mediaPlayer.stop();
        mediaPlayer = new MediaPlayer();
        setLooping(false);
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
        this.index = index;
        this.songs = songs;
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
        if (looping)
            mediaPlayer.setLooping(true);
        else
            mediaPlayer.setLooping(false);
    }
}
