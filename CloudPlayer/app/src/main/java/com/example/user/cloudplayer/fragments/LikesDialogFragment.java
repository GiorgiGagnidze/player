package com.example.user.cloudplayer.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.ListView;

import com.example.user.cloudplayer.R;
import com.example.user.cloudplayer.adapters.LikesDialogAdapter;
import com.example.user.cloudplayer.model.Like;

import java.util.ArrayList;


public class LikesDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog d = new Dialog(getActivity());
        d.setCanceledOnTouchOutside(false);
        d.setContentView(R.layout.likes_dialog);
        ListView list = (ListView)d.findViewById(R.id.dialog_likes_list_view);
        ArrayList<Like> likes = new ArrayList<Like>();
        for(int i = 0; i < 50; i++){
            likes.add(new Like());
        }
        LikesDialogAdapter adapter = new LikesDialogAdapter(getActivity(),likes);
        list.setAdapter(adapter);
        return d;
    }


}
