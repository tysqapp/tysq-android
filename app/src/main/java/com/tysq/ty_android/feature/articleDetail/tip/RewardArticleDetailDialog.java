package com.tysq.ty_android.feature.articleDetail.tip;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tysq.ty_android.R;
import com.tysq.ty_android.base.CommonBaseDialog;
import com.tysq.ty_android.config.Constant;
import com.tysq.ty_android.feature.articleDetail.listener.ArticleRewardListener;
import com.tysq.ty_android.feature.web.TyWebViewActivity;

import butterknife.BindView;
/**
 * author       : liaozhenlin
 * time         : 2019/11/13 16:41
 * desc         : 打赏文章金额不足弹窗
 * version      : 1.5.0
 */
public class RewardArticleDetailDialog extends CommonBaseDialog {

		@BindView(R.id.tv_reward_tip_des)
		TextView tvRewardTipDes;
		@BindView(R.id.tv_reward_tip_buy_coin)
		TextView tvRewardTipBuyCoin;
		@BindView(R.id.tv_reward_tip_change_coin)
		TextView tvRewardTipChangeCoin;
		@BindView(R.id.tv_how_to_earn_coin)
		TextView tvHowToEarnCoin;
		@BindView(R.id.iv_close)
		ImageView ivClose;

		//剩余金币
		private int mCoinNum;
		//应支出金币
		private int mRewardNum;

		private String mFormat;

		private ArticleRewardListener mListener;

		public static RewardArticleDetailDialog newInstance(){

				Bundle args = new Bundle();

				RewardArticleDetailDialog fragment = new RewardArticleDetailDialog();
				fragment.setArguments(args);
				return fragment;
		}

		public void setCoinAndReward(int coinNum, int rewardNum){
				this.mCoinNum = coinNum;
				this.mRewardNum = rewardNum;

		}

		public void setListener(ArticleRewardListener listener){ this.mListener = listener; }
		@Override
		protected int getLayoutResource() {
				return R.layout.dialog_article_detail_reward_tip;
		}

		@Override
		protected int obtainWidth() {
				return dp2px(335);
		}

		@Override
		protected int obtainHeight() {
				return dp2px(400);
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

				mFormat = String.format(getString(R.string.reward_tip_des), mCoinNum, mRewardNum);
				tvRewardTipDes.setText(mFormat);

				tvRewardTipBuyCoin.setOnClickListener(v -> {
						TyWebViewActivity.startActivity(getActivity(), Constant.HtmlAPI.COIN_URL);
						dismiss();
				});


				ivClose.setOnClickListener(v -> dismiss());

				tvRewardTipChangeCoin.setOnClickListener(v -> mListener.onShowArticleRewardNotEnough());

				tvHowToEarnCoin.setOnClickListener(v -> TyWebViewActivity.startActivity(getContext(), Constant.HtmlAPI.COIN));

		}
}
