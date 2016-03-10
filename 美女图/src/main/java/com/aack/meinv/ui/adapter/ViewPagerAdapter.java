package com.aack.meinv.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import org.apache.commons.lang.StringUtils;

import java.util.List;

/**
 * Created by aacc on 2015/8/14.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {

    List<Fragment> items;

    public ViewPagerAdapter(FragmentManager fm, List<Fragment> items) {
        super(fm);
        this.items=items;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String str= items.get(position).getArguments().getString("title");
        return StringUtils.isNotBlank(str)?str:"";
    }

    @Override
    public Fragment getItem(int position) {
        return items.get(position);
    }

    @Override
    public int getCount() {
        return items.size();
    }
}
