package vo.article;

/**
 * author       : frog
 * time         : 2019/5/22 上午9:55
 * desc         :
 * version      : 1.3.0
 */
public class ArticleMoreVO {

    private String articleId;
    private String topReviewId;
    // 是否有权限删除
    private boolean isCanDelete;
    // 是否有权限禁止评论权限
    private boolean isCanForbid;

    public ArticleMoreVO(String articleId,
                         String topReviewId,
                         boolean isCanDelete,
                         boolean isCanForbid) {
        this.articleId = articleId;
        this.topReviewId = topReviewId;
        this.isCanDelete = isCanDelete;
        this.isCanForbid = isCanForbid;
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

    public boolean isCanDelete() {
        return isCanDelete;
    }

    public void setCanDelete(boolean canDelete) {
        isCanDelete = canDelete;
    }

    public boolean isCanForbid() {
        return isCanForbid;
    }

    public void setCanForbid(boolean canForbid) {
        isCanForbid = canForbid;
    }
}
