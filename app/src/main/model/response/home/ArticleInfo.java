package response.home;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * author       : frog
 * time         : 2019/4/26 下午5:52
 * desc         : 文章信息
 * version      : 1.3.0
 */
public class ArticleInfo {

    // 视频
    public static final int VIDEO = 1;
    // 图像
    public static final int IMAGE = 2;
    // 文字
    public static final int TEXT = 3;
    // 公告
    public static final int AD = 4;
    // 排序
    public static final int SORT = 5;
    // 切换页面
    public static final int PAGE = 6;
    //广告位
    public static final int ADVERTISEMENT = 7;
    //置顶文章
    public static final int TOP_ARTICLE = 8;
    //下拉加载更多
    public static final int LOAD_MORE = 9;

    // 文章类型
    private int type;

    /**
     * id : 5d72020dbfb717f0f0ad0399
     * title : 测试写文章
     * read_number : 0
     * author_name : 小爱心
     * created_time : 1567752717
     * comment_number : 1
     * cover_url : http://47.244.130.115:8000/api/v1/file_infos/925/redirect
     * cover_type : image
     * content : 什么是思域;
     * labels : ["私域流量"]
     */

    @SerializedName("id")
    private String id;
    @SerializedName("title")
    private String title;
    @SerializedName("read_number")
    private long readNumber;
    @SerializedName("author_name")
    private String authorName;
    @SerializedName("author_id")
    private int authorId;
    @SerializedName("created_time")
    private long createdTime;
    @SerializedName("comment_number")
    private long commentNumber;
    @SerializedName("cover_url")
    private String coverUrl;
    @SerializedName("original_url")
    private String originalUrl;
    @SerializedName("cover_type")
    private String coverType;
    @SerializedName("content")
    private String content;
    @SerializedName("labels")
    private List<String> labels;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getReadNumber() {
        return readNumber;
    }

    public void setReadNumber(long readNumber) {
        this.readNumber = readNumber;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }

    public long getCommentNumber() {
        return commentNumber;
    }

    public void setCommentNumber(long commentNumber) {
        this.commentNumber = commentNumber;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getCoverType() {
        return coverType;
    }

    public void setCoverType(String coverType) {
        this.coverType = coverType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }
}
