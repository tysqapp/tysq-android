package com.tysq.ty_android.feature.articleDetail.dialog;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.abc.lib_utils.toast.ToastUtils;
import com.tysq.ty_android.R;
import com.tysq.ty_android.base.CommonBaseDialog;
import com.tysq.ty_android.feature.articleDetail.activity.ArticleDetailActivity;

import butterknife.BindView;

/**
 * author       : frog
 * time         : 2019/5/29 上午10:55
 * desc         : 审核
 * version      : 1.0.0
 */
public class ExaminationArticleDialog
        extends CommonBaseDialog
        implements View.OnClickListener {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.ll_pass)
    LinearLayout llPass;
    @BindView(R.id.cb_pass)
    CheckBox cbPass;
    @BindView(R.id.tv_pass)
    TextView tvPass;
    @BindView(R.id.ll_refuse)
    LinearLayout llRefuse;
    @BindView(R.id.cb_refuse)
    CheckBox cbRefuse;
    @BindView(R.id.tv_refuse)
    TextView tvRefuse;
    @BindView(R.id.iv_exam_pass_tip)
    ImageView ivExamPassTip;
    @BindView(R.id.tv_exam_pass_tip)
    TextView tvExamPassTip;
    @BindView(R.id.tv_confirm)
    TextView tvConfirm;
    @BindView(R.id.et_refuse_reason)
    EditText etRefuseReason;
    @BindView(R.id.iv_close)
    ImageView ivClose;

    // 是否通过，默认通过
    private boolean isPass = true;

    private int selectColor;
    private int unSelectColor;

    public static ExaminationArticleDialog newInstance() {

        Bundle args = new Bundle();

        ExaminationArticleDialog fragment = new ExaminationArticleDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initArgs(Bundle arguments) {
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.dialog_article_examination;
    }

    @Override
    protected int obtainWidth() {
        return dp2px(300);
    }

    @Override
    protected int obtainHeight() {
        return dp2px(380);
    }

    @Override
    protected int obtainGravity() {
        return Gravity.CENTER;
    }

    @Override
    protected boolean getCancelable() {
        return false;
    }

    @Override
    protected void initView(View view) {
        selectColor = ContextCompat.getColor(getContext(), R.color.main_blue_color);
        unSelectColor = ContextCompat.getColor(getContext(), R.color.des_text_color);

        controlView();

        llPass.setOnClickListener(this);
        llRefuse.setOnClickListener(this);
        tvConfirm.setOnClickListener(this);
        ivClose.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_pass:
                isPass = true;
                controlView();
                break;
            case R.id.ll_refuse:
                isPass = false;
                controlView();
                break;
            case R.id.tv_confirm:
                handleConfirm();
                break;
            case R.id.iv_close:
                dismiss();
                break;
        }
    }

    private void handleConfirm() {
        String reason = "";
        if (!isPass) {
            reason = etRefuseReason.getText().toString().trim();
            if (TextUtils.isEmpty(reason)) {
                ToastUtils.show(getString(R.string.article_detail_exam_refuse_not_empty));
                return;
            }
        }

        FragmentActivity activity = getActivity();
        if (activity == null) {
            dismiss();
            return;
        }

        if (activity instanceof ArticleDetailActivity) {
            ArticleDetailActivity articleDetailActivity = (ArticleDetailActivity) activity;
            articleDetailActivity.commitExamination(isPass, reason);
        }
    }

    private void controlView() {
        if (isPass) {
            tvPass.setTextColor(selectColor);
            tvRefuse.setTextColor(unSelectColor);

            cbPass.setChecked(true);
            cbRefuse.setChecked(false);

            ivExamPassTip.setVisibility(View.VISIBLE);
            tvExamPassTip.setVisibility(View.VISIBLE);
            etRefuseReason.setVisibility(View.GONE);
        } else {
            tvPass.setTextColor(unSelectColor);
            tvRefuse.setTextColor(selectColor);

            cbPass.setChecked(false);
            cbRefuse.setChecked(true);

            ivExamPassTip.setVisibility(View.GONE);
            tvExamPassTip.setVisibility(View.GONE);
            etRefuseReason.setVisibility(View.VISIBLE);
        }
    }

}
