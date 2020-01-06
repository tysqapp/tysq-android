package com.tysq.ty_android.feature.invite;

import android.Manifest;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.abc.lib_utils.toast.ToastUtils;
import com.bit.utils.UIUtils;
import com.bit.utils.code.ZxingUtils;
import com.tysq.ty_android.R;
import com.tysq.ty_android.base.CommonBaseDialog;
import com.tysq.ty_android.utils.TyFileUtils;
import com.zinc.libpermission.annotation.Permission;
import com.zinc.libpermission.annotation.PermissionCanceled;
import com.zinc.libpermission.annotation.PermissionDenied;
import com.zinc.libpermission.bean.CancelInfo;
import com.zinc.libpermission.bean.DenyInfo;
import com.zinc.libpermission.utils.JPermissionUtil;

import java.io.File;

/**
 * author       : frog
 * time         : 2019-07-17 11:47
 * desc         : 二维码提示弹框
 * version      : 1.3.0
 */
public class InviteDialog extends CommonBaseDialog {

    private static final String QR_CODE_CONTENT = "QR_CODE_CONTENT";

    private ImageView ivQr;
    private TextView tvSave;
    private ImageView ivClose;

    private String qrCodeContent;
    private Bitmap qrCodeBitmap;

    public static InviteDialog newInstance(String qrCodeContent) {

        Bundle args = new Bundle();
        args.putString(QR_CODE_CONTENT, qrCodeContent);

        InviteDialog fragment = new InviteDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initArgs(Bundle arguments) {
        qrCodeContent = arguments.getString(QR_CODE_CONTENT);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.dialog_qr_code;
    }

    @Override
    protected int obtainWidth() {
        return dp2px(255);
    }

    @Override
    protected int obtainHeight() {
        return ViewGroup.LayoutParams.WRAP_CONTENT;
    }

    @Override
    protected int obtainGravity() {
        return Gravity.CENTER;
    }

    @Override
    protected void initView(View view) {

        ivQr = view.findViewById(R.id.iv_qr);
        tvSave = view.findViewById(R.id.tv_save);
        ivClose = view.findViewById(R.id.iv_close);

        ivClose.setOnClickListener(v -> {
            dismiss();
        });

        int width = UIUtils.dip2px(getContext(), 190);

        qrCodeBitmap = ZxingUtils.createBitmap(qrCodeContent, width, width);

        tvSave.setOnClickListener(v -> {
            savePic();
        });

        ivQr.setImageBitmap(qrCodeBitmap);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        qrCodeBitmap.recycle();
    }

    @Permission(value = {Manifest.permission.WRITE_EXTERNAL_STORAGE}, requestCode = 200)
    private void savePic() {
        String path = TyFileUtils.saveBitmap(qrCodeBitmap);

        TyFileUtils.updateImageInSystem(getContext(), new File(path));

        if (TextUtils.isEmpty(path)) {
            ToastUtils.show(getString(R.string.invite_qr_failure));
        } else {
            ToastUtils.show(String.format(getString(R.string.invite_qr_suc), path));
        }
    }

    @PermissionCanceled(requestCode = 200)
    private void cancel(CancelInfo cancelInfo) {
        ToastUtils.show(getString(R.string.invite_need_save));
    }

    @PermissionDenied(requestCode = 200)
    private void deny(DenyInfo denyInfo) {
        ToastUtils.show(getString(R.string.invite_need_save));
        JPermissionUtil.goToMenu(getContext());
    }

}
