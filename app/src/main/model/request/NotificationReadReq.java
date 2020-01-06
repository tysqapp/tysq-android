package request;

import com.google.gson.annotations.SerializedName;

public class NotificationReadReq {

    @SerializedName("notify_id")
    private String notifyId;

    public NotificationReadReq(String notifyId) {
        this.notifyId = notifyId;
    }

    public String getNotifyId() {
        return notifyId;
    }

    public void setNotifyId(String notifyId) {
        this.notifyId = notifyId;
    }
}
