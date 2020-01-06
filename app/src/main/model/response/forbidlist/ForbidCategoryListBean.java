package response.forbidlist;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * author       : liaozhenlin
 * time         : 2019/8/29 14:48
 * desc         : 禁止评论列表返回的二级数据
 * version      : 1.0.0
 */
public class ForbidCategoryListBean {

    @SerializedName("category_id")
    private int categoryId;

    @SerializedName("category_name")
    private String categoryName;

    @SerializedName("sub_category")
    private List<ForbidSubCategoryListBean> subCategory;

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

    public List<ForbidSubCategoryListBean> getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(List<ForbidSubCategoryListBean> subCategory) {
        this.subCategory = subCategory;
    }
}
