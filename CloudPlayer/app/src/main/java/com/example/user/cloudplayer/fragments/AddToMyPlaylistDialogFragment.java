package com.example.user.cloudplayer.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.user.cloudplayer.App;
import com.example.user.cloudplayer.R;
import com.example.user.cloudplayer.adapters.AddToPlaylistAdapter;
import com.example.user.cloudplayer.adapters.LikesDialogAdapter;
import com.example.user.cloudplayer.adapters.ProfileActivityAdapter;
import com.example.user.cloudplayer.model.Comment;
import com.example.user.cloudplayer.model.Like;
import com.example.user.cloudplayer.model.PlayList;
import com.example.user.cloudplayer.model.Song;
import com.example.user.cloudplayer.transport.NetworkEventListener;

import java.util.ArrayList;

/**
 * Created by Irakli on 7/10/2015.
 */
public class AddToMyPlaylistDialogFragment extends DialogFragment implements NetworkEventListener {
    private Song song;
    private String playListID;
    private ListView list;
    private App app;
    private ArrayList<PlayList> playlist;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog d = new Dialog(getActivity());
        d.setCanceledOnTouchOutside(true);
        d.setContentView(R.layout.likes_dialog);
        list = (ListView)d.findViewById(R.id.dialog_likes_list_view);
        Bundle mArgs = getArguments();
        song=(Song)mArgs.get(getResources().getString(R.string.key_song));
        app = (App)getActivity().getApplication();
        app.addListener(this);
        if(savedInstanceState == null){
            playlist = new ArrayList<PlayList>();
        }
        App.getCloudStorage().downloadUsersPlaylists();
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("Irakli","Shemovida");
                Log.i("Irakli",song.getName());
                Log.i("Irakli",playlist.get(position).getID());
                App.getCloudStorage().addSongFromOtherUser(song,playlist.get(position).getID());
            }
        });
        return d;
    }

    @Override
    public void onPlayListAdded(PlayList playList) {
    }

    @Override
    public void onPlayListDeleted(PlayList playList) {

    }

    @Override
    public void onUsersPlayListsDownloaded(ArrayList<PlayList> playLists) {
        playlist=playLists;
        AddToPlaylistAdapter ad=new AddToPlaylistAdapter(getActivity(),playLists);
        list.setAdapter(ad);
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
        if(song==null){

        }else{
            dismiss();
        }
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

    @Override
    public void onDestroy(){
        super.onDestroy();
        app.removeListener(this);
    }

}

