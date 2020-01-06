package com.jerry.autorecyclerview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import java.lang.ref.WeakReference;

public class AutoRecyclerView extends RecyclerView {
    private static final long TIME_AUTO_POLL = 4000;
    AutoPollTask autoPollTask;
    private boolean running; //表示是否正在自动轮播
    private boolean canRun;  //表示是否可以自动轮播

    private int index = 1;
    public AutoRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        autoPollTask = new AutoPollTask(this, context);
    }

    /**
     * 一次只能滑一个item
     */
    static class AutoPollTask implements Runnable{

        private final WeakReference<AutoRecyclerView> mReference;

        private Context context;
        public AutoPollTask(AutoRecyclerView reference, Context context){
            this.mReference = new WeakReference<AutoRecyclerView>(reference);
            this.context = context;
        }

        @Override
        public void run() {

            AutoRecyclerView recyclerView = mReference.get();
            if (recyclerView != null && recyclerView.running && recyclerView.canRun){
                //recyclerView.smoothScrollBy(2 ,dip2px(context, 42));
                recyclerView.smoothScrollToPosition(++recyclerView.index);
                Log.d("AutoRecyclerView", String.valueOf(recyclerView.index));
                recyclerView.postDelayed(recyclerView.autoPollTask, TIME_AUTO_POLL);
            }
        }
    }

    public void start(){
        if (running){
            stop();
        }
        canRun = true;
        running = true;
        postDelayed(autoPollTask, TIME_AUTO_POLL);
    }

    public void stop(){
        running = false;
        removeCallbacks(autoPollTask);
    }


    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public void reStart(){
        index = 1;
        start();
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        //返回false，把事件交给子控件的onInterceptTouchEvent()处理
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        //返回true，则后续事件可以继续传递给该View的onTouchEvent()处理
        return true;
    }
}
