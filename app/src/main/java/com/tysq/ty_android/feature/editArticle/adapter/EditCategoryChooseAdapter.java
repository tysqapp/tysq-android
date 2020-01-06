package com.tysq.ty_android.feature.editArticle.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tysq.ty_android.R;
import com.tysq.ty_android.feature.editArticle.listener.OnCategoryChooseListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import response.home.SubCategory;
import response.home.TopCategory;

/**
 * author       : frog
 * time         : 2019/5/8 上午10:05
 * desc         : 编辑页——分类选择
 * version      : 1.3.0
 */
public class EditCategoryChooseAdapter extends RecyclerView.Adapter<EditCategoryChooseAdapter.ViewHolder> {

    public static final int TOP = 1;
    public static final int SUB = 2;

    private int mType;

    private List<TopCategory> mTopData;
    private List<SubCategory> mSubData;

    private int mTopId;
    private int mSubId;

    private LayoutInflater mInflater;

    private OnCategoryChooseListener mListener;

    private int mSelectColor;
    private int mUnselectColor;

    public EditCategoryChooseAdapter(Context context,
                                     List<TopCategory> mTopData,
                                     int type) {
        this.mTopData = mTopData;
        this.mInflater = LayoutInflater.from(context);
        this.mType = type;

        this.mSelectColor = ContextCompat.getColor(context, R.color.main_blue_color);
        this.mUnselectColor = ContextCompat.getColor(context, R.color.main_text_color);

        this.mTopId = -1;
        this.mSubId = -1;
    }

    public void setSubData(List<SubCategory> subData) {
        this.mSubData = subData;
    }

    public void setType(int type) {
        this.mType = type;
    }

    public void setTopId(int topId) {
        this.mTopId = topId;
    }

    public void setSubId(int subId) {
        this.mSubId = subId;
    }

    public void setListener(OnCategoryChooseListener listener) {
        this.mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                mInflater.inflate(R.layout.item_edit_category_choose, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final int pos = position;

        if (mType == TOP) {

            TopCategory topCategory = mTopData.get(position);
            holder.tvItem.setText(topCategory.getName());
            if (topCategory.getId() == mTopId) {
                holder.tvItem.setTextColor(mSelectColor);
            } else {
                holder.tvItem.setTextColor(mUnselectColor);
            }

        } else if (mType == SUB) {

            SubCategory subCategory = mSubData.get(position);
            holder.tvItem.setText(subCategory.getName());
            if (subCategory.getId() == mSubId) {
                holder.tvItem.setTextColor(mSelectColor);
            } else {
                holder.tvItem.setTextColor(mUnselectColor);
            }

        }

        holder.tvItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener == null) {
                    return;
                }

                mListener.onItemClick(mType, pos);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mType == TOP) {
            return mTopData.size();
        } else if (mType == SUB) {
            return mSubData.size();
        }

        return 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_item)
        TextView tvItem;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}
