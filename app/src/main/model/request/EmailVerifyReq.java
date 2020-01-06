package request;

import com.google.gson.annotations.SerializedName;

/**
 * author       : frog
 * time         : 2019-07-15 14:45
 * desc         : 邮箱验证
 * version      : 1.3.0
 */
public class EmailVerifyReq {

    /**
     * email : vgdusgush@mailinator.com
     * captcha : 123456
     * captcha_id : 2423423
     */

    @SerializedName("email")
    private String email;
    @SerializedName("captcha")
    private String captcha;
    @SerializedName("captcha_id")
    private String captchaId;

    public EmailVerifyReq(String email,
                          String captcha,
                          String captchaId) {
        this.email = email;
        this.captcha = captcha;
        this.captchaId = captchaId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    public String getCaptchaId() {
        return captchaId;
    }

    public void setCaptchaId(String captchaId) {
        this.captchaId = captchaId;
    }

}
