package response;

import com.google.gson.annotations.SerializedName;

/**
 * author       : frog
 * time         : 2019-07-23 11:44
 * desc         :
 * version      : 1.3.0
 */
public class ConfigurationResp {

    /**
     * article_by_reading : 10
     * article_by_commenting : 3
     * rec_register_click : 100
     * rec_register_success : 10000
     * register_score : 50
     * activate_email : 500
     * read_article : -10
     * read_article_limit_day : 7
     * comment_article : -3
     * create_article : -100
     * register_score_max_number : 5
     */

    @SerializedName("article_by_reading")
    private int articleByReading;
    @SerializedName("article_by_commenting")
    private int articleByCommenting;
    @SerializedName("rec_register_click")
    private int recRegisterClick;
    @SerializedName("rec_register_success")
    private int recRegisterSuccess;
    @SerializedName("register_score")
    private int registerScore;
    @SerializedName("activate_email")
    private int activateEmail;
    @SerializedName("read_article")
    private int readArticle;
    @SerializedName("read_article_limit_day")
    private int readArticleLimitDay;
    @SerializedName("comment_article")
    private int commentArticle;
    @SerializedName("create_article")
    private int createArticle;
    @SerializedName("register_score_max_number")
    private int registerScoreMaxNumber;

    public int getArticleByReading() {
        return articleByReading;
    }

    public void setArticleByReading(int articleByReading) {
        this.articleByReading = articleByReading;
    }

    public int getArticleByCommenting() {
        return articleByCommenting;
    }

    public void setArticleByCommenting(int articleByCommenting) {
        this.articleByCommenting = articleByCommenting;
    }

    public int getRecRegisterClick() {
        return recRegisterClick;
    }

    public void setRecRegisterClick(int recRegisterClick) {
        this.recRegisterClick = recRegisterClick;
    }

    public int getRecRegisterSuccess() {
        return recRegisterSuccess;
    }

    public void setRecRegisterSuccess(int recRegisterSuccess) {
        this.recRegisterSuccess = recRegisterSuccess;
    }

    public int getRegisterScore() {
        return registerScore;
    }

    public void setRegisterScore(int registerScore) {
        this.registerScore = registerScore;
    }

    public int getActivateEmail() {
        return activateEmail;
    }

    public void setActivateEmail(int activateEmail) {
        this.activateEmail = activateEmail;
    }

    public int getReadArticle() {
        return readArticle;
    }

    public void setReadArticle(int readArticle) {
        this.readArticle = readArticle;
    }

    public int getReadArticleLimitDay() {
        return readArticleLimitDay;
    }

    public void setReadArticleLimitDay(int readArticleLimitDay) {
        this.readArticleLimitDay = readArticleLimitDay;
    }

    public int getCommentArticle() {
        return commentArticle;
    }

    public void setCommentArticle(int commentArticle) {
        this.commentArticle = commentArticle;
    }

    public int getCreateArticle() {
        return createArticle;
    }

    public void setCreateArticle(int createArticle) {
        this.createArticle = createArticle;
    }

    public int getRegisterScoreMaxNumber() {
        return registerScoreMaxNumber;
    }

    public void setRegisterScoreMaxNumber(int registerScoreMaxNumber) {
        this.registerScoreMaxNumber = registerScoreMaxNumber;
    }
}
