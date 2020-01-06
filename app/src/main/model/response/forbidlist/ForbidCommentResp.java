package response.forbidlist;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * author       : liaozhenlin
 * time         : 2019/8/29 14:40
 * desc         : 禁止评论列表的内容数据返回
 * version      : 1.0.0
 */
public class ForbidCommentResp {

    @SerializedName("forbid_comment")
    private List<ForbidCommentBean> forbidCommentRespList;

    public List<ForbidCommentBean> getForbidCommentBean() {
        return forbidCommentRespList;
    }

    public void setForbidCommentBean(List<ForbidCommentBean> forbidCommentRespList) {
        this.forbidCommentRespList = forbidCommentRespList;
    }

    public static class ForbidCommentBean{

        @SerializedName("account")
        private String account;
        @SerializedName("account_id")
        private int accountId;
        @SerializedName("category_list")
        private List<ForbidCategoryListBean> categoryList;

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public int getAccountId() {
            return accountId;
        }

        public void setAccountId(int accountId) {
            this.accountId = accountId;
        }

        public List<ForbidCategoryListBean> getCategoryList() {
            return categoryList;
        }

        public void setCategoryList(List<ForbidCategoryListBean> categoryList) {
            this.categoryList = categoryList;
        }
    }


}
