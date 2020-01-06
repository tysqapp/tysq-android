package response.notification;

import com.google.gson.annotations.SerializedName;


/**
 * author       : liaozhenlin
 * time         : 2019/9/9 17:53
 * desc         : 消息设置返回的数据
 * version      : 1.0.0
 */
public class NotificationConfigResp {


    /**
     * article_reviewed_system : true
     * article_reviewed_email : true
     * article_review_system : true
     * article_review_email : true
     * new_comment_system : true
     * new_reply_system : true
     * "report_handler_system": true,
     * "report_handler_email": false,
     */

    @SerializedName("article_reviewed_system")
    private boolean articleReviewedSystem;
    @SerializedName("article_reviewed_email")
    private boolean articleReviewedEmail;
    @SerializedName("article_review_system")
    private boolean articleReviewSystem;
    @SerializedName("article_review_email")
    private boolean articleReviewEmail;
    @SerializedName("new_comment_system")
    private boolean newCommentSystem;
    @SerializedName("new_reply_system")
    private boolean newReplySystem;
    @SerializedName("report_handler_system")
    private boolean reportHandlerSystem;
    @SerializedName("report_handler_email")
    private boolean reportHandlerEmail;
    @SerializedName("report_handled_system")
    private boolean reportHandledSystem;

    public boolean isReportHandledSystem() {
        return reportHandledSystem;
    }

    public void setReportHandledSystem(boolean reportHandledSystem) {
        this.reportHandledSystem = reportHandledSystem;
    }

    public boolean isArticleReviewedSystem() {
        return articleReviewedSystem;
    }

    public void setArticleReviewedSystem(boolean articleReviewedSystem) {
        this.articleReviewedSystem = articleReviewedSystem;
    }

    public boolean isArticleReviewedEmail() {
        return articleReviewedEmail;
    }

    public void setArticleReviewedEmail(boolean articleReviewedEmail) {
        this.articleReviewedEmail = articleReviewedEmail;
    }

    public boolean isArticleReviewSystem() {
        return articleReviewSystem;
    }

    public void setArticleReviewSystem(boolean articleReviewSystem) {
        this.articleReviewSystem = articleReviewSystem;
    }

    public boolean isArticleReviewEmail() {
        return articleReviewEmail;
    }

    public void setArticleReviewEmail(boolean articleReviewEmail) {
        this.articleReviewEmail = articleReviewEmail;
    }

    public boolean isNewCommentSystem() {
        return newCommentSystem;
    }

    public void setNewCommentSystem(boolean newCommentSystem) {
        this.newCommentSystem = newCommentSystem;
    }

    public boolean isNewReplySystem() {
        return newReplySystem;
    }

    public void setNewReplySystem(boolean newReplySystem) {
        this.newReplySystem = newReplySystem;
    }

    public boolean isReportHandlerSystem() {
        return reportHandlerSystem;
    }

    public void setReportHandlerSystem(boolean reportHandlerSystem) {
        this.reportHandlerSystem = reportHandlerSystem;
    }

    public boolean isReportHandlerEmail() {
        return reportHandlerEmail;
    }

    public void setReportHandlerEmail(boolean reportHandlerEmail) {
        this.reportHandlerEmail = reportHandlerEmail;
    }
}
