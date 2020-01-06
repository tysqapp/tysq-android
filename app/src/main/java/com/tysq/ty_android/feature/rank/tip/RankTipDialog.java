package com.tysq.ty_android.feature.rank.tip;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tysq.ty_android.R;
import com.tysq.ty_android.base.CommonBaseDialog;
import com.tysq.ty_android.config.Constant;
import com.tysq.ty_android.feature.web.TyWebViewActivity;

/**
 * author       : frog
 * time         : 2019-07-17 11:47
 * desc         : 积分提示弹框
 * version      : 1.3.0
 */
public class RankTipDialog extends CommonBaseDialog {

    private static final String LIMIT_SCORE = "limitScore";
    private static final String TYPE = "type";

    private ImageView ivTopBg;
    private TextView tvTip;
    private TextView tvExchange;
    private ImageView ivClose;
    private TextView tvHowToObtain;

    private int limitScore;
    private String type;

    public static RankTipDialog newInstance(int limitScore, String type) {

        Bundle args = new Bundle();
        args.putInt(LIMIT_SCORE, limitScore);
        args.putString(TYPE, type);

        RankTipDialog fragment = new RankTipDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initArgs(Bundle arguments) {
        limitScore = arguments.getInt(LIMIT_SCORE);
        type = arguments.getString(TYPE);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.dialog_rank_tip;
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

        ivTopBg = view.findViewById(R.id.iv_top_bg);
        tvTip = view.findViewById(R.id.tv_tip);
        tvExchange = view.findViewById(R.id.tv_exchange);
        ivClose = view.findViewById(R.id.iv_close);
        tvHowToObtain = view.findViewById(R.id.tv_how_to_obtain);

        ivClose.setOnClickListener(v -> {
            dismiss();
        });

        tvExchange.setOnClickListener(v -> {
            TyWebViewActivity.startActivity(getContext(),
                    Constant.HtmlAPI.EXCHANGE_URL);
            dismiss();
        });

        tvHowToObtain.setOnClickListener(v -> {
            TyWebViewActivity.startActivity(getContext(),
                    Constant.HtmlAPI.RANK_DETAIL);
            dismiss();
        });

        int contentResId;
        switch (type) {
            case Constant.JudgementType.COMMENT:
                contentResId = R.string.rank_tip_write_review;
                break;
            case Constant.JudgementType.CREATE:
                contentResId = R.string.rank_tip_write_article;
                break;
            case Constant.JudgementType.READ:
                contentResId = R.string.rank_tip_read_article;
                break;
            case Constant.JudgementType.DOWNLOAD_VIDEO:
                contentResId = R.string.rank_tip_download_video;
                break;
            default:
                contentResId = R.string.rank_tip_read_article;
                break;
        }

        String score = limitScore + "";

        String tip = String.format(getString(contentResId), limitScore);
        int scoreStartIndex = tip.indexOf(score);

        SpannableString tipSpanBuilder = new SpannableString(tip);
        ForegroundColorSpan span = new ForegroundColorSpan(ContextCompat.getColor(getContext(), R.color.red));
        tipSpanBuilder.setSpan(span, scoreStartIndex,
                scoreStartIndex + score.length() + 2,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        tvTip.setText(tipSpanBuilder);

    }

}
