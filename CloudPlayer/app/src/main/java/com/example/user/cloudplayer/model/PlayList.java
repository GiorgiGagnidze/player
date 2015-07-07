package com.example.user.cloudplayer.model;

import java.util.ArrayList;

public class PlayList {
    private String name;
    private String ID;
    private ArrayList<Song> songs;
    private int numLikes;


    public PlayList(String name ,int numLikes){
        this.name = name;
        this.numLikes = numLikes;
    }

    public String getName(){
        return name;
    }

    public String getID(){
        return ID;
    }

    public ArrayList<Song> getSongs(){
        return songs;
    }


    public int getNumLikes(){
        return numLikes;
    }

    public void setSongs(ArrayList<Song> songs) {
        this.songs = songs;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
}
