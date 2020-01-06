package com.tysq.ty_android.feature.articleCollect;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.tysq.ty_android.R;
import com.tysq.ty_android.app.TyApplication;
import com.tysq.ty_android.feature.articleCollect.di.ArticleCollectModule;

import com.tysq.ty_android.base.SimpleLoadMoreFragment;
import com.tysq.ty_android.local.sp.UserCache;
import com.tysq.ty_android.feature.articleCollect.di.DaggerArticleCollectComponent;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.Override;
import java.util.List;

import eventbus.ArticleCollectEvent;
import eventbus.DeleteArticleEvent;
import response.article.ArticleCollectResp;

/**
 * author       : frog
 * time         : 2019-08-12 12:17
 * desc         : 我的收藏
 * version      : 1.3.0
 */

public final class ArticleCollectFragment
        extends SimpleLoadMoreFragment<ArticleCollectPresenter, ArticleCollectResp.CollectsBean>
        implements ArticleCollectView, ArticleCollectListener {

    public static final String TAG = "ArticleCollectFragment";

    private MyHeader myHeader = new MyHeader(0);

    public static ArticleCollectFragment newInstance() {
        return newInstance(UserCache.getDefault().getAccountId(),
                true,
                true,
                true);
    }

    public static ArticleCollectFragment newInstance(int userId,
                                                     boolean isNeedHeader,
                                                     boolean isNeedRefresh,
                                                     boolean isNeedSwipe) {
        Bundle args = new Bundle();
        args.putInt(USER_ID, userId);
        args.putBoolean(IS_NEED_HEADER, isNeedHeader);
        args.putBoolean(IS_NEED_REFRESH, isNeedRefresh);
        args.putBoolean(IS_NEED_SWIPE, isNeedSwipe);

        ArticleCollectFragment fragment = new ArticleCollectFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void registerDagger() {
        DaggerArticleCollectComponent.builder()
                .appComponent(TyApplication.getAppComponent())
                .articleCollectModule(new ArticleCollectModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected void loadData(int start, int pageSize, boolean isFirst) {
        mPresenter.getArticleCollect(userId, start, pageSize, isFirst);
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        ArticleCollectAdapter adapter = new ArticleCollectAdapter(getContext(),
                myHeader,
                mData,
                isNeedHeader,
                isNeedSwipe);
        adapter.setListener(this);
        return adapter;
    }

    @Override
    public void onGetArticleCollectError(boolean isFirst) {
        onHandleError(isFirst);
    }

    @Override
    public void onGetArticleCollect(boolean isFirst,
                                    List<ArticleCollectResp.CollectsBean> collects,
                                    int totalNum) {
        myHeader.setTotalNum(totalNum);
        onHandleResponseData(collects, isFirst);
    }

    @Override
    public void onCancelCollectError(String articleId) {
        mBaseAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCancelCollect(String articleId) {

        int pos = findArticleItemViaId(articleId);

        // 没找到则终止
        if (pos == -1) {
            return;
        }

        // 清理数据
        mData.remove(pos);
        myHeader.reduceTotal();
        size -= 1;
        if (size <= 0) {
            size = 0;
        }

        // 如果数量到达0，则重新load一下
        if (mData.size() <= 0 || myHeader.getTotalNum() <= 0 || size <= 0) {
            mBaseAdapter.onLoading();
            loadData(size, PAGE_SIZE, true);
            return;
        }

        pos += requestRefresh() ? 1 : 0;
        // 头部
        pos += 1;
        mBaseAdapter.notifyItemRemoved(pos);
    }

    @Override
    public int getTitleId() {
        return R.string.my_collect;
    }

    @Override
    protected boolean isNeedEmpty() {
        return true;
    }

    @Override
    protected int getEmptyView() {
        return R.layout.blank_empty_my_collect;
    }

    @Override
    public void cancelCollect(String articleId) {
        showDialog();
        mPresenter.putCancelCollect(articleId);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void removeItem(ArticleCollectEvent event) {
        onCancelCollect(event.getArticleId());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void removeItem(DeleteArticleEvent event) {
        onCancelCollect(event.getArticleId());
    }

    /**
     * 通过 文章id 来找文章的下标
     */
    private int findArticleItemViaId(String articleId) {
        int pos = -1;
        for (int i = 0; i < mData.size(); i++) {
            ArticleCollectResp.CollectsBean collectsBean = mData.get(i);
            if (collectsBean.getArticleId().equals(articleId)) {
                pos = i;
                break;
            }
        }

        return pos;
    }

    public static class MyHeader {
        int totalNum;

        public MyHeader(int totalNum) {
            this.totalNum = totalNum;
        }

        public int getTotalNum() {
            return totalNum;
        }

        public void setTotalNum(int totalNum) {
            this.totalNum = totalNum;
        }

        public void reduceTotal() {
            totalNum -= 1;
            if (totalNum <= 0) {
                totalNum = 0;
            }
        }
    }


}
