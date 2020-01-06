package com.tysq.ty_android.feature.articleReport;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tysq.ty_android.R;

import java.lang.ref.WeakReference;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import response.report.ArticleReportResp;

public class ArticleReportAdapter extends RecyclerView.Adapter<ArticleReportAdapter.ViewHolder> {

    private final WeakReference<Context> mContext;
    private final LayoutInflater mInflater;
    private final List<ArticleReportResp> mData;
    private final OnArticleReportItemClickListener mListener;

    public ArticleReportAdapter(Context context,
                                List<ArticleReportResp> data,
                                OnArticleReportItemClickListener listener) {
        this.mContext = new WeakReference<>(context);
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public ArticleReportAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                mInflater.inflate(R.layout.article_report_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleReportAdapter.ViewHolder holder,
                                 int pos) {
        final int position = pos;

        if (position == mData.size() - 1) {
            holder.divider.setVisibility(View.GONE);
        }
        holder.tvTitle.setText(mData.get(position).getTitle());

        boolean isChecked = mData.get(position).isCheck();
        holder.checkBox.setChecked(isChecked);

        holder.llArticleReportItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mData.get(position).setCheck(!mData.get(position).isCheck());
                boolean isChecked = mData.get(position).isCheck();
                isChecked = !isChecked;
                holder.checkBox.setChecked(isChecked);
                setOtherCheckBoxState(position);

                if (mListener == null) {
                    return;
                }
                mListener.onArticleReportItemClick(view, !holder.checkBox.isChecked());
            }
        });
    }

    /**
     * 设置其他checkbox为false
     *
     * @param position
     */
    private void setOtherCheckBoxState(int position) {
        for (int i = 0; i < mData.size(); i++) {
            if (i != position) {
                mData.get(i).setCheck(false);
            }

        }
        notifyDataSetChanged();
    }

    public String getReportType() {
        for (int i = 0; i < mData.size(); i++) {
            if (mData.get(i).isCheck()) {
                return mData.get(i).getTitle();
            }
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ll_article_report_item)
        LinearLayout llArticleReportItem;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.checkbox)
        CheckBox checkBox;
        @BindView(R.id.divider)
        View divider;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}
