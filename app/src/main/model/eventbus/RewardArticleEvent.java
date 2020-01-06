package eventbus;
/**
 * author       : liaozhenlin
 * time         : 2019/11/12 17:27
 * desc         : 打赏文章
 * version      : 1.5.0
 */
public class RewardArticleEvent {
    private String headUrl;
    private int coin;
    private int userId;

    public RewardArticleEvent(String headUrl, int coin, int userId) {
        this.headUrl = headUrl;
        this.coin = coin;
        this.userId = userId;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public int getCoin() {
        return coin;
    }

    public void setCoin(int coin) {
        this.coin = coin;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
