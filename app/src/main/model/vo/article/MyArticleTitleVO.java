package vo.article;

/**
 * author       : frog
 * time         : 2019-08-28 11:23
 * desc         : 标题数量
 * version      : 1.0.0
 */
public class MyArticleTitleVO {

    private int count;
    private int type;

    public MyArticleTitleVO(int count, int type) {
        this.count = count;
        this.type = type;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
