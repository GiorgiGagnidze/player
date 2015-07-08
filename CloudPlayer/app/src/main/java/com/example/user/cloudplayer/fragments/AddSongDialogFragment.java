package com.example.user.cloudplayer.fragments;

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
import com.example.user.cloudplayer.App;
import com.example.user.cloudplayer.R;
import com.example.user.cloudplayer.model.Comment;
import com.example.user.cloudplayer.model.Like;
import com.example.user.cloudplayer.model.PlayList;
import com.example.user.cloudplayer.model.Song;
import com.example.user.cloudplayer.transport.NetworkEventListener;

import java.util.ArrayList;
import java.util.HashMap;


public class AddSongDialogFragment extends DialogFragment implements NetworkEventListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog d = new Dialog(getActivity());
        d.setCanceledOnTouchOutside(true);
        d.setContentView(R.layout.add_new_song_dialog);
        final AutoCompleteTextView txt= (AutoCompleteTextView)d.findViewById(R.id.add_song_text_view);
        Bundle mArgs = getArguments();
        final String playListID = mArgs.getString(getResources().getString(R.string.key_playlistID));
        Button add=(Button)d.findViewById(R.id.add_song_button);
        final HashMap<String ,String>  ar=getSongNames();
        String[] songNames=ar.keySet().toArray(new String[ar.size()]);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                App.getCloudStorage().addSong(ar.get(txt.getText().toString()),playListID,txt.getText().toString());
                txt.setText("");
                d.dismiss();
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


    @Override
    public void onPlayListAdded(PlayList playList) {

    }

    @Override
    public void onPlayListDeleted(PlayList playList) {

    }

    @Override
    public void onUsersPlayListsDownloaded(ArrayList<PlayList> playLists) {

    }

    @Override
    public void onCommentsDownloaded(ArrayList<Comment> comments) {

    }

    @Override
    public void onLikesDownloaded(ArrayList<Like> likes) {

    }

    @Override
    public void onCommentAdded(Comment comment) {

    }

    @Override
    public void onSearchResultDownloaded(ArrayList<PlayList> playLists) {

    }

    @Override
    public void onTopTenDownloaded(ArrayList<PlayList> playLists) {

    }

    @Override
    public void onSongAdded(Song song) {

    }

    @Override
    public void onSongsDownloaded(ArrayList<Song> songs) {

    }

    @Override
    public void onLiked(Like like) {

    }

    @Override
    public void onHasLiked(Boolean bool) {

    }

    @Override
    public void onUnLiked(Like like) {

    }

    @Override
    public void onSongDeleted(Song song) {

    }
}
