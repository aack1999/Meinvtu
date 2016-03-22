package com.aack.meinv.ui.fragment;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.aack.meinv.R;
import com.aack.meinv.common.WaitProgressDialog;
import com.aack.meinv.response.TabListResponse;
import com.aack.meinv.utils.DialogUtils;
import com.aack.meinv.utils.ParseUtils;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by root on 16-3-20.
 */
public class MeinvPageFragment extends BaseFragment {

    @Bind(R.id.tablayout)
    TabLayout mTabLayout;
    @Bind(R.id.viewpager)
    ViewPager mViewPager;

    TabListResponse mResponse;
    FragmentPagerAdapter mAdapter;
    List<Fragment> mFragments;

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_meinv_content;
    }

    @Override
    protected void onFirstUserVisible() {

    }

    @Override
    protected void initViewAndEvent() {
        mFragments=new ArrayList<>();
        mAdapter=new FragmentPagerAdapter(getChildFragmentManager()) {
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
                return mResponse.getTngou().get(position).getTitle();
            }
        };
        getTabListData();
    }

    public void setUpViewPager(){
        for (int i = 0; i < mResponse.getTngou().size(); i++) {
            TabListResponse.TabModel model = mResponse.getTngou().get(i);
            mFragments.add(MeinvFragment.createFragment(model.getId()));
        }
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(mFragments.size());
        mTabLayout.setupWithViewPager(mViewPager);
    }

    public void getTabListData(){
        mTabLayout.removeAllTabs();
        String url="http://www.tngou.net/tnfs/api/classify";
        new WaitProgressDialog(getContext()).startGet(true, url, null, new WaitProgressDialog.RequestCallBackImpl() {
            @Override
            public void onSuccess(String result) {
                mResponse = ParseUtils.parseJson(result, TabListResponse.class);
                if (mResponse == null) {
                    DialogUtils.showMessage(getContext(), "提示", "接口异常");
                    return;
                }
                if (mResponse.isStatus() && StringUtils.isBlank(mResponse.getMsg())) {
                    if (mResponse.getTngou() != null && mResponse.getTngou().size() > 0) {
                        mTabLayout.setTabMode(mResponse.getTngou().size() > 4 ? TabLayout.MODE_SCROLLABLE : TabLayout.MODE_FIXED);
                        for (int i = 0; i < mResponse.getTngou().size(); i++) {
                            TabListResponse.TabModel model = mResponse.getTngou().get(i);
                            mTabLayout.addTab(mTabLayout.newTab().setText(model.getTitle()));
                        }
                        setUpViewPager();
                    }
                } else {
                    if (StringUtils.isNotBlank(mResponse.getMsg())) {
                        DialogUtils.showMessage(getContext(), "提示", mResponse.getMsg());
                    } else {
                        DialogUtils.showMessage(getContext(), "提示", "接口异常");
                    }
                }
            }

            @Override
            public void onFailure(String result) {
                DialogUtils.showMessage(getContext(), "提示", result);
            }
        });
    }


}
