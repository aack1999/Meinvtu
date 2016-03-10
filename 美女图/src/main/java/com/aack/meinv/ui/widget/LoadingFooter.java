package com.aack.meinv.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewStub;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aack.meinv.R;

/**
 * Created by cundong on 2015/10/9.
 * <p/>
 * ListView/GridView/RecyclerView 分页加载时使用到的FooterView
 */
public class LoadingFooter extends RelativeLayout {

    protected State mState = State.Normal;
    private View mLoadingView;
    private TextView endview;
    private ProgressWheel mLoadingProgress;

    public LoadingFooter(Context context) {
        super(context);
        init(context);
    }

    public LoadingFooter(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LoadingFooter(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context context) {

        inflate(context, R.layout.sample_common_list_footer, this);
        setOnClickListener(null);

        setState(State.Normal, true);
    }

    public State getState() {
        return mState;
    }

    public void setState(State status ) {
        setState(status, true);
    }

    /**
     * 设置状态
     *
     * @param status
     * @param showView 是否展示当前View
     */
    public void setState(State status, boolean showView) {
        if (mState == status) {
            return;
        }
        mState = status;

        switch (status) {

            case Normal:
                setOnClickListener(null);
                if (mLoadingProgress != null) {
                    mLoadingProgress.setVisibility(GONE);
                }
                break;
            case Loading:
                setOnClickListener(null);
                if (mLoadingView == null) {
                    ViewStub viewStub = (ViewStub) findViewById(R.id.loading_viewstub);
                    mLoadingView = viewStub.inflate();
                    mLoadingProgress = (ProgressWheel) mLoadingView.findViewById(R.id.vv_loading_progress);
                    endview=(TextView)mLoadingView.findViewById(R.id.vv_load_end);
                } else {
                    mLoadingView.setVisibility(VISIBLE);
                }
                mLoadingView.setVisibility(showView ? VISIBLE : GONE);
                mLoadingProgress.setVisibility(View.VISIBLE);
                mLoadingProgress.spin();
                break;
            case TheEnd:
                setOnClickListener(null);
                if (mLoadingView != null) {
                    mLoadingProgress.stopSpinning();
                    mLoadingProgress.setVisibility(GONE);
                    endview.setVisibility(VISIBLE);
                }
                break;
            default:
                break;
        }
    }

    public static enum State {
        Normal/**正常*/, TheEnd/**加载到最底了*/, Loading/**加载中..*/
    }
}