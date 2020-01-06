package eventbus;

/**
 * author       : frog
 * time         : 2019/5/26 下午5:49
 * desc         : 我的评论页面的事件
 * version      : 1.3.0
 */
public class MyReviewChangeEvent {

    private String commentId;

    public MyReviewChangeEvent(String commentId) {
        this.commentId = commentId;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }
}
