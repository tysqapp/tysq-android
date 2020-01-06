package response.coin;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * author       : frog
 * time         : 2019-08-13 11:26
 * desc         : 提现日志
 * version      : 1.3.0
 */
public class WithdrawLogResp {

    /**
     * withdraw_review_list : [{"id":30,"reason":"","created_at":1565665994,"coin_amount":0.0011,"btc_amount":3.7831E-4,"address":"","note":"是榨汁","status":2},{"id":29,"reason":"","created_at":1565665562,"coin_amount":0.0015,"btc_amount":8.7899E-4,"address":"","note":"是傻子","status":2},{"id":28,"reason":"","created_at":1565604979,"coin_amount":0.0015,"btc_amount":8.8266E-4,"address":"","note":"1231231","status":2},{"id":27,"reason":"","created_at":1565604967,"coin_amount":0.0015,"btc_amount":8.8266E-4,"address":"","note":"131313","status":2},{"id":26,"reason":"","created_at":1565604957,"coin_amount":0.0011,"btc_amount":3.8062E-4,"address":"","note":"75786578567865","status":2},{"id":25,"reason":"","created_at":1565604537,"coin_amount":0.0015,"btc_amount":8.824E-4,"address":"","note":"","status":2},{"id":24,"reason":"","created_at":1565604468,"coin_amount":0.0015,"btc_amount":8.8285E-4,"address":"","note":"","status":2},{"id":23,"reason":"","created_at":1565604201,"coin_amount":0.0015,"btc_amount":8.8285E-4,"address":"","note":"","status":2},{"id":22,"reason":"","created_at":1565603641,"coin_amount":0.0011,"btc_amount":3.7938E-4,"address":"","note":"4567456","status":2},{"id":20,"reason":"","created_at":1565602530,"coin_amount":0.0011,"btc_amount":3.7946E-4,"address":"","note":"chensirne","status":2}]
     * total_num : 35
     */

    @SerializedName("total_num")
    private int totalNum;
    @SerializedName("withdraw_review_list")
    private List<WithdrawReviewListBean> withdrawReviewList;

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public List<WithdrawReviewListBean> getWithdrawReviewList() {
        return withdrawReviewList;
    }

    public void setWithdrawReviewList(List<WithdrawReviewListBean> withdrawReviewList) {
        this.withdrawReviewList = withdrawReviewList;
    }

    public static class WithdrawReviewListBean {
        /**
         * id : 30
         * reason :
         * created_at : 1565665994
         * coin_amount : 0.0011
         * btc_amount : 3.7831E-4
         * address :
         * note : 是榨汁
         * status : 2
         */

        @SerializedName("id")
        private String id;
        @SerializedName("reason")
        private String reason;
        @SerializedName("created_at")
        private long createdAt;
        @SerializedName("coin_amount")
        private String coinAmount;
        @SerializedName("btc_amount")
        private double btcAmount;
        @SerializedName("address")
        private String address;
        @SerializedName("note")
        private String note;
        @SerializedName("status")
        private int status;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

        public long getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(long createdAt) {
            this.createdAt = createdAt;
        }

        public String getCoinAmount() {
            return coinAmount;
        }

        public void setCoinAmount(String coinAmount) {
            this.coinAmount = coinAmount;
        }

        public double getBtcAmount() {
            return btcAmount;
        }

        public void setBtcAmount(double btcAmount) {
            this.btcAmount = btcAmount;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getNote() {
            return note;
        }

        public void setNote(String note) {
            this.note = note;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }
}
