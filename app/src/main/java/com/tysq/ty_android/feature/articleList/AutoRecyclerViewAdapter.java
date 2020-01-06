package com.tysq.ty_android.feature.articleList;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tysq.ty_android.R;
import com.tysq.ty_android.feature.articleDetail.activity.ArticleDetailActivity;

import java.lang.ref.WeakReference;
import java.util.List;

import response.TopArticleResp;

public class AutoRecyclerViewAdapter extends RecyclerView.Adapter<AutoRecyclerViewAdapter.ContentViewHolder> {

    private List<TopArticleResp.TopArticleBean> mData;
    private WeakReference<Context> mContext;
    private LayoutInflater mInflater;
    public AutoRecyclerViewAdapter(Context context, List<TopArticleResp.TopArticleBean> list) {
        this.mContext = new WeakReference<>(context);
        this.mInflater = LayoutInflater.from(context);
        this.mData = list;
    }

    @NonNull
    @Override
    public AutoRecyclerViewAdapter.ContentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ContentViewHolder(mInflater.inflate(R.layout.item_article_list_top_article_info, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AutoRecyclerViewAdapter.ContentViewHolder holder, int position) {
        String articleTitle = mData.get(position % mData.size()).getTitle();
        String articleId = mData.get(position % mData.size()).getArticleId();
        holder.tvTitle.setText(articleTitle);
        holder.llTopArticle.setOnClickListener(v -> ArticleDetailActivity.startActivity(mContext.get(), articleId));
    }

    @Override
    public int getItemCount() {
        if (mData.size() <= 2){
            return mData.size();
        }
        return Integer.MAX_VALUE;
    }

    public static class ContentViewHolder extends RecyclerView.ViewHolder{

        private TextView tvTitle;
        private LinearLayout llTopArticle;
        public ContentViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            llTopArticle = itemView.findViewById(R.id.ll_top_article);
        }
    }
}
