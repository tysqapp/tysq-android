package request;

import com.google.gson.annotations.SerializedName;

/**
 * author       : frog
 * time         : 2019-09-02 15:02
 * desc         :
 * version      :
 */
public class ArticleReviewReq {

    @SerializedName("article_id")
    private String articleId;
    /**
     * 具体：{@link com.tysq.ty_android.config.Constant.ReviewStatus}
     */
    @SerializedName("status")
    private int status;
    @SerializedName("reason")
    private String reason;

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
