package eventbus;

/**
 * author       : frog
 * time         : 2019/4/29 上午11:29
 * desc         : 分类选择变换事件
 * version      : 1.3.0
 */
public class CategorySelectChangeEvent {

    private int topId;
    private int subId;

    public CategorySelectChangeEvent(int topId, int subId) {
        this.topId = topId;
        this.subId = subId;
    }

    public int getTopId() {
        return topId;
    }

    public int getSubId() {
        return subId;
    }
}
