package eventbus;

/**
 * author       : frog
 * time         : 2019/5/26 上午9:49
 * desc         : 更新文章信息
 * version      : 1.3.0
 */
public class UpdateArticleInfoEvent {

    public static final int NONE = -1;

    private final String articleId;
    private long readNum = NONE;
    private long commentNum = NONE;

    public UpdateArticleInfoEvent(String articleId) {
        this.articleId = articleId;
    }

    public String getArticleId() {
        return articleId;
    }

    public long getReadNum() {
        return readNum;
    }

    public void setReadNum(long readNum) {
        this.readNum = readNum;
    }

    public long getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(long commentNum) {
        this.commentNum = commentNum;
    }
}
