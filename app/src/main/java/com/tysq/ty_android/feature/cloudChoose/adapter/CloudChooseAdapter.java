package com.tysq.ty_android.feature.cloudChoose.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bit.view.fragment.BitBaseFragment;
import com.bumptech.glide.request.RequestOptions;
import com.tysq.ty_android.R;
import com.tysq.ty_android.feature.cloudChoose.listener.AdapterListener;
import com.tysq.ty_android.utils.TyUtils;

import java.lang.ref.WeakReference;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import response.cloud.FileInfoResp;

/**
 * author       : frog
 * time         : 2019/5/13 下午6:06
 * desc         :
 * version      :
 */
public class CloudChooseAdapter extends RecyclerView.Adapter<CloudChooseAdapter.ViewHolder> {

    private final LayoutInflater mInflater;

    private final List<FileInfoResp.FileItem> mData;

    private WeakReference<Context> mContext;
    private WeakReference<BitBaseFragment> mFragment;
    private AdapterListener mListener;

    public CloudChooseAdapter(BitBaseFragment fragment,
                              Context context,
                              List<FileInfoResp.FileItem> mFileInfoData,
                              AdapterListener listener) {
        this.mFragment = new WeakReference<>(fragment);
        this.mContext = new WeakReference<>(context);
        this.mInflater = LayoutInflater.from(context);
        this.mData = mFileInfoData;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public CloudChooseAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater
                .inflate(R.layout.item_cloud_choose, parent, false), mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CloudChooseAdapter.ViewHolder holder, int position) {

        FileInfoResp.FileItem item = mData.get(position);

        int placeholderSource;
        switch (item.getType()) {
            case TyUtils.IMAGE:
                placeholderSource = R.drawable.ic_cloud_choose_img;
                break;
            case TyUtils.VIDEO:
                placeholderSource = R.drawable.ic_cloud_choose_video;
                break;
            case TyUtils.AUDIO:
                placeholderSource = R.drawable.ic_cloud_choose_audio;
                break;
            default:
                placeholderSource = R.drawable.ic_cloud_choose_other;
                break;
        }

        RequestOptions requestOptions = new RequestOptions()
                .placeholder(placeholderSource)
                .error(placeholderSource);

        holder.cbCheck.setChecked(item.isSelect());

        String showUrl;
        switch (item.getType()) {
            case TyUtils.VIDEO:
                showUrl = getVideoUrl(item);
                break;
            case TyUtils.IMAGE:
                showUrl = item.getUrl();
                break;
            default:
                showUrl = "";
                break;
        }

        TyUtils.getGlideRequest(
                mFragment.get(),
                mContext.get(),
                showUrl,
                requestOptions,
                holder.ivImg);

        holder.tvDes.setText(item.getFilename());

    }

    private String getVideoUrl(FileInfoResp.FileItem info) {

        if (info.getCoverList() != null &&
                info.getCoverList().size() > 0) {
            return info.getCoverList().get(0).getCoverUrl();
        } else if (info.getScreenshotList() != null &&
                info.getScreenshotList().size() > 0) {
            return info.getScreenshotList().get(0).getScreenshotsUrl();
        } else {
            return "";
        }

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.rl_item)
        RelativeLayout rlItem;
        @BindView(R.id.iv_img)
        ImageView ivImg;
        @BindView(R.id.tv_des)
        TextView tvDes;
        @BindView(R.id.cb_check)
        CheckBox cbCheck;

        ViewHolder(View view, AdapterListener listener) {
            super(view);
            ButterKnife.bind(this, view);
            itemView.setOnClickListener(v -> {
                if (listener == null) {
                    return;
                }
                listener.onItemClick(itemView, getAdapterPosition());
            });
        }
    }

}
