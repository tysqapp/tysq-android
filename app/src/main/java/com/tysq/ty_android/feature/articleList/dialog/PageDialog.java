package com.tysq.ty_android.feature.articleList.dialog;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.abc.lib_wheelview.adapter.ArrayWheelAdapter;
import com.abc.lib_wheelview.widget.WheelView;
import com.bit.view.fragment.BitListFragment;
import com.tysq.ty_android.R;
import com.tysq.ty_android.base.CommonBaseDialog;
import com.tysq.ty_android.feature.articleList.ArticleListFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import response.home.PageInfo;

/**
 * author       : frog
 * time         : 2019-10-15 15:43
 * desc         :
 * version      :
 */
public class PageDialog extends CommonBaseDialog
        implements View.OnClickListener {

    private final static String PAGE_INFO = "pageInfo";

    private PageInfo pageInfo;

    @BindView(R.id.ll_content)
    LinearLayout llContent;
    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    @BindView(R.id.tv_confirm)
    TextView tvConfirm;
    @BindView(R.id.wheelview)
    WheelView<String> wheelview;

    ArticleListFragment mFragment;

    public static PageDialog newInstance(PageInfo pageInfo) {

        Bundle args = new Bundle();

        PageDialog fragment = new PageDialog();
        fragment.setArguments(args);

        args.putParcelable(PAGE_INFO, pageInfo);

        return fragment;
    }

    public PageDialog setFragment(ArticleListFragment fragment) {
        this.mFragment = fragment;
        return this;
    }

    @Override
    protected void initArgs(Bundle arguments) {
        pageInfo = arguments.getParcelable(PAGE_INFO);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.dialog_page;
    }

    @Override
    protected int obtainWidth() {
        return ViewGroup.LayoutParams.MATCH_PARENT;
    }

    @Override
    protected int obtainHeight() {
        return dp2px(270);
    }

    @Override
    protected int obtainGravity() {
        return Gravity.BOTTOM;
    }

    @Override
    protected void initView(View view) {
        long total = pageInfo.getTotal();
        int pageSize = pageInfo.getPageSize();

        long pageLength = total / pageSize + 1;

        List<String> list = new ArrayList<>();
        for (int i = 1; i <= pageLength; ++i) {
            list.add("第 " + i + " 页");
        }

        int textSize = 16;
        WheelView.WheelViewStyle style = new WheelView.WheelViewStyle();
        style.textSize = textSize;
        style.selectedTextSize = textSize;
        style.selectedTextColor = ContextCompat.getColor(getContext(), R.color.main_text_color);
        style.textColor = ContextCompat.getColor(getContext(), R.color.tip_text_color);
        wheelview.setStyle(style);

        wheelview.setWheelAdapter(new ArrayWheelAdapter<>(getContext()));
        wheelview.setWheelSize(5);
        wheelview.setSkin(WheelView.Skin.Holo); // common皮肤
        wheelview.setWheelData(list);  // 数据集合

        wheelview.setSelection((int) (pageInfo.getCurPage() - 1));

        tvCancel.setOnClickListener(v -> {
            dismiss();
        });

        tvConfirm.setOnClickListener(v -> {
            Log.i("PageDialog", "initView: " + wheelview.getCurrentPosition());
            int curPos = wheelview.getCurrentPosition() + 1;
            pageInfo.setCurPage(curPos);

            if (mFragment != null) {
                mFragment.getFirstData(BitListFragment.LoadType.CHANGE_PAGE);
            }

            dismiss();
        });
    }

    @Override
    public void onClick(View v) {
        dismiss();
    }
}
