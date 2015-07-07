package com.example.user.cloudplayer.adapters;


import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.user.cloudplayer.R;
import com.example.user.cloudplayer.model.Comment;

import java.util.ArrayList;

public class CommentsActivityAdapter extends BaseAdapter {

    private Activity context;
    private ArrayList<Comment> comments;

    public CommentsActivityAdapter(Activity context,ArrayList<Comment> comments){
        this.context = context;
        this.comments = comments;
    }

    public void updateListView(ArrayList<Comment> comments){
        this.comments = comments;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return comments.size();
    }

    @Override
    public Object getItem(int i) {
        return comments.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null){ // inflates only when it is necessary.it is correct style of writing such as code.
            view = View.inflate(context, R.layout.activity_comments_list_view_item,null);
        }

        Comment currentComment = comments.get(i);

        TextView userName = (TextView)view.findViewById(R.id.activity_comments_user_name);
        TextView comment = (TextView)view.findViewById(R.id.activity_comments_text);

        userName.setText(currentComment.getUserName() + ":");
        comment.setText(currentComment.getText());

        view.setMinimumHeight(125);

        return view;
    }
}
