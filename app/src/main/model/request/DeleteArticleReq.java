package request;

import com.google.gson.annotations.SerializedName;

/**
 * author       : frog
 * time         : 2019/5/29 上午11:56
 * desc         :
 * version      : 1.3.0
 */
public class DeleteArticleReq {

    @SerializedName("article_id")
    private String articleId;
//    @SerializedName("status")
//    private int status;

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

//    public int getStatus() {
//        return status;
//    }
//
//    public void setStatus(int status) {
//        this.status = status;
//    }
}
