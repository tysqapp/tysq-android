package response.forbid;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ForbidReviewBanned {
    @SerializedName("forbid_categorys")
    private List<ForbidReviewBannedBean> forbidReviewBannedBeanList;

    public List<ForbidReviewBannedBean> getForbidReviewBannedBeanList() {
        return forbidReviewBannedBeanList;
    }

    public void setForbidReviewBannedBeanList(List<ForbidReviewBannedBean> forbidReviewBannedBeanList)   {
        this.forbidReviewBannedBeanList = forbidReviewBannedBeanList;
    }

    public static class ForbidReviewBannedBean{
        @SerializedName("category_id")
        private int categoryId;

        @SerializedName("position_id")
        private int positionId;

        @SerializedName("sub_category")
        private List<ForbidReviewBannedSubBean> forbidReviewBannedSubBeanList;

        public int getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(int categoryId) {
            this.categoryId = categoryId;
        }

        public int getPositionId() {
            return positionId;
        }

        public void setPositionId(int positionId) {
            this.positionId = positionId;
        }

        public List<ForbidReviewBannedSubBean> getForbidReviewBannedSubBeanList() {
            return forbidReviewBannedSubBeanList;
        }

        public void setForbidReviewBannedSubBeanList(List<ForbidReviewBannedSubBean> forbidReviewBannedSubBeanList) {
            this.forbidReviewBannedSubBeanList = forbidReviewBannedSubBeanList;
        }
    }
}

