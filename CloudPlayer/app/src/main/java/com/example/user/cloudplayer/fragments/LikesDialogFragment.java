package com.example.user.cloudplayer.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.ListView;
import android.widget.Toast;

import com.example.user.cloudplayer.App;
import com.example.user.cloudplayer.R;
import com.example.user.cloudplayer.adapters.LikesDialogAdapter;
import com.example.user.cloudplayer.model.Comment;
import com.example.user.cloudplayer.model.Like;
import com.example.user.cloudplayer.model.PlayList;
import com.example.user.cloudplayer.model.Song;
import com.example.user.cloudplayer.transport.NetworkEventListener;

import java.util.ArrayList;


public class LikesDialogFragment extends DialogFragment implements NetworkEventListener{

    private String playListID;
    private ListView list;
    private App app;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog d = new Dialog(getActivity());
        d.setCanceledOnTouchOutside(false);
        d.setContentView(R.layout.likes_dialog);
        list = (ListView)d.findViewById(R.id.dialog_likes_list_view);
        playListID = "1";// merea shesacvleliii
        app.addListener(this);
        App.getCloudStorage().getLikes(playListID);
        return d;
    }

    public void setApp(App app){
        this.app = app;
    }


    @Override
    public void onPlayListAdded(PlayList playList) {
        sendToast("PlayList could not be added");
    }

    @Override
    public void onPlayListDeleted(PlayList playList) {
        sendToast("PlayList could not be deleted");
    }

    @Override
    public void onUsersPlayListsDownloaded(ArrayList<PlayList> playLists) {
        sendToast("PlayLists could not be downloaded");
    }

    @Override
    public void onCommentsDownloaded(ArrayList<Comment> comments) {
        sendToast("Comments could not be downloaded");
    }

    @Override
    public void onLikesDownloaded(ArrayList<Like> likes) {
        if(likes == null){
            sendToast("Authors of likes could not be downloaded");
        } else {
            LikesDialogAdapter adapter = new LikesDialogAdapter(getActivity(),likes);
            list.setAdapter(adapter);
        }
    }

    @Override
    public void onCommentAdded(Comment comment) {
        sendToast("Comment could not be posted");
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

    private void sendToast(CharSequence text){
        Context context = getActivity();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        app.removeListener(this);
    }

}
