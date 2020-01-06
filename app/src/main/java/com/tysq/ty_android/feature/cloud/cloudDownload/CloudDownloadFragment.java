package com.tysq.ty_android.feature.cloud.cloudDownload;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import com.abc.lib_multi_download.config.DownloadConfig;
import com.abc.lib_multi_download.listener.SuccessListener;
import com.abc.lib_multi_download.model.DownloadInfo;
import com.abc.lib_multi_download.model.status.RunningStatus;
import com.bit.view.fragment.BitListFragment;
import com.tysq.ty_android.R;
import com.tysq.ty_android.app.TyApplication;
import com.tysq.ty_android.feature.cloud.cloudDownload.adapter.CloudDownloadAdapter;
import com.tysq.ty_android.feature.cloud.cloudDownload.di.CloudDownloadModule;
import com.tysq.ty_android.feature.cloud.cloudDownload.di.DaggerCloudDownloadComponent;
import com.tysq.ty_android.feature.cloud.listener.CloudListener;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import eventbus.DownloadAddEvent;
import vo.cloud.CloudDownloadVO;

/**
 * author       : frog
 * time         : 2019-10-18 09:38
 * desc         : 云盘下载
 * version      : 1.0.0
 */

public final class CloudDownloadFragment
        extends BitListFragment<CloudDownloadPresenter>
        implements CloudDownloadView, CloudDownloadAdapter.Listener, SuccessListener {

    private final List<CloudDownloadVO> mData = new ArrayList<>();

    public static CloudDownloadFragment newInstance() {

        Bundle args = new Bundle();

        CloudDownloadFragment fragment = new CloudDownloadFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void registerDagger() {
        DaggerCloudDownloadComponent.builder()
                .appComponent(TyApplication.getAppComponent())
                .cloudDownloadModule(new CloudDownloadModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected boolean isLazyLoad() {
        return true;
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return new CloudDownloadAdapter(this, getContext(), mData, this);
    }

    @Override
    protected boolean requestRefresh() {
        return true;
    }

    @Override
    public void getFirstData(int type) {
        mBaseAdapter.onLoading();
        mPresenter.getDownloadInfo();
    }

    @Override
    public void onLoadError() {
        mBaseAdapter.onError();
    }

    @Override
    public void onLoadInfo(List<CloudDownloadVO> value, int count) {
        mData.clear();
        mData.addAll(value);

        if (getActivity() != null && getActivity() instanceof CloudListener) {
            CloudListener listener = (CloudListener) getActivity();
            listener.setDownloadInfo(count);
        }

        if (mData.size() <= 0) {
            mBaseAdapter.onEmpty();
            return;
        }

        mBaseAdapter.onSuccess();
        mBaseAdapter.notifyDataSetChanged();
    }

    @Override
    protected int getEmptyView() {
        return R.layout.blank_no_download;
    }

    @Override
    public void addFinishItem(long finishId) {

        int finishIndex = -1;

        for (int i = 0; i < mData.size(); i++) {
            CloudDownloadVO cloudDownloadVO = mData.get(i);
            if (cloudDownloadVO.getDownloadInfo() != null
                    && cloudDownloadVO.getDownloadInfo().getId() == finishId) {
                finishIndex = i;
                break;
            }
        }

        if (finishIndex == -1) {
            return;
        }

        // 移除原来的数据
        CloudDownloadVO finishData = mData.remove(finishIndex);
        mBaseAdapter.notifyItemRemoved(getRealPos(finishIndex));

        // 寻找完成头部
        int titlePos = finishIndex;
        for (; titlePos < mData.size(); ++titlePos) {
            if (mData.get(titlePos).getType() == CloudDownloadVO.TYPE.HEAD) {
                break;
            }
        }

        // 说明没有已经完成的，需要添加头部
        if (titlePos == mData.size()) {
            CloudDownloadVO headVO = new CloudDownloadVO(CloudDownloadVO.TYPE.HEAD);
            headVO.setSize(1);
            mData.add(headVO);
            mBaseAdapter.notifyItemInserted(getRealPos(titlePos));
        } else {
            CloudDownloadVO headVO = mData.get(titlePos);
            headVO.setSize(headVO.getSize() + 1);
            mBaseAdapter.notifyItemChanged(getRealPos(titlePos));
        }

        int finData = titlePos + 1;
        mData.add(finData, finishData);
        mBaseAdapter.notifyItemInserted(getRealPos(finData));
    }

    private int getRealPos(int pos) {
        return pos + (requestRefresh() ? 1 : 0);
    }

    @Override
    public void delete(DownloadInfo downloadInfo) {
        int pos = -1;
        for (int i = 0; i < mData.size(); i++) {
            CloudDownloadVO cloudDownloadVO = mData.get(i);
            if (cloudDownloadVO.getType() == CloudDownloadVO.TYPE.HEAD) {
                continue;
            }

            DownloadInfo curDownloadInfo = cloudDownloadVO.getDownloadInfo();
            if (curDownloadInfo.getId() == downloadInfo.getId()) {
                pos = i;
                break;
            }
        }

        if (pos == -1) {
            return;
        }

        CloudDownloadVO removeVO = mData.remove(pos);
        mBaseAdapter.notifyItemRemoved(getRealPos(pos));

        // 如果是下载完成的项，则需要进行减少
        if (removeVO.getType() == CloudDownloadVO.TYPE.CONTENT
                && removeVO.getDownloadInfo().getRunningStatus() == RunningStatus.SUCCESS
        ) {

            int totalSize = mData.size();

            for (int i = 0; i < mData.size(); i++) {
                CloudDownloadVO curItem = mData.get(i);

                if (curItem.getType() == CloudDownloadVO.TYPE.HEAD) {
                    int size = curItem.getSize();

                    totalSize -= 1;

                    if (size - 1 <= 0) {
                        mData.remove(i);
                        mBaseAdapter.notifyItemRemoved(getRealPos(i));
                    } else {
                        curItem.setSize(size - 1);
                        mBaseAdapter.notifyItemChanged(getRealPos(i));
                    }

                    break;
                }
            }

            if (getActivity() != null && getActivity() instanceof CloudListener) {
                CloudListener listener = (CloudListener) getActivity();
                listener.setDownloadInfo(totalSize);
            }

        } else {
            if (getActivity() != null && getActivity() instanceof CloudListener) {

                CloudListener listener = (CloudListener) getActivity();

                int totalSize = mData.size();

                for (int i = 0; i < mData.size(); i++) {
                    CloudDownloadVO curItem = mData.get(i);

                    if (curItem.getType() == CloudDownloadVO.TYPE.HEAD) {
                        totalSize -= 1;
                        break;
                    }
                }

                listener.setDownloadInfo(totalSize);
            }
        }

        // 数据为0时，进行重新刷新
        if (mData.size() <= 0) {
            getFirstData(LoadType.CUSTOM);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void addItem(DownloadAddEvent event) {
        for (CloudDownloadVO item : mData) {
            DownloadInfo downloadInfo = item.getDownloadInfo();
            if (downloadInfo == null) {
                continue;
            }
            downloadInfo.removeListener();
        }

        getFirstData(LoadType.CUSTOM);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DownloadConfig.getInstance().addListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        DownloadConfig.getInstance().removeListener(this);
    }

    @Override
    public void onSuccess(DownloadInfo downloadInfo) {
        addFinishItem(downloadInfo.getId());
    }
}
