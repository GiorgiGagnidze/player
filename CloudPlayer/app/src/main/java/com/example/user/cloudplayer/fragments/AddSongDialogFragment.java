package com.example.user.cloudplayer.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.example.user.cloudplayer.App;
import com.example.user.cloudplayer.R;
import com.example.user.cloudplayer.model.Comment;
import com.example.user.cloudplayer.model.Like;
import com.example.user.cloudplayer.model.PlayList;
import com.example.user.cloudplayer.model.Song;
import com.example.user.cloudplayer.transport.NetworkEventListener;

import java.util.ArrayList;
import java.util.HashMap;


public class AddSongDialogFragment extends DialogFragment {
    private AutoCompleteTextView txt;
    private String playListID;
    private Dialog d;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        d = new Dialog(getActivity());
        d.setCanceledOnTouchOutside(true);
        d.setContentView(R.layout.add_new_song_dialog);
        txt= (AutoCompleteTextView)d.findViewById(R.id.add_song_text_view);
        Bundle mArgs = getArguments();
        playListID = mArgs.getString(getResources().getString(R.string.key_playlistID));
        Button add=(Button)d.findViewById(R.id.add_song_button);
        final HashMap<String ,String>  ar=getSongNames();
        String[] songNames=ar.keySet().toArray(new String[ar.size()]);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String key=txt.getText().toString();
                String val=ar.get(key);
                if(val!=null){
                    App.getCloudStorage().addSong(val,playListID,key);
                    d.dismiss();
                }
                else Toast.makeText(getActivity().getApplicationContext(),getResources().getString(
                        R.string.enter_correct_song),Toast.LENGTH_SHORT).show();
            }
        });
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, songNames);
        txt.setAdapter(adapter);
        return d;
    }
    private  HashMap<String ,String>  getSongNames(){
        String[] all = { "*" };
        Uri allSongsUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
        HashMap<String ,String> songs=new HashMap<String, String>();
        Cursor cursor = getActivity().getContentResolver().query(allSongsUri,all,selection,null,null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                   String path = cursor.getString(cursor
                            .getColumnIndex(MediaStore.Audio.Media.DATA));
                    String song_name = cursor
                            .getString(cursor
                                    .getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
                    songs.put(song_name,path);
            }
            cursor.close();
        }
        return songs;
    }

}
