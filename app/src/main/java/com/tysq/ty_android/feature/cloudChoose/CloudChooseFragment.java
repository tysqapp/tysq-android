package com.tysq.ty_android.feature.cloudChoose;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.abc.lib_utils.toast.ToastUtils;
import com.bit.adapter.BitFrameAdapter;
import com.bit.callback.StateViewHolderListener;
import com.bit.view.fragment.BitLoadMoreFragment;
import com.tysq.ty_android.R;
import com.tysq.ty_android.app.TyApplication;
import com.tysq.ty_android.feature.cloudChoose.adapter.CloudChooseAdapter;
import com.tysq.ty_android.feature.cloudChoose.di.CloudChooseModule;
import com.tysq.ty_android.feature.cloudChoose.di.DaggerCloudChooseComponent;
import com.tysq.ty_android.feature.cloudChoose.listener.AdapterListener;
import com.tysq.ty_android.feature.cloudChoose.listener.CloudChooseChangeListener;
import com.zinc.jrecycleview.JRecycleView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import eventbus.UserInfoUpdateEvent;
import eventbus.VideoCoverChangeEvent;
import response.cloud.FileInfoResp;

public final class CloudChooseFragment
        extends BitLoadMoreFragment<CloudChoosePresenter>
        implements CloudChooseView, AdapterListener, StateViewHolderListener {

    private static final int LOAD_SIZE = 21;

    private int mType;
    private int mLimit;
    private int mStart;

    private String mSearchContent;

    private CloudChooseChangeListener mListener;

    private final List<FileInfoResp.FileItem> mData = new ArrayList<>();

    public static CloudChooseFragment newInstance(int type, int limit) {

        Bundle args = new Bundle();
        args.putInt(CloudChooseActivity.TYPE, type);
        args.putInt(CloudChooseActivity.LIMIT, limit);

        CloudChooseFragment fragment = new CloudChooseFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void setSearchContent(String searchContent) {
        this.mSearchContent = searchContent;
    }

    public void setListener(CloudChooseChangeListener listener) {
        this.mListener = listener;
    }

    @Override
    protected void initArgs(Bundle arguments) {
        super.initArgs(arguments);
        mType = arguments.getInt(CloudChooseActivity.TYPE);
        mLimit = arguments.getInt(CloudChooseActivity.LIMIT);
        mStart = 0;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        mBaseAdapter.setStateViewHolderListener(this);
    }

    @Override
    public void getLoadMoreData() {
        load(false);
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return new CloudChooseAdapter(this, getContext(), mData, this);
    }

    /**
     * 重新加载
     */
    public void reload() {
        mData.clear();
        mBaseAdapter.onLoading();
        getFirstData(LoadType.CUSTOM);
    }

    @Override
    public void getFirstData(int type) {
        mStart = 0;
        load(true);
    }

    private void load(boolean isFirst) {

        int type = mType;
        if (mType == CloudChooseActivity.HEAD_PHOTO ||
                mType == CloudChooseActivity.COVER) {
            type = CloudChooseActivity.IMAGE;
        }
        mPresenter.load(isFirst, type, mStart, LOAD_SIZE, mSearchContent);
    }

    @Override
    protected void registerDagger() {
        DaggerCloudChooseComponent.builder()
                .appComponent(TyApplication.getAppComponent())
                .cloudChooseModule(new CloudChooseModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void onError(boolean isFirst) {
        if (isFirst) {
            mBaseAdapter.onError();
        } else {
            mBaseAdapter.setLoadError();
        }
    }

    @Override
    protected void initRecycleView(JRecycleView recycleView) {
        recycleView.setLayoutManager(new GridLayoutManager(getContext(), 3));
    }

    @Override
    public void onLoad(boolean isFirst, List<FileInfoResp.FileItem> fileInfo) {
        if (isFirst) {
            if (fileInfo == null || fileInfo.size() <= 0) {
                mBaseAdapter.onEmpty();
                return;
            }
        }

        if (fileInfo == null) { // 非第一次 才会进入
            mBaseAdapter.setNoMore();
            return;
        } else if (fileInfo.size() < LOAD_SIZE) {
            mBaseAdapter.setNoMore();
        } else {
            mBaseAdapter.setLoadComplete();
        }

        // 第一次要清空数据
        if (isFirst) {
            mData.clear();
            mBaseAdapter.onSuccess();
        }

        mStart += mData.size();

        mData.addAll(fileInfo);

        mBaseAdapter.notifyDataSetChanged();

    }

    @Override
    public void onCommitHeadChangeInfo() {
        hideDialog();
        ToastUtils.show(getString(R.string.head_photo_change_success));
        EventBus.getDefault().post(new UserInfoUpdateEvent());
        if (getActivity() != null) {
            getActivity().finish();
        }
    }

    @Override
    public void onCommitCover(String url, int coverId, int fileId) {
        hideDialog();
        ToastUtils.show(getString(R.string.cloud_cover_change_success));
        EventBus.getDefault().post(new VideoCoverChangeEvent(url, coverId, fileId));
        if (getActivity() != null) {
            getActivity().finish();
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        // 去除下拉刷新的项
        int listPos = position - 1;

        if (mType == CloudChooseActivity.HEAD_PHOTO ||
                mType == CloudChooseActivity.COVER) {
            handleForHeadPhotoOrCover(view, listPos, position);
        } else {
            handleForJerry(view, listPos, position);
        }

    }

    /**
     * 处理头像
     */
    private void handleForHeadPhotoOrCover(View view, int listPos, int position) {
        FileInfoResp.FileItem fileItem = mData.get(listPos);
        // 此张相片已经选择，则终止
        if (fileItem.isSelect()) {
            return;
        }

        for (int i = 0; i < mData.size(); i++) {

            FileInfoResp.FileItem item = mData.get(i);

            if (item.isSelect()) {
                item.setSelect(false);

                // +1 是偏移头部
                mBaseAdapter.notifyItemChanged(i + 1);
            }
        }

        fileItem.setSelect(true);

        mBaseAdapter.notifyItemChanged(position);
        mListener.chooseChange(1);
    }

    /**
     * 此处暂时是为富文本处理
     */
    private void handleForJerry(View view, int listPos, int position) {

        // 计算选择的张数
        int count = 0;
        for (FileInfoResp.FileItem item : mData) {
            if (item.isSelect()) {
                ++count;
            }
        }

        FileInfoResp.FileItem fileItem = mData.get(listPos);


        if (!fileItem.isSelect() && mLimit != CloudChooseActivity.NO_LIMIT && count + 1 > mLimit) {
            ToastUtils.show(String.format(getString(R.string.cloud_choose_limit), mLimit));
            return;
        }


        if (fileItem.isSelect()) {
            --count;
            fileItem.setSelect(false);
        } else {
            ++count;
            fileItem.setSelect(true);
        }

        mBaseAdapter.notifyItemChanged(position);

        mListener.chooseChange(count);
    }

    /**
     * 获取选择的文件信息
     */
    public List<FileInfoResp.FileItem> getSelFileInfo() {
        List<FileInfoResp.FileItem> selFileInfo = new ArrayList<>();
        for (FileInfoResp.FileItem item : mData) {
            if (item.isSelect()) {
                selFileInfo.add(item);
            }
        }
        return selFileInfo;
    }

    /**
     * 提交用户头像更改信息
     */
    public void commitHeadPhotoChangeInfo() {

        FileInfoResp.FileItem selFileInfo = null;
        for (FileInfoResp.FileItem item : mData) {
            if (item.isSelect()) {
                selFileInfo = item;
                break;
            }
        }

        if (selFileInfo == null) {
            ToastUtils.show(getString(R.string.head_photo_sel_tip));
            return;
        }

        mPresenter.commitHeadChangeInfo(selFileInfo.getId());

    }

    /**
     * 提交封面
     */
    public void commitCover(int fileId) {

        FileInfoResp.FileItem selFileInfo = null;
        for (FileInfoResp.FileItem item : mData) {
            if (item.isSelect()) {
                selFileInfo = item;
                break;
            }
        }

        if (selFileInfo == null) {
            ToastUtils.show(getString(R.string.cloud_cover_sel_tip));
            return;
        }

        showDialog();

        mPresenter.commitCover(selFileInfo.getUrl(), selFileInfo.getId(), fileId);

    }

    @Override
    protected int getEmptyView() {
        return R.layout.blank_no_file;
    }

    @Override
    public void handleEmptyViewHolder(BitFrameAdapter.EmptyViewHolder holder) {
        TextView tvTip = holder.itemView.findViewById(R.id.tv_tip);

        if (TextUtils.isEmpty(mSearchContent)) {
            tvTip.setText(getString(R.string.blank_no_uploaded_file));
        } else {
            tvTip.setText(getString(R.string.blank_no_satisfaction));
        }
    }

    @Override
    public void handleRetryViewHolder(BitFrameAdapter.RetryViewHolder holder) {
        // 空实现
    }

    @Override
    public void handleLoadingViewHolder(BitFrameAdapter.LoadingViewHolder holder) {
        // 空实现
    }

}
