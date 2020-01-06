package com.tysq.ty_android.feature.cloud.cloudDownload.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abc.lib_multi_download.jerry.JerryDownload;
import com.abc.lib_multi_download.listener.DownloadListener;
import com.abc.lib_multi_download.model.DownloadInfo;
import com.abc.lib_multi_download.model.status.RunningStatus;
import com.abc.lib_multi_download.utils.DownloadFileUtils;
import com.abc.lib_utils.MimeTypeUtils;
import com.bit.utils.DateUtils;
import com.bit.view.fragment.BitBaseFragment;
import com.bumptech.glide.request.RequestOptions;
import com.tysq.ty_android.BuildConfig;
import com.tysq.ty_android.R;
import com.tysq.ty_android.base.BaseAdapter;
import com.tysq.ty_android.utils.TyUtils;
import com.tysq.ty_android.widget.ProgressView;
import com.zinc.jrecycleview.config.JRecycleConfig;
import com.zinc.jrecycleview.swipe.JSwipeViewHolder;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import vo.cloud.CloudDownloadVO;

/**
 * author       : frog
 * time         : 2019-10-18 09:41
 * desc         : 下载
 * version      : 1.5.0
 */
public class CloudDownloadAdapter extends BaseAdapter<RecyclerView.ViewHolder> {

    private final List<CloudDownloadVO> mData;

    private final Listener listener;

    private WeakReference<BitBaseFragment> mFragment;

    public CloudDownloadAdapter(BitBaseFragment fragment,
                                Context context,
                                List<CloudDownloadVO> data,
                                Listener listener) {
        super(context);
        this.mFragment = new WeakReference<>(fragment);
        this.mData = data;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                      int viewType) {
        if (viewType == CloudDownloadVO.TYPE.HEAD) {
            return new TitleViewHolder(
                    mInflater.inflate(R.layout.item_cloud_download_finish_title,
                            parent,
                            false)
            );
        } else {
            return new ContentViewHolder(
                    mInflater.inflate(JRecycleConfig.SWIPE_LAYOUT,
                            parent,
                            false)
            );
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        CloudDownloadVO itemVO = mData.get(position);

        if (holder instanceof TitleViewHolder) {

            TitleViewHolder titleViewHolder = (TitleViewHolder) holder;

            int size = itemVO.getSize();

            String content = String.format(mContext.get().getString(R.string.cloud_download_finish_title), size);
            titleViewHolder.tvInfo.setText(content);

        } else {
            ContentViewHolder contentViewHolder = (ContentViewHolder) holder;

            DownloadInfo downloadInfo = itemVO.getDownloadInfo();

            int nextPosition = position + 1;

            if (nextPosition >= mData.size()) { // 最后一个item
                contentViewHolder.vDivider.setVisibility(View.GONE);
            } else if (mData.get(nextPosition).getType() == CloudDownloadVO.TYPE.HEAD) {   // 下一个是标题
                contentViewHolder.vDivider.setVisibility(View.GONE);
            } else {    // 其他
                contentViewHolder.vDivider.setVisibility(View.VISIBLE);
            }

            downloadInfo.removeListener();

            bindView(contentViewHolder, downloadInfo);

        }

    }

    /**
     * 绑定
     */
    private void bindView(ContentViewHolder contentViewHolder, DownloadInfo downloadInfo) {

        setCommonView(contentViewHolder, downloadInfo);

        if (downloadInfo.getRunningStatus() == RunningStatus.EXCEPTION) {
            onError(contentViewHolder, downloadInfo);
            return;
        }

        if (downloadInfo.getRunningStatus() == RunningStatus.SUCCESS) {
            onFinish(contentViewHolder, downloadInfo);
            return;
        }

        if (downloadInfo.getRunningStatus() == RunningStatus.PAUSE) {
            onPause(contentViewHolder, downloadInfo);
            return;
        }

        if (downloadInfo.getRunningStatus() == RunningStatus.WAITING) {
            onWaiting(contentViewHolder, downloadInfo);
            return;
        }

        if (downloadInfo.getRunningStatus() == RunningStatus.INIT) {
            onInit(contentViewHolder, downloadInfo);
            return;
        }

        if (downloadInfo.getRunningStatus() == RunningStatus.DOWNLOADING) {
            onDownloading(contentViewHolder, downloadInfo);
            return;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return mData.get(position).getType();
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull RecyclerView.ViewHolder holder) {
        if (holder instanceof ContentViewHolder) {
            int adapterPosition = getRealPos(holder.getAdapterPosition());

            if (adapterPosition >= mData.size() || adapterPosition < 0) {
                return;
            }

            CloudDownloadVO cloudDownloadVO = mData.get(adapterPosition);
            cloudDownloadVO.getDownloadInfo().removeListener();
            cloudDownloadVO.setLastState(cloudDownloadVO.getDownloadInfo().getRunningStatus());
        }
    }

    @Override
    public void onViewAttachedToWindow(@NonNull RecyclerView.ViewHolder holder) {

        if (holder instanceof ContentViewHolder) {
            ContentViewHolder contentViewHolder = (ContentViewHolder) holder;

            int adapterPosition = getRealPos(holder.getAdapterPosition());
            CloudDownloadVO cloudDownloadVO = mData.get(adapterPosition);

            int runningStatus = cloudDownloadVO.getDownloadInfo().getRunningStatus();

            if (cloudDownloadVO.getLastState() != runningStatus
                    || runningStatus == RunningStatus.DOWNLOADING) {
                bindView(contentViewHolder, cloudDownloadVO.getDownloadInfo());
            }

            cloudDownloadVO.getDownloadInfo()
                    .setListener(new DownloadListener() {

                        @Override
                        public void onPause() {
                            setCommonView(contentViewHolder,
                                    cloudDownloadVO.getDownloadInfo());
                            CloudDownloadAdapter.this.onPause(
                                    contentViewHolder,
                                    cloudDownloadVO.getDownloadInfo()
                            );
                        }

                        @Override
                        public void onWaiting() {
                            setCommonView(contentViewHolder,
                                    cloudDownloadVO.getDownloadInfo());
                            CloudDownloadAdapter.this.onWaiting(
                                    contentViewHolder,
                                    cloudDownloadVO.getDownloadInfo()
                            );
                        }

                        @Override
                        public void onInit() {
                            setCommonView(contentViewHolder,
                                    cloudDownloadVO.getDownloadInfo());
                            CloudDownloadAdapter.this.onInit(
                                    contentViewHolder,
                                    cloudDownloadVO.getDownloadInfo()
                            );
                        }

                        @Override
                        public void onDownloading() {
                            setCommonView(contentViewHolder,
                                    cloudDownloadVO.getDownloadInfo());
                            CloudDownloadAdapter.this.onDownloading(
                                    contentViewHolder,
                                    cloudDownloadVO.getDownloadInfo()
                            );
                        }

                        @Override
                        public void onUpdateProgress() {
                            CloudDownloadAdapter.this.setProgress(
                                    contentViewHolder,
                                    cloudDownloadVO.getDownloadInfo()
                            );
                        }

                        @Override
                        public void onSuccess() {
                            setCommonView(contentViewHolder,
                                    cloudDownloadVO.getDownloadInfo());
                            addFinishItem(cloudDownloadVO.getDownloadInfo().getId());
                        }

                        @Override
                        public void onException() {
                            setCommonView(contentViewHolder,
                                    cloudDownloadVO.getDownloadInfo());
                            CloudDownloadAdapter.this.onError(
                                    contentViewHolder,
                                    cloudDownloadVO.getDownloadInfo()
                            );
                        }

                        @Override
                        public void onDelete() {
                            if (listener != null) {
                                listener.delete(cloudDownloadVO.getDownloadInfo());
                            }
                        }
                    });

        }

    }

    private void addFinishItem(long finishId) {
//        int curDataPos = getRealPos(finishId);
//        listener.addFinishItem(finishId);
    }

    /**
     * 获取真正的下标
     *
     * @param pos
     * @return
     */
    private int getRealPos(int pos) {
        return pos - 1;
    }

    /**
     * 初始化
     *
     * @param holder       视图
     * @param downloadInfo 下载信息【数据库】
     */
    private void setCommonView(ContentViewHolder holder,
                               DownloadInfo downloadInfo) {
        // 设置图片
        Integer img = null;
        if (downloadInfo.getMimeType() != null) {
            int mimeType = MimeTypeUtils.getMultimediaType(downloadInfo.getMimeType());
            switch (mimeType) {
                case MimeTypeUtils.TYPE_IMAGE:
                    img = R.drawable.ic_file_img;
                    break;
                case MimeTypeUtils.TYPE_AUDIO:
                    img = R.drawable.ic_file_audio;
                    break;
                case MimeTypeUtils.TYPE_VIDEO:
                    img = R.drawable.ic_file_video;
                    break;
                default:
                    img = null;
                    break;
            }
        }

        if (img == null) {
            img = TyUtils.CLOUD_TYPE_IMG.get(downloadInfo.getSubType());
        }

        if (img == null) {
            img = R.drawable.ic_file_other;
        }

        if (TextUtils.isEmpty(downloadInfo.getCover())) {
            holder.ivIcon.setImageResource(img);
        } else {
            RequestOptions requestOptions = new RequestOptions()
                    .placeholder(img)
                    .error(img);
            String imageUrl = downloadInfo.getCover();
            TyUtils.getGlideRequest(
                    mFragment.get(),
                    mContext.get(),
                    imageUrl,
                    requestOptions,
                    holder.ivIcon
            );
        }

        // 设置名称
        String filename = downloadInfo.getFileName();
//        if (TextUtils.isEmpty(filename)) {
//            filename = downloadInfo.getFileName();
//        }
        holder.tvName.setText(filename);

        // 时间 大小
        String info = DateUtils.getTimeStringViaTimestamp(downloadInfo.getCreateTime());
        long totalSize = downloadInfo.getTotalSize();
        if (totalSize > 0) {
            String fileSize = TyUtils.formatFileSize(totalSize);
            info = info + "    " + fileSize;
        }
        holder.tvInfo.setText(info);

        holder.tvDelete.setOnClickListener(v -> {
            JerryDownload.getInstance().delete(downloadInfo);

            holder.getSwipeItemLayout().close();
        });

    }

    /**
     * 设置进度
     */
    private void setProgress(ContentViewHolder holder, DownloadInfo downloadInfo) {

        holder.progressView.setVisibility(View.VISIBLE);
        holder.tvProgress.setVisibility(View.VISIBLE);

        holder.progressView.setProgress((int) downloadInfo.getProgress());
        holder.tvProgress.setText(downloadInfo.getProgress() + "%");
    }

    /**
     * 暂停
     */
    private void onPause(ContentViewHolder holder, DownloadInfo downloadInfo) {
        setCommonView(holder, downloadInfo);

        // 进度容器布局
        holder.rlProgress.setVisibility(View.VISIBLE);

        // 进度
        holder.progressView.setVisibility(View.VISIBLE);
        holder.progressView.setProgress((int) downloadInfo.getProgress());
        holder.progressView.setOnClickListener(v -> {
            JerryDownload.getInstance().download(downloadInfo);
        });

        // 状态图标
        holder.ivStatus.setVisibility(View.VISIBLE);
        holder.ivStatus.setImageResource(R.drawable.ic_cloud_download);

        // 进度提示
        holder.tvProgress.setVisibility(View.GONE);
//        holder.tvProgress.setText(downloadInfo.getPercent() + "%");

        // 异常
        holder.tvTip.setVisibility(View.GONE);

        // 错误
        holder.tvError.setVisibility(View.GONE);
    }


    private void openFile(File file, String type) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(mContext.get(),
                    BuildConfig.APPLICATION_ID + ".fileprovider", file);
        } else {
            uri = Uri.fromFile(file);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setDataAndType(uri, type);
        mContext.get().startActivity(intent);
    }

    /**
     * 等待下载
     */
    private void onWaiting(ContentViewHolder holder, DownloadInfo downloadInfo) {
        // 进度容器布局
        holder.rlProgress.setVisibility(View.VISIBLE);

        // 进度
        holder.progressView.setVisibility(View.VISIBLE);
        holder.progressView.setProgress((int) downloadInfo.getProgress());
        holder.progressView.setOnClickListener(v -> {
            JerryDownload.getInstance().pause(downloadInfo);
        });

        // 状态图标
        holder.ivStatus.setVisibility(View.VISIBLE);
        holder.ivStatus.setImageResource(R.drawable.ic_cloud_pause);

        // 进度提示
        holder.tvProgress.setVisibility(View.VISIBLE);
        holder.tvProgress.setText(mContext.get().getString(R.string.cloud_download_task_waiting));

        // 异常
        holder.tvTip.setVisibility(View.GONE);

        // 错误
        holder.tvError.setVisibility(View.GONE);
    }

    /**
     * 初始化
     */
    private void onInit(ContentViewHolder holder, DownloadInfo downloadInfo) {
        // 进度容器布局
        holder.rlProgress.setVisibility(View.VISIBLE);

        // 进度
        holder.progressView.setVisibility(View.VISIBLE);
        holder.progressView.setProgress((int) downloadInfo.getProgress());
        holder.progressView.setOnClickListener(v -> {
            JerryDownload.getInstance().pause(downloadInfo);
        });

        // 状态图标
        holder.ivStatus.setVisibility(View.VISIBLE);
        holder.ivStatus.setImageResource(R.drawable.ic_cloud_pause);

        // 进度提示
        holder.tvProgress.setVisibility(View.VISIBLE);
        holder.tvProgress.setText("loading...");

        // 异常
        holder.tvTip.setVisibility(View.GONE);

        // 错误
        holder.tvError.setVisibility(View.GONE);
    }

    /*
     * 下载中
     */
    private void onDownloading(ContentViewHolder holder, DownloadInfo downloadInfo) {
        // 进度容器布局
        holder.rlProgress.setVisibility(View.VISIBLE);

        // 进度
        holder.progressView.setVisibility(View.VISIBLE);
        holder.progressView.setProgress((int) downloadInfo.getProgress());
        holder.progressView.setOnClickListener(v -> {
            JerryDownload.getInstance().pause(downloadInfo);
        });


        // 状态图标
        holder.ivStatus.setVisibility(View.VISIBLE);
        holder.ivStatus.setImageResource(R.drawable.ic_cloud_pause);

        // 进度提示
        holder.tvProgress.setVisibility(View.VISIBLE);
        holder.tvProgress.setText(downloadInfo.getProgress() + "%");

        // 异常
        holder.tvTip.setVisibility(View.GONE);

        // 错误
        holder.tvError.setVisibility(View.GONE);
    }

    /**
     * 异常
     */
    private void onError(ContentViewHolder holder, DownloadInfo downloadInfo) {
        // 进度容器布局
        holder.rlProgress.setVisibility(View.VISIBLE);

        // 进度
        holder.progressView.setVisibility(View.VISIBLE);
        holder.progressView.setProgress((int) downloadInfo.getProgress());
        holder.progressView.setOnClickListener(v -> {
            JerryDownload.getInstance().download(downloadInfo);
        });

        // 状态图标
        holder.ivStatus.setVisibility(View.VISIBLE);
        holder.ivStatus.setImageResource(R.drawable.ic_cloud_download);

        // 进度提示
        holder.tvProgress.setVisibility(View.GONE);

        // 异常
        holder.tvTip.setVisibility(View.VISIBLE);
        holder.tvTip.setText(downloadInfo.getMsg());

        // 错误
        holder.tvError.setVisibility(View.GONE);
    }

    private void onFinish(ContentViewHolder holder, DownloadInfo downloadInfo) {
        // 进度容器布局
        holder.rlProgress.setVisibility(View.GONE);

        // 进度
        holder.progressView.setVisibility(View.GONE);
        holder.progressView.setOnClickListener(null);

        // 状态图标
        holder.ivStatus.setVisibility(View.GONE);

        // 进度提示
        holder.tvProgress.setVisibility(View.GONE);

        // 异常
        holder.tvTip.setVisibility(View.GONE);

        // 错误
        holder.tvError.setVisibility(View.GONE);

        holder.itemView.setOnClickListener(v -> {
            openFile(DownloadFileUtils.getFile(downloadInfo.getFileName()), downloadInfo.getMimeType());
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    static class ContentViewHolder extends JSwipeViewHolder {

        @BindView(R.id.iv_icon)
        ImageView ivIcon;

        @BindView(R.id.rl_progress)
        RelativeLayout rlProgress;

        @BindView(R.id.progress_view)
        ProgressView progressView;

        @BindView(R.id.iv_status)
        ImageView ivStatus;

        @BindView(R.id.tv_progress)
        TextView tvProgress;

        @BindView(R.id.tv_tip)
        TextView tvTip;

        @BindView(R.id.tv_error)
        TextView tvError;

        @BindView(R.id.tv_name)
        TextView tvName;

        @BindView(R.id.tv_info)
        TextView tvInfo;

        @BindView(R.id.v_divider)
        View vDivider;

        @BindView(R.id.tv_delete)
        TextView tvDelete;

        ContentViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public int getRightMenuLayout() {
            return R.layout.menu_right_delete;
        }

        @Override
        public int getContentLayout() {
            return R.layout.item_cloud_download;
        }

        @Override
        public void initItem(FrameLayout frameLayout) {
            ButterKnife.bind(this, frameLayout);
        }

    }

    static class TitleViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_info)
        TextView tvInfo;

        TitleViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public interface Listener {

        void delete(DownloadInfo downloadInfo);

        void addFinishItem(long finishId);

    }

}
