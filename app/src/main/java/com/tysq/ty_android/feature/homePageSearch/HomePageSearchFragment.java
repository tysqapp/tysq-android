package com.tysq.ty_android.feature.homePageSearch;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bit.view.fragment.BitLoadMoreFragment;
import com.tysq.ty_android.R;
import com.tysq.ty_android.app.TyApplication;
import com.tysq.ty_android.config.Constant;
import com.tysq.ty_android.feature.homePageSearch.adapter.HomePageSearchAdapter;
import com.tysq.ty_android.feature.homePageSearch.di.DaggerHomePageSearchComponent;
import com.tysq.ty_android.feature.homePageSearch.di.HomePageSearchModule;
import com.tysq.ty_android.feature.homePageSearch.listener.OnClickLocalLabel;
import com.tysq.ty_android.feature.web.TyWebViewFragment;
import com.tysq.ty_android.utils.DBManager.DBLocalSearchManager;
import com.tysq.ty_android.utils.ScreenAdapterUtils;
import com.tysq.ty_android.utils.TyUtils;
import com.zinc.jrecycleview.adapter.JRefreshAndLoadMoreAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import response.common.TitleCountVO;
import vo.search.HomePageSearchVO;

/**
 * author       : liaozhenlin
 * time         : 2019/10/18 16:49
 * desc         : 首页搜索
 * version      : 1.5.0
 */
public final class HomePageSearchFragment
        extends BitLoadMoreFragment<HomePageSearchPresenter>
        implements HomePageSearchView,
        View.OnClickListener,
        TextView.OnEditorActionListener,
        OnClickLocalLabel {

    @BindView(R.id.ll_tag)
    LinearLayout llTag;
    @BindView(R.id.tv_type)
    TextView tvType;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_search)
    TextView tvSearch;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.ic_cancel)
    ImageView icCancel;
    @BindView(R.id.web_advertisement)
    FrameLayout webAdvertisement;

    private static final int LOAD_SIZE = 20;
    private final TitleCountVO mTitleCountVO = new TitleCountVO();
    private final ArrayList<HomePageSearchVO> mData = new ArrayList<>();

    private int start = 0;
    private String type;
    private PopupWindow mTypeWindow;
    private HomePageSearchAdapter homePageSearchAdapter;

    private static final int mScreenHeight = (int) (ScreenAdapterUtils.getScreenWidth() / Constant.MULTIPLE);

    public static HomePageSearchFragment newInstance() {
        Bundle args = new Bundle();

        HomePageSearchFragment fragment = new HomePageSearchFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected View onCreateFragmentView(LayoutInflater inflater, ViewGroup container,
                                        Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_page_search, container, false);
        return view;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        type = Constant.HomePageSearchType.TYPE_ARTICLE;
        mBaseAdapter.setIsOpenLoadMore(false);

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) webAdvertisement.getLayoutParams();
        layoutParams.width = ScreenAdapterUtils.getScreenWidth();
        layoutParams.height = mScreenHeight;
        webAdvertisement.setLayoutParams(layoutParams);
        webAdvertisement.setVisibility(View.VISIBLE);
        addWebViewFragment(Constant.HtmlUrl.SEARCH_AD);

        llTag.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        tvSearch.setOnClickListener(this);
        icCancel.setOnClickListener(this);

        etSearch.setOnEditorActionListener(this);

        etSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    getActivity().getWindow().setSoftInputMode
                            (WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN|
                                    WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
                }
            }
        });
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (etSearch.getText().toString().trim().isEmpty()) {
                    icCancel.setVisibility(View.GONE);
                } else {
                    icCancel.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mBaseAdapter.setOnRefreshListener(new JRefreshAndLoadMoreAdapter.OnRefreshListener() {
            @Override
            public void onRefreshing() {
                mPresenter.handleTheResult(type,
                        etSearch.getText().toString(),
                        false,
                        LOAD_SIZE,
                        start);
            }
        });
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        homePageSearchAdapter = new HomePageSearchAdapter(this, getContext(), mTitleCountVO, mData, this);
        return homePageSearchAdapter;
    }

    @Override
    public void getFirstData(int type) {
        mPresenter.onFirstLoad();
    }

    @Override
    protected void registerDagger() {
        DaggerHomePageSearchComponent.builder()
                .appComponent(TyApplication.getAppComponent())
                .homePageSearchModule(new HomePageSearchModule(this))
                .build()
                .inject(this);
    }


    //动态添加webview的fragment
    private void addWebViewFragment(String url) {
        TyWebViewFragment fragment = TyWebViewFragment.newInstance(url);
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.web_advertisement, fragment);
        transaction.commit();
    }

    //弹窗
    private void showPopupWindow() {
        View window = getLayoutInflater()
                .inflate(R.layout.fragment_home_page_search_popupwindow, null);

        window.findViewById(R.id.tv_article).setOnClickListener(this);
        window.findViewById(R.id.tv_label).setOnClickListener(this);
        window.findViewById(R.id.tv_admin).setOnClickListener(this);

        mTypeWindow = new PopupWindow(window,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true);

        mTypeWindow.setOutsideTouchable(true);
        mTypeWindow.showAsDropDown(tvType, -tvType.getWidth() / 2, tvType.getHeight() / 2);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_tag:
                showPopupWindow();
                break;
            case R.id.tv_article:
                tvType.setText(Constant.HomePageSearchLabel.LABEL_ARTICLE);
                type = Constant.HomePageSearchType.TYPE_ARTICLE;
                mTypeWindow.dismiss();
                break;
            case R.id.tv_label:
                tvType.setText(Constant.HomePageSearchLabel.LABEL_TAG);
                type = Constant.HomePageSearchType.TYPE_TAG;
                mTypeWindow.dismiss();
                break;
            case R.id.tv_admin:
                tvType.setText(Constant.HomePageSearchLabel.LABEL_ADMIN);
                type = Constant.HomePageSearchType.TYPE_ADMIN;
                mTypeWindow.dismiss();
                break;
            case R.id.iv_back:
                getActivity().finish();
                break;
            case R.id.tv_search:
                searchAndLocal();
                break;
            case R.id.ic_cancel:
                etSearch.setText("");

                break;
        }

    }

    /**
     * 进行搜索操作，并将记录保存到本地， 隐藏状态栏
     */
    private void searchAndLocal() {
        start = 0;
        if (etSearch.getText().toString().trim().length() > 0) {
            DBLocalSearchManager.addLocalHistory(etSearch.getText().toString());
        }
        mPresenter.handleTheResult(type, etSearch.getText().toString(), true, LOAD_SIZE, start);
        mBaseAdapter.onLoading();
        TyUtils.hideKeyboard(etSearch.getWindowToken(), getActivity());
    }

    @Override
    public void onLoadHomePage(List<HomePageSearchVO> resultList,
                               boolean isRefresh,
                               boolean isNeedAdvertisement,
                               int count) {

        mTitleCountVO.setCount(count);
        mData.clear();
        mData.addAll(resultList);
        //判断是否需要下拉刷新
        mBaseAdapter.setIsOpenRefresh(isRefresh);
        //组装数据长度
        start += resultList.size();
        homePageSearchAdapter.setData(mData, etSearch.getText().toString());
        mBaseAdapter.onSuccess();

        //判断是否需要广告位
        webAdvertisement.setVisibility(isNeedAdvertisement ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onFirstLoad(List<HomePageSearchVO> resultList) {
        mData.clear();
        mData.addAll(resultList);
        mBaseAdapter.setIsOpenRefresh(false);
        mBaseAdapter.onSuccess();
    }


    @Override
    public void onLoadMoreData(List<HomePageSearchVO> resultList) {
        if (resultList.size() <= 0 || resultList == null) {
            mBaseAdapter.setNoMore();
        }
        //下拉加载将数据加在第一个之上
        mData.addAll(1, resultList);
        start += resultList.size();
        mBaseAdapter.onLoading();
        homePageSearchAdapter.setData(mData, etSearch.getText().toString());
        mBaseAdapter.onSuccess();
    }

    @Override
    protected int getEmptyView() {
        return R.layout.blank_empty_data_log;
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if (i == EditorInfo.IME_ACTION_SEARCH) {
            searchAndLocal();
        }
        return true;
    }

    @Override
    protected boolean requestLoadMore() {
        return false;
    }

    @Override
    public void getLoadMoreData() {
    }

    @Override
    public void onClickLocalLabel(String label) {
        etSearch.setText(label);
        searchAndLocal();
    }

}
