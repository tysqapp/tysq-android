package com.tysq.ty_android.feature.cloud.cloudUploading.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.tysq.ty_android.R;
import com.tysq.ty_android.utils.TyUtils;
import com.tysq.ty_android.widget.ProgressView;
import com.zinc.jrecycleview.config.JRecycleConfig;
import com.zinc.jrecycleview.swipe.JSwipeViewHolder;

import java.lang.ref.WeakReference;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import listener.UploadItemListener;
import model.FileModel;

/**
 * author       : frog
 * time         : 2019/6/16 下午5:37
 * desc         :
 * version      : 1.3.0
 */
public class CloudUploadingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<FileModel> mData;
    private final LayoutInflater mInflater;
    private final WeakReference<Context> mContext;
    private final Listener mListener;

    public CloudUploadingAdapter(Context context,
                                 List<FileModel> data,
                                 Listener listener) {
        this.mData = data;
        this.mContext = new WeakReference<>(context);
        this.mInflater = LayoutInflater.from(context);
        this.mListener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                      int viewType) {
        return new UploadingViewHolder(
                mInflater.inflate(JRecycleConfig.SWIPE_LAYOUT, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder,
                                 int position) {

        FileModel fileModel = mData.get(position);
        UploadingViewHolder uploadingViewHolder = (UploadingViewHolder) holder;
        uploadingViewHolder.tvTitle.setText(fileModel.getFilename());
        uploadingViewHolder.tvTime.setText(fileModel.getCreateTime());
        uploadingViewHolder.tvSize.setText(TyUtils.formatFileSize(fileModel.getTotalSize()));
        uploadingViewHolder.progressBar.setProgress(fileModel.getPercent());

        uploadingViewHolder.ivCover.setImageResource(fileModel.getFileType());

        if (fileModel.isError()) {
            uploadingViewHolder.tvError.setText(fileModel.getErrorMsg());
            uploadingViewHolder.tvError.setVisibility(View.VISIBLE);
        } else {
            uploadingViewHolder.tvError.setVisibility(View.GONE);
        }

        uploadingViewHolder.tvDelete.setOnClickListener(v -> {
            uploadingViewHolder.getSwipeItemLayout().close();

            mListener.deleteItem(uploadingViewHolder.getAdapterPosition());
        });

    }

    @Override
    public void onViewAttachedToWindow(@NonNull RecyclerView.ViewHolder holder) {

        if (holder instanceof UploadingViewHolder) {
            int adapterPosition = holder.getAdapterPosition();
            FileModel fileModel = mData.get(adapterPosition);

            Log.i("zinc", "onViewAttachedToWindow: " + fileModel.getFilename());

            UploadingViewHolder uploadingViewHolder = (UploadingViewHolder) holder;

            fileModel.setListener(new UploadItemListener() {
                @Override
                public void onProgress() {
                    uploadingViewHolder.progressBar.setProgress(fileModel.getPercent());
                }

                @Override
                public void onError() {
                    if (fileModel.isError()) {
                        uploadingViewHolder.tvError.setText(fileModel.getErrorMsg());
                        uploadingViewHolder.tvError.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onDelete() {
                    uploadingViewHolder.getSwipeItemLayout().close();
                    mListener.deleteItem(uploadingViewHolder.getAdapterPosition());
                }
            });
        }

    }

    @Override
    public void onViewDetachedFromWindow(@NonNull RecyclerView.ViewHolder holder) {
        if (holder instanceof UploadingViewHolder) {
            int adapterPosition = holder.getAdapterPosition();

            if (adapterPosition >= mData.size() || adapterPosition < 0) {
                return;
            }

            FileModel fileModel = mData.get(adapterPosition);
            Log.i("zinc", "onViewDetachedFromWindow: " + fileModel.getFilename());

            fileModel.setListener(null);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    static class UploadingViewHolder extends JSwipeViewHolder {
        @BindView(R.id.iv_cover)
        ImageView ivCover;
        @BindView(R.id.progress_bar)
        ProgressView progressBar;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tv_size)
        TextView tvSize;
        @BindView(R.id.tv_error)
        TextView tvError;

        @BindView(R.id.tv_delete)
        TextView tvDelete;

        UploadingViewHolder(View view) {
            super(view);
        }

        @Override
        public int getRightMenuLayout() {
            return R.layout.menu_right_delete;
        }

        @Override
        public int getContentLayout() {
            return R.layout.item_cloud_uploading;
        }

        @Override
        public void initItem(FrameLayout frameLayout) {
            ButterKnife.bind(this, itemView);
        }

    }

    public interface Listener {
        void deleteItem(int pos);
    }

}
