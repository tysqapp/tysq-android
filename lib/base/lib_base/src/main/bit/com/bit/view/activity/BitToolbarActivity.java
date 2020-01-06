package com.bit.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bit.R;
import com.bit.config.BitManager;
import com.bit.presenter.BasePresenter;

/**
 * author       : frog
 * time         : 2019/3/25 下午5:38
 * desc         : 带 Toolbar 的 Activity
 * version      : 1.3.0
 */
public abstract class BitToolbarActivity<T extends BasePresenter> extends BitBaseActivity<T> {

    protected Toolbar mToolbar;
    protected TextView mTvTitle;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        if (getSupportActionBar() == null) {
            initToolbar();
        } else {
            Log.w(TAG, "You had set a toolbar.Change the theme to NoActionBar in Manifest.");
        }

        super.onCreate(savedInstanceState);
    }

    protected void setToolbarColor(int color) {
        setToolbarColor(color, false);
    }

    /**
     * 设置 toolbar 的颜色
     *
     * @param color
     */
    protected void setToolbarColor(int color, boolean isRealColor) {
        int realColor = isRealColor ? color : ContextCompat.getColor(this, color);
        this.mToolbar.setBackgroundColor(realColor);
    }

    /**
     * 初始化toolbar
     */
    private void initToolbar() {

        // 获取 DecorView
        ViewGroup decorView = (ViewGroup) getWindow().getDecorView();

        // 获取到 TitleView
        ViewGroup viewGroup = (ViewGroup) decorView.getChildAt(0);

        if (viewGroup instanceof LinearLayout) {
            View toolbar = LayoutInflater.from(this).inflate(BitManager.getInstance().getToolbarLayout(), null, false);

            viewGroup.addView(toolbar, 0);

            this.mToolbar = toolbar.findViewById(R.id.toolbar);

            setSupportActionBar(this.mToolbar);

            if (getSupportActionBar() != null) {
                //隐藏标题
                getSupportActionBar().setDisplayShowTitleEnabled(false);
                //显示回退按钮
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }

            ViewGroup contentFrameLayout = findViewById(Window.ID_ANDROID_CONTENT);
            View parentView = contentFrameLayout.getChildAt(0);
            if (parentView != null) {
                parentView.setFitsSystemWindows(true);
            }
        }

    }

    /**
     * 设置标题
     *
     * @param title 标题
     */
    @Override
    public void setTitle(CharSequence title) {
        if (TextUtils.isEmpty(title)) {
            return;
        }

        if (this.mToolbar == null) {
            return;
        }

        this.mToolbar.setTitle(title);
    }

    /**
     * 隐藏回退按钮
     */
    protected void hideHomeBtn() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
