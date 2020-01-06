package eventbus;
/**
 * author       : liaozhenlin
 * time         : 2019/10/21 15:07
 * desc         : 粉丝取消/添加关注
 * version      : 1.5.0
 */
public class FansAttentionEvent {

    private int accountId;
    private boolean isFollow;

    public FansAttentionEvent(int accountId, boolean isFollow) {
        this.accountId = accountId;
        this.isFollow = isFollow;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public boolean isFollow() {
        return isFollow;
    }

    public void setFollow(boolean follow) {
        isFollow = follow;
    }
}
