package com.tysq.ty_android.utils;

import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.TextView;
/**
 * author       : liaozhenlin
 * time         : 2019/10/23 16:18
 * desc         : 富文本的拦截事件消除
 * version      : 1.5.0
 */
public class MyLinkedMovementMethod extends LinkMovementMethod {
    private static MyLinkedMovementMethod sInstance;
    public static MyLinkedMovementMethod getInstance() {
        if (sInstance == null)
            sInstance = new MyLinkedMovementMethod();
        return sInstance;
    }

    @Override
    public boolean onTouchEvent(TextView widget, Spannable buffer, MotionEvent event) {
        // 因为TextView没有点击事件，所以点击TextView的非富文本时，super.onTouchEvent()返回false；
        // 此时可以让TextView的父容器执行点击事件；
        boolean isConsume =  super.onTouchEvent(widget, buffer, event);
        if (!isConsume && event.getAction() == MotionEvent.ACTION_UP) {
            ViewParent parent = widget.getParent();
            if (parent instanceof ViewGroup) {
                // 获取被点击控件的父容器，让父容器执行点击；
                ((ViewGroup) parent).performClick();
            }
        }
        return isConsume;
    }

}
