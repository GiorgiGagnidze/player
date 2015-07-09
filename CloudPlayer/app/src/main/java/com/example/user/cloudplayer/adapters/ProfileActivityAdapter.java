package com.example.user.cloudplayer.adapters;


import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.user.cloudplayer.R;
import com.example.user.cloudplayer.model.PlayList;
import com.example.user.cloudplayer.ui.PlayListActivity;

import java.util.ArrayList;

public class ProfileActivityAdapter extends BaseAdapter{

    private Activity context;
    private ArrayList<PlayList> playLists;

    public ProfileActivityAdapter(Activity context, ArrayList<PlayList> playLists){
        this.context = context;
        this.playLists = playLists;
    }


    public void updateListView(ArrayList<PlayList> playLists){
        this.playLists = playLists;
        this.notifyDataSetChanged();
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
            view = View.inflate(context, R.layout.activity_profile_list_view_item,null);
        }
        TextView playListName = (TextView)view.findViewById(R.id.activity_profile_name);
        playListName.setText(playLists.get(i).getName());
        final int index = i;
        TextView playList = (TextView) view.findViewById(R.id.playList_hyper);
        playList.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent playListIntent = new Intent(context, PlayListActivity.class);
                playListIntent.putExtra(context.getResources().getString(R.string.key_playlistID),playLists.get(index));
                context.startActivity(playListIntent);

            }
        });

        view.setMinimumHeight(125);
        return view;
    }
}
