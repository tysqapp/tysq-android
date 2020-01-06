package eventbus;

/**
 * author       : frog
 * time         : 2019/4/30 下午5:22
 * desc         : 二级分类变换
 * version      : 1.3.0
 */
public class SubCategoryChangeEvent {

    private int topId;
    private int subId;

    public SubCategoryChangeEvent(int topId, int subId) {
        this.topId = topId;
        this.subId = subId;
    }

    public int getTopId() {
        return topId;
    }

    public int getSubId() {
        return subId;
    }

    @Override
    public String toString() {
        return "SubCategoryChangeEvent{" +
                "topId=" + topId +
                ", subId=" + subId +
                '}';
    }
}
