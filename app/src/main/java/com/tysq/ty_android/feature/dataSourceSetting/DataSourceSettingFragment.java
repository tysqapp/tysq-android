package com.tysq.ty_android.feature.dataSourceSetting;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abc.lib_utils.toast.ToastUtils;
import com.bit.view.fragment.BitBaseFragment;
import com.tysq.ty_android.R;
import com.tysq.ty_android.app.TyApplication;
import com.tysq.ty_android.feature.dataSourceSetting.di.DaggerDataSourceSettingComponent;
import com.tysq.ty_android.feature.dataSourceSetting.di.DataSourceSettingModule;
import com.tysq.ty_android.local.sp.NetCache;
import com.tysq.ty_android.utils.DBManager.DBLocalDataSourceManager;
import com.tysq.ty_android.utils.TyUtils;

import java.util.List;

import butterknife.BindView;
import db.LocalHistoryModel;
import exception.ParserUrlException;

/**
 * author       : frog
 * time         : 2019-08-14 16:10
 * desc         : 设置数据源
 * version      : 1.3.0
 */

public final class DataSourceSettingFragment
        extends BitBaseFragment<DataSourceSettingPresenter>
        implements DataSourceSettingView {


    @BindView(R.id.et_data_source)
    EditText etDataSource;
    @BindView(R.id.iv_clear)
    ImageView ivClear;
    @BindView(R.id.ll_history)
    LinearLayout llHistory;

    private boolean isRequest = false;

    public static DataSourceSettingFragment newInstance() {

        Bundle args = new Bundle();

        DataSourceSettingFragment fragment = new DataSourceSettingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected View onCreateFragmentView(LayoutInflater inflater,
                                        ViewGroup container,
                                        Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_data_source_setting, container, false);
    }

    @Override
    protected void initView(View view) {
        ivClear.setOnClickListener(v -> {
            etDataSource.setText("");
            ivClear.setVisibility(View.GONE);
        });

        etDataSource.setText(NetCache.getDefault().getDomain());
        etDataSource.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    refreshHistory(llHistory);
                }
            }
        });
        etDataSource.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (etDataSource.getText().toString().trim().length() <= 0) {
                    ivClear.setVisibility(View.GONE);
                } else {
                    ivClear.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    @Override
    protected void registerDagger() {
        DaggerDataSourceSettingComponent.builder()
                .appComponent(TyApplication.getAppComponent())
                .dataSourceSettingModule(new DataSourceSettingModule(this))
                .build()
                .inject(this);
    }

    /**
     * 是否完成
     */
    public void isCommit() {
        String dataSource = etDataSource.getText().toString().trim();

        if (TextUtils.isEmpty(dataSource)) {
            ToastUtils.show(getString(R.string.data_source_enter));
            return;
        }

        try {
            dataSource = TyUtils.getRealDataSource(dataSource);
        } catch (ParserUrlException e) {
            e.printStackTrace();
            ToastUtils.show(getString(R.string.data_source_please_enter_correct));
            return;
        }

        DBLocalDataSourceManager.addLocalHistory(dataSource);
        refreshHistory(llHistory);
        if (NetCache.getDefault().getDomain().equals(dataSource)) {
            ToastUtils.show(String.format(getString(R.string.data_source_current), dataSource));
            return;
        }

        if (isRequest) {
            logi("正在提交");
            ToastUtils.show(getString(R.string.data_source_checking));
            return;
        }

        isRequest = true;

        showDialog();

        try {
            mPresenter.checkDataSource(dataSource);
        } catch (Exception e) {
            e.printStackTrace();
            onCheckDataSourceError();
        }

    }

    @Override
    public void onCheckDataSourceError() {
        isRequest = false;
        hideDialog();
        ToastUtils.show(getString(R.string.data_source_error));
    }

    @Override
    public void onCheckDataSource() {
        isRequest = false;
        ToastUtils.show(getString(R.string.data_source_success));
        if (getActivity() instanceof DataSourceSettingActivity) {
            DataSourceSettingActivity activity = (DataSourceSettingActivity) getActivity();
            activity.closeAll();
        }
    }

    private void refreshHistory(LinearLayout linearLayout) {
        linearLayout.removeAllViews();
        List<LocalHistoryModel> list = DBLocalDataSourceManager.queryHistoryList();
        addHistoryToWindow(linearLayout, list);
    }

    private void addHistoryToWindow(final LinearLayout linearLayout,
                                    final List<LocalHistoryModel> list) {
        for (int i = 0; i < list.size(); i++) {
            final View itemView = getLayoutInflater().inflate(R.layout.item_data_source_history, null);
            TextView tvHistory = (TextView) itemView.findViewById(R.id.tv_history);
            RelativeLayout rlCancel = (RelativeLayout) itemView.findViewById(R.id.rl_cancel);

            int index = i;
            tvHistory.setText(list.get(i).url);
            tvHistory.setOnClickListener(view -> etDataSource.setText(tvHistory.getText().toString()));

            rlCancel.setOnClickListener(view -> {
                linearLayout.removeView(itemView);
                DBLocalDataSourceManager.deleteLocalHistory(list.get(index));
            });

            linearLayout.addView(itemView);
        }
    }
}
