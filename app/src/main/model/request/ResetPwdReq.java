package request;

import com.google.gson.annotations.SerializedName;

/**
 * author       : frog
 * time         : 2019/4/24 下午4:24
 * desc         :
 * version      : 1.3.0
 */
public class ResetPwdReq {

    /**
     * new_password : 123456
     * repeat_password : 123456
     * email : 123456@qq.com
     * captcha_id : captchaId_oxmxvzohjo
     * captcha : 123456
     */

    @SerializedName("email")
    private String email;
    @SerializedName("new_password")
    private String newPassword;
    @SerializedName("repeat_password")
    private String repeatPassword;
    @SerializedName("captcha")
    private String captcha;
    @SerializedName("captcha_id")
    private String captchaId;

    public ResetPwdReq(String email,
                       String newPassword,
                       String repeatPassword,
                       String captcha,
                       String captchaId) {
        this.newPassword = newPassword;
        this.repeatPassword = repeatPassword;
        this.email = email;
        this.captchaId = captchaId;
        this.captcha = captcha;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getRepeatPassword() {
        return repeatPassword;
    }

    public void setRepeatPassword(String repeatPassword) {
        this.repeatPassword = repeatPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCaptchaId() {
        return captchaId;
    }

    public void setCaptchaId(String captchaId) {
        this.captchaId = captchaId;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }
}
