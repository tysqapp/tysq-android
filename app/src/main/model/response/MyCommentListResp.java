package response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import response.article.ArticleReviewResp;

/**
 * author       : frog
 * time         : 2019/5/26 下午12:02
 * desc         : 我的评论列表
 * version      : 1.3.0
 */
public class MyCommentListResp {

    /**
     * comment_info : [{"id":"5ce9faa91810cc4b185c3c0c","content":"哈哈","commented_id":2,"commented_name":"猪猪侠007","respond_content":"打算","time":1558837929,"author_id":2,"article_id":"5ce763381a162670b2ce0bed","title":"打算","image_url":null},{"id":"5ce9f5bb1810cc4b185c3bfc","content":"套","commented_id":0,"commented_name":"","respond_content":"","time":1558836667,"author_id":9,"article_id":"5ce79be01a162670b2ce4ab7","title":"爱太\u201c沉重\u201d！武汉小伙520\u201c翻车\u201d：公主抱130斤女友手臂骨折了","image_url":null},{"id":"5ce9f5aa1810cc4b185c3bfb","content":"哈哈","commented_id":0,"commented_name":"","respond_content":"","time":1558836650,"author_id":9,"article_id":"5ce79be01a162670b2ce4ab7","title":"爱太\u201c沉重\u201d！武汉小伙520\u201c翻车\u201d：公主抱130斤女友手臂骨折了","image_url":null},{"id":"5ce9e9811810cc2bb4cf8252","content":"本来","commented_id":1,"commented_name":"zinc","respond_content":"哈咯路","time":1558833537,"author_id":100,"article_id":"5ce51ccd1a162670b2cccdca","title":"嘀咕嘀咕地方给","image_url":null},{"id":"5ce9e9761810cc2bb4cf8250","content":"哈哈哈","commented_id":1,"commented_name":"zinc","respond_content":"哈咯路","time":1558833526,"author_id":100,"article_id":"5ce51ccd1a162670b2cccdca","title":"嘀咕嘀咕地方给","image_url":null},{"id":"5ce9e9711810cc2bb4cf824f","content":"老子","commented_id":1,"commented_name":"zinc","respond_content":"哈咯路","time":1558833521,"author_id":100,"article_id":"5ce51ccd1a162670b2cccdca","title":"嘀咕嘀咕地方给","image_url":null},{"id":"5ce9e9691810cc2bb4cf824e","content":"哈咯路","commented_id":0,"commented_name":"","respond_content":"","time":1558833513,"author_id":100,"article_id":"5ce51ccd1a162670b2cccdca","title":"嘀咕嘀咕地方给","image_url":null},{"id":"5ce9e9631810cc2bb4cf824d","content":"哈喽","commented_id":100,"commented_name":"一点都不圆润，盘它.","respond_content":"发士大夫士大夫","time":1558833507,"author_id":100,"article_id":"5ce51ccd1a162670b2cccdca","title":"嘀咕嘀咕地方给","image_url":null},{"id":"5ce7cb321810cc00d8ac3f0b","content":"对啊","commented_id":13,"commented_name":"jzja15586703","respond_content":"的撒大","time":1558694706,"author_id":9,"article_id":"5ce78b171a162670b2ce2d33","title":"\u201c下下周\u201d千万不要翻译成\u201cnext next week\u201d，这就闹笑话了！","image_url":null},{"id":"5ce7aa8b1810ccf1648bd616","content":"5","commented_id":0,"commented_name":"","respond_content":"","time":1558686347,"author_id":1,"article_id":"5ce7aa6d1a162670b2ce5eef","title":"别来别来","image_url":null}]
     * total_num : 35
     */

    @SerializedName("total_num")
    private int totalNum;
    @SerializedName("comment_info")
    private List<CommentInfoBean> commentInfo;

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public List<CommentInfoBean> getCommentInfo() {
        return commentInfo;
    }

    public void setCommentInfo(List<CommentInfoBean> commentInfo) {
        this.commentInfo = commentInfo;
    }

    public static class CommentInfoBean {
        /**
         * id : 5ce9faa91810cc4b185c3c0c
         * content : 哈哈
         * commented_id : 2
         * commented_name : 猪猪侠007
         * respond_content : 打算
         * time : 1558837929
         * author_id : 2
         * article_id : 5ce763381a162670b2ce0bed
         * title : 打算
         * image_url : null
         */

        @SerializedName("id")
        private String id;
        @SerializedName("content")
        private String content;
        @SerializedName("commented_id")
        private int commentedId;
        @SerializedName("commented_name")
        private String commentedName;
        @SerializedName("respond_content")
        private String respondContent;
        @SerializedName("time")
        private long time;
        @SerializedName("author_id")
        private int authorId;
        @SerializedName("article_id")
        private String articleId;
        @SerializedName("title")
        private String title;
        @SerializedName("image_url")
        private List<ArticleReviewResp.ImageUrlBean> imageUrl;

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

        public String getRespondContent() {
            return respondContent;
        }

        public void setRespondContent(String respondContent) {
            this.respondContent = respondContent;
        }

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }

        public int getAuthorId() {
            return authorId;
        }

        public void setAuthorId(int authorId) {
            this.authorId = authorId;
        }

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

        public List<ArticleReviewResp.ImageUrlBean> getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(List<ArticleReviewResp.ImageUrlBean> imageUrl) {
            this.imageUrl = imageUrl;
        }
    }
}
