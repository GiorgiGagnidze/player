package com.example.user.cloudplayer.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

public class PlayList implements Serializable{
    private int mData;
    private String name;
    private String ID;
    private ArrayList<Song> songs;
    private int numLikes;
    private String userID;


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

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserID() {
        return userID;
    }


}
