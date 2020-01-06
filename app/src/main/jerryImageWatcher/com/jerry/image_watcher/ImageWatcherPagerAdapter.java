package com.jerry.image_watcher;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.jerry.image_watcher.model.WatchImageVO;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * author       : frog
 * time         : 2019-09-02 17:48
 * desc         : 图片查看器
 * version      : 1.4.0
 */

public class ImageWatcherPagerAdapter extends FragmentPagerAdapter {

    public WeakReference<Context> mContext;
    public WeakReference<ArrayList<WatchImageVO>> mData;

    public ImageWatcherPagerAdapter(FragmentManager fm,
                             Context context,
                             ArrayList<WatchImageVO> mData) {
        super(fm);
        this.mContext = new WeakReference<>(context);
        this.mData = new WeakReference<>(mData);
    }

    @Override
    public Fragment getItem(int position) {
        return ImageWatcherFragment.newInstance(mData.get().get(position));
    }

    @Override
    public int getCount() {
        return mData.get().size();
    }

}