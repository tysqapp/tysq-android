package eventbus;

/**
 * author       : frog
 * time         : 2019-09-24 15:17
 * desc         : 文章审核完毕
 * version      : 1.0.0
 */
public class ExamOverEvent {

    private String articleId;

    public ExamOverEvent(String articleId) {
        this.articleId = articleId;
    }

    public String getArticleId() {
        return articleId;
    }

}
