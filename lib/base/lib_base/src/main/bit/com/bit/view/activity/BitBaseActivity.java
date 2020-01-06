package com.bit.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;

import com.bit.R;
import com.bit.event.CloseEvent;
import com.bit.event.ReloadEvent;
import com.bit.presenter.BasePresenter;
import com.bit.utils.FragmentCompat;
import com.bit.utils.StatusUtils;
import com.bit.view.fragment.dialog.BitCommonLoadingFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * author       : frog
 * time         : 2019/3/25 下午5:18
 * desc         : 所有的 Activity 的基类
 * version      : 1.3.0
 */
public abstract class BitBaseActivity<T extends BasePresenter> extends AppCompatActivity {

    /**
     * ButterKnife 标记位
     */
    protected static final int BUTTER_KNIFE = 0x001;
    /**
     * EventBus 标记位
     */
    protected static final int EVENT_BUS = 0x002;

    /**
     * 装载 FrameLayout 的布局容器
     */
    protected final static int COMMON_FRAME_LAYOUT = R.layout.bit_common_frame_layout;
    /**
     * 装载 JRecycleView 的布局容器
     */
    protected final static int COMMON_RECYCLE_VIEW_LAYOUT = R.layout.bit_common_recycle_view;
    /**
     * 装载 FrameLayout 的布局容器中的 FrameLayout 的 id
     */
    protected final static int ID_FRAME_LAYOUT_CONTAINER = R.id.frame_layout_container;
    /**
     * 装载 JRecycleView 的布局容器中的 JRecycleView 的 id
     */
    protected final static int ID_RECYCLE_VIEW = R.id.recycle_view;


    protected final String TAG = this.getClass().getSimpleName();

    /**
     * 当前存活的activity， 用于关闭所有的activity
     */
    private static final LinkedList<Activity> EXIST_ACTIVITIES = new LinkedList<>();

    protected Bundle mSavedInstanceState;

    private Unbinder unbinder;

    @Inject
    @Nullable
    protected T mPresenter;

    /**
     * 表示无布局文件
     */
    protected static final int NONE = -1;

    private BitCommonLoadingFragment mLoadingFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mSavedInstanceState = savedInstanceState;

        EXIST_ACTIVITIES.add(this);

        onCreate();
    }

    protected void onCreate(){

        setStatusBarColor(R.color.tyStateColor);

        initContentView();

        registerDagger();
        registerLogic();

//        StatusBarUtil.setLightMode(this);

        initIntent(getIntent());
        initView();

    }

    protected void initContentView() {
        // 为 NONE 时，不进行设置视图的 layout
        if (getLayout() != NONE) {
            setContentView(getLayout());
        }
    }

    /**
     * dagger注入
     */
    protected void registerDagger() {

    }

    @Override
    protected void onDestroy() {
        EXIST_ACTIVITIES.remove(this);

        unregisterLogic();

        // 释放 presenter
        if (mPresenter != null) {
            mPresenter.onDestroy();
        }
        mPresenter = null;

        super.onDestroy();
    }

    /**
     * 退出所有的存活 Activity
     */
    public void exit(Activity activity) {
        Iterator<Activity> activityIterator = EXIST_ACTIVITIES.iterator();
        while (activityIterator.hasNext()) {
            Activity next = activityIterator.next();
            activityIterator.remove();

            if (next != activity) {
                next.finish();
            }

        }
    }

    /**
     * 当前activity是否为用户所见的activity
     *
     * @return true 表示当前 Activity 是最后一个 Activity；false 则相反
     */
    private boolean isCurActivity() {
        return EXIST_ACTIVITIES.getLast() == this;
    }

    /**
     * 添加一个 Fragment
     *
     * @param containerId 容器的ID
     * @param fragment    需要显示的Fragment
     */
    public void addLayerFragment(int containerId, Fragment fragment) {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(fragment);
        addLayerFragment(containerId, fragments, 0);
    }

    /**
     * 添加一个列表的 Fragment，并且显示列表的第一个 Fragment
     *
     * @param containerId 容器的ID
     * @param fragments   加入容器的 Fragment 列表
     */
    public void addLayerFragment(int containerId, List<Fragment> fragments) {
        addLayerFragment(containerId, fragments, 0);
    }

    /**
     * 添加一个列表的 Fragment，并且显示列表的指定下标的 Fragment
     *
     * @param containerId  容器的ID
     * @param fragments    加入容器的 Fragment 列表
     * @param showPosition 显示的下标
     */
    public void addLayerFragment(int containerId, List<Fragment> fragments, int showPosition) {
        if (fragments == null) {
            fragments = new ArrayList<>();
        }
        if (mSavedInstanceState == null) {
            FragmentCompat.Layer.init(getSupportFragmentManager(), containerId, showPosition, fragments);
        } else {
            FragmentCompat.Layer.restoreInstance(getSupportFragmentManager(), fragments);
        }
    }

    /**
     * 切换fragment
     *
     * @param from 当前显示的 Fragment
     * @param to   要切换至的 Fragment
     */
    public void toggleLayerFragment(Fragment from, Fragment to) {
        FragmentCompat.Layer.toggle(getSupportFragmentManager(), from, to);
    }

    /**
     * 获取视图layout的id
     *
     * @return 布局文件，可能为 {@link BitBaseActivity#NONE} 表示 BitBaseActivity 无需设置布局
     */
    protected abstract int getLayout();

    /**
     * 初始化intent
     */
    protected abstract void initIntent(Intent intent);

    /**
     * 初始化视图的View
     */
    protected abstract void initView();


    //====================是否需要butterKnife=====================

    /**
     * 返回需要注册的类型，默认注册 ButterKnife 、 EventBus
     *
     * @return 用或（|）连接的值；如果只需要ButterKnife，则只返回{@link #BUTTER_KNIFE}
     */
    protected int getInitRegister() {
        return BUTTER_KNIFE | EVENT_BUS;
    }

    /**
     * 注册一些第三方框架
     */
    protected void registerLogic() {

        // 注册 ButterKnife
        if (isContain(BUTTER_KNIFE)) {
            unbinder = ButterKnife.bind(this);
        }

        // 注册 EventBus
        if (isContain(EVENT_BUS)) {
            EventBus.getDefault().register(this);
        }

    }

    /**
     * 注销第三方框架
     */
    private void unregisterLogic() {

        // 注册 EventBus
        if (isContain(EVENT_BUS)) {
            EventBus.getDefault().unregister(this);
        }

        if (isContain(BUTTER_KNIFE) && unbinder != null) {
            unbinder.unbind();
        }

    }

    /**
     * 确认是否含有某个标记
     *
     * @param tag 标记
     * @return true：包含；false：不包含
     */
    private boolean isContain(int tag) {
        return (getInitRegister() & tag) == tag;
    }

    //=======================状态栏控制===========================

    protected void setStatusBarColor(int color) {
        setStatusBarColor(color, false);
    }

    /**
     * 设置状态栏颜色
     */
    protected void setStatusBarColor(int color, boolean isRealColor) {
        setStatusBarColor(color, isRealColor, false);
    }

    protected void setStatusBarColor(int color,
                                     boolean isRealColor,
                                     boolean isNeedAlpha) {
        // 5.0 以前，不做处理
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return;
        }

        Window window = getWindow();
        int realColor = isRealColor ? color : ContextCompat.getColor(this, color);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            StatusUtils.setStatusBar(this, getObscuredColor(realColor));
            return;
        }

        if (isNeedAlpha) {
            StatusUtils.setStatusBar(this, getObscuredColor(realColor));
            //window.setStatusBarColor(getObscuredColor(realColor));
        } else {
            //window.setStatusBarColor(realColor);
            StatusUtils.setStatusBar(this, realColor);
        }
    }

    /**
     * 加灰度
     *
     * @param c 设置的色值
     * @return 加灰度后的色值
     */
    protected int getObscuredColor(int c) {
        float[] hsv = new float[3];
        int color = c;
        Color.colorToHSV(color, hsv);
        hsv[2] *= 0.85f; // value component
        color = Color.HSVToColor(hsv);
        return color;
    }

    //===========================================================


    @Override
    public void onBackPressed() {

        boolean isConsume = FragmentCompat.onBackPressed(this);

        if (isConsume) {
            return;
        }

        super.onBackPressed();
    }

    //======================Loading 的 Fragment==================

    public void showDialog() {
        if (mLoadingFragment == null) {
            mLoadingFragment = new BitCommonLoadingFragment();
        }
        mLoadingFragment.show(this);
    }

    public void hideDialog() {
        if (mLoadingFragment != null) {
            mLoadingFragment.dismissAllowingStateLoss();
        }
    }
    //===========================================================

    /**
     * 进行重新刷新加载
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReload(ReloadEvent reloadEvent) {

    }

    /**
     * 进行关闭页面
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onClose(CloseEvent closeEvent) {
        if (isSameView(closeEvent.getViewId())) {
            finish();
        }
    }

    protected boolean isSameView(String viewId) {
        return viewId.equals(this.toString());
    }

    protected void setFullScreen(){
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
