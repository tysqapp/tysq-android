package vo.article;

/**
 * author       : frog
 * time         : 2019/5/20 上午9:31
 * desc         : 文章详情 的 列表VO
 * version      : 1.3.0
 */
public class ArticleDetailVO<T> {

    private int type;
    private T data;

    public ArticleDetailVO(int type, T data) {
        this.type = type;
        this.data = data;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
