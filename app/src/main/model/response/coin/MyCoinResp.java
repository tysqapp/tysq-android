package response.coin;

/**
 * author       : frog
 * time         : 2019-07-18 17:27
 * desc         : 我的金币
 * version      : 1.3.0
 */
public class MyCoinResp {

    private String count;
    private long countNum;

    public MyCoinResp(String count, long countNum) {
        this.count = count;
        this.countNum = countNum;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public long getCountNum() {
        return countNum;
    }

    public void setCountNum(long countNum) {
        this.countNum = countNum;
    }
}
