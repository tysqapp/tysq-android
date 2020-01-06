package response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * author       : frog
 * time         : 2019-08-22 14:28
 * desc         : 更新
 * version      : 1.3.0
 */
public class UpdateResp {

    /**
     * minimum_support_version : 1.0.1
     * latest_version : 3.1.0
     * new_features : ["1、新增提现功能","2、优化部分交互设计，首页整体布局优化","3、修改部分bug"]
     */

    @SerializedName("minimum_support_version")
    private String minimumSupportVersion;
    @SerializedName("latest_version")
    private String latestVersion;
    @SerializedName("new_features")
    private List<String> newFeatures;

    private int updateType;

    public String getMinimumSupportVersion() {
        return minimumSupportVersion;
    }

    public void setMinimumSupportVersion(String minimumSupportVersion) {
        this.minimumSupportVersion = minimumSupportVersion;
    }

    public String getLatestVersion() {
        return latestVersion;
    }

    public void setLatestVersion(String latestVersion) {
        this.latestVersion = latestVersion;
    }

    public List<String> getNewFeatures() {
        return newFeatures;
    }

    public void setNewFeatures(List<String> newFeatures) {
        this.newFeatures = newFeatures;
    }

    public int getUpdateType() {
        return updateType;
    }

    public void setUpdateType(int updateType) {
        this.updateType = updateType;
    }
}
