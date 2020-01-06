package request;

import com.google.gson.annotations.SerializedName;

/**
 * author       : frog
 * time         : 2019-08-12 10:29
 * desc         : 文章收集请求
 * version      : 1.3.0
 */
public class ArticleCollectReq {

    @SerializedName("article_id")
    private String articleId;
    @SerializedName("collect_status")
    private int collectStatus;

    public ArticleCollectReq(String articleId, int collectStatus) {
        this.articleId = articleId;
        this.collectStatus = collectStatus;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public int getCollectStatus() {
        return collectStatus;
    }

    public void setCollectStatus(int collectStatus) {
        this.collectStatus = collectStatus;
    }
}
