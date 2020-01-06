package eventbus;

import vo.article.ArticleDetailVO;

/**
 * author       : frog
 * time         : 2019/5/23 上午11:10
 * desc         : 评论添加
 * version      : 1.3.0
 */
public class ReviewAddEvent {

    private final String from;

    private final int pos;
    private final ArticleDetailVO articleDetailVO;

    private final String articleId;

    /**
     *
     * @param articleId 文章id
     * @param from
     * @param pos
     * @param articleDetailVO
     */
    public ReviewAddEvent(String articleId,
                          String from,
                          int pos,
                          ArticleDetailVO articleDetailVO) {
        this.from = from;
        this.pos = pos;
        this.articleDetailVO = articleDetailVO;
        this.articleId = articleId;
    }

    public String getArticleId() {
        return articleId;
    }

    public String getFrom() {
        return from;
    }

    public int getPos() {
        return pos;
    }

    public ArticleDetailVO getArticleDetailVO() {
        return articleDetailVO;
    }
}
