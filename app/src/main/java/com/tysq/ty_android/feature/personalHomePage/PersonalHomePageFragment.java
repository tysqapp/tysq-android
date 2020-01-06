package com.tysq.ty_android.feature.personalHomePage;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bit.view.fragment.BitBaseFragment;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.flyco.tablayout.SlidingTabLayout;
import com.jerry.image_watcher.ImageWatcherActivity;
import com.jerry.image_watcher.model.WatchImageVO;
import com.tysq.ty_android.R;
import com.tysq.ty_android.app.TyApplication;
import com.tysq.ty_android.config.Constant;
import com.tysq.ty_android.feature.articleCollect.ArticleCollectFragment;
import com.tysq.ty_android.feature.forbidReview.ForbidReviewActivity;
import com.tysq.ty_android.feature.myArticle.MyArticleFragment;
import com.tysq.ty_android.feature.myAttention.MyAttentionFragment;
import com.tysq.ty_android.feature.myFans.MyFansFragment;
import com.tysq.ty_android.feature.myReview.MyReviewFragment;
import com.tysq.ty_android.feature.person.PersonInfoActivity;
import com.tysq.ty_android.feature.personalHomePage.di.DaggerPersonalHomePageComponent;
import com.tysq.ty_android.feature.personalHomePage.di.PersonalHomePageModule;
import com.tysq.ty_android.local.sp.UserCache;
import com.tysq.ty_android.utils.TyUtils;
import com.zinc.lib_jerry_editor.span.typeface.JerryBoldSpan;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import eventbus.AttentionEvent;
import response.personal.PersonalPageResp;

/**
 * author       : frog
 * time         : 2019-09-17 12:25
 * desc         : 个人主页
 * version      : 1.0.0
 */

public final class PersonalHomePageFragment
        extends BitBaseFragment<PersonalHomePagePresenter>
        implements PersonalHomePageView, View.OnClickListener {

    private static final String USER_ID = "user_id";

    @BindView(R.id.iv_menu)
    ImageView ivMenu;
    @BindView(R.id.rl_focus_someone)
    RelativeLayout rlFocusSomeOne;
    @BindView(R.id.iv_add)
    ImageView ivAdd;
    @BindView(R.id.tv_focus_someone)
    TextView tvFocusSomeone;
    @BindView(R.id.iv_lv)
    ImageView ivLv;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.iv_user_photo)
    ImageView ivUserPhoto;
    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.tv_user_career)
    TextView tvUserCareer;
    @BindView(R.id.divider_1)
    View divider1;
    @BindView(R.id.tv_user_trade)
    TextView tvUserTrade;
    @BindView(R.id.divider_2)
    View divider2;
    @BindView(R.id.tv_user_address)
    TextView tvUserAddress;
    @BindView(R.id.tv_user_des)
    TextView tvUserDes;
    @BindView(R.id.tv_read_quantity)
    TextView tvReadQuantity;
    @BindView(R.id.tv_collection)
    TextView tvCollection;
    @BindView(R.id.tab_layout)
    SlidingTabLayout tabLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;

    private int userId;

    private PopupWindow mMenuWindow;
    private String name;
    private boolean isFollow;

    private final List<BitBaseFragment> mFragmentList = new ArrayList<>();
    private final String[] mTabTitle = new String[5];
    private final WatchImageVO mJerryImageModel = new WatchImageVO();

    public static PersonalHomePageFragment newInstance(int userId) {
        Bundle args = new Bundle();

        args.putInt(USER_ID, userId);

        PersonalHomePageFragment fragment = new PersonalHomePageFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    protected void initArgs(Bundle arguments) {
        super.initArgs(arguments);
        userId = arguments.getInt(USER_ID);
    }

    @Override
    protected View onCreateFragmentView(LayoutInflater inflater,
                                        ViewGroup container,
                                        Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_personal_home_page, container, false);
    }

    @Override
    protected void initView(View view) {

        initWindow();

        mFragmentList.add(MyArticleFragment.newInstance(userId,
                false,
                false,
                false,
                Constant.MyArticleType.PUBLISH));
        mFragmentList.add(MyReviewFragment.newInstance(userId,
                false,
                false,
                false));
        mFragmentList.add(ArticleCollectFragment.newInstance(userId,
                false,
                false,
                false));
        mFragmentList.add(MyAttentionFragment.newInstance(userId,
                false,
                false,
                false));
        mFragmentList.add(MyFansFragment.newInstance(userId,
                false,
                false,
                false));

        setTitle("", "", "", "", "");

        ivBack.setOnClickListener(this);
        ivMenu.setOnClickListener(this);

        viewPager.setAdapter(new PagerAdapter(getChildFragmentManager(), mFragmentList));
        viewPager.setOffscreenPageLimit(mFragmentList.size());
        tabLayout.setViewPager(viewPager, mTabTitle);

        loadData();

        rlFocusSomeOne.setOnClickListener(this);
    }

    private void loadData() {
        mPresenter.getPersonalPage(userId);
    }

    @Override
    protected void registerDagger() {
        DaggerPersonalHomePageComponent.builder()
                .appComponent(TyApplication.getAppComponent())
                .personalHomePageModule(new PersonalHomePageModule(this))
                .build()
                .inject(this);
    }

    /**
     * 设置用户信息
     */
    private void initUserInfo(PersonalPageResp value) {

        RequestOptions requestOptions = RequestOptions
                .bitmapTransform(new CircleCrop())
                .error(R.drawable.placeholder_user_photo)
                .placeholder(R.drawable.placeholder_user_photo);

        TyUtils.getGlideRequest(
                this,
                getContext(),
                value.getAccountInfo().getHeadUrl(),
                requestOptions,
                ivUserPhoto
        );

        //放大图片
        ivUserPhoto.setOnClickListener(view -> {
                    mJerryImageModel.setBlurryUrl(value.getAccountInfo().getHeadUrl());

                    mJerryImageModel.setOriginalUrl(value.getAccountInfo().getHeadUrl());
                    ImageWatcherActivity.startActivity(getContext(), mJerryImageModel);
                }
        );

        tvUserName.setText(value.getAccountInfo().getAccount());

        judgeOrHideLine(value.getAccountInfo().getCareer(),
                value.getAccountInfo().getTrade(),
                value.getAccountInfo().getHomeAddress());

        judgeAndSetData(value.getAccountInfo().getCareer(), tvUserCareer);
        judgeAndSetData(value.getAccountInfo().getTrade(), tvUserTrade);
        judgeAndSetData(value.getAccountInfo().getHomeAddress(), tvUserAddress);

        if (value.getAccountInfo().getPersonalProfile().trim().length() <= 0
                || value.getAccountInfo().getPersonalProfile() == null) {

            tvUserDes.setVisibility(View.GONE);
        } else {
            tvUserDes.setText(value.getAccountInfo().getPersonalProfile());
        }

        setAchievementInfo(tvReadQuantity,
                TyUtils.formatNum(value.getAccountInfo().getReadedNum()),
                R.string.personal_achievement_read_quantity);

        setAchievementInfo(tvCollection,
                TyUtils.formatNum(value.getAccountInfo().getCollectedNum()),
                R.string.personal_achievement_collection);

        //用户是否有登录
        if (UserCache.getDefault() == null) {
            rlFocusSomeOne.setVisibility(View.GONE);
            ivMenu.setVisibility(View.GONE);
        } else {
            rlFocusSomeOne.setVisibility(View.VISIBLE);
            judgeIsModerator();

            if (userId == UserCache.getDefault().getAccountId()) {
                tvFocusSomeone.setText(R.string.personal_edit_profile);
                rlFocusSomeOne.setOnClickListener(view1 ->
                        PersonInfoActivity.startActivity(getContext(), PersonInfoActivity.INFO_LIST)
                );
            } else {
                setFocus(true, value.getAccountInfo().isFollow());
                isFollow = value.getAccountInfo().isFollow();
            }
        }


        Integer gradeResource = Constant.LV_MAP.get(value.getAsset().getGrade());
        if (gradeResource == null) {
            gradeResource = Constant.LV_MAP.get(Constant.DEFAULT_GRADE);
        }
        ivLv.setImageDrawable(ContextCompat
                .getDrawable(getContext(), gradeResource));

        setTitle(TyUtils.formatNum(value.getAsset().getArticleNum()),
                TyUtils.formatNum(value.getAsset().getCommentNum()),
                TyUtils.formatNum(value.getAsset().getCollectNum()),
                TyUtils.formatNum(value.getAsset().getAttentionNum()),
                TyUtils.formatNum(value.getAsset().getFanNum()));


        for (int i = 0; i < mTabTitle.length; i++) {
            tabLayout.getTitleView(i).setText(mTabTitle[i]);
        }
    }

    //判断是否为版主
    private void judgeIsModerator() {
        if (UserCache.getDefault().isModerator()) {
            ivMenu.setVisibility(View.VISIBLE);
        } else {
            ivMenu.setVisibility(View.GONE);
        }
    }

    /**
     * 判断隐藏分割线
     *
     * @param career  职业
     * @param trade   行业
     * @param address 地址
     */
    private void judgeOrHideLine(String career, String trade, String address) {
        if (!TextUtils.isEmpty(career) && !TextUtils.isEmpty(trade)) {
            divider1.setVisibility(View.VISIBLE);
        } else {
            divider1.setVisibility(View.GONE);
        }

        if (TextUtils.isEmpty(address) || (TextUtils.isEmpty(career) && TextUtils.isEmpty(trade))) {
            divider2.setVisibility(View.GONE);
        } else {
            divider2.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 判断是否需要隐藏textView
     *
     * @param content
     * @param textView
     */
    private void judgeAndSetData(String content, TextView textView) {
        if (TextUtils.isEmpty(content)) {
            textView.setVisibility(View.GONE);
        } else {
            textView.setText(content);
            textView.setVisibility(View.VISIBLE);
        }
    }


    private void setTitle(String t1,
                          String t2,
                          String t3,
                          String t4,
                          String t5) {
        mTabTitle[0] = formatTitleString(R.string.personal_article, t1);
        mTabTitle[1] = formatTitleString(R.string.personal_comment, t2);
        mTabTitle[2] = formatTitleString(R.string.personal_collection, t3);
        mTabTitle[3] = formatTitleString(R.string.personal_focus, t4);
        mTabTitle[4] = formatTitleString(R.string.personal_fans, t5);
    }

    private String formatTitleString(int resId, String content) {
        return String.format(getString(resId), content);
    }

    /**
     * 设置个人成就富文本
     */
    private void setAchievementInfo(TextView textView, String quantity, int stringRes) {

        String content = getString(stringRes);

        String resultContent = String.format(content, quantity);

        int length = quantity.length();

        SpannableStringBuilder builder = new SpannableStringBuilder(resultContent);
        builder.setSpan(new JerryBoldSpan(),
                0,
                length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        builder.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getContext(), R.color.main_text_color)),
                0,
                length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        builder.setSpan(new AbsoluteSizeSpan(16, true),
                0,
                length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        textView.setText(builder);

    }

    /**
     * 设置是否关注
     *
     * @param isShow  是否显示
     * @param isFocus 是否已关注
     */
    private void setFocus(boolean isShow, boolean isFocus) {
        rlFocusSomeOne.setVisibility(isShow ? View.VISIBLE : View.GONE);
        if (!isShow) {
            return;
        }

        if (!isFocus) {
            rlFocusSomeOne.setVisibility(View.VISIBLE);
            rlFocusSomeOne.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.shape_blue_border_1dp));
            tvFocusSomeone.setText(R.string.personal_focus_someone);
            ivAdd.setVisibility(View.VISIBLE);
            tvFocusSomeone.setTextColor(ContextCompat.getColor(getContext(), R.color.main_blue_color));
        } else {
            rlFocusSomeOne.setVisibility(View.VISIBLE);
            rlFocusSomeOne.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.shape_dark_gray_fill_4dp));
            tvFocusSomeone.setText(R.string.personal_focused);
            tvFocusSomeone.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) tvFocusSomeone.getLayoutParams();
            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            ivAdd.setVisibility(View.GONE);
        }
    }

    private void initWindow() {
        View window = getLayoutInflater().inflate(R.layout.window_personal_menu, null);
        window.findViewById(R.id.tv_forbid_comment).setOnClickListener(this);
        mMenuWindow = new PopupWindow(window,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true);

        mMenuWindow.setOutsideTouchable(true);
        mMenuWindow.setBackgroundDrawable(
                ContextCompat.getDrawable(getContext(), R.drawable.ic_one_inch_alpha)
        );
    }

    private void showMenuWindow() {
        mMenuWindow.showAsDropDown(ivMenu, 0, 0, Gravity.START);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_forbid_comment:
                ForbidReviewActivity.startActivity(getContext(), name, userId);
                mMenuWindow.dismiss();
                break;
            case R.id.iv_menu:
                showMenuWindow();
                break;
            case R.id.iv_back:
                getActivity().finish();
                break;
            case R.id.rl_focus_someone:
                isFollow = !isFollow;
                setFocus(true, isFollow);
                mPresenter.postAttention(userId, isFollow);
                //通知关注状态变化
                EventBus.getDefault().post(new AttentionEvent(userId, isFollow));
                break;
        }
    }

    @Override
    public void getPersonalPage(PersonalPageResp value) {
        name = value.getAccountInfo().getAccount();
        initUserInfo(value);
    }

    @Override
    public void postAttention() {
    }

    @Override
    public void onResume() {
        super.onResume();

        if (UserCache.getDefault() == null) {
            return;
        }

        if (userId != UserCache.getDefault().getAccountId()) {
            mPresenter.getPersonalPage(userId);
            return;
        }

        RequestOptions requestOptions = RequestOptions
                .bitmapTransform(new CircleCrop())
                .error(R.drawable.placeholder_user_photo)
                .placeholder(R.drawable.placeholder_user_photo);

        TyUtils.getGlideRequest(
                this,
                getContext(),
                UserCache.getDefault().getHeadUrl(),
                requestOptions,
                ivUserPhoto);

        tvUserName.setText(UserCache.getDefault().getAccount());

        judgeOrHideLine(UserCache.getDefault().getCareer(),
                UserCache.getDefault().getTrade(),
                UserCache.getDefault().getHomeAddress());

        judgeAndSetData(UserCache.getDefault().getCareer(), tvUserCareer);
        judgeAndSetData(UserCache.getDefault().getTrade(), tvUserTrade);
        judgeAndSetData(UserCache.getDefault().getHomeAddress(), tvUserAddress);

        if (UserCache.getDefault().getPersonalProfile().trim().length() <= 0
                || UserCache.getDefault().getPersonalProfile() == null) {

            tvUserDes.setVisibility(View.GONE);
        } else {
            tvUserDes.setText(UserCache.getDefault().getPersonalProfile());
        }

    }

    /**
     * 页面适配
     */
    static class PagerAdapter extends FragmentPagerAdapter {

        private List<BitBaseFragment> mFragmentList;

        PagerAdapter(FragmentManager fm, List<BitBaseFragment> fragmentList) {
            super(fm);
            mFragmentList = fragmentList;
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

    }

}
