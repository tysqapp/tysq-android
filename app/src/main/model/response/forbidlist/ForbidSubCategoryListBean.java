package response.forbidlist;

import com.google.gson.annotations.SerializedName;

/**
 * author       : liaozhenlin
 * time         : 2019/8/29 14:52
 * desc         : 禁止评论列表返回的二级数据
 * version      : 1.0.0
 */
public class ForbidSubCategoryListBean {

    @SerializedName("category_id")
    private int categoryId;

    @SerializedName("category_name")
    private String categoryName;

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
}
