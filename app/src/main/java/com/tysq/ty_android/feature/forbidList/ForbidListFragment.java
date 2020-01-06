package com.tysq.ty_android.feature.forbidList;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bit.adapter.BitFrameAdapter;
import com.bit.callback.StateViewHolderListener;
import com.tysq.ty_android.R;
import com.tysq.ty_android.app.TyApplication;
import com.tysq.ty_android.base.SimpleLoadMoreFragment;
import com.tysq.ty_android.feature.forbidList.di.ForbidListModule;
import com.tysq.ty_android.feature.forbidList.di.DaggerForbidListComponent;

import java.lang.Override;
import java.util.List;

import butterknife.BindView;
import response.forbidlist.ForbidCommentResp;

/**
 * author       : liaozhenlin
 * time         : 2019/8/28 14:04
 * desc         : 禁止评论列表
 * version      : 1.0.0
 */
public final class ForbidListFragment extends
        SimpleLoadMoreFragment<ForbidListPresenter, ForbidCommentResp.ForbidCommentBean>
        implements ForbidListView, StateViewHolderListener {

    public static final String TAG = "ForbidListFragment";

    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.ll_cancel)
    LinearLayout llCancel;

    private ForbidListAdapter adapter;

    private String content = "";
    private int start;
    private int size;
    private boolean isFirst;

    public static ForbidListFragment newInstance(){

        Bundle args = new Bundle();

        ForbidListFragment fragment = new ForbidListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected View onCreateFragmentView(LayoutInflater inflater,
                                        @Nullable ViewGroup container,
                                        @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_forbid_review_list, container, false);
    }

    @Override
    protected void initView(View view) {
        super.initView(view);

        mBaseAdapter.setStateViewHolderListener(this);
        //去除焦点，让editText一进来不会弹出输入框
        etSearch.clearFocus();

        llCancel.setOnClickListener(new CancelListener());
        etSearch.setOnEditorActionListener(new EnterListener());
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().isEmpty()){
                    llCancel.setVisibility(View.VISIBLE);
                }else{
                    llCancel.setVisibility(View.GONE);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        adapter = new ForbidListAdapter(getContext(), mData);
        return adapter;
    }

    @Override
    protected void registerDagger() {
          DaggerForbidListComponent.builder()
          .appComponent(TyApplication.getAppComponent())
          .forbidListModule(new ForbidListModule(this))
          .build()
          .inject(this);
    }

    @Override
    protected void loadData(int start, int pageSize, boolean isFirst) {
        this.start = start;
        this.size = pageSize;
        this.isFirst = isFirst;
        mPresenter.getForbidList(start, pageSize, content, isFirst);
    }

    @Override
    public int getTitleId() {
        return R.string.forbid_list_title;
    }

    @Override
    public void onGetForbidListError(boolean isFirst) {
        onHandleError(isFirst);
    }

    @Override
    public void onGetForbidList(List<ForbidCommentResp.ForbidCommentBean> forbidCommentList,
                                boolean isFirst) {
        onHandleResponseData(forbidCommentList, isFirst);
    }

    @Override
    protected boolean isNeedEmpty() {
        return true;
    }

    @Override
    protected boolean requestRefresh() { return false; }

    @Override
    protected int getEmptyView() {
        return R.layout.blank_empty_data_log;
    }

    @Override
    public void handleEmptyViewHolder(BitFrameAdapter.EmptyViewHolder holder) {
        TextView tvTip = holder.itemView.findViewById(R.id.tv_tip);
        ImageView ivBlank = holder.itemView.findViewById(R.id.iv_blank);

        if (TextUtils.isEmpty(content)) {
            tvTip.setText(getString(R.string.blank_empty_data_log));
        } else {
            tvTip.setText(getString(R.string.blank_empty_search_log));
            ivBlank.setImageResource(R.drawable.ic_search_blank);
        }
    }

    @Override
    public void handleRetryViewHolder(BitFrameAdapter.RetryViewHolder holder) {

    }

    @Override
    public void handleLoadingViewHolder(BitFrameAdapter.LoadingViewHolder holder) {

    }


    /**
     * 监听软键盘的搜索键
     */
    class EnterListener implements TextView.OnEditorActionListener{

        @Override
        public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
            if (i == EditorInfo.IME_ACTION_SEARCH){
                content = etSearch.getText().toString();
                mPresenter.getForbidList(start, size, content, isFirst);
                //隐藏软键盘
                InputMethodManager im = (InputMethodManager)getContext()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);

                im.hideSoftInputFromWindow(etSearch.getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
            return false;
        }
    }

    /**
     * 监听搜索框的取消键
     */
    class CancelListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            etSearch.setText("");
            content = "";
            mPresenter.getForbidList(start, size, content, isFirst);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData(start,size,isFirst);
    }
}
