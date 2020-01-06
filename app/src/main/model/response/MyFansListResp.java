package response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MyFansListResp {
    @SerializedName("total_num")
    private int totalNum;
    @SerializedName("account_follower_infos")
    private List<AttentionInfoBean> attentionInfo;

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public List<AttentionInfoBean> getAttentionInfo() {
        return attentionInfo;
    }

    public void setAttentionInfo(List<AttentionInfoBean> attentionInfo) {
        this.attentionInfo = attentionInfo;
    }

    public static class AttentionInfoBean{
        @SerializedName("account_id")
        private int accountId;
        @SerializedName("head_url")
        private String headUrl;
        @SerializedName("account_name")
        private String accountName;
        @SerializedName("grade")
        private int grade;
        @SerializedName("personal_profile")
        private String personalProfile;
        @SerializedName("collected_num")
        private int collectedNum;
        @SerializedName("readed_num")
        private int readedNum;
        @SerializedName("article_num")
        private int articleNum;
        @SerializedName("is_follow")
        private boolean isFollow;

        public int getAccountId() {
            return accountId;
        }

        public void setAccountId(int accountId) {
            this.accountId = accountId;
        }

        public String getHeadUrl() {
            return headUrl;
        }

        public void setHeadUrl(String headUrl) {
            this.headUrl = headUrl;
        }

        public String getAccountName() {
            return accountName;
        }

        public void setAccountName(String accountName) {
            this.accountName = accountName;
        }

        public int getGrade() {
            return grade;
        }

        public void setGrade(int grade) {
            this.grade = grade;
        }

        public String getPersonalProfile() {
            return personalProfile;
        }

        public void setPersonalProfile(String personalProfile) {
            this.personalProfile = personalProfile;
        }

        public int getCollectedNum() {
            return collectedNum;
        }

        public void setCollectedNum(int collectedNum) {
            this.collectedNum = collectedNum;
        }

        public int getReadedNum() {
            return readedNum;
        }

        public void setReadedNum(int readedNum) {
            this.readedNum = readedNum;
        }

        public int getArticleNum() {
            return articleNum;
        }

        public void setArticleNum(int articleNum) {
            this.articleNum = articleNum;
        }

        public boolean isFollow() {
            return isFollow;
        }

        public void setFollow(boolean follow) {
            isFollow = follow;
        }
    }
}
