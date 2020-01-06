package response.article;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * author       : frog
 * time         : 2019/4/17 下午2:59
 * desc         : 评论
 * version      : 1.3.0
 */
public class ArticleReviewResp {


    /**
     * article_comments : [{"id":"5cde807e17390b71e60413d6","content":"快下大雨了勒","commentator_id":7,"commentator_name":"han","time":1558085758,"icon_url":"http://192.168.0.33:8081/v1.0/file/redirect/0","article_id":"5cde786f17390b71e6040917","second_num":3,"image_url":[],"reply":[{"id":"5cde856917390b71e6041ae2","content":"是大大撒法","commentator_id":2,"commentator_name":"xxx","time":1558087017,"icon_url":"http://192.168.0.33:8081/v1.0/file/redirect/0","parent_id":"5cde807e17390b71e60413d6","father_id":"5cde807e17390b71e60413d6","image_url":[],"commented_id":7,"commented_name":"han","commented_icon":"http://192.168.0.33:8081/v1.0/file/redirect/0"},{"id":"5cde809a17390b71e604141e","content":"自己回复自己的铁憨憨","commentator_id":7,"commentator_name":"han","time":1558085786,"icon_url":"http://192.168.0.33:8081/v1.0/file/redirect/0","parent_id":"5cde807e17390b71e60413d6","father_id":"5cde808617390b71e60413e7","image_url":[],"commented_id":7,"commented_name":"han","commented_icon":"http://192.168.0.33:8081/v1.0/file/redirect/0"},{"id":"5cde808617390b71e60413e7","content":"好怕怕哦","commentator_id":7,"commentator_name":"han","time":1558085766,"icon_url":"http://192.168.0.33:8081/v1.0/file/redirect/0","parent_id":"5cde807e17390b71e60413d6","father_id":"5cde807e17390b71e60413d6","image_url":[],"commented_id":7,"commented_name":"han","commented_icon":"http://192.168.0.33:8081/v1.0/file/redirect/0"}]}]
     * total_number : 4
     * first_number : 1
     */

    @SerializedName("total_number")
    private int totalNumber;
    @SerializedName("first_number")
    private int firstNumber;
    @SerializedName("article_comments")
    private List<ArticleCommentsBean> articleComments;

    public int getTotalNumber() {
        return totalNumber;
    }

    public void setTotalNumber(int totalNumber) {
        this.totalNumber = totalNumber;
    }

    public int getFirstNumber() {
        return firstNumber;
    }

    public void setFirstNumber(int firstNumber) {
        this.firstNumber = firstNumber;
    }

    public List<ArticleCommentsBean> getArticleComments() {
        return articleComments;
    }

    public void setArticleComments(List<ArticleCommentsBean> articleComments) {
        this.articleComments = articleComments;
    }

    public static class ArticleCommentsBean {
        /**
         * id : 5cde807e17390b71e60413d6
         * content : 快下大雨了勒
         * commentator_id : 7
         * commentator_name : han
         * time : 1558085758
         * icon_url : http://192.168.0.33:8081/v1.0/file/redirect/0
         * article_id : 5cde786f17390b71e6040917
         * second_num : 3
         * image_url : []
         * reply : [{"id":"5cde856917390b71e6041ae2","content":"是大大撒法","commentator_id":2,"commentator_name":"xxx","time":1558087017,"icon_url":"http://192.168.0.33:8081/v1.0/file/redirect/0","parent_id":"5cde807e17390b71e60413d6","father_id":"5cde807e17390b71e60413d6","image_url":[],"commented_id":7,"commented_name":"han","commented_icon":"http://192.168.0.33:8081/v1.0/file/redirect/0"},{"id":"5cde809a17390b71e604141e","content":"自己回复自己的铁憨憨","commentator_id":7,"commentator_name":"han","time":1558085786,"icon_url":"http://192.168.0.33:8081/v1.0/file/redirect/0","parent_id":"5cde807e17390b71e60413d6","father_id":"5cde808617390b71e60413e7","image_url":[],"commented_id":7,"commented_name":"han","commented_icon":"http://192.168.0.33:8081/v1.0/file/redirect/0"},{"id":"5cde808617390b71e60413e7","content":"好怕怕哦","commentator_id":7,"commentator_name":"han","time":1558085766,"icon_url":"http://192.168.0.33:8081/v1.0/file/redirect/0","parent_id":"5cde807e17390b71e60413d6","father_id":"5cde807e17390b71e60413d6","image_url":[],"commented_id":7,"commented_name":"han","commented_icon":"http://192.168.0.33:8081/v1.0/file/redirect/0"}]
         */

        @SerializedName("id")
        private String id;
        @SerializedName("content")
        private String content;
        @SerializedName("commentator_id")
        private int commentatorId;
        @SerializedName("commentator_name")
        private String commentatorName;
        // 评论人等级
        @SerializedName("commentator_grade")
        private int commentatorGrade;
        @SerializedName("time")
        private long time;
        @SerializedName("icon_url")
        private String iconUrl;
        @SerializedName("article_id")
        private String articleId;
        @SerializedName("second_num")
        private int secondNum;
        @SerializedName("image_url")
        private List<ImageUrlBean> imageUrl;
        @SerializedName("reply")
        private List<ReplyBean> reply;
        @SerializedName("updated_at")
        private long updatedAt;

        // 是否可以删除
        private boolean isCanDelete;
        // 是否可以禁止评论
        private boolean isCanForbid;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getCommentatorId() {
            return commentatorId;
        }

        public void setCommentatorId(int commentatorId) {
            this.commentatorId = commentatorId;
        }

        public String getCommentatorName() {
            return commentatorName;
        }

        public void setCommentatorName(String commentatorName) {
            this.commentatorName = commentatorName;
        }

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }

        public String getIconUrl() {
            return iconUrl;
        }

        public void setIconUrl(String iconUrl) {
            this.iconUrl = iconUrl;
        }

        public String getArticleId() {
            return articleId;
        }

        public void setArticleId(String articleId) {
            this.articleId = articleId;
        }

        public int getSecondNum() {
            return secondNum;
        }

        public void setSecondNum(int secondNum) {
            this.secondNum = secondNum;
        }

        public List<ImageUrlBean> getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(List<ImageUrlBean> imageUrl) {
            this.imageUrl = imageUrl;
        }

        public List<ReplyBean> getReply() {
            return reply;
        }

        public void setReply(List<ReplyBean> reply) {
            this.reply = reply;
        }

        public long getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(long updatedAt) {
            this.updatedAt = updatedAt;
        }

        public boolean isCanDelete() {
            return isCanDelete;
        }

        public void setCanDelete(boolean canDelete) {
            isCanDelete = canDelete;
        }

        public boolean isCanForbid() {
            return isCanForbid;
        }

        public void setCanForbid(boolean canForbid) {
            isCanForbid = canForbid;
        }

        public int getCommentatorGrade() {
            return commentatorGrade;
        }

        public void setCommentatorGrade(int commentatorGrade) {
            this.commentatorGrade = commentatorGrade;
        }

        public static class ReplyBean {
            /**
             * id : 5cde856917390b71e6041ae2
             * content : 是大大撒法
             * commentator_id : 2
             * commentator_name : frogPower
             * time : 1558087017
             * icon_url : http://192.168.0.33:8081/v1.0/file/redirect/0
             * parent_id : 5cde807e17390b71e60413d6
             * father_id : 5cde807e17390b71e60413d6
             * image_url : []
             * commented_id : 7
             * commented_name : han
             * commented_icon : http://192.168.0.33:8081/v1.0/file/redirect/0
             */

            @SerializedName("id")
            private String id;
            @SerializedName("content")
            private String content;
            @SerializedName("commentator_id")
            private int commentatorId;
            @SerializedName("commentator_name")
            private String commentatorName;
            // 评论人等级
            @SerializedName("commentator_grade")
            private int commentatorGrade;
            @SerializedName("time")
            private long time;
            @SerializedName("icon_url")
            private String iconUrl;
            @SerializedName("parent_id")
            private String parentId;
            @SerializedName("father_id")
            private String fatherId;
            @SerializedName("commented_id")
            private int commentedId;
            @SerializedName("commented_name")
            private String commentedName;
            @SerializedName("commented_icon")
            private String commentedIcon;
            // 被评论人等级
            @SerializedName("commented_grade")
            private int commentedGrade;
            @SerializedName("image_url")
            private List<ImageUrlBean> imageUrl;

            private String articleId;

            // 是否可以删除
            private boolean isCanDelete;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public int getCommentatorId() {
                return commentatorId;
            }

            public void setCommentatorId(int commentatorId) {
                this.commentatorId = commentatorId;
            }

            public String getCommentatorName() {
                return commentatorName;
            }

            public void setCommentatorName(String commentatorName) {
                this.commentatorName = commentatorName;
            }

            public long getTime() {
                return time;
            }

            public void setTime(long time) {
                this.time = time;
            }

            public String getIconUrl() {
                return iconUrl;
            }

            public void setIconUrl(String iconUrl) {
                this.iconUrl = iconUrl;
            }

            public String getParentId() {
                return parentId;
            }

            public void setParentId(String parentId) {
                this.parentId = parentId;
            }

            public String getFatherId() {
                return fatherId;
            }

            public void setFatherId(String fatherId) {
                this.fatherId = fatherId;
            }

            public int getCommentedId() {
                return commentedId;
            }

            public void setCommentedId(int commentedId) {
                this.commentedId = commentedId;
            }

            public String getCommentedName() {
                return commentedName;
            }

            public void setCommentedName(String commentedName) {
                this.commentedName = commentedName;
            }

            public String getCommentedIcon() {
                return commentedIcon;
            }

            public void setCommentedIcon(String commentedIcon) {
                this.commentedIcon = commentedIcon;
            }

            public List<ImageUrlBean> getImageUrl() {
                return imageUrl;
            }

            public void setImageUrl(List<ImageUrlBean> imageUrl) {
                this.imageUrl = imageUrl;
            }

            public String getArticleId() {
                return articleId;
            }

            public void setArticleId(String articleId) {
                this.articleId = articleId;
            }

            public boolean isCanDelete() {
                return isCanDelete;
            }

            public void setCanDelete(boolean canDelete) {
                isCanDelete = canDelete;
            }

            public int getCommentatorGrade() {
                return commentatorGrade;
            }

            public void setCommentatorGrade(int commentatorGrade) {
                this.commentatorGrade = commentatorGrade;
            }

            public int getCommentedGrade() {
                return commentedGrade;
            }

            public void setCommentedGrade(int commentedGrade) {
                this.commentedGrade = commentedGrade;
            }
        }
    }

    public static class ImageUrlBean{

        /**
         * url : https://xxx.tysqapp.com/api/file_infos/800/redirect
         * original_url :
         */

        @SerializedName("url")
        private String url;
        @SerializedName("original_url")
        private String originalUrl;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getOriginalUrl() {
            return originalUrl;
        }

        public void setOriginalUrl(String originalUrl) {
            this.originalUrl = originalUrl;
        }
    }

}
