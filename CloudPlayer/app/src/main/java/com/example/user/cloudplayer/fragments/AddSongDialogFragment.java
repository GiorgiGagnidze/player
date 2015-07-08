package com.example.user.cloudplayer.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.user.cloudplayer.R;
import com.example.user.cloudplayer.adapters.LikesDialogAdapter;
import com.example.user.cloudplayer.model.Like;

import java.util.ArrayList;


public class AddSongDialogFragment extends DialogFragment {

    String[] ar={
            "erti", "bla","yle"
    };
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog d = new Dialog(getActivity());
        d.setCanceledOnTouchOutside(false);
        d.setContentView(R.layout.add_new_song_dialog);

        AutoCompleteTextView txt= (AutoCompleteTextView)d.findViewById(R.id.add_song_text_view);
        Button add=(Button)d.findViewById(R.id.add_song_button);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSongPath("Pump");
            }
        });
        ArrayList<String> likes = new ArrayList<String>();
        for(int i = 0; i < 50; i++){
            likes.add("bla"+1);
        }
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, ar);

        txt.setAdapter(adapter);


        return d;
    }
    private String getSongPath(String songName){
        String[] all = { "*" };
        Uri allSongsUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
        String path=null;

        Cursor cursor = getActivity().getContentResolver().query(allSongsUri,all,selection,null,null);
        if (cursor != null) {
            //while (cursor.moveToNext()) {

                if (cursor.moveToFirst()) {
                    Log.i("Irakli", "shemovida");
                    path = cursor.getString(cursor
                            .getColumnIndex(MediaStore.Audio.Media.DATA));
                    String song_name = cursor
                            .getString(cursor
                                    .getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
                    Log.i("Irakli", song_name);
                    Toast.makeText(getActivity().getApplicationContext(),song_name,Toast.LENGTH_SHORT).show();
             //   }
            }

            cursor.close();
        }
        return path;
    }
    public void getAllSongsFromSDCARD() {
        String[] STAR = {"*"};
        Uri allsongsuri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Log.i("Irakli",allsongsuri.getPath());
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
        Log.i("Irakli",selection);
        Cursor cursor = getActivity().getContentResolver().query(allsongsuri, STAR, selection, null, null);

        if (cursor != null) {


            if (cursor.moveToFirst()) {
                do {
                    String song_name = cursor
                            .getString(cursor
                                    .getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));

                    int song_id = cursor.getInt(cursor
                            .getColumnIndex(MediaStore.Audio.Media._ID));

                    String fullpath = cursor.getString(cursor
                            .getColumnIndex(MediaStore.Audio.Media.DATA));


                    String album_name = cursor.getString(cursor
                            .getColumnIndex(MediaStore.Audio.Media.ALBUM));
                    int album_id = cursor.getInt(cursor
                            .getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));

                    String artist_name = cursor.getString(cursor
                            .getColumnIndex(MediaStore.Audio.Media.ARTIST));
                    int artist_id = cursor.getInt(cursor
                            .getColumnIndex(MediaStore.Audio.Media.ARTIST_ID));


                } while (cursor.moveToNext());

            }
            cursor.close();
        }
    }
}
