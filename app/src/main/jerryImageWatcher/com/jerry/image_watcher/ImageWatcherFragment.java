package com.jerry.image_watcher;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.abc.lib_utils.toast.ToastUtils;
import com.bit.view.fragment.BitBaseFragment;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.github.chrisbanes.photoview.PhotoView;
import com.jerry.image_watcher.model.WatchImageVO;
import com.jerry.image_watcher.utils.ImageStatusHelper;
import com.jerry.image_watcher.utils.ImageWatcherUtils;
import com.tysq.ty_android.R;
import com.tysq.ty_android.utils.TyUtils;

import butterknife.BindView;

/**
 * author       : frog
 * time         : 2019-09-12 12:04
 * desc         : 图片查看
 * version      : 1.4.0
 */

public class ImageWatcherFragment extends BitBaseFragment {

    private static final String IMAGE_INFO = "image_info";

    private WatchImageVO mWatchImageVO;

    @BindView(R.id.photo_view)
    PhotoView photoView;

    public static ImageWatcherFragment newInstance(WatchImageVO watchImageVO) {
        ImageWatcherFragment fragment = new ImageWatcherFragment();

        Bundle args = new Bundle();
        args.putParcelable(IMAGE_INFO, watchImageVO);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    protected void initArgs(Bundle arguments) {
        mWatchImageVO = arguments.getParcelable(IMAGE_INFO);
    }

    @Override
    protected View onCreateFragmentView(LayoutInflater inflater,
                                        @Nullable ViewGroup container,
                                        @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_image_watcher, container, false);
    }

    @Override
    protected void initView(View view) {

        photoView.setOnPhotoTapListener((view1, x, y) -> {
            if (getActivity() == null) {
                return;
            }
            getActivity().finish();
        });

        initImage();

    }

    /**
     * 初始化图片
     */
    private void initImage() {
        // 检测是否在内存中存在高清图
        boolean originalExistInGlideCache
                = ImageWatcherUtils.isExistInGlideCache(getContext(), mWatchImageVO.getOriginalUrl());

        // 是否存在原图
        mWatchImageVO.setOriginal(originalExistInGlideCache);

        int status = mWatchImageVO.getStatus();
        if (originalExistInGlideCache) {
            status = ImageStatusHelper.removeStatus(status, ImageStatusHelper.IS_SHOW_ORIGINAL_BTN);
        } else {
            status = ImageStatusHelper.addStatus(status, ImageStatusHelper.IS_SHOW_ORIGINAL_BTN);
        }
        mWatchImageVO.setStatus(status);

        updateActivityBtn();

        RequestOptions requestOptions = new RequestOptions()
                .error(R.drawable.placeholder_error)
                .placeholder(R.drawable.placeholder_loading)
                .fitCenter();

        RequestManager requestManager = Glide.with(getContext());

        RequestBuilder<Drawable> requestBuilder;
        if (originalExistInGlideCache) {
            requestBuilder = requestManager.load(mWatchImageVO.getOriginalUrl());
        } else {
            requestBuilder = requestManager.load(mWatchImageVO.getBlurryUrl());
        }

        requestBuilder.apply(requestOptions)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e,
                                                Object model,
                                                Target<Drawable> target,
                                                boolean isFirstResource) {

                        int curStatus = mWatchImageVO.getStatus();

                        // 移除 loading
                        curStatus = ImageStatusHelper.removeStatus(curStatus, ImageStatusHelper.IS_LOADING);
                        // 显示 "显示原图" 按钮
                        curStatus = ImageStatusHelper.addStatus(curStatus, ImageStatusHelper.IS_SHOW_ORIGINAL_BTN);
                        // 隐藏 "下载" 按钮
                        curStatus = ImageStatusHelper.removeStatus(curStatus, ImageStatusHelper.IS_SHOW_DOWNLOAD);

                        mWatchImageVO.setStatus(curStatus);

                        updateActivityBtn();

                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource,
                                                   Object model,
                                                   Target<Drawable> target,
                                                   DataSource dataSource,
                                                   boolean isFirstResource) {

                        int curStatus = mWatchImageVO.getStatus();

                        // 移除 loading
                        curStatus = ImageStatusHelper.removeStatus(curStatus, ImageStatusHelper.IS_LOADING);
                        // 显示 "显示原图" 按钮
                        curStatus = ImageStatusHelper.addStatus(curStatus, ImageStatusHelper.IS_SHOW_ORIGINAL_BTN);
                        // 显示 "下载" 按钮
                        curStatus = ImageStatusHelper.addStatus(curStatus, ImageStatusHelper.IS_SHOW_DOWNLOAD);

                        mWatchImageVO.setStatus(curStatus);

                        updateActivityBtn();

                        return false;
                    }
                })
                .into(photoView);
    }

    /**
     * 转换为清晰图片
     */
    public void changeToOriginal() {
        Glide.with(this)
                .load(mWatchImageVO.getOriginalUrl())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e,
                                                Object model, Target<Drawable> target,
                                                boolean isFirstResource) {

                        int curStatus = mWatchImageVO.getStatus();

                        // 移除 loading
                        curStatus = ImageStatusHelper.removeStatus(curStatus, ImageStatusHelper.IS_LOADING);

                        // 显示 "显示原图" 按钮
                        curStatus = ImageStatusHelper.addStatus(curStatus, ImageStatusHelper.IS_SHOW_ORIGINAL_BTN);

                        mWatchImageVO.setStatus(curStatus);

                        updateActivityBtn();

                        ToastUtils.show(getString(R.string.image_original_load_failure));

                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource,
                                                   Object model,
                                                   Target<Drawable> target,
                                                   DataSource dataSource,
                                                   boolean isFirstResource) {

                        int curStatus = mWatchImageVO.getStatus();

                        // 移除 loading
                        curStatus = ImageStatusHelper.removeStatus(curStatus, ImageStatusHelper.IS_LOADING);

                        // 隐藏 "显示原图" 按钮
                        curStatus = ImageStatusHelper.removeStatus(curStatus, ImageStatusHelper.IS_SHOW_ORIGINAL_BTN);

                        // 显示 "下载" 按钮
                        curStatus = ImageStatusHelper.addStatus(curStatus, ImageStatusHelper.IS_SHOW_DOWNLOAD);

                        mWatchImageVO.setStatus(curStatus);

                        updateActivityBtn();

                        ToastUtils.show(getString(R.string.image_original_load_suc));

                        return false;
                    }
                })
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource,
                                                @Nullable Transition<? super Drawable> transition) {
//                        Glide.with(getContext())
//                                .load(mWatchImageVO.getOriginalUrl())
//                                .into(photoView);

                        TyUtils.getGlideRequest(
                                ImageWatcherFragment.this,
                                getContext(),
                                mWatchImageVO.getOriginalUrl(),
                                null,
                                photoView);
                    }
                });
    }

    /**
     * 刷新 activity 的按钮
     */
    private void updateActivityBtn() {
        FragmentActivity activity = getActivity();
        if (activity instanceof ImageWatcherActivity) {
            ((ImageWatcherActivity) activity).updateControlBtn();
        }
    }

}