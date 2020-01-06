package com.tysq.ty_android.feature.notificationSetting;

import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bit.view.fragment.BitBaseFragment;
import com.tysq.ty_android.R;
import com.tysq.ty_android.app.TyApplication;
import com.tysq.ty_android.base.activity.CommonToolbarStrengthenActivity;
import com.tysq.ty_android.feature.notificationSetting.di.DaggerNotificationSettingComponent;
import com.tysq.ty_android.feature.notificationSetting.di.NotificationSettingModule;
import com.tysq.ty_android.local.sp.UserCache;

import java.lang.Override;

import butterknife.BindView;
import request.NotificationConfigReq;
import response.notification.NotificationConfigResp;

/**
 * author       : liaozhenlin
 * time         : 2019/9/9 17:37
 * desc         : 消息设置
 * version      : 1.0.0
 */
public final class NotificationSettingFragment
        extends BitBaseFragment<NotificationSettingPresenter>
        implements NotificationSettingView, CommonToolbarStrengthenActivity.ICommonFragment {

    public static final String TAG = "NotificationSettingFragment";

    @BindView(R.id.tv_my_article)
    TextView tvMyArticle;
    @BindView(R.id.tv_new_articles)
    TextView tvNewArticles;
    @BindView(R.id.tv_new_report)
    TextView tvNewReport;
    @BindView(R.id.tv_new_comment)
    TextView tvNewComment;
    @BindView(R.id.tv_new_reply)
    TextView tvNewReply;
    @BindView(R.id.ll_my_article_sys)
    LinearLayout llMyArticleSys;
    @BindView(R.id.sc_my_article_sys)
    SwitchCompat scMyArticleSys;
    @BindView(R.id.ll_my_article_email)
    LinearLayout llMyArticleEmail;
    @BindView(R.id.sc_my_article_email)
    SwitchCompat scMyArticleEmail;
    @BindView(R.id.ll_new_article_sys)
    LinearLayout llNewArticleSys;
    @BindView(R.id.sc_new_article_sys)
    SwitchCompat scNewArticleSys;
    @BindView(R.id.ll_new_article_email)
    LinearLayout llNewArticleEmail;
    @BindView(R.id.sc_new_article_email)
    SwitchCompat scNewArticleEmail;
    @BindView(R.id.ll_new_comment_sys)
    LinearLayout llNewCommentSys;
    @BindView(R.id.sc_new_comment_sys)
    SwitchCompat scNewCommentSys;
    @BindView(R.id.ll_new_reply_sys)
    LinearLayout llNewReplySys;
    @BindView(R.id.sc_new_reply_sys)
    SwitchCompat scNewReplySys;
    @BindView(R.id.ll_new_report_sys)
    LinearLayout llNewReportSys;
    @BindView(R.id.sc_new_report_sys)
    SwitchCompat scNewReportSys;
    @BindView(R.id.ll_new_report_email)
    LinearLayout llNewReportEmail;
    @BindView(R.id.sc_new_report_email)
    SwitchCompat scNewReportEmail;
    @BindView(R.id.ll_my_report_sys)
    LinearLayout llMyReportSys;
    @BindView(R.id.sc_my_report_sys)
    SwitchCompat scMyReportSys;

    private NotificationConfigReq notificationConfigReq ;

    public static NotificationSettingFragment newInstance(){

        Bundle args = new Bundle();

        NotificationSettingFragment fragment = new NotificationSettingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected View onCreateFragmentView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
      return inflater.inflate(R.layout.fragment_notification_setting, container, false);
    }

    @Override
    protected void initView(View view) {
        judgeIsModerator();
        mPresenter.getNotificationConfig();
    }

    @Override
    protected void registerDagger() {
        DaggerNotificationSettingComponent.builder()
        .appComponent(TyApplication.getAppComponent())
        .notificationSettingModule(new NotificationSettingModule(this))
        .build()
        .inject(this);
    }

    @Override
    public void getNotificationConfig(NotificationConfigResp configResp) {
        scMyArticleSys.setChecked(configResp.isArticleReviewedSystem());
        scMyArticleEmail.setChecked(configResp.isArticleReviewedEmail());
        scNewArticleSys.setChecked(configResp.isArticleReviewSystem());
        scNewArticleEmail.setChecked(configResp.isArticleReviewEmail());
        scNewCommentSys.setChecked(configResp.isNewCommentSystem());
        scNewReplySys.setChecked(configResp.isNewReplySystem());
        scNewReportSys.setChecked(configResp.isReportHandlerSystem());
        scNewReportEmail.setChecked(configResp.isReportHandlerEmail());
        scMyReportSys.setChecked(configResp.isReportHandledSystem());
    }

    @Override
    public void putNotificationConfigError() {
        hideDialog();
    }


    @Override
    public void putNotificationConfig() { }

    private void queryNotificationConfig(){
        notificationConfigReq = new NotificationConfigReq();
        notificationConfigReq.setArticleReviewedSystem(scMyArticleSys.isChecked());
        notificationConfigReq.setArticleReviewedEmail(scMyArticleEmail.isChecked());
        notificationConfigReq.setArticleReviewSystem(scNewArticleSys.isChecked());
        notificationConfigReq.setArticleReviewEmail(scNewArticleEmail.isChecked());
        notificationConfigReq.setNewCommentSystem(scNewCommentSys.isChecked());
        notificationConfigReq.setNewReplySystem(scNewReplySys.isChecked());
        notificationConfigReq.setReportHandlerSystem(scNewReportSys.isChecked());
        notificationConfigReq.setReportHandlerEmail(scNewReportEmail.isChecked());
        notificationConfigReq.setReportHandledSystem(scMyReportSys.isChecked());
    }

    @Override
    public int getTitleId() {
        return R.string.notification_setting;
    }

    @Override
    public int getConfirmId() {
        return R.string.common_confirm;
    }

    @Override
    public void setConfirmClick() {
        queryNotificationConfig();
        mPresenter.putNotificationConfig(notificationConfigReq);
        getActivity().finish();
    }

    @Override
    public int getConfirmBackground() {
        return R.drawable.selector_btn_round_blue_bg;
    }

    @Override
    public int getConfirmTextColor() {
        return R.color.white;
    }

    private void judgeIsModerator(){
        if (UserCache.getDefault().isModerator()){
            tvNewArticles.setVisibility(View.VISIBLE);
            tvNewReport.setVisibility(View.VISIBLE);
            llNewArticleSys.setVisibility(View.VISIBLE);
            llNewArticleEmail.setVisibility(View.VISIBLE);
            llNewReportEmail.setVisibility(View.VISIBLE);
            llNewReportSys.setVisibility(View.VISIBLE);
        }else {
            tvNewArticles.setVisibility(View.GONE);
            tvNewReport.setVisibility(View.GONE);
            llNewArticleSys.setVisibility(View.GONE);
            llNewArticleEmail.setVisibility(View.GONE);
            llNewReportEmail.setVisibility(View.GONE);
            llNewReportSys.setVisibility(View.GONE);
        }
    }

}
