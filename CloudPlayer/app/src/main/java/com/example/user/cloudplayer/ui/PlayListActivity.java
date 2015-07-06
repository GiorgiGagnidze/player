package com.example.user.cloudplayer.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.user.cloudplayer.R;
import com.example.user.cloudplayer.adapters.PlaylistActivityAdapter;
import com.example.user.cloudplayer.model.PlayList;
import com.example.user.cloudplayer.transport.NetworkEventListener;

import java.util.ArrayList;


public class PlayListActivity extends Activity implements NetworkEventListener{

    private ListView list;
    private PlaylistActivityAdapter adapter;
    private Button addButton;
    private Button deleteButton;
    private EditText edit;
    private int clickedPos;
    private ArrayList<PlayList> currentPlayLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlists);

        addButton = (Button)findViewById(R.id.activity_playlists_button_add);
        deleteButton = (Button)findViewById(R.id.activity_playlists_button_delete);
        edit = (EditText)findViewById(R.id.activity_playlists_edit_text);

        list = (ListView)findViewById(R.id.activity_playlists_list_view);
        ArrayList<PlayList> playLists = new ArrayList<PlayList>();
        currentPlayLists = playLists;
        for(int i = 0; i < 50; i++){
            playLists.add(new PlayList("droebiti saxeliii",null,null,i));
        }
        adapter = new PlaylistActivityAdapter(this,playLists);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                clickedPos = position;
                edit.setText(currentPlayLists.get(clickedPos).getName());
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if(edit.getText().length() > 0){
                    String editText = edit.getText().toString();
                    edit.setText("");
                    clickedPos = -1;
                }
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if(clickedPos != -1){
                    edit.setText("");
                    clickedPos = -1;
                }
            }
        });

    }

}
