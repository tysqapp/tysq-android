package vo.article;

/**
 * author       : frog
 * time         : 2019/5/20 上午11:32
 * desc         : 文章图片类型
 * version      : 1.3.0
 */
public class ArticleImageVO {

    private int id;
    private String url;
    private String originalUrl;

    private int width;
    private int height;

    // 下标
    private int pos;

    public ArticleImageVO(int id,
                          String url,
                          String originalUrl,
                          int width,
                          int height,
                          int pos) {
        this.id = id;
        this.url = url;
        this.originalUrl = originalUrl;
        this.width = width;
        this.height = height;
        this.pos = pos;
    }

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

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
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

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }
}
