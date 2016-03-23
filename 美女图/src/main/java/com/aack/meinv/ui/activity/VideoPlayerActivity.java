package com.aack.meinv.ui.activity;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.aack.meinv.R;
import com.aack.meinv.common.WaitProgressDialog;
import com.aack.meinv.response.Serises;
import com.aack.meinv.response.VideoModel;
import com.android.tedcoder.wkvideoplayer.model.Video;
import com.android.tedcoder.wkvideoplayer.model.VideoUrl;
import com.android.tedcoder.wkvideoplayer.util.DensityUtil;
import com.android.tedcoder.wkvideoplayer.view.MediaController;
import com.android.tedcoder.wkvideoplayer.view.SuperVideoPlayer;
import com.lusfold.androidkeyvaluestore.KVStore;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;


/**
 * Created by root on 16-3-21.
 */
public class VideoPlayerActivity extends BaseActivity{

    @Bind(R.id.video_player)
    SuperVideoPlayer mSuperVideoPlayer;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.gridview)GridView gridView;
    @Bind(R.id.video_num)TextView tvnum;

    ArrayAdapter<String> adapter;
    ArrayList<Video> infoList;
    List<String> videourllist;
    VideoModel model;
    String tag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_layout);
        initData();
    }

    public void initData() {
        model = (VideoModel) getIntent().getSerializableExtra("model");
        getSupportActionBar().setTitle("视频详情");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        title.setText(model.getTitle());
        tag = getIntent().getStringExtra("tag");
        if (StringUtils.isBlank(tag)) {
            initPlay();
        } else if ("youku".equals(tag)) {
            //优酷
            getYoukuPlayPath();
        }
        mSuperVideoPlayer.setVideoPlayCallback(mVideoPlayCallback);
        adapter = new ArrayAdapter<String>(this, R.layout.fragment_tv_item,videourllist ) {
            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                ViewHolder holder;
                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_tv_item, null);
                    holder = new ViewHolder();
                    holder.title = (TextView) convertView.findViewById(R.id.title);
                    convertView.setTag(holder);
                }
                holder = (ViewHolder) convertView.getTag();
                holder.title.setGravity(Gravity.CENTER);
                holder.title.setText(position+1+"");
                if (mSuperVideoPlayer.getCurrentIndex()==position){
                    holder.title.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    holder.title.setTextColor(Color.parseColor("#ffffff"));
                }else {
                    holder.title.setTextColor(Color.parseColor("#333333"));
                    holder.title.setBackgroundColor(Color.parseColor("#cccccc"));
                }
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mSuperVideoPlayer.loadMultipleVideo(infoList,position,0,0);
                        notifyDataSetChanged();
                    }
                });
                return convertView;
            }

            class ViewHolder {
                TextView title;
            }
        };
        gridView.setAdapter(adapter);
    }

    public void initPlay() {
        ArrayList<Video> infoList = new ArrayList<Video>();
        infoList.add(getVideo(model.getPlay_url()));
        mSuperVideoPlayer.loadMultipleVideo(infoList, 0, 0, 0);
    }



    public void getYoukuPlayPath() {
        videourllist=new ArrayList<>();
        new WaitProgressDialog(this).startNetWork(true, new WaitProgressDialog.NetWorkBackImpl() {
            @Override
            public void onRequest() {
                // TODO Auto-generated method stub
                String url = "http://www.flvcd.com/parse.php?flag=&format=&kw=" + model.getPlay_url();
                Document doc;
                try {
                    doc = Jsoup.connect(url).header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.63 Safari/537.36").get();
                    Elements links = doc.select("a[href]");
                    for (Element link : links) {
                        String temp = link.attr("abs:href");
                        if (temp.contains("http://k.youku.com/player/")) {
                            videourllist.add(temp);
                            Log.e("eee", temp);
                        }
                    }
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            @Override
            public void onSuccess() {
                if (videourllist.size() > 0) {
                    infoList = new ArrayList<Video>();
                    for (int i = 0; i < videourllist.size(); i++) {
                        infoList.add(getVideo(videourllist.get(i)));
                    }
                    tvnum.setText("共" + infoList.size() + "个视频源");
                    adapter.notifyDataSetChanged();

                    int lastIndex=0;
                    int lastPlayTime=0;
                    if (StringUtils.isNotBlank(KVStore.getInstance().get(model.getMovieid()))){
                        //上次看过
                        String temp[]=KVStore.getInstance().get(model.getMovieid()).split(",");
                        lastIndex=Integer.parseInt(temp[0]);
                        lastPlayTime=Integer.parseInt(temp[1]);
                    }
                    mSuperVideoPlayer.loadMultipleVideo(infoList, lastIndex, 0, lastPlayTime);
                }
            }
        });
    }

    public Video getVideo(String url){
        Video video = new Video();
        VideoUrl videoUrl1 = new VideoUrl();
        videoUrl1.setFormatUrl(url);
        ArrayList<VideoUrl> arrayList1 = new ArrayList<>();
        arrayList1.add(videoUrl1);
        video.setVideoName(model.getTitle());
        video.setVideoUrl(arrayList1);
        return video;
    }

    /***
     * 旋转屏幕之后回调
     *
     * @param newConfig newConfig
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (null == mSuperVideoPlayer) return;
        /***
         * 根据屏幕方向重新设置播放器的大小
         */
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().getDecorView().invalidate();
            float height = DensityUtil.getWidthInPx(this);
            float width = DensityUtil.getHeightInPx(this);
            mSuperVideoPlayer.getLayoutParams().height = (int) width;
            mSuperVideoPlayer.getLayoutParams().width = (int) height;
            setThemeColor(Color.TRANSPARENT);
            getSupportActionBar().hide();
        } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            final WindowManager.LayoutParams attrs = getWindow().getAttributes();
            attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().setAttributes(attrs);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            float width = DensityUtil.getWidthInPx(this);
            float height = DensityUtil.dip2px(this, 200.f);
            mSuperVideoPlayer.getLayoutParams().height = (int) height;
            mSuperVideoPlayer.getLayoutParams().width = (int) width;
            getSupportActionBar().show();
            setThemeColor(getResources().getColor(R.color.colorPrimary));
        }
    }


    private SuperVideoPlayer.VideoPlayCallbackImpl mVideoPlayCallback = new SuperVideoPlayer.VideoPlayCallbackImpl() {
        @Override
        public void onCloseVideo() {
            mSuperVideoPlayer.close();
            resetPageToPortrait();
        }

        @Override
        public void onSwitchPageType() {
            if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                mSuperVideoPlayer.setPageType(MediaController.PageType.SHRINK);
            } else {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                mSuperVideoPlayer.setPageType(MediaController.PageType.EXPAND);
            }
        }

        @Override
        public void onPlayFinish() {
            adapter.notifyDataSetChanged();
        }
    };

    /***
     * 恢复屏幕至竖屏
     */
    private void resetPageToPortrait() {
        if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            mSuperVideoPlayer.setPageType(MediaController.PageType.SHRINK);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        int playTime=mSuperVideoPlayer.getPlayTime();
        Log.e("ee",playTime+" ");
        KVStore.getInstance().insertOrUpdate(model.getMovieid(),mSuperVideoPlayer.getCurrentIndex()+","+playTime);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
