package request;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * author       : liaozhenlin
 * time         : 2019-8-27 10:07
 * desc         :
 * version      : 1.0.0
 */

public class ForbidReviewReq {

    @SerializedName("account_id")
    private int accountId;

    @SerializedName("category_ids")
    private List<ForbidCommentReq> forbidCommentReqList;

    public ForbidReviewReq(int accountId, List<ForbidCommentReq> forbidCommentReqList){
        this.accountId = accountId;
        this.forbidCommentReqList = forbidCommentReqList;
    }


    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public List<ForbidCommentReq> getForbidCommentReqList() {
        return forbidCommentReqList;
    }

    public void setForbidCommentReqList(List<ForbidCommentReq> forbidCommentReqList) {
        this.forbidCommentReqList = forbidCommentReqList;
    }
}
