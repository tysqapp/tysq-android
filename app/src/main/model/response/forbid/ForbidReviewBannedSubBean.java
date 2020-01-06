package response.forbid;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ForbidReviewBannedSubBean {

    @SerializedName("category_id")
    private int categoryId;

    @SerializedName("sub_category")
    private List<ForbidReviewBannedSubBean> forbidReviewBannedSubBeanList;

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public List<ForbidReviewBannedSubBean> getForbidReviewBannedSubBeanList() {
        return forbidReviewBannedSubBeanList;
    }

    public void setForbidReviewBannedSubBeanList(List<ForbidReviewBannedSubBean> forbidReviewBannedSubBeanList) {
        this.forbidReviewBannedSubBeanList = forbidReviewBannedSubBeanList;
    }
}
