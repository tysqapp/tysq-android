package response.coin;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * author       : liaozhenlin
 * time         : 2019/11/4 18:24
 * desc         : 金币订单
 * version      : 1.5.0
 */
public class CoinOrderResp {
    @SerializedName("total")
    private int total;
    @SerializedName("coin_orders")
    private List<CoinOrdersBean> coinOrdersBeanList;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<CoinOrdersBean> getCoinOrdersBeanList() {
        return coinOrdersBeanList;
    }

    public void setCoinOerdersBeanList(List<CoinOrdersBean> coinOrdersBeanList) {
        this.coinOrdersBeanList = coinOrdersBeanList;
    }

    public static class CoinOrdersBean{

        @SerializedName("created_at")
        private int createAt;
        @SerializedName("order_id")
        private String orderId;
        @SerializedName("coin_amount")
        private int coinAmount;
        @SerializedName("price")
        private float price;
        @SerializedName("status")
        private int status;

        public int getCreateAt() {
            return createAt;
        }

        public void setCreateAt(int createAt) {
            this.createAt = createAt;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public int getCoinAmount() {
            return coinAmount;
        }

        public void setCoinAmount(int coinAmount) {
            this.coinAmount = coinAmount;
        }

        public float getPrice() {
            return price;
        }

        public void setPrice(float price) {
            this.price = price;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }
}
