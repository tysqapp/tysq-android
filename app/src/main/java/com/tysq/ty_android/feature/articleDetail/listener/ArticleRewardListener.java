package com.tysq.ty_android.feature.articleDetail.listener;
/**
 * author       : liaozhenlin
 * time         : 2019/11/9 0009 9:53
 * desc         : 打赏文章
 * version      : 1.5.0
 */
public interface ArticleRewardListener {

	/**
	 * 打赏文章金额回调
	 * @param num
	 */
	void onPostArticleReward(int num);

	/**
	 * 金币不足返回打赏弹窗
	 */
	void onShowArticleRewardNotEnough();
}
