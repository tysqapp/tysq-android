package response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * author       : frog
 * time         : 2019-07-19 11:29
 * desc         : 邀请
 * version      : 1.3.0
 */
public class InviteResp {

    /**
     * referral_code : 176025
     * referral_link : http://192.168.0.157:8095/#/invitation?code=176025
     * domain_name : http://192.168.0.157:8095
     * referral_list : [{"email":"ctrrbknskw@qq.com","created_at":1563433317},{"email":"565117937@qq.com","created_at":1563415349}]
     * total_num : 2
     */

    @SerializedName("referral_code")
    private String referralCode;
    @SerializedName("score_number")
    private String scoreNumber;
    @SerializedName("referral_link")
    private String referralLink;
    @SerializedName("domain_name")
    private String domainName;
    @SerializedName("total_num")
    private int totalNum;
    @SerializedName("referral_list")
    private List<ReferralListBean> referralList;

    public String getReferralCode() {
        return referralCode;
    }

    public void setReferralCode(String referralCode) {
        this.referralCode = referralCode;
    }

    public String getReferralLink() {
        return referralLink;
    }

    public void setReferralLink(String referralLink) {
        this.referralLink = referralLink;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public List<ReferralListBean> getReferralList() {
        return referralList;
    }

    public void setReferralList(List<ReferralListBean> referralList) {
        this.referralList = referralList;
    }

    public String getScoreNumber() {
        return scoreNumber;
    }

    public void setScoreNumber(String scoreNumber) {
        this.scoreNumber = scoreNumber;
    }

    public static class ReferralListBean {
        /**
         * email : ctrrbknskw@qq.com
         * created_at : 1563433317
         */

        @SerializedName("email")
        private String email;
        @SerializedName("created_at")
        private long createdAt;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public long getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(long createdAt) {
            this.createdAt = createdAt;
        }
    }
}
