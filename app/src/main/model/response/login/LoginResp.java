package response.login;

import com.google.gson.annotations.SerializedName;

import cache.User;

/**
 * author       : frog
 * time         : 2019/4/24 下午2:24
 * desc         :
 * version      :
 */
public class LoginResp {

    @SerializedName("account_info")
    private User accountInfo;

    public User getAccountInfo() {
        return accountInfo;
    }

    public void setAccountInfo(User accountInfo) {
        this.accountInfo = accountInfo;
    }
}
