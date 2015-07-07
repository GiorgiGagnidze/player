package com.example.user.cloudplayer.transport;


import com.example.user.cloudplayer.model.Comment;
import com.example.user.cloudplayer.model.Like;
import com.example.user.cloudplayer.model.PlayList;
import com.example.user.cloudplayer.model.Song;

import java.util.ArrayList;

public interface NetworkEventListener {
    public void onPlayListAdded(PlayList playList);// tu null mashin ar daemata
    public void onPlayListDeleted(PlayList playList);// igive
    public void onUsersPlayListsDownloaded(ArrayList<PlayList> playLists);
    public void onCommentsDownloaded(ArrayList<Comment> comments);
    public void onLikesDownloaded(ArrayList<Like> likes);
    public void onCommentAdded(Comment comment);
    public void onSearchResultDownloaded(ArrayList<PlayList> playLists);
    public void onTopTenDownloaded(ArrayList<PlayList> playLists);
    public void onSongAdded(Song song);
    public void onSongsDownloaded(ArrayList<Song> songs);
}
