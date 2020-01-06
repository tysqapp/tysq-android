package com.tysq.ty_android.feature.web;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bit.utils.UIUtils;
import com.bit.view.activity.BitBaseActivity;
import com.tysq.ty_android.R;
import com.tysq.ty_android.utils.TyUtils;

import butterknife.BindView;

/**
 * author       : frog
 * time         : 2019/4/17 下午5:56
 * desc         : WebView
 * version      : 1.3.0
 */
public class TyWebViewActivity
        extends BitBaseActivity
        implements TyWebViewListener, View.OnClickListener {

    private static final String URL = "URL";

    private String mUrl;

    private int yOffset;
    private int xOffset;

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.frame_layout_container)
    FrameLayout frameLayoutContainer;
    @BindView(R.id.iv_menu)
    ImageView ivMenu;

    private TyWebViewFragment tyWebViewFragment;

    private PopupWindow mMenuWindow;

    public static void startActivity(Context context,
                                     String url) {
        Intent intent = new Intent(context, TyWebViewActivity.class);
        intent.putExtra(URL, url);

        context.startActivity(intent);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_web_view;
    }

    @Override
    protected void initIntent(Intent intent) {
        mUrl = intent.getStringExtra(URL);
    }

    @Override
    protected void initView() {
        setStatusBarColor(R.color.jColorToolbar);

        yOffset = UIUtils.dip2px(this, 3.5f);
        xOffset = UIUtils.dip2px(this, 20);

        ivBack.setOnClickListener(v -> finish());

        View view = getLayoutInflater().inflate(R.layout.window_web_view_menu, null);
        view.findViewById(R.id.ll_crop).setOnClickListener(this);
        view.findViewById(R.id.ll_open).setOnClickListener(this);
        view.findViewById(R.id.ll_refresh).setOnClickListener(this);

        if (mMenuWindow == null) {
            mMenuWindow = new PopupWindow(view,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    true);

            mMenuWindow.setOutsideTouchable(true);
            mMenuWindow.setBackgroundDrawable(
                    ContextCompat.getDrawable(this, R.drawable.ic_one_inch_alpha)
            );
        }

        ivMenu.setOnClickListener(v -> {
            mMenuWindow.showAsDropDown(ivMenu, -xOffset, -yOffset, Gravity.START);
        });

        tyWebViewFragment = TyWebViewFragment.newInstance(mUrl);
        tyWebViewFragment.setListener(this);

        addLayerFragment(ID_FRAME_LAYOUT_CONTAINER, tyWebViewFragment);

    }

    @Override
    public void setTitle(String title) {
        runOnUiThread(() -> tvTitle.setText(title));
    }

    @Override
    public void close() {
        tyWebViewFragment.setListener(null);
        finish();
    }

    @Override
    public void onClick(View v) {

        if (mMenuWindow != null) {
            mMenuWindow.dismiss();
        }

        if (tyWebViewFragment == null) {
            return;
        }

        switch (v.getId()) {
            case R.id.ll_crop:
                tyWebViewFragment.crop();
                break;

            case R.id.ll_open:
                TyUtils.goToOuterBrowser(tyWebViewFragment.getUrl());
                break;

            case R.id.ll_refresh:
                tyWebViewFragment.reload();
                break;
        }
    }
}
