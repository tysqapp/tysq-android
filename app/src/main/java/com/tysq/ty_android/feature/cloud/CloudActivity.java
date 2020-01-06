package com.tysq.ty_android.feature.cloud;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.abc.lib_utils.toast.ToastUtils;
import com.bit.utils.UIUtils;
import com.bit.view.activity.BitBaseActivity;
import com.bit.widget.NoScrollViewPager;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.tysq.ty_android.R;
import com.tysq.ty_android.config.TyFragmentManager;
import com.tysq.ty_android.feature.cloud.cloudUploading.dialog.FileTypeDialog;
import com.tysq.ty_android.feature.cloud.listener.CloudListener;
import com.zinc.lib_file_selector.LFilePicker;
import com.zinc.libpermission.annotation.Permission;
import com.zinc.libpermission.annotation.PermissionCanceled;
import com.zinc.libpermission.annotation.PermissionDenied;
import com.zinc.libpermission.bean.CancelInfo;
import com.zinc.libpermission.bean.DenyInfo;
import com.zinc.libpermission.utils.JPermissionUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import eventbus.UploadAddTaskEvent;
import factory.UploadFactory;
import model.FileModel;

/**
 * author       : frog
 * time         : 2019/6/5 上午11:47
 * desc         : 我的网盘
 * version      : 1.3.0
 */
public class CloudActivity extends BitBaseActivity
        implements View.OnClickListener, CloudListener, FileTypeDialog.FileTypeListener {

    private static final int REQUEST_FILE_CODE = 1001;
    private static final int REQUEST_MEDIA_CODE = 1002;

    private static final int PAGE_COUNT = 3;

    @BindView(R.id.iv_back)
    ImageView ivBack;

    @BindView(R.id.ll_uploaded)
    LinearLayout llUploaded;
    @BindView(R.id.tv_uploaded)
    TextView tvUploaded;
    @BindView(R.id.v_uploaded)
    View vUploaded;

    @BindView(R.id.ll_uploading)
    LinearLayout llUploading;
    @BindView(R.id.tv_uploading)
    TextView tvUploading;
    @BindView(R.id.v_uploading)
    View vUploading;

    @BindView(R.id.ll_download)
    LinearLayout llDownload;
    @BindView(R.id.tv_download)
    TextView tvDownload;
    @BindView(R.id.v_download)
    View vDownload;

    @BindView(R.id.view_pager)
    NoScrollViewPager viewPager;
    @BindView(R.id.tv_upload)
    TextView tvUpload;

    private FileTypeDialog mFileTypeDialog;

    @Override
    protected int getLayout() {
        return R.layout.activity_cloud;
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, CloudActivity.class);

        context.startActivity(intent);
    }

    @Override
    protected void initIntent(Intent intent) {

    }

    @Override
    protected void initView() {
        mFileTypeDialog = FileTypeDialog.newInstance();

        ivBack.setOnClickListener(this);
        llUploaded.setOnClickListener(this);
        llUploading.setOnClickListener(this);
        llDownload.setOnClickListener(this);
        tvUpload.setOnClickListener(this);

        setUploadedInfo(0);
        setUploadingInfo(0);
        setDownloadInfo(0);

        viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager()));
        viewPager.setOffscreenPageLimit(PAGE_COUNT);

        setUploadedState(0);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.ll_uploaded:
                setUploadedState(0);
                break;
            case R.id.ll_uploading:
                setUploadedState(1);
                break;
            case R.id.ll_download:
                setUploadedState(2);
                break;
            case R.id.tv_upload:
                chooseFile();
                break;
        }
    }

    /**
     * 设置上传的状态
     *
     * @param index 选择的下标
     */
    private void setUploadedState(int index) {
        tvUploaded.setTextColor(ContextCompat.getColor(this, R.color.main_text_color));
        tvUploading.setTextColor(ContextCompat.getColor(this, R.color.main_text_color));
        tvDownload.setTextColor(ContextCompat.getColor(this, R.color.main_text_color));

        vUploaded.setVisibility(View.GONE);
        vUploading.setVisibility(View.GONE);
        vDownload.setVisibility(View.GONE);

        switch (index) {
            case 0:
                tvUploaded.setTextColor(ContextCompat.getColor(this, R.color.main_blue_color));
                vUploaded.setVisibility(View.VISIBLE);
                break;
            case 1:
                tvUploading.setTextColor(ContextCompat.getColor(this, R.color.main_blue_color));
                vUploading.setVisibility(View.VISIBLE);
                break;
            case 2:
                tvDownload.setTextColor(ContextCompat.getColor(this, R.color.main_blue_color));
                vDownload.setVisibility(View.VISIBLE);
                break;
        }

        viewPager.setCurrentItem(index);

    }

    /**
     * 设置已上传的字数
     *
     * @param count 数量
     */
    public void setUploadedInfo(int count) {
        if (count <= 0) {
            tvUploaded.setText(getString(R.string.cloud_uploaded_without_num));
        } else {
            tvUploaded.setText(String.format(getString(R.string.cloud_uploaded), count));
        }
    }

    /**
     * 设置上传中的字数
     *
     * @param count 数量
     */
    public void setUploadingInfo(int count) {
        if (count <= 0) {
            tvUploading.setText(getString(R.string.cloud_uploading_without_num));
        } else {
            tvUploading.setText(String.format(getString(R.string.cloud_uploading), count));
        }
    }

    /**
     * 设置下载的字数
     *
     * @param count 数量
     */
    public void setDownloadInfo(int count) {
        if (count <= 0) {
            tvDownload.setText(getString(R.string.cloud_download_without_num));
        } else {
            tvDownload.setText(String.format(getString(R.string.cloud_download_with_num), count));
        }
    }

    @Permission(value = {Manifest.permission.WRITE_EXTERNAL_STORAGE}, requestCode = 200)
    public void chooseFile() {
        mFileTypeDialog.setListener(this);
        mFileTypeDialog.show(this);
    }

    @PermissionCanceled(requestCode = 200)
    private void cancel(CancelInfo cancelInfo) {
        ToastUtils.show(getString(R.string.upload_file_need_permission));
    }

    @PermissionDenied(requestCode = 200)
    private void deny(DenyInfo denyInfo) {
        ToastUtils.show(getString(R.string.upload_file_need_permission));
        JPermissionUtil.goToMenu(this);
    }

    @Override
    public void onModifyArticle(int type) {

        if (type == FileTypeDialog.OTHER) {
            new LFilePicker()
                    .withActivity(this)
                    .withRequestCode(REQUEST_FILE_CODE)
                    .withStartPath(Environment.getExternalStorageDirectory().getAbsolutePath())
                    .withMutilyMode(true)
                    .withMaxNum(9)
                    .start();

//                    .withActivity(this)
//                    .withRequestCode(REQUEST_FILE_CODE)
//                    .withTitle("文件选择")
//                    .withIconStyle(Constant.ICON_STYLE_YELLOW)
//
//                    //指定初始显示路径
//                    .withIsGreater(false)//过滤文件大小 小于指定大小的文件
//                    .withFileSize(1024 * 1024 * 1024)//指定文件大小为1G`
//                    .withChooseMode(false)//文件夹选择模式
//                    //.withFileFilter(new String[]{"txt", "png", "docx"})
//                    .start();
            return;
        }

        int mimeType;

        switch (type) {
            case FileTypeDialog.PIC:
                mimeType = PictureMimeType.ofImage();
                break;
            case FileTypeDialog.VIDEO:
                mimeType = PictureMimeType.ofVideo();
                break;
            case FileTypeDialog.AUDIO:
                mimeType = PictureMimeType.ofAudio();
                break;
            default:
                mimeType = PictureMimeType.ofImage();
                break;
        }

        int picWidth = UIUtils.dip2px(this, 50);
        PictureSelector.create(this)
                .openGallery(mimeType)// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .maxSelectNum(9)// 最大图片选择数量
                .minSelectNum(1)// 最小选择数量
                .imageSpanCount(3)// 每行显示个数
                .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选
                .previewImage(mimeType == PictureMimeType.ofImage())// 是否可预览图片 true or false
                .previewVideo(mimeType == PictureMimeType.ofVideo())// 是否可预览视频
                .enablePreviewAudio(mimeType == PictureMimeType.ofAudio())// 是否可播放音频 true or false
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                .synOrAsy(true)//同步true或异步false 压缩 默认同步
                .glideOverride(picWidth, picWidth)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                .hideBottomControls(false)// 是否显示uCrop工具栏，默认不显示
                .isGif(false)// 是否显示gif图片
                .freeStyleCropEnabled(true)// 裁剪框是否可拖拽
                .minimumCompressSize(100)// 小于100kb的图片不压缩
                .forResult(REQUEST_MEDIA_CODE);//结果回调onActivityResult code
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_MEDIA_CODE: {
                    // 图片、视频、音频选择结果回调
                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true  注意：音视频除外
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true  注意：音视频除外
                    // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的

                    for (LocalMedia localMedia : selectList) {
                        Log.i(TAG, "onActivityResult: " + localMedia.getPath());
                        FileModel fileModel = UploadFactory.submitTask(localMedia.getPath());
                        if (fileModel != null) {
                            EventBus.getDefault().post(new UploadAddTaskEvent(fileModel));
                        }
                    }

                    break;
                }
                case REQUEST_FILE_CODE: {
                    List<String> list = data.getStringArrayListExtra("paths");

                    Log.i(TAG, "Select file size: " + list.size() + "\n" +
                            "File list content: " + list);

                    for (String url : list) {
                        Log.i(TAG, "onActivityResult: " + url);
                        FileModel fileModel = UploadFactory.submitTask(url);
                        if (fileModel != null) {
                            EventBus.getDefault().post(new UploadAddTaskEvent(fileModel));
                        }
                    }

                    break;
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mFileTypeDialog != null) {
            mFileTypeDialog.setListener(null);
            if (mFileTypeDialog.isShowing()) {
                mFileTypeDialog.dismiss();
            }
            mFileTypeDialog = null;
        }
    }

    static class PagerAdapter extends FragmentPagerAdapter {

        PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return TyFragmentManager.getCloudFragment(position);
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

    }

}
