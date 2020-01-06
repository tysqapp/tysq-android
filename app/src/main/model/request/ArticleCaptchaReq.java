package request;

import com.google.gson.annotations.SerializedName;

/**
 * author       : frog
 * time         : 2019-08-12 18:09
 * desc         : 文章验证码
 * version      : 1.3.0
 */
public class ArticleCaptchaReq {

    @SerializedName("captcha")
    private String captcha;
    @SerializedName("captcha_id")
    private String captchaId;

    public ArticleCaptchaReq(String captchaId,
                             String captcha) {
        this.captcha = captcha;
        this.captchaId = captchaId;
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
