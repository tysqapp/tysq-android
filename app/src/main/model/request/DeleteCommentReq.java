package request;

import com.google.gson.annotations.SerializedName;

/**
 * author       : frog
 * time         : 2019/5/22 上午10:20
 * desc         :
 * version      : 1.3.0
 */
public class DeleteCommentReq {

    /**
     * comment_id : 5cb96df3775bdc3e385a26d8
     */

    @SerializedName("comment_id")
    private String commentId;

    public DeleteCommentReq(String commentId) {
        this.commentId = commentId;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }
}
