package response.notification;

import com.google.gson.annotations.SerializedName;

/**
 * author       : liaozhenlin
 * time         : 2019/11/4 16:31
 * desc         : 设置通知是否全部已读
 * version      : 1.5.0
 */
public class NotificationReadedResp {
    @SerializedName("is_un_read")
    private boolean isUnRead;

    public boolean isUnRead() {
        return isUnRead;
    }

    public void setUnRead(boolean unRead) {
        isUnRead = unRead;
    }
}
