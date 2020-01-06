package eventbus;

/**
 * author       : frog
 * time         : 2019/5/29 下午2:14
 * desc         : 删除文章事件
 * 1、【文章详情—>文章List页】
 * 2、【文章详情->我的文章页】
 * 3、【文章详情->我的收藏】
 * version      : 1.3.0
 */
public class DeleteArticleEvent {

    private String articleId;

    public DeleteArticleEvent(String articleId) {
        this.articleId = articleId;
    }

    public String getArticleId() {
        return articleId;
    }
}
