package com.tysq.ty_android.feature.video;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.abc.lib_cache.JerryProxy;
import com.bit.view.fragment.BitBaseFragment;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.tysq.ty_android.R;
import com.tysq.ty_android.utils.TyUtils;

import butterknife.BindView;

/**
 * author       : frog
 * time         : 2019/4/15 下午6:07
 * desc         :
 * version      : 1.3.0
 */
public class VideoFragment extends BitBaseFragment {

    public static final String URL = "URL";
    public static final String TITLE = "TITLE";
    public static final String COVER = "COVER";

    private String mUrl;
    private String mTitle;
    private String mCover;

    @BindView(R.id.video_player)
    StandardGSYVideoPlayer videoPlayer;

    OrientationUtils orientationUtils;

    public static VideoFragment newInstance(String url,
                                            String title,
                                            String cover) {

        Bundle args = new Bundle();
        args.putString(URL, url);
        args.putString(TITLE, title);
        args.putString(COVER, cover);

        VideoFragment fragment = new VideoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initArgs(Bundle arguments) {
        super.initArgs(arguments);
        mUrl = arguments.getString(URL);
        mTitle = arguments.getString(TITLE);
        mCover = arguments.getString(COVER);
    }

    @Override
    protected View onCreateFragmentView(LayoutInflater inflater,
                                        @Nullable ViewGroup container,
                                        @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_video, container, false);
    }

    @Override
    protected void initView(View view) {

        String proxyUrl = JerryProxy.getInstance().getProxyUrl(mUrl);
        videoPlayer.setUp(proxyUrl, false, mTitle);

        //增加封面
        ImageView imageView = new ImageView(getContext());
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        TyUtils.getGlideRequest(
                this,
                getContext(),
                mCover,
                null,
                imageView);

        videoPlayer.setThumbImageView(imageView);

        //增加title
        videoPlayer.getTitleTextView().setVisibility(View.VISIBLE);
        //设置返回键
        videoPlayer.getBackButton().setVisibility(View.VISIBLE);

        //设置旋转
        orientationUtils = new OrientationUtils(getActivity(), videoPlayer);

        //设置全屏按键功能,这是使用的是选择屏幕，而不是全屏
        videoPlayer.getFullscreenButton().setOnClickListener(v -> orientationUtils.resolveByClick());

        //是否可以滑动调整
        videoPlayer.setIsTouchWiget(true);

        //设置返回按键功能
        videoPlayer.getBackButton().setOnClickListener(v -> {
            if (getActivity() == null) {
                return;
            }
            getActivity().finish();
        });
        videoPlayer.startPlayLogic();
    }

    @Override
    public void onPause() {
        super.onPause();
        videoPlayer.onVideoPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        videoPlayer.onVideoResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        GSYVideoManager.releaseAllVideos();
        if (orientationUtils != null)
            orientationUtils.releaseListener();
    }

    @Override
    public boolean onConsumeBackEvent(FragmentManager fragmentManager) {

        //先返回正常状态
        if (orientationUtils.getScreenType() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            videoPlayer.getFullscreenButton().performClick();
            return true;
        }

        //释放所有
        videoPlayer.setVideoAllCallBack(null);

        return super.onConsumeBackEvent(fragmentManager);
    }

}
