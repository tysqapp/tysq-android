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

import static utils.UploadUtils.getString;

/**
 * author       : liaozhenlin
 * time         : 2019-07-15 17:46
 * desc         : 带返回按钮、标题、完成的Fragment装载容器，可以对完成按钮点击事件进行重写和改变按钮的背景和颜色
 * version      : 1.4.0
 */
public class CommonToolbarStrengthenActivity extends BitBaseActivity {

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
        Intent intent = new Intent(context, CommonToolbarStrengthenActivity.class);

        intent.putExtra(TYPE, fragmentType);

        context.startActivity(intent);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_common_toolbar_strengthen;
    }

    @Override
    protected void initIntent(Intent intent) {
        fragmentType = intent.getStringExtra(TYPE);
    }

    @Override
    protected void initView() {
        ivBack.setOnClickListener(v -> finish());

        Class<? extends ICommonFragment> fragmentClass
                = FragmentStrengthenConstant.FRAGMENT_INFO.get(fragmentType);

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

        //设置按钮
        if (iCommonFragment.getConfirmId() != 0){
            tvConfirm.setText(getString(iCommonFragment.getConfirmId()));
            tvConfirm.setVisibility(View.VISIBLE);
        }else{
            tvConfirm.setVisibility(View.GONE);
        }

        //设置按钮背景
        if (iCommonFragment.getConfirmBackground() != 0){
            tvConfirm.setBackgroundResource(iCommonFragment.getConfirmBackground());
        }

        //设置按钮的字体颜色
        if (iCommonFragment.getConfirmTextColor() != 0){
            tvConfirm.setTextColor(this.getResources().getColor(iCommonFragment.getConfirmTextColor()));
        }

        //设置按钮点击后所需要执行的方法
        tvConfirm.setOnClickListener(v ->
            iCommonFragment.setConfirmClick()
        );
        // 添加内容
        addLayerFragment(frameLayoutContainer.getId(), fragment);

    }


    public interface ICommonFragment {

        int getId();

        int getTitleId();

        //获取是否显示确认按钮
        int getConfirmId();

        //重写确认按钮的点击方法
        void setConfirmClick();

        //改变确认按钮的背景
        int getConfirmBackground();

        //改变按钮文本的颜色
        int getConfirmTextColor();
    }

}

