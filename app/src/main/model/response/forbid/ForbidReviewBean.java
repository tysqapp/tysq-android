package response.forbid;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * author       : liaozhenlin
 * time         : 2019/9/2 10:27
 * desc         : 禁止评论分类板块实体
 * version      : 1.0.0
 */
public class ForbidReviewBean {

    @SerializedName("category_id")
    private int categoryId;

    @SerializedName("category_name")
    private String categoryName;

    @SerializedName("position_id")
    private int positionId;

    @SerializedName("sub_category")
    private List<ForbidReviewSubBean> forbidReviewSubBeanList;

    //判断是否选中
    private boolean checked;

    public int getPositionId() {
        return positionId;
    }

    public void setPositionId(int positionId) {
        this.positionId = positionId;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public List<ForbidReviewSubBean> getForbidReviewSubBeanList() {
        return forbidReviewSubBeanList;
    }

    public void setForbidReviewSubBeanList(List<ForbidReviewSubBean> forbidReviewSubBeanList) {
        this.forbidReviewSubBeanList = forbidReviewSubBeanList;
    }
}
