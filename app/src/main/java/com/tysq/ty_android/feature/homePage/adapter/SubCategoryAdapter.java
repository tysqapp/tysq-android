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
import com.tysq.ty_android.feature.homePage.listener.OnSubCategoryItemClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import response.home.SubCategory;

/**
 * author       : frog
 * time         : 2019/4/28 上午10:35
 * desc         : 二级分类点击事件
 * version      : 1.3.0
 */
public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.SubViewHolder> {

    private final List<SubCategory> mData = new ArrayList<>();

    private final int mSelectTextColor;
    private final int mUnSelectTextColor;

    private LayoutInflater mInflater;
    private final OnSubCategoryItemClickListener mListener;

    private Context mContext;

    public SubCategoryAdapter(Context context,
                              OnSubCategoryItemClickListener listener) {
        this.mInflater = LayoutInflater.from(context);
        this.mContext = context;

        this.mSelectTextColor = ContextCompat.getColor(context, R.color.white);
        this.mUnSelectTextColor = ContextCompat.getColor(context, R.color.et_tip_text_color);

        this.mListener = listener;
    }

    public void setData(List<SubCategory> data) {
        mData.clear();
        mData.addAll(data);
    }

    public int getSize() {
        return mData.size();
    }

    @NonNull
    @Override
    public SubViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SubViewHolder(
                mInflater.inflate(R.layout.item_home_sub_category, parent, false),
                mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull SubViewHolder holder, int position) {

        SubCategory item = mData.get(position);

        holder.tvSub.setText(item.getName());

        if (item.isSelect()) {
            holder.tvSub.setTextColor(mSelectTextColor);
            holder.tvSub.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_round_blue));
        } else {
            holder.tvSub.setTextColor(mUnSelectTextColor);
            holder.tvSub.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_round_gray));
        }

    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    static class SubViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_sub)
        TextView tvSub;
        @BindView(R.id.rl_item)
        RelativeLayout rlItem;


        SubViewHolder(View view,
                      OnSubCategoryItemClickListener listener) {
            super(view);
            ButterKnife.bind(this, view);

            rlItem.setOnClickListener(v -> {
                if (listener == null) {
                    return;
                }

                listener.onSubCategoryClick(view, getAdapterPosition());
            });
        }
    }

}
