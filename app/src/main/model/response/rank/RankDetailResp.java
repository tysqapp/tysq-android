package response.rank;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * author       : frog
 * time         : 2019-07-16 10:43
 * desc         : 积分明细
 * version      : 1.3.0
 */
public class RankDetailResp {

    /**
     * scoredetail_list : [{"amount":-203000,"action":"积分过期","created_at":"0001-01-01T00:00:00Z","expired_at":"0001-01-01T00:00:00Z"},{"amount":-203000,"action":"积分过期","created_at":"0001-01-01T00:00:00Z","expired_at":"0001-01-01T00:00:00Z"},{"amount":-203000,"action":"积分过期","created_at":"0001-01-01T00:00:00Z","expired_at":"0001-01-01T00:00:00Z"},{"amount":-203000,"action":"积分过期","created_at":"0001-01-01T00:00:00Z","expired_at":"0001-01-01T00:00:00Z"},{"amount":-203000,"action":"积分过期","created_at":"0001-01-01T00:00:00Z","expired_at":"0001-01-01T00:00:00Z"}]
     * total_num : 3585
     */

    @SerializedName("total_num")
    private int totalNum;

    @SerializedName("score_sum")
    private long totalCount;

    @SerializedName("scoredetail_list")
    private List<ScoresListBean> scoresList;

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public List<ScoresListBean> getScoresList() {
        return scoresList;
    }

    public void setScoresList(List<ScoresListBean> scoresList) {
        this.scoresList = scoresList;
    }

    public RankDetailResp(int totalCount) {
        this.totalCount = totalCount;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public static class ScoresListBean {
        /**
         * amount : -203000
         * action : 积分过期
         * created_at : 0001-01-01T00:00:00Z
         * expired_at : 0001-01-01T00:00:00Z
         */

        @SerializedName("amount")
        private long amount;
        @SerializedName("action")
        private String action;
        @SerializedName("created_at")
        private long createdAt;
        @SerializedName("expired_at")
        private long expiredAt;

        public long getAmount() {
            return amount;
        }

        public void setAmount(long amount) {
            this.amount = amount;
        }

        public String getAction() {
            return action;
        }

        public void setAction(String action) {
            this.action = action;
        }

        public long getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(long createdAt) {
            this.createdAt = createdAt;
        }

        public long getExpiredAt() {
            return expiredAt;
        }

        public void setExpiredAt(long expiredAt) {
            this.expiredAt = expiredAt;
        }
    }
}
