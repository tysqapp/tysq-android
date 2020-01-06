package response.permission;

import com.google.gson.annotations.SerializedName;

/**
 * author       : frog
 * time         : 2019-08-30 18:03
 * desc         : 个人权限
 * version      : 1.0.0
 */
public class PermissionResp {

    @SerializedName("can_review")
    private boolean canReview;
    @SerializedName("can_delete_article")
    private boolean canDeleteArticle;
    @SerializedName("can_forbid_comment")
    private boolean canForbidComment;
    @SerializedName("can_delete_comment")
    private boolean canDeleteComment;
    @SerializedName("can_hide_article")
    private int canHideArticle;
    @SerializedName("can_set_article_top")
    private boolean canSetArticleTop;

    // 是否能够编辑（本地添加，通过user的id）
    private boolean canEdit;

    public boolean isCanReview() {
        return canReview;
    }

    public void setCanReview(boolean canReview) {
        this.canReview = canReview;
    }

    public boolean isCanDeleteArticle() {
        return canDeleteArticle;
    }

    public void setCanDeleteArticle(boolean canDeleteArticle) {
        this.canDeleteArticle = canDeleteArticle;
    }

    public boolean isCanForbidComment() {
        return canForbidComment;
    }

    public void setCanForbidComment(boolean canForbidComment) {
        this.canForbidComment = canForbidComment;
    }

    public boolean isCanEdit() {
        return canEdit;
    }

    public void setCanEdit(boolean canEdit) {
        this.canEdit = canEdit;
    }

    public boolean isCanDeleteComment() {
        return canDeleteComment;
    }

    public void setCanDeleteComment(boolean canDeleteComment) {
        this.canDeleteComment = canDeleteComment;
    }

    public int getCanHideArticle() {
        return canHideArticle;
    }

    public void setCanHideArticle(int canHideArticle) {
        this.canHideArticle = canHideArticle;
    }

    public boolean isCanSetArticleTop() {
        return canSetArticleTop;
    }

    public void setCanSetArticleTop(boolean canSetArticleTop) {
        this.canSetArticleTop = canSetArticleTop;
    }
}
