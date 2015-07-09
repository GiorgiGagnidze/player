package com.example.user.cloudplayer.model;


import java.io.Serializable;

public class Song implements Serializable {
    private String name;
    private String ID;
    private String playListID;
    private String url;

    public Song(String name, String ID,String playListID,String url){
        this.name = name;
        this.ID = ID;
        this.playListID = playListID;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public String getID() {
        return ID;
    }

    public String getPlayListID() {
        return playListID;
    }

    public String getUrl() {
        return url;
    }
}
