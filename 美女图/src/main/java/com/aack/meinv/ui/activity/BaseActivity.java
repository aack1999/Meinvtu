package com.aack.meinv.ui.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.view.WindowManager;

import com.aack.meinv.R;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by root on 16-2-22.
 */
public class BaseActivity extends AppCompatActivity {

    @Nullable
    @Bind(R.id.common_toolbar)
    Toolbar toolbar;
    SystemBarTintManager tintManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTranslucentStatus(true);
        tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.colorPrimary);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        bindViews();
    }

    public void bindViews() {
        ButterKnife.bind(this);
        setupToolbar();
    }

    public void setThemeColor(int color){
        tintManager.setStatusBarTintColor(color);
    }

    protected void setupToolbar() {
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    @TargetApi(19)
    public void setTranslucentStatus(boolean on) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window win = getWindow();
            WindowManager.LayoutParams winParams = win.getAttributes();
            final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            if (on) {
                winParams.flags |= bits;
            } else {
                winParams.flags &= ~bits;
            }
            win.setAttributes(winParams);
        }
    }

    public void showToast(String text){
        Snackbar.make(getWindow().getDecorView(),text,Snackbar.LENGTH_SHORT).show();
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
