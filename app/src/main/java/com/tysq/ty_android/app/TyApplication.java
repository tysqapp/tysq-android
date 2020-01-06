package com.tysq.ty_android.app;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.webkit.CookieSyncManager;

import com.abc.lib_cache.JerryProxy;
import com.abc.lib_utils.toast.ToastUtils;
import com.bit.cache.BitBaseCache;
import com.bit.config.BitManager;
import com.bit.utils.UIUtils;
import com.crashlytics.android.Crashlytics;
import com.raizlabs.android.dbflow.config.DatabaseConfig;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.config.TyGeneratedDatabaseHolder;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.model.VideoOptionModel;
import com.tysq.ty_android.R;
import com.tysq.ty_android.config.TyConfig;
import com.tysq.ty_android.di.component.AppComponent;
import com.tysq.ty_android.di.component.DaggerAppComponent;
import com.tysq.ty_android.di.module.AppModule;
import com.tysq.ty_android.local.db.TyDB;
import com.tysq.ty_android.local.sp.UserCache;
import com.tysq.ty_android.utils.ScreenAdapterUtils;
import com.abc.lib_multi_download.config.DownloadConfig;
import com.abc.lib_multi_download.download.JerryDownloadConfig;
import com.zinc.lib_jerry_editor.config.JerryConfig;

import java.util.ArrayList;
import java.util.List;

import tv.danmaku.ijk.media.player.IjkMediaPlayer;
import utils.UploadConfig;

/**
 * author       : frog
 * time         : 2019/4/11 下午2:34
 * desc         :
 * version      : 1.3.0
 */
public class TyApplication extends MultiDexApplication {

    private static AppComponent APP_COMPONENT;
    private static Context mContext;

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mContext = this;

        initDagger();
        initDb();
        initDownload();
        initUtils();
        initEditor();
        initBitFrame();
        initUploading();
        initVideo();
        initCache();

        if (UserCache.getDefault() != null) {
            Crashlytics.setUserIdentifier(UserCache.getDefault().getAccountId() + "");
        }

        CookieSyncManager.createInstance(this);
    }

    public static AppComponent getAppComponent() {
        return APP_COMPONENT;
    }

    public static Context getContext() {
        return mContext;
    }

    /**
     * 初始化dagger
     */
    private void initDagger() {
        APP_COMPONENT = DaggerAppComponent
                .builder()
                .appModule(new AppModule(this))
                .build();
        APP_COMPONENT.inject(this);
    }

    /**
     * 初始化数据库
     */
    private void initDb() {
        FlowManager
                .init(FlowConfig.builder(this)
                        .addDatabaseConfig(
                                DatabaseConfig.builder(TyDB.class)
                                        .databaseName(TyDB.NAME)
                                        .build()
                        )
                        .build());

        FlowManager.initModule(TyGeneratedDatabaseHolder.class);
    }

    /**
     * 初始化下载
     */
    private void initDownload() {

        JerryDownloadConfig.setThreadCount(2);
        JerryDownloadConfig.setDownloadFolder(TyConfig.Folder.CLOUD_DOWNLOAD);
        JerryDownloadConfig.init(this);

        DownloadConfig.getInstance().setRangeSize(5 * 1024 * 1024L);

    }

    /**
     * 初始化工具
     */
    private void initUtils() {
        BitBaseCache.init(this);
        ToastUtils.init(this);
        TyConfig.init(this);
        // 屏幕适配
        ScreenAdapterUtils.init(this);
    }

    /**
     * 初始化富文本
     */
    private void initEditor() {
        JerryConfig.init(this);
        JerryConfig.setPADDING(UIUtils.dip2px(this, 20));
    }

    /**
     * 初始化框架
     */
    private void initBitFrame() {
        BitManager.getInstance()
                .setRetryViewLayout(R.layout.blank_net_block);

        BitManager.getInstance()
                .setRetryBtnId(R.id.tv_refresh);

        BitManager.getInstance()
                .setEmptyBtnId(R.id.tv_refresh);
    }

    /**
     * 初始化上传控件
     */
    private void initUploading() {
        UploadConfig.CONTEXT = this;
    }

    /**
     * 初始化视屏播放器
     */
    private void initVideo() {
        VideoOptionModel videoOptionModel =
                new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_PLAYER,
                        "mediacodec-all-videos",
                        1);
        List<VideoOptionModel> list = new ArrayList<>();
        list.add(videoOptionModel);
        GSYVideoManager.instance().setOptionModelList(list);
    }

    /**
     * 初始化缓存
     */
    private void initCache() {
        JerryProxy.getInstance().init(this);
        JerryProxy.getInstance().startServer();
    }

}
