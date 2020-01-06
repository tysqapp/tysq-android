package com.tysq.ty_android.feature.rewardList;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bit.utils.DateUtils;
import com.bit.view.fragment.BitBaseFragment;
import com.tysq.ty_android.R;
import com.tysq.ty_android.base.adapter.CommonHeaderSimpleAdapter;
import com.tysq.ty_android.feature.personalHomePage.PersonalHomePageActivity;
import com.tysq.ty_android.utils.TyUtils;

import java.lang.ref.WeakReference;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import response.article.RewardListResp;
import vo.RewardListVO;

public class RewardListAdapter extends CommonHeaderSimpleAdapter<RewardListVO, RewardListResp.RewardListBean> {

    private WeakReference<BitBaseFragment> mFragment;

    public RewardListAdapter(BitBaseFragment fragment,
                             Context context,
                             RewardListVO header,
                             List<RewardListResp.RewardListBean> rewardListBeans) {
        super(context, header, rewardListBeans);
        mFragment = new WeakReference<>(fragment);
    }

    @Override
    protected RecyclerView.ViewHolder getHeaderViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HeadViewHolder(
                mInflater.inflate(R.layout.item_common_count_title, parent, false)
        );
    }

    @Override
    protected RecyclerView.ViewHolder getContentViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ContentViewHolder(mInflater.inflate(R.layout.item_reward_list, parent, false)
        );
    }

    @Override
    protected RecyclerView.ViewHolder getEmptyViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new EmptyViewHolder(mInflater.inflate(R.layout.blank_empty_data_log, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeadViewHolder) {
            HeadViewHolder headViewHolder = (HeadViewHolder) holder;
            String format = String.format(mContext.get().getString(R.string.reward_list_title_count), mHeader.getCount());
            headViewHolder.tvTitle.setText(format);
        } else {
            ContentViewHolder contentViewHolder = (ContentViewHolder) holder;

            RewardListResp.RewardListBean item = mDataList.get(position - 1);

            TyUtils.initUserPhoto(
                    mFragment.get(),
                    mContext.get(),
                    item.getHeadUrl(),
                    contentViewHolder.ivAvatar);

            contentViewHolder.tvTime.setText(DateUtils.getY_M_D_H_M_S_ViaTimestamp(item.getRewardedAt() * 1000L));

            String format = String.format(mContext.get().getString(R.string.reward_list_item_reward_num), item.getAmount());
            contentViewHolder.tvTitle.setText(format);

            contentViewHolder.ivAvatar.setOnClickListener(view ->
                    PersonalHomePageActivity.startActivity(mContext.get(), item.getRewardId())
            );
        }
    }

    public static class HeadViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_title)
        TextView tvTitle;

        public HeadViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public static class ContentViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_avatar)
        ImageView ivAvatar;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_time)
        TextView tvTime;

        public ContentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
