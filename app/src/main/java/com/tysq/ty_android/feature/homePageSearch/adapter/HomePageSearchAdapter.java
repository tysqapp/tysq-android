package com.tysq.ty_android.feature.homePageSearch.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bit.view.fragment.BitBaseFragment;
import com.tysq.ty_android.R;
import com.tysq.ty_android.config.Constant;
import com.tysq.ty_android.feature.articleDetail.activity.ArticleDetailActivity;
import com.tysq.ty_android.feature.homePageSearch.adapter.viewHolder.SearchAdminViewHolder;
import com.tysq.ty_android.feature.homePageSearch.adapter.viewHolder.SearchArticleViewHolder;
import com.tysq.ty_android.feature.homePageSearch.adapter.viewHolder.SearchEmptyViewHolder;
import com.tysq.ty_android.feature.homePageSearch.adapter.viewHolder.SearchHistoryViewHolder;
import com.tysq.ty_android.feature.homePageSearch.adapter.viewHolder.SearchLabelViewHolder;
import com.tysq.ty_android.feature.homePageSearch.adapter.viewHolder.TitleViewHolder;
import com.tysq.ty_android.feature.homePageSearch.config.HomePageSearchConstants;
import com.tysq.ty_android.feature.homePageSearch.listener.OnClickLocalLabel;
import com.tysq.ty_android.feature.personalHomePage.PersonalHomePageActivity;
import com.tysq.ty_android.utils.TyUtils;
import com.tysq.ty_android.widget.TagFlowLayout;

import java.lang.ref.WeakReference;
import java.util.List;

import response.common.TitleCountVO;
import response.search.SearchArticleInfoBean;
import response.search.SearchUsersInfoBean;
import vo.search.HomePageSearchVO;

/**
 * author       : liaozhenlin
 * time         : 2019/9/18 11:06
 * desc         : 首页搜索适配器
 * version      : 1.5.0
 */
public class HomePageSearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int SHOW_ALL = 1;
    private static final int HISTORY_LABEL = -1;
    private static final int ARTICLE_LABEL = -2;
    private static final int LABEL_LABEL = -3;
    private final WeakReference<BitBaseFragment> mFragment;
    private final WeakReference<Context> mContext;
    private final LayoutInflater mInflater;
    private List<HomePageSearchVO> mData;
    private TitleCountVO mTitleCountVO;
    private Context context;
    private String input;
    private OnClickLocalLabel mListener;

    public HomePageSearchAdapter(BitBaseFragment fragment,
                                 Context context,
                                 TitleCountVO header,
                                 List<HomePageSearchVO> data,
                                 OnClickLocalLabel listener) {
        this.mFragment = new WeakReference<>(fragment);
        this.mContext = new WeakReference<>(context);
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context = context;
        this.mTitleCountVO = header;
        this.mListener = listener;
    }

    public void setData(List<HomePageSearchVO> data, String input) {
        this.mData = data;
        this.input = input;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        if (viewType == HomePageSearchConstants.TITLE) {
            viewHolder = new TitleViewHolder(mInflater
                    .inflate(R.layout.item_common_count_title, parent, false));
        } else if (viewType == HomePageSearchConstants.HISTORY) {
            viewHolder = new SearchHistoryViewHolder(mInflater
                    .inflate(R.layout.item_home_page_search_history, parent, false));
        } else if (viewType == HomePageSearchConstants.NODATA) {
            viewHolder = new SearchEmptyViewHolder(mInflater
                    .inflate(R.layout.item_home_page_search_no_data, parent, false));
        } else if (viewType == HomePageSearchConstants.ARTICLE) {
            viewHolder = new SearchArticleViewHolder(mInflater
                    .inflate(R.layout.item_search_text_article, parent, false));
        } else if (viewType == HomePageSearchConstants.LABEL) {
            viewHolder = new SearchLabelViewHolder(mInflater
                    .inflate(R.layout.item_search_text_article, parent, false));
        } else if (viewType == HomePageSearchConstants.ADMIN) {
            viewHolder = new SearchAdminViewHolder(mInflater
                    .inflate(R.layout.item_home_page_search_admin, parent, false));
        }


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        HomePageSearchVO itemInfo = mData.get(position);

        if (holder instanceof TitleViewHolder) {

            TitleViewHolder titleViewHolder = (TitleViewHolder) holder;
            String format = String.format(mContext.get()
                    .getString(R.string.home_page_search_num), mTitleCountVO.getCount());
            titleViewHolder.tvTitle.setText(format);

        } else if (holder instanceof SearchHistoryViewHolder) {

            SearchHistoryViewHolder searchHistoryViewHolder = (SearchHistoryViewHolder) holder;
            List<db.LocalLabel> labelItems = (List<db.LocalLabel>) itemInfo.getData();
            createTag(labelItems, searchHistoryViewHolder.tvSearchHistory, searchHistoryViewHolder.flowLayout);

        } else if (holder instanceof SearchArticleViewHolder) {

            SearchArticleViewHolder searchArticleViewHolder = (SearchArticleViewHolder) holder;
            SearchArticleInfoBean articleInfoBean
                    = (SearchArticleInfoBean) itemInfo.getData();

            SpannableStringBuilder title = setTextColor(articleInfoBean.getTitle(), input);
            searchArticleViewHolder.tvTitle.setText(title);

            searchArticleViewHolder.tvReadCount.setText(String.valueOf(articleInfoBean.getReadNumber()));
            searchArticleViewHolder.tvReviewCount.setText(String.valueOf(articleInfoBean.getCommentNumber()));
            searchArticleViewHolder.tvTime.setText(TyUtils.getPublishTime(articleInfoBean.getCreatedTime()));
            searchArticleViewHolder.tvAuthor.setText(articleInfoBean.getAuthorName());
            searchArticleViewHolder.flowLayout.setNeedShowLine(SHOW_ALL);

            searchArticleViewHolder.rlItem.setOnClickListener(view ->
                    ArticleDetailActivity.startActivity(mContext.get(), articleInfoBean.getId())
            );
            searchArticleViewHolder.tvAuthor.setOnClickListener(view ->
                    PersonalHomePageActivity.startActivity(
                            mContext.get(), articleInfoBean.getAuthorId())
            );

            if (articleInfoBean.getLabels() != null || articleInfoBean.getLabels().size() > 0) {
                createTag(articleInfoBean.getLabels(), searchArticleViewHolder.flowLayout, ARTICLE_LABEL);
            }

        } else if (holder instanceof SearchLabelViewHolder) {

            SearchLabelViewHolder searchLabelViewHolder = (SearchLabelViewHolder) holder;
            SearchArticleInfoBean articleInfoBean
                    = (SearchArticleInfoBean) itemInfo.getData();

            searchLabelViewHolder.tvTitle.setText(articleInfoBean.getTitle());
            searchLabelViewHolder.tvAuthor.setText(articleInfoBean.getAuthorName());
            searchLabelViewHolder.tvReadCount.setText(TyUtils.formatNum(articleInfoBean.getReadNumber()));
            searchLabelViewHolder.tvReviewCount.setText(TyUtils.formatNum(articleInfoBean.getCommentNumber()));
            searchLabelViewHolder.tvTime.setText(TyUtils.getPublishTime(articleInfoBean.getCreatedTime()));

            searchLabelViewHolder.flowLayout.setNeedShowLine(SHOW_ALL);

            searchLabelViewHolder.rlItem.setOnClickListener(view ->
                    ArticleDetailActivity.startActivity(mContext.get(), articleInfoBean.getId()));

            searchLabelViewHolder.tvAuthor.setOnClickListener(view ->
                    PersonalHomePageActivity.startActivity(
                            mContext.get(), articleInfoBean.getAuthorId())
            );
            createTag(articleInfoBean.getLabels(), searchLabelViewHolder.flowLayout, LABEL_LABEL);

        } else if (holder instanceof SearchAdminViewHolder) {

            SearchAdminViewHolder searchAdminViewHolder = (SearchAdminViewHolder) holder;
            SearchUsersInfoBean userInfoBean
                    = (SearchUsersInfoBean) itemInfo.getData();

            judgeOrHideLine(userInfoBean.getCareer(),
                    userInfoBean.getTrade(),
                    userInfoBean.getHomeAddress(),
                    searchAdminViewHolder.divider1,
                    searchAdminViewHolder.divider2);

            judgeAndSetData(userInfoBean.getCareer(),
                    searchAdminViewHolder.tvJob);

            judgeAndSetData(userInfoBean.getTrade(),
                    searchAdminViewHolder.tvType);

            judgeAndSetData(userInfoBean.getHomeAddress(),
                    searchAdminViewHolder.tvAddress);


            searchAdminViewHolder.tvIntroduction.setText(userInfoBean.getPersonalProfile());
            searchAdminViewHolder.tvName.setText(userInfoBean.getName());


            //设置头像
            TyUtils.initUserPhoto(
                    mFragment.get(),
                    mContext.get(),
                    userInfoBean.getHeadUrl(),
                    searchAdminViewHolder.ivPhoto);

            searchAdminViewHolder.llSearchAdmin.setOnClickListener(view ->
                    PersonalHomePageActivity.startActivity(
                            mContext.get(), userInfoBean.getId())
            );

            //设置等级
            Integer gradeResource = Constant.LV_MAP.get(userInfoBean.getGrade());
            if (gradeResource == null) {
                gradeResource = Constant.LV_MAP.get(Constant.DEFAULT_GRADE);
            }
            searchAdminViewHolder.ivLv.setImageDrawable(ContextCompat
                    .getDrawable(context, gradeResource));

            if (TextUtils.isEmpty(userInfoBean.getCareer())
                    && TextUtils.isEmpty(userInfoBean.getHomeAddress())
                    && TextUtils.isEmpty(userInfoBean.getTrade())
                    && TextUtils.isEmpty(userInfoBean.getPersonalProfile())) {

                searchAdminViewHolder.rlAdminDecription.setVisibility(View.GONE);
                searchAdminViewHolder.tvIntroduction.setVisibility(View.GONE);
            }
        }

    }

    /**
     * 判断隐藏分割线
     *
     * @param career  职业
     * @param trade   行业
     * @param address 地址
     */
    private void judgeOrHideLine(String career, String trade, String address, View divider1, View divider2) {
        if (!TextUtils.isEmpty(career) && !TextUtils.isEmpty(trade)) {
            divider1.setVisibility(View.VISIBLE);
        } else {
            divider1.setVisibility(View.GONE);
        }

        if (TextUtils.isEmpty(address) || (TextUtils.isEmpty(career) && TextUtils.isEmpty(trade))) {
            divider2.setVisibility(View.GONE);
        } else {
            divider2.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 判断是否需要隐藏textView
     *
     * @param content
     * @param textView
     */
    private void judgeAndSetData(String content, TextView textView) {
        if (TextUtils.isEmpty(content)) {
            textView.setVisibility(View.GONE);
        } else {
            textView.setText(content);
            textView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return mData.get(position).getType();
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    /**
     * 创建历史标签
     */
    private void createTag(List<db.LocalLabel> list,
                           TextView tvSearchHistory,
                           TagFlowLayout tagFlowLayout) {
        tagFlowLayout.removeAllViews();
        if (list != null && list.size() > 0) {
            for (db.LocalLabel label : list) {
                TextView textView = (TextView) mInflater
                        .inflate(R.layout.item_history_label, tagFlowLayout, false);
                textView.setText(label.getLabel());
                textView.setOnClickListener(view -> mListener.onClickLocalLabel(label.getLabel()));
                tagFlowLayout.addView(textView);
            }
            tagFlowLayout.setVisibility(View.VISIBLE);
            //tvSearchHistory.setVisibility(View.VISIBLE);
        } else {
            tagFlowLayout.setVisibility(View.GONE);
            //tvSearchHistory.setVisibility(View.GONE);
        }
    }

    /**
     * 创建文章标签
     */
    private void createTag(List<String> list,
                           TagFlowLayout tagFlowLayout,
                           int typeId) {
        tagFlowLayout.removeAllViews();

        if (list != null && list.size() > 0) {

            if (typeId == HISTORY_LABEL) {
                addTagFlow(list, tagFlowLayout, R.layout.item_history_label, false);
            } else if (typeId == ARTICLE_LABEL) {
                addTagFlow(list, tagFlowLayout, R.layout.item_label, false);
            } else {
                addTagFlow(list, tagFlowLayout, R.layout.item_label, true);
            }

            tagFlowLayout.setVisibility(View.VISIBLE);
        } else {
            tagFlowLayout.setVisibility(View.GONE);
        }
    }

    /**
     * 添加标签到流式布局
     *
     * @param list          数据
     * @param tagFlowLayout 流式布局
     * @param resId         标签的样式
     * @param isRed         标签内的内容是否与输入框的内容相同，相同则显红色
     */
    private void addTagFlow(List<String> list,
                            TagFlowLayout tagFlowLayout,
                            int resId,
                            boolean isRed) {
        for (String label : list) {
            TextView textView = (TextView) mInflater
                    .inflate(resId, tagFlowLayout, false);
            if (isRed) {
                SpannableStringBuilder content = setTextColor(label, input);
                textView.setText(content);
            } else {
                textView.setText(label);
            }

            tagFlowLayout.addView(textView);
        }
    }

    /**
     * 拼装红色字体
     */
    private SpannableStringBuilder setTextColor(String content, String input) {
        int startIndex = content.indexOf(input);
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder(content);
        if (startIndex != -1) {

            ForegroundColorSpan colorSpan
                    = new ForegroundColorSpan(context.getResources().getColor(R.color.search_color));
            stringBuilder.setSpan(colorSpan,
                    startIndex,
                    startIndex + input.length(),
                    Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            Log.d("few", "fwe");
        }
        return stringBuilder;
    }


}
