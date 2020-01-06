package com.tysq.ty_android.feature.editArticle.dialog;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.abc.lib_utils.toast.ToastUtils;
import com.tysq.ty_android.R;
import com.tysq.ty_android.base.CommonBaseDialog;
import com.tysq.ty_android.feature.editArticle.adapter.EditCategoryChooseAdapter;
import com.tysq.ty_android.feature.editArticle.listener.OnCategoryChooseListener;
import com.tysq.ty_android.feature.editArticle.listener.OnEditCategoryResultListener;

import java.util.ArrayList;

import butterknife.BindView;
import response.home.SubCategory;
import response.home.TopCategory;

/**
 * author       : frog
 * time         : 2019/5/7 下午6:02
 * desc         : 分类选择
 * version      : 1.3.0
 */
public class CategoryChooseDialog extends CommonBaseDialog
        implements OnCategoryChooseListener, View.OnClickListener {

    private static final int NONE = -1;

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_top)
    TextView tvTop;
    @BindView(R.id.v_top_divider)
    View vTopDivider;
    @BindView(R.id.v_sub_divider)
    View vSubDivider;
    @BindView(R.id.tv_sub)
    TextView tvSub;
    @BindView(R.id.recycle_view)
    RecyclerView recycleView;
    @BindView(R.id.iv_close)
    ImageView ivClose;

    // 分类数据
    private ArrayList<TopCategory> mCategoryData;

    private TopCategory mTopItem;
    private SubCategory mSubItem;

    // 选择的 一级分类
    private TopCategory mSelTop;

    private EditCategoryChooseAdapter mAdapter;
    private OnEditCategoryResultListener mListener;

    private int mType;

    public static CategoryChooseDialog newInstance() {
        Bundle args = new Bundle();
        CategoryChooseDialog fragment = new CategoryChooseDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initArgs(Bundle arguments) {
    }

    public void setTopItem(TopCategory topItem) {
        this.mSelTop = topItem;
        this.mTopItem = topItem;
        if (this.mAdapter != null
                && topItem != null
                && topItem.getSubCategoryList() != null) {
            mAdapter.setSubData(topItem.getSubCategoryList());
            mAdapter.notifyDataSetChanged();
        }
    }

    public void setSubItem(SubCategory subItem) {
        this.mSubItem = subItem;
    }

    /**
     * 设置分类数据
     */
    public void setCategoryData(ArrayList<TopCategory> categoryData) {
        this.mCategoryData = categoryData;
    }

    public void setListener(OnEditCategoryResultListener listener) {
        this.mListener = listener;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.dialog_category_fragment;
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
        mType = EditCategoryChooseAdapter.TOP;

        mAdapter = new EditCategoryChooseAdapter(getContext(), mCategoryData, mType);
        mAdapter.setListener(this);
        mAdapter.setTopId(getTopId());
        mAdapter.setSubId(getSubId());

        tvTop.setOnClickListener(this);
        tvSub.setOnClickListener(this);
        ivClose.setOnClickListener(this);

        recycleView.setAdapter(mAdapter);
        recycleView.setLayoutManager(new LinearLayoutManager(getContext()));

        setCategoryTitle();

        showTop();
    }

    @Override
    public void onItemClick(int type, int position) {
        Log.i("zinc", "onItemClick: [type:" + type + "; pos:" + position + "]");

        if (type == EditCategoryChooseAdapter.TOP) {

            mSelTop = mCategoryData.get(position);

            tvTop.setText(mSelTop.getName());

            // 如果没有二级分类，直接返回
            if (mSelTop.getSubCategoryList() == null
                    || mSelTop.getSubCategoryList().size() <= 0) {

                if (mListener == null) {
                    Log.e(getTag(), "listener is null");
                    dismiss();
                    return;
                }

                mListener.onCategoryChoose(mSelTop, null);
                dismiss();

                return;
            }

            mType = EditCategoryChooseAdapter.SUB;

            mTopItem = mSelTop;
            mSubItem = null;
            setCategoryTitle();

            mAdapter.setType(mType);
            mAdapter.setTopId(getTopId());
            mAdapter.setSubData(mSelTop.getSubCategoryList());
            mAdapter.notifyDataSetChanged();

            showSub();

        } else if (type == EditCategoryChooseAdapter.SUB) {

            if (mListener == null) {
                Log.e(getTag(), "listener is null");
                dismiss();
                return;
            }

            mListener.onCategoryChoose(mSelTop, mSelTop.getSubCategoryList().get(position));
            dismiss();

        }

    }

    /**
     * 显示一级
     */
    private void showTop() {
        vTopDivider.setVisibility(View.VISIBLE);
        vSubDivider.setVisibility(View.INVISIBLE);
    }

    /**
     * 显示二级
     */
    private void showSub() {
        vTopDivider.setVisibility(View.INVISIBLE);
        vSubDivider.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_top:

                if (mType == EditCategoryChooseAdapter.TOP) {
                    return;
                }

                showTop();

                mType = EditCategoryChooseAdapter.TOP;

                mAdapter.setType(mType);
                mAdapter.notifyDataSetChanged();

                break;
            case R.id.tv_sub:

                if (mType == EditCategoryChooseAdapter.SUB) {
                    return;
                }

                if (mSelTop == null) {
                    ToastUtils.show(getString(R.string.edit_please_choose_top_first));
                    return;
                }

                if (mSelTop.getSubCategoryList().size() <= 0) {
                    ToastUtils.show(getString(R.string.edit_no_sub));
                    return;
                }

                showSub();

                mType = EditCategoryChooseAdapter.SUB;

                mAdapter.setType(mType);
                mAdapter.setSubId(getSubId());
                mAdapter.setSubData(mSelTop.getSubCategoryList());
                mAdapter.notifyDataSetChanged();

                break;
            case R.id.iv_close:
                dismiss();
                break;
        }
    }

    private int getTopId() {
        return mTopItem == null ? NONE : mTopItem.getId();
    }

    private int getSubId() {
        return mSubItem == null ? NONE : mSubItem.getId();
    }

    private void setCategoryTitle() {
        if (mTopItem == null) {
            tvTop.setText(getString(R.string.category_top));
        } else {
            tvTop.setText(mTopItem.getName());
        }

        if (mSubItem == null) {
            tvSub.setText(getString(R.string.category_sub));
        } else {
            tvSub.setText(mSubItem.getName());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        this.mListener = null;
    }
}
