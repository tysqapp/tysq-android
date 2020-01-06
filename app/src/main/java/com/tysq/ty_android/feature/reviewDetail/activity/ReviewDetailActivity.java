package com.tysq.ty_android.feature.reviewDetail.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bit.view.activity.BitBaseActivity;
import com.tysq.ty_android.R;
import com.tysq.ty_android.app.TyApplication;
import com.tysq.ty_android.config.Constant;
import com.tysq.ty_android.feature.rank.tip.RankTipDialog;
import com.tysq.ty_android.feature.review.ReviewActivity;
import com.tysq.ty_android.feature.reviewDetail.activity.di.DaggerReviewDetailComponent;
import com.tysq.ty_android.feature.reviewDetail.activity.di.ReviewDetailModule;
import com.tysq.ty_android.feature.reviewDetail.fragment.ReviewDetailFragment;
import com.tysq.ty_android.feature.reviewDetail.listener.ReviewDetailListener;

import butterknife.BindView;
import response.rank.JudgementResp;

/**
 * author       : frog
 * time         : 2019/5/21 下午5:06
 * desc         : 评论详情
 * version      : 1.3.0
 */
public class ReviewDetailActivity
        extends BitBaseActivity<ReviewDetailPresenter>
        implements View.OnClickListener,
        ReviewDetailListener,
        ReviewDetailView {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.frame_layout_container)
    FrameLayout frameLayoutContainer;
    @BindView(R.id.ll_enter_review)
    LinearLayout llEnterReview;

    private String mArticleId;
    private String mReviewId;
    private boolean mIsCanDelete;
    private boolean mIsCanForbid;

    private String mReceiverName;
    private int mTopUserId;
    private String mParentId;

    private ReviewDetailFragment mFragment;

    public static void startActivity(Context context,
                                     String articleId,
                                     String reviewId,
                                     boolean isCanDelete,
                                     boolean isCanForbid) {
        Intent intent = new Intent(context, ReviewDetailActivity.class);
        intent.putExtra(ReviewDetailFragment.ARTICLE_ID, articleId);
        intent.putExtra(ReviewDetailFragment.REVIEW_ID, reviewId);
        intent.putExtra(ReviewDetailFragment.IS_CAN_DELETE, isCanDelete);
        intent.putExtra(ReviewDetailFragment.IS_CAN_FORBID, isCanForbid);

        context.startActivity(intent);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_review_detail;
    }

    @Override
    protected void initIntent(Intent intent) {
        mArticleId = intent.getStringExtra(ReviewDetailFragment.ARTICLE_ID);
        mReviewId = intent.getStringExtra(ReviewDetailFragment.REVIEW_ID);
        mIsCanDelete = intent.getBooleanExtra(ReviewDetailFragment.IS_CAN_DELETE, false);
        mIsCanForbid = intent.getBooleanExtra(ReviewDetailFragment.IS_CAN_FORBID, false);
    }

    @Override
    protected void initView() {

        ivBack.setOnClickListener(this);
        llEnterReview.setOnClickListener(this);

        mFragment = ReviewDetailFragment
                .newInstance(mArticleId,
                        mReviewId,
                        mIsCanDelete,
                        mIsCanForbid);

        addLayerFragment(frameLayoutContainer.getId(), mFragment);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;

            case R.id.ll_enter_review:

                if (TextUtils.isEmpty(mReceiverName)) {
                    return;
                }

                if (TextUtils.isEmpty(mParentId)) {
                    return;
                }

                showDialog();
                mPresenter.getJudgement(mArticleId);

                break;
        }
    }

    @Override
    public void onSetInfo(String receiverName, int topUserId, String parentId) {
        mReceiverName = receiverName;
        mTopUserId = topUserId;
        mParentId = parentId;
    }

    @Override
    protected void registerDagger() {
        DaggerReviewDetailComponent.builder()
                .appComponent(TyApplication.getAppComponent())
                .reviewDetailModule(new ReviewDetailModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void onGetJudgement(JudgementResp value) {
        if (value.isIsSatisfy()) {
            ReviewActivity.startActivity(
                    this,
                    ReviewDetailFragment.TAG,
                    mReceiverName,
                    mArticleId,
                    mTopUserId,
                    mParentId,
                    mParentId,
                    ReviewDetailFragment.IS_NEED_REFRESH ? 1 : 0);
        } else {
            RankTipDialog
                    .newInstance(value.getLimitScore(), Constant.JudgementType.COMMENT)
                    .show(this);
        }

    }
}
