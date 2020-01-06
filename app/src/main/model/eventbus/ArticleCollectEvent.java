package eventbus;

/**
 * author       : frog
 * time         : 2019-08-13 10:39
 * desc         : 文章收藏更新事件
 * 1、【文章详情->我的收藏】
 * version      : 1.3.0
 */
public class ArticleCollectEvent {

    private String articleId;

    public ArticleCollectEvent(String articleId) {
        this.articleId = articleId;
    }

    public String getArticleId() {
        return articleId;
    }
}
