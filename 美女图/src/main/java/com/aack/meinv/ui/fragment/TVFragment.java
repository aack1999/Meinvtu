package com.aack.meinv.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aack.meinv.R;
import com.aack.meinv.common.WaitProgressDialog;
import com.aack.meinv.response.DetailResponse;
import com.aack.meinv.response.Results;
import com.aack.meinv.response.Serises;
import com.aack.meinv.response.TVResponse;
import com.aack.meinv.response.VideoModel;
import com.aack.meinv.ui.activity.VideoPlayerActivity;
import com.aack.meinv.ui.widget.ListViewForScrollView;
import com.aack.meinv.utils.ParseUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.florent37.glidepalette.GlidePalette;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * 电视类型的Fragment
 * Created by root on 16-3-21.
 */
public class TVFragment extends BaseFragment {

    Results results;
    String sq;

    public static TVFragment createFragment(String sq) {
        TVFragment tvFragment = new TVFragment();
        Bundle bundle = new Bundle();
        bundle.putString("sq", sq);
        tvFragment.setArguments(bundle);
        return tvFragment;
    }

    @Bind(R.id.img)
    ImageView img;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.swiperefreshlayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.listview)
    ListViewForScrollView listview;

    ArrayAdapter<Serises> adapter;
    List<Serises> mItems;

    @Override
    protected View getLoadingTargetView() {
        return root.findViewById(R.id.content);
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_tv_layout;
    }

    @Override
    protected void onFirstUserVisible() {
        toggleShowLoading(true);
        getTvData();
    }

    @Override
    protected void initViewAndEvent() {
        sq = getArguments().getString("sq");
        mItems = new ArrayList<>();
        adapter = new ArrayAdapter<Serises>(getContext(), R.layout.fragment_tv_item, mItems) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                ViewHolder holder;
                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_tv_item, null);
                    holder = new ViewHolder();
                    holder.title = (TextView) convertView.findViewById(R.id.title);
                    convertView.setTag(holder);
                }
                holder = (ViewHolder) convertView.getTag();
                holder.title.setText(getItem(position).getTitle());
                return convertView;
            }

            class ViewHolder {
                TextView title;
            }
        };
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (StringUtils.isBlank(mItems.get(position).getVideoid())) {
                    return;
                }
                String url = "http://v.youku.com/v_show/id_" + mItems.get(position).getVideoid() + ".html";
                Intent intent = new Intent(getContext(), VideoPlayerActivity.class);
                VideoModel video = new VideoModel();
                video.setPlay_url(url);
                video.setMovieid(mItems.get(position).getVideoid());
                video.setTitle(mItems.get(position).getTitle());
                intent.putExtra("model", video);
                intent.putExtra("tag", "youku");
                startActivity(intent);
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getTvData();
            }
        });
    }

    public void getTvData() {
        final String url = "http://api.appsdk.soku.com/d/s?keyword=" + sq + "&_t_=1458553016&_s_=61211e5aba898b54a57230a629f8661a";
        new WaitProgressDialog(getContext()).startGet(false, url, null, new WaitProgressDialog.RequestCallBackImpl() {
            @Override
            public void onSuccess(String result) {
                toggleShowLoading(false);
                swipeRefreshLayout.setRefreshing(false);
                TVResponse response = ParseUtils.parseJson(result, TVResponse.class);
                if (response == null) {
                    toggleShowError(true, "接口异常", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            getTvData();
                        }
                    });
                    return;
                }

                if ("success".equals(response.getStatus())) {
                    if (response.getResults() != null && response.getResults().size() > 0) {
                        results = response.getResults().get(0);
                        if (results != null)
                            upTVDataUI();
                    } else {
                        toggleShowEmpty(true, "未搜索到数据");
                    }
                } else {
                    toggleShowError(true, "接口异常", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            getTvData();
                        }
                    });
                }

            }

            @Override
            public void onFailure(String result) {
                toggleShowError(true, null, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getTvData();
                    }
                });
            }
        });
    }

    public void getTVDetailData(){
        String url="http://api.mobile.youku.com/layout/android5_0/play/detail?pid=bb2388e929bc3038&guid=ffe45c256911c2d7c9e3a94905747065&mac=38%3Abc%3A1a%3Aa6%3Ac8%3Abe&imei=861138024623239&ver=5.4.4&_t_=1458635168&e=md5&_s_=2220714d18bebe4740b7acf3bd6378c6&network=WIFI&format=&id="+results.getShowid();
        Log.e("eeeee",url);
        new WaitProgressDialog(getContext()).startGet(false, url, null, new WaitProgressDialog.RequestCallBackImpl() {
            @Override
            public void onSuccess(String result) {
                mItems.clear();
                DetailResponse model=ParseUtils.parseJson(result,DetailResponse.class);
                if (model!=null&&"success".equals(model.getStatus())&&model.getDetail()!=null&&StringUtils.isNotBlank(model.getDetail().getVideoid())){
                    //存在数据
                    Serises serises=new Serises();
                    if (StringUtils.isNotBlank(model.getDetail().getTitle())){
                        String temp=model.getDetail().getVideo_type();
                        if (StringUtils.isNotBlank(temp)){
                            temp="("+temp+")";
                        }else {
                            temp="";
                        }
                        serises.setTitle(model.getDetail().getTitle()+temp);
                    }else {
                        serises.setTitle(results.getShowname());
                    }
                    serises.setVideoid(model.getDetail().getVideoid());
                    mItems.add(serises);
                    adapter.notifyDataSetChanged();
                }else {
                    Serises serises=new Serises();
                    serises.setTitle("获取视频数据失败");
                    mItems.add(serises);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(String result) {
                Log.e("eeeeeee", result + " ");
            }
        });
    }

    public void upTVDataUI() {
        if (StringUtils.isNotBlank(results.getShowname())) {
            title.setText(results.getShowname());
        }
        Glide.with(getActivity()).load(results.getShow_vthumburl()).listener(GlidePalette.with(results.getShow_vthumburl())
                .use(GlidePalette.Profile.MUTED_DARK)
                .intoBackground(title)
                .intoTextColor(title)).diskCacheStrategy(DiskCacheStrategy.ALL).into(img);
        mItems.clear();

        if (results.getEpisodes()!=null){
            //优酷没有资源
            Serises serises=new Serises();
            serises.setTitle("优酷没有资源,请到其他视频网站搜索");
            mItems.add(serises);
        }else if (results.getSerises() != null) {
            mItems.addAll(results.getSerises());
        }else {
            //要再掉个接口获取播放movieid
            Serises serises=new Serises();
            serises.setTitle("正在获取资源...");
            mItems.add(serises);
            getTVDetailData();
        }
        if (mItems.size() > 0) {
            adapter.notifyDataSetChanged();
        }
    }
}
