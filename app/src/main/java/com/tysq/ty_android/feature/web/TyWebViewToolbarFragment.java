package com.tysq.ty_android.feature.web;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bit.utils.UIUtils;
import com.bit.view.fragment.BitBaseFragment;
import com.tysq.ty_android.R;
import com.tysq.ty_android.utils.TyUtils;

import butterknife.BindView;

/**
 * author       : frog
 * time         : 2019/4/17 下午5:56
 * desc         : WebView
 * version      : 1.5.2
 */
public class TyWebViewToolbarFragment
        extends BitBaseFragment
        implements TyWebViewListener, View.OnClickListener {

    private static final String URL = "URL";

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_menu)
    ImageView ivMenu;

    private TyWebViewFragment tyWebViewFragment;

    private PopupWindow mMenuWindow;

    private String mUrl;

    private int yOffset;
    private int xOffset;

    public static TyWebViewToolbarFragment newInstance(String url) {

        Bundle args = new Bundle();
        args.putString(URL, url);

        TyWebViewToolbarFragment fragment = new TyWebViewToolbarFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initArgs(Bundle arguments) {
        super.initArgs(arguments);
        mUrl = arguments.getString(URL);
    }

    @Override
    protected View onCreateFragmentView(LayoutInflater inflater,
                                        @Nullable ViewGroup container,
                                        @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_web_view, container, false);
    }

    @Override
    protected void initView(View view) {
        yOffset = UIUtils.dip2px(getContext(), 3.5f);
        xOffset = UIUtils.dip2px(getContext(), 20);

        View window = View.inflate(getContext(), R.layout.window_web_view_menu, null);
        window.findViewById(R.id.ll_crop).setOnClickListener(this);
        window.findViewById(R.id.ll_open).setOnClickListener(this);
        window.findViewById(R.id.ll_refresh).setOnClickListener(this);

        if (mMenuWindow == null) {
            mMenuWindow = new PopupWindow(window,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    true);

            mMenuWindow.setOutsideTouchable(true);
            mMenuWindow.setBackgroundDrawable(
                    ContextCompat.getDrawable(getContext(), R.drawable.ic_one_inch_alpha)
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
        FragmentActivity activity = getActivity();
        if (activity != null) {
            activity.runOnUiThread(() -> tvTitle.setText(title));
        }
    }

    @Override
    public void close() {
        tyWebViewFragment.setListener(null);
        FragmentActivity activity = getActivity();
        if (activity != null) {
            activity.finish();
        }
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
