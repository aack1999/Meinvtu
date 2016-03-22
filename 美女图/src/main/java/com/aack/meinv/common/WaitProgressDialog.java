package com.aack.meinv.common;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.aack.meinv.R;
import com.aack.meinv.ui.widget.ProgressWheel;
import com.aack.meinv.utils.CheckUtils;
import com.aack.meinv.utils.Utils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.GetBuilder;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Map;

import okhttp3.Call;

public class WaitProgressDialog extends Dialog {

    TextView tv_content;
    ProgressWheel progressWheel;

    public WaitProgressDialog(Context context) {
        super(context, R.style.dialog);
        init();
    }

    private void init() {
        setCanceledOnTouchOutside(false);
        LayoutParams ll = new LayoutParams(Utils.dpToPx(100), Utils.dpToPx(80));
        View v = getLayoutInflater().inflate(R.layout.dialog_wait_layout, null);
        tv_content=(TextView)v.findViewById(R.id.dialog_content);
        progressWheel=(ProgressWheel)v.findViewById(R.id.progresswheel);
        progressWheel.setProgress(0);
        setContentView(v, ll);
    }

    public void setContent(String text){
        tv_content.setText(text);
    }

    public void startGet(boolean isShowDialog, final String url, final Map<String, String> map, final RequestCallBackImpl callBack) {
//        if (!CheckUtils.checkNetWork(getContext())){
//            callBack.onFailure("无可用网络");
//            return ;
//        }
        if (isShowDialog) {
            show();
        }
        GetBuilder getBuilder=OkHttpUtils.get().url(url);
        if (map!=null){
            getBuilder.params(map);
        }
        getBuilder.build().connTimeOut(20000).execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                dismiss();
                callBack.onFailure(e.getMessage());
                if (e instanceof SocketTimeoutException) {
                    callBack.onFailure("请求超时,请稍后再试");
                } else if (e instanceof UnknownHostException) {
                    //无可用网路
                }
            }

            @Override
            public void onResponse(String response) {
                dismiss();
                Log.e("dd", response);
                callBack.onSuccess(response);
            }
        });
    }

    public void startNetWork(boolean isShowDialog,final NetWorkBackImpl netWorkBack){
        if (isShowDialog) {
            show();
        }
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                netWorkBack.onSuccess();
                hide();
            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                netWorkBack.onRequest();
                handler.sendEmptyMessage(0);
            }
        }).start();
    }

    @Override
    public void show() {
        super.show();
        progressWheel.spin();
    }

    public interface RequestCallBackImpl {
        public void onSuccess(String result);
        public void onFailure(String result);
    }

    public interface NetWorkBackImpl{
        public void onRequest();
        public void onSuccess();
    }

}
