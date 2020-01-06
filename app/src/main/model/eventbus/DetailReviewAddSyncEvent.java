package eventbus;

import response.article.ArticleReviewResp;

/**
 * author       : frog
 * time         : 2019/5/24 下午5:17
 * desc         : 评论详情——增加同步
 * version      : 1.3.0
 */
public class DetailReviewAddSyncEvent {

    private String parentId;
    private ArticleReviewResp.ArticleCommentsBean.ReplyBean reviewData;

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public ArticleReviewResp.ArticleCommentsBean.ReplyBean getReviewData() {
        return reviewData;
    }

    public void setReviewData(ArticleReviewResp.ArticleCommentsBean.ReplyBean reviewData) {
        this.reviewData = reviewData;
    }
}
