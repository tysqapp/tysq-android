package com.bit.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.v4.graphics.ColorUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.bit.R;


/**
 * author       : liaozhenlin
 * time         : 2019/10/10 9:42
 * desc         : 改变系统状态栏颜色，字体，以及布局延伸至状态栏
 * version      : 1.5.0
 */
public class StatusUtils {

    private static final int FAKE_TRANSLUCENT_VIEW_ID = R.id.status_bar_util_translucent_view;
    /**
     * 设置状态栏颜色，并改变状态栏字体颜色
     * @param activity 当前所在的activity
     * @param color 想要设置的颜色
     */
    public static void setStatusBar(Activity activity, int color){

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){//小于5.0不做处理
            return;
        }

        Window window = activity.getWindow();
        window.setStatusBarColor(color);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M){ // 小于6.0字体不做处理
            return;
        }

        if (isLightColor(color)){
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }else{
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        }
    }

    /**
     * 判断颜色是不是亮色
     * @param color 想要设置的颜色
     * @return
     */
    private static boolean isLightColor(@ColorInt int color){
        return ColorUtils.calculateLuminance(color) >= 0.5;
    }

    /**
     * 单纯改变状态栏字体颜色
     * 字体颜色修改除了小米的MIUI和魅族的Flyme在Android4.4之后各自提供了自家的修改方法
     * 其他品牌只能在Android6.0及以后才能修改
     *
     * @param isBack 是否显示黑色字体
     * @param activity 当前所在的activity
     */
    public static void changeStatusBarTextColor(boolean isBack, @NonNull Activity activity){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (isBack){
                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }else {
                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            }
        }
    }

    /**
     * 单纯设置根布局延伸至状态栏
     */
    public static void setTransaction(Activity activity){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            //设置状态栏为透明
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    /**
     * 同时设置根布局延伸至状态栏以及改变状态栏的字体, 6.0以下不做处理
     * @param activity 当前所在的activity
     * @param isBlack   是否显示黑色字体
     */
    public static void setTransactionAndChangeTextColor(Activity activity, boolean isBlack){

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            return;
        }
        ViewGroup decorView = (ViewGroup)activity.getWindow().getDecorView();

        if (isBlack){
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                            View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
            decorView.addView(createTranslucentStatusBarView(activity, 0));
            setRootView(activity,true);
        }else {
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            activity.getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                            View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
            decorView.findViewById(FAKE_TRANSLUCENT_VIEW_ID).setVisibility(View.GONE);
            setRootView(activity,false);
        }
    }


    /**
     * 创造一个和状态栏一样高的矩形
     * @param activity
     * @param alpha 透明度
     * @return
     */
    private static View createTranslucentStatusBarView(Activity activity,int alpha) {
        // 绘制一个和状态栏一样高的矩形
        View statusBarView = new View(activity);
        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight(activity));
        statusBarView.setLayoutParams(params);
        //statusBarView.setBackgroundColor(Color.argb(alpha, 0, 0, 0));
        statusBarView.setBackgroundColor(Color.TRANSPARENT);
        statusBarView.setAlpha(0);
        statusBarView.setId(FAKE_TRANSLUCENT_VIEW_ID);
        Log.d("fakeStatusBarView", "执行了2");
        return statusBarView;
    }

    /**
     * 获取状态栏的高度
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        if (context == null) {
            return 0;
        }
        // 获得状态栏高度
        int resourceId = context.getResources()
                .getIdentifier("status_bar_height", "dimen", "android");
        return context.getResources().getDimensionPixelSize(resourceId);
    }

    private static void setRootView(Activity activity, boolean isClicp) {
        ViewGroup parent = (ViewGroup) activity.findViewById(android.R.id.content);
        for (int i = 0, count = parent.getChildCount(); i < count; i++) {
            View childView = parent.getChildAt(i);
            if (childView instanceof ViewGroup) {
                childView.setFitsSystemWindows(isClicp);
                ((ViewGroup) childView).setClipToPadding(isClicp);
            }
        }
    }

}
