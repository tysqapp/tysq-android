package vo.article;

/**
 * author       : frog
 * time         : 2019/5/22 上午9:55
 * desc         :
 * version      : 1.3.0
 */
public class ArticleDividerVO {

    private String articleId;
    private String topReviewId;

    public ArticleDividerVO(String articleId, String topReviewId) {
        this.articleId = articleId;
        this.topReviewId = topReviewId;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public String getTopReviewId() {
        return topReviewId;
    }

    public void setTopReviewId(String topReviewId) {
        this.topReviewId = topReviewId;
    }
}
