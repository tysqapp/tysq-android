package com.tysq.ty_android.feature.review;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.abc.lib_utils.toast.ToastUtils;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bit.view.activity.BitBaseActivity;
import com.tysq.ty_android.R;
import com.tysq.ty_android.app.TyApplication;
import com.tysq.ty_android.feature.articleDetail.fragment.ArticleDetailFragment;
import com.tysq.ty_android.feature.cloudChoose.CloudChooseActivity;
import com.tysq.ty_android.feature.review.di.DaggerReviewComponent;
import com.tysq.ty_android.feature.review.di.ReviewModule;
import com.tysq.ty_android.feature.reviewDetail.fragment.ReviewDetailFragment;
import com.tysq.ty_android.login.CheckLogin;
import com.tysq.ty_android.utils.TyUtils;
import com.tysq.ty_android.widget.TagFlowLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import eventbus.ReviewAddEvent;
import response.cloud.FileInfoResp;
import vo.article.ArticleDetailVO;
import vo.cloud.CloudFileVO;

/**
 * author       : frog
 * time         : 2019/5/22 下午3:53
 * desc         : 发表评论
 * version      : 1.3.0
 */

public final class ReviewActivity extends BitBaseActivity<ReviewPresenter>
        implements ReviewView, View.OnClickListener {

    // 选择图片的最大数量
    private static final int MAX_SELECT_IMG_COUNT = 9;

    public static final int EMPTY = 0;
    public static final String EMPTY_STRING = "";

    private static final String ARTICLE_ID = "ARTICLE_ID";
    private static final String RECEIVER_NAME = "RECEIVER_NAME";
    private static final String AT_ACCOUNT_ID = "AT_ACCOUNT_ID";
    private static final String PARENT_ID = "PARENT_ID";
    private static final String FATHER_ID = "FATHER_ID";

    // 位置
    private static final String POSITION = "POSITION";
    private static final String FROM = "FROM";

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_confirm)
    TextView tvConfirm;
    @BindView(R.id.et_content)
    EditText etContent;
    @BindView(R.id.tag_flow)
    TagFlowLayout tagFlow;
    @BindView(R.id.iv_img)
    ImageView ivImg;

    private List<FileInfoResp.FileItem> mImgList = new ArrayList<>();
    private List<View> mViewList = new ArrayList<>();

    private String mReceiverName;
    private String mArticleId;
    private int mAtAccountId;
    private String mParentId;
    private String mFatherId;
    private int mPosition;
    private String mFrom;

    /**
     * @param context      上下文
     * @param from         来的页面
     *                     目前参数有：
     *                     1、{@link ArticleDetailFragment#TAG}
     *                     2、{@link ReviewDetailFragment#TAG}
     * @param receiverName 被评论者的名字
     * @param articleId    文章id
     * @param atAccountId  被评论者的id，如果没有，传 {@link #EMPTY}
     * @param parentId     一级评论的id，如果没有，传 {@link #EMPTY_STRING}
     * @param fatherId     被评论的该条评论的id，如果没有，传 {@link #EMPTY_STRING}
     * @param position     下标
     */
    @CheckLogin
    public static void startActivity(Context context,
                                     String from,
                                     String receiverName,
                                     String articleId,
                                     int atAccountId,
                                     String parentId,
                                     String fatherId,
                                     int position) {
        Intent intent = new Intent(context, ReviewActivity.class);

        intent.putExtra(POSITION, position);
        intent.putExtra(FROM, from);

        intent.putExtra(ARTICLE_ID, articleId);
        intent.putExtra(RECEIVER_NAME, receiverName);
        intent.putExtra(AT_ACCOUNT_ID, atAccountId);
        intent.putExtra(PARENT_ID, parentId);
        intent.putExtra(FATHER_ID, fatherId);

        context.startActivity(intent);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_review;
    }

    @Override
    protected void initIntent(Intent intent) {
        mPosition = intent.getIntExtra(POSITION, -1);
        mFrom = intent.getStringExtra(FROM);

        mArticleId = intent.getStringExtra(ARTICLE_ID);
        mReceiverName = intent.getStringExtra(RECEIVER_NAME);
        mAtAccountId = intent.getIntExtra(AT_ACCOUNT_ID, EMPTY);
        mParentId = intent.getStringExtra(PARENT_ID);
        mFatherId = intent.getStringExtra(FATHER_ID);
    }

    @Override
    protected void initView() {
        etContent.setHint(String.format(getString(R.string.review_send_review), mReceiverName));

        ivBack.setOnClickListener(this);
        tvConfirm.setOnClickListener(this);
        ivImg.setOnClickListener(this);
    }

    @Override
    protected void registerDagger() {
        DaggerReviewComponent.builder()
                .appComponent(TyApplication.getAppComponent())
                .reviewModule(new ReviewModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                safeFinish();
                break;

            case R.id.tv_confirm:
                confirmReview();
                break;

            case R.id.iv_img:
                int limit = MAX_SELECT_IMG_COUNT - mImgList.size();

                CloudChooseActivity
                        .startActivity(this, TAG, CloudChooseActivity.IMAGE, limit);
                break;
        }
    }

    private void confirmReview() {

        String content = getContent();

        if (TextUtils.isEmpty(content) && mImgList.isEmpty()) {
            ToastUtils.show(getString(R.string.review_send_publish_empty_tip));
            return;
        }

        showDialog();

        List<Integer> imgList = new ArrayList<>();
        for (FileInfoResp.FileItem item : mImgList) {
            imgList.add(item.getId());
        }

        mPresenter.publishComment(mArticleId, mAtAccountId, content,
                mParentId, mFatherId, imgList);

    }

    /**
     * 退出提示
     */
    private void safeFinish() {
        if (!TextUtils.isEmpty(getContent()) || mImgList.size() > 0) {
            new MaterialDialog.Builder(this)
                    .title(getString(R.string.review_send_quit))
                    .content(getString(R.string.review_send_quit_content))
                    .negativeColor(ContextCompat.getColor(this, R.color.et_tip_text_color))
                    .negativeText(getString(R.string.common_confirm))
                    .onNegative((dialog, which) -> {
                        mImgList.clear();
                        mViewList.clear();
                        finish();
                    })
                    .positiveColor(ContextCompat.getColor(this, R.color.main_blue_color))
                    .positiveText(getString(R.string.common_cancel))
                    .show();
            return;
        }

        finish();
    }

    @Override
    protected int getInitRegister() {
        return EVENT_BUS | BUTTER_KNIFE;
    }

    /**
     * 选择文件回调
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSelResult(CloudFileVO cloudFileVO) {
        if (!cloudFileVO.getFrom().equals(TAG)) {
            return;
        }

        mImgList.addAll(cloudFileVO.getFileList());

        createReviewImage(tagFlow, mImgList);

    }

    /**
     * 获取内容
     */
    private String getContent() {
        return etContent.getText().toString().trim();
    }

    /**
     * 填充用户图片
     */
    private void createReviewImage(TagFlowLayout tagFlowLayout,
                                   List<FileInfoResp.FileItem> imageUrlList) {

        tagFlowLayout.removeAllViews();

        tagFlowLayout.setVisibility(View.VISIBLE);

        for (FileInfoResp.FileItem fileItem : imageUrlList) {
            View item = getLayoutInflater()
                    .inflate(R.layout.widget_reviewing_image, tagFlowLayout, false);

            ImageView ivImage = item.findViewById(R.id.iv_image);

            TyUtils.getGlideRequest(
                    this,
                    this,
                    fileItem.getUrl(),
                    null,
                    ivImage);

            ImageView ivDelete = item.findViewById(R.id.iv_delete);
            ivDelete.setOnClickListener(v -> {
                deleteImg(fileItem);
            });

            mViewList.add(item);

            // 将 item 添加进流式布局
            tagFlowLayout.addView(item);
        }

    }

    /**
     * 删除选中图片
     *
     * @param fileItem
     */
    private void deleteImg(FileInfoResp.FileItem fileItem) {

        int index = -1;
        for (int i = 0; i < mImgList.size(); i++) {
            if (mImgList.get(i) == fileItem) {
                index = i;
                break;
            }
        }

        if (index == -1) {
            return;
        }

        mImgList.remove(fileItem);
        mViewList.remove(index);

        tagFlow.removeViewAt(index);

    }

    @Override
    public void onPublishComment(ArticleDetailVO articleDetailVO, int score) {

        hideDialog();

        EventBus.getDefault().post(new ReviewAddEvent(mArticleId, mFrom, mPosition, articleDetailVO));

        if (score <= 0) {
            ToastUtils.show(getString(R.string.review_send_success));
        } else {
            String tip = getString(R.string.review_send_success_with_score);
            tip = String.format(tip, score);
            ToastUtils.show(tip);
        }

        finish();

    }

}
