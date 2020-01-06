package com.tysq.ty_android.feature.personalHomePage.test;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tysq.ty_android.R;

import java.util.List;

/**
 * author       : frog
 * time         : 2019-09-18 15:13
 * desc         :
 * version      :
 */
public class TestAdapter extends RecyclerView.Adapter<TestAdapter.ViewHolder> {

    private final LayoutInflater mInflater;
    private final List<String> mData;

    public TestAdapter(Context context,
                       List<String> mData) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = mData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.item_test, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.tvContent.setText(mData.get(position));

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvContent;

        public ViewHolder(View itemView) {
            super(itemView);
            tvContent = itemView.findViewById(R.id.tv_content);
        }

    }

}
