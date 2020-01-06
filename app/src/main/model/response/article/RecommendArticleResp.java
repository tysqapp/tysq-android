package response.article;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * author       : frog
 * time         : 2019/5/20 上午10:41
 * desc         : 推荐文章
 * version      : 1.3.0
 */
public class RecommendArticleResp {

    @SerializedName("articles_info")
    private List<ArticlesInfoBean> articlesInfo;

    public List<ArticlesInfoBean> getArticlesInfo() {
        return articlesInfo;
    }

    public void setArticlesInfo(List<ArticlesInfoBean> articlesInfo) {
        this.articlesInfo = articlesInfo;
    }

    public static class ArticlesInfoBean {
        /**
         * article_id : 5cdce6ef17390b71e602e138
         * account_id : 100
         * category_id : 3
         * account : ding_testAccount
         * title : 士大夫
         * content :
         * video_url : []
         * picture_url : ["http://192.168.0.33:8081/v1.0/file/redirect/56"]
         * read_number : 5
         * admire_number : 0
         * admire_status : 0
         * comment_number : 0
         * created_time : 1557980911
         * status : 1
         * label : [{"label_id":43,"label_name":" 是否是"}]
         * operation : false
         */

        @SerializedName("article_id")
        private String articleId;
        @SerializedName("account_id")
        private int accountId;
        @SerializedName("category_id")
        private int categoryId;
        @SerializedName("account")
        private String account;
        @SerializedName("title")
        private String title;
        @SerializedName("content")
        private String content;
        @SerializedName("read_number")
        private long readNumber;
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
        @SerializedName("video_url")
        private List<?> videoUrl;
        @SerializedName("picture_url")
        private List<String> pictureUrl;
        @SerializedName("label")
        private List<LabelBean> label;

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

        public int getAdmireStatus() {
            return admireStatus;
        }

        public long getCommentNumber() {
            return commentNumber;
        }

        public void setCommentNumber(long commentNumber) {
            this.commentNumber = commentNumber;
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

        public List<?> getVideoUrl() {
            return videoUrl;
        }

        public void setVideoUrl(List<?> videoUrl) {
            this.videoUrl = videoUrl;
        }

        public List<String> getPictureUrl() {
            return pictureUrl;
        }

        public void setPictureUrl(List<String> pictureUrl) {
            this.pictureUrl = pictureUrl;
        }

        public List<LabelBean> getLabel() {
            return label;
        }

        public void setLabel(List<LabelBean> label) {
            this.label = label;
        }

        public static class LabelBean {
            /**
             * label_id : 43
             * label_name :  是否是
             */

            @SerializedName("label_id")
            private int labelId;
            @SerializedName("label_name")
            private String labelName;

            public int getLabelId() {
                return labelId;
            }

            public void setLabelId(int labelId) {
                this.labelId = labelId;
            }

            public String getLabelName() {
                return labelName;
            }

            public void setLabelName(String labelName) {
                this.labelName = labelName;
            }
        }
    }
}
