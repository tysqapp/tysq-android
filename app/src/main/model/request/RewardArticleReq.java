package request;

import com.google.gson.annotations.SerializedName;

import butterknife.BindView;

/**
 * author       : liaozhenlin
 * time         : 2019/11/8 0008 23:45
 * desc         : 打赏文章
 * version      : 1.5.0
 */
public class RewardArticleReq {
		@SerializedName("article_id")
		private String articleId;
		@SerializedName("reward_num")
		private int rewardNum;

		public RewardArticleReq(String articleId, int rewardNum) {
				this.articleId = articleId;
				this.rewardNum = rewardNum;
		}

		public String getArticleId() {
				return articleId;
		}

		public void setArticleId(String articleId) {
				this.articleId = articleId;
		}

		public int getRewardNum() {
				return rewardNum;
		}

		public void setRewardNum(int rewardNum) {
				this.rewardNum = rewardNum;
		}
}
