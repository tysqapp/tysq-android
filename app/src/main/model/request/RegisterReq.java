package request;

import com.google.gson.annotations.SerializedName;
import com.tysq.ty_android.config.Constant;

/**
 * author       : frog
 * time         : 2019/4/24 上午11:12
 * desc         : 用户注册请求
 * version      : 1.3.0
 */
public class RegisterReq {

    /**
     * password : 123456
     * email : 123456@qq.com
     * referral_code : ""
     */

    @SerializedName("password")
    private String password;
    @SerializedName("email")
    private String email;
    @SerializedName("referral_code")
    private String referralCode;
    @SerializedName("type")
    private int type;
    @SerializedName("captcha_id")
    private String captchaId;
    @SerializedName("captcha")
    private String captcha;

    public RegisterReq(String email,
                       String password,
                       String referralCode,
                       String captchaId,
                       String captcha) {
        this.email = email;
        this.password = password;
        this.referralCode = referralCode;
        this.type = Constant.RegisterType.DIY;
        this.captchaId = captchaId;
        this.captcha = captcha;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getReferralCode() {
        return referralCode;
    }

    public void setReferralCode(String referralCode) {
        this.referralCode = referralCode;
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
