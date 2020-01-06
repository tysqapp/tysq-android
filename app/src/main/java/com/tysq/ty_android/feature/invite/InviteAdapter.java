package com.tysq.ty_android.feature.invite;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abc.lib_utils.toast.ToastUtils;
import com.bit.utils.DateUtils;
import com.bit.utils.UIUtils;
import com.bit.utils.code.ZxingUtils;
import com.tysq.ty_android.R;
import com.tysq.ty_android.base.adapter.CommonHeaderSimpleAdapter;
import com.tysq.ty_android.utils.ScreenAdapterUtils;

import java.lang.ref.WeakReference;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import response.InviteResp;

import static android.content.Context.CLIPBOARD_SERVICE;

/**
 * author       : frog
 * time         : 2019-07-19 11:35
 * desc         :
 * version      :
 */
public class InviteAdapter extends CommonHeaderSimpleAdapter<InviteResp, InviteResp.ReferralListBean> {

    private final WeakReference<InviteFragment> mFragment;

    private ClipboardManager mClipboard;

    private final int offset;
    private final int bannerWidth;
    private final int bannerHeight;

    public InviteAdapter(InviteFragment fragment,
                         Context context,
                         InviteResp inviteResp,
                         List<InviteResp.ReferralListBean> data) {
        super(context, inviteResp, data);
        this.mFragment = new WeakReference<>(fragment);
        this.mClipboard = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
        this.offset = UIUtils.dip2px(context, 1.5f);
        this.bannerWidth = ScreenAdapterUtils.getScreenWidth() -
                2 * UIUtils.dip2px(mContext.get(), 10);
        this.bannerHeight = (int) ScreenAdapterUtils
                .getHeight(bannerWidth, 710 / 285f);
    }

    @Override
    protected RecyclerView.ViewHolder getHeaderViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HeadViewHolder(
                mInflater.inflate(R.layout.item_invite_title, parent, false)
        );
    }

    @Override
    protected RecyclerView.ViewHolder getContentViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ContentViewHolder(
                mInflater.inflate(R.layout.item_invite_content, parent, false)
        );
    }

    @Override
    protected RecyclerView.ViewHolder getEmptyViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new EmptyViewHolder(
                mInflater.inflate(R.layout.blank_empty_tip, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder,
                                 int position) {

        if (holder instanceof HeadViewHolder) {

            HeadViewHolder headViewHolder = (HeadViewHolder) holder;

            headViewHolder.tvCodeInfo.setText(mHeader.getReferralCode());
            headViewHolder.tvUrlInfo.setText(mHeader.getDomainName());
            headViewHolder.tvCopyUrl.setOnClickListener(v -> {
                ClipData clipData = ClipData.newPlainText("url", mHeader.getDomainName());
                mClipboard.setPrimaryClip(clipData);
                ToastUtils.show(mContext.get().getString(R.string.edit_copy_suc));
            });

            headViewHolder.tvScaleCode.setOnClickListener(v -> {
                if (mFragment != null && mFragment.get() != null) {
                    mFragment.get().showQrCodeFragment();
                }
            });

            int width = UIUtils.dip2px(mContext.get(), 100);
            Bitmap codeBitmap = ZxingUtils.createBitmap(mHeader.getReferralLink(), width, width);
            headViewHolder.ivCode.setImageBitmap(codeBitmap);

            headViewHolder.tvTitleNum.setText(
                    String.format(mContext.get().getString(R.string.invite_num_title), mHeader.getTotalNum())
            );

            // 邀请规则的html按钮
            headViewHolder.tvRule.setOnClickListener(v -> {

                    }
            );

            // Banner 图宽高设置
            ViewGroup.LayoutParams params = headViewHolder.ivBanner.getLayoutParams();
            params.width = bannerWidth;
            params.height = bannerHeight;
            headViewHolder.ivBanner.setLayoutParams(params);
            // Banner 文字边距设置
            RelativeLayout.LayoutParams bannerLp
                    = new RelativeLayout.LayoutParams(headViewHolder.tvBannerTip.getLayoutParams());
            bannerLp.setMargins((int) (bannerWidth * 0.1f), (int) (bannerHeight * 0.3f), 0, 0);
            headViewHolder.tvBannerTip.setLayoutParams(bannerLp);

            RelativeLayout.LayoutParams bannerShadowLp
                    = new RelativeLayout.LayoutParams(headViewHolder.tvBannerTip.getLayoutParams());
            bannerShadowLp.setMargins((int) (bannerWidth * 0.1f) + offset,
                    (int) (bannerHeight * 0.3f) + offset, 0, 0);
            headViewHolder.tvBannerTipShadow.setLayoutParams(bannerShadowLp);

            String bannerTip = String.format(mContext.get()
                    .getString(R.string.invite_banner_tip), mHeader.getScoreNumber());
            headViewHolder.tvBannerTip.setText(bannerTip);
            headViewHolder.tvBannerTipShadow.setText(bannerTip);

        } else if (holder instanceof ContentViewHolder) {

            InviteResp.ReferralListBean referralListBean = mDataList.get(position - 1);

            ContentViewHolder contentViewHolder = (ContentViewHolder) holder;
            contentViewHolder.tvAccount.setText(referralListBean.getEmail());
            contentViewHolder.tvTime.setText(
                    DateUtils.getY_M_D_ViaTimestamp(referralListBean.getCreatedAt() * 1000L)
            );

        } else {
            handleEmptyTip(holder, R.string.invite_empty_tip);
        }

    }

    public static class HeadViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_banner)
        ImageView ivBanner;
        @BindView(R.id.tv_banner_tip)
        TextView tvBannerTip;
        @BindView(R.id.tv_banner_tip_shadow)
        TextView tvBannerTipShadow;
        @BindView(R.id.v_tag)
        View vTag;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_rule)
        TextView tvRule;
        @BindView(R.id.tv_invite_code)
        TextView tvInviteCode;
        @BindView(R.id.tv_code_info)
        TextView tvCodeInfo;
        @BindView(R.id.tv_url)
        TextView tvUrl;
        @BindView(R.id.tv_copy_url)
        TextView tvCopyUrl;
        @BindView(R.id.tv_url_info)
        TextView tvUrlInfo;
        @BindView(R.id.tv_type_2)
        TextView tvType2;
        @BindView(R.id.tv_invite_scan)
        TextView tvInviteScan;
        @BindView(R.id.tv_scale_code)
        TextView tvScaleCode;
        @BindView(R.id.iv_code)
        ImageView ivCode;
        @BindView(R.id.tv_title_num)
        TextView tvTitleNum;

        public HeadViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

    public static class ContentViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tv_account)
        TextView tvAccount;

        public ContentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

}
