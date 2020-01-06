package response.report;

import com.google.gson.annotations.SerializedName;

public class ReportDetailResp {

    /**
     * report_id : 5da58415f44ff7fd82033d89
     * article_id : 5d9c028171a9430d3754fb11
     * title : 测试水印
     * type : 广告或垃圾信息
     * note : 可以的
     * result :
     * process_basis :
     */
    @SerializedName("report_id")
    private String reportId;
    @SerializedName("article_id")
    private String articleId;
    @SerializedName("title")
    private String title;
    @SerializedName("type")
    private String type;
    @SerializedName("note")
    private String note;
    @SerializedName("result")
    private String result;
    @SerializedName("process_reason")
    private String processReason;

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getProcessReason() {
        return processReason;
    }

    public void setProcessReason(String processReason) {
        this.processReason = processReason;
    }
}
