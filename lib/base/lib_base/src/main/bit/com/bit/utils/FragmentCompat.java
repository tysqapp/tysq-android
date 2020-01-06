package com.bit.utils;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.bit.config.BitManager;
import com.bit.view.fragment.BitBaseFragment;

import java.util.List;

/**
 * author       : frog
 * time         : 2019/3/25 下午6:05
 * desc         : Fragment 的切换
 * version      : 1.3.0
 */
public class FragmentCompat {

    /**
     * 专用于定制fragment流程
     */
    public static class Flow {
        /**
         * 初次启动fragment
         *
         * @param savedInstanceState 判断是否需要初始化加载
         * @param fragmentManager    管理器
         * @param containerId        容器id
         * @param to                 目标Fragment
         */
        public static void add(Bundle savedInstanceState,
                               FragmentManager fragmentManager,
                               int containerId,
                               Fragment to) {
            if (savedInstanceState == null) {
                add(fragmentManager, containerId, to);
            }
        }

        /**
         * 初次启动fragment
         *
         * @param fragmentManager 管理器
         * @param containerId     容器id
         * @param to              目标Fragment
         */
        public static void add(FragmentManager fragmentManager,
                               int containerId,
                               Fragment to) {
            start(fragmentManager, containerId, null, to);
        }

        /**
         * 切换fragment
         *
         * @param fragmentManager 管理器
         * @param containerId     容器id
         * @param from            被隐藏的fragment
         * @param to              目标Fragment
         */
        public static void toggle(FragmentManager fragmentManager, int containerId, Fragment from, Fragment to) {
            start(fragmentManager, containerId, from, to);
        }

        private static void start(FragmentManager fragmentManager, int containerId, Fragment from, Fragment to) {
            String toName = to.getClass().getName();
            FragmentTransaction ft = fragmentManager.beginTransaction();

            if (from == null) {
                ft.add(containerId, to, toName);
            } else {
                ft.add(containerId, to, toName);
                ft.hide(from);
            }

            //添加至回退站
            ft.addToBackStack(toName);
            ft.commit();
        }
    }

    /**
     * 专用于定制类似微信的tab切换fragment
     */
    public static class Layer {

        /**
         * 初始化fragment，list
         *
         * @param fragmentManager
         * @param containerId
         * @param showPosition
         * @param fragments
         */
        public static void init(FragmentManager fragmentManager,
                                int containerId,
                                int showPosition,
                                List<Fragment> fragments) {
            FragmentTransaction ft = fragmentManager.beginTransaction();
            int size = fragments.size();
            for (int i = 0; i < size; i++) {
                Fragment fragment = fragments.get(i);

                String toName = fragment.getClass().getName();
                ft.add(containerId, fragment, toName);

                if (i != showPosition) {
                    ft.hide(fragment);
                } else {
                    fragment.onHiddenChanged(false);
                }
            }
            ft.commit();
        }

        /**
         *
         * @param fragmentManager
         * @param myFragment
         */
        public static void restoreInstance(FragmentManager fragmentManager, List<Fragment> myFragment) {
            List<Fragment> fragments = fragmentManager.getFragments();
            myFragment.clear();
            if (fragments != null && fragments.size() > 0) {
                for (Fragment fragment : fragments) {
                    if (fragment instanceof BitBaseFragment) {
                        myFragment.add(fragments.indexOf(fragment), fragment);
                    }
                }
            }
        }

        /**
         * 切换fragment
         *
         * @param fragmentManager
         * @param hideFragment
         * @param showFragment
         */
        public static void toggle(FragmentManager fragmentManager, Fragment hideFragment, Fragment showFragment) {
            if (fragmentManager == null || showFragment == hideFragment) {
                return;
            }

            FragmentTransaction ft = fragmentManager.beginTransaction().show(showFragment);

            if (hideFragment == null) {
                List<Fragment> fragments = fragmentManager.getFragments();
                if (fragments != null && fragments.size() > 0) {
                    for (Fragment fragment : fragments) {
                        if (fragment != null && fragment != showFragment) {
                            ft.hide(fragment);
                        }
                    }
                }
            } else {
                ft.hide(hideFragment);
            }

            ft.commit();
        }

        /**
         * @date 创建时间 2018/4/22
         * @author a2
         * @Description 是否是当前fragment
         * @version
         */
        public static boolean isCurrent(FragmentManager fragmentManager, Fragment currentPage) {
            List<Fragment> fragments = fragmentManager.getFragments();
            if (fragments != null && fragments.size() > 0) {
                for (Fragment fragment : fragments) {
                    if (fragment != null && !fragment.isHidden() && fragment == currentPage) {
                        return true;
                    }
                }
            }

            return false;
        }
    }


    /**
     * 通用，实现fragment监听返回键事件
     *
     * @param fragmentActivity
     * @return
     */
    public static boolean onBackPressed(FragmentActivity fragmentActivity) {
        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        return isConsumeBackEvent(fragmentManager);
    }

    /**
     * 通用，判断当前manager内的fragment是否消费回退事件
     *
     * @param fragmentManager 调用对象自己的Manager
     * @return
     */
    public static boolean isConsumeBackEvent(FragmentManager fragmentManager) {
        if (fragmentManager == null) {
            return false;
        }

        List<Fragment> fragments = fragmentManager.getFragments();
        if (fragments != null && fragments.size() > 0) {
            int size = fragments.size();

            for (int i = size - 1; i >= 0; i--) {
                Fragment fragment = fragments.get(i);
                if (fragment instanceof BitBaseFragment) {
                    BitBaseFragment baseFragment = (BitBaseFragment) fragment;
                    boolean consume = baseFragment.onConsumeBackEvent(fragmentManager);

                    if (BitManager.getInstance().isDebug()) {
                        Log.i("FragmentCompat:",
                                baseFragment.getFragmentTag() + ",是否消费:" + consume);
                    }

                    if (consume) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

}
