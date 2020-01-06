package response.common;

/**
 * author       : frog
 * time         : 2019/5/26 下午3:06
 * desc         : 我的评论标题
 * version      : 1.3.0
 */
public class TitleCountVO {

    private int count;

    public TitleCountVO() {
    }

    public TitleCountVO(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void reduceTotal() {
        count -= 1;
        if (count <= 0) {
            count = 0;
        }
    }
}
