package response.article;

import com.google.gson.annotations.SerializedName;

/**
 * author       : frog
 * time         : 2019/5/28 下午2:05
 * desc         : 文章发布
 * version      : 1.3.0
 */
public class PublishArticleResp {

    /**
     * article_id : 5ceccf6a1810ccd2247597c5
     */

    @SerializedName("article_id")
    private String articleId;
    @SerializedName("is_review")
    private boolean isReview;
    @SerializedName("limit_score")
    private int limitScore;

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public boolean isReview() {
        return isReview;
    }

    public void setReview(boolean review) {
        isReview = review;
    }

    public int getLimitScore() {
        return limitScore;
    }

    public void setLimitScore(int limitScore) {
        this.limitScore = limitScore;
    }
}
