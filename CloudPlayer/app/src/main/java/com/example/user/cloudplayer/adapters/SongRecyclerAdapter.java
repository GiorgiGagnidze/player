package com.example.user.cloudplayer.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.cloudplayer.R;
import com.example.user.cloudplayer.model.Song;

import java.util.ArrayList;


public class SongRecyclerAdapter extends RecyclerView.Adapter<SongRecyclerAdapter.Holder>{
    private ArrayList<Song> songs;
    private Context context;
    private View.OnClickListener onClickListener;

    public SongRecyclerAdapter(Context context, ArrayList<Song> songs,
                               View.OnClickListener onClickListener) {
        this.context = context;
        this.songs = songs;
        this.onClickListener = onClickListener;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerlist_item, viewGroup, false);
        view.setOnClickListener(onClickListener);
        Holder holder = new Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(Holder holder, int i) {
        holder.title.setText(songs.get(i).getName());
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        CardView cardItemLayout;
        TextView title;


        public Holder(View itemView) {
            super(itemView);

            cardItemLayout = (CardView) itemView.findViewById(R.id.cardlist_item);
            title = (TextView) itemView.findViewById(R.id.listitem_name);

        }
    }
}
