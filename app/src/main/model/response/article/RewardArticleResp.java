package response.article;

import com.google.gson.annotations.SerializedName;

/**
 * author       : liaozhenlin
 * time         : 2019/11/8 0008 23:51
 * desc         : 打赏文章返回数据
 * version      : 1.5.0
 */
public class RewardArticleResp {
		@SerializedName("coin_num")
		private int coinNum;
		@SerializedName("reward_num")
		private int rewardNum;

		public int getCoinNum() {
				return coinNum;
		}

		public void setCoinNum(int coinNum) {
				this.coinNum = coinNum;
		}

		public int getRewardNum() {
				return rewardNum;
		}

		public void setRewardNum(int rewardNum) {
				this.rewardNum = rewardNum;
		}
}
