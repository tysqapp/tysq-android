package response.home;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * author       : frog
 * time         : 2019/4/26 下午2:21
 * desc         : 分类信息返回
 * version      : 1.3.0
 */
public class CategoryResp {

    @SerializedName("category_info")
    private List<TopCategory> categoryInfo;

    @SerializedName("total_num")
    private int totalNum;

    public List<TopCategory> getCategoryInfo() {
        return categoryInfo;
    }

    public void setCategoryInfo(List<TopCategory> categoryInfo) {
        this.categoryInfo = categoryInfo;
    }

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }
}
