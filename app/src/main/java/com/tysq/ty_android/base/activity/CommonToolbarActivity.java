package com.tysq.ty_android.base.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bit.view.activity.BitBaseActivity;
import com.tysq.ty_android.R;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import butterknife.BindView;

/**
 * author       : frog
 * time         : 2019-07-15 17:46
 * desc         : 带返回按钮、标题、Fragment装载容器
 * version      : 1.3.0
 */
public class CommonToolbarActivity extends BitBaseActivity {

    private static final String TYPE = "type";


    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_confirm)
    TextView tvConfirm;
    @BindView(R.id.frame_layout_container)
    FrameLayout frameLayoutContainer;

    private String fragmentType;

    public static void startActivity(Context context,
                                     String fragmentType) {
        Intent intent = new Intent(context, CommonToolbarActivity.class);

        intent.putExtra(TYPE, fragmentType);

        context.startActivity(intent);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_common_toolbar;
    }

    @Override
    protected void initIntent(Intent intent) {
        fragmentType = intent.getStringExtra(TYPE);
    }

    @Override
    protected void initView() {
        ivBack.setOnClickListener(v -> finish());

        Class<? extends ICommonFragment> fragmentClass
                = FragmentConstant.FRAGMENT_INFO.get(fragmentType);

        if (fragmentClass == null) {
            Log.e(TAG, "FragmentClass is NULL !!!");
            return;
        }

        Fragment fragment = null;

        try {
            Method method = fragmentClass.getMethod("newInstance");
            fragment = (Fragment) method.invoke(null);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        if (fragment == null) {
            Log.e(TAG, "Fragment is NULL !!!");
            return;
        }

        if (!(fragment instanceof ICommonFragment)) {
            Log.e(TAG, "Fragment is not implement ICommonFragment !!!");
            return;
        }

        ICommonFragment iCommonFragment = (ICommonFragment) fragment;
        // 设置标题
        tvTitle.setText(getString(iCommonFragment.getTitleId()));

        // 添加内容
        addLayerFragment(frameLayoutContainer.getId(), fragment);

    }

    public interface ICommonFragment {

        int getId();

        int getTitleId();

    }

}
