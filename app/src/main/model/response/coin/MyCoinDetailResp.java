package response.coin;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * author       : frog
 * time         : 2019-07-18 17:22
 * desc         : 我的金币
 * version      : 1.3.0
 */
public class MyCoinDetailResp {

    /**
     * total_number : 70
     * details_info : [{"time":1563508489,"action":"推荐好友购买积分","change_number":15000},{"time":1563249289,"action":"文章被阅读","change_number":50}]
     * details_count : 2
     */

    @SerializedName("total_number")
    private long totalNumber;
    @SerializedName("details_count")
    private int detailsCount;
    @SerializedName("details_info")
    private List<DetailsInfoBean> detailsInfo;

    public long getTotalNumber() {
        return totalNumber;
    }

    public void setTotalNumber(long totalNumber) {
        this.totalNumber = totalNumber;
    }

    public int getDetailsCount() {
        return detailsCount;
    }

    public void setDetailsCount(int detailsCount) {
        this.detailsCount = detailsCount;
    }

    public List<DetailsInfoBean> getDetailsInfo() {
        return detailsInfo;
    }

    public void setDetailsInfo(List<DetailsInfoBean> detailsInfo) {
        this.detailsInfo = detailsInfo;
    }

    public static class DetailsInfoBean {
        /**
         * time : 1563508489
         * action : 推荐好友购买积分
         * change_number : 15000
         */

        @SerializedName("time")
        private int time;
        @SerializedName("action")
        private String action;
        @SerializedName("change_number")
        private int changeNumber;

        public int getTime() {
            return time;
        }

        public void setTime(int time) {
            this.time = time;
        }

        public String getAction() {
            return action;
        }

        public void setAction(String action) {
            this.action = action;
        }

        public int getChangeNumber() {
            return changeNumber;
        }

        public void setChangeNumber(int changeNumber) {
            this.changeNumber = changeNumber;
        }
    }
}
