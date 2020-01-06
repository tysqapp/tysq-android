package response.login;

import com.google.gson.annotations.SerializedName;

/**
 * author       : frog
 * time         : 2019-07-22 17:56
 * desc         : 注册
 * version      : 1.3.0
 */
public class RegisterResp {

    @SerializedName("activate_email_score")
    private String activateEmailScore;

    public String getActivateEmailScore() {
        return activateEmailScore;
    }

    public void setActivateEmailScore(String activateEmailScore) {
        this.activateEmailScore = activateEmailScore;
    }
}
