package com.example.user.cloudplayer.adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.user.cloudplayer.R;
import com.example.user.cloudplayer.model.Song;

import java.util.ArrayList;


public class SongAdapter extends BaseAdapter{

    private Activity context;
    private ArrayList<Song> songs;

    public SongAdapter(Activity context, ArrayList<Song> songs){
        this.context = context;
        this.songs = songs;
    }

    @Override
    public int getCount() {
        return songs.size();
    }

    @Override
    public Object getItem(int i) {
        return songs.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null){ // inflates only when it is necessary.it is correct style of writing such as code.
            view = View.inflate(context, R.layout.activity_playlist_list_view_item,null);
        }

        TextView songName = (TextView)view.findViewById(R.id.activity_playlist_name);
        songName.setText(songs.get(i).getName());
        view.setMinimumHeight(125);

        return view;
    }
}
