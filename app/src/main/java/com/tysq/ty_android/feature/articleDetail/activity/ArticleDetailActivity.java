package com.tysq.ty_android.feature.articleDetail.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.abc.lib_utils.toast.ToastUtils;
import com.bit.utils.UIUtils;
import com.bit.view.activity.BitBaseActivity;
import com.tysq.ty_android.R;
import com.tysq.ty_android.app.TyApplication;
import com.tysq.ty_android.config.Constant;
import com.tysq.ty_android.feature.articleDetail.activity.di.ArticleDetailModule;
import com.tysq.ty_android.feature.articleDetail.activity.di.DaggerArticleDetailComponent;
import com.tysq.ty_android.feature.articleDetail.code.CodeDialog;
import com.tysq.ty_android.feature.articleDetail.dialog.ControlArticleDialog;
import com.tysq.ty_android.feature.articleDetail.dialog.ExaminationArticleDialog;
import com.tysq.ty_android.feature.articleDetail.fragment.ArticleDetailFragment;
import com.tysq.ty_android.feature.articleDetail.listener.ArticleDetailActivityListener;
import com.tysq.ty_android.feature.articleDetail.tip.TopArticleTipDialog;
import com.tysq.ty_android.feature.rank.tip.RankTipDialog;
import com.tysq.ty_android.feature.review.ReviewActivity;
import com.tysq.ty_android.utils.TyUtils;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import eventbus.ArticleCollectEvent;
import eventbus.ExamOverEvent;
import eventbus.HideArticleEvent;
import eventbus.TopArticlePositionEvent;
import response.permission.PermissionResp;
import response.rank.JudgementResp;

/**
 * author       : frog
 * time         : 2019/5/19 下午3:30
 * desc         : 文章详情
 * version      : 1.3.0
 */
public class ArticleDetailActivity extends BitBaseActivity<ArticleDetailPresenter>
        implements View.OnClickListener,
        ArticleDetailActivityListener,
        ControlArticleDialog.ControlListener,
        TopArticleTipDialog.PostTopArticleListener,
        ArticleDetailView {

    @BindView(R.id.iv_back)
    ImageView ivBack;

    @BindView(R.id.tv_title)
    TextView tvTitle;

    @BindView(R.id.ll_control_bar)
    LinearLayout llControlBar;

    @BindView(R.id.v_divider)
    View vDivider;

    @BindView(R.id.frame_layout_container)
    FrameLayout frameLayoutContainer;

    @BindView(R.id.ll_enter_review)
    LinearLayout llEnterReview;
    @BindView(R.id.iv_review)
    ImageView ivReview;
    @BindView(R.id.tv_review_count)
    TextView tvReviewCount;
    @BindView(R.id.iv_star)
    ImageView ivStar;
    @BindView(R.id.iv_menu)
    ImageView ivMenu;

    @BindView(R.id.iv_code_bg)
    ImageView ivCodeBg;

    private static final int HIDE_ARTICLE = -4;
    private static final int SHOW_ARTICLE = 1;

    private String mArticleId;
    private String mCommentId;

    private String mReceiverName;
    private int mReviewCount;

    // 收集状态
    private boolean mCollectState;
    // 收集状态锁，防止多次点击
    private boolean mCollectStateLock = false;

    // 验证码
    private String mCaptchaId;

    private ArticleDetailFragment mFragment;
    private CodeDialog mCodeDialog;

    // 权限
    private PermissionResp mPermissionInfo;
    // 权限弹框
    private ControlArticleDialog mControlArticleDialog;
    //返回隐藏状态
    private int mStatus;
    private int mTopPosition;

    /**
     * @param context   上下文
     * @param articleId 文章id
     */
    public static void startActivity(Context context,
                                     String articleId) {
        startActivity(context, articleId, "");
    }

    /**
     * @param context   上下文
     * @param articleId 文章id
     * @param commentId 需要定位的评论id
     */
    public static void startActivity(Context context,
                                     String articleId,
                                     String commentId) {
        Intent intent = new Intent(context, ArticleDetailActivity.class);

        intent.putExtra(ArticleDetailFragment.ARTICLE_ID, articleId);
        intent.putExtra(ArticleDetailFragment.COMMENT_ID, commentId);

        context.startActivity(intent);
    }

    @Override
    protected void registerDagger() {
        DaggerArticleDetailComponent.builder()
                .appComponent(TyApplication.getAppComponent())
                .articleDetailModule(new ArticleDetailModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_article_detail;
    }

    @Override
    protected void initIntent(Intent intent) {
        mArticleId = intent.getStringExtra(ArticleDetailFragment.ARTICLE_ID);
        mCommentId = intent.getStringExtra(ArticleDetailFragment.COMMENT_ID);
    }

    @Override
    protected void initView() {
        mFragment = ArticleDetailFragment.newInstance(mArticleId, mCommentId);

        addLayerFragment(ID_FRAME_LAYOUT_CONTAINER, mFragment);

        ivBack.setOnClickListener(this);
        llEnterReview.setOnClickListener(this);
        ivReview.setOnClickListener(this);
        tvReviewCount.setOnClickListener(this);
        ivMenu.setOnClickListener(this);
        ivStar.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_back) {
            finish();
            return;
        }

        if (!mFragment.isLoadOver()) {
            Log.e(TAG, "文章信息未加载完。");
            return;
        }

        switch (v.getId()) {

            case R.id.ll_enter_review:
                if (TextUtils.isEmpty(mReceiverName)) {
                    Log.e(TAG, "文章信息未加载完。");
                    return;
                }

                int reviewPos = getReviewPos();
                if (reviewPos == -1) {
                    Log.e(TAG, "查找不到评论头");
                    return;
                }

                showDialog();
                mPresenter.getJudgement(mArticleId, reviewPos);
                break;

            case R.id.iv_review:
            case R.id.tv_review_count:
                scrollerToReview();
                break;

            case R.id.iv_menu:
                //TopArticleTipDialog.newInstance().show(this);
                if (TextUtils.isEmpty(mFragment.getShareLink())) {
                    Log.e(TAG, "文章信息未加载完。");
                    return;
                }

                if (mFragment.getAuthorId() == -1) {
                    Log.e(TAG, "文章信息未加载完。");
                    return;
                }

                showControlDialog();

                break;

            case R.id.iv_star:

                if (TextUtils.isEmpty(mArticleId)) {
                    Log.e(TAG, "文章信息未加载完。");
                    return;
                }

                if (mCollectStateLock) {
                    return;
                }

                mCollectStateLock = true;

                ivStar.setSelected(!mCollectState);

                mPresenter.collectArticle(mArticleId,
                        mCollectState ? Constant.CollectType.CANCEL : Constant.CollectType.COLLECT);

                break;
        }
    }

    /**
     * 滚动至评论
     */
    private void scrollerToReview() {
        mFragment.scrollToReview();
    }

    private int getReviewPos() {
        return mFragment.getReviewTopPos();
    }

    @Override
    public void onSetArticleInfo(String name, int count) {
        this.mReceiverName = name;
        this.mReviewCount = count;

        onSetCount(mReviewCount);
    }

    /**
     * 更新底部栏的评论数
     */
    @Override
    public void onSetCount(long count) {
        this.tvReviewCount.setText(TyUtils.formatNum(count));
    }

    @Override
    public void onModifyArticle() {
        mFragment.modifyArticle();
        finish();
    }

    @Override
    public void onDeleteArticle() {
        mFragment.deleteArticle();
    }

    @Override
    public void onPutHideArticle() {
        if (mStatus == Constant.MyArticleType.HIDE){
            mPresenter.putHideArticle(mArticleId, SHOW_ARTICLE);
        }else {
            mPresenter.putHideArticle(mArticleId, HIDE_ARTICLE);
        }

        hideControlDialog();
    }

    public void onSetTitle(String title, float alpha) {
        tvTitle.setText(title);
        if (alpha > 1) {
            alpha = 1;
        }
        tvTitle.setAlpha(alpha);
    }

    @Override
    public void onSetCollectState(boolean state) {
        mCollectState = state;
        ivStar.setSelected(mCollectState);
    }

    @Override
    public void onHideControlBar() {
        ViewGroup.LayoutParams layoutParams = llControlBar.getLayoutParams();
        layoutParams.height = 0;
        llControlBar.setLayoutParams(layoutParams);

        ViewGroup.LayoutParams dividerLayoutParams = vDivider.getLayoutParams();
        dividerLayoutParams.height = 0;
        vDivider.setLayoutParams(dividerLayoutParams);
    }

    @Override
    public void onSetPermission(PermissionResp value) {
        this.mPermissionInfo = value;
    }

    @Override
    public void onGetIsHideArticle(int status) {
        mStatus = status;
        Log.d("onGetIsHideArticle", String.valueOf(mStatus));
    }

    @Override
    public void onGetJudgement(JudgementResp value, int reviewPos) {

        if (value.isIsSatisfy()) {
            ReviewActivity.startActivity(
                    this,
                    ArticleDetailFragment.TAG,
                    mReceiverName, mArticleId,
                    ReviewActivity.EMPTY,
                    ReviewActivity.EMPTY_STRING,
                    ReviewActivity.EMPTY_STRING,
                    reviewPos
            );
        } else {
            RankTipDialog
                    .newInstance(value.getLimitScore(), Constant.JudgementType.COMMENT)
                    .show(this);
        }

    }

    @Override
    public void onCollectError() {

        ivStar.setSelected(mCollectState);
        mCollectStateLock = false;

    }

    @Override
    public void onCollect() {

        // 变更状态
        mCollectState = !mCollectState;

        int resultContent = mCollectState ?
                R.string.article_collect_success : R.string.article_cancel_collect_success;
        ToastUtils.show(getString(resultContent));

        // 取消成功需要进行通知
        if (!mCollectState) {
            EventBus.getDefault().post(new ArticleCollectEvent(mArticleId));
        }

        mCollectStateLock = false;
    }

    @Override
    public void onGetArticleCodeFail() {
        getCodeDialogFragment().loadOver();
    }

    @Override
    public void onGetArticleCode(String captchaId,
                                 String captchaBase64) {
        mCaptchaId = captchaId;
        getCodeDialogFragment().setCodeInfo(captchaBase64);
    }

    @Override
    public void onPostArticleCaptcha() {
        showArticle();
        mFragment.reload();
        getCodeDialogFragment().dismiss();
    }

    @Override
    public void onPutArticleReviewError() {
        hideDialog();
    }

    public int getStatus(){ return  mStatus; }
    @Override
    public void onPutArticleReview() {
        hideDialog();
        ExaminationArticleDialog dialog = getExaminationArticleDialog();
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
        EventBus.getDefault().post(new ExamOverEvent(mArticleId));
        finish();
    }

    @Override
    public void onPutArticleHideSuccess(int state) {
        mStatus = state;
        if (state == SHOW_ARTICLE){
            EventBus.getDefault().post(new HideArticleEvent(mArticleId, SHOW_ARTICLE));
            ToastUtils.show(getString(R.string.article_detail_show_article));
        }else if (state == HIDE_ARTICLE){
            EventBus.getDefault().post(new HideArticleEvent(mArticleId, HIDE_ARTICLE));
            ToastUtils.show(getString(R.string.article_detail_hide_article));
        }

    }

    @Override
    public void onPutTopArticleSuccess(int position) {
        if (position == 0){
            ToastUtils.show(getString(R.string.top_article_cancel));
            return;
        }
        ToastUtils.show(getString(R.string.top_article_success));
        EventBus.getDefault().post(new TopArticlePositionEvent(position));
    }

    public void showArticle() {
        setStatusBarColor(R.color.tyStateColor);
        controlArticleBtn(true);
        controlCode(false);
    }

    public void showCode() {
        setStatusBarColor(R.color.rv_unselect, false, false);
        controlArticleBtn(false);
        controlCode(true);

        getCodeDialogFragment().show(this);

        mPresenter.getArticleCode(CodeDialog.CODE_WIDTH, CodeDialog.CODE_HEIGHT);
    }

    /**
     * 文章的控制按钮是否要显示
     *
     * @param isShow true 显示；false 不显示
     */
    private void controlArticleBtn(boolean isShow) {
        ivBack.setVisibility(isShow ? View.VISIBLE : View.GONE);
        tvTitle.setVisibility(isShow ? View.VISIBLE : View.GONE);
        llControlBar.setVisibility(isShow ? View.VISIBLE : View.GONE);
        vDivider.setVisibility(isShow ? View.VISIBLE : View.GONE);
        frameLayoutContainer.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    /**
     * 文章的邮箱验证控件是否要显示
     *
     * @param isShow true 显示；false 不显示
     */
    private void controlCode(boolean isShow) {
        ivCodeBg.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    /**
     * 获取验证码
     */
    public void loadCode() {
        mPresenter.getArticleCode(UIUtils.dip2px(this, CodeDialog.CODE_WIDTH),
                UIUtils.dip2px(this, CodeDialog.CODE_HEIGHT));
    }

    /**
     * 验证验证码
     */
    public void validateCode(String code) {
        mPresenter.postArticleCaptcha(mCaptchaId, code);
    }

    private CodeDialog getCodeDialogFragment() {
        if (mCodeDialog == null) {
            mCodeDialog = CodeDialog.newInstance();
            mCodeDialog.setCancelable(false);
        }

        return mCodeDialog;
    }

    private ExaminationArticleDialog mExaminationArticleDialog;

    private ExaminationArticleDialog getExaminationArticleDialog() {
        if (mExaminationArticleDialog == null) {
            mExaminationArticleDialog = ExaminationArticleDialog.newInstance();
        }
        return mExaminationArticleDialog;
    }

    public void showExamination() {
        ExaminationArticleDialog dialog = getExaminationArticleDialog();
        if (!dialog.isShowing()) {
            dialog.show(this);
        }
    }

    public void commitExamination(boolean isPass, String reason) {
        showDialog();

        mPresenter.putArticleReview(mArticleId, isPass, reason);
    }

    private ControlArticleDialog getControlDialog() {
        if (mControlArticleDialog == null) {
            mControlArticleDialog = ControlArticleDialog
                    .newInstance(mFragment.getShareLink(),
                            mFragment.getAccount(),
                            mArticleId,
                            mFragment.getArticleTitle());

            // 设置编辑权限
            mPermissionInfo.setCanEdit(TyUtils.isCanControl(mFragment.getAuthorId()));

            mControlArticleDialog.setControlInfo(mPermissionInfo);
        }
        //设置文章隐藏状态
        mControlArticleDialog.setStatus(mStatus);
        mControlArticleDialog.setTopPosition(mFragment.getTopPosition());


        return mControlArticleDialog;
    }

    private void showControlDialog() {
        if (getControlDialog().isShowing()) {
            return;
        }
        getControlDialog().show(this);
    }

    private void hideControlDialog() {
        if (getControlDialog().isShowing()) {
            getControlDialog().dismiss();
        }
    }

    @Override
    public void onPutTopArticle(int position) {
        mPresenter.putTopArticlePosition(mArticleId, position);
    }
}
