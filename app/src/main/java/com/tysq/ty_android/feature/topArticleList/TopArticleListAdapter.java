package com.tysq.ty_android.feature.topArticleList;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tysq.ty_android.R;
import com.tysq.ty_android.base.adapter.CommonSimpleAdapter;
import com.tysq.ty_android.feature.articleDetail.activity.ArticleDetailActivity;
import com.tysq.ty_android.utils.TyUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import response.TopArticleResp;

public class TopArticleListAdapter
        extends CommonSimpleAdapter<TopArticleResp.TopArticleBean,
        TopArticleListAdapter.ContentViewHolder> {


    public TopArticleListAdapter(Context context, List<TopArticleResp.TopArticleBean> topArticleBeans) {
        super(context, topArticleBeans);
    }

    @Override
    public void onBindViewHolder(@NonNull ContentViewHolder holder, int position) {
        TopArticleResp.TopArticleBean data = mDataList.get(position);

        TyUtils.addTopTag(mContext.get(), holder.tvTitle, data.getTitle());

        holder.rlTopArticleList.setOnClickListener(v ->
                ArticleDetailActivity.startActivity(mContext.get(), data.getArticleId())
        );

    }

    @NonNull
    @Override
    public ContentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ContentViewHolder(
                mInflater.inflate(R.layout.item_top_article_list_detail, parent, false)
        );
    }

    public static class ContentViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.rl_top_ariticle_list)
        RelativeLayout rlTopArticleList;
        public ContentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
