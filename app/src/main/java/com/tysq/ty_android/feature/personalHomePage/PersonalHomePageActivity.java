package com.tysq.ty_android.feature.personalHomePage;

import android.content.Context;
import android.content.Intent;

import com.bit.view.activity.BitBaseActivity;
import com.tysq.ty_android.local.sp.UserCache;

/**
 * author       : frog
 * time         : 2019-09-17 15:28
 * desc         : 个人主页
 * version      : 1.5.0
 */
public class PersonalHomePageActivity extends BitBaseActivity {

    private final static String USER_ID = "USER_ID";
    private final static int USER_ERROR = -1;


    private int userId;

    public static void startActivity(Context context, int userId) {
        Intent intent = new Intent(context, PersonalHomePageActivity.class);

        intent.putExtra(USER_ID, userId);

        context.startActivity(intent);
    }

    @Override
    protected int getLayout() {
        return COMMON_FRAME_LAYOUT;
    }

    @Override
    protected void initIntent(Intent intent) {
        userId = intent.getIntExtra(USER_ID, USER_ERROR);
    }

    @Override
    protected void initView() {
        addLayerFragment(ID_FRAME_LAYOUT_CONTAINER, PersonalHomePageFragment.newInstance(userId));
    }
}
