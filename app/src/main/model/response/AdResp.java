package response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * author       : frog
 * time         : 2019-07-25 10:35
 * desc         : 公告数据
 * version      : 1.3.0
 */
public class AdResp {

    @SerializedName("announcement_list")
    private List<AdvertisementListBean> advertisementList;

    public List<AdvertisementListBean> getAdvertisementList() {
        return advertisementList;
    }

    public void setAdvertisementList(List<AdvertisementListBean> advertisementList) {
        this.advertisementList = advertisementList;
    }

    public static class AdvertisementListBean {
        /**
         * content : 等级低？无法阅读文章？立即购买积分
         * pc_url : http://192.168.0.157:8095/#/buyPoints
         * app_url : http://192.168.0.157:8095/#/buyPoints
         * position : 1
         * is_app : false
         */

        @SerializedName("title")
        private String title;
        @SerializedName("url")
        private String url;
        @SerializedName("position")
        private int position;
        @SerializedName("status")
        private int status;

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

    }
}