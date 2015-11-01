package com.example.user.cloudplayer.adapters;


import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.user.cloudplayer.App;
import com.example.user.cloudplayer.R;
import com.example.user.cloudplayer.model.PlayList;

import java.util.ArrayList;

public class ProfileRecyclerAdapter extends RecyclerView.Adapter<ProfileRecyclerAdapter.Holder>{
    private ArrayList<PlayList> playLists;
    private Context context;
    private View.OnClickListener onClickListener;

    public ProfileRecyclerAdapter(Context context, ArrayList<PlayList> playLists,
                   View.OnClickListener onClickListener) {
        this.context = context;
        this.playLists = playLists;
        this.onClickListener = onClickListener;
    }

    public void updateListView(ArrayList<PlayList> playLists){
        this.playLists = playLists;
        this.notifyDataSetChanged();
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.profile_recycler_list_item
                , viewGroup, false);
        view.setOnClickListener(onClickListener);

        Holder holder = new Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(Holder holder,final int i) {
        holder.title.setText(playLists.get(i).getName());
        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                App.getCloudStorage().deletePlayList(playLists.get(i));
            }
        });
    }

    @Override
    public int getItemCount() {
        return playLists.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        CardView cardItemLayout;
        TextView title;
        ImageButton imageButton;

        public Holder(View itemView) {
            super(itemView);

            cardItemLayout = (CardView) itemView.findViewById(R.id.profile_list_item);
            title = (TextView) itemView.findViewById(R.id.listitem_name);
            imageButton = (ImageButton) itemView.findViewById(R.id.listitem_delete_button);
        }
    }
}
