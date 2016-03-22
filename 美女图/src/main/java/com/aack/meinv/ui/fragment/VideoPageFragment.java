package com.aack.meinv.ui.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.aack.meinv.R;
import com.aack.meinv.common.WaitProgressDialog;
import com.aack.meinv.response.Results;
import com.aack.meinv.response.TVResponse;
import com.aack.meinv.response.TabListResponse;
import com.aack.meinv.utils.DialogUtils;
import com.aack.meinv.utils.ParseUtils;
import com.afollestad.materialdialogs.MaterialDialog;
import com.lusfold.androidkeyvaluestore.KVStore;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by root on 16-3-20.
 */
public class VideoPageFragment extends BaseFragment {

    @Bind(R.id.tablayout)
    TabLayout mTabLayout;
    @Bind(R.id.viewpager)
    ViewPager mViewPager;

    FragmentPagerAdapter mAdapter;
    List<Fragment> mFragments;
    List<String> tabTitles;

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_video_content;
    }

    @Override
    protected void onFirstUserVisible() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.action_add){
            new MaterialDialog.Builder(getContext())
                    .title("添加")
                    .inputType(InputType.TYPE_CLASS_TEXT)
                    .input("请输入要视频关键字", "", new MaterialDialog.InputCallback() {
                        @Override
                        public void onInput(MaterialDialog dialog, CharSequence input) {
                            if (StringUtils.isNotBlank(input.toString().trim())){
                                addVideoTitle(input.toString().trim());
                            }
                        }
                    }).show();
        }
        return true;
    }

    public void addVideoTitle(String title){
        getTvData(title);
    }

    public void getTvData(String sq){
        final String url="http://api.appsdk.soku.com/d/s?keyword="+sq+"&_t_=1458553016&_s_=61211e5aba898b54a57230a629f8661a";
        new WaitProgressDialog(getContext()).startGet(false, url, null, new WaitProgressDialog.RequestCallBackImpl() {
            @Override
            public void onSuccess(String result) {
                TVResponse response = ParseUtils.parseJson(result, TVResponse.class);
                if (response == null) {
                    showDialog("接口异常,请重试");
                    return;
                }

                if ("success".equals(response.getStatus())) {
                    if (response.getResults() != null && response.getResults().size() > 0) {
                        Results results = response.getResults().get(0);
                        String temp= KVStore.getInstance().get("videotabs");
                        KVStore.getInstance().insertOrUpdate("videotabs",temp+","+results.getShowname());
                        setUpViewPager();
                    } else {
                        toggleShowEmpty(true, "未搜索到数据");
                    }
                } else {
                    showDialog("接口异常,请重试");
                }

            }

            @Override
            public void onFailure(String result) {
                showDialog("接口异常,请重试");
            }
        });
    }

    @Override
    protected void initViewAndEvent() {
        tabTitles=new ArrayList<>();
        String temp= KVStore.getInstance().get("videotabs");
        if (StringUtils.isBlank(temp)){
            KVStore.getInstance().insertOrUpdate("videotabs","热门视频,Big笑工仿");
        }
        mFragments = new ArrayList<>();
        mAdapter = new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragments.get(position);
            }

            @Override
            public int getCount() {
                return mFragments.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return tabTitles.get(position);
            }
        };
        setUpViewPager();
    }

    public void setUpViewPager() {
        String teemp[]=KVStore.getInstance().get("videotabs").split(",");
        tabTitles.clear();
        mFragments.clear();
        for (int i = 0; i <teemp.length; i++) {
            tabTitles.add(teemp[i]);
        }
        for (int i = 0; i < tabTitles.size(); i++) {
            if (!"热门视频".equals(tabTitles.get(i))) {
                mFragments.add(TVFragment.createFragment(tabTitles.get(i)));
            } else {
                mFragments.add(VideoFragment.createFragment(0));
            }
        }
        mTabLayout.setTabMode(tabTitles.size() > 3 ? TabLayout.MODE_SCROLLABLE : TabLayout.MODE_FIXED);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(tabTitles.size());
        mTabLayout.setupWithViewPager(mViewPager);
    }


}
