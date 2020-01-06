package eventbus;
/**
 * author       : liaozhenlin
 * time         : 2019/10/22 17:53
 * desc         : 隐藏文章事件
 * version      : 1.5.0
 */
public class HideArticleEvent {

    private String accountId;
    private int statu;

    public HideArticleEvent(String accountId, int statu) {
        this.accountId = accountId;
        this.statu = statu;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public int getStatu() {
        return statu;
    }

    public void setStatu(int statu) {
        this.statu = statu;
    }
}
