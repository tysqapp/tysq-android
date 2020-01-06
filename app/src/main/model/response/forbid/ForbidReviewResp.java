package response.forbid;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * author       : liaozhenlin
 * time         : 2019/9/2 10:24
 * desc         : 禁止评论分类列表
 * version      : 1.0.0
 */
public class ForbidReviewResp {
    @SerializedName("categorys")
    private List<ForbidReviewBean> forbidReviewBeanList;

    public List<ForbidReviewBean> getForbidReviewBeanList() {
        return forbidReviewBeanList;
    }

    public void setForbidReviewBeanList(List<ForbidReviewBean> forbidReviewBeanList) {
        this.forbidReviewBeanList = forbidReviewBeanList;
    }
}
