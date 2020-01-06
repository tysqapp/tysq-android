package com.tysq.ty_android.feature.homePage.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tysq.ty_android.R;
import com.tysq.ty_android.feature.homePage.listener.OnTopCategoryItemClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import response.home.TopCategory;

/**
 * author       : frog
 * time         : 2019/4/28 上午10:35
 * desc         : 顶级分类点击事件
 * version      : 1.3.0
 */
public class TopCategoryAdapter extends RecyclerView.Adapter<TopCategoryAdapter.TopViewHolder> {

    private final List<TopCategory> mData = new ArrayList<>();

    private final int mSelectSize;
    private final int mSelectColor;
    private final int mUnSelectSize;
    private final int mUnSelectColor;

    private final LayoutInflater mInflater;
    private final OnTopCategoryItemClickListener mListener;

    public TopCategoryAdapter(Context context,
                              OnTopCategoryItemClickListener listener) {

        this.mInflater = LayoutInflater.from(context);

        this.mSelectSize = 18;
        this.mUnSelectSize = 14;
        this.mSelectColor = ContextCompat.getColor(context, R.color.main_text_color);
        this.mUnSelectColor = ContextCompat.getColor(context, R.color.et_tip_text_color);

        this.mListener = listener;
    }

    public void setData(List<TopCategory> data) {
        mData.clear();
        mData.addAll(data);
    }

    @NonNull
    @Override
    public TopViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TopViewHolder(
                mInflater.inflate(R.layout.item_home_top_category, parent, false),
                mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull TopViewHolder holder, int position) {

        TopCategory item = mData.get(position);

        holder.tvTop.setText(item.getName());

        if (item.isSelect()) {
            holder.tvTop.setTextSize(mSelectSize);
            holder.tvTop.setTextColor(mSelectColor);
        } else {
            holder.tvTop.setTextSize(mUnSelectSize);
            holder.tvTop.setTextColor(mUnSelectColor);
        }

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    static class TopViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_top)
        TextView tvTop;

        TopViewHolder(View view, OnTopCategoryItemClickListener listener) {
            super(view);
            ButterKnife.bind(this, view);
            tvTop.setOnClickListener(v -> {
                if (listener == null) {
                    return;
                }
                listener.onTopCategoryClick(view, getAdapterPosition());
            });
        }
    }

}
