package response.search;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchResultResp {
    @SerializedName("type")
    private String type;
    @SerializedName("keyword")
    private String keyWord;
    @SerializedName("articles")
    private List<SearchArticleInfoBean> articlesList;
    @SerializedName("users")
    private List<SearchUsersInfoBean> usersList;
    @SerializedName("count")
    private int count;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public List<SearchArticleInfoBean> getArticlesList() {
        return articlesList;
    }

    public void setArticlesList(List<SearchArticleInfoBean> articlesList) {
        this.articlesList = articlesList;
    }

    public List<SearchUsersInfoBean> getUsersList() {
        return usersList;
    }

    public void setUsersList(List<SearchUsersInfoBean> usersList) {
        this.usersList = usersList;
    }
}
