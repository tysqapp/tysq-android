package response.article;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * author       : frog
 * time         : 2019-08-12 11:09
 * desc         : 收藏文章
 * version      : 1.3.0
 */
public class ArticleCollectResp {

    /**
     * collects : [{"article_id":"5d4a9f116611d130f6e1f9d5","title":"测试","create_time":1565577693}]
     * total_num : 1
     */

    @SerializedName("total_num")
    private int totalNum;
    @SerializedName("collects")
    private List<CollectsBean> collects;

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public List<CollectsBean> getCollects() {
        return collects;
    }

    public void setCollects(List<CollectsBean> collects) {
        this.collects = collects;
    }

    public static class CollectsBean {
        /**
         * article_id : 5d4a9f116611d130f6e1f9d5
         * title : 测试
         * create_time : 1565577693
         */

        @SerializedName("article_id")
        private String articleId;
        @SerializedName("title")
        private String title;
        @SerializedName("create_time")
        private long createTime;

        public String getArticleId() {
            return articleId;
        }

        public void setArticleId(String articleId) {
            this.articleId = articleId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }
    }
}