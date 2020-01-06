package response.rank;

import com.google.gson.annotations.SerializedName;

/**
 * author       : frog
 * time         : 2019-07-17 14:06
 * desc         : 积分权限
 * version      : 1.3.0
 */
public class JudgementResp {

    /**
     * is_satisfy : false
     * resource_status : 0
     * limit_score : 10
     */

    // 积分数量是否满足操作
    @SerializedName("is_satisfy")
    private boolean isSatisfy;
    // 操作资源状态
    @SerializedName("resource_status")
    private int resourceStatus;
    // 操作最低积分数
    @SerializedName("limit_score")
    private int limitScore;

    public boolean isIsSatisfy() {
        return isSatisfy;
    }

    public void setIsSatisfy(boolean isSatisfy) {
        this.isSatisfy = isSatisfy;
    }

    public int getResourceStatus() {
        return resourceStatus;
    }

    public void setResourceStatus(int resourceStatus) {
        this.resourceStatus = resourceStatus;
    }

    public int getLimitScore() {
        return limitScore;
    }

    public void setLimitScore(int limitScore) {
        this.limitScore = limitScore;
    }
}
