package com.aack.meinv.ui.activity;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;

import com.aack.meinv.R;
import com.aack.meinv.common.WaitProgressDialog;
import com.aack.meinv.response.VideoModel;
import com.android.tedcoder.wkvideoplayer.model.Video;
import com.android.tedcoder.wkvideoplayer.model.VideoUrl;
import com.android.tedcoder.wkvideoplayer.util.DensityUtil;
import com.android.tedcoder.wkvideoplayer.view.MediaController;
import com.android.tedcoder.wkvideoplayer.view.SuperVideoPlayer;

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
        title.setText(model.getTitle());
        tag = getIntent().getStringExtra("tag");
        if (StringUtils.isBlank(tag)) {
            initPlay();
        } else if ("youku".equals(tag)) {
            //优酷
            getYoukuPlayPath();
        }
        mSuperVideoPlayer.setVideoPlayCallback(mVideoPlayCallback);
    }

    public void initPlay() {
        ArrayList<Video> infoList = new ArrayList<Video>();
        infoList.add(getVideo(model.getPlay_url()));
        mSuperVideoPlayer.loadMultipleVideo(infoList, 0, 0, 0);
    }

    List<String> videourllist;

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
                    Elements media = doc.select("[src]");
                    Elements imports = doc.select("link[href]");
                    for (Element link : links) {
                        String temp=link.attr("abs:href");
                        if (temp.contains("http://k.youku.com/player/")){
                            videourllist.add(temp);
                            Log.e("eee",temp);
                        }
                    }
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            @Override
            public void onSuccess() {
                if (videourllist.size()>0){
                    ArrayList<Video> infoList = new ArrayList<Video>();
                    showToast("发现" + videourllist.size() + "个视频");
                    for (int i = 0; i < videourllist.size(); i++) {
                        infoList.add(getVideo(videourllist.get(i)));
                    }
                    mSuperVideoPlayer.loadMultipleVideo(infoList, 0, 0, 0);
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

}
