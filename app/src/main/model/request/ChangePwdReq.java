package request;

import com.google.gson.annotations.SerializedName;

/**
 * author       : frog
 * time         : 2019/5/16 下午5:23
 * desc         : 修改密码
 * version      : 1.3.0
 */
public class ChangePwdReq {

    /**
     * old_password : 123456
     * new_password : zxcvbn
     */

    @SerializedName("old_password")
    private String oldPassword;
    @SerializedName("new_password")
    private String newPassword;

    public ChangePwdReq(String oldPassword, String newPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
