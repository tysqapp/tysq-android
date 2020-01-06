package com.tysq.ty_android.feature.articleExam;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.bit.adapter.BitFrameAdapter;
import com.bit.callback.StateViewHolderListener;
import com.tysq.ty_android.R;
import com.tysq.ty_android.app.TyApplication;
import com.tysq.ty_android.base.SimpleLoadMoreFragment;
import com.tysq.ty_android.base.activity.CommonToolbarActivity;
import com.tysq.ty_android.feature.articleExam.di.ArticleExamModule;
import com.tysq.ty_android.feature.articleExam.di.DaggerArticleExamComponent;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import eventbus.ExamOverEvent;
import response.article.ReviewArticleListResp;
import response.common.TitleCountVO;

/**
 * author       : xxxx
 * time         : 2019-08-28 10:58
 * desc         : 待审核文章
 * version      : 1.0.0
 */

public final class ArticleExamFragment
        extends SimpleLoadMoreFragment<ArticleExamPresenter, ReviewArticleListResp.ReviewArticlesBean>
        implements ArticleExamView, CommonToolbarActivity.ICommonFragment, StateViewHolderListener {

    public static final String TAG = "ArticleExamFragment";
    private final TitleCountVO mTitleCountVO = new TitleCountVO();

    public static ArticleExamFragment newInstance() {
        Bundle args = new Bundle();

        ArticleExamFragment fragment = new ArticleExamFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void registerDagger() {
        DaggerArticleExamComponent.builder()
                .appComponent(TyApplication.getAppComponent())
                .articleExamModule(new ArticleExamModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int getTitleId() {
        return R.string.exam_title;
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return new ArticleExamAdapter(getContext(), mTitleCountVO, mData);
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        mBaseAdapter.setStateViewHolderListener(this);
    }

    @Override
    protected void loadData(int start, int pageSize, boolean isFirst) {
        mPresenter.loadExamArticle(start, pageSize, isFirst);
    }

    @Override
    protected boolean isNeedEmpty() {
        return true;
    }

    @Override
    public void onLoadExamArticle(List<ReviewArticleListResp.ReviewArticlesBean> value,
                                  int totalNum,
                                  boolean isFirst) {

        mTitleCountVO.setCount(totalNum);

        onHandleResponseData(value, isFirst);

    }

    @Override
    public void onLoadExamArticleError(boolean isFirst) {
        onHandleError(isFirst);
    }

    @Override
    protected int getEmptyView() {
        return R.layout.blank_list_full;
    }


    @Override
    public void handleEmptyViewHolder(BitFrameAdapter.EmptyViewHolder holder) {
        holder.itemView.findViewById(R.id.blank_refresh_btn).setVisibility(View.GONE);
        ((TextView) holder.itemView.findViewById(R.id.blank_tip))
                .setText(getString(R.string.blank_no_exam_article));
    }

    @Override
    public void handleRetryViewHolder(BitFrameAdapter.RetryViewHolder holder) {

    }

    @Override
    public void handleLoadingViewHolder(BitFrameAdapter.LoadingViewHolder holder) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdateExamList(ExamOverEvent event) {

        int index = -1;

        for (int i = 0; i < mData.size(); i++) {
            ReviewArticleListResp.ReviewArticlesBean articleInfo = mData.get(i);

            if (articleInfo.getArticleId() == null) {
                continue;
            }
            if (articleInfo.getArticleId().equals(event.getArticleId())) {
                index = i;
                break;
            }

        }

        if (index == -1) {
            return;
        }

        mTitleCountVO.reduceTotal();

        mData.remove(index);
        mBaseAdapter.notifyItemChanged(1);
        mBaseAdapter.notifyItemRemoved(2 + index);
    }

}
