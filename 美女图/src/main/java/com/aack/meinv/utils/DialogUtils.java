package com.aack.meinv.utils;

import android.content.Context;

import com.afollestad.materialdialogs.MaterialDialog;

/**
 * Created by root on 16-2-25.
 */
public class DialogUtils {

    public static void showMessage(Context context,String title,String message){
        new MaterialDialog.Builder(context).title(title).content(message).positiveText("确定").show();
    }
}
