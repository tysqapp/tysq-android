package com.tysq.ty_android.feature.cloud.cloudUploading.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.bit.view.fragment.dialog.BitBaseDialogFragment;
import com.tysq.ty_android.R;
import com.tysq.ty_android.base.CommonBaseDialog;

/**
 * author       : frog
 * time         : 2019/5/29 上午10:55
 * desc         : 文件类型选择
 * version      : 1.3.0
 */
public class FileTypeDialog
        extends CommonBaseDialog
        implements View.OnClickListener {

    public static final int PIC = 1;
    public static final int VIDEO = 2;
    public static final int AUDIO = 3;
    public static final int OTHER = 4;

    TextView tvPic;
    TextView tvVideo;
    TextView tvAudio;
    TextView tvOther;
    TextView tvCancel;

    private FileTypeListener mListener;

    public static FileTypeDialog newInstance() {
        Bundle args = new Bundle();
        FileTypeDialog fragment = new FileTypeDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initArgs(Bundle arguments) {

    }

    public void setListener(FileTypeListener listener) {
        this.mListener = listener;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.dialog_file_type;
    }

    @Override
    protected int obtainWidth() {
        return ViewGroup.LayoutParams.MATCH_PARENT;
    }

    @Override
    protected int obtainHeight() {
        return ViewGroup.LayoutParams.WRAP_CONTENT;
    }

    @Override
    protected int obtainGravity() {
        return Gravity.BOTTOM;
    }

    @Override
    protected void initView(View view) {

        tvPic = view.findViewById(R.id.tv_pic);
        tvVideo = view.findViewById(R.id.tv_video);
        tvAudio = view.findViewById(R.id.tv_audio);
        tvOther = view.findViewById(R.id.tv_other);
        tvCancel = view.findViewById(R.id.tv_cancel);

        tvPic.setOnClickListener(this);
        tvVideo.setOnClickListener(this);
        tvAudio.setOnClickListener(this);
        tvOther.setOnClickListener(this);
        tvCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (mListener == null) {
            dismiss();
            return;
        }
        switch (v.getId()) {
            case R.id.tv_pic:
                mListener.onModifyArticle(PIC);
                break;
            case R.id.tv_video:
                mListener.onModifyArticle(VIDEO);
                break;
            case R.id.tv_audio:
                mListener.onModifyArticle(AUDIO);
                break;
            case R.id.tv_other:
                mListener.onModifyArticle(OTHER);
                break;
            case R.id.tv_cancel:
                break;
        }
        dismiss();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mListener = null;
    }

    public interface FileTypeListener {
        void onModifyArticle(int type);
    }

}
