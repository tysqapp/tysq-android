package response.report;
/**
 * author       : liaozhenlin
 * time         : 2019/10/14 15:51
 * desc         :
 * version      : 1.5.0
 */
public class ArticleReportResp {
    private String title;
    private boolean isCheck = false;

    public ArticleReportResp(String title, boolean isCheck){
        this.title = title;
        this.isCheck = isCheck;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }
}
