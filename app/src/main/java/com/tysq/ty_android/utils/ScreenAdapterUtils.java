package com.tysq.ty_android.utils;

import android.content.Context;

import com.bit.utils.UIUtils;
import com.tysq.ty_android.config.Constant;

/**
 * author       : frog
 * time         : 2019-07-22 12:14
 * desc         : 屏幕适配
 * version      : 1.3.0
 */
public class ScreenAdapterUtils {

    private static int SCREEN_WIDTH;
    private static int SCREEN_HEIGHT;

    private static int AD_HEIGHT;

    public static void init(Context context) {

        SCREEN_WIDTH = UIUtils.getScreenWidth(context);
        SCREEN_HEIGHT = UIUtils.getScreenHeight(context);

        initADHeight();

    }

    public static int getScreenWidth() {
        return SCREEN_WIDTH;
    }

    public static int getScreenHeight() {
        return SCREEN_HEIGHT;
    }

    /**
     * @param percent 宽高比
     */
    public static float getHeight(int width, float percent) {
        return width / percent;
    }

    private static void initADHeight() {
        AD_HEIGHT = (int) (getScreenWidth() / Constant.MULTIPLE);
    }

    /**
     * 获取广告位的高度
     */
    public static int getAdHeight() {
        return AD_HEIGHT;
    }

}
