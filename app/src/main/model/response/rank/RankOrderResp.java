package response.rank;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * author       : frog
 * time         : 2019-07-16 14:33
 * desc         : 积分订单
 * version      : 1.3.0
 */
public class RankOrderResp {

    /**
     * scoresorder_list : [{"created_at":1563434950,"order_id":0,"amount":0,"price":0,"mark":"0","status":0}]
     * total_num : 10
     */

    @SerializedName("total_num")
    private int totalNum;
    @SerializedName("scoresorder_list")
    private List<ScoresorderListBean> scoresorderList;

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public List<ScoresorderListBean> getScoresorderList() {
        return scoresorderList;
    }

    public void setScoresorderList(List<ScoresorderListBean> scoresorderList) {
        this.scoresorderList = scoresorderList;
    }

    public static class ScoresorderListBean {
        /**
         * created_at : 1563434950
         * order_id : 0
         * amount : 0
         * price : 0
         * mark : 0
         * status : 0
         */

        @SerializedName("created_at")
        private long createdAt;
        @SerializedName("order_id")
        private String orderId;
        @SerializedName("amount")
        private String amount;
        @SerializedName("price")
        private String price;
        @SerializedName("mark")
        private String mark;
        @SerializedName("status")
        private int status;

        public long getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(long createdAt) {
            this.createdAt = createdAt;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getMark() {
            return mark;
        }

        public void setMark(String mark) {
            this.mark = mark;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }
}
