package request;

import com.google.gson.annotations.SerializedName;

/**
 * author       : frog
 * time         : 2019-08-15 14:17
 * desc         : 提现请求
 * version      : 1.3.0
 */
public class WithdrawReq {

    /**
     * withdraw_url :
     * coin_amount : 0
     * note :
     * email :
     * captcha_id :
     * captcha :
     */

    @SerializedName("withdraw_url")
    private String withdrawUrl;
    @SerializedName("coin_amount")
    private long coinAmount;
    @SerializedName("note")
    private String note;
    @SerializedName("email")
    private String email;
    @SerializedName("captcha_id")
    private String captchaId;
    @SerializedName("captcha")
    private String captcha;

    public WithdrawReq(String withdrawUrl,
                       long coinAmount,
                       String note,
                       String email,
                       String captchaId,
                       String captcha) {
        this.withdrawUrl = withdrawUrl;
        this.coinAmount = coinAmount;
        this.note = note;
        this.email = email;
        this.captchaId = captchaId;
        this.captcha = captcha;
    }

    public String getWithdrawUrl() {
        return withdrawUrl;
    }

    public void setWithdrawUrl(String withdrawUrl) {
        this.withdrawUrl = withdrawUrl;
    }

    public long getCoinAmount() {
        return coinAmount;
    }

    public void setCoinAmount(long coinAmount) {
        this.coinAmount = coinAmount;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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
