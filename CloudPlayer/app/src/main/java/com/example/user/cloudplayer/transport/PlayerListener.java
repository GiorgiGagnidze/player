package com.example.user.cloudplayer.transport;

import com.example.user.cloudplayer.model.Song;

public interface PlayerListener {
    public void onSongChanged(Song song);
}
