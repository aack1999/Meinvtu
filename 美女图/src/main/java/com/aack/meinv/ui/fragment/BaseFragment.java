package com.aack.meinv.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aack.meinv.R;
import com.aack.meinv.varyview.VaryViewHelper;
import com.afollestad.materialdialogs.MaterialDialog;

import java.lang.reflect.Field;

import butterknife.ButterKnife;

/**
 * Created by root on 16-2-5.
 */
public abstract class BaseFragment extends Fragment{

    VaryViewHelper mVaryViewHelper;

    public View root;
    private boolean isPrepared;
    private boolean isFirstVisible = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return root=inflater.inflate(getContentViewLayoutID(),container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);
        if (null != getLoadingTargetView()) {
            mVaryViewHelper = new VaryViewHelper.Builder()
                    .setDataView(getLoadingTargetView())
                    .setLoadingView(LayoutInflater.from(getActivity()).inflate(R.layout.layout_loadingview,null))
                    .setEmptyView(LayoutInflater.from(getActivity()).inflate(R.layout.layout_emptyview, null))
                    .setErrorView(LayoutInflater.from(getActivity()).inflate(R.layout.layout_errorview,null))
                    .build() ;
        }
        initViewAndEvent();
    }

    protected abstract View getLoadingTargetView();

    protected abstract int getContentViewLayoutID();

    protected abstract void onFirstUserVisible();

    protected abstract void initViewAndEvent();

    @Override
    public void onDetach() {
        super.onDetach();
        // for bug ---> java.lang.IllegalStateException: Activity has been destroyed
        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);

        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (isFirstVisible) {
                isFirstVisible = false;
                initPrepare();
            }
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initPrepare();
    }

    private synchronized void initPrepare() {
        if (isPrepared) {
            onFirstUserVisible();
        } else {
            isPrepared = true;
        }
    }


    protected void doActivity(Class<?> clazz) {
        Intent intent = new Intent(getActivity(), clazz);
        startActivity(intent);
    }

    protected void doActivityForResult(Class<?> clazz, int requestCode) {
        Intent intent = new Intent(getActivity(), clazz);
        startActivityForResult(intent, requestCode);
    }

    protected void doActivityForResult(Class<?> clazz,Bundle b, int requestCode) {
        Intent intent = new Intent(getActivity(), clazz);
        if (b!=null)
            intent.putExtras(b);
        startActivityForResult(intent, requestCode);
    }

    public void doActivity(Class c, Bundle bundle) {
        Intent intent = new Intent(getActivity(), c);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    protected void showToast(String msg) {
        Snackbar.make(getActivity().getWindow().getDecorView(), msg, Snackbar.LENGTH_SHORT).show();
    }

    public void showDialog(String msg){
        new MaterialDialog.Builder(getContext()).title("提示").content(msg).positiveText("确定").show();
    }

    protected void toggleShowLoading(boolean toggle) {
        if (null == mVaryViewHelper) {
            throw new IllegalArgumentException("You must return a right target view for loading");
        }

        if (toggle) {
            mVaryViewHelper.showLoadingView();
        } else {
            mVaryViewHelper.showDataView();
        }
    }

    protected void toggleShowEmpty(boolean toggle, String msg) {
        if (null == mVaryViewHelper) {
            throw new IllegalArgumentException("You must return a right target view for loading");
        }
        if (toggle) {
            mVaryViewHelper.showEmptyView();
        } else {
            mVaryViewHelper.showDataView();
        }
    }

    protected void toggleShowError(boolean toggle, String msg,View.OnClickListener onClickListener) {
        if (null == mVaryViewHelper) {
            throw new IllegalArgumentException("You must return a right target view for loading");
        }

        if (toggle) {
            mVaryViewHelper.showErrorView(msg,onClickListener);
        } else {
            mVaryViewHelper.showDataView();
        }
    }

}
