package com.tysq.ty_android.feature.editArticle.labelChoose;

import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.abc.lib_utils.toast.ToastUtils;
import com.bit.utils.KeyboardUtils;
import com.bit.view.activity.BitBaseActivity;
import com.tysq.ty_android.R;
import com.tysq.ty_android.app.TyApplication;
import com.tysq.ty_android.feature.editArticle.labelChoose.di.DaggerLabelChooseComponent;
import com.tysq.ty_android.feature.editArticle.labelChoose.di.LabelChooseModule;
import com.tysq.ty_android.widget.TagFlowLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import eventbus.EditLabelChangeEvent;
import response.LabelResp;

/**
 * author       : frog
 * time         : 2019/6/5 下午2:11
 * desc         : 标签选择
 * version      : 1.3.0
 */
public final class LabelChooseActivity extends BitBaseActivity<LabelChoosePresenter>
        implements LabelChooseView, View.OnClickListener {

    private static final String SELECT_LABEL = "select_label";

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_confirm)
    TextView tvConfirm;
    @BindView(R.id.v_divider)
    View vDivider;
    @BindView(R.id.tv_added)
    TextView tvAdded;
    @BindView(R.id.tv_most)
    TextView tvMost;
    @BindView(R.id.tag_flow_select)
    TagFlowLayout tagFlowSelect;
    @BindView(R.id.v_divider_middle)
    View vDividerMiddle;
    @BindView(R.id.ll_search)
    LinearLayout llSearch;
    @BindView(R.id.tag_flow_choose)
    TagFlowLayout tagFlowChoose;
    @BindView(R.id.iv_clear)
    ImageView ivClear;
    @BindView(R.id.et_search)
    EditText etSearch;

    @BindView(R.id.iv_search_blank)
    ImageView ivSearchBlank;
    @BindView(R.id.tv_search_blank)
    TextView tvSearchBlank;

    // 选中的标签
    private final List<LabelResp.LabelItem> mSelectedLabelList = new ArrayList<>();
    private final List<View> mSelectedLabelViewList = new ArrayList<>();

    // 可选中的标签
    private final List<LabelResp.LabelItem> mData = new ArrayList<>();
    private final List<View> mLabelViewList = new ArrayList<>();

    public static void startActivity(Context context,
                                     ArrayList<LabelResp.LabelItem> labelList) {
        Intent intent = new Intent(context, LabelChooseActivity.class);

        intent.putParcelableArrayListExtra(SELECT_LABEL, labelList);

        context.startActivity(intent);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_label_choose;
    }

    @Override
    protected void initIntent(Intent intent) {
        mSelectedLabelList.addAll(intent.getParcelableArrayListExtra(SELECT_LABEL));
    }

    @Override
    protected void initView() {

        ivBack.setOnClickListener(this);
        tvConfirm.setOnClickListener(this);
        ivClear.setOnClickListener(this);

        initSelectTagFlow();

        etSearch.setOnEditorActionListener((arg0, arg1, arg2) -> {

            if (arg1 == EditorInfo.IME_ACTION_SEARCH ||
                    (arg2 != null && arg2.getKeyCode() == KeyEvent.KEYCODE_ENTER &&
                            arg2.getAction() == KeyEvent.ACTION_DOWN)) {

                KeyboardUtils.hideSoftInput(etSearch);

                load();

                return true;
            }

            return false;
        });

        etSearch.post(() -> KeyboardUtils.hideSoftInput(etSearch));

        load();

    }

    private void load() {
        mPresenter.loadLabel(getSearchContent());
    }

    @Override
    protected void registerDagger() {
        DaggerLabelChooseComponent.builder()
                .appComponent(TyApplication.getAppComponent())
                .labelChooseModule(new LabelChooseModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;

            case R.id.tv_confirm:
                EventBus.getDefault().post(new EditLabelChangeEvent(mSelectedLabelList));
                finish();
                break;

            case R.id.iv_clear:
                etSearch.setText("");
                load();
                break;
        }
    }

    /**
     * 初始化标签 为 已添加
     */
    public void initSelectTagFlow() {
        tagFlowSelect.removeAllViews();
        mSelectedLabelViewList.clear();
        for (LabelResp.LabelItem item : mSelectedLabelList) {
            createLabelForSelected(item);
        }
    }

    /**
     * 创建标签给已添加
     */
    public void createLabelForSelected(final LabelResp.LabelItem label) {
        View item = getLayoutInflater()
                .inflate(R.layout.widget_label_add, tagFlowSelect, false);

        TextView tvName = item.findViewById(R.id.tv_name);
        tvName.setText(label.getName());

        ImageView ivClose = item.findViewById(R.id.iv_close);
        ivClose.setOnClickListener(v -> {
            deleteLabelForSelected(item);
        });

        // 将自己添加进列表
        mSelectedLabelViewList.add(item);
        // 将 item 添加进流式布局
        tagFlowSelect.addView(item);
    }

    /**
     * 从已经选择的标签中删除
     */
    public void deleteLabelForSelected(View deleteView) {
        // 1、先清空视图
        tagFlowSelect.removeAllViews();

        // 2、从选择列表中删除
        int index = mSelectedLabelViewList.indexOf(deleteView);
        if (index != -1) {
            mSelectedLabelList.remove(index);
        }
        mSelectedLabelViewList.remove(deleteView);

        // 3、将视图添加
        for (View view : mSelectedLabelViewList) {
            tagFlowSelect.addView(view);
        }
    }

    /**
     * 获取搜索内容
     */
    private String getSearchContent() {
        return etSearch.getText().toString().trim();
    }

    @Override
    public void onLoadLabel(List<LabelResp.LabelItem> labelList) {

        // 清空：1、数据；2、视图；3、TagFlow中的所有视图；
        mData.clear();
        mLabelViewList.clear();
        tagFlowChoose.removeAllViews();

        // 添加数据
        mData.addAll(labelList);

        if (mData.isEmpty()) {
            tagFlowChoose.setVisibility(View.GONE);
            ivSearchBlank.setVisibility(View.VISIBLE);
            tvSearchBlank.setVisibility(View.VISIBLE);
            return;
        } else {
            tagFlowChoose.setVisibility(View.VISIBLE);
            ivSearchBlank.setVisibility(View.GONE);
            tvSearchBlank.setVisibility(View.GONE);
        }

        // 循环添加数据进：1、视图；2、TagFlow中的所有视图；
        for (LabelResp.LabelItem label : mData) {
            createLabelForChoose(label);
        }

    }

    /**
     * 创建选择 标签
     */
    private void createLabelForChoose(LabelResp.LabelItem label) {

        View item = getLayoutInflater()
                .inflate(R.layout.widget_label_choose, tagFlowChoose, false);

        TextView tvName = item.findViewById(R.id.tv_name);
        tvName.setText(label.getName());

        LinearLayout llItem = item.findViewById(R.id.ll_item);
        llItem.setOnClickListener(v -> {

            if (mSelectedLabelList.size() >= 6) {
                ToastUtils.show(getString(R.string.label_most_label));
                return;
            }

            LabelResp.LabelItem selLabel = removeLabelForChoose(v);

            for (LabelResp.LabelItem labelItem : mSelectedLabelList) {
                if (labelItem.getLabelId() == selLabel.getLabelId()) {
                    return;
                }
            }

            mSelectedLabelList.add(selLabel);
            createLabelForSelected(selLabel);

        });

        // 将自己添加进列表
        mLabelViewList.add(item);

        // 将 item 添加进流式布局
        tagFlowChoose.addView(item);

    }

    /**
     * 删除标签
     */
    public LabelResp.LabelItem removeLabelForChoose(View deleteView) {
        tagFlowChoose.removeAllViews();

        int index = mLabelViewList.indexOf(deleteView);
        LabelResp.LabelItem removeLabel = mData.remove(index);
        mLabelViewList.remove(deleteView);

        for (View view : mLabelViewList) {
            tagFlowChoose.addView(view);
        }

        return removeLabel;
    }

}
