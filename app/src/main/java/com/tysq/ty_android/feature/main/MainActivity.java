package com.tysq.ty_android.feature.main;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.View;

import com.abc.lib_cache.config.ProxyConfig;
import com.bit.utils.DoubleClickExitDetector;
import com.bit.utils.StatusUtils;
import com.bit.view.activity.BitBaseActivity;
import com.bit.widget.NoScrollViewPager;
import com.tysq.ty_android.R;
import com.tysq.ty_android.adapter.OnTabSelectedListenerAdapter;
import com.tysq.ty_android.config.TyFragmentManager;
import com.tysq.ty_android.local.sp.NetCache;
import com.tysq.ty_android.widget.MainNavTabItem;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import eventbus.NotificationUpdateEvent;
import vo.MenuItemVO;

/**
 * author       : frog
 * time         : 2019/6/5 下午2:10
 * desc         : 主页
 * version      : 1.3.0
 */
public class MainActivity extends BitBaseActivity {

    @BindView(R.id.view_pager)
    NoScrollViewPager viewPager;
    @BindView(R.id.v_divider)
    View vDivider;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;

    private DoubleClickExitDetector mDoubleClickExitDetector;

    public static final int DEFAULT_SELECT_INDEX = 0;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, MainActivity.class);

        context.startActivity(intent);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initIntent(Intent intent) {
    }

    @Override
    protected void initView() {

        ProxyConfig.getInstance().setServerHost(NetCache.getDefault().getDomain());

        mDoubleClickExitDetector = new DoubleClickExitDetector(getString(R.string.main_exit_click_tip));

        StatusUtils.changeStatusBarTextColor(true, MainActivity.this);
        final ArrayList<MenuItemVO> mainNav = TyFragmentManager.getMainNav();

        viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager()) {
            @Override
            public CharSequence getPageTitle(int position) {
                return getString(mainNav.get(position).getTitleRes());
            }
        });
        tabLayout.setupWithViewPager(viewPager);
        //设置自定义视图(curIndex默认选定为第0个)
        int curIndex = 0;
        for (int i = 0; i < tabLayout.getTabCount(); i++) {

            TabLayout.Tab item = tabLayout.getTabAt(i);
            MainNavTabItem tabItem = new MainNavTabItem(this).initData(mainNav.get(i));

            if (item != null) {
                item.setCustomView(tabItem);
                if (curIndex == i) {
                    item.select();
                }
            } else {
                Log.e(TAG, "Tab is null!!!");
            }

        }
        viewPager.setOffscreenPageLimit(mainNav.size());

        tabLayout.addOnTabSelectedListener(new OnTabSelectedListenerAdapter() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();

                if (position == tabLayout.getTabCount() - 1) {
                    setStatusBarColor(R.color.main_blue_color, false);
                } else {
                    setStatusBarColor(R.color.white, false);
                }


                updateTab(position, true, tab);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                updateTab(position, false, tab);
            }
        });

    }

    private void updateTab(int position,
                           boolean isSelect,
                           TabLayout.Tab tab) {

        if (!(tab.getCustomView() instanceof MainNavTabItem)) {
            return;
        }

        TyFragmentManager
                .getMainNav()
                .get(position)
                .setSelect(isSelect);

        MainNavTabItem item = (MainNavTabItem) tab.getCustomView();
        item.refresh();

    }

    static class PagerAdapter extends FragmentPagerAdapter {

        PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return TyFragmentManager
                    .getMainFragment(TyFragmentManager
                            .getMainNav()
                            .get(position)
                            .getId()
                    );
        }

        @Override
        public int getCount() {
            return TyFragmentManager.getMainNav().size();
        }

    }

    @Override
    public void onBackPressed() {
        if (tabLayout.getSelectedTabPosition() == DEFAULT_SELECT_INDEX) {
            boolean doubleClick = mDoubleClickExitDetector.click();
            if (doubleClick) {
                super.onBackPressed();
            }
        } else {
            TabLayout.Tab tab = tabLayout.getTabAt(DEFAULT_SELECT_INDEX);
            if (tab != null) {
                tab.select();
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void update(NotificationUpdateEvent event) {
        MenuItemVO menuItemVO = TyFragmentManager.getMainNav().get(2);

        // 我的
        if (menuItemVO != null) {
            menuItemVO.setNotifyNum(event.getCount());
            TabLayout.Tab tab = tabLayout.getTabAt(2);

            if (tab == null) {
                return;
            }
            MainNavTabItem mainNavTabItem = (MainNavTabItem) tab.getCustomView();

            if (mainNavTabItem == null) {
                return;
            }

            mainNavTabItem.updateNotificationNum();
        }

    }

}
