package com.tysq.ty_android.feature.cloud.cloudList.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bit.utils.DateUtils;
import com.bit.view.fragment.BitBaseFragment;
import com.tysq.ty_android.R;
import com.tysq.ty_android.config.Constant;
import com.tysq.ty_android.feature.cloud.listener.CloudListListener;
import com.tysq.ty_android.local.sp.UserCache;
import com.tysq.ty_android.utils.TyUtils;
import com.zinc.jrecycleview.config.JRecycleConfig;
import com.zinc.jrecycleview.swipe.JSwipeViewHolder;
import com.zinc.lib_jerry_editor.config.glide.GlideOptions;

import java.lang.ref.WeakReference;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import response.cloud.FileInfoResp;

/**
 * author       : frog
 * time         : 2019/6/5 下午2:43
 * desc         : 云盘——已上传
 * version      : 1.3.0
 */
public class CloudListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<FileInfoResp.FileItem> mData;

    private final WeakReference<BitBaseFragment> mFragment;
    private final WeakReference<Context> mContext;
    private final LayoutInflater mInflater;

    private CloudListListener mListener;

    public CloudListAdapter(BitBaseFragment fragment,
                            Context context,
                            List<FileInfoResp.FileItem> cloudItemRespList) {
        this.mData = cloudItemRespList;
        this.mFragment = new WeakReference<>(fragment);
        this.mContext = new WeakReference<>(context);
        this.mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder = null;

        View inflate = mInflater.inflate(JRecycleConfig.SWIPE_LAYOUT,
                parent,
                false);

        switch (viewType) {
            case TyUtils.IMAGE_TYPE:
                viewHolder = new ImageViewHolder(inflate);
                break;
            case TyUtils.AUDIO_TYPE:
                viewHolder = new OtherViewHolder(inflate);
                break;
            case TyUtils.VIDEO_TYPE:
                viewHolder = new VideoViewHolder(inflate);
                break;
            case TyUtils.OTHER_TYPE:
                viewHolder = new OtherViewHolder(inflate);
                break;
            default:
                viewHolder = new OtherViewHolder(inflate);
                break;
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        FileInfoResp.FileItem item = mData.get(position);

        if (holder instanceof OtherViewHolder) {
            OtherViewHolder otherViewHolder = (OtherViewHolder) holder;

            String itemType = item.getType();

            String type;
            if (itemType.equals(TyUtils.AUDIO)) {
                type = TyUtils.AUDIO;
            } else {
                type = TyUtils.clipTheType(item.getFilename());
            }

            Integer cover = TyUtils.CLOUD_TYPE_IMG.get(type);

            if (cover == null) {
                cover = TyUtils.CLOUD_TYPE_IMG.get(TyUtils.OTHER);
            }

            setImage(otherViewHolder.ivCover, "", cover);

            otherViewHolder.tvTitle.setText(item.getFilename());
            otherViewHolder.tvTime.setText(DateUtils
                    .getTimeStringViaTimestamp(item.getCreateAt() * 1000L));
            otherViewHolder.tvSize.setText(TyUtils.formatFileSize(item.getSize()));

            otherViewHolder.llDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    otherViewHolder.getSwipeItemLayout().close();
                    if (mListener != null) {
                        mListener.deleteFile(item.getId());
                    }
                }
            });

            otherViewHolder.llDownload.setOnClickListener(v -> {
                otherViewHolder.getSwipeItemLayout().close();
                download(item.getId(),
                        item.getFilename(),
                        false,
                        item.getDownloadUrl(),
                        "");
            });

            otherViewHolder.llRename.setOnClickListener(v -> {
                otherViewHolder.getSwipeItemLayout().close();
                rename(item.getId());
            });

            if (item.getStorage() == Constant.STORAGE.AWS) {
                otherViewHolder.llDownload.setVisibility(View.VISIBLE);
                otherViewHolder.tvTip.setVisibility(View.GONE);
            } else {
                otherViewHolder.llDownload.setVisibility(View.GONE);
                otherViewHolder.tvTip.setVisibility(View.VISIBLE);
            }

        } else if (holder instanceof ImageViewHolder) {
            ImageViewHolder imageViewHolder = (ImageViewHolder) holder;

            setImage(imageViewHolder.ivCover,
                    item.getUrl(),
                    TyUtils.CLOUD_TYPE_IMG.get(TyUtils.IMAGE));

            imageViewHolder.tvTitle.setText(item.getFilename());
            imageViewHolder.tvTime.setText(DateUtils
                    .getTimeStringViaTimestamp(item.getCreateAt() * 1000L));
            imageViewHolder.tvSize.setText(TyUtils.formatFileSize(item.getSize()));

            imageViewHolder.llDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imageViewHolder.getSwipeItemLayout().close();
                    if (mListener != null) {
                        mListener.deleteFile(item.getId());
                    }
                }
            });

            imageViewHolder.llDownload.setOnClickListener(v -> {
                imageViewHolder.getSwipeItemLayout().close();
                download(item.getId(),
                        item.getFilename(),
                        false,
                        item.getDownloadUrl(),
                        item.getUrl());
            });

            imageViewHolder.llRename.setOnClickListener(v -> {
                imageViewHolder.getSwipeItemLayout().close();
                rename(item.getId());
            });

            if (item.getStorage() == Constant.STORAGE.AWS) {
                imageViewHolder.llDownload.setVisibility(View.VISIBLE);
                imageViewHolder.tvTip.setVisibility(View.GONE);
            } else {
                imageViewHolder.llDownload.setVisibility(View.GONE);
                imageViewHolder.tvTip.setVisibility(View.VISIBLE);
            }

        } else if (holder instanceof VideoViewHolder) {
            VideoViewHolder videoViewHolder = (VideoViewHolder) holder;

            String url = null;

            if (item.getCoverList() != null && item.getCoverList().size() > 0) {
                url = item.getCoverList().get(0).getCoverUrl();
            }

            if (url == null) {
                if (item.getScreenshotList() != null && item.getScreenshotList().size() > 0) {
                    url = item.getScreenshotList().get(0).getScreenshotsUrl();
                }
            }

            setImage(videoViewHolder.ivCover,
                    url,
                    R.color.replace_color);

            videoViewHolder.tvTitle.setText(item.getFilename());
            videoViewHolder.tvTime.setText(DateUtils
                    .getTimeStringViaTimestamp(item.getCreateAt() * 1000L));
            videoViewHolder.tvSize.setText(TyUtils.formatFileSize(item.getSize()));

            videoViewHolder.llDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    videoViewHolder.getSwipeItemLayout().close();
                    if (mListener != null) {
                        mListener.deleteFile(item.getId());
                    }
                }
            });

            videoViewHolder.llCover.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (mListener != null) {
                        mListener.chooseCover(item.getId());
                    }

                }
            });

            videoViewHolder.llRename.setOnClickListener(v -> {
                videoViewHolder.getSwipeItemLayout().close();
                rename(item.getId());
            });

            String finalUrl = url;

            videoViewHolder.llDownload.setOnClickListener(v -> {
                videoViewHolder.getSwipeItemLayout().close();
                download(item.getId(),
                        item.getFilename(),
                        true,
                        item.getDownloadUrl(),
                        finalUrl == null ? "" : finalUrl);
            });

            if (item.getStorage() == Constant.STORAGE.AWS) {
                videoViewHolder.llDownload.setVisibility(View.VISIBLE);
                videoViewHolder.tvTip.setVisibility(View.GONE);
            } else {
                videoViewHolder.llDownload.setVisibility(View.GONE);
                videoViewHolder.tvTip.setVisibility(View.VISIBLE);
            }

        }
    }

    private void rename(int id) {
        if (mListener == null) {
            return;
        }
        mListener.rename(id);
    }

    private void download(int id,
                          String filename,
                          boolean isVideo,
                          String downloadUrl,
                          String cover) {
        if (mListener == null) {
            return;
        }
        int accountId = UserCache.getDefault().getAccountId();
        mListener.downloadFile(id,
                filename,
                accountId,
                isVideo,
                downloadUrl,
                cover);
    }

    private void setImage(ImageView imageView,
                          String url,
                          int placeHolder) {
        if (url == null || url.equals("")) {
            imageView.setImageResource(placeHolder);
            return;
        }

        GlideOptions options = new GlideOptions()
                .placeholder(placeHolder)
                .error(placeHolder);

        TyUtils.getGlideRequest(
                mFragment.get(),
                mContext.get(),
                url,
                options,
                imageView
        );
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        Integer type = TyUtils.CLOUD_TYPE.get(mData.get(position).getType());

        if (type == null) {
            return TyUtils.OTHER_TYPE;
        }

        return type;
    }

    public void setListener(CloudListListener listener) {

        mListener = listener;
    }

    /**
     * 图片
     */
    public static class ImageViewHolder extends JSwipeViewHolder {

        @BindView(R.id.iv_cover)
        ImageView ivCover;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tv_size)
        TextView tvSize;
        @BindView(R.id.tv_tip)
        TextView tvTip;

        @BindView(R.id.ll_rename)
        LinearLayout llRename;
        @BindView(R.id.ll_download)
        LinearLayout llDownload;
        @BindView(R.id.ll_delete)
        LinearLayout llDelete;

        public ImageViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public int getRightMenuLayout() {
            return R.layout.menu_right_cloud_list;
        }

        @Override
        public int getContentLayout() {
            return R.layout.item_cloud_img;
        }

        @Override
        public void initItem(FrameLayout frameLayout) {
            ButterKnife.bind(this, frameLayout);
        }

    }

    /**
     * 音频、其他
     */
    public static class OtherViewHolder extends JSwipeViewHolder {

        @BindView(R.id.iv_cover)
        ImageView ivCover;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tv_size)
        TextView tvSize;
        @BindView(R.id.tv_tip)
        TextView tvTip;

        @BindView(R.id.ll_rename)
        LinearLayout llRename;
        @BindView(R.id.ll_download)
        LinearLayout llDownload;
        @BindView(R.id.ll_delete)
        LinearLayout llDelete;

        public OtherViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public int getRightMenuLayout() {
            return R.layout.menu_right_cloud_list;
        }

        @Override
        public int getContentLayout() {
            return R.layout.item_cloud_other;
        }

        @Override
        public void initItem(FrameLayout frameLayout) {
            ButterKnife.bind(this, itemView);
        }

    }

    /**
     * 视频
     */
    public static class VideoViewHolder extends JSwipeViewHolder {

        @BindView(R.id.ll_cover)
        RelativeLayout llCover;
        @BindView(R.id.iv_cover)
        ImageView ivCover;
        @BindView(R.id.iv_arrow)
        ImageView ivArrow;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tv_size)
        TextView tvSize;
        @BindView(R.id.tv_tip)
        TextView tvTip;

        @BindView(R.id.ll_rename)
        LinearLayout llRename;
        @BindView(R.id.ll_download)
        LinearLayout llDownload;
        @BindView(R.id.ll_delete)
        LinearLayout llDelete;

        VideoViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public int getRightMenuLayout() {
            return R.layout.menu_right_cloud_list;
        }

        @Override
        public int getContentLayout() {
            return R.layout.item_cloud_video;
        }

        @Override
        public void initItem(FrameLayout frameLayout) {
            ButterKnife.bind(this, itemView);
        }

    }

}
