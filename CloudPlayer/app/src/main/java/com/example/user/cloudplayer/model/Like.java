package com.example.user.cloudplayer.model;


public class Like {

    private String playListID;
    private String userName;

    public Like(String playListID,String userName){
        this.playListID = playListID;
        this.userName = userName;
    }

    public String getPlayListID(){
        return playListID;
    }

    public String getUserName(){
        return userName;
    }

}
