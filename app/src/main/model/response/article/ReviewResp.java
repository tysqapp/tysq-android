package response.article;

import com.google.gson.annotations.SerializedName;

/**
 * author       : frog
 * time         : 2019/5/23 上午11:01
 * desc         : 发表评论返回
 * version      : 1.3.0
 */
public class ReviewResp {

    @SerializedName("top_comment")
    private ArticleReviewResp.ArticleCommentsBean topComment;

    @SerializedName("sub_comment")
    private ArticleReviewResp.ArticleCommentsBean.ReplyBean subComment;

    @SerializedName("limit_score")
    private int limitScore;

    public ArticleReviewResp.ArticleCommentsBean getTopComment() {
        return topComment;
    }

    public void setTopComment(ArticleReviewResp.ArticleCommentsBean topComment) {
        this.topComment = topComment;
    }

    public ArticleReviewResp.ArticleCommentsBean.ReplyBean getSubComment() {
        return subComment;
    }

    public void setSubComment(ArticleReviewResp.ArticleCommentsBean.ReplyBean subComment) {
        this.subComment = subComment;
    }

    public int getLimitScore() {
        return limitScore;
    }

    public void setLimitScore(int limitScore) {
        this.limitScore = limitScore;
    }
}
