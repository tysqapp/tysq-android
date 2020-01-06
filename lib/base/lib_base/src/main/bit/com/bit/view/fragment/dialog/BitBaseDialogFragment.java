package com.bit.view.fragment.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.bit.R;
import com.bit.config.BitManager;
import com.bit.utils.FragmentCompat;

import java.util.List;

/**
 * author       : frog
 * time         : 2019/3/26 下午3:19
 * desc         : 对话框
 * version      : 1.3.0
 */

public abstract class BitBaseDialogFragment extends AppCompatDialogFragment {

    public static final int CLOSE = -2;
    public static final int NONE = -1;

    private final String TAG = Integer.toHexString(System.identityHashCode(this));
    private Bundle mSavedInstanceState;
    private int mAnim = NONE;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Bundle args = getArguments();
        if (args != null) {
            this.initArgs(args);
        }
    }

    /**
     * 初始化参数
     */
    protected abstract void initArgs(Bundle arguments);

    /**
     * 创建自己的布局
     *
     * @return dialog 的布局
     */
    protected abstract View onCreateFragmentView(LayoutInflater inflater,
                                                 @Nullable ViewGroup container,
                                                 @Nullable Bundle savedInstanceState);

    /**
     * 初始化控件
     */
    protected abstract void initView(View view);

    /**
     * 初始化窗口
     *
     * @param window
     */
    protected void initWindows(Window window) {

        if (this.mAnim != CLOSE) {

            if (this.mAnim == NONE) { // 默认动画
                if (BitManager.getInstance().getDialogAnim() != NONE) {
                    window.getAttributes().windowAnimations =
                            BitManager.getInstance().getDialogAnim();
                }
            } else {    // 自行设置动画
                window.getAttributes().windowAnimations = this.mAnim;
            }
        }

    }

    /**
     * 显示对话框
     *
     * @param activity 所依赖的 Activity
     */
    public BitBaseDialogFragment show(FragmentActivity activity) {
        showWithAllow(activity);
        return this;
    }

    public BitBaseDialogFragment showWithStatus(FragmentActivity activity) {
        show(activity.getSupportFragmentManager(), TAG);
        return this;
    }

    /**
     * 显示对话框
     *
     * @param fragment 所依赖的 Fragment
     */
    public BitBaseDialogFragment show(Fragment fragment) {
        showWithAllow(fragment);
        return this;
    }

    /**
     * 是否显示
     *
     * @return true: 正在显示; false: 未显示
     */
    public boolean isShowing() {
        return getDialog() != null && getDialog().isShowing();
    }

    /**
     * 显示对话框
     */
    public BitBaseDialogFragment showWithAllow(FragmentActivity activity) {
        FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
        ft.add(this, TAG);
        ft.commitAllowingStateLoss();
        return this;
    }

    /**
     * 显示对话框
     */
    public BitBaseDialogFragment showWithAllow(Fragment fragment) {
        FragmentTransaction ft = fragment.getChildFragmentManager().beginTransaction();
        ft.add(this, TAG);
        ft.commitAllowingStateLoss();
        return this;
    }

    public void addLayerFragment(int containerId, List<Fragment> fragments) {
        addLayerFragment(containerId, fragments, 0);
    }

    public void addLayerFragment(int containerId,
                                 List<Fragment> fragments,
                                 int showPosition) {
        if (mSavedInstanceState == null) {
            FragmentCompat.Layer.init(getChildFragmentManager(), containerId, showPosition, fragments);
        } else {
            FragmentCompat.Layer.restoreInstance(getChildFragmentManager(), fragments);
        }
    }

    public void toggleLayerFragment(Fragment from,
                                    Fragment to) {
        FragmentCompat.Layer.toggle(getChildFragmentManager(), from, to);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.BitFrameDialog);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return this.onCreateFragmentView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.mSavedInstanceState = savedInstanceState;
        initWindows(getDialog().getWindow());
        initView(view);
    }

    /**
     * 设置动画
     *
     * @param animSource
     */
    public void setAnim(int animSource) {
        this.mAnim = animSource;
    }

    /**
     * 关闭动画
     */
    public void closeAnim() {
        this.mAnim = CLOSE;
    }

}
