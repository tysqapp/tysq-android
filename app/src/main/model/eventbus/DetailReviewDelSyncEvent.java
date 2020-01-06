package eventbus;

import response.article.ArticleReviewResp;

/**
 * author       : frog
 * time         : 2019/5/24 下午5:17
 * desc         : 评论详情——删除同步
 * version      : 1.3.0
 */
public class DetailReviewDelSyncEvent {

    private String commentId;
    private String parentId;
    private boolean isTop;

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public boolean isTop() {
        return isTop;
    }

    public void setTop(boolean top) {
        isTop = top;
    }
}
