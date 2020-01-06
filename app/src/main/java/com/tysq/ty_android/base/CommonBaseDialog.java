package com.tysq.ty_android.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.bit.utils.UIUtils;
import com.bit.view.fragment.dialog.BitBaseDialogFragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * author       : frog
 * time         : 2019-08-29 10:27
 * desc         : 通用的弹框
 * version      : 1.0.0
 */
public abstract class CommonBaseDialog extends BitBaseDialogFragment {

    private Unbinder mBinder;

    @Override
    protected View onCreateFragmentView(LayoutInflater inflater,
                                        @Nullable ViewGroup container,
                                        @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(getLayoutResource(),
                container, false);
        mBinder = ButterKnife.bind(this, fragmentView);
        return fragmentView;
    }

    abstract protected int getLayoutResource();

    protected boolean getCancelable() {
        return true;
    }

    abstract protected int obtainWidth();

    abstract protected int obtainHeight();

    abstract protected int obtainGravity();

    @Override
    protected void initWindows(Window window) {

        setCancelable(getCancelable());

        View decorView = window.getDecorView();
        decorView.setPadding(0, 0, 0, 0);
        window.setLayout(obtainWidth(), obtainHeight());
        window.setGravity(obtainGravity());
        super.initWindows(window);

    }

    protected int dp2px(float dp) {
        return UIUtils.dip2px(getContext(), dp);
    }

    @Override
    public void onDestroy() {
        if (mBinder != null) {
            mBinder.unbind();
        }

        super.onDestroy();
    }
}
