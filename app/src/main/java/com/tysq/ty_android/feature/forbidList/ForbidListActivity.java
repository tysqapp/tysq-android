package com.tysq.ty_android.feature.forbidList;

import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.bit.view.activity.BitBaseActivity;
import com.tysq.ty_android.R;

import butterknife.BindView;

/**
 * author       : liaozhenlin
 * time         : 2019/8/27 15:11
 * desc         : 禁止评论列表
 * version      : 1.0.0
 */
public class ForbidListActivity extends BitBaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, ForbidListActivity.class);

        context.startActivity(intent);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_forbidlist;
    }

    @Override
    protected void initIntent(Intent intent) { }

    @Override
    protected void initView() {
        addLayerFragment(ID_FRAME_LAYOUT_CONTAINER, ForbidListFragment.newInstance());
        ivBack.setOnClickListener(view -> finish());
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                hideKeyboard(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false;
    }

    /**
     * 获取InputMethodManager，隐藏软键盘
     * @param token
     */
    private void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
