package com.tysq.ty_android.feature.setting;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.tysq.ty_android.R;
import com.tysq.ty_android.base.activity.CommonBarActivity;
import com.tysq.ty_android.feature.setting.resetPwd.ResetPwdFragment;

/**
 * author       : frog
 * time         : 2019/5/16 下午2:44
 * desc         : 设置
 * version      : 1.3.0
 */
public class SettingActivity extends CommonBarActivity {

    public static final int SETTING = 1;
    public static final int RESET = 2;

    public static final String TYPE = "TYPE";

    public int mType;

    public static void startActivity(Context context, int type) {
        Intent intent = new Intent(context, SettingActivity.class);

        intent.putExtra(TYPE, type);

        context.startActivity(intent);
    }

    @Override
    protected void initIntent(Intent intent) {
        mType = intent.getIntExtra(TYPE, SETTING);
    }

    @Override
    protected int getPageTitle() {
        int title;
        switch (mType) {
            case SETTING:
                title = R.string.setting_title;
                break;
            case RESET:
                title = R.string.reset_pwd_title;
                break;
            default:
                title = R.string.setting_title;
                break;
        }
        return title;
    }

    @Override
    protected Fragment getContentFragment() {
        Fragment fragment;
        switch (mType) {
            case SETTING:
                fragment = SettingFragment.newInstance();
                break;
            case RESET:
                fragment = ResetPwdFragment.newInstance();
                break;
            default:
                fragment = SettingFragment.newInstance();
                break;
        }
        return fragment;
    }
}
