package response.notification;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * author       : liaozhenlin
 * time         : 2019/9/9 14:59
 * desc         : 通知实体的一级数据
 * version      : 1.0.0
 */
public class NotifyInfoResp {

    @SerializedName("notify_info")
    private List<NotificationResp> notificationResps;

    @SerializedName("total_number")
    private int totalNumber;

    public int getTotalNumber() {
        return totalNumber;
    }

    public void setTotalNumber(int totalNumber) {
        this.totalNumber = totalNumber;
    }

    public List<NotificationResp> getNotificationResps() {
        return notificationResps;
    }

    public void setNotificationResps(List<NotificationResp> notificationResps) {
        this.notificationResps = notificationResps;
    }
}
