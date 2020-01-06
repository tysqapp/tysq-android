package com.tysq.ty_android.feature.articleList;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bit.utils.UIUtils;
import com.bit.view.fragment.BitBaseFragment;
import com.jerry.autorecyclerview.AutoRecyclerView;
import com.jerry.autorecyclerview.ScrollSpeedLinearLayoutManager;
import com.jerry.image_watcher.ImageWatcherActivity;
import com.jerry.image_watcher.model.WatchImageVO;
import com.luck.picture.lib.tools.ScreenUtils;
import com.tysq.ty_android.R;
import com.tysq.ty_android.base.activity.CommonToolbarActivity;
import com.tysq.ty_android.config.Constant;
import com.tysq.ty_android.feature.announcement.AnnouncementFragment;
import com.tysq.ty_android.feature.articleList.listener.OnArticleItemClickListener;
import com.tysq.ty_android.feature.personalHomePage.PersonalHomePageActivity;
import com.tysq.ty_android.local.sp.WebViewCache;
import com.tysq.ty_android.utils.TyUtils;
import com.tysq.ty_android.widget.TagFlowLayout;
import com.zinc.jrecycleview.stick.IStick;
import com.zinc.lib_banner.JerryBanner;
import com.zinc.lib_banner.JerryViewPager;
import com.zinc.lib_jerry_editor.config.glide.GlideOptions;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import response.AdResp;
import response.TopArticleResp;
import response.home.ArticleInfo;
import response.home.PageInfo;
import vo.SortVO;

/**
 * author       : frog
 * time         : 2019/4/26 下午5:48
 * desc         : 文章列表 项 适配器
 * version      : 1.3.0
 */
public class ArticleListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // TagFlow 需要现实的行数
    private static final int SHOW_TAG_LINE = 1;

    private final WeakReference<BitBaseFragment> mFragment;
    private final List<ArticleInfo> mData;
    private final List<AdResp.AdvertisementListBean> mAdList;
    private final List<View> mAdViewList;
    private final List<SortVO> mSortList;
    private final List<View> mTopArticleViewList;
    private final List<TopArticleResp.TopArticleBean> mTopArticleList;

    private final PageInfo mPageInfo;

    private final int dp_10;

    private final LayoutInflater mInflater;

    private ViewGroup.LayoutParams layoutParams;
    private AutoRecyclerViewAdapter autoRecyclerViewAdapter;

    private WeakReference<Context> mContext;
    private OnArticleItemClickListener mListener;

    private int mAdShowIndex = 0;
    private int mTopArticleShowIndex = 0;

    private final int unselectColor;
    private final int selectColor;

    private final WatchImageVO mJerryImageModel = new WatchImageVO();

    private WebViewCache mWebViewCache;

    ArticleListAdapter(BitBaseFragment fragment,
                       Context context,
                       OnArticleItemClickListener listener,
                       List<ArticleInfo> mData,
                       List<AdResp.AdvertisementListBean> adList,
                       List<SortVO> sortList,
                       PageInfo pageInfo,
                       List<TopArticleResp.TopArticleBean> topArticleList) {
        this.mFragment = new WeakReference<>(fragment);
        this.mContext = new WeakReference<>(context);
        this.mInflater = LayoutInflater.from(context);
        this.mListener = listener;
        this.mData = mData;
        this.mAdList = adList;
        this.mSortList = sortList;
        this.mAdViewList = new ArrayList<>();
        this.mPageInfo = pageInfo;
        this.mTopArticleList = topArticleList;
        this.mTopArticleViewList = new ArrayList<>();
        this.mWebViewCache = new WebViewCache(context,
                Constant.HtmlUrl.HOME_AD,
                Constant.AdvertisementNum.HOME_AD);

        this.unselectColor = ContextCompat.getColor(context, R.color.tip_text_color);
        this.selectColor = ContextCompat.getColor(context, R.color.select_text_color);

        this.dp_10 = UIUtils.dip2px(context, 10);

        this.autoRecyclerViewAdapter = new AutoRecyclerViewAdapter(context, mTopArticleList);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder;

        switch (viewType) {
            case ArticleInfo.IMAGE:
                viewHolder = new ImageViewHolder(
                        mInflater.inflate(R.layout.item_home_image_article, parent, false)
                );
                break;
            case ArticleInfo.TEXT:
                viewHolder = new TextViewHolder(
                        mInflater.inflate(R.layout.item_home_text_article, parent, false)
                );
                break;
            case ArticleInfo.VIDEO:
                viewHolder = new VideoViewHolder(
                        mInflater.inflate(R.layout.item_home_video_article, parent, false)
                );
                break;
            case ArticleInfo.AD:
                viewHolder = new AdViewHolder(
                        mInflater.inflate(R.layout.item_banner, parent, false)
                );
                break;
            case ArticleInfo.SORT:
                viewHolder = new SortViewHolder(
                        mInflater.inflate(R.layout.item_sort, parent, false)
                );
                break;
            case ArticleInfo.PAGE:
                viewHolder = new PageTurningViewHolder(
                        mInflater.inflate(R.layout.item_home_page_turning, parent, false)
                );
                break;
            case ArticleInfo.ADVERTISEMENT:
                viewHolder = new PageAdvertisementViewHolder(
                        mInflater.inflate(R.layout.item_article_detail_advertisement, parent, false)
                );
                break;
            case ArticleInfo.TOP_ARTICLE:
                viewHolder = new TopArticleViewHolder(
                        mInflater.inflate(R.layout.item_article_list_top_article, parent, false), mContext.get()
                );
                break;

            default:
                viewHolder = null;
                break;
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder,
                                 int position) {

        if (holder instanceof VideoViewHolder) {
            ArticleInfo info = mData.get(position);
            VideoViewHolder videoViewHolder = (VideoViewHolder) holder;
            handleVideoType(videoViewHolder, info, position);

        } else if (holder instanceof ImageViewHolder) {
            ArticleInfo info = mData.get(position);
            ImageViewHolder imageViewHolder = (ImageViewHolder) holder;

            setImageView(imageViewHolder.ivCover, info.getCoverUrl());
            imageViewHolder.ivCover.setVisibility(View.VISIBLE);

            handleImageView(imageViewHolder, info, position);

        } else if (holder instanceof TextViewHolder) {

            ArticleInfo info = mData.get(position);
            TextViewHolder textViewHolder = (TextViewHolder) holder;

            handleTextView(textViewHolder, info, position);

        } else if (holder instanceof AdViewHolder) {
            AdViewHolder adViewHolder = (AdViewHolder) holder;
            handleAd(adViewHolder);
        } else if (holder instanceof SortViewHolder) {
            SortViewHolder sortViewHolder = (SortViewHolder) holder;
            handleSort(sortViewHolder);
        } else if (holder instanceof PageTurningViewHolder) {
            PageTurningViewHolder pageTurningViewHolder = (PageTurningViewHolder) holder;
            handlePageTurning(pageTurningViewHolder);
        } else if (holder instanceof TopArticleViewHolder) {
            TopArticleViewHolder topArticleViewHolder = (TopArticleViewHolder) holder;
            handleTopArticle(topArticleViewHolder);
        }
    }


    /**
     * 切换页面
     */
    private void handlePageTurning(PageTurningViewHolder pageTurningViewHolder) {
        // 总页数
        long pageLength = mPageInfo.getTotal() / mPageInfo.getPageSize() + 1;

        // 第一页时，"前一页"不能点击
        if (mPageInfo.getCurPage() <= 1) {
            pageTurningViewHolder.tvLastPage.setClickable(false);
            pageTurningViewHolder.tvLastPage.setTextColor(
                    ContextCompat.getColor(mContext.get(), R.color.et_tip_text_color)
            );
            pageTurningViewHolder.tvLastPage.setBackground(
                    ContextCompat.getDrawable(mContext.get(), R.drawable.shape_light_gray_4dp_border)
            );
        } else {
            pageTurningViewHolder.tvLastPage.setClickable(true);
            pageTurningViewHolder.tvLastPage.setTextColor(
                    ContextCompat.getColor(mContext.get(), R.color.main_blue_color)
            );
            pageTurningViewHolder.tvLastPage.setBackground(
                    ContextCompat.getDrawable(mContext.get(), R.drawable.shape_blue_4dp_border)
            );
            pageTurningViewHolder.tvLastPage.setOnClickListener(v -> {
                long curPage = mPageInfo.getCurPage();
                mPageInfo.setCurPage(curPage - 1);

                if (mListener == null) {
                    return;
                }
                mListener.onChangePage();
            });
        }

        // 最后一页时，"后一页"不能点击
        if (mPageInfo.getCurPage() >= pageLength) {
            pageTurningViewHolder.tvNextPage.setClickable(false);
            pageTurningViewHolder.tvNextPage.setTextColor(
                    ContextCompat.getColor(mContext.get(), R.color.et_tip_text_color)
            );
            pageTurningViewHolder.tvNextPage.setBackground(
                    ContextCompat.getDrawable(mContext.get(), R.drawable.shape_light_gray_4dp_border)
            );
        } else {
            pageTurningViewHolder.tvNextPage.setClickable(true);
            pageTurningViewHolder.tvNextPage.setTextColor(
                    ContextCompat.getColor(mContext.get(), R.color.main_blue_color)
            );
            pageTurningViewHolder.tvNextPage.setBackground(
                    ContextCompat.getDrawable(mContext.get(), R.drawable.shape_blue_4dp_border)
            );
            pageTurningViewHolder.tvNextPage.setOnClickListener(v -> {
                long curPage = mPageInfo.getCurPage();
                mPageInfo.setCurPage(curPage + 1);

                if (mListener == null) {
                    return;
                }
                mListener.onChangePage();
            });
        }

        // 总页数只有一页时
        if (pageLength <= 1) {
            pageTurningViewHolder.tvPageCountUnselect.setVisibility(View.VISIBLE);
            pageTurningViewHolder.tvPageCountSelect.setVisibility(View.GONE);

            pageTurningViewHolder.rlPage.setClickable(false);
            pageTurningViewHolder.rlPage.setBackground(
                    ContextCompat.getDrawable(mContext.get(), R.drawable.shape_light_gray_4dp_border)
            );
        } else {
            pageTurningViewHolder.tvPageCountUnselect.setVisibility(View.GONE);
            pageTurningViewHolder.tvPageCountSelect.setVisibility(View.VISIBLE);

            pageTurningViewHolder.rlPage.setBackground(
                    ContextCompat.getDrawable(mContext.get(), R.drawable.shape_blue_4dp_border)
            );

            pageTurningViewHolder.rlPage.setClickable(true);
            pageTurningViewHolder.rlPage.setOnClickListener(v -> {
                if (mListener == null) {
                    return;
                }
                mListener.onShowPageDialog();
            });
        }

        String homePage = mContext.get().getString(R.string.home_page);
        homePage = String.format(homePage, mPageInfo.getCurPage(), pageLength);
        pageTurningViewHolder.tvPageCountSelect.setText(homePage);
        pageTurningViewHolder.tvPageCountUnselect.setText(homePage);

    }

    /**
     * 排序
     */
    private void handleSort(final SortViewHolder sortViewHolder) {

        sortViewHolder.tvComposite.setOnClickListener(v -> {
            handleSelectSortItem(sortViewHolder, Constant.SortType.COMPOSITE);
        });

        sortViewHolder.tvHot.setOnClickListener(v -> {
            handleSelectSortItem(sortViewHolder, Constant.SortType.HOT);
        });

        sortViewHolder.tvNew.setOnClickListener(v -> {
            handleSelectSortItem(sortViewHolder, Constant.SortType.NEW);
        });

        sortViewHolder.tvSearch.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onSearch();
            }
        });

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

        handleSortItemShow(sortViewHolder, selectSortItem.getType());

    }

    /**
     * 处理选择 分类
     */
    private void handleSelectSortItem(SortViewHolder sortViewHolder, int type) {
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

        handleSortItemShow(sortViewHolder, type);
        if (mListener != null) {
            mListener.onSortItemClick(curSelect);
        }

    }

    /**
     * 处理分类显示
     */
    private void handleSortItemShow(SortViewHolder sortViewHolder,
                                    int type) {
        sortViewHolder.tvComposite.setTextColor(unselectColor);
        sortViewHolder.tvHot.setTextColor(unselectColor);
        sortViewHolder.tvNew.setTextColor(unselectColor);

        switch (type) {
            case Constant.SortType.NEW:
                sortViewHolder.tvNew.setTextColor(selectColor);
                break;

            case Constant.SortType.HOT:
                sortViewHolder.tvHot.setTextColor(selectColor);
                break;

            case Constant.SortType.COMPOSITE:
                sortViewHolder.tvComposite.setTextColor(selectColor);
                break;
        }
    }

    /**
     * 刷新公告
     */
    public void refreshAd() {
        mAdShowIndex = 0;

        if (mAdViewList != null) {
            mAdViewList.clear();
        }

        if (mAdList == null || mAdList.size() <= 0 || mAdViewList == null) {
            return;
        }

        for (int i = 0; i < mAdList.size(); i++) {
            AdResp.AdvertisementListBean adResp = mAdList.get(i);

            View item = mInflater.inflate(R.layout.item_ad, null);

            ImageView ivBg = item.findViewById(R.id.iv_bg);
            ivBg.setImageResource(Constant.BANNER_BG_RES.get(i % Constant.BANNER_BG_RES.size()));

            TextView tvAdContent = item.findViewById(R.id.tv_content);
            tvAdContent.setText(adResp.getTitle());

            ImageView ivGoTo = item.findViewById(R.id.iv_go_to);
            if (TextUtils.isEmpty(adResp.getUrl())) {
                ivGoTo.setVisibility(View.GONE);
            } else {
                ivGoTo.setVisibility(View.VISIBLE);
            }

            mAdViewList.add(item);
        }
    }

    private void handleAd(AdViewHolder adViewHolder) {

        adViewHolder.jerryBanner.setData(mAdViewList);
        adViewHolder.jerryBanner.setShowIndicator(true);
        adViewHolder.jerryBanner.setDelegate((banner, itemView, model, position) -> {

            String url = mAdList.get(position).getUrl();
            if (TextUtils.isEmpty(url)) {
                return;
            }

//            String articleId = TyUtils.getArticleId(url);
//            if (articleId == null) {
//                TyWebViewActivity.startActivity(mContext.get(), url);
//            } else {
//                ArticleDetailActivity.startActivity(mContext.get(), articleId);
//            }

            TyUtils.handleWebViewLink(mContext.get(), url);

        });
        adViewHolder.jerryBanner.setAllowUserScrollable(true);
        adViewHolder.jerryBanner.setPageChangeDuration(5_000);

        JerryViewPager viewPager = adViewHolder.jerryBanner.getViewPager();
        viewPager.setClipToPadding(false);
        viewPager.setPadding(0, 0, dp_10, 0);

        adViewHolder.jerryBanner.setAutoPlayAble(true);

        adViewHolder.tvMore.setOnClickListener(v -> {
            CommonToolbarActivity.startActivity(mContext.get(), AnnouncementFragment.TAG);
        });
    }

    /**
     * 刷新置顶文章
     */
    public void refreshTopArticle() {
        mTopArticleShowIndex = 0;

        if (mTopArticleViewList != null) {
            mTopArticleViewList.clear();
        }

        if (mTopArticleList == null || mTopArticleList.size() <= 0) {
            return;
        }

        for (int i = 0; i < mTopArticleList.size(); i++) {
            TopArticleResp.TopArticleBean topArticleBean = mTopArticleList.get(i);

            View item = mInflater.inflate(R.layout.item_article_list_top_article_info, null);

            LinearLayout llTopArticle = item.findViewById(R.id.ll_top_article);
            TextView tvTitle = item.findViewById(R.id.tv_title);

            tvTitle.setText(topArticleBean.getTitle());

            mTopArticleViewList.add(item);
        }
    }

    private void handleTopArticle(TopArticleViewHolder topArticleViewHolder) {

        topArticleViewHolder.llMore.setOnClickListener(v -> mListener.onTopArticleList());

        layoutParams = topArticleViewHolder.recyclerView.getLayoutParams();

        if (mTopArticleList == null || mTopArticleList.size() <= 0) {
            topArticleViewHolder.llTopArticleList.setVisibility(View.GONE);
            return;
        } else {
            if (mTopArticleList.size() == 1) {
                layoutParams.height = ScreenUtils.dip2px(mContext.get(), Constant.TOP_ARTICLE_HEIGHT);
            } else {
                layoutParams.height = ScreenUtils.dip2px(mContext.get(), Constant.TOP_ARTICLE_HEIGHT * 2);
            }
        }

        topArticleViewHolder.recyclerView.setAdapter(autoRecyclerViewAdapter);
        topArticleViewHolder.recyclerView.start();

    }

    /**
     * 图片类型
     *
     * @param holder
     * @param info
     * @param position
     */
    private void handleImageView(ImageViewHolder holder, ArticleInfo info, int position) {

        // 标题
        holder.tvTitle.setText(info.getTitle());

        // 内容
        if (TextUtils.isEmpty(info.getContent())) {
            holder.tvDes.setVisibility(View.GONE);
        } else {
            holder.tvDes.setVisibility(View.VISIBLE);
            holder.tvDes.setText(info.getContent());
        }

        // 作者
        holder.tvAuthor.setText(info.getAuthorName());
        // 阅读量
        holder.tvReadCount.setText(TyUtils.formatNum(info.getReadNumber()));
        // 评论数
        holder.tvReviewCount.setText(TyUtils.formatNum(info.getCommentNumber()));
        // 时间
        holder.tvTime.setText(TyUtils.getPublishTime(info.getCreatedTime()));

        holder.flowLayout.setNeedShowLine(SHOW_TAG_LINE);
        addLabel(info.getLabels(), holder.flowLayout);

        holder.tvAuthor.setOnClickListener(v -> {
            PersonalHomePageActivity.startActivity(mContext.get(), info.getAuthorId());
        });

        holder.ivCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mJerryImageModel.setBlurryUrl(info.getCoverUrl());
                mJerryImageModel.setOriginalUrl(info.getOriginalUrl());
                ImageWatcherActivity.startActivity(mContext.get(), mJerryImageModel);
            }
        });

        holder.itemView.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onArticleItemClick(v, position);
            }
        });
    }

    /**
     * 文字类型
     *
     * @param holder
     * @param info
     * @param position
     */
    private void handleTextView(TextViewHolder holder, ArticleInfo info, int position) {

        // 标题
        holder.tvTitle.setText(info.getTitle());
        // 内容
        holder.tvDes.setText(info.getContent());
        // 作者
        holder.tvAuthor.setText(info.getAuthorName());
        // 阅读量
        holder.tvReadCount.setText(TyUtils.formatNum(info.getReadNumber()));
        // 评论数
        holder.tvReviewCount.setText(TyUtils.formatNum(info.getCommentNumber()));
        // 时间
        holder.tvTime.setText(TyUtils.getPublishTime(info.getCreatedTime()));

        holder.flowLayout.setNeedShowLine(SHOW_TAG_LINE);
        addLabel(info.getLabels(), holder.flowLayout);

        holder.tvAuthor.setOnClickListener(v -> {
            PersonalHomePageActivity.startActivity(mContext.get(), info.getAuthorId());
        });

        holder.itemView.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onArticleItemClick(v, position);
            }
        });
    }

    /**
     * 视频类型
     *
     * @param holder
     * @param info
     * @param position
     */
    private void handleVideoType(VideoViewHolder holder,
                                 ArticleInfo info,
                                 int position) {

        // 设置封面
        setImageView(holder.ivCover, info.getCoverUrl());
        // 标题
        holder.tvTitle.setText(info.getTitle());
        // 作者
        holder.tvAuthor.setText(info.getAuthorName());
        // 阅读量
        holder.tvReadCount.setText(TyUtils.formatNum(info.getReadNumber()));
        // 评论数
        holder.tvReviewCount.setText(TyUtils.formatNum(info.getCommentNumber()));
        // 时间
        holder.tvTime.setText(TyUtils.getPublishTime(info.getCreatedTime()));

        holder.tvAuthor.setOnClickListener(v -> {
            PersonalHomePageActivity.startActivity(mContext.get(), info.getAuthorId());
        });

        // 内容
        if (TextUtils.isEmpty(info.getContent())) {
            holder.tvDes.setVisibility(View.GONE);
        } else {
            holder.tvDes.setVisibility(View.VISIBLE);
            holder.tvDes.setText(info.getContent());
        }

        holder.flowLayout.setNeedShowLine(SHOW_TAG_LINE);
        addLabel(info.getLabels(), holder.flowLayout);

        holder.ivCover.setOnClickListener(v -> {
            mJerryImageModel.setBlurryUrl(info.getCoverUrl());
            mJerryImageModel.setOriginalUrl(info.getOriginalUrl());
            ImageWatcherActivity.startActivity(mContext.get(), mJerryImageModel);
        });

        holder.itemView.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onArticleItemClick(v, position);
            }
        });

    }

    private void addLabel(List<String> list, TagFlowLayout tagFlowLayout) {
        tagFlowLayout.removeAllViews();

        if (list != null && list.size() > 0) {

            for (String label : list) {
                TextView textView = (TextView) mInflater
                        .inflate(R.layout.item_label, tagFlowLayout, false);

                textView.setText(label);
                tagFlowLayout.addView(textView);
            }

            tagFlowLayout.setVisibility(View.VISIBLE);
        } else {
            tagFlowLayout.setVisibility(View.GONE);
        }
    }

    private void setImageView(ImageView imageView, String url) {
        GlideOptions options = new GlideOptions()
                .error(R.drawable.shape_placeholder_gray_fill_round_4dp)
                .placeholder(R.drawable.shape_placeholder_gray_fill_round_4dp);

        TyUtils.getGlideRequest(
                mFragment.get(),
                mContext.get(),
                url,
                options,
                imageView);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    private boolean isExistAd() {
        return mAdList != null && mAdList.size() > 0;
    }

    private boolean isExistTopArticle() {
        return mTopArticleList != null && mTopArticleList.size() > 0;
    }

    public WebViewCache getWebViewCache() {
        return mWebViewCache;
    }

    @Override
    public int getItemViewType(int position) {
        return mData.get(position).getType();
    }

//    private int getPosition(int pos) {
//        return pos - (isExistAd() ? 1 : 0);
//    }

    @Override
    public void onViewDetachedFromWindow(@NonNull RecyclerView.ViewHolder holder) {
        if (holder instanceof AdViewHolder) {

            AdViewHolder adViewHolder = (AdViewHolder) holder;
            adViewHolder.jerryBanner.stopAutoPlay();
            mAdShowIndex = adViewHolder.jerryBanner.getCurrentItem();

        } else if (holder instanceof TopArticleViewHolder) {

            TopArticleViewHolder topArticleViewHolder = (TopArticleViewHolder) holder;
            topArticleViewHolder.recyclerView.stop();

        } else if (holder instanceof PageAdvertisementViewHolder) {

            PageAdvertisementViewHolder pageAdvertisementViewHolder
                    = (PageAdvertisementViewHolder) holder;
            WebView webView =
                    (WebView) pageAdvertisementViewHolder.llAdvertisement.getChildAt(0);
            mWebViewCache.saveWebView(webView);
            pageAdvertisementViewHolder.llAdvertisement.removeView(webView);

        }
    }

    @Override
    public void onViewAttachedToWindow(@NonNull RecyclerView.ViewHolder holder) {
        if (holder instanceof AdViewHolder) {

            AdViewHolder adViewHolder = (AdViewHolder) holder;
            adViewHolder.jerryBanner.setCurrentItem(mAdShowIndex);
            adViewHolder.jerryBanner.startAutoPlay();

        } else if (holder instanceof TopArticleViewHolder) {

            TopArticleViewHolder topArticleViewHolder = (TopArticleViewHolder) holder;
            topArticleViewHolder.recyclerView.reStart();

        } else if (holder instanceof PageAdvertisementViewHolder) {

            PageAdvertisementViewHolder pageAdvertisementViewHolder
                    = (PageAdvertisementViewHolder) holder;
            pageAdvertisementViewHolder.llAdvertisement.addView(mWebViewCache.getWebView(), 0);

        }
    }

    /**
     * 视频
     */
    static class VideoViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.rl_item)
        RelativeLayout rlItem;

        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.flow_layout)
        TagFlowLayout flowLayout;
        @BindView(R.id.iv_cover)
        ImageView ivCover;
        @BindView(R.id.tv_des)
        TextView tvDes;

        @BindView(R.id.tv_review_count)
        TextView tvReviewCount;
        @BindView(R.id.iv_review_img)
        ImageView ivReviewImg;
        @BindView(R.id.tv_read_count)
        TextView tvReadCount;
        @BindView(R.id.iv_read_img)
        ImageView ivReadImg;
        @BindView(R.id.tv_author)
        TextView tvAuthor;
        @BindView(R.id.tv_time)
        TextView tvTime;

        VideoViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    /**
     * 文字
     */
    static class ImageViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.rl_item)
        RelativeLayout rlItem;

        @BindView(R.id.iv_cover)
        ImageView ivCover;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.flow_layout)
        TagFlowLayout flowLayout;
        @BindView(R.id.tv_des)
        TextView tvDes;

        @BindView(R.id.tv_review_count)
        TextView tvReviewCount;
        @BindView(R.id.iv_review_img)
        ImageView ivReviewImg;
        @BindView(R.id.tv_read_count)
        TextView tvReadCount;
        @BindView(R.id.iv_read_img)
        ImageView ivReadImg;
        @BindView(R.id.tv_author)
        TextView tvAuthor;
        @BindView(R.id.tv_time)
        TextView tvTime;

        ImageViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

        }

    }

    /**
     * 文字
     */
    static class TextViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.rl_item)
        RelativeLayout rlItem;

        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.flow_layout)
        TagFlowLayout flowLayout;
        @BindView(R.id.tv_des)
        TextView tvDes;

        @BindView(R.id.tv_review_count)
        TextView tvReviewCount;
        @BindView(R.id.iv_review_img)
        ImageView ivReviewImg;
        @BindView(R.id.tv_read_count)
        TextView tvReadCount;
        @BindView(R.id.iv_read_img)
        ImageView ivReadImg;
        @BindView(R.id.tv_author)
        TextView tvAuthor;
        @BindView(R.id.tv_time)
        TextView tvTime;

        TextViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

        }

    }


    /**
     * 排序
     */
    static class SortViewHolder extends RecyclerView.ViewHolder {

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

        SortViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

    /**
     * 公告
     */
    static class AdViewHolder extends RecyclerView.ViewHolder implements IStick {

        @BindView(R.id.jerry_banner)
        JerryBanner jerryBanner;
        @BindView(R.id.tv_more)
        TextView tvMore;

        AdViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

    /**
     * 切换
     */
    static class PageTurningViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_last_page)
        TextView tvLastPage;
        @BindView(R.id.rl_page)
        RelativeLayout rlPage;
        @BindView(R.id.tv_page_count_unselect)
        TextView tvPageCountUnselect;
        @BindView(R.id.tv_page_count_select)
        TextView tvPageCountSelect;
        @BindView(R.id.tv_next_page)
        TextView tvNextPage;

        PageTurningViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    /**
     * 广告
     */
    static class PageAdvertisementViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ll_advertisement)
        LinearLayout llAdvertisement;

        public PageAdvertisementViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    /**
     * 置顶文章
     */
    static class TopArticleViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.recycleView)
        AutoRecyclerView recyclerView;
        @BindView(R.id.iv_more)
        ImageView ivMore;
        @BindView(R.id.ll_top_ariticle_list)
        LinearLayout llTopArticleList;
        @BindView(R.id.ll_more)
        LinearLayout llMore;

        public TopArticleViewHolder(View itemView, Context context) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            ScrollSpeedLinearLayoutManager layoutManager = new ScrollSpeedLinearLayoutManager(context);
            layoutManager.setSpeedSlow();
            recyclerView.setLayoutManager(layoutManager);
        }
    }
}
