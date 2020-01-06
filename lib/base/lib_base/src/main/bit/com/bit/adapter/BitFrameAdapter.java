package com.bit.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bit.callback.IStateListener;
import com.bit.callback.StateViewHolderListener;
import com.bit.config.BitManager;
import com.zinc.jrecycleview.adapter.JRefreshAndLoadMoreAdapter;
import com.zinc.jrecycleview.config.JRecycleConfig;

/**
 * author       : frog
 * time         : 2019/3/26 下午3:04
 * desc         : 上拉加载更多 和 下拉刷新的适配器
 * version      : 1.3.0
 */
public class BitFrameAdapter extends JRefreshAndLoadMoreAdapter {

    /**
     * 重试类型
     */
    private final static int RETRY_TYPE = 0xABC201;
    /**
     * 加载类型
     */
    private final static int LOADING_TYPE = 0xABC202;
    /**
     * 空类型
     */
    private final static int EMPTY_TYPE = 0xABC203;
    /**
     * 成功类型
     */
    private final static int SUCCESS_TYPE = 0xABC204;

    /**
     * 当前类型
     */
    private int mCurrentType = LOADING_TYPE;

    private LayoutInflater mLayoutInflater;

    /**
     * 状态监听器
     */
    private IStateListener mStateListener;
    private StateViewHolderListener mStateViewHolderListener;

    private RecyclerView.Adapter mRealAdapter;

    private int mEmptyView;
    private int mRetryView;
    private int mLoadingView;

    private int mRetryBtnId;
    private int mEmptyBtnId;

    public BitFrameAdapter(Context context,
                           RecyclerView.Adapter adapter) {
        super(context, adapter);
        this.mRealAdapter = adapter;
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    public void setEmptyView(int emptyView) {
        this.mEmptyView = emptyView;
    }

    public void setRetryView(int retryView) {
        this.mRetryView = retryView;
    }

    public void setLoadingView(int loadingView) {
        this.mLoadingView = loadingView;
    }

    public void setRetryBtnId(int retryBtnId) {
        this.mRetryBtnId = retryBtnId;
    }

    public void setEmptyBtnId(int emptyBtnId) {
        this.mEmptyBtnId = emptyBtnId;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        switch (viewType) {
            case RETRY_TYPE:
                return new RetryViewHolder(mLayoutInflater
                        .inflate(mRetryView,
                                parent,
                                false));
            case LOADING_TYPE:
                return new LoadingViewHolder(mLayoutInflater
                        .inflate(mLoadingView,
                                parent,
                                false));
            case EMPTY_TYPE:
                return new EmptyViewHolder(mLayoutInflater
                        .inflate(mEmptyView,
                                parent,
                                false));
        }

        return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (this.mStateListener == null) {
            super.onBindViewHolder(holder, position);
            return;
        }

        if (holder instanceof RetryViewHolder) {

            handleRetryViewHolder((RetryViewHolder) holder);

            if (mRetryBtnId == BitManager.NO_CLICK) {
                return;
            }

            if (mRetryBtnId == BitManager.NONE) {
                holder.itemView.setOnClickListener(v -> {
                    // TODO: 2019/3/26 这里可以优化
                    BitFrameAdapter.this.mCurrentType = LOADING_TYPE;
                    BitFrameAdapter.super.notifyDataSetChanged();
                    BitFrameAdapter.this.mStateListener.onRetry();
                });
            } else {
                holder.itemView
                        .findViewById(mRetryBtnId)
                        .setOnClickListener(v -> {
                            // TODO: 2019/3/26 这里可以优化
                            BitFrameAdapter.this.mCurrentType = LOADING_TYPE;
                            BitFrameAdapter.super.notifyDataSetChanged();
                            BitFrameAdapter.this.mStateListener.onRetry();
                        });
            }

        } else if (holder instanceof LoadingViewHolder) {

            handleLoadingViewHolder((LoadingViewHolder) holder);

            this.mStateListener.onLoading();

        } else if (holder instanceof EmptyViewHolder) {

            handleEmptyViewHolder((EmptyViewHolder) holder);

            if (mEmptyBtnId == BitManager.NO_CLICK) {
                return;
            }

            if (mEmptyBtnId == BitManager.NONE) {
                holder.itemView
                        .setOnClickListener(v -> {
                            // TODO: 2019/3/26 这里可以优化
                            BitFrameAdapter.this.mCurrentType = LOADING_TYPE;
                            BitFrameAdapter.super.notifyDataSetChanged();
                            BitFrameAdapter.this.mStateListener.onEmpty();
                        });
            } else {
                View emptyBtnView = holder.itemView.findViewById(mEmptyBtnId);
                if (emptyBtnView != null) {
                    emptyBtnView.setOnClickListener(v -> {
                        // TODO: 2019/3/26 这里可以优化
                        BitFrameAdapter.this.mCurrentType = LOADING_TYPE;
                        BitFrameAdapter.super.notifyDataSetChanged();
                        BitFrameAdapter.this.mStateListener.onEmpty();
                    });
                }

            }


        } else {

            super.onBindViewHolder(holder, position);

        }
    }

    private void handleLoadingViewHolder(LoadingViewHolder holder) {
        if (mStateViewHolderListener != null) {
            mStateViewHolderListener.handleLoadingViewHolder(holder);
        }
    }

    private void handleRetryViewHolder(RetryViewHolder holder) {
        if (mStateViewHolderListener != null) {
            mStateViewHolderListener.handleRetryViewHolder(holder);
        }
    }

    private void handleEmptyViewHolder(EmptyViewHolder holder) {
        if (mStateViewHolderListener != null) {
            mStateViewHolderListener.handleEmptyViewHolder(holder);
        }
    }

    @Override
    public int getItemCount() {
        if (isState()) {
            return 1;
        }
        return super.getItemCount();
    }

    public void setStateListener(IStateListener stateListener) {
        this.mStateListener = stateListener;
    }

    public void setStateViewHolderListener(StateViewHolderListener listener) {
        this.mStateViewHolderListener = listener;
    }

    @Override
    public int getItemViewType(int position) {

        if (this.mCurrentType == SUCCESS_TYPE) {
            return super.getItemViewType(position);
        } else {
            return this.mCurrentType;
        }

    }

    //是否处在loading、retry、empty状态
    public boolean isState() {
        return this.mCurrentType != SUCCESS_TYPE;
    }

    /**
     * 设置为加载状态
     */
    public void onLoading() {
        this.mCurrentType = LOADING_TYPE;
        this.notifyDataSetChanged();
    }

    public void onSuccess() {
        super.setRefreshComplete();
        this.mCurrentType = SUCCESS_TYPE;
        this.notifyDataSetChanged();
    }

    public void onError() {
        this.mCurrentType = RETRY_TYPE;
        super.notifyDataSetChanged();
    }

    public void onEmpty() {
        this.mCurrentType = EMPTY_TYPE;
        super.notifyDataSetChanged();
    }

    /**
     * 重试视图
     */
    public class RetryViewHolder extends RecyclerView.ViewHolder {

        RetryViewHolder(View itemView) {
            super(itemView);
        }

    }

    /**
     * 加载视图
     */
    public class LoadingViewHolder extends RecyclerView.ViewHolder {

        LoadingViewHolder(View itemView) {
            super(itemView);
        }
    }

    /**
     * 空视图
     */
    public class EmptyViewHolder extends RecyclerView.ViewHolder {

        EmptyViewHolder(View itemView) {
            super(itemView);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();

        if (manager instanceof GridLayoutManager) {

            GridLayoutManager gridLayoutManager = (GridLayoutManager) manager;

            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {

                    int itemViewType = getItemViewType(position);

                    if (itemViewType == RETRY_TYPE ||
                            itemViewType == SUCCESS_TYPE ||
                            itemViewType == EMPTY_TYPE ||
                            itemViewType == LOADING_TYPE ||
                            itemViewType == JRecycleConfig.FOOT ||
                            itemViewType == JRecycleConfig.HEAD) {
                        return gridLayoutManager.getSpanCount();
                    }

                    return 1;
                }
            });
        }

    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        if (mRealAdapter != null) {
            mRealAdapter.onViewAttachedToWindow(holder);
        }
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull RecyclerView.ViewHolder holder) {
        if (mRealAdapter != null) {
            mRealAdapter.onViewDetachedFromWindow(holder);
        }
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        if (mRealAdapter != null) {
            mRealAdapter.onDetachedFromRecyclerView(recyclerView);
        }
    }
}
