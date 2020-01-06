package response;

import com.google.gson.annotations.SerializedName;

import cache.User;

/**
 * author       : frog
 * time         : 2019-07-18 14:25
 * desc         : 用户信息
 * version      : 1.3.0
 */
public class UserInfoResp {

    @SerializedName("account_info")
    private User accountInfo;

    @SerializedName("asset")
    private Asset asset;

    public User getAccountInfo() {
        return accountInfo;
    }

    public void setAccountInfo(User accountInfo) {
        this.accountInfo = accountInfo;
    }

    public Asset getAsset() {
        return asset;
    }

    public void setAsset(Asset asset) {
        this.asset = asset;
    }

    public static class Asset {

        /**
         * score : 0
         * hot_coin : 0
         * grade : 0
         * invited : 0
         * article_num : 0
         * comment_num : 0
         */

        @SerializedName("score")
        private long score;
        @SerializedName("hot_coin")
        private long hotCoin;
        @SerializedName("grade")
        private int grade;
        @SerializedName("invited")
        private String invited;
        @SerializedName("article_num")
        private String articleNum;
        @SerializedName("comment_num")
        private String commentNum;
        @SerializedName("award")
        private String award;
        @SerializedName("collect_num")
        private String collectNum;
        @SerializedName("fan_num")
        private String fanNum;
        @SerializedName("attention_num")
        private String attentionNum;

        public String getFanNum() {
            return fanNum;
        }

        public void setFanNum(String fanNum) {
            this.fanNum = fanNum;
        }

        public String getAttentionNum() {
            return attentionNum;
        }

        public void setAttentionNum(String attentionNum) {
            this.attentionNum = attentionNum;
        }

        public long getScore() {
            return score;
        }

        public void setScore(long score) {
            this.score = score;
        }

        public long getHotCoin() {
            return hotCoin;
        }

        public void setHotCoin(long hotCoin) {
            this.hotCoin = hotCoin;
        }

        public int getGrade() {
            return grade;
        }

        public void setGrade(int grade) {
            this.grade = grade;
        }

        public String getInvited() {
            return invited;
        }

        public void setInvited(String invited) {
            this.invited = invited;
        }

        public String getArticleNum() {
            return articleNum;
        }

        public void setArticleNum(String articleNum) {
            this.articleNum = articleNum;
        }

        public String getCommentNum() {
            return commentNum;
        }

        public void setCommentNum(String commentNum) {
            this.commentNum = commentNum;
        }

        public String getAward() {
            return award;
        }

        public void setAward(String award) {
            this.award = award;
        }

        public String getCollectNum() {
            return collectNum;
        }

        public void setCollectNum(String collectNum) {
            this.collectNum = collectNum;
        }
    }

}
