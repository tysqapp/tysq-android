package request;

import com.google.gson.annotations.SerializedName;

public class ForbidCommentReq {

    @SerializedName("parent_id")
    private int parentId;

    @SerializedName("sub_id")
    private int[] subId;

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public int[] getSubId() {
        return subId;
    }

    public void setSubId(int[] subId) {
        this.subId = subId;
    }
}