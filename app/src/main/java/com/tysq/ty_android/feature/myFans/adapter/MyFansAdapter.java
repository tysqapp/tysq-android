package com.tysq.ty_android.feature.myFans.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bit.view.fragment.BitBaseFragment;
import com.tysq.ty_android.R;
import com.tysq.ty_android.base.adapter.CommonHeaderSimpleAdapter;
import com.tysq.ty_android.config.Constant;
import com.tysq.ty_android.feature.login.LoginActivity;
import com.tysq.ty_android.feature.myFans.adapter.viewHolder.ContentViewHolder;
import com.tysq.ty_android.feature.myFans.adapter.viewHolder.TitleViewHolder;
import com.tysq.ty_android.feature.myFans.listener.MyFansAttentionClickListener;
import com.tysq.ty_android.feature.personalHomePage.PersonalHomePageActivity;
import com.tysq.ty_android.local.sp.UserCache;
import com.tysq.ty_android.utils.TyUtils;

import java.lang.ref.WeakReference;
import java.util.List;

import response.MyFansListResp;
import response.common.TitleCountVO;

import static com.tysq.ty_android.app.TyApplication.getContext;

/**
 * author       : liaozhenlin
 * time         : 2019/10/17 11:14
 * desc         :
 * version      : 1.5.0
 */
public class MyFansAdapter
        extends CommonHeaderSimpleAdapter<TitleCountVO, MyFansListResp.AttentionInfoBean> {

    private MyFansAttentionClickListener mListener;
    private WeakReference<BitBaseFragment> mFragment;

    public MyFansAdapter(BitBaseFragment fragment,
                         Context context,
                         TitleCountVO header,
                         List<MyFansListResp.AttentionInfoBean> attentionInfoBeans,
                         MyFansAttentionClickListener listener,
                         boolean isNeedHeader) {
        super(context, header, attentionInfoBeans, isNeedHeader);
        this.mListener = listener;
        this.mFragment = new WeakReference<>(fragment);
    }


    @Override
    protected RecyclerView.ViewHolder getHeaderViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TitleViewHolder(
                mInflater.inflate(R.layout.item_common_count_title, parent, false));
    }

    @Override
    protected RecyclerView.ViewHolder getContentViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ContentViewHolder(
                mInflater.inflate(R.layout.item_my_attention_or_fans_content, parent, false));
    }

    @Override
    protected RecyclerView.ViewHolder getEmptyViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TitleViewHolder) {
            TitleViewHolder titleViewHolder = (TitleViewHolder) holder;

            String format = String.format(mContext.get()
                    .getString(R.string.my_fans_num), mHeader.getCount());

            titleViewHolder.tvTitle.setText(format);
        } else if (holder instanceof ContentViewHolder) {
            ContentViewHolder contentViewHolder = (ContentViewHolder) holder;

            MyFansListResp.AttentionInfoBean attentionInfoBean
                    = mDataList.get(position - getHeaderCount());

            contentViewHolder.tvName.setText(attentionInfoBean.getAccountName());
            if (attentionInfoBean.getPersonalProfile().trim().length() <= 0) {
                contentViewHolder.tvIntroduction.setVisibility(View.GONE);
            } else {
                contentViewHolder.tvIntroduction.setVisibility(View.VISIBLE);
                contentViewHolder.tvIntroduction.setText(attentionInfoBean.getPersonalProfile());
            }


            contentViewHolder.articleNum.setText(String.valueOf(attentionInfoBean.getArticleNum()));
            contentViewHolder.collectNum.setText(String.valueOf(attentionInfoBean.getCollectedNum()));
            contentViewHolder.readNum.setText(String.valueOf(attentionInfoBean.getReadedNum()));

            TyUtils.initUserPhoto(
                    mFragment.get(),
                    mContext.get(),
                    attentionInfoBean.getHeadUrl(),
                    contentViewHolder.ivPhoto);

            //改变按钮的样式

            if (UserCache.getDefault() == null) {
                changeBtnAttention(false,
                        contentViewHolder.tvFocusSomeOne,
                        contentViewHolder.ivAdd,
                        contentViewHolder.rlFocusSomeOne);
            } else {
                changeBtnAttention(attentionInfoBean.isFollow(),
                        contentViewHolder.tvFocusSomeOne,
                        contentViewHolder.ivAdd,
                        contentViewHolder.rlFocusSomeOne);
            }


            contentViewHolder.rlFocusSomeOne.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (UserCache.getDefault() == null) {
                        LoginActivity.startActivity(mContext.get(), "");
                        return;
                    }

                    attentionInfoBean.setFollow(!attentionInfoBean.isFollow());

                    changeBtnAttention(attentionInfoBean.isFollow(),
                            contentViewHolder.tvFocusSomeOne,
                            contentViewHolder.ivAdd,
                            contentViewHolder.rlFocusSomeOne);

                    mListener.onMyFansAttentionClick(view,
                            attentionInfoBean.getAccountId(),
                            attentionInfoBean.isFollow());
                }
            });

            Integer gradeResource = Constant.LV_MAP.get(attentionInfoBean.getGrade());
            if (gradeResource == null) {
                gradeResource = Constant.LV_MAP.get(Constant.DEFAULT_GRADE);
            }
            contentViewHolder.ivLv.setImageDrawable(ContextCompat
                    .getDrawable(mContext.get(), gradeResource));

            contentViewHolder.llMyAttention.setOnClickListener(view ->
                    PersonalHomePageActivity.startActivity(
                            mContext.get(), attentionInfoBean.getAccountId())
            );
        }
    }

    /**
     * 改变关注状态
     *
     * @param isAttention 是否关注
     * @param button      关注按钮
     * @param add         +图片
     * @param layout      父布局
     */
    private void changeBtnAttention(boolean isAttention, TextView button, ImageView add, RelativeLayout layout) {
        if (!isAttention) {
            layout.setVisibility(View.VISIBLE);
            layout.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.shape_blue_rect));
            button.setText(R.string.personal_focus_someone);
            add.setVisibility(View.VISIBLE);
            button.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
        } else {
            layout.setVisibility(View.VISIBLE);
            layout.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.shape_dark_gray_fill_4dp));
            button.setText(R.string.personal_focused);
            button.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) button.getLayoutParams();
            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            add.setVisibility(View.GONE);
        }
    }

}
