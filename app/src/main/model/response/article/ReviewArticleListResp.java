package response.article;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * author       : frog
 * time         : 2019-09-03 18:07
 * desc         : 审核列表
 * version      : 1.0.0
 */
public class ReviewArticleListResp {

    /**
     * review_articles : [{"title":"日历老师","first_category":"佛法","sub_category":"大须尔法","updated_at":1567388447,"operator":"","author":"拉拉","status_name":"已发布"},{"title":"芥子纳须弥，一花一世界","frist_category":"佛法","sub_category":"大须尔法","updated_at":1567388427,"operator":"拉拉","author":"拉拉","status_name":"已发布"},{"title":"芥子纳须弥，一花一世界","frist_category":"天河","sub_category":"五山","updated_at":1567159272,"operator":"ding_code","author":"ding_code","status_name":"已发布"},{"title":"芥子纳须弥，一花一世界","frist_category":"佛法","sub_category":"大须尔法","updated_at":1567137186,"operator":"拉拉","author":"拉拉","status_name":"已发布"},{"title":"撒旦大神","frist_category":"天河","sub_category":"体育西路","updated_at":1567069066,"operator":"拉拉","author":"拉拉","status_name":"已发布"},{"title":"的撒大","frist_category":"天河","sub_category":"五山","updated_at":1567067521,"operator":"拉拉","author":"拉拉","status_name":"已发布"}]
     * articles_num : 6
     */

    @SerializedName("articles_num")
    private int articlesNum;
    @SerializedName("review_articles")
    private List<ReviewArticlesBean> reviewArticles;

    public int getArticlesNum() {
        return articlesNum;
    }

    public void setArticlesNum(int articlesNum) {
        this.articlesNum = articlesNum;
    }

    public List<ReviewArticlesBean> getReviewArticles() {
        return reviewArticles;
    }

    public void setReviewArticles(List<ReviewArticlesBean> reviewArticles) {
        this.reviewArticles = reviewArticles;
    }

    public static class ReviewArticlesBean {
        /**
         * title : 日历老师
         * article_id: "5d3fb3b6432370a165875456",
         * frist_category : 佛法
         * sub_category : 大须尔法
         * updated_at : 1567388447
         * operator_name :
         * author : 拉拉
         * status_name : 已发布
         */

        @SerializedName("article_id")
        private String articleId;
        @SerializedName("title")
        private String title;
        @SerializedName("first_category")
        private String firstCategory;
        @SerializedName("sub_category")
        private String subCategory;
        @SerializedName("updated_at")
        private long updatedAt;
        @SerializedName("operator_name")
        private String operatorName;
        @SerializedName("author")
        private String author;
        @SerializedName("status_name")
        private String statusName;

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

        public String getFirstCategory() {
            return firstCategory;
        }

        public void setFirstCategory(String firstCategory) {
            this.firstCategory = firstCategory;
        }

        public String getSubCategory() {
            return subCategory;
        }

        public void setSubCategory(String subCategory) {
            this.subCategory = subCategory;
        }

        public long getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(long updatedAt) {
            this.updatedAt = updatedAt;
        }

        public String getOperatorName() {
            return operatorName;
        }

        public void setOperatorName(String operatorName) {
            this.operatorName = operatorName;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getStatusName() {
            return statusName;
        }

        public void setStatusName(String statusName) {
            this.statusName = statusName;
        }
    }
}
