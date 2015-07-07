package com.example.user.cloudplayer.storage;


import android.content.res.Resources;

import com.example.user.cloudplayer.R;
import com.example.user.cloudplayer.model.Comment;
import com.example.user.cloudplayer.model.PlayList;
import com.example.user.cloudplayer.transport.NetworkEventListener;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class CloudStorage {
    private NetworkEventListener listener;
    private Resources resources;

    public void setListener(NetworkEventListener listener) {
        this.listener = listener;
    }

    public void setResources(Resources resources) {
        this.resources = resources;
    }

    public void getSearchResult(String keyword){
        ParseQuery<ParseObject> query = ParseQuery.getQuery(resources.getString(R.string.play_table));
        query.whereContains(resources.getString(R.string.name_col),keyword);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (e == null){
                    ArrayList<PlayList> playLists = new ArrayList<PlayList>();
                    for (ParseObject object: parseObjects){
                        PlayList playList =new PlayList(
                                        object.getString(resources.getString(R.string.name_col)),
                                        object.getInt(resources.getString(R.string.numlikes_col)));
                        playList.setID(object.getObjectId());
                        playList.setUserID(object.getParseUser(resources.getString(R.string.key_user)).
                                getObjectId());
                        playLists.add(playList);
                    }
                    listener.onSearchResultDownloaded(playLists);
                } else
                    listener.onSearchResultDownloaded(null);
            }
        });
    }

    public void getTopTen(){
        final int limit = 10;
        ParseQuery<ParseObject> query = ParseQuery.getQuery(resources.getString(R.string.play_table));
        query.addDescendingOrder(resources.getString(R.string.numlikes_col));
        query.setLimit(limit);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (e == null){
                    ArrayList<PlayList> playLists = new ArrayList<PlayList>();
                    for (ParseObject object: parseObjects){
                        PlayList playList =new PlayList(
                                object.getString(resources.getString(R.string.name_col)),
                                object.getInt(resources.getString(R.string.numlikes_col)));
                        playList.setID(object.getObjectId());
                        playList.setUserID(object.getParseUser(resources.getString(R.string.key_user)).
                                getObjectId());
                        playLists.add(playList);

                    }
                    listener.onTopTenDownloaded(playLists);
                } else
                    listener.onTopTenDownloaded(null);
            }
        });
    }

    public void addNewPlaylist(final PlayList playList){
        final ParseObject object = new ParseObject(resources.getString(R.string.play_table));
        object.put(resources.getString(R.string.name_col),playList.getName());
        object.put(resources.getString(R.string.numlikes_col),playList.getNumLikes());
        object.put(resources.getString(R.string.key_user), ParseUser.getCurrentUser());
        object.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null){
                    playList.setID(object.getObjectId());
                    playList.setUserID(ParseUser.getCurrentUser().getObjectId());
                    listener.onPlayListAdded(playList);
                }else
                    listener.onPlayListAdded(null);
            }
        });
    }

    public void deletePlayList(final PlayList playList){
        ParseQuery<ParseObject> query = ParseQuery.getQuery(resources.getString(
                R.string.play_table));
        query.getInBackground(playList.getID(),new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (e == null){
                    parseObject.deleteInBackground(new DeleteCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null)
                                listener.onPlayListDeleted(playList);
                            else
                                listener.onPlayListDeleted(null);
                        }
                    });
                } else
                    listener.onPlayListDeleted(null);
            }
        });
    }

    public void downloadUsersPlaylists(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery(resources.getString(R.string.play_table));
        query.whereEqualTo(resources.getString(R.string.key_user),ParseUser.getCurrentUser());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (e == null){
                    ArrayList<PlayList> playLists = new ArrayList<PlayList>();
                    for (ParseObject object: parseObjects){
                        PlayList playList =new PlayList(
                                object.getString(resources.getString(R.string.name_col)),
                                object.getInt(resources.getString(R.string.numlikes_col)));
                        playList.setID(object.getObjectId());
                        playList.setUserID(object.getParseUser(resources.getString(R.string.key_user)).
                                getObjectId());
                        playLists.add(playList);

                    }
                    listener.onUsersPlayListsDownloaded(playLists);
                } else
                    listener.onUsersPlayListsDownloaded(null);
            }
        });
    }

    public void getComments(String playlistID){

    }

    public void getLikes(String playlistID){

    }

    public void addComment(Comment comment){
//        final ParseObject object = new ParseObject(resources.getString(R.string.comment_table));
//        object.put(resources.getString(R.string.name_col),comment.getUserName());
//        object.put(resources.getString(R.string.playlistID_col),comment.getNumLikes());
//        object.put(resources.getString(R.string.text_col), comment.getText());
//        object.saveInBackground(new SaveCallback() {
//            @Override
//            public void done(ParseException e) {
//                if (e == null){
//
//                }
//            }
//        });
    }

    public void getSongs(String playListID){

    }

    public void addSong(String path,String playlistID){

    }
}
