package com.example.user.cloudplayer.adapters;


import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.user.cloudplayer.R;
import com.example.user.cloudplayer.model.PlayList;

import java.util.ArrayList;

public class PlaylistActivityAdapter extends BaseAdapter{

    private Activity context;
    private ArrayList<PlayList> playLists;

    public PlaylistActivityAdapter(Activity context,ArrayList<PlayList> playLists){
        this.context = context;
        this.playLists = playLists;
    }

    @Override
    public int getCount() {
        return playLists.size();
    }

    @Override
    public Object getItem(int i) {
        return playLists.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null){ // inflates only when it is necessary.it is correct style of writing such as code.
            view = View.inflate(context, R.layout.activity_playlists_list_view_item,null);
        }

        TextView userName = (TextView)view.findViewById(R.id.activity_playlists_name);

        view.setMinimumHeight(125);

        return view;
    }
}
