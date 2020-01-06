package com.jerry.image_watcher;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.abc.lib_utils.toast.ToastUtils;
import com.bit.view.activity.BitBaseActivity;
import com.jerry.image_watcher.model.WatchImageVO;
import com.jerry.image_watcher.utils.ImageStatusHelper;
import com.jerry.image_watcher.utils.ImageWatcherUtils;
import com.luck.picture.lib.config.PictureMimeType;
import com.tysq.ty_android.R;
import com.tysq.ty_android.utils.TyFileUtils;
import com.zinc.libpermission.annotation.Permission;
import com.zinc.libpermission.annotation.PermissionCanceled;
import com.zinc.libpermission.annotation.PermissionDenied;
import com.zinc.libpermission.bean.CancelInfo;
import com.zinc.libpermission.bean.DenyInfo;
import com.zinc.libpermission.utils.JPermissionUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import butterknife.BindView;

/**
 * author       : frog
 * time         : 2019-08-29 18:00
 * desc         : 图片查看器
 * version      : 1.4.0
 */
public class ImageWatcherActivity
        extends BitBaseActivity
        implements ViewPager.OnPageChangeListener {

    private static final String IMAGE_DATA = "image_data";
    private static final String IMAGE_POSITION = "image_position";

    // 显示的图片数据
    private ArrayList<WatchImageVO> mImageDataList;

    @BindView(R.id.tv_num)
    TextView tvNum;
    @BindView(R.id.view_pager)
    ViewPager viewPager;

    @BindView(R.id.iv_download)
    ImageView ivDownload;
    @BindView(R.id.tv_original)
    TextView tvOriginal;

    // 显示的下标
    private int mPosition;

    public static void startActivity(Context context,
                                     WatchImageVO watchImageVO) {
        ArrayList<WatchImageVO> list = new ArrayList<>();
        list.add(watchImageVO);
        startActivity(context, list, 0);
    }

    public static void startActivity(Context context,
                                     ArrayList<WatchImageVO> dataList) {
        startActivity(context, dataList, 0);
    }

    public static void startActivity(Context context,
                                     ArrayList<WatchImageVO> dataList,
                                     int position) {
        Intent intent = new Intent(context, ImageWatcherActivity.class);
        intent.putParcelableArrayListExtra(IMAGE_DATA, dataList);
        intent.putExtra(IMAGE_POSITION, position);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate() {

        setStatusBarColor(R.color.black);
        initContentView();

        registerDagger();
        registerLogic();

        initIntent(getIntent());
        initView();

    }

    @Override
    protected void onResume() {
        super.onResume();
        setFullScreen();
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_jerry_image_watcher;
    }

    @Override
    protected void initIntent(Intent intent) {
        mImageDataList = intent.getParcelableArrayListExtra(IMAGE_DATA);
        mPosition = intent.getIntExtra(IMAGE_POSITION, 0);
    }

    @Override
    protected void initView() {

        if (mPosition >= mImageDataList.size()) {
            mPosition = mImageDataList.size() - 1;
        }

        viewPager.setAdapter(new ImageWatcherPagerAdapter(getSupportFragmentManager(),
                this,
                mImageDataList));
        viewPager.setCurrentItem(mPosition);

        if (mImageDataList.size() <= 1) {
            tvNum.setVisibility(View.GONE);
        } else {
            tvNum.setVisibility(View.VISIBLE);
            viewPager.addOnPageChangeListener(this);
            setImageNum(viewPager.getCurrentItem());
        }

        // 添加下载
        ivDownload.setOnClickListener(v -> {
            downloadImage();
        });

        // 查看原图
        tvOriginal.setOnClickListener(v -> {
            ImageWatcherFragment fragment = (ImageWatcherFragment) getSupportFragmentManager()
                    .findFragmentByTag(
                            "android:switcher:" + R.id.view_pager + ":" + viewPager.getCurrentItem());
            if (fragment != null) {
                if (fragment.getView() != null) {

                    WatchImageVO curItem = getCurItem();
                    int curStatus = curItem.getStatus();

                    curStatus = ImageStatusHelper.addStatus(curStatus, ImageStatusHelper.IS_LOADING);

                    curItem.setStatus(curStatus);
                    updateControlBtn();

                    fragment.changeToOriginal();
                }
            }
        });

        updateControlBtn();

    }

    /**
     * 下载图片
     */
    @Permission(value = {Manifest.permission.WRITE_EXTERNAL_STORAGE}, requestCode = 200)
    private void downloadImage() {
        WatchImageVO curItem = getCurItem();
        String url;

        if (curItem.isOriginal()) {
            url = curItem.getOriginalUrl();
        } else {
            url = curItem.getBlurryUrl();
        }

        if (TextUtils.isEmpty(url)) {
            Toast.makeText(ImageWatcherActivity.this,
                    getString(R.string.image_download_failure),
                    Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        File cacheFile = ImageWatcherUtils.getGlideCacheFileViaUrl(this, url);
        if (cacheFile == null || !cacheFile.exists()) {
            Toast.makeText(ImageWatcherActivity.this,
                    getString(R.string.image_download_failure),
                    Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        String suffix;

        String mimeType = ImageWatcherUtils.getMimeType(cacheFile);
        boolean gif = PictureMimeType.isGif(mimeType);
        if (gif) {
            suffix = ".gif";
        } else {
            suffix = PictureMimeType.getLastImgType(mimeType);
        }

        String fileName = TyFileUtils.getFileName(url + System.currentTimeMillis()) + suffix;
        File saveFolder = TyFileUtils.getImgFolder();

        File imageFile = TyFileUtils.createFile(saveFolder, fileName);
        if (imageFile == null) {
            Toast.makeText(ImageWatcherActivity.this,
                    getString(R.string.image_download_failure),
                    Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(cacheFile);
            TyFileUtils.saveToFile(imageFile, inputStream);

            String successTip = getString(R.string.image_download_success);
            successTip = String.format(successTip, imageFile.getAbsolutePath());

            TyFileUtils.updateImageInSystem(this, imageFile);

            Toast.makeText(ImageWatcherActivity.this,
                    successTip,
                    Toast.LENGTH_SHORT)
                    .show();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(ImageWatcherActivity.this,
                    getString(R.string.image_download_failure),
                    Toast.LENGTH_SHORT)
                    .show();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        // 2019-09-03 空实现
    }

    /**
     * 图片选择
     */
    @Override
    public void onPageSelected(int position) {
        // 只有多个的时候，会进行设置
        if (mImageDataList.size() > 1) {
            setImageNum(position);
        }

        updateControlBtn();

    }

    /**
     * 设置图片索引 "1/2"
     */
    private void setImageNum(int position) {
        String imageNum = getString(R.string.image_num);
        String imageNumContent
                = String.format(imageNum, position + 1, mImageDataList.size());
        tvNum.setText(imageNumContent);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        // 2019-09-03 空实现
    }

    /**
     * 获取当前的 VO 模型
     */
    private WatchImageVO getCurItem() {
        int currentItem = viewPager.getCurrentItem();
        return mImageDataList.get(currentItem);
    }

    /**
     * 更新各种状态
     */
    public void updateControlBtn() {

        WatchImageVO curItem = getCurItem();
        int status = curItem.getStatus();

        // 是否显示 "查看原图" 按钮
        if (ImageStatusHelper.isContain(status, ImageStatusHelper.IS_SHOW_ORIGINAL_BTN)
                && !curItem.isOriginal()) {
            tvOriginal.setVisibility(View.VISIBLE);

            if (ImageStatusHelper.isContain(status, ImageStatusHelper.IS_LOADING)) { // 显示 "加载中..."
                tvOriginal.setText(getString(R.string.image_loading));
                tvOriginal.setClickable(false);
            } else { // 显示 "查看原图"
                tvOriginal.setText(getString(R.string.image_original));
                tvOriginal.setClickable(true);
            }

        } else {
            tvOriginal.setVisibility(View.GONE);
        }

        if(curItem.getBlurryUrl().equals(curItem.getOriginalUrl())){
            tvOriginal.setVisibility(View.GONE);
        }

        if (ImageStatusHelper.isContain(status, ImageStatusHelper.IS_SHOW_DOWNLOAD)) {
            ivDownload.setVisibility(View.VISIBLE);
        } else {
            ivDownload.setVisibility(View.GONE);
        }

    }

    // ==================== 权限申请 ==========================
    @PermissionCanceled(requestCode = 200)
    private void cancel(CancelInfo cancelInfo) {
        ToastUtils.show(getString(R.string.image_need_save_permission));
    }

    @PermissionDenied(requestCode = 200)
    private void deny(DenyInfo denyInfo) {
        ToastUtils.show(getString(R.string.image_need_save_permission));
        JPermissionUtil.goToMenu(this);
    }

}
