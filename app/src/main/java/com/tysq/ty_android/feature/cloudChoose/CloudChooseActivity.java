package com.tysq.ty_android.feature.cloudChoose;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bit.utils.KeyboardUtils;
import com.bit.view.activity.BitBaseActivity;
import com.tysq.ty_android.R;
import com.tysq.ty_android.adapter.TextWatcherAdapter;
import com.tysq.ty_android.feature.cloudChoose.listener.CloudChooseChangeListener;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import response.cloud.FileInfoResp;
import vo.cloud.CloudFileVO;

/**
 * author       : frog
 * time         : 2019/6/5 下午2:11
 * desc         : 云盘选择
 * version      : 1.3.0
 */
public final class CloudChooseActivity extends BitBaseActivity
        implements View.OnClickListener, CloudChooseChangeListener {

    public static final String TYPE = "TYPE";
    public static final String FROM = "FROM";
    public static final String LIMIT = "LIMIT";
    public static final String FILE_ID = "FILE_ID";
    public static final int NO_LIMIT = -1;

    public static final int IMAGE = 1;
    public static final int VIDEO = 2;
    public static final int AUDIO = 3;
    public static final int OTHER = 4;

    // 头像
    public static final int HEAD_PHOTO = 5;
    // 封面
    public static final int COVER = 6;

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_search)
    ImageView ivSearch;
    @BindView(R.id.tv_confirm)
    TextView tvConfirm;
    @BindView(R.id.frame_layout_container)
    FrameLayout frameLayoutContainer;
    @BindView(R.id.ll_search)
    LinearLayout llSearch;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.iv_clear)
    ImageView ivClear;

    private int mType;
    private CloudChooseFragment fragment;
    private String mFrom;
    private int mLimit;
    private int mFileId = -1;

    /**
     * @param context 上下文
     * @param from    来自的页面
     * @param type    类型
     * @param limit   限制选择数量
     */
    public static void startActivity(Context context,
                                     String from,
                                     int type,
                                     int limit) {
        Intent intent = new Intent(context, CloudChooseActivity.class);

        intent.putExtra(TYPE, type);
        intent.putExtra(FROM, from);
        intent.putExtra(LIMIT, limit);

        context.startActivity(intent);
    }

    /**
     * @param context 上下文
     * @param from    来自的页面
     * @param type    类型
     * @param limit   限制选择数量
     * @param fileId  文件id
     */
    public static void startActivity(Context context,
                                     String from,
                                     int type,
                                     int limit,
                                     int fileId) {
        Intent intent = new Intent(context, CloudChooseActivity.class);

        intent.putExtra(TYPE, type);
        intent.putExtra(FROM, from);
        intent.putExtra(LIMIT, limit);
        intent.putExtra(FILE_ID, fileId);

        context.startActivity(intent);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_cloud_choose;
    }

    @Override
    protected void initIntent(Intent intent) {
        mType = intent.getIntExtra(TYPE, OTHER);
        mFrom = intent.getStringExtra(FROM);
        mLimit = intent.getIntExtra(LIMIT, NO_LIMIT);
        mFileId = intent.getIntExtra(FILE_ID, -1);
    }

    @Override
    protected void initView() {

        fragment = CloudChooseFragment.newInstance(mType, mLimit);
        fragment.setListener(this);

        String title;
        switch (mType) {
            case IMAGE:
                title = getString(R.string.cloud_choose_img);
                break;

            case VIDEO:
                title = getString(R.string.cloud_choose_video);
                break;

            case AUDIO:
                title = getString(R.string.cloud_choose_audio);
                break;

            case HEAD_PHOTO:
                title = getString(R.string.head_photo_change_title);
                break;

            case COVER:
                title = getString(R.string.cloud_cover_title);
                break;

            default:
                title = "";
                break;
        }

        tvTitle.setText(title);

        ivBack.setOnClickListener(this);
        ivSearch.setOnClickListener(this);
        tvConfirm.setOnClickListener(this);
        ivClear.setOnClickListener(this);

        etSearch.setOnEditorActionListener((textView, action, keyEvent) -> {

            if (action == EditorInfo.IME_ACTION_SEARCH ||
                    (keyEvent != null && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER &&
                            keyEvent.getAction() == KeyEvent.ACTION_DOWN)) {

                KeyboardUtils.hideSoftInput(etSearch);

                chooseChange(0);

                fragment.setSearchContent(getSearchContent());
                fragment.reload();

                return true;
            }

            return false;
        });

        etSearch.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(Editable s) {
                fragment.setSearchContent(getSearchContent());
            }
        });

        fragment.setSearchContent(getSearchContent());
        addLayerFragment(frameLayoutContainer.getId(), fragment);

        chooseChange(0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;

            case R.id.iv_search:
                llSearch.setVisibility(View.VISIBLE);
                ivSearch.setVisibility(View.GONE);
                break;

            case R.id.iv_clear:
                etSearch.setText("");
                break;

            case R.id.tv_confirm:
                if (mType == HEAD_PHOTO) {
                    handleConfirmForHeadPhoto();
                } else if (mType == COVER) {
                    handleCoverForCloud();
                } else {
                    handleConfirmForJerry();
                }
                break;
        }
    }

    /**
     * 提交文件封面
     */
    private void handleCoverForCloud() {
        showDialog();
        fragment.commitCover(mFileId);
    }

    /**
     * 提交头像更换
     */
    private void handleConfirmForHeadPhoto() {
        showDialog();
        fragment.commitHeadPhotoChangeInfo();
    }

    /**
     * 提交给富文本
     */
    private void handleConfirmForJerry() {
        List<FileInfoResp.FileItem> fileItemList = fragment.getSelFileInfo();
        CloudFileVO cloudFileVO = new CloudFileVO(mFrom, mType, fileItemList);
        EventBus.getDefault().post(cloudFileVO);
        finish();
    }

    /**
     * 获取搜索框内容
     */
    private String getSearchContent() {
        return etSearch.getText().toString().trim();
    }

    @Override
    public void chooseChange(int count) {
        if (count <= 0) {
            tvConfirm.setText(
                    getString(R.string.cloud_choose_confirm));
            tvConfirm.setEnabled(false);
        } else if (mType == HEAD_PHOTO || mType == COVER) {
            tvConfirm.setEnabled(true);
        } else {
            tvConfirm.setText(
                    String.format(getString(R.string.cloud_choose_confirm_count), count));
            tvConfirm.setEnabled(true);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        fragment.setListener(null);
    }
}
