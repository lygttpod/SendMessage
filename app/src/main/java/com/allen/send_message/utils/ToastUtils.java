package com.allen.send_message.utils;

import android.widget.TextView;
import android.widget.Toast;

import com.allen.send_message.MyApplication;

/**
 * Created by allen on 2015/10/20.
 */
public class ToastUtils {
    private static Toast mToast;
    private static TextView titleTv;
    private static TextView contentTv;

    /**
     * 新建一个Toast显示
     */
    public static void show(String msg) {
        Toast newToast = Toast.makeText(MyApplication.getAppContext(), msg, Toast.LENGTH_SHORT);
        newToast.show();
    }

    /**
     * 新建一个长时间的Toast显示
     */
    public static void showLong(String msg) {
        Toast newToast = Toast.makeText(MyApplication.getAppContext(), msg, Toast.LENGTH_LONG);
        newToast.show();
    }

    /**
     * 不新建Toast，直接展示全局唯一的长时间Toast
     *
     * @param msg
     */
    public static void showGlobalLong(String msg) {
        if (mToast == null) {
            mToast = Toast.makeText(MyApplication.getAppContext(), msg, Toast.LENGTH_LONG);
        } else {
            mToast.setText(msg);
            mToast.setDuration(Toast.LENGTH_LONG);
        }
        mToast.show();
    }

    /**
     * 不新建Toast，直接展示全局唯一的Toast
     * <p>
     * 防止连续弹出大量的Toast，影响用户体验
     */
    public static void showGlobalToast(String msg) {
        if (mToast == null) {
            mToast = Toast.makeText(MyApplication.getAppContext(), msg, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(msg);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }
}
