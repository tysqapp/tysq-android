package com.tysq.ty_android.base.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * author       : frog
 * time         : 2019-07-22 10:22
 * desc         : 带有 多个相同类型的类型内容item
 * version      : 1.3.0
 */
public abstract class CommonSimpleAdapter<DATA, RV extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<RV> {

    protected final List<DATA> mDataList;

    protected final LayoutInflater mInflater;

    protected final WeakReference<Context> mContext;

    public CommonSimpleAdapter(Context context, List<DATA> dataList) {
        this.mInflater = LayoutInflater.from(context);
        this.mContext = new WeakReference<>(context);
        this.mDataList = dataList;
    }

    @NonNull
    @Override
    public abstract RV onCreateViewHolder(@NonNull ViewGroup parent, int viewType);

    @Override
    public int getItemCount() {
        return mDataList.size();
    }
}
