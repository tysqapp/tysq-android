package request;

import com.google.gson.annotations.SerializedName;

/**
 * author       : frog
 * time         : 2019/5/17 下午2:46
 * desc         : 用户信息更新
 * version      : 1.3.0
 */
public class PersonInfoUpdateReq {

    /**
     * account : frogPower
     */

    public PersonInfoUpdateReq(String account) {
        this.account = account;
    }

    public PersonInfoUpdateReq(String account, int fileId, String address, String trade, String career, String profile) {
        this.account = account;
        this.fileId = fileId;
        this.address = address;
        this.trade = trade;
        this.career = career;
        this.profile = profile;
    }

    public PersonInfoUpdateReq(){}

    @SerializedName("account")
    private String account;
    @SerializedName("icon_id")
    private int fileId;
    @SerializedName("home_address")
    private String address;
    @SerializedName("trade")
    private String trade;
    @SerializedName("career")
    private String career;
    @SerializedName("personal_profile")
    private String profile;

    public String getCareer() {
        return career;
    }

    public void setCareer(String career) {
        this.career = career;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getTrade() {
        return trade;
    }

    public void setTrade(String trade) {
        this.trade = trade;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public int getFileId() {
        return fileId;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
    }
}
