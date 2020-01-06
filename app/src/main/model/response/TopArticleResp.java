package response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * author       : liaozhenlin
 * time         : 2019/11/11 15:01
 * desc         : 置顶文章返回信息
 * version      : 1.5.0
 */
public class TopArticleResp {
    @SerializedName("articles")
    private List<TopArticleBean> topArticleBeanList;

    public List<TopArticleBean> getTopArticleBeanList() {
        return topArticleBeanList;
    }

    public void setTopArticleBeanList(List<TopArticleBean> topArticleBeanList) {
        this.topArticleBeanList = topArticleBeanList;
    }

    public static class TopArticleBean{
        @SerializedName("article_id")
        private String articleId;
        @SerializedName("title")
        private String title;

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
    }

}
