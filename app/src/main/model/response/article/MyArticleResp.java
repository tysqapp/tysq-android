package response.article;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * author       : frog
 * time         : 2019/5/27 上午10:52
 * desc         : 我的文章
 * version      : 1.3.0
 */
public class MyArticleResp {

    /**
     * articles_info : [{"article_id":"5ce7aa6d1a162670b2ce5eef","account_id":0,"category_id":0,"account":"","title":"别来别来","content":"不要来\u200b","video_url":[],"picture_url":["http://192.168.0.33:8081/v1.0/file/redirect/140"],"read_number":9,"admire_number":0,"admire_status":0,"comment_number":10,"created_time":1558686317,"status":1,"label":[{"label_id":3,"label_name":"golang"}],"operation":false},{"article_id":"5ce764071a162670b2ce0d1e","account_id":0,"category_id":0,"account":"","title":"大安卓测试","content":"哈哈哈\u200b\u200b\u200b","video_url":[{"video":"http://192.168.0.33:8081/v1.0/file/redirect/139","cover":"","screen_shot":["http://192.168.0.33:8081/v1.0/file/redirect/19","http://192.168.0.33:8081/v1.0/file/redirect/20","http://192.168.0.33:8081/v1.0/file/redirect/21"]}],"picture_url":["http://192.168.0.33:8081/v1.0/file/redirect/130","http://192.168.0.33:8081/v1.0/file/redirect/140"],"read_number":17,"admire_number":0,"admire_status":0,"comment_number":22,"created_time":1558668295,"status":1,"label":[{"label_id":3,"label_name":"golang"}],"operation":false},{"article_id":"5ce63ef41a162670b2cd5a5c","account_id":0,"category_id":0,"account":"","title":"大安卓测试😁","content":"测试\u200b","video_url":[],"picture_url":["http://192.168.0.33:8081/v1.0/file/redirect/141"],"read_number":0,"admire_number":0,"admire_status":0,"comment_number":0,"created_time":1558593268,"status":1,"label":[],"operation":false},{"article_id":"5ce37562ef465721d9fe8f33","account_id":0,"category_id":0,"account":"","title":"同乐","content":"\u200b\u200b","video_url":[],"picture_url":["http://192.168.0.33:8081/v1.0/file/redirect/141","http://192.168.0.33:8081/v1.0/file/redirect/140"],"read_number":0,"admire_number":0,"admire_status":0,"comment_number":0,"created_time":1558410594,"status":1,"label":[],"operation":false},{"article_id":"5ce37300ef465721d9fe8cb7","account_id":0,"category_id":0,"account":"","title":"来咯来咯了","content":"\u200b","video_url":[],"picture_url":["http://192.168.0.33:8081/v1.0/file/redirect/129"],"read_number":0,"admire_number":0,"admire_status":0,"comment_number":0,"created_time":1558409984,"status":1,"label":[{"label_id":25,"label_name":""}],"operation":false},{"article_id":"5ce35902ef465721d9fe689c","account_id":0,"category_id":0,"account":"","title":"安卓 大安卓","content":"\u200b\u200b\u200b六六六","video_url":[{"video":"http://192.168.0.33:8081/v1.0/file/redirect/139","cover":"","screen_shot":["http://192.168.0.33:8081/v1.0/file/redirect/19","http://192.168.0.33:8081/v1.0/file/redirect/20","http://192.168.0.33:8081/v1.0/file/redirect/21"]}],"picture_url":["http://192.168.0.33:8081/v1.0/file/redirect/130"],"read_number":0,"admire_number":0,"admire_status":0,"comment_number":0,"created_time":1558403330,"status":1,"label":[{"label_id":28,"label_name":""}],"operation":false},{"article_id":"5ce250f4ef465721d9fdddf1","account_id":0,"category_id":0,"account":"","title":"达古冰川","content":"封天印地火女发","video_url":[],"picture_url":[],"read_number":0,"admire_number":0,"admire_status":0,"comment_number":0,"created_time":1558335732,"status":1,"label":[],"operation":false},{"article_id":"5ce0fe83673d0617e6950300","account_id":0,"category_id":0,"account":"","title":"福特翼虎","content":"闺女腹肌\u200b","video_url":[],"picture_url":["http://192.168.0.33:8081/v1.0/file/redirect/130"],"read_number":0,"admire_number":0,"admire_status":0,"comment_number":0,"created_time":1558249091,"status":1,"label":[{"label_id":28,"label_name":""},{"label_id":25,"label_name":""}],"operation":false},{"article_id":"5ce0ce82673d0617e694e9ea","account_id":0,"category_id":0,"account":"","title":"写文章","content":"好词佳句\u200b","video_url":[],"picture_url":[],"read_number":0,"admire_number":0,"admire_status":0,"comment_number":0,"created_time":1558236802,"status":1,"label":[],"operation":false},{"article_id":"5ce0cded673d0617e694e983","account_id":0,"category_id":0,"account":"","title":"写文章","content":"独孤皇后吧\u200b飞鸿花海\u200b\u200b","video_url":[],"picture_url":["http://192.168.0.33:8081/v1.0/file/redirect/129","http://192.168.0.33:8081/v1.0/file/redirect/129","http://192.168.0.33:8081/v1.0/file/redirect/130"],"read_number":0,"admire_number":0,"admire_status":0,"comment_number":0,"created_time":1558236653,"status":1,"label":[{"label_id":28,"label_name":""},{"label_id":25,"label_name":""}],"operation":false}]
     * total_num : 12
     */

    @SerializedName("total_num")
    private int totalNum;
    @SerializedName("articles_info")
    private List<ArticlesInfoBean> articlesInfo;

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public List<ArticlesInfoBean> getArticlesInfo() {
        return articlesInfo;
    }

    public void setArticlesInfo(List<ArticlesInfoBean> articlesInfo) {
        this.articlesInfo = articlesInfo;
    }

    public static class ArticlesInfoBean {


        /**
         * status_name : 待审核
         * isDeleted : false
         * comment_number : 0
         * status : -1
         * cover_type : video
         * id : 5d9008f80db17597b0abbe62
         * created_time : 1569720568
         * title : 测试视频文章在个人文章中的内容显示
         * read_number : 2
         * cover_url : http://192.168.0.194:8082/api//file_infos/1132/redirect
         * labels : ["好喜欢你","请问请问","0925-4","0925-17"]
         * reason :
         * content : 这是一个黑金高逼格机械键盘，你心动了吗？
         */

        @SerializedName("status_name")
        private String statusName;
        @SerializedName("isDeleted")
        private boolean isDeleted;
        @SerializedName("comment_number")
        private int commentNumber;
        @SerializedName("status")
        private int status;
        @SerializedName("cover_type")
        private String coverType;
        @SerializedName("id")
        private String id;
        @SerializedName("created_time")
        private int createdTime;
        @SerializedName("title")
        private String title;
        @SerializedName("read_number")
        private int readNumber;
        @SerializedName("cover_url")
        private String coverUrl;
        @SerializedName("reason")
        private String reason;
        @SerializedName("content")
        private String content;
        @SerializedName("labels")
        private List<String> labels;

        public String getStatusName() {
            return statusName;
        }

        public void setStatusName(String statusName) {
            this.statusName = statusName;
        }

        public boolean isIsDeleted() {
            return isDeleted;
        }

        public void setIsDeleted(boolean isDeleted) {
            this.isDeleted = isDeleted;
        }

        public int getCommentNumber() {
            return commentNumber;
        }

        public void setCommentNumber(int commentNumber) {
            this.commentNumber = commentNumber;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getCoverType() {
            return coverType;
        }

        public void setCoverType(String coverType) {
            this.coverType = coverType;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getCreatedTime() {
            return createdTime;
        }

        public void setCreatedTime(int createdTime) {
            this.createdTime = createdTime;
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

        public String getCoverUrl() {
            return coverUrl;
        }

        public void setCoverUrl(String coverUrl) {
            this.coverUrl = coverUrl;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
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
}
