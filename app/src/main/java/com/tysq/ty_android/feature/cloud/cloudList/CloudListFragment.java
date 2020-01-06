package com.tysq.ty_android.feature.cloud.cloudList;

import android.Manifest;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.abc.lib_utils.toast.ToastUtils;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bit.view.fragment.BitLoadMoreFragment;
import com.tysq.ty_android.R;
import com.tysq.ty_android.app.TyApplication;
import com.tysq.ty_android.feature.cloud.cloudList.adapter.CloudListAdapter;
import com.tysq.ty_android.feature.cloud.cloudList.di.CloudListModule;
import com.tysq.ty_android.feature.cloud.cloudList.di.DaggerCloudListComponent;
import com.tysq.ty_android.feature.cloud.listener.CloudListListener;
import com.tysq.ty_android.feature.cloud.listener.CloudListener;
import com.tysq.ty_android.feature.cloudChoose.CloudChooseActivity;
import com.zinc.libpermission.annotation.Permission;
import com.zinc.libpermission.annotation.PermissionCanceled;
import com.zinc.libpermission.annotation.PermissionDenied;
import com.zinc.libpermission.bean.CancelInfo;
import com.zinc.libpermission.bean.DenyInfo;
import com.zinc.libpermission.utils.JPermissionUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import eventbus.DownloadAddEvent;
import eventbus.VideoCoverChangeEvent;
import model.UploadUpdateEvent;
import response.cloud.FileInfoResp;

/**
 * author       : frog
 * time         : 2019/6/5 下午2:39
 * desc         : 已上传的页面
 * version      : 1.3.0
 */
public final class CloudListFragment
        extends BitLoadMoreFragment<CloudListPresenter>
        implements CloudListView, CloudListListener {

    public static final String TAG = "CloudListFragment";

    private static final int LOAD_SIZE = 20;

    private final List<FileInfoResp.FileItem> mData = new ArrayList<>();
    private int mStart = 0;

    public static CloudListFragment newInstance() {
        Bundle args = new Bundle();

        CloudListFragment fragment = new CloudListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void getLoadMoreData() {
        loadData(false);
    }

    @Override
    protected boolean isLazyLoad() {
        return false;
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        CloudListAdapter cloudListAdapter = new CloudListAdapter(this, getContext(), mData);
        cloudListAdapter.setListener(this);
        return cloudListAdapter;
    }

    @Override
    public void getFirstData(int type) {
        mStart = 0;

        loadData(true);
    }

    private void loadData(boolean isFirst) {
        mPresenter.loadData(isFirst, mStart, LOAD_SIZE);
    }

    @Override
    public void onLoadDataError(boolean isFirst) {
        if (isFirst) {
            mBaseAdapter.onError();
        } else {
            mBaseAdapter.setLoadError();
        }
    }

    @Override
    public void onLoadData(boolean isFirst,
                           int totalNumber,
                           List<FileInfoResp.FileItem> fileInfo) {
        if (getActivity() != null && getActivity() instanceof CloudListener) {
            CloudListener listener = (CloudListener) getActivity();
            listener.setUploadedInfo(totalNumber);
        }

        if (fileInfo != null) {
            mStart += fileInfo.size();
        }

        if (isFirst) {
            mData.clear();
        }

        if (isFirst) {
            if (fileInfo == null || fileInfo.size() <= 0) {
                mBaseAdapter.onEmpty();
                return;
            }

            mData.addAll(fileInfo);

            if (fileInfo.size() < LOAD_SIZE) {
                mBaseAdapter.onSuccess();

                mBaseAdapter.setNoMore();
            } else {
                mBaseAdapter.onSuccess();

                mBaseAdapter.setLoadComplete();
            }

            return;
        }

        if (fileInfo == null) {
            mBaseAdapter.setNoMore();
        } else if (fileInfo.size() < LOAD_SIZE) {
            mData.addAll(fileInfo);
            mBaseAdapter.notifyDataSetChanged();
            mBaseAdapter.setNoMore();
        } else {
            mData.addAll(fileInfo);
            mBaseAdapter.notifyDataSetChanged();
            mBaseAdapter.setLoadComplete();
        }

    }

    @Override
    public void onDeleteFile(int fileId) {
        hideDialog();

        ToastUtils.show(getString(R.string.cloud_delete_success));

        int index = -1;
        for (int i = 0; i < mData.size(); i++) {
            if (fileId == mData.get(i).getId()) {
                index = i;
            }
        }
        if (index == -1) {
            return;
        }

        mData.remove(index);

        mBaseAdapter.notifyItemRemoved(index + 1);

        if (mData.size() <= 0) {
            mBaseAdapter.onEmpty();
        }
    }

    @Override
    public void onPutRename(int id, String filename) {
        hideDialog();

        ToastUtils.show(getString(R.string.cloud_rename_suc));

        int pos = -1;
        for (int i = 0; i < mData.size(); i++) {
            FileInfoResp.FileItem fileItem = mData.get(i);
            if (id == fileItem.getId()) {
                pos = i;
                break;
            }
        }

        if (pos == -1) {
            return;
        }

        FileInfoResp.FileItem fileItem = mData.get(pos);
        fileItem.setFilename(filename);

        pos = requestRefresh() ? pos + 1 : pos;

        mBaseAdapter.notifyItemChanged(pos);
    }

    @Override
    public void onDownloadError() {
        ToastUtils.show(getString(R.string.cloud_download_task_add_failure));
    }

    @Override
    public void onDownload() {
        EventBus.getDefault().post(new DownloadAddEvent());
        ToastUtils.show(getString(R.string.cloud_download_task_add_suc));
    }

    @Override
    protected int getEmptyView() {
        return R.layout.blank_no_file;
    }

    @Override
    protected void registerDagger() {
        DaggerCloudListComponent.builder()
                .appComponent(TyApplication.getAppComponent())
                .cloudListModule(new CloudListModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void deleteFile(int fileId) {

        new MaterialDialog.Builder(getContext())
                .content(R.string.cloud_delete_confirm)
                .positiveColor(ContextCompat.getColor(getContext(), R.color.main_blue_color))
                .positiveText(getContext().getString(R.string.common_confirm))
                .onPositive((dialog, which) -> {
                    showDialog();

                    mPresenter.deleteFile(fileId);
                })
                .negativeColor(ContextCompat.getColor(getContext(), R.color.et_tip_text_color))
                .negativeText(getContext().getString(R.string.common_cancel))
                .show();

    }

    @Override
    public void chooseCover(int id) {
        CloudChooseActivity.startActivity(getContext(), CloudListFragment.TAG,
                CloudChooseActivity.COVER, 1, id);
    }

    @Override
    @Permission(value = {Manifest.permission.WRITE_EXTERNAL_STORAGE}, requestCode = 200)
    public void downloadFile(int id,
                             String filename,
                             int accountId,
                             boolean isVideo,
                             String downloadUrl,
                             String cover) {
        mPresenter.download(filename, accountId, downloadUrl, cover);
    }

    @Override
    public void rename(int id) {
        new MaterialDialog.Builder(getContext())
                .customView(R.layout.widget_input_view, true)
                .positiveColor(ContextCompat.getColor(getContext(),
                        R.color.main_blue_color))
                .positiveText(getContext().getString(R.string.common_confirm))
                .onPositive((dialog, which) -> {

                    View customView = dialog.getCustomView();

                    if (customView == null) {
                        ToastUtils.show(getString(R.string.cloud_rename_enter));
                        return;
                    }

                    EditText editText = customView.findViewById(R.id.et_content);

                    if (editText == null) {
                        ToastUtils.show(getString(R.string.cloud_rename_enter));
                        return;
                    }

                    String filename = editText.getText().toString().trim();

                    if (TextUtils.isEmpty(filename)) {
                        ToastUtils.show(getString(R.string.cloud_rename_enter));
                        return;
                    }

                    showDialog();
                    mPresenter.putRename(id, filename);

                })
                .negativeColor(ContextCompat.getColor(getContext(),
                        R.color.et_tip_text_color))
                .negativeText(getContext().getString(R.string.common_cancel))
                .show();
    }

    @PermissionCanceled(requestCode = 200)
    private void cancel(CancelInfo cancelInfo) {
        ToastUtils.show(getString(R.string.cloud_download_need_permission));
    }

    @PermissionDenied(requestCode = 200)
    private void deny(DenyInfo denyInfo) {
        ToastUtils.show(getString(R.string.cloud_download_need_permission));
        JPermissionUtil.goToMenu(getContext());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void changeCover(VideoCoverChangeEvent event) {

        int index = -1;

        for (int i = 0; i < mData.size(); i++) {
            if (mData.get(i).getId() == event.getFileId()) {
                index = i;
                break;
            }
        }

        if (index == -1) {
            return;
        }

        ArrayList<FileInfoResp.CoverInfo> coverList = new ArrayList<>();
        FileInfoResp.CoverInfo coverInfo = new FileInfoResp.CoverInfo();
        coverInfo.setCoverUrl(event.getCoverUrl());
        coverInfo.setId(event.getCoverId());
        coverList.add(coverInfo);

        mData.get(index).setCoverList(coverList);
        mBaseAdapter.notifyItemChanged(index + 1);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void addItem(UploadUpdateEvent event) {
        for (FileInfoResp.FileItem fileItem : mData) {
            // 如果列表中已经有id是相同的，则不添加
            if (fileItem.getId() == event.getFileItem().getId()) {
                return;
            }
        }

        mStart = 0;
        loadData(true);
    }

    @Override
    protected int getInitRegister() {
        return BUTTER_KNIFE | EVENT_BUS;
    }

}
