package com.aack.meinv.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.aack.meinv.R;
import com.aack.meinv.ui.widget.SmoothImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

/**
 * Author: liuk
 * Created at: 15/12/15
 */
public class GalleryAdapter extends PagerAdapter {

    private PopupWindow window;
    private String[] urls;
    private int locationW, locationH, locationX, locationY;
    private Context mContext;
    private boolean isFirstEnter=true;

    public GalleryAdapter(Context context,PopupWindow window, String[] urls, int w, int h, int x, int y) {
        this.window = window;
        mContext=context;
        this.urls = urls;
        this.locationH = h;
        this.locationW = w;
        this.locationX = x;
        this.locationY = y;
    }

    @Override
    public int getCount() {
        return urls.length;
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        SmoothImageView smoothImageView = (SmoothImageView) LayoutInflater.from(mContext).inflate(R.layout.pager_item, null);
        container.addView(smoothImageView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        smoothImageView.setOriginalInfo(locationW, locationH, locationX, locationY);
        if (isFirstEnter){
            isFirstEnter=false;
            smoothImageView.transformIn();
        }else {
            smoothImageView.setStateNormal();
        }

        Glide.with(mContext)
                .load(urls[position])
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .priority(Priority.IMMEDIATE)
                .into(smoothImageView);

        smoothImageView.setOnTransformListener(new SmoothImageView.TransformListener() {
            @Override
            public void onTransformComplete(int mode) {
                if (mode == 2) {
                    window.dismiss();
                }
            }
        });

        smoothImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((SmoothImageView) v).transformOut();
            }
        });
        return smoothImageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

}
