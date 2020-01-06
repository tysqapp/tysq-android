package com.bit.utils;

import com.abc.lib_utils.toast.ToastUtils;

/**
 * author       : frog
 * time         : 2019/4/25 上午11:03
 * desc         : 双击退出识别器
 * version      : 1.3.0
 */

public class DoubleClickExitDetector {

    private int effectiveIntervalTime;    // 有效的间隔时间，单位毫秒
    private long lastClickTime;    // 上次点击时间
    private String hintMessage;    // 提示消息

    /**
     * 创建一个双击退出识别器
     *
     * @param hintMessage           提示消息
     * @param effectiveIntervalTime 有效间隔时间
     */
    public DoubleClickExitDetector(String hintMessage,
                                   int effectiveIntervalTime) {
        this.hintMessage = hintMessage;
        this.effectiveIntervalTime = effectiveIntervalTime;
    }

    /**
     * 创建一个双击退出识别器，有效间隔时间默认为2000毫秒
     *
     * @param hintMessage 提示消息
     */
    public DoubleClickExitDetector(String hintMessage) {
        this(hintMessage, 2000);
    }

    /**
     * 点击，你需要重写Activity的onBackPressed()方法，先调用此方法，如果返回true就执行父类的onBackPressed()方法来关闭Activity否则不执行
     *
     * @return 当两次点击时间间隔小于有效间隔时间时就会返回true，否则返回false
     */
    public boolean click() {
        long currentTime = System.currentTimeMillis();
        boolean result = (currentTime - lastClickTime) < effectiveIntervalTime;
        lastClickTime = currentTime;
        if (!result) {
            ToastUtils.show(hintMessage);
        }
        return result;
    }

    /**
     * 设置有效间隔时间，单位毫秒
     *
     * @param effectiveIntervalTime 效间隔时间，单位毫秒。当两次点击时间间隔小于有效间隔时间click()方法就会返回true
     */
    public void setEffectiveIntervalTime(int effectiveIntervalTime) {
        this.effectiveIntervalTime = effectiveIntervalTime;
    }

    /**
     * 设置提示消息
     *
     * @param hintMessage 当前点击同上次点击时间间隔大于有效间隔时间click()方法就会返回false，并且显示提示消息
     */
    public void setHintMessage(String hintMessage) {
        this.hintMessage = hintMessage;
    }
}
