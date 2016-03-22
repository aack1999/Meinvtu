package com.aack.meinv.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aack.meinv.R;
import com.aack.meinv.response.VideoModel;
import com.aack.meinv.ui.activity.VideoPlayerActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.florent37.glidepalette.GlidePalette;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by root on 16-2-25.
 */
public class VideoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<VideoModel> mItems;
    Context mContext;

    public VideoAdapter(Context context,List<VideoModel> list) {
        mItems=list;
        mContext=context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(mContext).inflate(R.layout.fragment_meivn_item,parent,false);
        MyViewHolder holder=new MyViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final VideoModel model=mItems.get(position);
        MyViewHolder viewHolder=(MyViewHolder)holder;
        viewHolder.title.setText(model.getTitle());
        String url=model.getPoster();
        Glide.with(mContext).load(url).listener(GlidePalette.with(url)
                .use(GlidePalette.Profile.MUTED_DARK)
                .intoBackground(((MyViewHolder) holder).title)
                .intoTextColor(((MyViewHolder) holder).title)).diskCacheStrategy(DiskCacheStrategy.ALL).into(viewHolder.img);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext, VideoPlayerActivity.class);
                intent.putExtra("model",model);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        @Bind(R.id.img)
        ImageView img;
        @Bind(R.id.title)
        TextView title;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

}
