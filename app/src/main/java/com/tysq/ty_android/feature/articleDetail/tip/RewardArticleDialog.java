package com.tysq.ty_android.feature.articleDetail.tip;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.CheckedTextView;
import android.widget.TextView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abc.lib_utils.toast.ToastUtils;
import com.tysq.ty_android.R;
import com.tysq.ty_android.base.CommonBaseDialog;
import com.tysq.ty_android.config.Constant;
import com.tysq.ty_android.feature.articleDetail.listener.ArticleRewardListener;
import com.tysq.ty_android.local.sp.UserCache;
import com.tysq.ty_android.utils.TyUtils;

import butterknife.BindView;
/**
 * author       : liaozhenlin
 * time         : 2019/11/13 16:41
 * desc         : 打赏文章弹窗
 * version      : 1.5.0
 */
public class RewardArticleDialog extends CommonBaseDialog implements View.OnClickListener {

		public static final String TAG = "RewardArticleDialog";
		
    @BindView(R.id.ll_reward_coin_1)
    LinearLayout llRewardCoin1;
    @BindView(R.id.ll_reward_coin_2)
    LinearLayout llRewardCoin2;
    @BindView(R.id.ll_reward_100)
	LinearLayout llReward100;
    @BindView(R.id.ctv_reward_100)
	CheckedTextView ctvReward100;
    @BindView(R.id.ll_reward_200)
	LinearLayout llReward200;
    @BindView(R.id.ctv_reward_200)
	CheckedTextView ctvReward200;
    @BindView(R.id.ll_reward_500)
	LinearLayout llReward500;
    @BindView(R.id.ctv_reward_500)
	CheckedTextView ctvReward500;
    @BindView(R.id.ll_reward_1000)
	LinearLayout llReward1000;
    @BindView(R.id.ctv_reward_1000)
	CheckedTextView ctvReward1000;
    @BindView(R.id.ll_reward_2000)
	LinearLayout llReward2000;
    @BindView(R.id.ctv_reward_2000)
	CheckedTextView ctvReward2000;
    @BindView(R.id.ctv_reward_customize)
	CheckedTextView ctvRewardCustomize;
    @BindView(R.id.ll_edit_text_customize)
	LinearLayout llEditTextCustomize;
    @BindView(R.id.et_reward_coin)
    EditText etRewardCoin;
    @BindView(R.id.divider)
	View divider;
    @BindView(R.id.iv_close)
    ImageView ivClose;
    @BindView(R.id.tv_reward)
    TextView tvReward;

    //打赏金额
    private int mRewardNum;

    private ArticleRewardListener mListener;

    public static RewardArticleDialog newInstance(){

        Bundle args = new Bundle();

        RewardArticleDialog fragment = new RewardArticleDialog();
        fragment.setArguments(args);
        return fragment;
    }

    public void setListener(ArticleRewardListener listener){ this.mListener = listener; }
    @Override
    protected int getLayoutResource() {
        return R.layout.dialog_article_detail_reward;
    }

    @Override
    protected int obtainWidth() {
        return dp2px(335);
    }

    @Override
    protected int obtainHeight() {
        return dp2px(450);
    }

    @Override
    protected int obtainGravity() {
        return Gravity.CENTER;
    }

    @Override
    protected void initArgs(Bundle arguments) {

    }

    @Override
    protected void initView(View view) {
    		/*setRewardCoinInfo(ctvReward100, Constant.ArticleRewardNum.REWARD_100);
    		setRewardCoinInfo(ctvReward200, Constant.ArticleRewardNum.REWARD_200);
    		setRewardCoinInfo(ctvReward500, Constant.ArticleRewardNum.REWARD_500);
    		setRewardCoinInfo(ctvReward1000, Constant.ArticleRewardNum.REWARD_1000);
    		setRewardCoinInfo(ctvReward2000, Constant.ArticleRewardNum.REWARD_2000);*/

    		llEditTextCustomize.setVisibility(View.GONE);
    		divider.setVisibility(View.GONE);

    		llReward100.setOnClickListener(this);
    		llReward200.setOnClickListener(this);
    		llReward500.setOnClickListener(this);
    		llReward1000.setOnClickListener(this);
    		llReward2000.setOnClickListener(this);
    		ctvRewardCustomize.setOnClickListener(this);
			ivClose.setOnClickListener(this);
			tvReward.setOnClickListener(this);

			etRewardCoin.setInputType(EditorInfo.TYPE_CLASS_PHONE);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.iv_close:
                dismiss();
                break;

			case R.id.ll_reward_100:
					setCheckedStatus(ctvReward100,
									ctvReward200,
									ctvReward500,
									ctvReward1000,
									ctvReward2000,
									llReward100,
									llReward200,
									llReward500,
									llReward1000,
									llReward2000,
									Constant.ArticleRewardNum.REWARD_100);
					setCustomizeUnSelected();
					break;

			case R.id.ll_reward_200:
					setCheckedStatus(ctvReward200,
									ctvReward100,
									ctvReward500,
									ctvReward1000,
									ctvReward2000,
									llReward200,
									llReward100,
									llReward500,
									llReward1000,
									llReward2000,
									Constant.ArticleRewardNum.REWARD_200);
					setCustomizeUnSelected();
					break;

			case R.id.ll_reward_500:
					setCheckedStatus(ctvReward500,
									ctvReward100,
									ctvReward200,
									ctvReward1000,
									ctvReward2000,
									llReward500,
									llReward100,
									llReward200,
									llReward1000,
									llReward2000,
									Constant.ArticleRewardNum.REWARD_500);
					setCustomizeUnSelected();
					break;

			case R.id.ll_reward_1000:
					setCheckedStatus(ctvReward1000,
									ctvReward100,
									ctvReward200,
									ctvReward500,
									ctvReward2000,
									llReward1000,
									llReward100,
									llReward200,
									llReward500,
									llReward2000,
									Constant.ArticleRewardNum.REWARD_1000);
					setCustomizeUnSelected();
					break;

			case R.id.ll_reward_2000:
					setCheckedStatus(ctvReward2000,
									ctvReward100,
									ctvReward200,
									ctvReward500,
									ctvReward1000,
									llReward2000,
									llReward100,
									llReward200,
									llReward500,
									llReward1000,
									Constant.ArticleRewardNum.REWARD_2000);
					setCustomizeUnSelected();
					break;

			case R.id.ctv_reward_customize:
				ctvReward100.setChecked(false);
				ctvReward200.setChecked(false);
				ctvReward500.setChecked(false);
				ctvReward1000.setChecked(false);
				ctvReward2000.setChecked(false);

				setRewardCoinUnSelectedColor(llReward100, ctvReward100);
				setRewardCoinUnSelectedColor(llReward200, ctvReward200);
				setRewardCoinUnSelectedColor(llReward500, ctvReward500);
				setRewardCoinUnSelectedColor(llReward1000, ctvReward1000);
				setRewardCoinUnSelectedColor(llReward2000, ctvReward2000);

				ctvRewardCustomize.toggle();
				if (ctvRewardCustomize.isChecked()){
						llEditTextCustomize.setVisibility(View.VISIBLE);
						divider.setVisibility(View.VISIBLE);
						ctvRewardCustomize.setTextColor(getResources().getColor(R.color.main_blue_color));

						etRewardCoin.setFocusable(true);
						etRewardCoin.requestFocus();
						InputMethodManager inputMethodManager =
								(InputMethodManager) etRewardCoin
										.getContext()
										.getSystemService(Context.INPUT_METHOD_SERVICE);

						inputMethodManager.showSoftInput(etRewardCoin, 0);
				}else {
						setCustomizeUnSelected();
				}

				break;

            case R.id.tv_reward:

				if (!ctvReward100.isChecked()
					&& !ctvReward200.isChecked()
					&& !ctvReward500.isChecked()
					&& !ctvReward1000.isChecked()
					&& !ctvReward2000.isChecked()
					&& !ctvRewardCustomize.isChecked()){

					ToastUtils.show(getString(R.string.reward_select_reward_num));
					return;
				}

				if (TextUtils.isEmpty(etRewardCoin.getText().toString()) && ctvRewardCustomize.isChecked()){
					ToastUtils.show(getString(R.string.reward_select_reward_num));
					return;
				}

				if (ctvRewardCustomize.isChecked()){
					try {
						int num = Integer.parseInt(etRewardCoin.getText().toString());
						mRewardNum = num;
					}catch (NumberFormatException e){
						e.printStackTrace();
						ToastUtils.show(getString(R.string.top_article_tip_dialog_integer));
						return;
					}
					if (mRewardNum < 100 && mRewardNum >=0){
						ToastUtils.show(getString(R.string.top_article_tip_dialog_min_coin));
						return;
					}else if (mRewardNum < 0){
						ToastUtils.show(getString(R.string.top_article_tip_dialog_integer));
						return;
					}

				}

				this.mListener.onPostArticleReward(mRewardNum);

				TyUtils.hideKeyboard(etRewardCoin.getWindowToken(), getActivity());
				break;
        }
    }

	/**
	 * 设置textview为选中状态
	 * @param textView
	 */
    private void setRewardCoinSelectedColor(LinearLayout linearLayout, TextView textView){
        linearLayout.setBackgroundResource(R.drawable.shape_light_blue_rect_border_4dp);
        textView.setTextColor(getResources().getColor(R.color.main_blue_color));
    }

    /**
     * 设置textView为没选中的状态
     * @param textView
     */
    private void setRewardCoinUnSelectedColor(LinearLayout linearLayout, TextView textView){
    	linearLayout.setBackgroundResource(R.drawable.shape_dark_gray_rect_border_4dp);
        textView.setTextColor(getResources().getColor(R.color.main_text_color));
    }

	/**
	 * 将textview设置为未选中状态
	 */
	private void setCheckedStatus(CheckedTextView textView,
								CheckedTextView textView1,
								CheckedTextView textView2,
								CheckedTextView textView3,
								CheckedTextView textView4,
								LinearLayout linearLayout,
								LinearLayout linearLayout1,
								LinearLayout linearLayout2,
								LinearLayout linearLayout3,
								LinearLayout linearLayout4,
								int rewardNum){

			textView1.setChecked(false);
			textView2.setChecked(false);
			textView3.setChecked(false);
			textView4.setChecked(false);

			setRewardCoinUnSelectedColor(linearLayout1, textView1);
			setRewardCoinUnSelectedColor(linearLayout2, textView2);
			setRewardCoinUnSelectedColor(linearLayout3, textView3);
			setRewardCoinUnSelectedColor(linearLayout4, textView4);

			textView.toggle();
			if (textView.isChecked()){
					mRewardNum = rewardNum;
					setRewardCoinSelectedColor(linearLayout, textView);
			}else {
					setRewardCoinUnSelectedColor(linearLayout, textView);
					mRewardNum = 0;
			}

			Log.d("RewardArticleDialog", String.valueOf(textView.isChecked()) + rewardNum);

	}

	/**
	 * 设置自定义按钮为未选中状态
	 */
	private void setCustomizeUnSelected(){
			ctvRewardCustomize.setChecked(false);
			ctvRewardCustomize.setTextColor(getResources().getColor(R.color.main_text_color));
			llEditTextCustomize.setVisibility(View.GONE);
			divider.setVisibility(View.GONE);
	}

	/**
	 * 判断checkedTextView的选中状态，并展示相应的样式
	 */
	private void judgeCheckedTextStatus(LinearLayout linearLayout, CheckedTextView checkedTextView){
			if (checkedTextView.isChecked()){
					setRewardCoinSelectedColor(linearLayout, checkedTextView);
			} else {
					setRewardCoinUnSelectedColor(linearLayout, checkedTextView);
			}
	}

	@Override
	public void onResume() {
			super.onResume();

			if (ctvRewardCustomize.isChecked()){
					llEditTextCustomize.setVisibility(View.VISIBLE);
					divider.setVisibility(View.VISIBLE);
					ctvRewardCustomize.setTextColor(getResources().getColor(R.color.main_blue_color));
			}else {
					llEditTextCustomize.setVisibility(View.GONE);
					divider.setVisibility(View.GONE);
					ctvRewardCustomize.setTextColor(getResources().getColor(R.color.main_text_color));
			}

			judgeCheckedTextStatus(llReward100, ctvReward100);
			judgeCheckedTextStatus(llReward200, ctvReward200);
			judgeCheckedTextStatus(llReward500, ctvReward500);
			judgeCheckedTextStatus(llReward1000, ctvReward1000);
			judgeCheckedTextStatus(llReward2000, ctvReward2000);
	}

}
