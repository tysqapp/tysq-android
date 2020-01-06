package response.common;

import com.google.gson.annotations.SerializedName;

/**
 * author       : frog
 * time         : 2019/4/24 上午11:11
 * desc         : 邮箱验证码返回
 * version      : 1.3.0
 */
public class EmailCodeResp {

    /**
     * captcha_id : captchaId_exisqaegkj
     */

    @SerializedName("captcha_id")
    private String captchaId;

    public String getCaptchaId() {
        return captchaId;
    }

    public void setCaptchaId(String captchaId) {
        this.captchaId = captchaId;
    }

}
