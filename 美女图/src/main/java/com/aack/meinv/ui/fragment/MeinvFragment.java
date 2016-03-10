package com.aack.meinv.ui.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.aack.meinv.R;
import com.aack.meinv.common.WaitProgressDialog;
import com.aack.meinv.response.Gallery;
import com.aack.meinv.response.GalleryResponse;
import com.aack.meinv.ui.adapter.MeinvAdapter;
import com.aack.meinv.ui.widget.EndlessRecyclerOnScrollListener;
import com.aack.meinv.ui.widget.HeaderAndFooterRecyclerViewAdapter;
import com.aack.meinv.ui.widget.HeaderSpanSizeLookup;
import com.aack.meinv.ui.widget.LoadingFooter;
import com.aack.meinv.ui.widget.RecyclerViewStateUtils;
import com.aack.meinv.utils.ParseUtils;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by root on 16-2-25.
 */
public class MeinvFragment extends BaseFragment {

    public static MeinvFragment createFragment(int id){
        MeinvFragment fragment=new MeinvFragment();
        Bundle bundle=new Bundle();
        bundle.putInt("id", id);
        fragment.setArguments(bundle);
        return fragment;
    }

    RecyclerView mRecyclerView;
    MeinvAdapter mAdapter;
    List<Gallery> mItems;
    SwipeRefreshLayout swipeRefreshLayout;

    private HeaderAndFooterRecyclerViewAdapter mHeaderAndFooterRecyclerViewAdapter = null;

    int id;
    int currentPage=1;
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
        getGalleryList();
    }

    @Override
    protected void initViewAndEvent() {
        id=getArguments().getInt("id");
        mItems=new ArrayList<>();
        mRecyclerView=(RecyclerView)root.findViewById(R.id.recyclerview);
        swipeRefreshLayout=(SwipeRefreshLayout)root.findViewById(R.id.refreshlayout);
        mAdapter=new MeinvAdapter(getContext(),mItems);
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
                currentPage=1;
                getGalleryList();
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
                getGalleryList();
            }
        }
    };

    public void getGalleryList(){
        final String url="http://www.tngou.net/tnfs/api/list";
        final Map<String,String> map=new HashMap<>();
        map.put("page",currentPage+"");
        map.put("id",id+"");
        new WaitProgressDialog(getContext()).startGet(false, url, map, new WaitProgressDialog.RequestCallBackImpl() {
            @Override
            public void onSuccess(String result) {
                toggleShowLoading(false);
                swipeRefreshLayout.setRefreshing(false);
                RecyclerViewStateUtils.setFooterViewState(mRecyclerView, LoadingFooter.State.Normal);
                GalleryResponse response= ParseUtils.parseJson(result,GalleryResponse.class);
                if (response==null){
                    toggleShowError(true,"接口异常");
                    return;
                }

                if (response.isStatus()&& StringUtils.isBlank(response.getMsg())){
                    if (response.getTngou()!=null){
                        if (currentPage==1){
                            mItems.clear();
                        }
                        totalPage=response.getTotal();
                        mItems.addAll(response.getTngou());
                        currentPage++;
                        mAdapter.notifyDataSetChanged();
                    }else {
                        if (mItems.size()<=0){
                        toggleShowEmpty(true, "");
                        }
                    }
                }else {
                    toggleShowError(true,"接口异常");
                }

            }

            @Override
            public void onFailure(String result) {
                toggleShowError(true, null);
            }
        });
    }
}
