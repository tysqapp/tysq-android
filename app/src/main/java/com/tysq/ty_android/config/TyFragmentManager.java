package com.tysq.ty_android.config;

import android.support.v4.app.Fragment;

import com.tysq.ty_android.R;
import com.tysq.ty_android.feature.cloud.cloudDownload.CloudDownloadFragment;
import com.tysq.ty_android.feature.cloud.cloudList.CloudListFragment;
import com.tysq.ty_android.feature.cloud.cloudUploading.CloudUploadingFragment;
import com.tysq.ty_android.feature.homePage.HomePageFragment;
import com.tysq.ty_android.feature.main.MainActivity;
import com.tysq.ty_android.feature.mine.MineFragment;
import com.tysq.ty_android.feature.web.TyWebViewToolbarFragment;

import java.util.ArrayList;

import vo.MenuItemVO;

/**
 * author       : frog
 * time         : 2019/4/11 下午5:58
 * desc         : fragment 管理汇集
 * version      : 1.3.0
 */
public class TyFragmentManager {

    // 首页导航的菜单
    private final static ArrayList<MenuItemVO> MAIN_NAV;

    private static int INDEX = 0;

    public static final int MAIN_HOME = INDEX++;
    public static final int MAIN_PROMOTION = INDEX++;
    public static final int MAIN_MINE = INDEX++;

    static {
        MAIN_NAV = new ArrayList<>();
        MAIN_NAV.add(new MenuItemVO(MAIN_HOME,
                R.string.main_home,
                R.drawable.ic_home,
                R.drawable.ic_home_unsel,
                true));

        MAIN_NAV.add(new MenuItemVO(MAIN_PROMOTION,
                R.string.main_promotion,
                R.drawable.ic_promotion_selected,
                R.drawable.ic_promotion_unselected,
                false));

        MAIN_NAV.add(new MenuItemVO(MAIN_MINE,
                R.string.main_mine,
                R.drawable.ic_mine,
                R.drawable.ic_mine_unsel,
                false));
    }

    public static ArrayList<MenuItemVO> getMainNav() {
        return MAIN_NAV;
    }

    public static Fragment getMainFragment(int menuId) {
        Fragment fragment;
        if (menuId == MAIN_HOME) {
            fragment = HomePageFragment.newInstance();
        } else if (menuId == MAIN_MINE) {
            fragment = MineFragment.newInstance();
        } else if (menuId == MAIN_PROMOTION) {
            fragment = TyWebViewToolbarFragment.newInstance(Constant.HtmlAPI.PROMOTE_EARNING);
        } else {
            fragment = HomePageFragment.newInstance();
        }
        return fragment;
    }

    public static void reset() {
        for (MenuItemVO item : MAIN_NAV) {
            item.setSelect(false);
        }
        MAIN_NAV.get(MainActivity.DEFAULT_SELECT_INDEX).setSelect(true);
    }

    /**
     * 获取云盘选择的fragment
     *
     * @param pos 0: 已上传
     *            1: 上传中
     */
    public static Fragment getCloudFragment(int pos) {
        Fragment fragment;
        if (pos == 0) {
            fragment = CloudListFragment.newInstance();
        } else if (pos == 1) {
            fragment = CloudUploadingFragment.newInstance();
        } else if (pos == 2) {
            fragment = CloudDownloadFragment.newInstance();
        } else {
            fragment = CloudListFragment.newInstance();
        }
        return fragment;
    }

}
