package request;

import com.google.gson.annotations.SerializedName;

/**
 * author       : frog
 * time         : 2019/4/24 下午2:18
 * desc         : 登录请求
 * version      : 1.3.0
 */
public class LoginReq {

    /**
     * account : xxxxx
     * password : 12345
     * captcha_id : captchaId_oxmxvzohjo
     * captcha : 123456
     */

    @SerializedName("account")
    private String account;
    @SerializedName("password")
    private String password;
    @SerializedName("captcha_id")
    private String captchaId;
    @SerializedName("captcha")
    private String captcha;

    public LoginReq(String account,
                    String password,
                    String captcha,
                    String captchaId) {
        this.account = account;
        this.password = password;
        this.captchaId = captchaId;
        this.captcha = captcha;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
