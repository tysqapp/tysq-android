package webview;

import com.google.gson.annotations.SerializedName;

/**
 * author       : liaozhenlin
 * time         : 2019/11/1 9:35
 * desc         : 开启新页面
 * version      : 1.5.0
 */
public class OpenNewLink {
    /**
     * navigationLink: 开启地址
     * isNeedLogin: 是否需要登陆
     */

    @SerializedName("navigationLink")
    private String navigationLink;

    @SerializedName("isNeedLogin")
    private boolean isNeedLogin;

    public String getNavigationLink() {
        return navigationLink;
    }

    public void setNavigationLink(String navigationLink) {
        this.navigationLink = navigationLink;
    }

    public boolean isNeedLogin() {
        return isNeedLogin;
    }

    public void setNeedLogin(boolean needLogin) {
        isNeedLogin = needLogin;
    }
}
