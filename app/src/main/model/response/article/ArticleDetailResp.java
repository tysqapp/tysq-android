package response.article;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import response.LabelResp;

/**
 * author       : frog
 * time         : 2019/5/20 上午10:21
 * desc         : 文章详情
 * version      : 1.3.0
 */
public class ArticleDetailResp {

    /**
     * article_info : {"article_id":"5ce0fe83673d0617e6950300","account_id":1,"category_id":9,"account":"zinc","head_url":"http://192.168.0.33:8081/v1.0/file/redirect/15","title":"福特翼虎","content":"<p><h3>闺女<\/h3><\/p>\n<br>\n<p><hr/><h3><\/h3><\/p>\n<br>\n<p>腹肌<\/p>\n<br>\n<p><img src=http://192.168.0.33:8081/v1.0/file/redirect/130><\/p>\n<p>\u200b<\/p>\n","read_number":14,"check_status":0,"admire_number":0,"admire_status":0,"comment_number":0,"created_time":1558249091,"status":1,"label":[{"label_id":25,"label_name":"王者荣耀"},{"label_id":28,"label_name":"小猫"}],"operation":false}
     */

    @SerializedName("article_info")
    private ArticleInfoBean articleInfo;

    public ArticleInfoBean getArticleInfo() {
        return articleInfo;
    }

    public void setArticleInfo(ArticleInfoBean articleInfo) {
        this.articleInfo = articleInfo;
    }

    public static class ArticleInfoBean {

        /**
         * article_id : 5ce27910ef465721d9fe03be
         * account_id : 1
         * category_id : 13
         * account : frog
         * head_url : http://192.168.0.33:8081/v1.0/file/redirect/15
         * title : 种类多多
         * content : <br>
         * <p><video src=http://192.168.0.33:8081/v1.0/file/redirect/139 controls></video></p>
         * <p>​</p>
         * <br>
         * <p><hr/></p>
         * <br>
         * <p><img src=http://192.168.0.33:8081/v1.0/file/redirect/129></p>
         * <p>​</p>
         * <br>
         * <p><hr/></p>
         * <br>
         * <p><img src=http://192.168.0.33:8081/v1.0/file/redirect/130></p>
         * <p>​</p>
         * <br>
         * <p><hr/></p>
         * <br>
         * <p><video src=http://192.168.0.33:8081/v1.0/file/redirect/139 controls></video></p>
         * <p>​</p>
         * <br>
         * <p><hr/></p>
         * <br>
         * <p><audio src=http://192.168.0.33:8081/v1.0/file/redirect/132 controls></audio></p>
         * <p>​</p>
         * <br>
         * <p><hr/></p>
         * <br>
         * <p><video src=http://192.168.0.33:8081/v1.0/file/redirect/139 controls></video></p>
         * <p>​</p>
         * <p>
         * content_original : <br>
         * <p><video src={{video}} controls></video></p>
         * <p>​</p>
         * <br>
         * <p><hr/></p>
         * <br>
         * <p><img src={{img}}></p>
         * <p>​</p>
         * <br>
         * <p><hr/></p>
         * <br>
         * <p><img src={{img}}></p>
         * <p>​</p>
         * <br>
         * <p><hr/></p>
         * <br>
         * <p><video src={{video}} controls></video></p>
         * <p>​</p>
         * <br>
         * <p><hr/></p>
         * <br>
         * <p><audio src={{audio}} controls></audio></p>
         * <p>​</p>
         * <br>
         * <p><hr/></p>
         * <br>
         * <p><video src={{video}} controls></video></p>
         * <p>​</p>
         * <p>
         * videos : [{"video":139,"cover":[],"screenshot":[19,20,21]},{"video":139,"cover":[],"screenshot":[19,20,21]},{"video":139,"cover":[],"screenshot":[19,20,21]}]
         * audios : [132]
         * images : [129,130]
         * read_number : 62
         * check_status : 0
         * admire_number : 0
         * admire_status : 0
         * comment_number : 32568
         * created_time : 1558346000
         * status : 1
         * label : [{"label_id":28,"label_name":"小猫"},{"label_id":29,"label_name":"是否是"}]
         * operation : false
         * limit_score: 0,
         * is_satisfy: false,
         * collect_status: false,
         * is_need_captcha: false
         */

        @SerializedName("article_id")
        private String articleId;
        @SerializedName("account_id")
        private int accountId;
        @SerializedName("category_id")
        private int categoryId;
        @SerializedName("parent_category_name")
        private String parentCategoryName;
        @SerializedName("category_name")
        private String categoryName;
        @SerializedName("account")
        private String account;
        @SerializedName("head_url")
        private String headUrl;
        @SerializedName("title")
        private String title;
        @SerializedName("content")
        private String content;
        @SerializedName("content_original")
        private String contentOriginal;
        @SerializedName("read_number")
        private long readNumber;
        @SerializedName("check_status")
        private int checkStatus;
        @SerializedName("admire_number")
        private long admireNumber;
        @SerializedName("admire_status")
        private int admireStatus;
        @SerializedName("comment_number")
        private long commentNumber;
        @SerializedName("created_time")
        private long createdTime;
        @SerializedName("status")
        private int status;
        @SerializedName("operation")
        private boolean operation;
        @SerializedName("videos")
        private List<VideosBean> videos;
        @SerializedName("audios")
        private List<MediaBean> audios;
        @SerializedName("images")
        private List<MediaBean> images;
        @SerializedName("label")
        private List<LabelResp.LabelItem> label;
        @SerializedName("article_link")
        private String articleLink;
        @SerializedName("limit_score")
        private int limitScore;
        @SerializedName("is_satisfy")
        private boolean isSatisfy;
        @SerializedName("collect_status")
        private boolean collectStatus;
        @SerializedName("is_need_captcha")
        private boolean isNeedCaptcha;
        @SerializedName("reason")
        private String reason;
        @SerializedName("status_name")
        private String statusName;
        @SerializedName("grade")
        private int grade;
        @SerializedName("top_position")
        private int topPosition;

        public int getTopPosition() {
            return topPosition;
        }

        public void setTopPosition(int topPosition) {
            this.topPosition = topPosition;
        }

        public int getGrade() {
            return grade;
        }

        public void setGrade(int grade) {
            this.grade = grade;
        }

        public String getStatusName() {
            return statusName;
        }

        public void setStatusName(String statusName) {
            this.statusName = statusName;
        }

        public String getParentCategoryName() {
            return parentCategoryName;
        }

        public void setParentCategoryName(String parentCategoryName) {
            this.parentCategoryName = parentCategoryName;
        }

        public String getCategoryName() {
            return categoryName;
        }

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }

        public String getArticleId() {
            return articleId;
        }

        public void setArticleId(String articleId) {
            this.articleId = articleId;
        }

        public int getAccountId() {
            return accountId;
        }

        public void setAccountId(int accountId) {
            this.accountId = accountId;
        }

        public int getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(int categoryId) {
            this.categoryId = categoryId;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getHeadUrl() {
            return headUrl;
        }

        public void setHeadUrl(String headUrl) {
            this.headUrl = headUrl;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getContentOriginal() {
            return contentOriginal;
        }

        public void setContentOriginal(String contentOriginal) {
            this.contentOriginal = contentOriginal;
        }

        public int getCheckStatus() {
            return checkStatus;
        }

        public void setCheckStatus(int checkStatus) {
            this.checkStatus = checkStatus;
        }

        public int getAdmireStatus() {
            return admireStatus;
        }

        public void setAdmireStatus(int admireStatus) {
            this.admireStatus = admireStatus;
        }

        public long getCreatedTime() {
            return createdTime;
        }

        public void setCreatedTime(long createdTime) {
            this.createdTime = createdTime;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public boolean isOperation() {
            return operation;
        }

        public void setOperation(boolean operation) {
            this.operation = operation;
        }

        public List<VideosBean> getVideos() {
            return videos;
        }

        public void setVideos(List<VideosBean> videos) {
            this.videos = videos;
        }

        public List<MediaBean> getAudios() {
            return audios;
        }

        public void setAudios(List<MediaBean> audios) {
            this.audios = audios;
        }

        public List<MediaBean> getImages() {
            return images;
        }

        public void setImages(List<MediaBean> images) {
            this.images = images;
        }

        public List<LabelResp.LabelItem> getLabel() {
            return label;
        }

        public void setLabel(List<LabelResp.LabelItem> label) {
            this.label = label;
        }

        public long getReadNumber() {
            return readNumber;
        }

        public void setReadNumber(long readNumber) {
            this.readNumber = readNumber;
        }

        public long getAdmireNumber() {
            return admireNumber;
        }

        public void setAdmireNumber(long admireNumber) {
            this.admireNumber = admireNumber;
        }

        public long getCommentNumber() {
            return commentNumber;
        }

        public void setCommentNumber(long commentNumber) {
            this.commentNumber = commentNumber;
        }

        public String getArticleLink() {
            return articleLink;
        }

        public void setArticleLink(String articleLink) {
            this.articleLink = articleLink;
        }

        public int getLimitScore() {
            return limitScore;
        }

        public void setLimitScore(int limitScore) {
            this.limitScore = limitScore;
        }

        public boolean isSatisfy() {
            return isSatisfy;
        }

        public void setSatisfy(boolean satisfy) {
            isSatisfy = satisfy;
        }

        public boolean isCollectStatus() {
            return collectStatus;
        }

        public void setCollectStatus(boolean collectStatus) {
            this.collectStatus = collectStatus;
        }

        public boolean isNeedCaptcha() {
            return isNeedCaptcha;
        }

        public void setNeedCaptcha(boolean needCaptcha) {
            isNeedCaptcha = needCaptcha;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

        public static class VideosBean {
            /**
             * video : 139
             * cover : []
             * screenshot : [19,20,21]
             */

            @SerializedName("video")
            private MediaBean video;
            @SerializedName("cover")
            private List<MediaBean> cover;
            @SerializedName("screen_shot")
            private List<MediaBean> screenshot;
            @SerializedName("status")
            private int status;

            public MediaBean getVideo() {
                return video;
            }

            public void setVideo(MediaBean video) {
                this.video = video;
            }

            public List<MediaBean> getCover() {
                return cover;
            }

            public void setCover(List<MediaBean> cover) {
                this.cover = cover;
            }

            public List<MediaBean> getScreenshot() {
                return screenshot;
            }

            public void setScreenshot(List<MediaBean> screenshot) {
                this.screenshot = screenshot;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

        }
    }

    public static class MediaBean {
        private int id;
        private String url;

        private int width;
        private int height;

        private String name;

        @SerializedName("original_url")
        private String originalUrl;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public String getOriginalUrl() {
            return originalUrl;
        }

        public void setOriginalUrl(String originalUrl) {
            this.originalUrl = originalUrl;
        }
    }

}
