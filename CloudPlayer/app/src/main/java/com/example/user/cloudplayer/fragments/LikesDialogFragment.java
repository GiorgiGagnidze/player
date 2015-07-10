package com.example.user.cloudplayer.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
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
    private ArrayList<Like> currentLikes;
    private LikesDialogAdapter adapter;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog d = new Dialog(getActivity());
        d.setCanceledOnTouchOutside(true);
        d.setContentView(R.layout.likes_dialog);
        list = (ListView)d.findViewById(R.id.dialog_likes_list_view);
        Bundle mArgs = getArguments();
        playListID = mArgs.getString(getResources().getString(R.string.key_playlistID));
        app = (App)getActivity().getApplication();
        app.addListener(this);
        if(savedInstanceState == null){
            currentLikes = new ArrayList<Like>();
        }
        App.getCloudStorage().getLikes(playListID);
        return d;
    }

    @Override
    public void onPlayListAdded(PlayList playList) {
    }

    @Override
    public void onPlayListDeleted(PlayList playList) {
        if(playList != null && playList.getID().equals(playListID)){
            this.dismiss();
        }
    }

    @Override
    public void onUsersPlayListsDownloaded(ArrayList<PlayList> playLists) {
    }

    @Override
    public void onCommentsDownloaded(ArrayList<Comment> comments) {
    }

    @Override
    public void onLikesDownloaded(ArrayList<Like> likes) {
        if(likes != null){
            adapter = new LikesDialogAdapter(getActivity(),likes);
            list.setAdapter(adapter);
        }
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
        if(like != null && like.getPlayListID().equals(playListID)){
            currentLikes.add(like);
            adapter.updateListView(currentLikes);
        }
    }

    @Override
    public void onHasLiked(Boolean bool) {
    }

    private int checkLike(String playListID){
        for(int i = 0; i < currentLikes.size(); i++){
            if(currentLikes.get(i).getPlayListID().equals(playListID)){
                return i;
            }
        }
        return -1;
    }

    @Override
    public void onUnLiked(Like like) {
        if(like != null && like.getPlayListID().equals(playListID)){
            int index = checkLike(like.getPlayListID());
            if(index != -1) {
                currentLikes.remove(index);
                adapter.updateListView(currentLikes);
            }
        }
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
