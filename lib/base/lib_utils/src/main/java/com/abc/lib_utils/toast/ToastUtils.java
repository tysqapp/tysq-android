package com.abc.lib_utils.toast;

import android.app.Application;
import android.view.Gravity;

/**
 * author       : frog
 * time         : 2019/4/23 上午10:19
 * desc         : Toast
 * version      : 1.3.0
 */

public class ToastUtils {

    private static JToast TOAST;

    public static void init(Application context) {
        TOAST = JToast.makeText(context, "", android.widget.Toast.LENGTH_SHORT);
    }

    public static void show(String message) {
        showCenter(message);
    }

    public static void showTop(String message) {
        TOAST.setText(message)
                .setGravity(Gravity.CENTER | Gravity.TOP, 0, 0)
                .show();
    }

    public static void showCenter(String message) {
        TOAST.setText(message)
                .setGravity(Gravity.CENTER, 0, 0)
                .show();
    }

    public static void showBottom(String message) {
        TOAST.setText(message)
                .setGravity(Gravity.CENTER | Gravity.BOTTOM, 0, 0)
                .show();
    }

}
