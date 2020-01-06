package response.home;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * author       : frog
 * time         : 2019/4/26 下午5:52
 * desc         : 文章列表
 * version      : 1.3.0
 */
public class ArticleResp {

    @SerializedName("articles")
    private List<ArticleInfo> articleInfoList;

    @SerializedName("total_num")
    private long totalNum;

    public List<ArticleInfo> getArticleInfoList() {
        return articleInfoList;
    }

    public void setArticleInfoList(List<ArticleInfo> articleInfoList) {
        this.articleInfoList = articleInfoList;
    }

    public long getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(long totalNum) {
        this.totalNum = totalNum;
    }
}
