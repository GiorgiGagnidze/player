package com.example.user.cloudplayer.model;


public class Comment {

    private String playListID;
    private String userName;
    private String text;

    public Comment(String playListID,String userName,String text){
        this.playListID = playListID;
        this.userName = userName;
        this.text = text;
    }

    public String getPlayListID(){
        return playListID;
    }

    public String getUserName(){
        return userName;
    }

    public String getText(){
        return text;
    }

}
