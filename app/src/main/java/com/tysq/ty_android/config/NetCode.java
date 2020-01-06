package com.tysq.ty_android.config;

/**
 * author       : frog
 * time         : 2019/4/24 下午2:47
 * desc         : 错误码
 * version      : 1.3.0
 */
public class NetCode {

    /**
     * 账号尚未登录
     */
    public static final int LOGIN_ERROR_CODE = 2999;
    /**
     * 登录账号已过期，请重新登录
     */
    public static final int LOGIN_EXPIRE_ERROR_CODE = 2998;
    /**
     * 登录失败，请输入验证码
     */
    public static final int LOGIN_FAIL_ERROR_CODE = 2997;

    /**
     * 请输入验证码
     */
    public static final int NO_CAPTCHA_ERROR_CODE = 2996;

    /**
     * 文章删除
     */
    public static final int ARTICLE_REMOVE_ERROR_CODE = 3998;

    /**
     * 积分不够
     */
    public static final int NO_PERMISSION_CODE = 3997;
    /**
     *  文章验证码失效
     */
    public static final int CODE_INVALIDATE = 2995;
    /**
     * 等级不够
     */
    public static final int GRADE_NO_ENOUGH_CODE = 3996;
    /**
     * 金币数额不足
     */
    public static final int COIN_NO_ENOUGH_CODE = 2994;
    /**
     * 等级不够-积分判断
     */
    public static final int GRADE_NO_ENOUGH_PERMISSION_CODE = 3995;
}
