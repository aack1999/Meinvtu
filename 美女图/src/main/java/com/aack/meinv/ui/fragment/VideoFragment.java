package com.aack.meinv.ui.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.aack.meinv.R;
import com.aack.meinv.common.WaitProgressDialog;
import com.aack.meinv.response.VideoModel;
import com.aack.meinv.response.VideoResponse;
import com.aack.meinv.ui.adapter.VideoAdapter;
import com.aack.meinv.ui.widget.EndlessRecyclerOnScrollListener;
import com.aack.meinv.ui.widget.HeaderAndFooterRecyclerViewAdapter;
import com.aack.meinv.ui.widget.HeaderSpanSizeLookup;
import com.aack.meinv.ui.widget.LoadingFooter;
import com.aack.meinv.ui.widget.RecyclerViewStateUtils;
import com.aack.meinv.utils.ParseUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by root on 16-3-20.
 */
public class VideoFragment extends BaseFragment {

    public static VideoFragment createFragment(int type){
        VideoFragment fragment=new VideoFragment();
        Bundle bundle=new Bundle();
        bundle.putInt("type", type);
        fragment.setArguments(bundle);
        return fragment;
    }

    RecyclerView mRecyclerView;
    VideoAdapter mAdapter;
    List<VideoModel> mItems;
    SwipeRefreshLayout swipeRefreshLayout;
    private HeaderAndFooterRecyclerViewAdapter mHeaderAndFooterRecyclerViewAdapter = null;

    int type;
    int currentPage=0;
    int totalPage=1;

    @Override
    protected View getLoadingTargetView() {
        return root.findViewById(R.id.recyclerview);
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_meinv_layout;
    }

    @Override
    protected void onFirstUserVisible() {
        toggleShowLoading(true);
        getVideoList();
    }

    @Override
    protected void initViewAndEvent() {
        type=getArguments().getInt("type");
        mItems=new ArrayList<>();
        mRecyclerView=(RecyclerView)root.findViewById(R.id.recyclerview);
        swipeRefreshLayout=(SwipeRefreshLayout)root.findViewById(R.id.refreshlayout);
        mAdapter=new VideoAdapter(getContext(),mItems);
        mHeaderAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(mAdapter);
        mRecyclerView.setAdapter(mHeaderAndFooterRecyclerViewAdapter);
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 2);
        manager.setSpanSizeLookup(new HeaderSpanSizeLookup((HeaderAndFooterRecyclerViewAdapter) mRecyclerView.getAdapter(), manager.getSpanCount()));
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.addOnScrollListener(mOnScrollListener);
        swipeRefreshLayout.setColorSchemeColors(R.color.colorAccent);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                currentPage=0;
                getVideoList();
            }
        });
    }

    private EndlessRecyclerOnScrollListener mOnScrollListener = new EndlessRecyclerOnScrollListener() {

        @Override
        public void onLoadNextPage(View view) {
            super.onLoadNextPage(view);

            LoadingFooter.State state = RecyclerViewStateUtils.getFooterViewState(mRecyclerView);
            if(state == LoadingFooter.State.Loading||state== LoadingFooter.State.TheEnd) {
                return;
            }

            if (mItems.size()>=totalPage){
                RecyclerViewStateUtils.setFooterViewState(mRecyclerView, LoadingFooter.State.TheEnd);
            }else {
                // loading more
                RecyclerViewStateUtils.setFooterViewState(getActivity(), mRecyclerView, 20, LoadingFooter.State.Loading, null);
                getVideoList();
            }
        }
    };

    public void getVideoList(){
        final String url="http://interface.m.sjzhushou.com/hotresource/list";
        final Map<String,String> map=new HashMap<>();
        map.put("type",type+"");
        map.put("length",20+"");
        map.put("offset",currentPage+"");
        map.put("time",System.currentTimeMillis()+"");
        new WaitProgressDialog(getContext()).startGet(false, url, map, new WaitProgressDialog.RequestCallBackImpl() {
            @Override
            public void onSuccess(String result) {
                toggleShowLoading(false);
                swipeRefreshLayout.setRefreshing(false);
                RecyclerViewStateUtils.setFooterViewState(mRecyclerView, LoadingFooter.State.Normal);
                VideoResponse response= ParseUtils.parseJson(result, VideoResponse.class);
                if (response==null){
                    toggleShowError(true, "接口异常", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            getVideoList();
                        }
                    });
                    return;
                }

                if ("ok".equals(response.getResult())){
                    if (response.getVideo_list()!=null){
                        if (currentPage==0){
                            mItems.clear();
                        }
                        if (response.getVideo_list().size()<=0){
                            totalPage=mItems.size();
                        }else {
                            totalPage=1000;
                        }
                        mItems.addAll(response.getVideo_list());
                        currentPage=mItems.size();
                        mAdapter.notifyDataSetChanged();
                    }else {
                        if (mItems.size()<=0){
                            toggleShowEmpty(true, "");
                        }
                    }
                }else {
                    toggleShowError(true, "接口异常", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            getVideoList();
                        }
                    });
                }

            }

            @Override
            public void onFailure(String result) {
                toggleShowError(true, null, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getVideoList();
                    }
                });
            }
        });
    }
}
