package com.aack.meinv.ui.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.aack.meinv.R;
import com.aack.meinv.common.WaitProgressDialog;
import com.aack.meinv.response.TabListResponse;
import com.aack.meinv.ui.fragment.MeinvFragment;
import com.aack.meinv.utils.DialogUtils;
import com.aack.meinv.utils.ParseUtils;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class MainActivity extends BaseActivity {

    @Bind(R.id.tablayout)
    TabLayout mTabLayout;
    @Bind(R.id.viewpager)
    ViewPager mViewPager;

    TabListResponse mResponse;
    FragmentPagerAdapter mAdapter;
    List<Fragment> mFragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initValue();
    }

    public void initValue(){
        mFragments=new ArrayList<>();
        mAdapter=new FragmentPagerAdapter(getSupportFragmentManager()) {
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

    public void getTabListData(){
        mTabLayout.removeAllTabs();
        String url="http://www.tngou.net/tnfs/api/classify";
        new WaitProgressDialog(this).startGet(true, url, null, new WaitProgressDialog.RequestCallBackImpl() {
            @Override
            public void onSuccess(String result) {
                mResponse = ParseUtils.parseJson(result, TabListResponse.class);
                if (mResponse == null) {
                    DialogUtils.showMessage(MainActivity.this, "提示", "接口异常");
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
                        DialogUtils.showMessage(MainActivity.this, "提示", mResponse.getMsg());
                    } else {
                        DialogUtils.showMessage(MainActivity.this, "提示", "接口异常");
                    }
                }
            }

            @Override
            public void onFailure(String result) {
                DialogUtils.showMessage(MainActivity.this, "提示", result);
            }
        });
    }

    public void setUpViewPager(){
        for (int i = 0; i < mResponse.getTngou().size(); i++) {
            TabListResponse.TabModel model = mResponse.getTngou().get(i);
            mFragments.add(MeinvFragment.createFragment(model.getId()));
        }
        mViewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

}
