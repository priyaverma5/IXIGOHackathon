package com.iappstreat.ixigohackathon.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.iappstreat.ixigohackathon.AppController;
import com.iappstreat.ixigohackathon.R;
import com.iappstreat.ixigohackathon.models.VisitsDetails;

import java.util.List;

/**
 * Created by verma on 08-04-2017.
 */

public class HotelsAdapter  extends RecyclerView.Adapter<HotelsAdapter.MyViewHolder> {

    private Context mContext;
    private List<VisitsDetails> albumList;
    ImageLoader imageLoader ;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, count;
        public ImageView thumbnail, overflow;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            count = (TextView) view.findViewById(R.id.count);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
        }
    }


    public HotelsAdapter(Context mContext, List<VisitsDetails> albumList) {
        this.mContext = mContext;
        this.albumList = albumList;
        imageLoader = AppController.getInstance().getImageLoader();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        VisitsDetails visit = albumList.get(position);
        holder.title.setText(visit.getName());
        holder.count.setText(visit.getShortDescription());
        imageLoader.get(visit.getKeyImageUrl(), ImageLoader.getImageListener(
                holder.thumbnail, R.drawable.loading, R.drawable.loading));
       /* // loading album cover using Glide library
        Glide.with(mContext).load(album.getThumbnail()).into(holder.thumbnail);*/

    }





    @Override
    public int getItemCount() {
        return albumList.size();
    }

}
