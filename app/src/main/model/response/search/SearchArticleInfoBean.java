package response.search;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchArticleInfoBean {

    /**
     * id : 5da57030b3c27b4900c23735
     * title : 随机title，数字：470812212089063011
     * read_number : 0
     * author_id : 1001
     * author_name : xxx
     * created_time : 1571123251
     * comment_number : 1
     * cover_url :
     * original_url :
     * cover_type :
     * content : 随机content，数字：1335978243951296839
     * labels : []
     */

    private String id;
    private String title;
    @SerializedName("read_number")
    private int readNumber;
    @SerializedName("author_id")
    private int authorId;
    @SerializedName("author_name")
    private String authorName;
    @SerializedName("created_time")
    private int createdTime;
    @SerializedName("comment_number")
    private int commentNumber;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getReadNumber() {
        return readNumber;
    }

    public void setReadNumber(int readNumber) {
        this.readNumber = readNumber;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public int getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(int createdTime) {
        this.createdTime = createdTime;
    }

    public int getCommentNumber() {
        return commentNumber;
    }

    public void setCommentNumber(int commentNumber) {
        this.commentNumber = commentNumber;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
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
