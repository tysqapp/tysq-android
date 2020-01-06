package com.tysq.ty_android.feature.homePage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bit.view.fragment.BitBaseFragment;
import com.tysq.ty_android.R;
import com.tysq.ty_android.config.TyConfig;
import com.tysq.ty_android.feature.homePage.adapter.CategoryAdapter;
import com.tysq.ty_android.feature.homePage.listener.OnTopCategoryItemClickListener;
import com.tysq.ty_android.widget.TagFlowLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import eventbus.CategorySelectChangeEvent;
import response.home.SubCategory;
import response.home.TopCategory;

/**
 * author       : frog
 * time         : 2019/4/28 下午3:21
 * desc         : 分类信息页
 * version      : 1.3.0
 */
public class CategoryInfoFragment extends BitBaseFragment
        implements OnTopCategoryItemClickListener, View.OnClickListener {

    private final ArrayList<SubCategory> EMPTY_LIST = new ArrayList<>();

    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    @BindView(R.id.v_divider)
    View vDivider;
    @BindView(R.id.recycle_view)
    RecyclerView recycleView;
    @BindView(R.id.rl_recommend_title)
    RelativeLayout rlRecommendTitle;
    @BindView(R.id.tv_recommend)
    TextView tvRecommend;
    @BindView(R.id.tag_flow)
    TagFlowLayout tagFlow;

    private List<TopCategory> mCategoryData;

    private CategoryAdapter mAdapter;

    private final List<TextView> mCategoryCache = new ArrayList<>();

    private int mSelectColor;
    private int mUnSelectColor;

    private int mSelTopId;
    private int mSelSubId;

    public static CategoryInfoFragment newInstance(ArrayList<TopCategory> categoryData,
                                                   int mSelTopId,
                                                   int mSelSubId) {

        Bundle args = new Bundle();
        args.putParcelableArrayList(CategoryInfoActivity.CATEGORY_INFO, categoryData);
        args.putInt(CategoryInfoActivity.TOP_ID, mSelTopId);
        args.putInt(CategoryInfoActivity.SUB_ID, mSelSubId);

        CategoryInfoFragment fragment = new CategoryInfoFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    protected void initArgs(Bundle arguments) {
        super.initArgs(arguments);
        mCategoryData = arguments.getParcelableArrayList(CategoryInfoActivity.CATEGORY_INFO);
        mSelTopId = arguments.getInt(CategoryInfoActivity.TOP_ID);
        mSelSubId = arguments.getInt(CategoryInfoActivity.SUB_ID);
    }

    @Override
    protected View onCreateFragmentView(LayoutInflater inflater,
                                        @Nullable ViewGroup container,
                                        @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_category_info, container, false);
    }

    @Override
    protected void initView(View view) {
        mSelectColor = ContextCompat.getColor(getContext(), R.color.main_blue_color);
        mUnSelectColor = ContextCompat.getColor(getContext(), R.color.main_text_color);

        mAdapter = new CategoryAdapter(getContext(), mCategoryData, mSelTopId, this);

        recycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        recycleView.setAdapter(mAdapter);

        int selTopIndex = findSelTopIndex(mCategoryData);
        recycleView.scrollToPosition(selTopIndex);

        tvCancel.setOnClickListener(this);

        refreshSubCategory();

    }

    private void refreshSubCategory() {
        int selTopIndex = findSelTopIndex(mCategoryData);
        if (selTopIndex == TyConfig.ERROR) {
            return;
        }

        TopCategory topCategory = mCategoryData.get(selTopIndex);

        List<SubCategory> selectCategoryList = topCategory.getSubCategoryList();

        EMPTY_LIST.clear();
        SubCategory subCategory = new SubCategory();
        subCategory.setName(getString(R.string.main_nav_sub_all));
//        subCategory.setId(TyConfig.CATEGORY_RECOMMEND_ID);
        subCategory.setId(topCategory.getId());
        EMPTY_LIST.add(subCategory);

        createSubCategory((selectCategoryList == null || selectCategoryList.size() <= 0) ?
                EMPTY_LIST : selectCategoryList);
    }

    /**
     * 查找选中一级
     * @param list
     * @return
     */
    private int findSelTopIndex(List<TopCategory> list) {
        for (int i = 0; i < list.size(); i++) {
            TopCategory item = list.get(i);

            if (item.getId() == mSelTopId) {
                return i;
            }
        }

        return TyConfig.ERROR;
    }

    private void createSubCategory(List<SubCategory> subCategoryList) {

        if (getContext() == null) {
            loge("上下文出问题！！");
            return;
        }

        tagFlow.removeAllViews();

        for (int i = 0; i < subCategoryList.size(); ++i) {

            SubCategory subCategory = subCategoryList.get(i);

            TextView item;
            if (i < mCategoryCache.size()) {
                item = mCategoryCache.get(i);

                logi("category 缓存");
            } else {
                item = (TextView) getLayoutInflater()
                        .inflate(R.layout.item_sub_category, tagFlow, false);
                mCategoryCache.add(item);

                logi("category 新建");
            }

            item.setText(subCategory.getName());
            if (subCategory.getId() == mSelSubId) {
                item.setTextColor(mSelectColor);
                item.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.shape_blue_4dp_border));
            } else {
                item.setTextColor(mUnSelectColor);
                item.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.shape_gray_border));
            }

            int finalI = i;
            item.setOnClickListener(v -> {
                logi("sub category: [ index:" + finalI + "; " +
                        "id:" + subCategory.getId() + ";" +
                        "name:" + subCategory.getName() + "]");

                handleSubCategorySel(finalI, subCategory.getId());

            });

            tagFlow.addView(item);

        }

    }

    /**
     * 处理二级菜单选中逻辑
     *
     * @param index 选中的下标
     * @param selId 选中的id
     */
    private void handleSubCategorySel(int index, int selId) {
        if (mSelSubId == selId) {
            changeOver();
            return;
        }

        int beforeSelSubId = mSelSubId;

        TextView selTextView = (TextView) tagFlow.getChildAt(index);
        selTextView.setTextColor(mSelectColor);
        selTextView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.shape_blue_4dp_border));

        mSelSubId = selId;

        int selTopIndex = getSelTopIndex();

        if (selTopIndex == CategoryInfoActivity.ERROR_ID) {
            loge("handleSubCategorySel: [selTopIndex 为 -1！！！]");
            if (getActivity() != null) {
                getActivity().finish();
            }
            return;
        }

        // 二级菜单处理
        List<SubCategory> subCategoryList = mCategoryData.get(selTopIndex).getSubCategoryList();
        if (subCategoryList != null) {

            TextView beforeSelTextView = null;
            for (int i = 0; i < subCategoryList.size(); i++) {
                SubCategory subCategory = subCategoryList.get(i);

                if (subCategory.getId() == beforeSelSubId) {
                    beforeSelTextView = (TextView) tagFlow.getChildAt(i);
                    break;
                }
            }

            // 将之前选中的设置为未选中
            if (beforeSelTextView != null) {
                beforeSelTextView.setTextColor(mUnSelectColor);
                beforeSelTextView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.shape_gray_border));
            }

        }

        changeOver();

    }

    private void changeOver() {
        EventBus.getDefault().post(new CategorySelectChangeEvent(mSelTopId, mSelSubId));
        if (getActivity() != null) {
            getActivity().finish();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mCategoryCache.clear();
    }

    @Override
    public void onTopCategoryClick(View view, int position) {
        logi("[position:" + position + "]");

        int beforeSelectPos = -1;

        beforeSelectPos = getSelTopIndex();

        // 如果之前和这次选择的同一个则不操作
        if (beforeSelectPos == position) {
            return;
        }

        mSelTopId = mCategoryData.get(position).getId();
        // 设置当前选择的id
        mAdapter.setSelTopId(mSelTopId);

        mAdapter.notifyDataSetChanged();

        refreshSubCategory();

    }

    /**
     * 获取选择的一级菜单的下标
     *
     * @return 选中的一级下标
     */
    private int getSelTopIndex() {
        for (int i = 0; i < mCategoryData.size(); ++i) {
            if (mCategoryData.get(i).getId() == mSelTopId) {
                return i;
            }
        }

        return -1;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cancel:
                if (getActivity() != null) {
                    getActivity().finish();
                }
                break;
        }
    }
}
