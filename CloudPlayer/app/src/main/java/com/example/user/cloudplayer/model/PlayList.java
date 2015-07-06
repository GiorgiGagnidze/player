package com.example.user.cloudplayer.model;

import java.util.ArrayList;

public class PlayList {
    private String name;
    private String ID;
    private ArrayList<Song> songs;
    private int numLikes;


    public PlayList(String name,String ID,ArrayList<Song> songs,int numLikes){
        this.name = name;
        this.ID = ID;
        this.songs = songs;
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



}
