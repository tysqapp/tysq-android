package com.tysq.ty_android.feature.invite;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bit.utils.UIUtils;
import com.bit.view.activity.BitBaseActivity;
import com.bit.widget.NoScrollViewPager;
import com.tysq.ty_android.R;
import com.tysq.ty_android.config.Constant;
import com.tysq.ty_android.feature.web.TyWebViewFragment;
import com.tysq.ty_android.net.config.NetConfig;
import com.tysq.ty_android.utils.TyUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class InviteActivity extends BitBaseActivity implements View.OnClickListener {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_invite_friend)
    TextView tvInviteFriend;
    @BindView(R.id.v_divider_1)
    View divider1;
    @BindView(R.id.tv_friend_list)
    TextView tvFriendList;
    @BindView(R.id.v_divider_2)
    View divider2;
    @BindView(R.id.iv_menu)
    ImageView ivMenu;
    @BindView(R.id.view_pager)
    NoScrollViewPager viewPager;

    private PopupWindow mMenuWindow;
    private int yOffset;
    private int xOffset;

    private final List<Fragment> mFragmentList = new ArrayList<>();
    private TyWebViewFragment tyWebViewFragment
            = TyWebViewFragment.newInstance(Constant.HtmlAPI.INVITE_URL);
    private InviteFragment inviteFragment
            = InviteFragment.newInstance();

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, InviteActivity.class);

        context.startActivity(intent);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_invite;
    }

    @Override
    protected void initIntent(Intent intent) {

    }

    @Override
    protected void initView() {
        yOffset = UIUtils.dip2px(this, 3.5f);
        xOffset = UIUtils.dip2px(this, 20);

        mFragmentList.add(tyWebViewFragment);
        mFragmentList.add(inviteFragment);

        ivBack.setOnClickListener(this);
        tvInviteFriend.setOnClickListener(this);
        tvFriendList.setOnClickListener(this);
        ivMenu.setOnClickListener(this);

        viewPager.setCurrentItem(0);

        viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager(), mFragmentList));

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_invite_friend:
                changeStatus(tvInviteFriend, divider1, tvFriendList, divider2);
                viewPager.setCurrentItem(0);
                ivMenu.setVisibility(View.VISIBLE);
                break;
            case R.id.tv_friend_list:
                changeStatus(tvFriendList, divider2, tvInviteFriend, divider1);
                viewPager.setCurrentItem(1);
                ivMenu.setVisibility(View.GONE);
                break;
            case R.id.iv_menu:
                showPopupWindow();
                break;

            case R.id.ll_crop:
                tyWebViewFragment.crop();
                closeMenuPopWindow();
                break;

            case R.id.ll_open:
                TyUtils.goToOuterBrowser(tyWebViewFragment.getUrl());
                closeMenuPopWindow();
                break;

            case R.id.ll_refresh:
                tyWebViewFragment.reload();
                closeMenuPopWindow();
                break;
        }
    }

    private void closeMenuPopWindow() {
        if (mMenuWindow != null) {
            mMenuWindow.dismiss();
        }
    }

    /**
     * 展开popupWindow
     */
    private void showPopupWindow() {
        View window = getLayoutInflater()
                .inflate(R.layout.window_web_view_menu, null);

        window.findViewById(R.id.ll_crop).setOnClickListener(this);
        window.findViewById(R.id.ll_open).setOnClickListener(this);
        window.findViewById(R.id.ll_refresh).setOnClickListener(this);

        if (mMenuWindow == null) {
            mMenuWindow = new PopupWindow(window,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    true);

            mMenuWindow.setOutsideTouchable(true);
            mMenuWindow.setBackgroundDrawable(
                    ContextCompat.getDrawable(this, R.drawable.ic_one_inch_alpha)
            );
        }
        mMenuWindow.showAsDropDown(ivMenu, -xOffset, -yOffset, Gravity.START);
    }

    /**
     * 改变顶部的textview样式
     *
     * @param textView1
     * @param divider1
     * @param textView2
     * @param divider2
     */
    private void changeStatus(TextView textView1, View divider1, TextView textView2, View divider2) {
        textView1.setTextColor(getResources().getColor(R.color.main_blue_color));
        divider1.setVisibility(View.VISIBLE);
        textView2.setTextColor(getResources().getColor(R.color.et_tip_text_color));
        divider2.setVisibility(View.GONE);
    }

    static class PagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> mFragments;

        public PagerAdapter(FragmentManager fm, List<Fragment> fragmentList) {
            super(fm);
            this.mFragments = fragmentList;
        }

        @Override
        public Fragment getItem(int position) {
            Log.d("getItem", mFragments.get(position).getClass().getName());
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

    }
}
