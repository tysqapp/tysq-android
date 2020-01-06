package request;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * author       : frog
 * time         : 2019/5/22 下午5:36
 * desc         : 评论请求
 * version      : 1.3.0
 */
public class ReviewReq {

    @SerializedName("article_id")
    private String articleId;
    @SerializedName("content")
    private String content;
    @SerializedName("at_account_id")
    private int atAccountId;
    @SerializedName("parent_id")
    private String parentId;
    @SerializedName("image_id")
    private List<Integer> imageId;
    @SerializedName("father_id")
    private String fatherId;

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getAtAccountId() {
        return atAccountId;
    }

    public void setAtAccountId(int atAccountId) {
        this.atAccountId = atAccountId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public List<Integer> getImageId() {
        return imageId;
    }

    public void setImageId(List<Integer> imageId) {
        this.imageId = imageId;
    }

    public String getFatherId() {
        return fatherId;
    }

    public void setFatherId(String fatherId) {
        this.fatherId = fatherId;
    }
}
