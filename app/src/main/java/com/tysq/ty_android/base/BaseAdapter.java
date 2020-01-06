package com.tysq.ty_android.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import java.lang.ref.WeakReference;
import java.util.zip.Inflater;

/**
 * author       : frog
 * time         : 2019-10-18 10:24
 * desc         : adapter
 * version      : 1.5.0
 */
public abstract class BaseAdapter<T extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<T> {

    protected final WeakReference<Context> mContext;
    protected final LayoutInflater mInflater;

    public BaseAdapter(Context context) {
        this.mContext = new WeakReference<>(context);
        this.mInflater = LayoutInflater.from(context);
    }
}
