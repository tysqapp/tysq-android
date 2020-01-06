package com.tysq.ty_android.feature.notification;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.abc.lib_utils.toast.ToastUtils;
import com.tysq.ty_android.R;
import com.tysq.ty_android.app.TyApplication;
import com.tysq.ty_android.base.SimpleLoadMoreFragment;
import com.tysq.ty_android.feature.notification.di.DaggerNotificationComponent;
import com.tysq.ty_android.feature.notification.di.NotificationModule;
import com.tysq.ty_android.feature.notification.listener.OnClickNotificationRead;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.Override;

import eventbus.NotificationUpdateEvent;
import response.notification.NotificationReadedResp;
import response.notification.NotificationResp;
import response.common.TitleCountVO;
import response.notification.NotificationResp;
import response.notification.NotifyInfoResp;

/**
 * author       : liaozhenlin
 * time         : 2019/11/14 0014 9:25
 * desc         : 通知
 * version      : 1.4.0
 */

public final class NotificationFragment
        extends SimpleLoadMoreFragment<NotificationPresenter, NotificationResp>
        implements NotificationView, OnClickNotificationRead {

    private final TitleCountVO mTitleCountVO = new TitleCountVO();

    private int notifyCount;

    public static NotificationFragment newInstance() {

        Bundle args = new Bundle();

        NotificationFragment fragment = new NotificationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void registerDagger() {
        DaggerNotificationComponent.builder()
                .appComponent(TyApplication.getAppComponent())
                .notificationModule(new NotificationModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected void loadData(int start, int pageSize, boolean isFirst) {
        mPresenter.loadNotification(start, pageSize, isFirst);
        mPresenter.getNotifyUnReadCount();
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return new NotificationAdapter(this, getContext(), mTitleCountVO, mData, this);
    }

    @Override
    public int getTitleId() {
        return R.string.notification;
    }

    @Override
    public void onLoadNotification(NotifyInfoResp data, int totalSize, boolean isFirst) {
        mTitleCountVO.setCount(totalSize);
        onHandleResponseData(data.getNotificationResps(), isFirst);
    }

    @Override
    public void onPutNotificationRead() {
    }

    @Override
    public void onPutNotificationAllRead(NotificationReadedResp value) {
        if (!value.isUnRead()){
            ToastUtils.show(getString(R.string.notification_all_no_read));
            return;
        }

        //全部标记为已读
        for (int i = 0; i < mData.size(); i++){
            mData.get(i).setRead(true);
        }

        mBaseAdapter.notifyDataSetChanged();

        EventBus.getDefault().post(new NotificationUpdateEvent(0));
        ToastUtils.show(getString(R.string.notification_all_read));
    }

    @Override
    public void onGetNotifyUnCountRead(int count) {
        notifyCount = count;
    }

    @Override
    protected int getEmptyView() {
        return R.layout.blank_empty_data_log;
    }

    @Override
    protected boolean isNeedEmpty() {
        return true;
    }

    @Override
    public void onClickNotificationRead(String notifyId) {
        mPresenter.putNotificationRead(notifyId);
    }

    @Override
    public void onClickNotificationAllRead() {
        mPresenter.putNotificationAllRead();
    }

}
