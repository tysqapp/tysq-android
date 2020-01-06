package request;

import com.google.gson.annotations.SerializedName;

/**
 * author       : liaozhenlin
 * time         : 2019/10/14 12:17
 * desc         :
 * version      : 1.5.0
 */
public class ArticleReportReq {
    @SerializedName("id")
    private String id;
    @SerializedName("report_type")
    private String reportType;
    @SerializedName("note")
    private String note;

    public ArticleReportReq(String id, String reportType, String note){
        this.id = id;
        this.reportType = reportType;
        this.note = note;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
