package com.aack.meinv.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.aack.meinv.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by root on 16-2-22.
 */
public class BaseActivity extends AppCompatActivity {

    @Nullable
    @Bind(R.id.common_toolbar)
    Toolbar toolbar;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        bindViews();
    }

    public void bindViews(){
        ButterKnife.bind(this);
        setupToolbar();
    }

    protected void setupToolbar() {
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    protected void doActivity(Class<?> clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }

    protected void doActivityForResult(Class<?> clazz, int requestCode) {
        Intent intent = new Intent(this, clazz);
        startActivityForResult(intent, requestCode);
    }

    protected void doActivityForResult(Class<?> clazz,Bundle b, int requestCode) {
        Intent intent = new Intent(this, clazz);
        if (b!=null)
            intent.putExtras(b);
        startActivityForResult(intent, requestCode);
    }

    public void doActivity(Class c, Bundle bundle) {
        Intent intent = new Intent(this, c);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    public void doActivity(Class c,boolean isFinish){
        Intent intent = new Intent(this, c);
        startActivity(intent);
        if (isFinish){
            finish();
        }
    }

}
