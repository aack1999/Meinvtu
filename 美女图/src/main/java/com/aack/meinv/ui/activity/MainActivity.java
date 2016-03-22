package com.aack.meinv.ui.activity;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;

import com.aack.meinv.R;
import com.aack.meinv.common.WaitProgressDialog;
import com.aack.meinv.response.TabListResponse;
import com.aack.meinv.ui.adapter.TabFragmentAdapter;
import com.aack.meinv.ui.fragment.MeinvFragment;
import com.aack.meinv.ui.fragment.MeinvPageFragment;
import com.aack.meinv.ui.fragment.VideoFragment;
import com.aack.meinv.ui.fragment.VideoPageFragment;
import com.aack.meinv.utils.DialogUtils;
import com.aack.meinv.utils.ParseUtils;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener{

    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;



    MeinvPageFragment meinvFragment;
    VideoPageFragment videoFragment;

    TabFragmentAdapter adapter;
    List<Fragment> fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initValue();
    }

    public void initViews(){
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void initValue(){
        fragments=new ArrayList<>();
        meinvFragment=new MeinvPageFragment();
        videoFragment=new VideoPageFragment();
        fragments.add(meinvFragment);
        fragments.add(videoFragment);
        adapter=new TabFragmentAdapter(getSupportFragmentManager(),fragments,R.id.content);
    }





    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_meinv:
                adapter.showFragment(0);
                break;
            case R.id.nav_video:
                adapter.showFragment(1);
                break;
        }
        item.setCheckable(true);
        drawerLayout.closeDrawers();
        return true;
    }
}
