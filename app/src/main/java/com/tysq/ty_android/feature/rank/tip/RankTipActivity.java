package com.tysq.ty_android.feature.rank.tip;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bit.view.activity.BitBaseActivity;
import com.tysq.ty_android.R;
import com.tysq.ty_android.config.Constant;
import com.tysq.ty_android.feature.web.TyWebViewActivity;

import butterknife.BindView;

/**
 * author       : frog
 * time         : 2019-07-17 15:31
 * desc         : 积分提示带背景
 * version      : 1.3.0
 */
public class RankTipActivity extends BitBaseActivity {

    private static final String LIMIT_SCORE = "limitScore";
    private static final String TYPE = "type";

    @BindView(R.id.tv_tip)
    TextView tvTip;
    @BindView(R.id.tv_exchange)
    TextView tvExchange;
    @BindView(R.id.tv_how_to_obtain)
    TextView tvHowToObtain;
    @BindView(R.id.iv_close)
    ImageView ivClose;

    private int limitScore;
    private String type;

    public static void startActivity(Context context,
                                     int limitScore,
                                     String type) {
        Intent intent = new Intent(context, RankTipActivity.class);

        intent.putExtra(LIMIT_SCORE, limitScore);
        intent.putExtra(TYPE, type);

        context.startActivity(intent);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_rank_tip;
    }

    @Override
    protected void initIntent(Intent intent) {
        limitScore = intent.getIntExtra(LIMIT_SCORE, 100);
        type = intent.getStringExtra(TYPE);
    }

    @Override
    protected void initView() {
        setStatusBarColor(R.color.rv_unselect, false, false);

        ivClose.setOnClickListener(v -> {
            finish();
        });


        int contentResId;
        switch (type) {
            case Constant.JudgementType.COMMENT:
                contentResId = R.string.rank_tip_write_review;
                setTextAndClick(R.string.rank_tip_exchange, Constant.HtmlAPI.EXCHANGE_URL, tvExchange);
                setTextAndClick(R.string.rank_tip_how_to_get, Constant.HtmlAPI.RANK_DETAIL, tvHowToObtain);
                break;
            case Constant.JudgementType.CREATE:
                contentResId = R.string.rank_tip_write_article;
                setTextAndClick(R.string.rank_tip_exchange, Constant.HtmlAPI.EXCHANGE_URL, tvExchange);
                setTextAndClick(R.string.rank_tip_how_to_get, Constant.HtmlAPI.RANK_DETAIL, tvHowToObtain);
                break;
            case Constant.JudgementType.READ:
                contentResId = R.string.rank_tip_read_article;
                setTextAndClick(R.string.rank_tip_exchange, Constant.HtmlAPI.EXCHANGE_URL, tvExchange);
                setTextAndClick(R.string.rank_tip_how_to_get, Constant.HtmlAPI.RANK_DETAIL, tvHowToObtain);
                break;
            case Constant.JudgementType.GRADE:
                contentResId = R.string.rank_tip_grade_not_enough;
                setTextAndClick(R.string.rank_tip_exchange_to_grade, Constant.HtmlAPI.EXCHANGE_URL, tvExchange);
                setTextAndClick(R.string.rank_check_grade_rule, Constant.HtmlAPI.GRAND, tvHowToObtain);
                break;

            case Constant.JudgementType.GRADE_TO_RANK:
                contentResId = R.string.rank_tip_grade_to_rank_not_enough;
                setTextAndClick(R.string.rank_tip_exchange_to_grade, Constant.HtmlAPI.EXCHANGE_URL, tvExchange);
                setTextAndClick(R.string.rank_check_grade_rule, Constant.HtmlAPI.GRAND, tvHowToObtain);
                break;
            default:
                contentResId = R.string.rank_tip_read_article;
                setTextAndClick(R.string.rank_tip_exchange, Constant.HtmlAPI.EXCHANGE_URL, tvExchange);
                setTextAndClick(R.string.rank_tip_how_to_get, Constant.HtmlAPI.RANK_DETAIL, tvHowToObtain);
                break;
        }

        if (type.equals(Constant.JudgementType.GRADE)){
            Integer gradeResource = Constant.LV_MAP_STRING.get(limitScore);
            String score = String.format(getString(gradeResource));

            String tip = String.format(getString(contentResId), score);

            AssemBleText(score, tip, true);
        } else if (type.equals(Constant.JudgementType.GRADE_TO_RANK)){
            String score = limitScore + "";

            String tip = String.format(getString(contentResId), limitScore);

            AssemBleTextRank(score, tip);
        }
        else {
            String score = limitScore + "";

            String tip = String.format(getString(contentResId), limitScore);

            AssemBleText(score, tip, false);
        }



    }

    /**
     * 设置内容并添加监听
     * @param resId 内容文件
     * @param url 地址
     * @param textView 控件
     */
    private void setTextAndClick(int resId, String url, TextView textView){
        textView.setText(getString(resId));
        textView.setOnClickListener(v -> {
            TyWebViewActivity.startActivity(this, url);
            finish();
        });
    }

    /**
     * 组装红色字体
     */
    private void AssemBleText(String score, String tip, boolean isGrade){

        int scoreStartIndex = tip.indexOf(score);
        int scoreEndIndex = scoreStartIndex + score.length();
        if (!isGrade){
            scoreEndIndex += 2;
        }

        SpannableString tipSpanBuilder = new SpannableString(tip);
        ForegroundColorSpan span = new ForegroundColorSpan(ContextCompat.getColor(this, R.color.red));
        tipSpanBuilder.setSpan(span, scoreStartIndex,
                scoreEndIndex,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        tvTip.setText(tipSpanBuilder);
    }

    /**
     * 组装等级不够对应的积分不够的红色字体
     */
    private void AssemBleTextRank(String score, String tip){

        int scoreStartIndex = tip.indexOf(score) - 1;
        int scoreEndIndex = scoreStartIndex + score.length() + 3;

        SpannableString tipSpanBuilder = new SpannableString(tip);
        ForegroundColorSpan span = new ForegroundColorSpan(ContextCompat.getColor(this, R.color.red));
        tipSpanBuilder.setSpan(span, scoreStartIndex,
                scoreEndIndex,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        tvTip.setText(tipSpanBuilder);
    }
}
