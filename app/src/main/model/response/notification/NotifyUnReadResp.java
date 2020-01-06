package response.notification;

import com.google.gson.annotations.SerializedName;

public class NotifyUnReadResp {
    @SerializedName("unread_count")
    private int unReadCount;

    public int getUnReadCount() {
        return unReadCount;
    }

    public void setUnReadCount(int unReadCount) {
        this.unReadCount = unReadCount;
    }
}
