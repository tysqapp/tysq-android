package com.tysq.ty_android.feature.articleList;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abc.lib_utils.toast.ToastUtils;
import com.bit.view.fragment.BitLoadMoreFragment;
import com.tysq.ty_android.R;
import com.tysq.ty_android.app.TyApplication;
import com.tysq.ty_android.config.Constant;
import com.tysq.ty_android.config.TyConfig;
import com.tysq.ty_android.feature.articleDetail.activity.ArticleDetailActivity;
import com.tysq.ty_android.feature.articleList.di.ArticleListModule;
import com.tysq.ty_android.feature.articleList.di.DaggerArticleListComponent;
import com.tysq.ty_android.feature.articleList.dialog.PageDialog;
import com.tysq.ty_android.feature.articleList.listener.OnArticleItemClickListener;
import com.tysq.ty_android.feature.homePageSearch.HomePageSearchActivity;
import com.tysq.ty_android.feature.topArticleList.TopArticleListActivity;
import com.tysq.ty_android.login.CheckLogin;
import com.zinc.jrecycleview.listener.JRecycleListener;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import eventbus.DeleteArticleEvent;
import eventbus.SubCategoryChangeEvent;
import eventbus.UpdateArticleInfoEvent;
import response.AdResp;
import response.TopArticleResp;
import response.home.ArticleInfo;
import response.home.PageInfo;
import vo.SortVO;

/**
 * author       : frog
 * time         : 2019/5/23 下午2:46
 * desc         : 文章列表
 * version      : 1.3.0
 */

public final class ArticleListFragment
        extends BitLoadMoreFragment<ArticleListPresenter>
        implements ArticleListView, OnArticleItemClickListener, View.OnClickListener {

    private static final boolean IS_NEED_REFRESH = true;

    private static final int PAGE_SIZE = 20;

    private final List<ArticleInfo> mData = new ArrayList<>();
    private final List<AdResp.AdvertisementListBean> mAdList = new ArrayList<>();
    private final List<SortVO> mSortList = new ArrayList<>();
    private final List<TopArticleResp.TopArticleBean> mTopArticleList = new ArrayList<>();


    @BindView(R.id.rl_sort)
    RelativeLayout rlSort;
    @BindView(R.id.tv_composite)
    TextView tvComposite;
    @BindView(R.id.tv_new)
    TextView tvNew;
    @BindView(R.id.tv_hot)
    TextView tvHot;
    @BindView(R.id.tv_search)
    TextView tvSearch;

    private final PageInfo mPageInfo = new PageInfo(0, 1, PAGE_SIZE);

    private int mSelTopId;
    private int mSelSubId;

    private ArticleListAdapter mAdapter;

    private JRecycleListener mListener;

    private int unselectColor;
    private int selectColor;


    public static ArticleListFragment newInstance(int topId, int subId) {

        Bundle args = new Bundle();
        args.putInt(TyConfig.TOP_CATEGORY, topId);
        args.putInt(TyConfig.SUB_CATEGORY, subId);

        ArticleListFragment fragment = new ArticleListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected View onCreateFragmentView(LayoutInflater inflater,
                                        @Nullable ViewGroup container,
                                        @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_article_list, container, false);
    }

    @Override
    protected void initArgs(Bundle arguments) {
        super.initArgs(arguments);
        mSelTopId = arguments.getInt(TyConfig.TOP_CATEGORY);
        mSelSubId = arguments.getInt(TyConfig.SUB_CATEGORY);


    }

    @Override
    protected boolean requestLoadMore() {
        return false;
    }

    @Override
    public void getFirstData(int type) {

        if (type != LoadType.RETRY
                && type != LoadType.CHANGE_PAGE) {
            mPageInfo.setCurPage(1);
        }

        if (type == LoadType.CHANGE_PAGE) {
            mBaseAdapter.onLoading();
        }

        mPresenter.loadArticleList(mSelTopId,
                mSelSubId,
                getCurType(),
                getStartIndex(),
                mPageInfo.getPageSize(),
                mPageInfo,
                true);

    }

    @Override
    public void getLoadMoreData() {
        mPresenter.loadArticleList(mSelTopId,
                mSelSubId,
                getCurType(),
                getStartIndex(),
                mPageInfo.getPageSize(),
                mPageInfo,
                false);
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        mAdapter = new ArticleListAdapter(
                this,
                getContext(),
                this,
                mData,
                mAdList,
                mSortList,
                mPageInfo,
                mTopArticleList);
        return mAdapter;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);

        unselectColor = ContextCompat.getColor(getContext(), R.color.tip_text_color);
        selectColor = ContextCompat.getColor(getContext(), R.color.select_text_color);

        if (mListener != null) {
            mRecycleView.setListener(mListener);
        }

        mSortList.add(
                new SortVO(
                        Constant.SortType.COMPOSITE,
                        getString(R.string.article_list_composite),
                        true)
        );
        mSortList.add(
                new SortVO(
                        Constant.SortType.NEW,
                        getString(R.string.article_list_new),
                        false)
        );
        mSortList.add(
                new SortVO(
                        Constant.SortType.HOT,
                        getString(R.string.article_list_hot),
                        false)
        );

        handleSort();

        tvComposite.setOnClickListener(this);
        tvHot.setOnClickListener(this);
        tvNew.setOnClickListener(this);
        tvSearch.setOnClickListener(this);

        mRecycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                View firstView = recyclerView.getLayoutManager().getChildAt(0);
                if (firstView == null) {
                    return;
                }

                RecyclerView.ViewHolder childViewHolder = recyclerView.getChildViewHolder(firstView);

                if (childViewHolder == null) {
                    return;
                }

                if (childViewHolder instanceof ArticleListAdapter.ImageViewHolder
                        || childViewHolder instanceof ArticleListAdapter.VideoViewHolder
                        || childViewHolder instanceof ArticleListAdapter.SortViewHolder
                        || childViewHolder instanceof ArticleListAdapter.TopArticleViewHolder
                        || childViewHolder instanceof ArticleListAdapter.TextViewHolder
                        || childViewHolder instanceof ArticleListAdapter.PageAdvertisementViewHolder) {
                    rlSort.setVisibility(View.VISIBLE);
                } else {
                    rlSort.setVisibility(View.GONE);
                }
            }
        });

        Log.d(TAG, "mSelSubId"+ mSelSubId);
    }

    public void setListener(JRecycleListener listener) {
        this.mListener = listener;
        if (mRecycleView != null && listener != null) {
            mRecycleView.setListener(listener);
        }
    }

    @Override
    protected void registerDagger() {
        DaggerArticleListComponent.builder()
                .appComponent(TyApplication.getAppComponent())
                .articleListModule(new ArticleListModule(this))
                .build()
                .inject(this);
    }

    /**
     * 获取开始下标
     */
    private long getStartIndex() {
        long curPage = mPageInfo.getCurPage() - 1;
        if (curPage < 0) {
            curPage = 0;
        }
        return curPage * mPageInfo.getPageSize();
    }

    /**
     * 第一次拉取回调
     *
     * @param articleInfoList   文章列表
     * @param advertisementList
     */
    @Override
    public void onLoadArticleList(List<ArticleInfo> articleInfoList,
                                  List<AdResp.AdvertisementListBean> advertisementList,
                                  List<TopArticleResp.TopArticleBean> topArticleList,
                                  int topId, int subId) {
        this.mAdList.clear();
        if (advertisementList != null) {
            this.mAdList.addAll(advertisementList);
        }
        if (this.mAdapter != null) {
            this.mAdapter.refreshAd();
        }

        this.mTopArticleList.clear();
        if (topArticleList != null) {
            this.mTopArticleList.addAll(topArticleList);
        }
        if (this.mAdapter != null) {
            this.mAdapter.refreshTopArticle();
        }

        mSelTopId = topId;
        mSelSubId = subId;

        handleData(articleInfoList, true);
    }


    /**
     * 第一次加载失败
     */
    @Override
    public void onError() {
        mBaseAdapter.onError();
    }

    @Override
    public void onErrorLoadMore() {
        mBaseAdapter.setLoadError();
    }

    @Override
    public void onLoadMoreArticleList(List<ArticleInfo> articleInfoList) {
        handleData(articleInfoList, false);
    }

    /**
     * 处理加载数据
     */
    private void handleData(List<ArticleInfo> articleInfoList,
                            boolean isFirst) {

        int size = articleInfoList == null ? 0 : articleInfoList.size();

        mData.clear();

        // 如果数据为空，且为第一次
        if (size <= 0 && isFirst) {
            mBaseAdapter.onEmpty();
            return;
        }

        // 添加公告
        if (isExistAd()) {
            ArticleInfo adInfo = new ArticleInfo();
            adInfo.setType(ArticleInfo.AD);
            mData.add(adInfo);
        }

        // 添加排序
        ArticleInfo articleInfo = new ArticleInfo();
        articleInfo.setType(ArticleInfo.SORT);
        mData.add(articleInfo);

        // 添加指定给文章
        if (isExistTop()) {
            ArticleInfo topInfo = new ArticleInfo();
            topInfo.setType(ArticleInfo.TOP_ARTICLE);
            mData.add(topInfo);
        }

        mData.addAll(articleInfoList);

        // 小于页数大小则，没有上啦加载更多
        if (size < mPageInfo.getPageSize()) {
            // 没有更多了
            mBaseAdapter.onSuccess();
            mBaseAdapter.setNoMore();
            return;
        }

        mBaseAdapter.onSuccess();
        mBaseAdapter.setLoadComplete();

    }

    /**
     * 是否存在公告
     */
    private boolean isExistAd() {
        return mAdList.size() > 0;
    }

    private boolean isExistTop() {
        return mTopArticleList.size() > 0;
    }

    @Override
    @CheckLogin
    public void onArticleItemClick(View view, int position) {

        ArticleInfo info = mData.get(position);
        if (info == null) {
            ToastUtils.show(getString(R.string.article_no_find));
            return;
        }

        Log.e(TAG, "onArticleItemClick: " + info.toString());

        ArticleDetailActivity.startActivity(getContext(), info.getId());

    }

    @Override
    public void onSortItemClick(SortVO sortVO) {
        Log.i(TAG, "onSortItemClick: " + sortVO.getType());

        sortVO.setSelect(true);

        //如果不是综合，则不显示置顶文章
        if (sortVO.getType() == Constant.SortType.NEW){

        }
        handleSortItemShow(sortVO.getType());

        mBaseAdapter.onLoading();
        getFirstData(LoadType.CUSTOM);
        rlSort.setVisibility(View.GONE);
    }

    @Override
    public void onChangePage() {
        mBaseAdapter.onLoading();
        mData.clear();
        getLoadMoreData();
    }

    @Override
    public void onShowPageDialog() {
        PageDialog
                .newInstance(mPageInfo)
                .setFragment(this)
                .show(getActivity());
    }

    @Override
    public void onTopArticleList() {
        Log.d(TAG, "topId:" + mSelTopId + "mSelSubId"+ mSelSubId);
        TopArticleListActivity.startActivity(getContext(), mSelTopId, mSelSubId);
    }

    @Override
    public void onSearch() {
        HomePageSearchActivity.startActivity(getContext());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public synchronized void onReloadData(SubCategoryChangeEvent event) {

        // 一级id 不同，不理会
        if (event.getTopId() != mSelTopId) {
            return;
        }

        // 二级id 相同，不理会
        if (event.getSubId() == mSelSubId) {
            return;
        }

        Log.i(TAG, "onReloadData: " + event.toString());

        mSelSubId = event.getSubId();

        Log.d(TAG,"event" + mSelSubId);

        mData.clear();
        mBaseAdapter.onLoading();

        getFirstData(LoadType.CUSTOM);

    }

    @Override
    protected boolean requestRefresh() {
        return IS_NEED_REFRESH;
    }

    /**
     * 更新信息
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateInfo(UpdateArticleInfoEvent event) {

        int articleIndex = -1;
        for (int i = 0; i < mData.size(); i++) {
            ArticleInfo info = mData.get(i);

            if (event.getArticleId().equals(info.getId())) {
                articleIndex = i;
                break;
            }
        }

        // 没找到对应的文章
        if (articleIndex == -1) {
            return;
        }

        ArticleInfo info = mData.get(articleIndex);
        boolean isNeedRefresh = false;
        if (event.getReadNum() != UpdateArticleInfoEvent.NONE) {
            info.setReadNumber(event.getReadNum());
            isNeedRefresh = true;
        }

        if (event.getCommentNum() != UpdateArticleInfoEvent.NONE) {
            info.setCommentNumber(event.getCommentNum());
            isNeedRefresh = true;
        }

        if (isNeedRefresh) {
            mBaseAdapter.notifyItemChanged(getRealPos(articleIndex));
        }

    }

    /**
     * 删除文章
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void deleteArticle(DeleteArticleEvent event) {
        int articleIndex = -1;
        for (int i = 0; i < mData.size(); i++) {
            ArticleInfo info = mData.get(i);
            if (TextUtils.isEmpty(info.getId())) {
                continue;
            }
            if (info.getId().equals(event.getArticleId())) {
                articleIndex = i;
                break;
            }
        }
        if (articleIndex == -1) {
            return;
        }

        mData.remove(articleIndex);

        mBaseAdapter.notifyItemRemoved(getRealPos(articleIndex));
    }

    private int getRealPos(int articleIndex) {
        int pos = articleIndex;
        // 下拉刷新
        pos += IS_NEED_REFRESH ? 1 : 0;
        // banner
        pos += (mAdList.size() <= 0) ? 0 : 1;
        // 排序
//        pos += 1;
        return pos;
    }

    @Override
    protected int getInitRegister() {
        return BUTTER_KNIFE | EVENT_BUS;
    }

    @Override
    protected int getEmptyView() {
        return R.layout.blank_white_empty_article;
    }

    @Override
    protected int getRetryView() {
        return R.layout.blank_net_block;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_composite:
                handleSelectSortItem(Constant.SortType.COMPOSITE);
                break;
            case R.id.tv_new:
                handleSelectSortItem(Constant.SortType.NEW);
                break;
            case R.id.tv_hot:
                handleSelectSortItem(Constant.SortType.HOT);
                break;
            case R.id.tv_search:
                onSearch();
                break;
        }
    }

    /**
     * 获取选择的排序
     */
    private int getCurType() {
        SortVO curSort = null;
        for (SortVO sortVO : mSortList) {
            if (sortVO.isSelect()) {
                curSort = sortVO;
                break;
            }
        }
        if (curSort == null) {
            return Constant.SortType.COMPOSITE;
        }
        return curSort.getType();
    }

    /**
     * 排序
     */
    private void handleSort() {

        SortVO selectSortItem = null;
        for (SortVO sortVO : mSortList) {
            if (sortVO.isSelect()) {
                selectSortItem = sortVO;
                break;
            }
        }

        if (selectSortItem == null) {
            return;
        }

        handleSortItemShow(selectSortItem.getType());

    }

    /**
     * 处理选择 分类
     */
    private void handleSelectSortItem(int type) {
        SortVO curSelect = null;
        for (SortVO sortVO : mSortList) {
            if (sortVO.isSelect()) {
                curSelect = sortVO;
                break;
            }
        }

        // 如果有选择，且选择的项与传入类型相同，则不处理
        if (curSelect != null && curSelect.getType() == type) {
            return;
        }

        if (curSelect != null) {
            curSelect.setSelect(false);
        }

        // 获取本次选择
        for (SortVO sortVO : mSortList) {
            if (sortVO.getType() == type) {
                curSelect = sortVO;
            }
        }

        if (curSelect == null) {
            return;
        }

        onSortItemClick(curSelect);

        int pos = 0;
        boolean existAd = isExistAd();
        pos = existAd ? pos + 1 : pos;

        pos = IS_NEED_REFRESH ? pos + 1 : pos;

        mBaseAdapter.notifyItemChanged(pos);

    }

    /**
     * 处理分类显示
     */
    private void handleSortItemShow(int type) {
        tvComposite.setTextColor(unselectColor);
        tvHot.setTextColor(unselectColor);
        tvNew.setTextColor(unselectColor);

        switch (type) {
            case Constant.SortType.NEW:
                tvNew.setTextColor(selectColor);
                break;

            case Constant.SortType.HOT:
                tvHot.setTextColor(selectColor);
                break;

            case Constant.SortType.COMPOSITE:
                tvComposite.setTextColor(selectColor);
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mAdapter.getWebViewCache().destroyWebView();
    }

    @Override
    public void onPause() {
        super.onPause();
        mAdapter.getWebViewCache().pauseWebView();
    }

    @Override
    public void onResume() {
        super.onResume();
        mAdapter.getWebViewCache().resumeWebView();
    }
}
