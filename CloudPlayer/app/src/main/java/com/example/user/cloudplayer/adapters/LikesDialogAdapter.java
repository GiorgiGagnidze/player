package com.example.user.cloudplayer.adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.user.cloudplayer.R;
import com.example.user.cloudplayer.model.Like;

import java.util.ArrayList;


public class LikesDialogAdapter extends BaseAdapter {

    private Activity context;
    private ArrayList<Like> likes;

    public LikesDialogAdapter(Activity context,ArrayList<Like> likes){
        this.context = context;
        this.likes = likes;
    }

    @Override
    public int getCount() {
        return likes.size();
    }

    @Override
    public Object getItem(int i) {
        return likes.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null){ // inflates only when it is necessary.it is correct style of writing such as code.
            view = View.inflate(context, R.layout.likes_dialog_list_view_item,null);
        }

        TextView userName = (TextView)view.findViewById(R.id.likes_dialog_list_item_name);
        userName.setText(likes.get(i).getUserName());
        view.setMinimumHeight(125);

        return view;
    }

}
