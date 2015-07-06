package com.example.user.cloudplayer.ui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.example.user.cloudplayer.R;
import com.example.user.cloudplayer.adapters.CommentsActivityAdapter;
import com.example.user.cloudplayer.model.Comment;
import com.example.user.cloudplayer.transport.NetworkEventListener;

import java.util.ArrayList;

// playlist id
public class CommentsActivity extends Activity implements NetworkEventListener {

    private ListView list;
    private CommentsActivityAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        list = (ListView)findViewById(R.id.activity_comments_list_view);
        ArrayList<Comment> comments = new ArrayList<Comment>();
        for(int i = 0; i < 50; i++){
            comments.add(new Comment());
        }
        adapter = new CommentsActivityAdapter(this,comments);
        list.setAdapter(adapter);

    }


}
