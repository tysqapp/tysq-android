package com.tysq.ty_android.config;

import android.content.Context;

import com.bit.utils.UIUtils;

/**
 * author       : frog
 * time         : 2019/4/23 下午6:16
 * desc         :
 * version      : 1.3.0
 */
public class TyConfig {

    /**
     * 验证码倒计时
     */
    public static final int TIME_LONG = 60;

    /**
     * 密码最小长度
     */
    public static final int PWD_MIN_LENGTH = 6;

    /**
     * email 的字段
     */
    public static final String EMAIL = "email";

    /**
     * email 的正则
     */
    public static final String EMAIL_PARENT = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";

    /**
     * 首页——推荐分类ID
     */
    public static final int CATEGORY_RECOMMEND_ID = -1;

    /**
     * 全部
     */
    public static final int CATEGORY_SUB_ALL_ID = 0;

    /**
     * 一级分类类型
     */
    public static final String TOP_CATEGORY = "TOP_CATEGORY";

    /**
     * 二级分类类型
     */
    public static final String SUB_CATEGORY = "SUB_CATEGORY";

    /**
     * 分类错误
     */
    public static final int ERROR = -2;

    /**
     * 文件夹
     */
    public interface Folder {
        /**
         * 主文件夹
         */
        String MAIN = "tysq";
        /**
         * 二维码
         */
        String QR = MAIN + "/qr";
        /**
         * 图像
         */
        String IMG = MAIN + "/img";
        /**
         * 云盘下载
         */
        String CLOUD_DOWNLOAD = MAIN + "/cloud";
    }

    public static int SCREEN_WIDTH;
    public static int SCREEN_HEIGHT;

    public static void init(Context context) {
        SCREEN_WIDTH = UIUtils.getScreenWidth(context);
        SCREEN_HEIGHT = UIUtils.getScreenWidth(context);
    }

    public static int getScreenWidth() {
        return SCREEN_WIDTH;
    }

    public static int getScreenHeight() {
        return SCREEN_HEIGHT;
    }
}
