package com.tysq.ty_android.feature.homePage;

import android.animation.ValueAnimator;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.abc.lib_utils.toast.ToastUtils;
import com.bit.config.BitManager;
import com.bit.utils.UIUtils;
import com.bit.view.fragment.BitBaseFragment;
import com.bit.widget.NoScrollViewPager;
import com.bit.widget.StateLayout;
import com.tysq.ty_android.R;
import com.tysq.ty_android.app.TyApplication;
import com.tysq.ty_android.config.TyConfig;
import com.tysq.ty_android.feature.articleList.ArticleListFragment;
import com.tysq.ty_android.feature.editArticle.EditArticleActivity;
import com.tysq.ty_android.feature.homePage.adapter.SubCategoryAdapter;
import com.tysq.ty_android.feature.homePage.adapter.TopCategoryAdapter;
import com.tysq.ty_android.feature.homePage.di.DaggerHomePageComponent;
import com.tysq.ty_android.feature.homePage.di.HomePageModule;
import com.tysq.ty_android.feature.homePage.listener.OnSubCategoryItemClickListener;
import com.tysq.ty_android.feature.homePage.listener.OnTopCategoryItemClickListener;
import com.tysq.ty_android.login.CheckLogin;
import com.zinc.jrecycleview.listener.JRecycleListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import eventbus.CategorySelectChangeEvent;
import eventbus.SubCategoryChangeEvent;
import response.AdResp;
import response.home.CategoryResp;
import response.home.SubCategory;
import response.home.TopCategory;

/**
 * author       : frog
 * time         : 2019-07-17 11:52
 * desc         : 首页
 * version      : 1.3.0
 */
public final class HomePageFragment extends BitBaseFragment<HomePagePresenter>
        implements HomePageView,
        OnSubCategoryItemClickListener,
        OnTopCategoryItemClickListener,
        View.OnClickListener, JRecycleListener {

    private static final int DURATION = 100;

    private final List<SubCategory> EMPTY_SUB_CATEGORY = new ArrayList<>();

    @BindView(R.id.iv_edit)
    ImageView ivEdit;
    @BindView(R.id.iv_menu)
    ImageView ivMenu;
    @BindView(R.id.rv_top_category)
    RecyclerView rvTopCategory;
    @BindView(R.id.rv_sub_category)
    RecyclerView rvSubCategory;
    @BindView(R.id.view_pager)
    NoScrollViewPager viewPager;
    @BindView(R.id.v_sub_shade)
    View vSubShade;
    @BindView(R.id.v_sub_divider)
    View vSubDivider;
    @BindView(R.id.ll_sub_category)
    LinearLayout llSubCategory;

    private TopCategoryAdapter mTopAdapter;
    private SubCategoryAdapter mSubAdapter;

    public static final ArrayList<TopCategory> mCategoryData = new ArrayList<>();

    private ValueAnimator mAnim;
    private int mSubHeight;

    // 是否显示公告
    public boolean isShowAnnouncement = false;

    public static HomePageFragment newInstance() {

        Bundle args = new Bundle();

        HomePageFragment fragment = new HomePageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private StateLayout mStateLayout;

    @Override
    protected View onCreateFragmentView(LayoutInflater inflater, ViewGroup container,
                                        Bundle savedInstanceState) {
        mStateLayout = wrapFragmentView(inflater
                .inflate(R.layout.fragment_home_page, container, false));
        return mStateLayout;
    }

    @Override
    protected void initView(View view) {
        //StatusUtils.fitTitleBar(getActivity(), rvTopCategory);
        mSubHeight = UIUtils.dip2px(getContext(), 46);
        initAnim();

        mStateLayout.showLoading();

        mStateLayout.getRetryView()
                .findViewById(BitManager.getInstance().getRetryBtnId())
                .setOnClickListener(v -> {
                    mStateLayout.showLoading();
                    loadData();
                });

        LinearLayoutManager topLinearLayoutManager = new LinearLayoutManager(getContext());
        topLinearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvTopCategory.setLayoutManager(topLinearLayoutManager);

        mTopAdapter = new TopCategoryAdapter(getContext(), this);
        rvTopCategory.setAdapter(mTopAdapter);

        LinearLayoutManager subLinearLayoutManager = new LinearLayoutManager(getContext());
        subLinearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvSubCategory.setLayoutManager(subLinearLayoutManager);

        mSubAdapter = new SubCategoryAdapter(getContext(), this);
        rvSubCategory.setAdapter(mSubAdapter);

        ivMenu.setOnClickListener(this);
        ivEdit.setOnClickListener(this);

        loadData();

        if (!isShowAnnouncement) {
            mPresenter.loadAnnouncement();
        }

    }

    private void loadData() {
        mPresenter.loadCategory(getString(R.string.main_nav_top_recommend),
                getString(R.string.main_nav_sub_all));
    }

    @Override
    protected void registerDagger() {
        DaggerHomePageComponent.builder()
                .appComponent(TyApplication.getAppComponent())
                .homePageModule(new HomePageModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void onLoadCategoryFailure() {
        mStateLayout.showRetry();
        ToastUtils.show(getString(R.string.home_category_load_error));
    }

    @Override
    public void onLoadCategory(CategoryResp value) {
        mStateLayout.showContent();

        mCategoryData.clear();
        mCategoryData.addAll(value.getCategoryInfo());

        mTopAdapter.setData(mCategoryData);

        initViewPage(mCategoryData);

        checkShowOrHideSubBar();
    }

//    @Override
//    public void onGetJudgement(JudgementResp value) {
//
//        EditArticleActivity.startActivity(getContext(), "", false);
//
//    }

    @Override
    public void onLoadAnnouncement(List<AdResp.AdvertisementListBean> advertisementList) {
        isShowAnnouncement = true;

        if (advertisementList == null || advertisementList.size() <= 0) {
            return;
        }

        AnnouncementDialog fragment
                = AnnouncementDialog.newInstance(advertisementList.size());
        fragment.setData(advertisementList);

        fragment.show(this);
    }

    /**
     * 检测当前是否要显示二级菜单
     */
    private void checkShowOrHideSubBar() {
        boolean isShow = true;
        for (TopCategory topItem : mCategoryData) {
            if (!topItem.isSelect()) {
                continue;
            }

            if (topItem.getSubCategoryList() == null
                    || topItem.getSubCategoryList().size() <= 0) {
                isShow = false;
                break;
            }
        }
        controlSubBar(isShow);
    }

    /**
     * 控制二级栏
     *
     * @param isShow 是否显示
     *               true：显示
     *               false：不显示
     */
    private void controlSubBar(boolean isShow) {
        if (isShow) {
            llSubCategory.setVisibility(View.VISIBLE);
            vSubShade.setVisibility(View.VISIBLE);
            setSubCateHeight(mSubHeight);
        } else {
            llSubCategory.setVisibility(View.GONE);
            vSubShade.setVisibility(View.GONE);
        }
    }

    @Override
    public void onSubCategoryClick(View view, int position) {

        logi("onSubCategoryClick: [position:" + position + "]");

        List<SubCategory> subList = null;

        int selTopId = TyConfig.ERROR;
        int selSubId = TyConfig.ERROR;

        for (int i = 0; i < mCategoryData.size(); i++) {
            TopCategory item = mCategoryData.get(i);
            if (item.isSelect()) {
                selTopId = item.getId();
                subList = item.getSubCategoryList();
                break;
            }
        }

        if (selTopId == TyConfig.ERROR) {
            loge("selTopPos 错误，数据有问题！");
            return;
        }

        if (subList == null) {
            loge("subList 为空，这个不正常！");
            return;
        }

        for (int j = 0; j < subList.size(); ++j) {
            SubCategory subCategory = subList.get(j);

            if (j == position) {
                subCategory.setSelect(true);
                selSubId = subCategory.getId();
            } else {
                subCategory.setSelect(false);
            }
        }

        mSubAdapter.notifyDataSetChanged();

        if (selSubId == TyConfig.ERROR) {
            loge("selSubId 错误，数据有问题！");
            return;
        }

        EventBus.getDefault().post(new SubCategoryChangeEvent(selTopId, selSubId));

        moveToCenter(rvSubCategory, view, position);

    }

    @Override
    public void onTopCategoryClick(View view, int position) {
        logi("onTopCategoryClick: [position:" + position + "]");

        List<SubCategory> subList = null;

        for (int i = 0; i < mCategoryData.size(); ++i) {
            TopCategory item = mCategoryData.get(i);
            item.setSelect(position == i);

            // 获取当前分类的列表
            if (position == i) {
                subList = item.getSubCategoryList();
            }
        }

        // 设置二级分类数据
        mSubAdapter.setData(subList == null ? EMPTY_SUB_CATEGORY : subList);

        // 检测是否要显示二级分类
        checkShowOrHideSubBar();

        int subIndex = TyConfig.ERROR;
        if (subList != null) {
            for (int i = 0; i < subList.size(); i++) {
                if (subList.get(i).isSelect()) {
                    subIndex = i;
                    break;
                }
            }
        }

        // 刷新数据
        mTopAdapter.notifyDataSetChanged();
        mSubAdapter.notifyDataSetChanged();

        moveToCenter(rvTopCategory, view, position);
        if (subIndex != TyConfig.ERROR) {
            ((LinearLayoutManager) rvSubCategory.getLayoutManager())
                    .scrollToPositionWithOffset(subIndex, 0);
        }

        viewPager.setCurrentItem(position);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 进入分类详情页
            case R.id.iv_menu:
                int selTopId = TyConfig.ERROR;
                int selSubId = TyConfig.ERROR;

                for (TopCategory topItem : mCategoryData) {
                    if (topItem.isSelect()) {
                        selTopId = topItem.getId();
                        List<SubCategory> subCategoryList = topItem.getSubCategoryList();
                        if (subCategoryList == null) {
                            break;
                        }
                        for (SubCategory subItem : subCategoryList) {
                            if (subItem.isSelect()) {
                                selSubId = subItem.getId();
                                break;
                            }
                        }
                    }
                }

                if (selSubId == TyConfig.ERROR) {
                    selSubId = selTopId;
                }

                CategoryInfoActivity.startActivity(getContext(), mCategoryData, selTopId, selSubId);
                break;
            case R.id.iv_edit:
                goToEdit();
                break;
        }
    }

    @CheckLogin()
    private void goToEdit() {
        EditArticleActivity.startActivity(getContext(), "", false);
    }

    /**
     * 将 RecycleView 点击的 item 移至中间
     */
    private void moveToCenter(RecyclerView rv, View view, int position) {

        int vWidth = view.getWidth();

        Rect rect = new Rect();

        rv.getGlobalVisibleRect(rect);

        //除掉点击View的宽度，剩下整个屏幕的宽度
        int reWidth = rect.right - rect.left - vWidth;

        final int firstPosition =
                ((LinearLayoutManager) rv.getLayoutManager()).findFirstVisibleItemPosition();

        //从左边到点击的Item的位置距离
        View childView = rv.getChildAt(position - firstPosition);
        if (childView == null) {
            return;
        }
        int left = childView.getLeft();

        int half = reWidth / 2;//半个屏幕的宽度

        int moveDis = left - half;//向中间移动的距离

        rv.smoothScrollBy(moveDis, 0);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCategorySelChange(CategorySelectChangeEvent event) {

        List<SubCategory> subList = null;

        int selTopIndex = TyConfig.ERROR;

        for (int i = 0; i < mCategoryData.size(); ++i) {
            TopCategory topItem = mCategoryData.get(i);
            if (topItem.getId() == event.getTopId()) {
                topItem.setSelect(true);

                // 修改二级选中项
                List<SubCategory> subCategoryList = topItem.getSubCategoryList();
                if (subCategoryList != null) {

                    subList = subCategoryList;

                    for (SubCategory subCategory : subCategoryList) {
                        subCategory.setSelect(subCategory.getId() == event.getSubId());
                    }
                }

                selTopIndex = i;
            } else {
                topItem.setSelect(false);
            }
        }

        mSubAdapter.setData(subList == null ? EMPTY_SUB_CATEGORY : subList);

        mTopAdapter.notifyDataSetChanged();
        mSubAdapter.notifyDataSetChanged();

        checkShowOrHideSubBar();

        int subIndex = TyConfig.ERROR;
        if (subList != null) {
            for (int i = 0; i < subList.size(); i++) {
                if (subList.get(i).isSelect()) {
                    subIndex = i;
                    break;
                }
            }
        }

        if (selTopIndex != TyConfig.ERROR) {
            ((LinearLayoutManager) rvTopCategory.getLayoutManager())
                    .scrollToPositionWithOffset(selTopIndex, 0);
        }

        if (subIndex != TyConfig.ERROR) {
            ((LinearLayoutManager) rvSubCategory.getLayoutManager())
                    .scrollToPositionWithOffset(subIndex, 0);
        }

        // 发送页面刷新
        EventBus.getDefault().post(new SubCategoryChangeEvent(event.getTopId(), event.getSubId()));
        viewPager.setCurrentItem(selTopIndex);
    }

    private void initViewPage(List<TopCategory> categoryList) {
        viewPager.setAdapter(new PagerAdapter(getChildFragmentManager(), categoryList, this));
        viewPager.setOffscreenPageLimit(categoryList.size());
    }

    @Override
    protected int getInitRegister() {
        return BUTTER_KNIFE | EVENT_BUS;
    }

    @Override
    public boolean onTouch(MotionEvent motionEvent, float deltaY) {
        int height = llSubCategory.getLayoutParams().height;

        if (mSubAdapter.getSize() <= 0) {
            return false;
        }

        if (height <= 0 && deltaY < 0) {
            return false;
        } else if (height >= mSubHeight && deltaY > 0) {
            return false;
        } else {
            setSubCateHeight((int) (height + deltaY));
            return true;
        }
    }

    @Override
    public boolean onUp(MotionEvent motionEvent) {
        if (mSubAdapter.getSize() <= 0) {
            return false;
        }

        int curHeight = llSubCategory.getLayoutParams().height;

        if (curHeight >= mSubHeight || curHeight <= 0) {
            return false;
        }

        int halfHeight = mSubHeight >> 1;

        if (mAnim.isRunning()) {
            mAnim.cancel();
        }

        if (curHeight > halfHeight) {
            mAnim.setIntValues(curHeight, mSubHeight);
        } else {
            mAnim.setIntValues(curHeight, 0);
        }
        mAnim.start();

        return true;
    }

    /**
     * 初始化动画
     */
    private void initAnim() {
        mAnim = ValueAnimator.ofInt(0, mSubHeight);
        mAnim.setDuration(DURATION);
        mAnim.addUpdateListener(animation -> {
            int value = (int) animation.getAnimatedValue();
            setSubCateHeight(value);
        });
    }

    /**
     * 设置二级高度
     */
    private void setSubCateHeight(int height) {
        if (height > mSubHeight) {
            height = mSubHeight;
        } else if (height < 0) {
            height = 0;
        }

        ViewGroup.LayoutParams layoutParams = llSubCategory.getLayoutParams();
        layoutParams.height = height;
        llSubCategory.setLayoutParams(layoutParams);
    }

    static class PagerAdapter extends FragmentPagerAdapter {

        private final List<TopCategory> mCategoryList;
        private final JRecycleListener mListener;

        PagerAdapter(FragmentManager fm,
                     List<TopCategory> categoryList,
                     JRecycleListener listener) {
            super(fm);
            this.mCategoryList = categoryList;
            this.mListener = listener;
        }

        @Override
        public ArticleListFragment getItem(int position) {
            TopCategory topCategory = mCategoryList.get(position);

            int subId = 0;
            List<SubCategory> subCategoryList = topCategory.getSubCategoryList();
            if (subCategoryList != null) {
                for (SubCategory subCategory : subCategoryList) {
                    if (subCategory.isSelect()) {
                        subId = subCategory.getId();
                        break;
                    }
                }
            }

            ArticleListFragment fragment
                    = ArticleListFragment.newInstance(topCategory.getId(), subId);

            fragment.setListener(mListener);

            return fragment;
        }

        @Override
        public int getCount() {
            return mCategoryList.size();
        }

    }

}
