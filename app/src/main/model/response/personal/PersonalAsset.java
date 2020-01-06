package response.personal;

import com.google.gson.annotations.SerializedName;

public class PersonalAsset {

    /**
     * score : 9418
     * hot_coin : 1200
     * grade : 4
     * name : lv4
     * invited : 2
     * article_num : 41233
     * comment_num : 39
     * collect_num : 10
     * fan_num : 4
     * attention_num : 10
     */

    @SerializedName("score")
    private int score;
    @SerializedName("hot_coin")
    private int hotCoin;
    @SerializedName("grade")
    private int grade;
    @SerializedName("name")
    private String name;
    @SerializedName("invited")
    private int invited;
    @SerializedName("article_num")
    private int articleNum;
    @SerializedName("comment_num")
    private int commentNum;
    @SerializedName("collect_num")
    private int collectNum;
    @SerializedName("fan_num")
    private int fanNum;
    @SerializedName("attention_num")
    private int attentionNum;

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getHotCoin() {
        return hotCoin;
    }

    public void setHotCoin(int hotCoin) {
        this.hotCoin = hotCoin;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getInvited() {
        return invited;
    }

    public void setInvited(int invited) {
        this.invited = invited;
    }

    public int getArticleNum() {
        return articleNum;
    }

    public void setArticleNum(int articleNum) {
        this.articleNum = articleNum;
    }

    public int getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(int commentNum) {
        this.commentNum = commentNum;
    }

    public int getCollectNum() {
        return collectNum;
    }

    public void setCollectNum(int collectNum) {
        this.collectNum = collectNum;
    }

    public int getFanNum() {
        return fanNum;
    }

    public void setFanNum(int fanNum) {
        this.fanNum = fanNum;
    }

    public int getAttentionNum() {
        return attentionNum;
    }

    public void setAttentionNum(int attentionNum) {
        this.attentionNum = attentionNum;
    }
}
