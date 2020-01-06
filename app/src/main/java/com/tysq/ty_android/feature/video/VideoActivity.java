package com.tysq.ty_android.feature.video;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.View;

import com.bit.view.activity.BitBaseActivity;

/**
 * author       : frog
 * time         : 2019/5/24 上午10:17
 * desc         : 视屏播放
 * version      : 1.3.0
 */
public class VideoActivity extends BitBaseActivity {

    private String mUrl;
    private String mTitle;
    private String mCover;

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && Build.VERSION.SDK_INT >= 19) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    @Override
    protected void setStatusBarColor(int color) {
        super.setStatusBarColor(color);
    }

    public static void startActivity(Context context,
                                     String url,
                                     String title,
                                     String cover) {
        Intent intent = new Intent(context, VideoActivity.class);

        intent.putExtra(VideoFragment.URL, url);
        intent.putExtra(VideoFragment.TITLE, title);
        intent.putExtra(VideoFragment.COVER, cover);

        context.startActivity(intent);
    }

    @Override
    protected int getLayout() {
        return COMMON_FRAME_LAYOUT;
    }

    @Override
    protected void initIntent(Intent intent) {

        mUrl = intent.getStringExtra(VideoFragment.URL);
        mTitle = intent.getStringExtra(VideoFragment.TITLE);
        mCover = intent.getStringExtra(VideoFragment.COVER);

    }

    @Override
    protected void initView() {
        addLayerFragment(ID_FRAME_LAYOUT_CONTAINER, VideoFragment.newInstance(mUrl, mTitle, mCover));
    }
}
