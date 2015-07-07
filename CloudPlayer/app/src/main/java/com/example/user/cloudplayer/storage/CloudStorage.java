package com.example.user.cloudplayer.storage;


import android.content.res.Resources;

import com.example.user.cloudplayer.R;
import com.example.user.cloudplayer.model.PlayList;
import com.example.user.cloudplayer.transport.NetworkEventListener;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
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
                    ArrayList<PlayList> playLists = new ArrayList<>();
                    for (ParseObject object: parseObjects){
                        PlayList playList =new PlayList(
                                        object.getString(resources.getString(R.string.name_col)),
                                        object.getInt(resources.getString(R.string.numlikes_col)));
                        playList.setID(object.getObjectId());
                        playLists.add(playList);
                    }
                    //listeneris gamodzaxeba aq da else shi
                }
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
                    ArrayList<PlayList> playLists = new ArrayList<>();
                    for (ParseObject object: parseObjects){
                        PlayList playList =new PlayList(
                                object.getString(resources.getString(R.string.name_col)),
                                object.getInt(resources.getString(R.string.numlikes_col)));
                        playList.setID(object.getObjectId());
                        playLists.add(playList);
                    }
                    //listeneris gamodzaxeba aq da else shi
                }
            }
        });
    }

    public void addNewPlaylist(final PlayList playList){
        final ParseObject object = new ParseObject(resources.getString(R.string.play_table));
        object.put(resources.getString(R.string.name_col),playList.getName());
        object.put(resources.getString(R.string.numlikes_col),playList.getNumLikes());
        object.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null){
                    playList.setID(object.getObjectId());
                    // listeneris aq da elseshi
                }
            }
        });
    }
}
