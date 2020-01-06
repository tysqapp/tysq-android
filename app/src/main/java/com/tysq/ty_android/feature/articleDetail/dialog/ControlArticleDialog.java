package com.tysq.ty_android.feature.articleDetail.dialog;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.abc.lib_utils.toast.ToastUtils;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bit.view.fragment.dialog.BitBaseDialogFragment;
import com.tysq.ty_android.R;
import com.tysq.ty_android.base.CommonBaseDialog;
import com.tysq.ty_android.config.Constant;
import com.tysq.ty_android.feature.articleDetail.activity.ArticleDetailActivity;
import com.tysq.ty_android.feature.articleDetail.activity.ArticleDetailPresenter;
import com.tysq.ty_android.feature.articleDetail.tip.TopArticleTipDialog;
import com.tysq.ty_android.feature.articleReport.ArticleReportActivity;
import com.tysq.ty_android.local.sp.UserCache;
import com.tysq.ty_android.utils.TyUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import eventbus.HideArticleEvent;
import response.permission.PermissionResp;

import static android.content.Context.CLIPBOARD_SERVICE;

/**
 * author       : frog
 * time         : 2019/5/29 上午10:55
 * desc         : 文章控制框
 * version      : 1.3.0
 */
public class ControlArticleDialog
        extends CommonBaseDialog
        implements View.OnClickListener {

    private static final String URL = "URL";
    private static final String ACCOUNT_NAME = "ACCOUNT_NAME";
    private static final String ARTICLE_ID = "ARTICLE_ID";
    private static final String STATUS_NAME = "STATUS_NAME";
    private static final String ARTICLE_TITLE = "ARTICLE_TITLE";

    @BindView(R.id.ll_edit)
    LinearLayout llEdit;
    @BindView(R.id.ll_delete)
    LinearLayout llDelete;
    @BindView(R.id.ll_copy)
    LinearLayout llCopy;
    @BindView(R.id.ll_examination)
    LinearLayout llExamination;
    @BindView(R.id.ll_report)
    LinearLayout llReport;
    @BindView(R.id.ll_hide_article)
    LinearLayout llHideArticle;
    @BindView(R.id.iv_hide_article)
    ImageView ivHideArticle;
    @BindView(R.id.tv_hide_article)
    TextView tvHideArticle;
    @BindView(R.id.ll_top_article)
    LinearLayout llTopArticle;
    @BindView(R.id.iv_top_article)
    ImageView ivTopArticle;
    @BindView(R.id.tv_top_article)
    TextView tvTopArticle;

    @BindView(R.id.iv_close)
    ImageView ivClose;

    private ClipboardManager mClipboard;

    private String mUrl;
    private String mAccountName;
    private String mArticleId;
    private int mStatus;
    private String mTitle;
    private int status;
    private int mTopPosition;

    private PermissionResp mPermissionInfo;

    public static ControlArticleDialog newInstance(String url,
                                                   String accountName,
                                                   String articleId,
                                                   String title) {

        Bundle args = new Bundle();
        args.putString(URL, url);
        args.putString(ACCOUNT_NAME, accountName);
        args.putString(ARTICLE_ID, articleId);
        args.putString(ARTICLE_TITLE, title);

        ControlArticleDialog fragment = new ControlArticleDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initArgs(Bundle arguments) {
        mUrl = arguments.getString(URL);
        mAccountName = arguments.getString(ACCOUNT_NAME);
        mArticleId = arguments.getString(ARTICLE_ID);
        mTitle = arguments.getString(ARTICLE_TITLE);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.dialog_article_detail_control;
    }

    @Override
    protected int obtainWidth() {
        return ViewGroup.LayoutParams.MATCH_PARENT;
    }

    @Override
    protected int obtainHeight() {
        return ViewGroup.LayoutParams.WRAP_CONTENT;
    }

    @Override
    protected int obtainGravity() {
        return Gravity.BOTTOM;
    }

    @Override
    protected void initView(View view) {
        mClipboard = (ClipboardManager) getContext().getSystemService(CLIPBOARD_SERVICE);

        llCopy.setOnClickListener(this);
        ivClose.setOnClickListener(this);
        llExamination.setOnClickListener(this);
        llEdit.setOnClickListener(this);
        llDelete.setOnClickListener(this);
        llReport.setOnClickListener(this);
        llHideArticle.setOnClickListener(this);
        llTopArticle.setOnClickListener(this);

        llExamination.setVisibility(mPermissionInfo.isCanReview() ? View.VISIBLE : View.GONE);
        llEdit.setVisibility(mPermissionInfo.isCanEdit() ? View.VISIBLE : View.GONE);
        llDelete.setVisibility(mPermissionInfo.isCanDeleteArticle() ? View.VISIBLE : View.GONE);

        if (mPermissionInfo.getCanHideArticle() == 0){
            llHideArticle.setVisibility(View.GONE);
        }else {
            llHideArticle.setVisibility(View.VISIBLE);
        }

        if (status == Constant.MyArticleType.HIDE){
            ivHideArticle.setImageResource(R.drawable.ic_show);
            tvHideArticle.setText(R.string.edit_show_article);
        }else{
            ivHideArticle.setImageResource(R.drawable.ic_hide);
            tvHideArticle.setText(R.string.edit_hide_article);
        }

        llTopArticle.setVisibility(mPermissionInfo.isCanSetArticleTop() ? View.VISIBLE : View.GONE);


    }

    @Override
    public void onClick(View v) {
        if (getActivity() == null) {
            return;
        }

        if (!(getActivity() instanceof ControlListener)) {
            return;
        }

        ControlListener listener = (ControlListener) getActivity();

        switch (v.getId()) {
            case R.id.ll_edit:
                listener.onModifyArticle();
                break;
            case R.id.ll_delete:
                listener.onDeleteArticle();
                break;
            case R.id.ll_copy:
                ClipData clipData = ClipData.newPlainText("url", mUrl);
                mClipboard.setPrimaryClip(clipData);
                ToastUtils.show(getString(R.string.edit_copy_suc));
                break;
            case R.id.iv_close:
                break;
            case R.id.ll_report:
                ArticleReportActivity.startActivity(getContext(),mTitle,mArticleId);
                break;
            case R.id.ll_examination:
                FragmentActivity activity = getActivity();
                if (activity != null && (activity instanceof ArticleDetailActivity)) {
                    ArticleDetailActivity articleDetailActivity = (ArticleDetailActivity) activity;
                    articleDetailActivity.showExamination();
                }
                break;
            case R.id.ll_hide_article:
                if (status == Constant.MyArticleType.HIDE){
                    new MaterialDialog
                            .Builder(getContext())
                            .title(getString(R.string.edit_cancel_hide_article))
                            .cancelable(false)
                            .positiveText(getString(R.string.common_confirm))
                            .negativeText(getString(R.string.setting_quit_cancel))
                            .onPositive((dialog, which) -> {
                                listener.onPutHideArticle();
                            }).show();

                }else{
                    new MaterialDialog
                            .Builder(getContext())
                            .title(getString(R.string.edit_hide_article_title_dialog))
                            .content(getString(R.string.edit_hide_article_contetn_dialog))
                            .cancelable(false)
                            .negativeText(getString(R.string.setting_quit_cancel))
                            .positiveText(getString(R.string.common_confirm))
                            .onPositive(((dialog, which) -> {
                                listener.onPutHideArticle();
                            }))
                            .show();
                }
                break;

            case R.id.ll_top_article:
                TopArticleTipDialog.newInstance(mTopPosition).show(getActivity());
                break;

        }

        dismiss();
    }

    /**
     * 设置文章隐藏状态
     *  @param status 状态
     */
    public void setStatus(int status){
        this.status = status;
    }

    public void setControlInfo(PermissionResp info) {
        mPermissionInfo = info;
    }

    public void setTopPosition(int position){ this.mTopPosition = position; }

    public interface ControlListener {
        void onModifyArticle();

        void onDeleteArticle();

        void onPutHideArticle();
    }

}
