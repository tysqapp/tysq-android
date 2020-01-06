package com.jerry.autorecyclerview;

import android.content.Context;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;

public class ScrollSpeedLinearLayoutManager extends LinearLayoutManager {
    private float MILLISECONDS_PER_INCH = 0.03f;

    private Context context;


    public ScrollSpeedLinearLayoutManager(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
        LinearSmoothScroller linearSmoothScroller = new LinearSmoothScroller(recyclerView.getContext()){
            @Nullable
            @Override
            public PointF computeScrollVectorForPosition(int targetPosition) {
                return ScrollSpeedLinearLayoutManager.this.computeScrollVectorForPosition(targetPosition);
            }

            @Override
            protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                setSpeedSlow();
                return MILLISECONDS_PER_INCH / displayMetrics.density;
            }
        };

        linearSmoothScroller.setTargetPosition(position);
        startSmoothScroll(linearSmoothScroller);

    }

    public void setSpeedSlow(){
        MILLISECONDS_PER_INCH = context.getResources().getDisplayMetrics().density * 3f;
    }

    public void setSpeedFase(){
        MILLISECONDS_PER_INCH = context.getResources().getDisplayMetrics().density * 0.03f;
    }

}
