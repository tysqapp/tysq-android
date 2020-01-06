package response.rank;

/**
 * author       : frog
 * time         : 2019-07-16 10:42
 * desc         : 我的积分 信息
 * version      : 1.3.0
 */
public class MyRankResp {

    private String count;

    public MyRankResp(String count) {
        this.count = count;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

}
