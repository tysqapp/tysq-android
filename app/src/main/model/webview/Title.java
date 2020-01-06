package webview;

import com.google.gson.annotations.SerializedName;

/**
 * author       : frog
 * time         : 2019-07-22 16:24
 * desc         : 更换标题
 * version      : 1.3.0
 */
public class Title {
    /**
     * navigationTitle : 购买积分
     */

    @SerializedName("navigationTitle")
    private String navigationTitle;

    public String getNavigationTitle() {
        return navigationTitle;
    }

    public void setNavigationTitle(String navigationTitle) {
        this.navigationTitle = navigationTitle;
    }
}
