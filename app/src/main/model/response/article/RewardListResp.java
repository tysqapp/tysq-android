package response.article;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * author       : liaozhenlin
 * time         : 2019/11/11 9:43
 * desc         : 打赏文章列表
 * version      : 1.5.0
 */
public class RewardListResp {
    @SerializedName("reward_list")
    private List<RewardListBean> rewardListBeanList;
    @SerializedName("total_num")
    private int totalNum;

    public List<RewardListBean> getRewardListBeanList() {
        return rewardListBeanList;
    }

    public void setRewardListBeanList(List<RewardListBean> rewardListBeanList) {
        this.rewardListBeanList = rewardListBeanList;
    }

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public static class RewardListBean{
        @SerializedName("rewarder_id")
        private int rewardId;
        @SerializedName("head_url")
        private String headUrl;
        @SerializedName("amount")
        private int amount;
        @SerializedName("rewarded_at")
        private int rewardedAt;

        public int getRewardId() {
            return rewardId;
        }

        public void setRewardId(int rewardId) {
            this.rewardId = rewardId;
        }

        public String getHeadUrl() {
            return headUrl;
        }

        public void setHeadUrl(String headUrl) {
            this.headUrl = headUrl;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public int getRewardedAt() {
            return rewardedAt;
        }

        public void setRewardedAt(int rewardedAt) {
            this.rewardedAt = rewardedAt;
        }
    }
}
