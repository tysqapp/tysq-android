package com.tysq.ty_android.feature.homePage.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tysq.ty_android.R;
import com.tysq.ty_android.feature.homePage.listener.OnTopCategoryItemClickListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import response.home.TopCategory;

/**
 * author       : frog
 * time         : 2019/4/28 下午4:21
 * desc         :
 * version      :
 */
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private List<TopCategory> mData;

    private LayoutInflater mInflater;

    private int mSelectTextColor;
    private int mSelectBgColor;
    private int mUnSelectTextColor;
    private int mUnSelectBgColor;

    private final OnTopCategoryItemClickListener mListener;

    private int mSelTopId;

    public CategoryAdapter(Context context,
                           List<TopCategory> data,
                           int selTopId,
                           OnTopCategoryItemClickListener listener) {
        this.mData = data;
        this.mInflater = LayoutInflater.from(context);

        this.mSelectTextColor = ContextCompat.getColor(context, R.color.main_text_color);
        this.mSelectBgColor = ContextCompat.getColor(context, R.color.white);

        this.mUnSelectTextColor = ContextCompat.getColor(context, R.color.et_tip_text_color);
        this.mUnSelectBgColor = ContextCompat.getColor(context, R.color.rv_color);

        this.mSelTopId = selTopId;

        this.mListener = listener;
    }

    public void setSelTopId(int selTopId) {
        this.mSelTopId = selTopId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                mInflater.inflate(R.layout.item_category_info, parent, false),
                mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        TopCategory item = mData.get(position);

        holder.tvCategoryTitle.setText(item.getName());

        if (item.getId() == mSelTopId) {
            holder.vSelectTag.setVisibility(View.VISIBLE);
            holder.llRecommendItem.setBackgroundColor(mSelectBgColor);
            holder.tvCategoryTitle.setTextColor(mSelectTextColor);
        } else {
            holder.vSelectTag.setVisibility(View.GONE);
            holder.llRecommendItem.setBackgroundColor(mUnSelectBgColor);
            holder.tvCategoryTitle.setTextColor(mUnSelectTextColor);
        }

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.v_select_tag)
        View vSelectTag;
        @BindView(R.id.tv_category_title)
        TextView tvCategoryTitle;
        @BindView(R.id.rl_recommend_item)
        RelativeLayout llRecommendItem;

        ViewHolder(View view, OnTopCategoryItemClickListener listener) {
            super(view);
            ButterKnife.bind(this, view);

            llRecommendItem.setOnClickListener(v -> {
                if (listener == null) {
                    return;
                }

                listener.onTopCategoryClick(view, getAdapterPosition());
            });

        }
    }

}
