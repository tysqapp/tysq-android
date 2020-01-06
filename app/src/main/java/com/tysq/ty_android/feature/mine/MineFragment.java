package com.tysq.ty_android.feature.mine;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bit.utils.UIUtils;
import com.bit.view.fragment.BitLazyFragment;
import com.bumptech.glide.Glide;
import com.tysq.ty_android.R;
import com.tysq.ty_android.app.TyApplication;
import com.tysq.ty_android.base.activity.CommonToolbarActivity;
import com.tysq.ty_android.base.activity.CommonToolbarStrengthenActivity;
import com.tysq.ty_android.config.Constant;
import com.tysq.ty_android.feature.aboutUs.AboutUsFragment;
import com.tysq.ty_android.feature.adminCenter.AdminCenterFragment;
import com.tysq.ty_android.feature.articleCollect.ArticleCollectFragment;
import com.tysq.ty_android.feature.cloud.CloudActivity;
import com.tysq.ty_android.feature.coin.myCoin.MyCoinActivity;
import com.tysq.ty_android.feature.dataSourceSetting.DataSourceSettingActivity;
import com.tysq.ty_android.feature.invite.InviteActivity;
import com.tysq.ty_android.feature.login.LoginActivity;
import com.tysq.ty_android.feature.mine.di.DaggerMineComponent;
import com.tysq.ty_android.feature.mine.di.MineModule;
import com.tysq.ty_android.feature.myArticle.MyArticleFragment;
import com.tysq.ty_android.feature.myAttention.MyAttentionFragment;
import com.tysq.ty_android.feature.myFans.MyFansFragment;
import com.tysq.ty_android.feature.myReview.MyReviewFragment;
import com.tysq.ty_android.feature.notification.NotificationActivity;
import com.tysq.ty_android.feature.personalHomePage.PersonalHomePageActivity;
import com.tysq.ty_android.feature.rank.myRank.MyRankFragment;
import com.tysq.ty_android.feature.setting.SettingActivity;
import com.tysq.ty_android.feature.web.TyWebViewActivity;
import com.tysq.ty_android.local.sp.NetCache;
import com.tysq.ty_android.local.sp.UserCache;
import com.tysq.ty_android.login.CheckLogin;
import com.tysq.ty_android.utils.TyUtils;
import com.tysq.ty_android.widget.SettingItemView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import eventbus.LoginStateChangeEvent;
import eventbus.NotificationUpdateEvent;
import eventbus.UserInfoUpdateEvent;
import response.UserInfoResp;

/**
 * author       : frog
 * time         : 2019-07-16 11:33
 * desc         : 我的界面
 * version      : 1.3.0
 */
public final class MineFragment extends BitLazyFragment<MinePresenter>
        implements MineView, View.OnClickListener {

    @BindView(R.id.scrollView)
    ScrollView scrollView;
    @BindView(R.id.iv_photo)
    ImageView ivPhoto;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.iv_user_info)
    ImageView ivUserInfo;

    @BindView(R.id.tv_article)
    TextView tvArticle;
    @BindView(R.id.tv_review)
    TextView tvReview;
    @BindView(R.id.tv_collect)
    TextView tvCollect;

    @BindView(R.id.siv_admin)
    SettingItemView sivAdmin;
    @BindView(R.id.v_divider_admin)
    View vDividerAdmin;
    @BindView(R.id.siv_cloud)
    SettingItemView sivCloud;
    @BindView(R.id.siv_follow)
    SettingItemView sivFollow;
    @BindView(R.id.siv_fans)
    SettingItemView sivFans;
    @BindView(R.id.siv_setting)
    SettingItemView sivSetting;
    @BindView(R.id.siv_data_source)
    SettingItemView sivDataSource;
    @BindView(R.id.siv_about)
    SettingItemView sivAbout;
    @BindView(R.id.siv_invite)
    SettingItemView sivInvite;

    @BindView(R.id.rl_rank)
    RelativeLayout rlRank;
    @BindView(R.id.rl_coin)
    RelativeLayout rlCoin;
    @BindView(R.id.tv_rank)
    TextView tvRank;
    @BindView(R.id.tv_coin)
    TextView tvCoin;

    @BindView(R.id.iv_lv)
    ImageView ivLv;

    @BindView(R.id.ll_notification)
    LinearLayout llNotification;
    @BindView(R.id.tv_notification)
    TextView tvNotification;
    @BindView(R.id.iv_notification)
    ImageView ivNotification;

    @BindView(R.id.tv_coin_format)
    TextView tvCoinFormat;

    private int lvMarginLeft;
    private int lvMarginTop;


    public static MineFragment newInstance() {

        Bundle args = new Bundle();

        MineFragment fragment = new MineFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected View onCreateFragmentView(LayoutInflater inflater, ViewGroup container,
                                        Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mine, container, false);
    }

    @Override
    protected void initView(View view) {

        //StatusUtils.fitTitleBar(getActivity(), scrollView);
        tvArticle.setOnClickListener(this);
        tvReview.setOnClickListener(this);
        tvCollect.setOnClickListener(this);

        llNotification.setOnClickListener(this);
        sivInvite.setOnClickListener(this);
        sivAdmin.setOnClickListener(this);
        sivCloud.setOnClickListener(this);

        sivFollow.setOnClickListener(this);
        sivFans.setOnClickListener(this);

        sivSetting.setOnClickListener(this);
        sivDataSource.setOnClickListener(this);
        sivAbout.setOnClickListener(this);

        ivUserInfo.setOnClickListener(this);
        rlRank.setOnClickListener(this);
        rlCoin.setOnClickListener(this);
        ivLv.setOnClickListener(this);

        initUserInfo();
//        mPresenter.loadConfig();
        mPresenter.getNotifyUnReadCount();

        lvMarginLeft = UIUtils.dip2px(getContext(), 96);
        lvMarginTop = UIUtils.dip2px(getContext(), (152 - 21) / 2);

        sivDataSource.setTip(NetCache.getDefault().getDomain());

        setArticleInfoSpanInfo(tvArticle, R.string.mine_article, "0");
        setArticleInfoSpanInfo(tvReview, R.string.mine_review, "0");
        setArticleInfoSpanInfo(tvCollect, R.string.mine_collect, "0");

        sivInvite.setTip(getString(R.string.mine_invite_tip));

        setSpanInfo(tvRank, R.string.mine_rank, "0");
        setSpanInfo(tvCoin, R.string.mine_coin, "0");

    }

    private void initUserInfo() {
        if (UserCache.isEmpty()) {
            tvName.setText(getString(R.string.mine_login));
            calculateLvMargin();
            tvName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LoginActivity.startActivity(getContext(), "");
                }
            });

            Glide.with(getContext())
                    .load(R.drawable.placeholder_user_photo)
                    .into(ivPhoto);

            ivUserInfo.setVisibility(View.GONE);
            ivLv.setVisibility(View.GONE);

        } else {
            tvName.setText(UserCache.getDefault().getAccount());
            calculateLvMargin();
            tvName.setClickable(false);
            setHeadPhoto();

            ivUserInfo.setVisibility(View.VISIBLE);
            ivLv.setVisibility(View.VISIBLE);
        }

        isShowAdmin();

        loadMyInfo();

    }

    @Override
    protected void registerDagger() {
        DaggerMineComponent.builder()
                .appComponent(TyApplication.getAppComponent())
                .mineModule(new MineModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.siv_about:
                goToAbout();
                break;

            case R.id.siv_data_source:
                DataSourceSettingActivity.startActivity(getContext());
                break;

            case R.id.iv_lv:
                TyWebViewActivity.startActivity(getContext(),
                        Constant.HtmlAPI.GRAND);
                break;

            default:
                needLogin(v.getId());
        }
    }

    public void goToAbout() {
        CommonToolbarActivity.startActivity(getContext(), AboutUsFragment.TAG);
    }

    @CheckLogin
    private void needLogin(int id) {
        switch (id) {
            case R.id.siv_setting:
                SettingActivity.startActivity(getContext(), SettingActivity.SETTING);
                break;

            case R.id.iv_user_info:
//                PersonInfoActivity.startActivity(getContext(), PersonInfoActivity.INFO_LIST);
                PersonalHomePageActivity.startActivity(
                        getContext(), UserCache.getDefault().getAccountId());
                break;

            case R.id.siv_cloud:
                CloudActivity.startActivity(getContext());
                break;

            case R.id.tv_article:
                CommonToolbarActivity.startActivity(getContext(), MyArticleFragment.TAG);
                break;

            case R.id.tv_review:
                CommonToolbarActivity.startActivity(getContext(), MyReviewFragment.TAG);
                break;

            case R.id.tv_collect:
                CommonToolbarActivity.startActivity(getContext(), ArticleCollectFragment.TAG);
                break;

            case R.id.rl_rank:
                CommonToolbarStrengthenActivity.startActivity(getContext(), MyRankFragment.TAG);
                break;
            case R.id.rl_coin:
                //CommonToolbarStrengthenActivity.startActivity(getContext(), MyCoinFragment.TAG);
                MyCoinActivity.startActivity(getContext());
                break;
            case R.id.siv_invite:
//                CommonToolbarActivity.startActivity(getContext(), InviteFragment.TAG);
                InviteActivity.startActivity(getContext());
                break;
            case R.id.ll_notification:
                NotificationActivity.startActivity(getContext());
                //ReportDetailActivity.startActivity(getContext(), "5da58415f44ff7fd82033d89");
                break;
            case R.id.siv_admin:
                CommonToolbarActivity.startActivity(getContext(), AdminCenterFragment.TAG);
                break;

            case R.id.siv_follow:
                CommonToolbarStrengthenActivity.startActivity(getContext(), MyAttentionFragment.TAG);
                break;
            case R.id.siv_fans:
                CommonToolbarStrengthenActivity.startActivity(getContext(), MyFansFragment.TAG);
                break;
        }
    }

    @Override
    protected int getInitRegister() {
        return BUTTER_KNIFE | EVENT_BUS;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateUserInfo(UserInfoUpdateEvent userInfoUpdate) {
        initUserInfo();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginSt(LoginStateChangeEvent loginStateChangeEvent) {
        initUserInfo();
    }

    @Override
    protected boolean isLazyLoad() {
        return false;
    }

    @Override
    protected void onFragmentVisible() {
        initUserInfo();
    }

    private void loadMyInfo() {
        if (UserCache.isEmpty()) {
            setArticleInfoSpanInfo(tvArticle, R.string.mine_article, "0");
            setArticleInfoSpanInfo(tvReview, R.string.mine_review, "0");
            setArticleInfoSpanInfo(tvCollect, R.string.mine_collect, "0");

            setSpanInfo(tvRank, R.string.mine_rank, "0");
            setSpanInfo(tvCoin, R.string.mine_coin, "0");

        } else {
            mPresenter.loadInfo();
        }
    }

    /**
     * 设置头像
     */
    private void setHeadPhoto() {
        if (getContext() == null) {
            return;
        }

        TyUtils.initUserPhoto(this, getContext(), UserCache.getDefault().getHeadUrl(), ivPhoto);

    }

    @Override
    public void onResume() {
        super.onResume();
        initUserInfo();
        if (UserCache.getDefault() != null){
            tvCoinFormat.setVisibility(View.VISIBLE);

        }else {
            tvCoinFormat.setVisibility(View.GONE);
            sivFollow.setTip("");
            sivFans.setTip("");
        }
        //请求通知条数
        mPresenter.getNotifyUnReadCount();
    }

    @Override
    public void onGetUserInfo() {
        tvName.setText(UserCache.getDefault().getAccount());
        calculateLvMargin();
        setHeadPhoto();
    }

    @Override
    public void onUploadInfo(UserInfoResp.Asset asset) {

        setArticleInfoSpanInfo(tvArticle, R.string.mine_article, asset.getArticleNum());
        setArticleInfoSpanInfo(tvReview, R.string.mine_review, asset.getCommentNum());
        setArticleInfoSpanInfo(tvCollect, R.string.mine_collect, asset.getCollectNum());

        setSpanInfo(tvRank, R.string.mine_rank, TyUtils.formatDotNum(asset.getScore()));
        setSpanInfo(tvCoin, R.string.mine_coin, TyUtils.formatDotNum(asset.getHotCoin()));

        if (asset.getHotCoin() != 0){
            tvCoinFormat.setVisibility(View.VISIBLE);
            String format = String.format(getString(R.string.mine_coin_format), TyUtils.formatCoin(asset.getHotCoin()));
            tvCoinFormat.setText(format);
        } else {
            tvCoinFormat.setVisibility(View.GONE);
        }


        sivFans.setTip(asset.getFanNum());
        sivFollow.setTip(asset.getAttentionNum());

        Integer gradeResource = TyUtils.getLvIcon(asset.getGrade());
        ivLv.setImageDrawable(ContextCompat.getDrawable(getContext(), gradeResource));
    }

//    @Override
//    public void onLoadConfig(int recRegisterSuccess) {
//        sivInvite.setTip(String.format(getString(R.string.mine_invite_tip), recRegisterSuccess));
//    }

    @Override
    public void getNotifyUnCountRead(int count) {
        EventBus.getDefault().post(new NotificationUpdateEvent(count));
    }

    /**
     * 设置信息
     *
     * @param textView
     * @param sourceId
     * @param info
     */
    private void setSpanInfo(TextView textView,
                             int sourceId,
                             String info) {
        String content = String.format(getString(sourceId), info);

        AbsoluteSizeSpan absoluteSizeSpan = new AbsoluteSizeSpan(18, true);

        SpannableString spannableString = new SpannableString(content);
        spannableString.setSpan(absoluteSizeSpan,
                content.length() - info.length(),
                content.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        textView.setText(spannableString);
    }

    /**
     * 设置 "文章"、"评论"、"收藏"
     */
    private void setArticleInfoSpanInfo(TextView textView,
                                        int sourceId,
                                        String info) {
        String content = String.format(getString(sourceId), info);

        AbsoluteSizeSpan absoluteSizeSpan = new AbsoluteSizeSpan(20, true);

        SpannableString spannableString = new SpannableString(content);
        spannableString.setSpan(absoluteSizeSpan,
                0,
                info == null ? 0 : info.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        textView.setText(spannableString);
    }

    private void calculateLvMargin() {
        tvName.post(() -> {
            int width = tvName.getWidth();

            int marginLeft = lvMarginLeft + width;

            RelativeLayout.LayoutParams lvLp = new RelativeLayout.LayoutParams(ivLv.getLayoutParams());
            lvLp.setMargins(marginLeft, lvMarginTop, 0, 0);
            ivLv.setLayoutParams(lvLp);

        });
    }

    /**
     * 是否显示版主
     */
    private void isShowAdmin() {
        if (UserCache.isEmpty() || !UserCache.getDefault().isModerator()) {
            sivAdmin.setVisibility(View.GONE);
            vDividerAdmin.setVisibility(View.GONE);
        } else {
            sivAdmin.setVisibility(View.VISIBLE);
            vDividerAdmin.setVisibility(View.VISIBLE);
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void update(NotificationUpdateEvent event) {
        if (event.getCount() <= 0) {
            tvNotification.setVisibility(View.GONE);
            return;
        }
        tvNotification.setVisibility(View.VISIBLE);
        tvNotification.setText(String.valueOf(event.getCount()));
    }

}
