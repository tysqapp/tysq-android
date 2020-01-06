package com.tysq.ty_android.base.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tysq.ty_android.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * author       : frog
 * time         : 2019-07-22 10:22
 * desc         : 带有 一个标题、多个相同类型的类型内容item
 * version      : 1.3.0
 */
public abstract class CommonHeaderSimpleAdapter<HEAD, DATA>
        extends CommonSimpleAdapter<DATA, RecyclerView.ViewHolder> {

    protected static final int HEAD = 1;
    protected static final int CONTENT = 2;
    protected static final int EMPTY = 3;

    protected final HEAD mHeader;

    public CommonHeaderSimpleAdapter(Context context,
                                     HEAD header,
                                     List<DATA> dataList) {
        this(context, header, dataList, true);
    }

    public CommonHeaderSimpleAdapter(Context context,
                                     HEAD header,
                                     List<DATA> dataList,
                                     boolean isNeedHeader) {
        super(context, dataList);
        if(isNeedHeader){
            this.mHeader = header;
        }else{
            this.mHeader = null;
        }
    }

    /**
     * 获取 头部的ViewHolder
     */
    protected abstract RecyclerView.ViewHolder getHeaderViewHolder(@NonNull ViewGroup parent,
                                                                   int viewType);

    /**
     * 获取 内容的ViewHolder
     */
    protected abstract RecyclerView.ViewHolder getContentViewHolder(@NonNull ViewGroup parent,
                                                                    int viewType);

    /**
     * 获取 空的ViewHolder
     */
    protected abstract RecyclerView.ViewHolder getEmptyViewHolder(@NonNull ViewGroup parent,
                                                                  int viewType);

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case HEAD:
                return getHeaderViewHolder(parent, viewType);
            case CONTENT:
                return getContentViewHolder(parent, viewType);
            case EMPTY:
                return getEmptyViewHolder(parent, viewType);
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        // 包含标题 且 下标为0
        if (isContainHeader() && position == 0) {
            return HEAD;
        } else if (isEmptyData()) { // 内容为空
            return EMPTY;
        } else {
            return CONTENT;
        }
    }

    @Override
    public int getItemCount() {
        if (isEmptyData()) {
            return 1 + getHeaderCount();
        } else {
            return mDataList.size() + getHeaderCount();
        }
    }

    /**
     * 是否包含头部
     */
    protected boolean isContainHeader() {
        return mHeader != null;
    }

    /**
     * 获取头部个数
     */
    protected int getHeaderCount() {
        return isContainHeader() ? 1 : 0;
    }

    /**
     * 是否为空数据
     *
     * @return
     */
    protected boolean isEmptyData() {
        return mDataList == null || mDataList.size() == 0;
    }

    /**
     * 处理空
     */
    protected void handleEmptyTip(RecyclerView.ViewHolder holder, int tipResourceId) {
        if (holder instanceof EmptyViewHolder) {
            EmptyViewHolder emptyViewHolder = (EmptyViewHolder) holder;
            emptyViewHolder.tvTip.setText(tipResourceId);
        }
    }

    public static class EmptyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_tip)
        public TextView tvTip;

        public EmptyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

}
