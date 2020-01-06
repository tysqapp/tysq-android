package request;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import model.VideoModel;

/**
 * author       : frog
 * time         : 2019/5/19 上午10:25
 * desc         : 发布和更新文章
 * version      : 1.3.0
 */
public class PublishArticleReq {

    /**
     * title : 郭郭和俊俊
     * category_id : 1
     * content : <p>图片<img src={{img}}></p><p>视频<video src={{video}} controls></video></p><p>大叔大</p><p>音频<audio src={{audio}} controls></audio></p>
     * images : [3]
     * videos : [{"video":1,"cover":[3,2,5],"screenshot":[1,2]}]
     * audios : [1]
     * files : [1]
     * label : [1]
     * status:
     */
    @SerializedName("article_id")
    private String articleId;
    @SerializedName("title")
    private String title;
    @SerializedName("category_id")
    private int categoryId;
    @SerializedName("content")
    private String content;
    @SerializedName("images")
    private List<Integer> images;
    @SerializedName("videos")
    private List<VideoModel> videos;
    @SerializedName("audios")
    private List<Integer> audios;
    @SerializedName("files")
    private List<Integer> files;
    @SerializedName("label")
    private List<Integer> label;
    @SerializedName("status")
    private int status;

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

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<Integer> getImages() {
        return images;
    }

    public void setImages(List<Integer> images) {
        this.images = images;
    }

    public List<VideoModel> getVideos() {
        return videos;
    }

    public void setVideos(List<VideoModel> videos) {
        this.videos = videos;
    }

    public List<Integer> getAudios() {
        return audios;
    }

    public void setAudios(List<Integer> audios) {
        this.audios = audios;
    }

    public List<Integer> getFiles() {
        return files;
    }

    public void setFiles(List<Integer> files) {
        this.files = files;
    }

    public List<Integer> getLabel() {
        return label;
    }

    public void setLabel(List<Integer> label) {
        this.label = label;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
