package com.aack.meinv.ui.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.aack.meinv.R;
import com.aack.meinv.response.Picture;
import com.aack.meinv.ui.activity.GalleryAdapter;
import com.aack.meinv.ui.adapter.ViewAdapter;
import com.aack.meinv.utils.Utils;
import com.bm.library.PhotoView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.nineoldandroids.animation.ObjectAnimator;

import java.util.ArrayList;
import java.util.List;



/**
 * Created by root on 16-3-9.
 */
public class ImagePopupView extends PopupWindow {

    ViewPager mViewPager;
    List<View> list;
    List<Picture> imgs=null;
    int location[]=new int[4];

    public ImagePopupView(Context context,List<Picture> imgs,int location[]) {
        super(context);
        this.imgs=imgs;
        this.location=location;
        View v= LayoutInflater.from(context).inflate(R.layout.img_popup_layout,null);
        setContentView(v);
        setWidth(Utils.getScreenWidth(context));
        setHeight(Utils.getScreenHeight(context));
        setFocusable(true);
        setOutsideTouchable(false);
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        init();

        v.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK){
                    ((SmoothImageView) list.get(mViewPager.getCurrentItem())).transformOut();
                }
                return false;
            }
        });
    }

    public void init(){
        list=new ArrayList<>();
        String urls[]=new String [imgs.size()];
        for (int i = 0; i <imgs.size() ; i++) {
            urls[i]="http://tnfs.tngou.net/img"+imgs.get(i).getSrc();
        }
        mViewPager=(ViewPager)getContentView().findViewById(R.id.viewpager);
        GalleryAdapter adapter=new GalleryAdapter(mViewPager.getContext(),this,urls,location[0],location[1],location[2],location[3]);
        mViewPager.setAdapter(adapter);
    }




}
