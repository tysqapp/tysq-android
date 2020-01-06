package com.tysq.ty_android.feature.homePage;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.bit.utils.UIUtils;
import com.tysq.ty_android.R;
import com.tysq.ty_android.base.CommonBaseDialog;
import com.tysq.ty_android.feature.announcement.AnnouncementAdapter;

import java.util.ArrayList;
import java.util.List;

import response.AdResp;

/**
 * author       : frog
 * time         : 2019-07-17 11:47
 * desc         : 公告弹框
 * version      : 1.3.0
 */
public class AnnouncementDialog extends CommonBaseDialog {

    private static final String DATA_SIZE = "DATA_SIZE";

    private int size = 0;

    private final List<AdResp.AdvertisementListBean> mData = new ArrayList<>();

    public static AnnouncementDialog newInstance(int dataSize) {

        Bundle args = new Bundle();

        args.putInt(DATA_SIZE, dataSize);

        AnnouncementDialog fragment = new AnnouncementDialog();
        fragment.setArguments(args);
        return fragment;

    }

    public void setData(List<AdResp.AdvertisementListBean> data) {
        mData.clear();
        mData.addAll(data);
    }

    @Override
    protected void initArgs(Bundle arguments) {
        size = arguments.getInt(DATA_SIZE);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.dialog_announcement;
    }

    @Override
    protected int obtainWidth() {
        return ViewGroup.LayoutParams.MATCH_PARENT;
    }

    @Override
    protected int obtainHeight() {
        int showSize = size;
        if (size > 5) {
            showSize = 5;
        }

        int itemHeight = dp2px(75);
        int topHeight = dp2px(67);
        return topHeight + itemHeight * showSize;
    }

    @Override
    protected int obtainGravity() {
        return Gravity.CENTER;
    }

    @Override
    protected void initView(View view) {

        view.findViewById(R.id.iv_close).setOnClickListener(v -> {
            dismiss();
        });

        RecyclerView recycleView = view.findViewById(R.id.recycle_view);

        AnnouncementAdapter announcementAdapter
                = new AnnouncementAdapter(getContext(), mData);

        recycleView.setAdapter(announcementAdapter);
        recycleView.setLayoutManager(new LinearLayoutManager(getContext()));

    }

}
