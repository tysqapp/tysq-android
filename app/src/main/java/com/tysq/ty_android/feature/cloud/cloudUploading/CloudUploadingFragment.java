package com.tysq.ty_android.feature.cloud.cloudUploading;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.bit.config.BitManager;
import com.bit.view.fragment.BitListFragment;
import com.tysq.ty_android.R;
import com.tysq.ty_android.app.TyApplication;
import com.tysq.ty_android.feature.cloud.cloudUploading.adapter.CloudUploadingAdapter;
import com.tysq.ty_android.feature.cloud.cloudUploading.di.CloudUploadingModule;
import com.tysq.ty_android.feature.cloud.cloudUploading.di.DaggerCloudUploadingComponent;
import com.tysq.ty_android.feature.cloud.listener.CloudListener;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.Override;
import java.util.ArrayList;
import java.util.List;

import eventbus.UploadAddTaskEvent;
import factory.UploadFactory;
import model.FileModel;
import model.UploadUpdateEvent;

/**
 * author       : frog
 * time         : 2019/6/18 上午9:49
 * desc         : 网盘上传
 * version      : 1.3.0
 */
public final class CloudUploadingFragment
        extends BitListFragment<CloudUploadingPresenter>
        implements CloudUploadingView, CloudUploadingAdapter.Listener {

    private final List<FileModel> mData = new ArrayList<>();

    public static CloudUploadingFragment newInstance() {
        Bundle args = new Bundle();

        CloudUploadingFragment fragment = new CloudUploadingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void registerDagger() {
        DaggerCloudUploadingComponent.builder()
                .appComponent(TyApplication.getAppComponent())
                .cloudUploadingModule(new CloudUploadingModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return new CloudUploadingAdapter(getContext(), mData, this);
    }

    @Override
    protected boolean isLazyLoad() {
        return false;
    }

    @Override
    public void getFirstData(int type) {
        mData.clear();

        mData.addAll(UploadFactory.getUploadData());

        if (mData.size() > 0) {
            mBaseAdapter.onSuccess();
        } else {
            mBaseAdapter.onEmpty();
        }

        updateCount();
    }

    @Override
    protected boolean requestRefresh() {
        return false;
    }

    @Override
    protected int getEmptyView() {
        return R.layout.blank_no_upload;
    }

    @Override
    public void deleteItem(int pos) {
        FileModel removeFileModel = mData.remove(pos);

        if (removeFileModel == null) {
            return;
        }
        UploadFactory.removeModel(removeFileModel);

        mBaseAdapter.notifyItemRemoved(pos);

        if (mData.size() <= 0) {
            mBaseAdapter.onEmpty();
        }

        updateCount();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public synchronized void removeItem(UploadUpdateEvent event) {

        int index = -1;

        for (int i = 0; i < mData.size(); i++) {
            FileModel fileModel = mData.get(i);

            // 如果列表中已经有id是相同的，则不添加
            if (fileModel.getUrl().equals(event.getFileModel().getUrl())
                    && fileModel.getFilename().equals(event.getFileModel().getFilename())) {
                index = i;
            }
        }

        if (index == -1) {
            return;
        }

        mData.remove(index);

        mBaseAdapter.notifyItemRemoved(index);
        if (mData.size() == 0) {
            mBaseAdapter.onEmpty();
        }

        updateCount();
    }

    @Override
    protected int getInitRegister() {
        return BUTTER_KNIFE | EVENT_BUS;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public synchronized void onSubmitTask(UploadAddTaskEvent event) {
        mData.add(0, event.getFileModel());

        if (mData.size() == 1) {
            mBaseAdapter.onSuccess();
        }

        mBaseAdapter.notifyItemInserted(0);
        updateCount();
    }

    private void updateCount() {
        if (getActivity() != null && getActivity() instanceof CloudListener) {
            CloudListener listener = (CloudListener) getActivity();
            listener.setUploadingInfo(mData.size());
        }
    }

    @Override
    protected int getEmptyBtnId() {
        return BitManager.NONE;
    }
}
