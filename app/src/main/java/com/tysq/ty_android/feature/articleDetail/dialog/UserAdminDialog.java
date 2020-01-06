package com.tysq.ty_android.feature.articleDetail.dialog;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tysq.ty_android.R;
import com.tysq.ty_android.base.CommonBaseDialog;
import com.tysq.ty_android.feature.forbidReview.ForbidReviewActivity;
import com.tysq.ty_android.utils.TyUtils;

import butterknife.BindView;

/**
 * author       : frog
 * time         : 2019-08-29 11:02
 * desc         : 用户权限弹框
 * version      : 1.0.0
 */
public class UserAdminDialog extends CommonBaseDialog implements View.OnClickListener {

    @BindView(R.id.iv_close)
    ImageView ivClose;
    @BindView(R.id.iv_user_photo)
    ImageView ivUserPhoto;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.iv_lv)
    ImageView ivLv;
    @BindView(R.id.tv_forbid_comment)
    TextView tvForbidComment;

    private int userId;
    private String photoUrl;
    private String userName;
    private int userLv;

    public static UserAdminDialog newInstance() {

        Bundle args = new Bundle();

        UserAdminDialog fragment = new UserAdminDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.dialog_user_admin;
    }

    @Override
    protected int obtainWidth() {
        return dp2px(250);
    }

    @Override
    protected int obtainHeight() {
        return dp2px(175);
    }

    @Override
    protected int obtainGravity() {
        return Gravity.CENTER;
    }

    @Override
    protected void initArgs(Bundle arguments) {

    }

    public UserAdminDialog setInfo(int userId,
                                   String photoUrl,
                                   String userName,
                                   int userLv) {
        this.userId = userId;
        this.photoUrl = photoUrl;
        this.userName = userName;
        this.userLv = userLv;
        return this;
    }

    @Override
    protected void initView(View view) {
        ivClose.setOnClickListener(this);
        tvForbidComment.setOnClickListener(this);

        TyUtils.initUserPhoto(
                this,
                getContext(),
                photoUrl,
                ivUserPhoto);

        tvName.setText(userName);

        Integer gradeResource = TyUtils.getLvIcon(userLv);
        ivLv.setImageDrawable(ContextCompat.getDrawable(getContext(), gradeResource));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_close:
                dismiss();
                break;
            case R.id.tv_forbid_comment:
                ForbidReviewActivity.startActivity(getContext(),
                        userName,
                        userId);
                dismiss();
                break;
        }
    }
}
