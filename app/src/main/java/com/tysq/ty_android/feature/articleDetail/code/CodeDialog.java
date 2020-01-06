package com.tysq.ty_android.feature.articleDetail.code;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.abc.lib_utils.toast.ToastUtils;
import com.tysq.ty_android.R;
import com.tysq.ty_android.base.CommonBaseDialog;
import com.tysq.ty_android.feature.articleDetail.activity.ArticleDetailActivity;
import com.tysq.ty_android.utils.TyUtils;

/**
 * author       : frog
 * time         : 2019-08-12 16:49
 * desc         : 验证码弹框
 * version      : 1.3.0
 */
public class CodeDialog extends CommonBaseDialog {

    public static final int CODE_WIDTH = 300;
    public static final int CODE_HEIGHT = 300;

    private ImageView ivCode;
    private TextView tvConfirm;
    private ImageView ivClose;
    private ImageView ivRefresh;
    private EditText etCode;

    private ObjectAnimator mRefreshAnim;

    private Bitmap mCodeBitmap;

    private boolean mIsLoading = true;

    public static CodeDialog newInstance() {

        Bundle args = new Bundle();

        CodeDialog fragment = new CodeDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initArgs(Bundle arguments) {

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.dialog_article_code_validate;
    }

    @Override
    protected int obtainWidth() {
        return dp2px(300);
    }

    @Override
    protected int obtainHeight() {
        return dp2px(490);
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

        mRefreshAnim = ObjectAnimator.ofFloat(ivRefresh, "rotation", 0, 360);
        mRefreshAnim.setDuration(2_000);
        mRefreshAnim.setInterpolator(new LinearInterpolator());
        mRefreshAnim.setRepeatCount(ValueAnimator.INFINITE);

        ivCode = view.findViewById(R.id.iv_code);
        tvConfirm = view.findViewById(R.id.tv_confirm);
        ivClose = view.findViewById(R.id.iv_close);
        ivRefresh = view.findViewById(R.id.iv_refresh);
        etCode = view.findViewById(R.id.et_code);

        mIsLoading = false;

        ivClose.setOnClickListener(v -> {
            dismiss();
            if (getActivity() != null) {
                getActivity().finish();
            }
        });

        tvConfirm.setOnClickListener(v -> {
            if (TextUtils.isEmpty(getCode())) {
                ToastUtils.show(getString(R.string.article_code));
                return;
            }

            if (getActivity() instanceof ArticleDetailActivity) {
                ArticleDetailActivity activity = (ArticleDetailActivity) getActivity();
                activity.validateCode(getCode());
            }
        });

        ivRefresh.setOnClickListener(v -> {

            if (mIsLoading) {
                return;
            }
            mIsLoading = true;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mRefreshAnim.start();
            }

            if (getActivity() instanceof ArticleDetailActivity) {
                ArticleDetailActivity activity = (ArticleDetailActivity) getActivity();
                activity.loadCode();
            }

        });
    }

    /**
     * 设置 base64 验证码
     *
     * @param base64
     */
    public void setCodeInfo(String base64) {

        if (mCodeBitmap != null) {
            mCodeBitmap.recycle();
        }

        byte[] bitmapArray = Base64.decode(TyUtils.clipBase64(base64), Base64.DEFAULT);
        mCodeBitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);

        ivCode.setImageBitmap(mCodeBitmap);

        loadOver();
    }

    public void loadOver() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (mRefreshAnim != null && mRefreshAnim.isRunning()) {
                mRefreshAnim.cancel();
            }
        }
        mIsLoading = false;

    }

    public String getCode() {
        return etCode.getText().toString().trim();
    }

}
