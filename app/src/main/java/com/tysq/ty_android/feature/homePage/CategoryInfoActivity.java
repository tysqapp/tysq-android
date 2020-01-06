package com.tysq.ty_android.feature.homePage;

import android.content.Context;
import android.content.Intent;

import com.bit.view.activity.BitBaseActivity;

import java.util.ArrayList;

import response.home.TopCategory;

/**
 * author       : frog
 * time         : 2019/4/28 下午4:10
 * desc         : 用于放置 fragment 的 activity
 * version      : 1.3.0
 */
public class CategoryInfoActivity extends BitBaseActivity {

    public static final String CATEGORY_INFO = "CATEGORY_INFO";
    public static final int ERROR_ID = -1;
    public static final String TOP_ID = "topId";
    public static final String SUB_ID = "subId";

    private ArrayList<TopCategory> mCategoryData;
    private int mSelTopId;
    private int mSelSubId;

    public static void startActivity(Context context,
                                     ArrayList<TopCategory> categoryData,
                                     int topId,
                                     int subId) {
        Intent intent = new Intent(context, CategoryInfoActivity.class);

        intent.putParcelableArrayListExtra(CATEGORY_INFO, categoryData);
        intent.putExtra(TOP_ID, topId);
        intent.putExtra(SUB_ID, subId);

        context.startActivity(intent);
    }

    @Override
    protected int getLayout() {
        return COMMON_FRAME_LAYOUT;
    }

    @Override
    protected void initIntent(Intent intent) {
        mCategoryData = intent.getParcelableArrayListExtra(CATEGORY_INFO);
        mSelTopId = intent.getIntExtra(TOP_ID, ERROR_ID);
        mSelSubId = intent.getIntExtra(SUB_ID, ERROR_ID);
    }

    @Override
    protected void initView() {
        addLayerFragment(ID_FRAME_LAYOUT_CONTAINER,
                CategoryInfoFragment.newInstance(
                        mCategoryData,
                        mSelTopId,
                        mSelSubId));

    }

}
