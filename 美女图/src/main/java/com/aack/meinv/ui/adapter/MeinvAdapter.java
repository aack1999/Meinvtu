package com.aack.meinv.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aack.meinv.R;
import com.aack.meinv.common.WaitProgressDialog;
import com.aack.meinv.response.Gallery;
import com.aack.meinv.ui.widget.ImagePopupView;
import com.aack.meinv.ui.widget.SmoothImageView;
import com.aack.meinv.utils.DialogUtils;
import com.aack.meinv.utils.ParseUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.florent37.glidepalette.GlidePalette;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by root on 16-2-25.
 */
public class MeinvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<Gallery> mItems;
    Context mContext;

    public MeinvAdapter(Context context,List<Gallery> list) {
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
        Gallery model=mItems.get(position);
        MyViewHolder viewHolder=(MyViewHolder)holder;
        viewHolder.title.setText(model.getTitle());
        String url="http://tnfs.tngou.net/img"+model.getImg();
        Glide.with(mContext).load(url).listener(GlidePalette.with(url)
                .use(GlidePalette.Profile.MUTED_DARK)
                .intoBackground(((MyViewHolder) holder).title)
                .intoTextColor(((MyViewHolder) holder).title)).diskCacheStrategy(DiskCacheStrategy.ALL).into(viewHolder.img);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImageList(v,position);
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

    public void getImageList(final View v,final int position){
        final String url="http://www.tngou.net/tnfs/api/show";
        final Map<String,String> map=new HashMap<>();
        map.put("id",mItems.get(position).getId()+"");
        new WaitProgressDialog(mContext).startGet(false, url, map, new WaitProgressDialog.RequestCallBackImpl() {
            @Override
            public void onSuccess(String result) {
                Gallery gallery=ParseUtils.parseJson(result,Gallery.class);
                if (gallery==null){
                    return;
                }
                if (gallery.isStatus()&&gallery.getList()!=null&&gallery.getList().size()>0){
                    int[] locationXY = new int[2];
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        Rect frame = new Rect();
                        ((Activity)mContext).getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
                        int statusBarHeight = frame.top;
                        v.getLocationOnScreen(locationXY);
                        locationXY[1] += statusBarHeight;
                    } else {
                        v.getLocationOnScreen(locationXY);
                    }
                    int location[] =new int[4];
                    location[0]=v.getWidth();
                    location[1]=v.getHeight();
                    location[2]=locationXY[0];
                    location[3]=locationXY[1];
                    ImagePopupView view=new ImagePopupView(v.getContext(),gallery.getList(),location);
                    view.showAtLocation(v, Gravity.LEFT|Gravity.TOP,0,0);
                }
            }

            @Override
            public void onFailure(String result) {
                DialogUtils.showMessage(mContext,"提示",result);
            }
        });
    }

}
