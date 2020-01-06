package com.tysq.ty_android.feature.forbidReview;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.abc.lib_utils.toast.ToastUtils;
import com.bit.view.fragment.BitBaseFragment;
import com.tysq.ty_android.R;
import com.tysq.ty_android.app.TyApplication;
import com.tysq.ty_android.feature.forbidReview.di.DaggerForbidReviewComponent;
import com.tysq.ty_android.feature.forbidReview.di.ForbidReviewModule;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import request.ForbidCommentReq;
import response.forbid.ForbidReviewBean;
import response.forbid.ForbidReviewResp;

/**
 * author       : liaozhenlin
 * time         : 2019-8-27 10:07
 * desc         :禁止评论
 * version      : 1.0.0
 */
public final class ForbidReviewFragment extends BitBaseFragment<ForbidReviewPresenter>
        implements ForbidReviewView,
        View.OnClickListener{

      public static final String TAG = "ForbidReviewFragment";
      public static final String ACCOUNT_NAME = "account_name";
      public static final String ACCOUNT_ID = "account_id";

      private ForBidReviewAdapter adapter;
      private ExpandableListView elv;
      @BindView(R.id.tv_confirm)
      TextView tvConfirm;
      @BindView(R.id.iv_back)
      ImageView ivBack;

      private ArrayList<ForbidReviewBean> mCategoryData = new ArrayList<>();
      private String accountName;
      private int accountId;

      public static ForbidReviewFragment newInstance(String account, int accountId){
            Bundle args = new Bundle();

            args.putString(ACCOUNT_NAME, account);
            args.putInt(ACCOUNT_ID, accountId);

            ForbidReviewFragment fragment = new ForbidReviewFragment();
            fragment.setArguments(args);
            return fragment;
      }

      @Override
      protected void initArgs(Bundle arguments) {
            accountName = arguments.getString(ACCOUNT_NAME);
            accountId = arguments.getInt(ACCOUNT_ID);
      }

      @Override
      protected View onCreateFragmentView(LayoutInflater inflater, ViewGroup container,
                                          Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_forbid_review, container, false);
      }

      @Override
      protected void initView(View view) {
            loadData();

            TextView tvName = view.findViewById(R.id.tv_name);
            elv = view.findViewById(R.id.elv);

            String title = String.format(getString(R.string.forbid_review_label_content), accountName);
            tvName.setText(title);

            tvConfirm.setOnClickListener(this);
            ivBack.setOnClickListener(this);
            elv.setGroupIndicator(null);

            adapter = new ForBidReviewAdapter(getContext());
      }

      @Override
      protected void registerDagger() {
            DaggerForbidReviewComponent.builder()
                    .appComponent(TyApplication.getAppComponent())
                    .forbidReviewModule(new ForbidReviewModule(this))
                    .build()
                    .inject(this);
      }


      private void loadData(){
            mPresenter.getForbidCategory(accountId);
      }

      @Override
      public void onLoadForbidCategoryFailure() {
            ToastUtils.show(getString(R.string.forbid_list_error));
      }

      @Override
      public void onPostForbidComment() { }

      @Override
      public void onGetForbidReview(ForbidReviewResp forbidReviewResp) {
            mCategoryData.clear();
            mCategoryData.addAll(forbidReviewResp.getForbidReviewBeanList());
            adapter.setData(mCategoryData);
            elv.setAdapter(adapter);
      }

      @Override
      public void onClick(View view) {
            switch (view.getId()){
                  case R.id.tv_confirm:
                        postForbidComment();
                        break;
                  case R.id.iv_back:
                        getActivity().finish();
            }
      }

      /**
       * 提交禁止评论情况
       */
      private void postForbidComment() {
            List<ForbidCommentReq> value = adapter.queryCategoryId();
            mPresenter.postForbidCategory(accountId, value);
            getActivity().finish();

      }


}
