package com.tysq.ty_android.feature.articleDetail.tip;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.abc.lib_utils.toast.ToastUtils;
import com.tysq.ty_android.R;
import com.tysq.ty_android.base.CommonBaseDialog;
/**
 * author       : liaozhenlin
 * time         : 2019/11/5 17:13
 * desc         : 置顶文章的弹窗
 * version      : 1.5.0
 */
public class TopArticleTipDialog extends CommonBaseDialog {

    public static final String TAG = "TopArticleTipDialog";
    public static final String TOP_POSITION = "TOP_POSITION";
    private EditText etTopArticleSort;
    private TextView tvConfirm;
    private TextView tvCancel;
    private int mTopPosition;

    public static TopArticleTipDialog newInstance(int topPosition){

        Bundle args = new Bundle();
        args.putInt(TOP_POSITION, topPosition);

        TopArticleTipDialog fragment = new TopArticleTipDialog();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    protected int getLayoutResource() {
        return R.layout.dialog_top_article_sort;
    }

    @Override
    protected int obtainWidth() {
        return dp2px(300);
    }

    @Override
    protected int obtainHeight() {
        return dp2px(250);
    }

    @Override
    protected int obtainGravity() {
        return Gravity.CENTER;
    }


    @Override
    protected void initArgs(Bundle arguments) {
        mTopPosition = arguments.getInt(TOP_POSITION);
    }

    @Override
    protected void initView(View view) {
        etTopArticleSort = view.findViewById(R.id.et_top_article_sort);
        tvConfirm = view.findViewById(R.id.tv_confirm);
        tvCancel = view.findViewById(R.id.tv_cancel);

        tvCancel.setOnClickListener(v -> dismiss());

        PostTopArticleListener listener = (PostTopArticleListener) getActivity();
        tvConfirm.setOnClickListener(v -> {
            try {

                int position = Integer.parseInt(etTopArticleSort.getText().toString());

                listener.onPutTopArticle(position);
                dismiss();
            }catch (NumberFormatException e){

                e.printStackTrace();
                ToastUtils.show(getString(R.string.top_article_tip_dialog_integer));

            }
        });

        etTopArticleSort.setText(String.valueOf(mTopPosition));
    }

    public interface PostTopArticleListener{
        void onPutTopArticle(int position);
    }
}
