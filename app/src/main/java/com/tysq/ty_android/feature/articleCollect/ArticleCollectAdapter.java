package com.tysq.ty_android.feature.articleCollect;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.bit.utils.DateUtils;
import com.tysq.ty_android.R;
import com.tysq.ty_android.base.adapter.CommonHeaderSimpleAdapter;
import com.tysq.ty_android.feature.articleDetail.activity.ArticleDetailActivity;
import com.zinc.jrecycleview.config.JRecycleConfig;
import com.zinc.jrecycleview.swipe.JSwipeViewHolder;

import java.lang.ref.WeakReference;
import java.util.List;

import response.article.ArticleCollectResp;

/**
 * author       : frog
 * time         : 2019-08-12 11:21
 * desc         : 我的收藏
 * version      : 1.3.0
 */
public class ArticleCollectAdapter
        extends CommonHeaderSimpleAdapter<ArticleCollectFragment.MyHeader, ArticleCollectResp.CollectsBean> {

    private WeakReference<ArticleCollectListener> mListener;

    private boolean mIsNeedSwipe;

    public ArticleCollectAdapter(Context context,
                                 ArticleCollectFragment.MyHeader header,
                                 List<ArticleCollectResp.CollectsBean> collectsBeans,
                                 boolean isNeedHeader,
                                 boolean isNeedSwipe) {
        super(context, header, collectsBeans, isNeedHeader);

        mIsNeedSwipe = isNeedSwipe;
    }

    @Override
    protected RecyclerView.ViewHolder getHeaderViewHolder(@NonNull ViewGroup parent,
                                                          int viewType) {
        return new HeadViewHolder(
                mInflater.inflate(R.layout.item_common_count_title, parent, false)
        );
    }

    @Override
    protected RecyclerView.ViewHolder getContentViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ContentViewHolder(
                mInflater.inflate(JRecycleConfig.SWIPE_LAYOUT, parent, false)
        );
    }

    @Override
    protected RecyclerView.ViewHolder getEmptyViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof HeadViewHolder) {
            HeadViewHolder headViewHolder = (HeadViewHolder) holder;

            String title = mContext.get().getString(R.string.my_collect_title);
            headViewHolder.tvTitle.setText(String.format(title, mHeader.getTotalNum()));
        } else if (holder instanceof ContentViewHolder) {

            int realPos = position - getHeaderCount();
            ArticleCollectResp.CollectsBean collectsBean = mDataList.get(realPos);
            final ContentViewHolder contentViewHolder = (ContentViewHolder) holder;
            contentViewHolder
                    .tvTime
                    .setText(DateUtils
                            .getTimeStringViaTimestamp(collectsBean.getCreateTime() * 1000L));

            contentViewHolder.tvTitle.setText(collectsBean.getTitle());

            contentViewHolder.tvDelete.setText(mContext.get().getString(R.string.my_collect_cancel));

            contentViewHolder.itemView.setOnClickListener(v -> {
                ArticleDetailActivity.startActivity(mContext.get(),
                        collectsBean.getArticleId()
                );
            });

            contentViewHolder.tvDelete.setOnClickListener(v -> {
                if (mListener != null && mListener.get() != null) {
                    mListener.get().cancelCollect(collectsBean.getArticleId());
                }
                contentViewHolder.getSwipeItemLayout().close();
            });

            contentViewHolder.getSwipeItemLayout().setSwipeEnable(mIsNeedSwipe);

        }
    }

    public void setListener(ArticleCollectListener listener) {
        this.mListener = new WeakReference<>(listener);
    }

    class HeadViewHolder extends RecyclerView.ViewHolder {

        private TextView tvTitle;

        HeadViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
        }

    }

    class ContentViewHolder extends JSwipeViewHolder {

        private TextView tvDelete;
        private TextView tvTitle;
        private TextView tvTime;

        ContentViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public int getRightMenuLayout() {
            return R.layout.menu_right_delete;
        }

        @Override
        public int getContentLayout() {
            return R.layout.item_article_collect_content;
        }

        @Override
        public void initItem(FrameLayout frameLayout) {
            tvTitle = frameLayout.findViewById(R.id.tv_title);
            tvTime = frameLayout.findViewById(R.id.tv_time);
            tvDelete = frameLayout.findViewById(R.id.tv_delete);
        }

    }
}
