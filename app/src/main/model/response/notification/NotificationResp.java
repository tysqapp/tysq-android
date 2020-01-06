package response.notification;
import com.google.gson.annotations.SerializedName;


/**
 * author       : frog
 * time         : 2019-08-29 17:02
 * desc         : 消息实体
 * version      : 1.0.0
 */
public class NotificationResp {


    /**
     * remind_type : 评论通知
     * content : egje1567666393评论你的文章《测试》。
     * article_id : 5d70d0e5adcc34196356dd7b
     * title : 测试
     * time : 1567674752
     * sender_name : egje1567666393
     * avatar_url :
     */

    @SerializedName("notify_id")
    private String notifyId;

    @SerializedName("remind_type")
    private String remindType;

    @SerializedName("article_id")
    private String articleId;

    @SerializedName("report_id")
    private String reportId;

    @SerializedName("report_number")
    private String reportNumber;

    @SerializedName("title")
    private String title;

    @SerializedName("time")
    private int time;

    @SerializedName("sender_name")
    private String senderName;

    @SerializedName("avatar_url")
    private String avatarUrl;

    @SerializedName("is_read")
    private boolean isRead;

    @SerializedName("sender")
    private int sender;

    @SerializedName("remind_id")
    private String remindId;

    @SerializedName("action")
    private String action;

    public String getReportNumber() {
        return reportNumber;
    }

    public void setReportNumber(String reportNumber) {
        this.reportNumber = reportNumber;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public String getNotifyId() {
        return notifyId;
    }

    public void setNotifyId(String notifyId) {
        this.notifyId = notifyId;
    }

    public int getSender() {
        return sender;
    }

    public void setSender(int sender) {
        this.sender = sender;
    }

    public String getRemindId() {
        return remindId;
    }

    public void setRemindId(String remindId) {
        this.remindId = remindId;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(){
        this.isRead = isRead;
    }

    public String getRemindType() {
        return remindType;
    }

    public void setRemindType(String remindType) {
        this.remindType = remindType;
    }

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
